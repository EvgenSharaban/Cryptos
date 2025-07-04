package com.jeksonsoftsolutions.cryptos.ui.screens.profile.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeksonsoftsolutions.cryptos.domain.repositories.UserProfileRepository
import com.jeksonsoftsolutions.cryptos.domain.usecases.CheckConnectionUseCase
import com.jeksonsoftsolutions.cryptos.ui.components.LoadResult
import com.jeksonsoftsolutions.cryptos.ui.screens.Events
import com.jeksonsoftsolutions.cryptos.ui.screens.utils.handleNetConnectionError
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: UserProfileRepository,
    private val checkConnectionUseCase: CheckConnectionUseCase,
    @ApplicationContext val context: Context,
) : ViewModel() {

    private val _state = MutableStateFlow<LoadResult<ProfileState>>(LoadResult.Loading)
    val state: StateFlow<LoadResult<ProfileState>> = _state.asStateFlow()

    private val _event = MutableStateFlow<Events>(Events.None)
    val event = _event.asStateFlow()

    fun loadUserProfile() {
        viewModelScope.launch {
            _state.update { LoadResult.Loading }
            _event.handleNetConnectionError(
                context = context,
                checkInternet = { checkConnectionUseCase.isConnectedToNetwork() }
            )
            val userProfile = profileRepository.getUserProfile().first()
            _state.update {
                LoadResult.Success(
                    ProfileState(
                        username = userProfile.username,
                        email = userProfile.email,
                        bio = userProfile.bio,
                        avatarUri = userProfile.avatarUri
                    )
                )
            }
        }
    }

    fun clearEvent() {
        _event.update { Events.None }
    }
}

data class ProfileState(
    val username: String = "",
    val email: String = "",
    val bio: String = "",
    val avatarUri: String? = null,
)

