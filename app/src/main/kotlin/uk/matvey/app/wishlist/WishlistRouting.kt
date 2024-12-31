package uk.matvey.app.wishlist

import io.ktor.server.html.respondHtml
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import kotlinx.html.body
import uk.matvey.app.wishlist.WishlistHtml.wishlist

fun Route.wishlistRouting() {
    route("/wishlist") {
        get {
            call.respondHtml { body { wishlist() } }
        }
    }
}