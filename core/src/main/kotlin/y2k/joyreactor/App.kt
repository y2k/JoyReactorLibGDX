package y2k.joyreactor

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.StretchViewport
import y2k.joyreactor.common.Window
import y2k.joyreactor.views.MainPage

class App : ApplicationAdapter() {

    private val viewport = StretchViewport(480f, 850f)
    private val batch by lazy { SpriteBatch() }
    private val window by lazy { Window(viewport, batch) }

    override fun create() {
        window.root.MainPage()
    }

    override fun render() = window.draw()
    override fun resize(width: Int, height: Int) = window.resize(width, height)
    override fun dispose() = batch.dispose()
}