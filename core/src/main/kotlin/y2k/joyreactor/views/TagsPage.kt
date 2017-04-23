package y2k.joyreactor.views

import com.badlogic.gdx.math.Vector2
import im.y2k.libgdxdsl.*
import y2k.joyreactor.common.Environment
import y2k.joyreactor.common.Size
import y2k.joyreactor.common.asyncGdx
import y2k.joyreactor.common.await
import y2k.joyreactor.domain.navigateToPostList
import y2k.joyreactor.domain.requestTags

/**
* Created by y2k on 28/11/2016.
**/
fun GroupEx.TagsPage() {
    setView {
        stackV {
            toolbar("My tags")

            tagList()
            layout(LayoutParam.fill)

            tabs(this@TagsPage)
        }
    }
}

private fun GroupEx.tagList() {
    scrollV {
        asyncGdx {
            for (tag in await(requestTags(Environment)))
                item(tag)
        }
    }
}

private fun GroupEx.item(tag: Tag) {
    stackH {
        image {
            size = Vector2(60f, 60f)
            src = tag.image
        }
        button {
            text = tag.title
            click { navigateToPostList(this@stackH, tag.id) }
            layout(width = LayoutParam.fill)
        }
    }
}