package y2k.joyreactor.domain

import cc.joyreactor.core.ImageRef
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Texture
import java8.util.concurrent.CompletableFuture
import y2k.joyreactor.common.Environment
import y2k.joyreactor.common.doAsync
import java.io.File
import java.net.URL
import java.net.URLEncoder
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
* Created by y2k on 03/12/2016.
**/

fun ImageRef.makeRemoteCacheUrl(width: Int, height: Int): String {
    if (width > 200) { // FIXME:
        val h = (200 / (width.toFloat() / height)).toInt()
        return URLEncoder
            .encode(url, "UTF-8")
            .let { "$BASE_URL&width=200&height=$h&url=$it" }
    }
    return URLEncoder
        .encode(url, "UTF-8")
        .let { "$BASE_URL&width=$width&height=$height&url=$it" }
}

fun downloadTexture(evn: Environment, url: String?): CompletableFuture<Texture> = doAsync(IMAGE_EXECUTOR) {
    url!! // TODO: delete !!
    val imgFile = File(TEMP_DIR, "${url.hashCode()}.jpg")
    if (!imgFile.exists()) downloadToFile(imgFile, url)
    imgFile
}.thenApply { Texture(FileHandle(it)) }

private fun downloadToFile(imgFile: File, url: String) {
    URL(url)
        .openConnection()
        .apply { addRequestProperty("User-Agent", "Opera/9.80 (X11; Linux i686; Ubuntu/14.10) Presto/2.12.388 Version/12.16") }
        .inputStream
        .use { it.readBytes() }
        .let { imgFile.writeBytes(it) }
}

private const val BASE_URL = "https://rc.y2k.work/cache/fit?quality=30&bgColor=ffffff"
private val TEMP_DIR by lazy {
    val cache = Gdx.files.local(".joyreactor/image-cache")
    cache.mkdirs()
    cache.file()
}
private val IMAGE_EXECUTOR: Executor = Executors.newSingleThreadExecutor()