package com.jeksonsoftsolutions.cryptos.ui.screens.profile.edit_profile

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeksonsoftsolutions.cryptos.R
import com.jeksonsoftsolutions.cryptos.core.other.TAG
import com.jeksonsoftsolutions.cryptos.domain.models.UserProfile
import com.jeksonsoftsolutions.cryptos.domain.usecases.CheckConnectionUseCase
import com.jeksonsoftsolutions.cryptos.domain.usecases.profile.ProfileUseCases
import com.jeksonsoftsolutions.cryptos.ui.components.LoadResult
import com.jeksonsoftsolutions.cryptos.ui.components.NoInternetException
import com.jeksonsoftsolutions.cryptos.ui.screens.Events
import com.jeksonsoftsolutions.cryptos.ui.screens.utils.handleNetConnectionError
import com.jeksonsoftsolutions.cryptos.utils.SavingFileUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val profileUseCases: ProfileUseCases,
    private val checkConnectionUseCase: CheckConnectionUseCase,
    @ApplicationContext val context: Context,
) : ViewModel() {

    private val _state = MutableStateFlow<LoadResult<EditProfileState>>(LoadResult.Loading)
    val state: StateFlow<LoadResult<EditProfileState>> = _state.asStateFlow()

    private val _event = MutableStateFlow<Events>(Events.None)
    val event = _event.asStateFlow()

    private val _isImageLoading = MutableStateFlow(false)
    val isImageLoading = _isImageLoading.asStateFlow()

    init {
        loadUserProfile()
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            _state.update { LoadResult.Loading }
            val userProfile = profileUseCases.getUserProfile().first()
            _state.update {
                LoadResult.Success(
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

    fun onAvatarUriChange(uri: String? = null) {
        val avatarUri = if (uri.isNullOrEmpty()) {
            "https://i.pravatar.cc/300?u=${UUID.randomUUID()}"
        } else {
            uri
        }
        _event.handleNetConnectionError(context) {
            checkConnectionUseCase.isConnectedToNetwork()
        }
        updateState { it.copy(avatarUri = avatarUri) }
    }

    fun processAndSaveImage(context: Context, uri: Uri) {
        viewModelScope.launch {
            _isImageLoading.update { true }
            try {
                val savedUri = SavingFileUtil.saveImageFromUri(context, uri)
                Log.d(TAG, "EditProfileViewModel processAndSaveImage: uri = $savedUri")
                savedUri ?: run {
                    onAvatarUriChange(null)
                    return@launch
                }
                // TODO remove after check if it doesn't need
//              ImageUtil.saveProfileImageUriString(context, uriString)
                onAvatarUriChange(savedUri.toString())
            } catch (e: Exception) {
                Log.e(TAG, "EditProfileViewModel processAndSaveImage error", e)
                onAvatarUriChange(null)
            }
            _isImageLoading.update { false }
        }
    }

    fun saveProfile(onSuccess: () -> Unit) {
        val currentState = (state.value as? LoadResult.Success)?.data ?: return

        viewModelScope.launch {
            _state.update { LoadResult.Loading }
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
                _event.update {
                    Events.MessageForUser(
                        messageTitle = title,
                        messageDescription = description
                    )
                }
            }
        }
    }

    fun clearEvent() {
        _event.update { Events.None }
    }

    private fun updateState(update: (EditProfileState) -> EditProfileState) {
        val current = (_state.value as? LoadResult.Success)?.data ?: return
        _state.update { LoadResult.Success(update(current)) }
    }
}

data class EditProfileState(
    val username: String = "",
    val email: String = "",
    val bio: String = "",
    val avatarUri: String? = null,
    val originalProfile: UserProfile? = null,
)