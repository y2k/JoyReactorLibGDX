package y2k.joyreactor.domain

import im.y2k.libgdxdsl.GroupEx
import y2k.joyreactor.views.*

/**
* Created by y2k on 04/12/2016.
**/

fun navigateToPostList(group: GroupEx, tagId: String) {
    group.getRoot().MainPage(tagId)
}

fun navigateToComments(group: GroupEx, postId: Long) {
    group.getRoot().CommentsPage(postId)
}

fun navigateToGallery(group: GroupEx, postId: Long) {
    group.getRoot().GalleryPage(postId)
}

fun navigateToTab(parent: GroupEx, index: Int) {
    val root = parent.getRoot()
    when (index) {
        0 -> root.MainPage()
        1 -> root.TagsPage()
        2 -> root.ThreadsPage()
        3 -> root.ProfilePage()
    }
}

fun navigateToPost(group: GroupEx, postId: Long) {
    group.getRoot().PostPage(postId)
}

private fun GroupEx.getRoot(): GroupEx = parent?.getRoot() ?: this