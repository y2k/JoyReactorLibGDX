package y2k.joyreactor.common

import com.badlogic.gdx.Gdx
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
* Created by y2k on 03/12/2016.
**/

    private val indexCache = ConcurrentHashMap<String, IntArray>()

fun getTagImageId(name: String): Int? {
    val names = getIndexes("tag.names.dat")

    val index = Arrays.binarySearch(names, name.toLowerCase().hashCode())
    if (index < 0) return null

    return getIndexes("tag.icons.dat")[index]
}

fun getIndexes(name: String): IntArray {
    return indexCache.getOrPut(name) {
        Gdx.files.internal(name)
            .readBytes()
            .let { ByteBuffer.wrap(it) }
            .order(ByteOrder.LITTLE_ENDIAN)
            .asIntBuffer()
            .let {
                val intArray = IntArray(it.remaining())
                it.get(intArray)
                intArray
            }
    }
}