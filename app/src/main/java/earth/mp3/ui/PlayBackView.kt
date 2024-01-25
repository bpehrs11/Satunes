package earth.mp3.ui

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import earth.mp3.ui.components.music.MusicControlBar

@Composable
fun PlayBackView(
    modifier: Modifier = Modifier,
    mediaPlayer: MediaPlayer
) {
    Column(
        modifier = modifier
    ) {
        MusicControlBar(mediaPlayer = mediaPlayer)
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
@Preview
fun PlayBackViewPreview() {
    PlayBackView(mediaPlayer = MediaPlayer())
}