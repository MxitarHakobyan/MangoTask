package com.mango.task.ui.screens.profile.components

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.mango.task.BuildConfig.BASE_IMAGE_URL
import com.mango.task.R
import com.mango.task.ui.screens.profile.ProfileState
import com.mango.task.ui.utils.convertImageToBase64
import kotlinx.coroutines.launch

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun ProfileHeader(
    modifier: Modifier = Modifier,
    scrollState: ScrollState,
    state: ProfileState,
    onAvatarSelected: (String?) -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var tempImageUri by remember { mutableStateOf<Uri?>(null) }

    if (!state.isEditing) tempImageUri = null

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            tempImageUri = uri
            coroutineScope.launch {
                val base64Image = convertImageToBase64(context, uri)
                onAvatarSelected(base64Image)
            }
        }
    }

    val scale = 1f - (scrollState.value / 300f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        val painter = when {
            state.isEditing && tempImageUri != null -> rememberAsyncImagePainter(model = tempImageUri)
            state.isEditing && state.miniAvatarUrl.isNotEmpty() -> rememberAsyncImagePainter(model = "$BASE_IMAGE_URL${state.miniAvatarUrl}")
            state.miniAvatarUrl.isEmpty() -> painterResource(R.drawable.ic_default_avatar)
            else -> rememberAsyncImagePainter(model = "$BASE_IMAGE_URL${state.miniAvatarUrl}")
        }

        Image(
            modifier = Modifier
                .size(120.dp)
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                )
                .offset(y = (scrollState.value / 3).dp)
                .clip(CircleShape)
                .clickable(state.isEditing) {
                    if (state.isEditing) galleryLauncher.launch("image/*")
                },
            contentScale = ContentScale.Crop,
            painter = painter,
            contentDescription = stringResource(R.string.user_avatar_text),
        )
    }
}