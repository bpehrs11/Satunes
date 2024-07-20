/*
 * This file is part of Satunes.
 *
 *  Satunes is free software: you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software Foundation,
 *  either version 3 of the License, or (at your option) any later version.
 *
 *  Satunes is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with Satunes.
 *  If not, see <https://www.gnu.org/licenses/>.
 *
 *  **** INFORMATIONS ABOUT THE AUTHOR *****
 *  The author of this file is Antoine Pirlot, the owner of this project.
 *  You find this original project on github.
 *
 *  My github link is: https://github.com/antoinepirlot
 *  This current project's link is: https://github.com/antoinepirlot/Satunes
 *
 *  You can contact me via my email: pirlot.antoine@outlook.com
 *  PS: I don't answer quickly.
 */

package io.github.antoinepirlot.satunes.ui.views.playback.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import io.github.antoinepirlot.satunes.R
import io.github.antoinepirlot.satunes.database.models.MediaImpl
import io.github.antoinepirlot.satunes.database.models.Music
import io.github.antoinepirlot.satunes.router.utils.openMedia
import io.github.antoinepirlot.satunes.ui.components.cards.media.MediaCardCollection
import io.github.antoinepirlot.satunes.ui.components.texts.Title
import io.github.antoinepirlot.satunes.ui.viewmodels.PlaybackViewModel

/**
 * @author Antoine Pirlot on 23/06/2024
 */

@Composable
internal fun PlaybackQueueView(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    playbackViewModel: PlaybackViewModel = viewModel(),
) {
    val playbackPlaylist: List<Music> = playbackViewModel.getPlaylist()

    MediaCardCollection(
        modifier = modifier,
        navController = navController,
        scrollToMusicPlaying = true,
        header = { Title(text = stringResource(id = R.string.playback_queue)) },
        mediaImplCollection = playbackPlaylist,
        openMedia = { mediaImpl: MediaImpl ->
            openMedia(
                playbackViewModel = playbackViewModel,
                mediaImpl,
                navigate = false,
                navController = null
            )
        }
    )
}

@Preview
@Composable
private fun PlaybackQueueViewPreview() {
    val navController: NavHostController = rememberNavController()
    PlaybackQueueView(navController = navController)
}