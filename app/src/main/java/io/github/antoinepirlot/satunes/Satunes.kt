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

package io.github.antoinepirlot.satunes

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import io.github.antoinepirlot.satunes.router.Router
import io.github.antoinepirlot.satunes.ui.components.bars.SatunesBottomAppBar
import io.github.antoinepirlot.satunes.ui.components.bars.SatunesTopAppBar
import io.github.antoinepirlot.satunes.ui.components.dialog.WhatsNewDialog
import io.github.antoinepirlot.satunes.ui.states.SatunesUiState
import io.github.antoinepirlot.satunes.ui.theme.SatunesTheme
import io.github.antoinepirlot.satunes.ui.viewmodels.SatunesViewModel

/**
 * @author Antoine Pirlot on 10/04/2024
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Satunes(
    modifier: Modifier = Modifier,
    satunesViewModel: SatunesViewModel = viewModel()
) {
    satunesViewModel.loadAllData()
    val satunesUiState: SatunesUiState by satunesViewModel.uiState.collectAsState()
    SatunesTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val scrollBehavior =
                TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
            val navController: NavHostController = rememberNavController()
            Scaffold(
                modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    SatunesTopAppBar(
                        scrollBehavior = scrollBehavior,
                        navController = navController
                    )
                },
                bottomBar = { SatunesBottomAppBar(navController = navController) }
            ) { innerPadding ->
                Router(
                    modifier = Modifier.padding(innerPadding),
                    navController = navController
                )

                if (!satunesUiState.whatsNewSeen) {
                    WhatsNewDialog(
                        onConfirm = {
                            // When app relaunch, it's not shown again
                            satunesViewModel.seeWhatsNew(permanently = true)
                        },
                        onDismiss = {
                            // When app relaunch, it's shown again
                            satunesViewModel.seeWhatsNew()
                        }
                    )
                }
            }
        }
    }

}

@Preview
@Composable
private fun ApplicationPreview() {
    Satunes()
}