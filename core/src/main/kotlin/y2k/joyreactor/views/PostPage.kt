package y2k.joyreactor.views

import cc.joyreactor.core.Attachment
import cc.joyreactor.core.Comment
import com.badlogic.gdx.math.Vector2
import im.y2k.libgdxdsl.*
import y2k.joyreactor.common.Environment
import y2k.joyreactor.common.Size
import y2k.joyreactor.common.asyncGdx
import y2k.joyreactor.common.await
import y2k.joyreactor.domain.*

/**
* Created by y2k on 03/12/2016.
**/
fun GroupEx.PostPage(postId: Long) {
    setView {
        stackV {
            toolbar("Post")

            scrollV {
                asyncGdx {
                    val post = await(getDetailedPost(Environment, postId))

                    image {
                        src = post.image
                    }

                    tagList(post.tags)
                    attachmentList(postId, post.attachments)
                    commentList(postId, post.comments)
                }
            }
            layout(height = LayoutParam.fill)
        }
    }
}

private fun GroupEx.tagList(tags: List<String>) {
    wrapPanel {
        for (t in tags)
            button {
                text = t
                click {
                    navigateToPostList(this@wrapPanel, t)
                }
            }
    }
}

private fun GroupEx.attachmentList(postId: Long, items: List<Attachment>) {
    if (items.isEmpty()) return

    header("Images", items.size > 3) {
        navigateToGallery(this, postId)
    }
    stackH {
        for (a in items.take(3))
            image {
                size = Size(150f, 150f)
                src = a.image
            }
    }
}

private fun GroupEx.commentList(postId: Long, comments: List<Comment>) {
    if (comments.isEmpty()) return

    header("Top comments", true) {
        navigateToComments(this, postId)
    }
    stackV {
        for (c in comments)
            stackH {
                image {
                    size = Vector2(60f, 60f)
                    src = c.image
                }
                label {
                    padding = 10
                    text = c.text
                }
                layout(width = LayoutParam.fill)
            }
    }
}

private fun GroupEx.header(title: String, showMore: Boolean, action: () -> Unit) {
    stackH {
        label {
            text = title
            layout(width = LayoutParam.fill)
        }
        if (showMore)
            button {
                text = "More"
                click { action() }
            }
    }
}