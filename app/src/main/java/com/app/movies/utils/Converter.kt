package com.app.movies.utils

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.app.movies.network.model.ItunesItem
import com.app.movies.network.model.ItunesItemFeed
import java.util.*


class Converter {
    companion object {
        fun convertToItemList(itemFeed: ItunesItemFeed): List<ItunesItem> {
            val list: MutableList<ItunesItem> = mutableListOf()
            val feedItems = itemFeed.feed.results

            for (feedItem in feedItems) {
                if(feedItem.artworkUrl100 == "")
                    continue

                val itunesItem = ItunesItem(
                    feedItem.artworkUrl100,
                    "",
                    feedItem.genres.get(0).name,
                    feedItem.id.toInt(),
                    feedItem.name,
                    feedItem.artistName
                )
                list.add(itunesItem)
            }

            return list
        }

        fun getRandomDrawableColor(): ColorDrawable? {
            val idx: Int = Random().nextInt(vibrantLightColorList.size-1)
            return vibrantLightColorList[idx]
        }

        private val vibrantLightColorList = arrayOf(
            ColorDrawable(Color.parseColor("#9ACCCD")), ColorDrawable(Color.parseColor("#8FD8A0")),
            ColorDrawable(Color.parseColor("#CBD890")), ColorDrawable(Color.parseColor("#DACC8F")),
            ColorDrawable(Color.parseColor("#D9A790")), ColorDrawable(Color.parseColor("#D18FD9")),
            ColorDrawable(Color.parseColor("#FF6772")), ColorDrawable(Color.parseColor("#DDFB5C"))
        )

    }


}