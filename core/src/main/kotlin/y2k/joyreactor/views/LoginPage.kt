package y2k.joyreactor.views

import im.y2k.libgdxdsl.*
import y2k.joyreactor.common.Environment
import y2k.joyreactor.common.asyncGdx
import y2k.joyreactor.common.await
import y2k.joyreactor.domain.Iteractor

/**
* Created by y2k on 03/12/2016.
**/
fun LoginPage(parent: GroupEx) {
    parent.setView {
        stackV {
            toolbar("Login")

            space { layout(height = LayoutParam.fill) }

            edit {
                hint = "Username"
                textChanged = Iteractor.listenerUsername(Environment)
            }
            space { height = 20 }
            edit {
                hint = "Password"
                textChanged = Iteractor.listenerPassword(Environment)
            }
            space { height = 20 }
            loginButton(parent)

            space { layout(height = LayoutParam.fill) }
        }
    }
}

private fun GroupEx.loginButton(parent: GroupEx) {
    button {
        text = "Login"
        click {
            asyncGdx {
                await(Iteractor.login(Environment))
                parent.MainPage()
            }
        }
    }
}