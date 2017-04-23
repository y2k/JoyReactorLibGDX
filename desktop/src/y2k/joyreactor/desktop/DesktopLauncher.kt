package y2k.joyreactor.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import y2k.joyreactor.App

fun main(arg: Array<String>) {
    LwjglApplication(
        App(),
        LwjglApplicationConfiguration().apply {
            width = 360
            height = 640
            resizable = false
            title = "JoyReactor"
        })
}