package y2k.joyreactor.common

import org.jsoup.nodes.Document

object Environment {

    val postForm: (String, Map<String, String>) -> Unit
        get() = ::postFormAuthorized.partial(
            MemoryRepository::readAll.partial("cookies"),
            MemoryRepository::writeAll.partial("cookies"))

    val downloadDocument: (String) -> Document
        get() = ::getDocumentAuthorized.partial(
            MemoryRepository::readAll.partial("cookies"),
            MemoryRepository::writeAll.partial("cookies"))

    val writeField = MemoryRepository::write
    val readField = MemoryRepository::read
    val getTagIconId = ::getTagImageId
    val getPostsFromRepository = MemoryRepository::getPostsFromRepository
    val savePostsToRepository = MemoryRepository::savePostsToRepository
}