package y2k.joyreactor.views

import im.y2k.libgdxdsl.GroupEx
import im.y2k.libgdxdsl.LayoutParam
import im.y2k.libgdxdsl.button
import im.y2k.libgdxdsl.stackH
import y2k.joyreactor.domain.navigateToTab

/**
* Created by y2k on 29/11/2016.
**/

fun GroupEx.tabs(root: GroupEx) {
    val buttons = listOf("Feed", "Tags", "Inbox", "Profile")
    stackH {
        for ((index, title) in buttons.withIndex()) {
            button {
                text = title
                click { navigateToTab(root, index) }
            }
            layout(width = LayoutParam.fill)
        }
    }
}