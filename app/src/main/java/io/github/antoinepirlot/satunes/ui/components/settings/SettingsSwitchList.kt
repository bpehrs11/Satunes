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

package io.github.antoinepirlot.satunes.ui.components.settings

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ListItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.github.antoinepirlot.satunes.database.models.MenuTitle
import io.github.antoinepirlot.satunes.database.services.settings.SettingsManager
import io.github.antoinepirlot.satunes.ui.views.settings.Settings
import kotlinx.coroutines.runBlocking

/**
 *   @author Antoine Pirlot 06/03/2024
 */

@Composable
fun SettingsSwitchList(
    modifier: Modifier = Modifier,
    checkedMap: Map<Settings, MutableState<Boolean>>,
    onCheckedChangeMap: Map<Settings, () -> Unit>? = null,
) {
    val context: Context = LocalContext.current
    Column(
        modifier = modifier
    ) {
        for (setting: Settings in checkedMap.keys.toList()) {
            ListItem(
                headlineContent = {
                    SettingWithSwitch(
                        text = stringResource(id = setting.stringId),
                        checked = checkedMap[setting]!!.value,
                        onCheckedChange = {
                            if (onCheckedChangeMap == null) {
                                runIfIsSwitchMenuTitles(
                                        context = context,
                                        setting = setting
                                )
                            } else {
                                onCheckedChangeMap[setting]!!()
                            }
                        }
                    )
                }
            )
        }
    }
}

@Composable
@Preview
fun SettingsSwitchListPreview() {
    SettingsSwitchList(checkedMap = mapOf(), onCheckedChangeMap = mapOf())
}

private fun runIfIsSwitchMenuTitles(context: Context, setting: Settings): Boolean {
    when (setting) {
        Settings.FOLDERS_CHECKED -> {
            runBlocking {
                SettingsManager.switchMenuTitle(
                    context = context,
                    menuTitle = MenuTitle.FOLDERS
                )
            }
        }

        Settings.ARTISTS_CHECKED -> {
            runBlocking {
                SettingsManager.switchMenuTitle(
                    context = context,
                    menuTitle = MenuTitle.ARTISTS
                )
            }
        }

        Settings.ALBUMS_CHECKED -> {
            runBlocking {
                SettingsManager.switchMenuTitle(
                    context = context,
                    menuTitle = MenuTitle.ALBUMS
                )
            }
        }

        Settings.GENRES_CHECKED -> {
            runBlocking {
                SettingsManager.switchMenuTitle(
                    context = context,
                    menuTitle = MenuTitle.GENRES
                )
            }
        }

        Settings.PLAYLISTS_CHECKED -> {
            runBlocking {
                SettingsManager.switchMenuTitle(
                    context = context,
                    menuTitle = MenuTitle.PLAYLISTS
                )
            }
        }

        else -> {
            return false
        }
    }
    return true
}