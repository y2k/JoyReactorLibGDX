package y2k.joyreactor.common

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import java8.util.concurrent.CompletableFuture
import org.jsoup.nodes.Element
import java.util.concurrent.Executor
import kotlin.properties.ObservableProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
* Created by y2k on 06/11/2016.
**/

fun makeFont(name: String? = null): BitmapFont {
    val font = BitmapFont(Gdx.files.internal(name ?: "roboto.fnt"))
    font.region.texture.antialias()
    return font
}

inline fun <T, T2, R> List<Pair<T, T2>>.flatMapPair(f: (T, T2) -> List<R>): List<R> {
    return flatMap { f(it.first, it.second) }
}

fun <T, R> List<T>.join(other: List<R>): List<Pair<T, R>> {
    return flatMap { t -> other.map { t to it } }
}

fun Element.first(cssQuery: String): Element {
    return select(cssQuery).first() ?: throw Exception("Can't find DOM for '$cssQuery'")
}

data class Size(val width: Float = 0f, val height: Float = 0f) {
    val isZero: Boolean
        get() = width == 0f && height == 0f
}

fun colorRgba(value: Long) = Color(value.toInt())

fun createFillTexture(width: Int, height: Int, color: Color): Texture {
    val pixmap = Pixmap(width, height, Pixmap.Format.RGBA8888)
    pixmap.setColor(color)
    pixmap.fillRectangle(0, 0, width, height)
    val texture = Texture(pixmap)
    pixmap.dispose()
    return texture
}

fun Texture.toDrawable(): TextureRegionDrawable {
    return TextureRegionDrawable(TextureRegion(this))
}

fun Texture.antialias() = setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)

fun <T> doAsync(f: () -> T): CompletableFuture<T> {
    return CompletableFuture
        .supplyAsync(f)
        .thenApplyAsync({ it }, { Gdx.app.postRunnable(it) })
}

fun <T> doAsync(executor: Executor, f: () -> T): CompletableFuture<T> {
    return CompletableFuture
        .supplyAsync(f, { executor.execute(it) })
        .thenApplyAsync({ it }, { Gdx.app.postRunnable(it) })
}

inline fun <T> listenable(initialValue: T, crossinline onChange: (newValue: T) -> Unit):
    ReadWriteProperty<Any?, T> = object : ObservableProperty<T>(initialValue) {
    override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) {
        if (newValue != oldValue)
            onChange(newValue)
    }
}

inline fun <T> listenable(crossinline onChange: (newValue: T?) -> Unit):
    ReadWriteProperty<Any?, T?> = object : ObservableProperty<T?>(null) {
    override fun afterChange(property: KProperty<*>, oldValue: T?, newValue: T?) {
        if (newValue != oldValue)
            onChange(newValue)
    }
}
