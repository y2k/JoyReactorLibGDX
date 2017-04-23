package y2k.joyreactor.views

import cc.joyreactor.core.Post
import im.y2k.libgdxdsl.*
import y2k.joyreactor.common.Environment
import y2k.joyreactor.common.asyncGdx
import y2k.joyreactor.common.await
import y2k.joyreactor.domain.getPosts
import y2k.joyreactor.domain.navigateToPost
import y2k.joyreactor.domain.syncNextPostPage

/**
* Created by y2k on 14/10/2016.
**/
fun GroupEx.MainPage(tagId: String = "") {
    setView {
        stackV {
            toolbar("JoyReactor - " + tagId)

            postList(tagId)
            layout(LayoutParam.fill)

            tabs(this)
        }
    }
}

private fun GroupEx.postList(tagId: String) {
    scrollV {
        postListContent(tagId)
    }
}

private fun GroupEx.postListContent(tagId: String) {
    clearChildren()
    asyncGdx {
        for (post in await(getPosts(Environment, tagId)))
            item(post)
        loadMoreButton(tagId)
    }
}

private fun GroupEx.item(post: Post) {
    image { src = post.image }
    button {
        text = post.title ?: "<not title>"
        click { navigateToPost(this@item, post.id) }
    }
}

private fun GroupEx.loadMoreButton(tagId: String) {
    button {
        text = "Load more"
        click {
            asyncGdx {
                await(syncNextPostPage(Environment, tagId))
                postListContent(tagId)
            }
        }
    }
}