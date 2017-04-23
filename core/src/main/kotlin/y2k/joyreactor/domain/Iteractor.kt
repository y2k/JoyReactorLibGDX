package y2k.joyreactor.domain

import cc.joyreactor.core.Comment
import cc.joyreactor.core.Thread
import cc.joyreactor.core.ImageRef
import cc.joyreactor.core.Message
import cc.joyreactor.core.Post
import java8.util.concurrent.CompletableFuture
import org.jsoup.nodes.Document
import y2k.joyreactor.common.Environment
import y2k.joyreactor.common.doAsync
import y2k.joyreactor.common.partial
import java.util.*

/**
* Created by y2k on 28/11/2016.
**/
object Iteractor {

    fun getMessages(): CompletableFuture<List<Message>> = doAsync {
        listOf(
            Message(
                true,
                "Игра выполнена в жанре гонки. Во время соревнований персонажи используют специальные воздушные доски «Extreme Gear», которые способны к полёту.",
                Date()),
            Message(
                false,
                "Sonic Riders совместно разрабатывалась студиями Sonic Team, Now Production[en] и United Game Artists",
                Date()),
            Message(
                true,
                "Гамаюн — мифическая райская птица в русской культуре. В произведениях книжности XVII—XIX веков это залетающая из рая безногая и бескрылая вечнолетающая при помощи хвоста птица",
                Date()))
    }

    fun getThreads(): CompletableFuture<List<Thread>> = doAsync {
        listOf(
            Thread(
                "M_10th",
                "Игра выполнена в жанре гонки. Во время соревнований персонажи используют специальные воздушные доски «Extreme Gear», которые способны к полёту.",
                ImageRef(1f, "http://img1.joyreactor.cc/pics/avatar/user/96331"),
                Date()),
            Thread(
                "Songe",
                "Sonic Riders совместно разрабатывалась студиями Sonic Team, Now Production[en] и United Game Artists",
                ImageRef(1f, "http://img1.joyreactor.cc/pics/avatar/user/45181"),
                Date()),
            Thread(
                "Блинский",
                "Гамаюн — мифическая райская птица в русской культуре. В произведениях книжности XVII—XIX веков это залетающая из рая безногая и бескрылая вечнолетающая при помощи хвоста птица",
                ImageRef(1f, "http://img0.joyreactor.cc/pics/avatar/user/269534"),
                Date()))
    }

    fun sendUserMessage(text: String) {
        TODO("not implemented")
    }

    fun listenerUsername(env: Environment): (String) -> Unit = env.writeField.partial("username")
    fun listenerPassword(env: Environment): (String) -> Unit = env.writeField.partial("password")

    fun login(env: Environment) = doAsync {
        env.downloadDocument("login")
            .findCsrfToken()
            .let { token ->
                mapOf(
                    "signin[username]" to env.readField("username")!!,
                    "signin[password]" to env.readField("password")!!,
                    "signin[_csrf_token]" to token)
            }
            .let { form -> env.postForm("login", form) }
    }

    private fun Document.findCsrfToken(): String {
        return getElementById("signin__csrf_token").attr("value")
    }
}

fun isUserAuthorized(): Boolean {
    return false // TODO:
}

fun syncNextPostPage(env: Environment, tagId: String) = doAsync {
    env.downloadAndSavePosts(tagId)
}

fun getPosts(env: Environment, tagId: String) = doAsync {
    val posts = env.getPostsFromRepository(tagId)
    if (posts.isNotEmpty()) posts
    else {
        env.downloadAndSavePosts(tagId)
        env.getPostsFromRepository(tagId)
    }
}

private fun Environment.downloadAndSavePosts(tagId: String) {
    makeTagsPath(tagId, getNextPageNumber(tagId))
        .let { downloadDocument(it) }
        .let { doc ->
            parseNewPageNumber(doc)
                .let { saveNextPageNumber(tagId, it) }
            parsePostsForTag(doc)
        }
        .let { saveNewPosts(tagId, it) }
}

private fun Environment.getNextPageNumber(tagId: String): Int? {
    return readField("page-$tagId")?.toInt()
}

private fun Environment.saveNextPageNumber(tagId: String, page: Int) {
    writeField("page-$tagId", "" + page)
}

private fun makeTagsPath(tagId: String, page: Int?): String {
    val url = if (tagId == "") "" else "tag/$tagId"
    return url + "/" + (page ?: 0)
}

private fun Environment.saveNewPosts(tagId: String, newPosts: List<Post>) {
    getPostsFromRepository(tagId)
        .let { mergeOldWithNewPosts(it, newPosts) }
        .let { savePostsToRepository(tagId, it) }
}

private fun mergeOldWithNewPosts(old: List<Post>, new: List<Post>): List<Post> {
    val oldIds = old.map(Post::id)
    val uniqueNew = new.filterNot { oldIds.contains(it.id) }
    return ArrayList(old).apply { addAll(uniqueNew) }
}

fun getDetailedPost(env: Environment, postId: Long) = doAsync {
    val post = env.getPostFromWeb(postId)
    post.copy(
        comments = post.comments.filterTop(),
        attachments = post.attachments.take(3))
}

private fun List<Comment>.filterTop(): List<Comment> {
    return filter { it.parentId == 0L && it.rating >= 0 }
        .sortedByDescending { it.rating }
        .take(10)
}

fun getImagesForPost(env: Environment, postId: Long) = doAsync {
    env.getPostFromWeb(postId)
        .attachments
        .map { it.image }
}

fun getCommentsForParent(env: Environment, postId: Long, parentId: Long) = doAsync {
    env.getPostFromWeb(postId)
        .comments
        .filter { it.parentId == parentId }
}

private fun Environment.getPostFromWeb(postId: Long): Post {
    return downloadDocument("post/$postId").let(::parsePost)
}