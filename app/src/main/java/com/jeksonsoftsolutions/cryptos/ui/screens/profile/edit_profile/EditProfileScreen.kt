package com.jeksonsoftsolutions.cryptos.ui.screens.profile.edit_profile

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.jeksonsoftsolutions.cryptos.R
import com.jeksonsoftsolutions.cryptos.core.other.TAG
import com.jeksonsoftsolutions.cryptos.ui.components.LoadResultContent
import com.jeksonsoftsolutions.cryptos.ui.scaffold.AppScaffold
import com.jeksonsoftsolutions.cryptos.ui.screens.Events
import com.jeksonsoftsolutions.cryptos.ui.screens.Events.MessageForUser
import com.jeksonsoftsolutions.cryptos.ui.screens.LocalNavController

@Composable
fun EditProfileScreen() {
    val viewModel: EditProfileViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    val event by viewModel.event.collectAsState()
    val isImageLoading by viewModel.isImageLoading.collectAsState()

    val navController = LocalNavController.current
    val context = LocalContext.current

    val showImageSourceDialog = remember { mutableStateOf(false) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.setLoadingImageState()
            Log.d(TAG, "EditProfileViewModel EditProfileScreen savedUri = $it ")
            viewModel.processAndSaveImage(context, it)
        }
    }

    val navBarHeight = remember { mutableStateOf(0.dp) }
    val bottomPadding = measureBottomPadding(navBarHeight.value)

    // TODO end it later
    // Launcher for requesting permission
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            photoPickerLauncher.launch("image/*")
        } else {
            // Show message that permission was not granted
        }
    }

    AppScaffold(
        title = stringResource(R.string.edit_profile_screen),
        showNavigationUp = true,
        onHeightAppNavBarMeasured = { height -> navBarHeight.value = height },
        toolbarAction = {
            IconButton(
                onClick = {
                    viewModel.saveProfile(onSuccess = { navController.navigateUp() })
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(R.string.save_profile)
                )
            }
        }
    ) { paddingValues ->
        LoadResultContent(
            loadResult = state,
            content = { editProfile ->
                EditProfileContent(
                    state = editProfile,
                    onUsernameChange = viewModel::onUsernameChange,
                    onEmailChange = viewModel::onEmailChange,
                    onBioChange = viewModel::onBioChange,
                    bottomPadding = bottomPadding,
                    isImageLoading = isImageLoading,
                    showImageSourceDialog = showImageSourceDialog,
                    context = context,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        )
    }

    if (showImageSourceDialog.value) {
        AlertDialog(
            onDismissRequest = { showImageSourceDialog.value = false },
            title = { Text(stringResource(R.string.select_image_source)) },
            text = {
                Column {
                    TextButton(
                        onClick = {
                            photoPickerLauncher.launch("image/*")
                            showImageSourceDialog.value = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.gallery))
                    }

                    TextButton(
                        onClick = {
                            viewModel.onAvatarUriChange()
                            showImageSourceDialog.value = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.random_from_internet))
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showImageSourceDialog.value = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    when (event) {
        is MessageForUser -> {
            AlertDialog(
                onDismissRequest = { viewModel.clearEvent() },
                title = { Text((event as MessageForUser).messageTitle) },
                text = {
                    if ((event as MessageForUser).messageDescription.isNotEmpty()) {
                        Text((event as MessageForUser).messageDescription)
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.clearEvent()
                        }
                    ) {
                        Text(stringResource(R.string.try_again))
                    }
                },
            )
        }

        Events.None -> {}
    }
}

@Composable
fun EditProfileContent(
    state: EditProfileState,
    onUsernameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onBioChange: (String) -> Unit,
    bottomPadding: Dp,
    isImageLoading: Boolean,
    showImageSourceDialog: MutableState<Boolean>,
    context: Context,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp + bottomPadding
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                .clickable { showImageSourceDialog.value = true },
            contentAlignment = Alignment.Center
        ) {
            if (isImageLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(40.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            } else if (state.avatarUri != null) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(state.avatarUri)
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(R.string.profile_image),
                    error = painterResource(R.drawable.ic_broken_image),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = stringResource(R.string.profile_image),
                    modifier = Modifier.size(60.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Photo,
                    contentDescription = "Change Photo",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.7f))
                        .padding(4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = state.username,
            onValueChange = onUsernameChange,
            label = { Text(stringResource(R.string.user_name)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            trailingIcon = {
                if (state.username.isNotEmpty()) {
                    IconButton(onClick = { onUsernameChange("") }) {
                        Icon(Icons.Default.Clear, "Clear username")
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.email,
            onValueChange = onEmailChange,
            label = { Text(stringResource(R.string.email_address)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            trailingIcon = {
                if (state.email.isNotEmpty()) {
                    IconButton(onClick = { onEmailChange("") }) {
                        Icon(Icons.Default.Clear, "Clear email")
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.bio,
            onValueChange = onBioChange,
            label = { Text(stringResource(R.string.about_me)) },
            modifier = Modifier.fillMaxWidth(),
            minLines = 4,
            maxLines = 40,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            trailingIcon = {
                if (state.bio.isNotEmpty()) {
                    IconButton(onClick = { onBioChange("") }) {
                        Icon(Icons.Default.Clear, "Clear bio")
                    }
                }
            }
        )
    }
}

@Composable
private fun measureBottomPadding(navBarHeight: Dp): Dp {
    val ime = WindowInsets.ime
    val navBars = WindowInsets.navigationBars
    val density = LocalDensity.current

    val imeBottom = with(density) { ime.getBottom(density).toDp() }
    val navBottom = with(density) { navBars.getBottom(density).toDp() }

    val visibleInset = if (imeBottom > 0.dp && imeBottom > navBarHeight) {
        imeBottom - navBarHeight + navBottom
    } else {
        navBottom
    }
    return visibleInset + BOTTOM_SPACING
}

private val BOTTOM_SPACING = 16.dp