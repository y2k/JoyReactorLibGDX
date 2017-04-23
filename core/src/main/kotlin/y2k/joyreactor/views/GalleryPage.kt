package y2k.joyreactor.views

import im.y2k.libgdxdsl.*
import y2k.joyreactor.common.Environment
import y2k.joyreactor.common.Size
import y2k.joyreactor.common.asyncGdx
import y2k.joyreactor.common.await
import y2k.joyreactor.domain.getImagesForPost

/**
* Created by y2k on 05/12/2016.
**/

fun GroupEx.GalleryPage(postId: Long) {
    setView {
        stackV {
            toolbar("Gallery")

            imageGrid(postId)
            layout(height = LayoutParam.fill)
        }
    }
}

fun GroupEx.imageGrid(postId: Long) {
    scrollV {
        asyncGdx {
            for (i in await(getImagesForPost(Environment, postId))) {
                image {
                    size = Size(100f, 100f)
                    src = i
                }
                layout(width = LayoutParam.wrap)
            }
        }
    }
}
