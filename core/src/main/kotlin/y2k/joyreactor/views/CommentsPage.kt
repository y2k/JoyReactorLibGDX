package y2k.joyreactor.views

import im.y2k.libgdxdsl.*
import y2k.joyreactor.common.Environment
import y2k.joyreactor.common.asyncGdx
import y2k.joyreactor.common.await
import y2k.joyreactor.domain.getCommentsForParent

/**
* Created by y2k on 05/12/2016.
**/

fun GroupEx.CommentsPage(postId: Long) {
    setView {
        stackV {
            toolbar("Comments")

            commentList(postId)
            layout(height = LayoutParam.fill)
        }
    }
}

private fun GroupEx.commentList(postId: Long) {
    scrollV {
        commentListContent(postId)
    }
}

private fun GroupEx.commentListContent(postId: Long) {
    asyncGdx {
        for (c in await(getCommentsForParent(Environment, postId, 0L))) {
            button {
                text = c.text
                click {
                    // TODO()
                }
            }
        }
    }
}