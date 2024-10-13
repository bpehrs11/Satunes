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

package io.github.antoinepirlot.satunes.ui.components.settings.playback

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.antoinepirlot.jetpack_libs.components.texts.NormalText
import io.github.antoinepirlot.satunes.R
import io.github.antoinepirlot.satunes.data.local.LocalMainScope
import io.github.antoinepirlot.satunes.data.local.LocalSnackBarHostState
import io.github.antoinepirlot.satunes.data.viewmodels.PlaybackViewModel
import io.github.antoinepirlot.satunes.ui.components.forms.OutlinedNumberField
import io.github.antoinepirlot.satunes.ui.components.settings.SubSettings
import io.github.antoinepirlot.satunes.ui.components.settings.playback.timer.RemainingTime
import kotlinx.coroutines.CoroutineScope

/**
 * @author Antoine Pirlot on 03/10/2024
 */

@Composable
internal fun TimerSubSetting(
    modifier: Modifier = Modifier,
    playbackViewModel: PlaybackViewModel = viewModel(),
) {
    SubSettings(
        modifier = modifier.fillMaxWidth(),
        title = stringResource(R.string.timer_settings_title)
    ) {
        Column(
            modifier = Modifier
                .selectableGroup()
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val scope: CoroutineScope = LocalMainScope.current
            val snackBarHostState: SnackbarHostState = LocalSnackBarHostState.current

            val minutesTextField: MutableIntState = rememberSaveable { mutableIntStateOf(0) }

            Row {
                OutlinedNumberField(
                    modifier = Modifier.fillMaxWidth(fraction = 0.2f),
                    value = minutesTextField,
                    label = stringResource(R.string.minutes_text_field_label),
                    maxValue = 480
                ) //max 8 hours
            }
            RemainingTime()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        playbackViewModel.cancelTimer(
                            scope = scope,
                            snackBarHostState = snackBarHostState,
                        )
                    }
                ) {
                    NormalText(text = stringResource(R.string.cancel))
                }

                Spacer(modifier = Modifier.size(5.dp))

                Button(
                    onClick = {
                        playbackViewModel.setTimer(
                            scope = scope,
                            snackBarHostState = snackBarHostState,
                            delay = minutesTextField.intValue
                        )
                    }
                ) {
                    NormalText(text = stringResource(R.string.start_timer_button_content))
                }
            }
        }
    }
}

@Preview
@Composable
private fun TimerSubSettingPreview() {
    TimerSubSetting()
}