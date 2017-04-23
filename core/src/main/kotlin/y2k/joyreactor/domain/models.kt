//package y2k.joyreactor.domain
//
//import java.util.*
//
///**
// * Created by y2k on 14/10/2016.
// */
//
//class LoginRequest(
//    val username: String,
//    val password: String)
//
//class Message(
//    val isMine: Boolean,
//    val message: String,
//    val date: Date)
//
//class Thread(
//    val user: String,
//    val message: String,
//    val image: ImageRef,
//    val date: Date)
//
//class Profile(
//    val name: String,
//    val image: ImageRef,
//    val rating: Float,
//    val stars: Int,
//    val progress: Float)
//
//data class ImageRef(
//    val aspect: Float,
//    val url: String)
//
//class Tag(
//    val id: String,
//    val title: String,
//    val image: ImageRef)
//
//data class Post(
//    val tags: List<String>,
//    val id: Long,
//    val title: String?,
//    val image: ImageRef?,
//    var attachments: List<Attachment> = emptyList(),
//    val comments: List<Comment> = emptyList())
//
//class Attachment(
//    val image: ImageRef)
//
//class Comment(
//    val postId: Long,
//    val id: Long,
//    val rating: Float,
//    val parentId: Long,
//    val image: ImageRef,
//    val text: String)
