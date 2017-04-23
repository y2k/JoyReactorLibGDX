package y2k.joyreactor.common

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.Viewport
import im.y2k.libgdxdsl.GroupEx

/**
 * Created by y2k on 05/11/2016.
 **/
class Window(viewport: Viewport, batch: Batch) {

    val root: GroupEx
        get() = GroupEx(stage.root)

    private val stage = Stage(viewport, batch)

    init {
        stage.setDebugAll(false)
        Gdx.input.inputProcessor = stage
    }

    fun draw() {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        stage.act()
        stage.draw()
    }

    fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }
}