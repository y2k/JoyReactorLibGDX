package y2k.joyreactor.views

import com.badlogic.gdx.math.Vector2
import im.y2k.libgdxdsl.*
import y2k.joyreactor.common.Environment
import y2k.joyreactor.domain.isUserAuthorized
import y2k.joyreactor.domain.requestProfile

/**
* Created by y2k on 29/11/2016.
**/
fun GroupEx.ProfilePage() {
    setView {
        stackV {
            toolbar("Profile")

            if (isUserAuthorized()) {
                profileDetails()
                layout(height = LayoutParam.fill)

                logoutButton(this@ProfilePage)
                layout(width = LayoutParam.fill)
            } else {
                space { layout(height = LayoutParam.fill) }
                button {
                    text = "LOGIN"
                    click { LoginPage(this@ProfilePage) }
                }
                space { layout(height = LayoutParam.fill) }
            }

            tabs(this@ProfilePage)
        }
    }
}

private fun GroupEx.profileDetails() {
    stackV {
        requestProfile(Environment).thenAccept { profile ->
            stackH {
                space { layout(width = LayoutParam.fill) }
                image {
                    size = Vector2(100f, 100f)
                    src = profile.image
                }
                space { layout(width = LayoutParam.fill) }
            }

            record("Name", profile.name)
            record("Rating", "" + profile.rating)
            record("Stars", "" + profile.stars)
            record("Progress", "${(100 * profile.progress).toInt()}%")
        }
    }
}

private fun GroupEx.record(title: String, value: String) {
    stackH {
        label { text = "$title:" }
        layout(width = LayoutParam.fill)
        label { text = value }
        layout(width = LayoutParam.fill)
    }
    layout(width = LayoutParam.fill)
}

private fun GroupEx.logoutButton(parent: GroupEx) {
    button {
        text = "LOGOUT"
        click {
            LoginPage(parent) // TODO:
        }
    }
}