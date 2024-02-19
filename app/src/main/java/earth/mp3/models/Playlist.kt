package earth.mp3.models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.media3.common.MediaItem
import earth.mp3.services.PlaybackController
import java.util.SortedMap

class Playlist(
    musicMediaItemSortedMap: SortedMap<Music, MediaItem>,
) {
    private val originalMusicMediaItemMap: SortedMap<Music, MediaItem>
    var musicList: List<Music>
    var mediaItemList: MutableList<MediaItem>
    var isShuffle: MutableState<Boolean> =
        mutableStateOf(PlaybackController.DEFAULT_IS_SHUFFLE)


    init {
        this.musicList = musicMediaItemSortedMap.keys.toList()
        this.mediaItemList = musicMediaItemSortedMap.values.toMutableList()
        this.originalMusicMediaItemMap = musicMediaItemSortedMap.toSortedMap()
    }

    /**
     * Shuffle the playing and if music playing is not null, returns the new index of the music playing
     *
     * @param musicPlaying the music that is playing, null if no music is playing
     *
     * @return the new index of the music playing, null if music playing is null
     */
    fun shuffle(musicPlaying: Music? = null): Int? {
        if (this.isShuffle.value) {
            // Deactivate Shuffle
            this.musicList = this.originalMusicMediaItemMap.keys.toList()
            this.mediaItemList = this.originalMusicMediaItemMap.values.toMutableList()
        } else {
            // Activate Shuffle
            this.musicList = this.musicList.shuffled()
            this.mediaItemList = mutableListOf()
            this.musicList.forEach { music: Music ->
                this.mediaItemList.add(music.mediaItem)
            }
        }
        this.isShuffle.value = !this.isShuffle.value
        if (musicPlaying != null) {
            val musicIndex = this.musicList.indexOf(musicPlaying)
            if (musicIndex < 0) {
                throw IllegalArgumentException("This music is not present in music list")
            }
            return musicIndex
        }
        //No music is playing
        return null
    }

    @Suppress("NAME_SHADOWING")
    fun getMediaItems(fromIndex: Int, toIndex: Int): List<MediaItem> {
        val toIndex = if (toIndex > this.mediaItemList.size) this.mediaItemList.size else toIndex
        val fromIndex = if (fromIndex < 0) 0 else fromIndex
        if (fromIndex >= toIndex) {
            throw IllegalArgumentException("The fromIndex has to be lower than toIndex")
        }

        val toReturn: MutableList<MediaItem> = mutableListOf()
        for (i: Int in fromIndex..<toIndex) {
            toReturn.add(this.mediaItemList[i])
        }
        return toReturn
    }
}