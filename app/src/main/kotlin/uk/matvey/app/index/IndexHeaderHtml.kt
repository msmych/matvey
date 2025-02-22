package uk.matvey.app.index

import kotlinx.html.HtmlBlockTag
import kotlinx.html.header
import kotlinx.html.img
import kotlinx.html.style
import uk.matvey.app.index.MenuHtml.MenuTab

object IndexHeaderHtml {

    fun HtmlBlockTag.pageHeader(
        activeTab: MenuTab,
    ) = header {
        img {
            width = "100%"
            style = when (activeTab) {
                MenuTab.VTORNIK -> "transform: translateY(-68%);"
                else -> "transform: rotate(180deg);"
            }
            src = when (activeTab) {
                MenuTab.VTORNIK -> "https://live.staticflickr.com/4497/24156735998_97f4600e21_k.jpg"
                else -> "https://live.staticflickr.com/961/41778488722_4612755202_k.jpg"
            }
        }
    }
}