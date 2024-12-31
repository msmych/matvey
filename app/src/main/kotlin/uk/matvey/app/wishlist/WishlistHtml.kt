package uk.matvey.app.wishlist

import kotlinx.html.HtmlBlockTag
import uk.matvey.app.html.CommonHtml.t1

object WishlistHtml {

    fun HtmlBlockTag.wishlist() {
        t1("Wishlist")
    }
}