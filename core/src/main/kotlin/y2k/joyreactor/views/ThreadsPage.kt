package y2k.joyreactor.views

import com.badlogic.gdx.math.Vector2
import im.y2k.libgdxdsl.*
import y2k.joyreactor.common.Size
import y2k.joyreactor.common.asyncGdx
import y2k.joyreactor.common.await
import y2k.joyreactor.domain.Iteractor
import y2k.joyreactor.domain.Thread

/**
* Created by y2k on 14/10/2016.
**/
fun GroupEx.ThreadsPage() {
    setView {
        stackV {
            toolbar("Inbox")

            scrollV {
                asyncGdx {
                    val threads = await(Iteractor.getThreads())
                    for (t in threads)
                        item(t)
                }
            }
            layout(LayoutParam.fill)

            tabs(this@ThreadsPage)
        }
    }
}

private fun GroupEx.item(thread: Thread) {
    stackH {
        image {
            size = Vector2(60f, 60f)
            src = thread.image
        }
        stackV {
            label {
                padding = 5
                text = thread.user
            }
            label {
                maxLines = 1
                padding = 5
                text = thread.message
            }
        }
    }
}