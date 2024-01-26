package earth.mp3.ui.components.music

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import earth.mp3.models.MediaPlayerManager

@Composable
fun NextMusicButton(
    modifier: Modifier = Modifier,
    hasNext: MutableState<Boolean>,
    hasPrevious: MutableState<Boolean>,
) {
    IconButton(onClick = { nextMusic(hasNext, hasPrevious) }) {
        Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = "ArrowForward")
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
@Preview
fun NextMusicButtonPreview() {
    NextMusicButton(hasNext = mutableStateOf(true), hasPrevious = mutableStateOf(true))
}

/**
 * Play the next music
 */
private fun nextMusic(hasNext: MutableState<Boolean>, hasPrevious: MutableState<Boolean>) {
    MediaPlayerManager.next()
    hasNext.value = MediaPlayerManager.hasNext()
    hasPrevious.value = MediaPlayerManager.hasPrevious()
}