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

package io.github.antoinepirlot.satunes.database.models

import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.media3.common.MediaItem
import java.util.SortedMap

/**
 * @author Antoine Pirlot on 29/03/2024
 */
abstract class MediaImpl(
    id: Long,
    title: String
) : Media, Comparable<MediaImpl> {
    override var id: Long = id
        internal set
    override var title: String = title
        set(title) {
            if (title.isNotBlank()) {
                field = title
            }
        }

    var artwork: Bitmap? = null
        get(): Bitmap? = field?.copy(field!!.config, false)
        set(artwork) {
            field = artwork?.copy(artwork.config, false)
        }

    override val musicMediaItemMap: SortedMap<Music, MediaItem> = sortedMapOf()
        get() = field.toSortedMap()

    override val musicMediaItemMapUpdate: MutableState<Boolean> = mutableStateOf(false)

    override fun compareTo(other: MediaImpl): Int {
        return StringComparator.compare(o1 = this.title, o2 = other.title)
    }
}