package y2k.joyreactor.common

import cc.joyreactor.core.Post
import java.util.concurrent.ConcurrentHashMap

/**
* Created by y2k on 03/12/2016.
**/

object MemoryRepository {

    private val memory = ConcurrentHashMap<String, Map<String, String>>()
    private val postMap = ConcurrentHashMap<String, List<Post>>()

    fun readAll(name: String): Map<String, String> = memory.getOrElse(name) { emptyMap() }

    fun writeAll(name: String, values: Map<String, String>) {
        memory[name] = values
    }

    fun write(name: String, value: String) {
        memory[name] = mapOf("value" to value)
    }

    fun read(name: String): String? {
        return memory.getOrElse(name, { emptyMap() })["value"]
    }

    fun getPostsFromRepository(tagId: String): List<Post> {
        return postMap.getOrElse(tagId, { emptyList() })
    }

    fun savePostsToRepository(tagId: String, posts: List<Post>) {
        postMap[tagId] = posts
    }
}