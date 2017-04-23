//package y2k.joyreactor.domain
//
//import org.jsoup.nodes.Document
//import org.jsoup.nodes.Element
//import y2k.joyreactor.common.Environment
//import y2k.joyreactor.common.first
//import java.util.*
//import java.util.regex.Pattern
//
///**
// * Created by y2k on 03/12/2016.
// */
//
//fun parseTags(document: Document): List<Tag> {
//    return document
//        .select("div.sidebarContent > a")
//        .map { body ->
//            Tag(id = body.text(),
//                title = body.text(),
//                image = getTagImageRef(body))
//        }
//}
//
//private val DEFAULT_TAG_ICON = 1279
//private fun getTagImageRef(body: Element): ImageRef {
//    val icon = Environment.getTagIconId(body.text()) ?: DEFAULT_TAG_ICON
//    return ImageRef(1f, "http://img1.joyreactor.cc/pics/avatar/tag/$icon")
//}
//
//fun parsePost(document: Document): Post {
//    return document
//        .first("div.postContainer")
//        .let(::parserSinglePost)
//}
//
//fun parseProfile(document: Document): Profile {
//    return Profile(
//        name = document.first("div.tagname").text(),
//        image = document.first("div.user > img").attr("src").let { ImageRef(1f, it) },
//        rating = document.first("#rating-text > b").text().toFloat(),
//        stars = document.select("div.star-0").size,
//        progress = getProgressToNewStar(document))
//}
//
//private fun getProgressToNewStar(document: Document): Float {
//    return document
//        .first("div.stars div.poll_res_bg_active").attr("style")
//        .let { "width:([\\d]+)%".toRegex().find(it)?.groupValues?.get(1) }
//        .let { it ?: throw Exception("Can't parse progress") }
//        .toFloat() / 100f
//}
//
//fun parseNewPageNumber(document: Document): Int {
//    return document
//        .select("a.next").first()!!
//        .let { document.select("a.next").first() }
//        .attr("href")
//        .let(::findNumber).toInt()
//}
//
//fun parsePostsForTag(document: Document): List<Post> {
//    return document
//        .select("div.postContainer")
//        .map(::parserSinglePost)
//}
//
//private fun parserSinglePost(body: Element): Post {
//    return Post(
//        tags = parseTagsInPost(body),
//        id = findNumber(body.id()),
//        title = body.select("div.post_content > div > h3").first()?.text(),
//        image = queryImage(body),
//        attachments = parseAttachments(body),
//        comments = parseComments(body))
//}
//
//private fun parseTagsInPost(body: Element): List<String> {
//    return body.select(".taglist a").map { it.text() }
//}
//
//fun parseAttachments(document: Element): List<Attachment> {
//    return document
//        .first("div.post_top")
//        .let {
//            parserThumbnails(it)
//                .union(parseYoutubeThumbnails(it))
//                .union(parseVideoThumbnails(it))
//        }
//        .map(::Attachment)
//}
//
//private fun parserThumbnails(element: Element): List<ImageRef> {
//    return element
//        .select("div.post_content img")
//        .filter { it != null && it.hasAttr("width") }
//        .filterNot { it.attr("height").endsWith("%") }
//        .map {
//            ImageRef(
//                it.attr("width").toFloat() / it.attr("height").toFloat(),
//                getThumbnailImageLink(it))
//        }
//}
//
//private fun getThumbnailImageLink(it: Element): String {
//    fun hasFull(img: Element): Boolean = "a" == img.parent().tagName()
//    return if (hasFull(it))
//        it.parent().attr("href").replace("(/full/).+(-\\d+\\.)".toRegex(), "$1$2")
//    else
//        it.attr("src").replace("(/post/).+(-\\d+\\.)".toRegex(), "$1$2")
//}
//
//private fun parseYoutubeThumbnails(element: Element): List<ImageRef> {
//    return element
//        .select("iframe.youtube-player")
//        .map {
//            val m = SRC_PATTERN.matcher(it.attr("src"))
//            if (!m.find()) throw IllegalStateException(it.attr("src"))
//            ImageRef(
//                it.attr("width").toFloat() / it.attr("height").toFloat(),
//                "http://img.youtube.com/vi/" + m.group(1) + "/0.jpg")
//        }
//}
//
//private val SRC_PATTERN = Pattern.compile("/embed/([^?]+)")
//
//private fun parseVideoThumbnails(element: Element): List<ImageRef> {
//    return element
//        .select("video[poster]")
//        .map {
//            ImageRef(
//                it.attr("width").toFloat() / it.attr("height").toFloat(),
//                element.select("span.video_gif_holder > a").first().attr("href").replace("(/post/).+(-)".toRegex(), "$1$2"))
//        }
//}
//
//private fun parseComments(document: Element): List<Comment> {
//    val postId = findNumber(document.id())
//    val comments = ArrayList<Comment>()
//    for (node in document.select("div.comment")) {
//        val parent = node.parent()
//        val parentId = if ("comment_list" == parent.className()) findNumber(parent.id()) else 0
//
//        val comment = Comment(
//            text = node.select("div.txt > div").first().text(),
//            image = ImageRef(1f, node.select("img.avatar").attr("src")),
//            parentId = parentId,
//            rating = node.select("span.comment_rating").text().trim { it <= ' ' }.toFloat(),
//            postId = postId,
//            id = (node.select("span.comment_rating").attr("comment_id")).toLong())
//        comments.add(comment)
//    }
//    return comments
//}
//
//private val NUMBER_REGEX = Regex("\\d+")
//private fun findNumber(value: String): Long {
//    val m = NUMBER_REGEX.find(value) ?: throw Exception("Can't find number in '$value'")
//    return m.value.toLong()
//}
//
//private fun queryImage(element: Element): ImageRef? {
//    return element
//        .select("div.post_content img")
//        .filter { it.hasAttr("width") }
//        .filterNot { it.attr("height").endsWith("%") }
//        .map {
//            val aspect = it.attr("width").toFloat() / it.attr("height").toFloat()
//            it.attr("src")
//                .let(::normalizeUrl)
//                .let { ImageRef(aspect, it) }
//        }
//        .firstOrNull()
//}
//
//private fun normalizeUrl(url: String): String {
//    return url
//        .replace("(/comment/).+(-\\d+\\.[\\w\\d]+)$".toRegex(), "$1$2")
//        .replace("(/full/).+(-\\d+\\.)".toRegex(), "$1$2")
//        .replace("(/post/).+(-\\d+\\.)".toRegex(), "$1$2")
//}
