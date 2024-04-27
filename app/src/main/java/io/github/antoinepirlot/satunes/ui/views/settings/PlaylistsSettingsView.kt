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

package io.github.antoinepirlot.satunes.ui.views.settings

import android.content.Context
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.antoinepirlot.satunes.R
import io.github.antoinepirlot.satunes.database.services.DataManager
import io.github.antoinepirlot.satunes.database.services.DatabaseManager

/**
 * @author Antoine Pirlot on 27/04/2024
 */

@Composable
fun PlaylistsSettingsView(
    modifier: Modifier = Modifier,
) {
    val context: Context = LocalContext.current
    Row(
        modifier = modifier,
    ) {
        Button(onClick = {
            DatabaseManager(context = context).exportAll(
                context = context,
                playlistWithMusics = DataManager.playlistWithMusicsMap.values.toTypedArray()
            )
        }) {
            Text(text = stringResource(id = R.string.export_all))
        }
        Spacer(modifier = Modifier.size(16.dp))
        Button(onClick = {
            DatabaseManager(context = context).importPlaylists(context = context)
        }) {
            Text(text = stringResource(id = R.string._import))
        }
    }
}

@Preview
@Composable
fun PlaylistsSettingsViewPreview() {
    PlaylistsSettingsView()
}