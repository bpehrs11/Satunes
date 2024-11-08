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

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import io.github.antoinepirlot.satunes.data.viewmodels.utils.isAudioAllowed
import io.github.antoinepirlot.satunes.database.models.Playlist
import io.github.antoinepirlot.satunes.database.services.database.DatabaseManager
import io.github.antoinepirlot.satunes.database.services.settings.SettingsManager
import io.github.antoinepirlot.satunes.playback.services.WidgetPlaybackManager
import io.github.antoinepirlot.satunes.utils.getNow
import io.github.antoinepirlot.satunes.utils.initSatunes
import io.github.antoinepirlot.satunes.utils.logger.SatunesLogger
import io.github.antoinepirlot.satunes.utils.utils.showToastOnUiThread
import io.github.antoinepirlot.satunes.widgets.ClassicPlaybackWidget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author Antoine Pirlot on 18/01/24
 */

internal class MainActivity : ComponentActivity() {

    companion object {
        internal lateinit var instance: MainActivity
        private const val IMPORT_PLAYLIST_CODE: Int = 1
        private const val EXPORT_ALL_PLAYLISTS_CODE: Int = 2
        private const val EXPORT_PLAYLIST_CODE: Int = 3
        private const val EXPORT_LOGS_CODE: Int = 4
        const val SELECT_FOLDER_TREE_CODE: Int = 5
        const val MIME_JSON: String = "application/json"
        private const val MIME_TEXT: String = "application/text"
        val DEFAULT_URI: Uri =
            Uri.parse(Environment.getExternalStorageDirectory().path + '/' + Environment.DIRECTORY_DOCUMENTS)

        private val createFileIntent: Intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                putExtra(DocumentsContract.EXTRA_INITIAL_URI, DEFAULT_URI)
            }
        }
    }

    private lateinit var _logger: SatunesLogger
    private var _playlistToExport: Playlist? = null

    override fun onStart() {
        super.onStart()
        if (isAudioAllowed(context = applicationContext)) {
            initSatunes(context = applicationContext, satunesViewModel = null)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SatunesLogger.DOCUMENTS_PATH =
            applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)!!.path
        _logger = SatunesLogger.getLogger()
        _logger.info("Satunes started on API: ${Build.VERSION.SDK_INT}")
        instance = this

        ClassicPlaybackWidget.setRefreshWidget(context = baseContext)
        WidgetPlaybackManager.refreshWidgets()

        setContent {
            Satunes()
        }
    }

    fun createFileToExportPlaylists(defaultFileName: String) {
        createFileIntent.putExtra(Intent.EXTRA_TITLE, defaultFileName)
        createFileIntent.type = MIME_JSON
        startActivityForResult(createFileIntent, EXPORT_ALL_PLAYLISTS_CODE)
    }

    fun openFileToImportPlaylists() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = MIME_JSON
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                putExtra(DocumentsContract.EXTRA_INITIAL_URI, DEFAULT_URI)
            }
        }
        startActivityForResult(intent, IMPORT_PLAYLIST_CODE)
    }

    fun exportLogs() {
        createFileIntent.putExtra(Intent.EXTRA_TITLE, "Satunes_logs_${getNow()}.txt")
        createFileIntent.type = MIME_TEXT
        startActivityForResult(createFileIntent, EXPORT_LOGS_CODE)
    }


    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                EXPORT_PLAYLIST_CODE, EXPORT_ALL_PLAYLISTS_CODE, EXPORT_LOGS_CODE -> {
                    data?.data?.also { uri: Uri ->
                        if (uri.path == null) {
                            showToastOnUiThread(
                                context = this,
                                message = this.getString(R.string.no_file_created)
                            )
                        }

                        if (requestCode == EXPORT_LOGS_CODE) {
                            _logger.exportLogs(context = this, uri = uri)
                        } else {
                            CoroutineScope(Dispatchers.IO).launch {
                                if (requestCode == EXPORT_ALL_PLAYLISTS_CODE) {
                                    DatabaseManager.getInstance().exportPlaylists(
                                        context = this@MainActivity.applicationContext,
                                        uri = uri
                                    )
                                } else {
                                    DatabaseManager.getInstance().exportPlaylist(
                                        context = applicationContext,
                                        uri = uri,
                                        playlist = _playlistToExport!!
                                    )
                                    _playlistToExport = null
                                }
                            }
                        }
                    }
                }

                IMPORT_PLAYLIST_CODE -> {
                    data?.data?.also {
                        DatabaseManager.getInstance().importPlaylists(context = this, uri = it)
                    }
                }

                SELECT_FOLDER_TREE_CODE -> {
                    data?.data?.also {
                        CoroutineScope(Dispatchers.IO).launch {
                            SettingsManager.addPath(context = applicationContext, uri = it)
                        }
                    }
                }
            }
        }
    }

    fun createFileToExportPlaylist(
        defaultFileName: String,
        playlist: Playlist
    ) {
        _playlistToExport = playlist
        createFileIntent.putExtra(Intent.EXTRA_TITLE, defaultFileName)
        createFileIntent.type = MIME_JSON
        startActivityForResult(createFileIntent, EXPORT_PLAYLIST_CODE)
    }

    override fun onDestroy() {
        super.onDestroy()
        WidgetPlaybackManager.refreshWidgets()
    }
}