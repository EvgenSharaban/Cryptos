package com.example.cryptos.ui.screens.profile.edit_profile

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptos.R
import com.example.cryptos.core.other.TAG
import com.example.cryptos.domain.models.UserProfile
import com.example.cryptos.domain.usecases.CheckConnectionUseCase
import com.example.cryptos.domain.usecases.profile.ProfileUseCases
import com.example.cryptos.ui.components.LoadResult
import com.example.cryptos.ui.components.NoInternetException
import com.example.cryptos.ui.screens.Events
import com.example.cryptos.ui.screens.profile.utils.runWithInternetCheckAndErrorHandling
import com.example.cryptos.utils.ImageUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val profileUseCases: ProfileUseCases,
    private val checkConnectionUseCase: CheckConnectionUseCase,
    @ApplicationContext val context: Context,
) : ViewModel() {

    private val _state = MutableStateFlow<LoadResult<EditProfileState>>(LoadResult.Loading)
    val state: StateFlow<LoadResult<EditProfileState>> = _state.asStateFlow()

    private val _event = Channel<Events>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    init {
        loadUserProfile()
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            _state.value = LoadResult.Loading
            _event.runWithInternetCheckAndErrorHandling(
                context = context,
                checkInternet = { checkConnectionUseCase.isConnectedToNetwork() },
            ) {
                val userProfile = profileUseCases.getUserProfile().first()
                _state.value = LoadResult.Success(
                    EditProfileState(
                        username = userProfile.username,
                        email = userProfile.email,
                        bio = userProfile.bio,
                        avatarUri = userProfile.avatarUri,
                        originalProfile = userProfile
                    )
                )
            }
        }
    }

    fun onUsernameChange(username: String) {
        updateState { it.copy(username = username) }
    }

    fun onEmailChange(email: String) {
        updateState { it.copy(email = email) }
    }

    fun onBioChange(bio: String) {
        updateState { it.copy(bio = bio) }
    }

    fun onAvatarUriChange(uri: String?) {
        updateState { it.copy(avatarUri = uri) }
    }

    fun processAndSaveImage(context: Context, uri: Uri, onComplete: (String?) -> Unit) {
        viewModelScope.launch {
            try {
                val savedUri = ImageUtil.saveImageFromUri(context, uri)
                Log.d(TAG, "EditProfileViewModel processAndSaveImage: uri = $savedUri")
                savedUri?.let {
                    val uriString = it.toString()
                    // TODO remove after check if it doesn't need
//                    ImageUtil.saveProfileImageUriString(context, uriString)
                    onComplete(uriString)
                } ?: onComplete(null)
            } catch (e: Exception) {
                Log.d(TAG, "EditProfileViewModel processAndSaveImage error = $e")
                e.printStackTrace()
                onComplete(null)
            }
        }
    }

    fun saveProfile(onSuccess: () -> Unit) {
        val currentState = (state.value as? LoadResult.Success)?.data ?: return

        viewModelScope.launch {
            _state.value = LoadResult.Loading
            try {
                val updatedProfile = UserProfile(
                    username = currentState.username.trim(),
                    email = currentState.email.trim(),
                    bio = currentState.bio.trim(),
                    avatarUri = currentState.avatarUri
                )
                profileUseCases.updateUserProfile(updatedProfile)
                onSuccess()
            } catch (e: Exception) {
                val title = e.message ?: context.getString(R.string.unknown_error)
                val description = if (e.cause is NoInternetException) {
                    context.getString(R.string.connect_to_internet_and_try_again)
                } else {
                    ""
                }
                _event.trySend(
                    Events.MessageForUser(
                        messageTitle = title,
                        messageDescription = description
                    )
                )
            }
        }
    }

    private fun updateState(update: (EditProfileState) -> EditProfileState) {
        val current = (_state.value as? LoadResult.Success)?.data ?: return
        _state.value = LoadResult.Success(update(current))
    }
}

data class EditProfileState(
    val username: String = "",
    val email: String = "",
    val bio: String = "",
    val avatarUri: String? = null,
    val originalProfile: UserProfile? = null,
)