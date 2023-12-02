package ru.netology.posts

import ru.netology.posts.attachments.*
import ru.netology.posts.exceptions.NoSuchUserException
import ru.netology.posts.exceptions.PostNotFoundException


object WallService {
    private var currentEntityId : Int = 0

    private var posts = emptyArray<Post>()
    private var comments = emptyArray<Comment>()
    private var reports = emptyArray<ReportComment>()

    private fun getNextEntityId() : Int {
        return ++ currentEntityId
    }
    //---------------------------------------------------------------------

    fun add(post: Post): Post {
        val newPost : Post = post.copy(id = getNextEntityId())

        posts += newPost

        return posts.last()
    }
    //---------------------------------------------------------------------

    fun update(post: Post): Boolean {
        var result : Boolean = false
        for ((index, postToUpdate) in posts.withIndex()) {
            if (postToUpdate.id == post.id) {
                posts[index] = post
                result = true
            }
        }
        return result
    }
    //---------------------------------------------------------------------

    fun createComment(postId: Int, comment: Comment): Comment {
        var newComment : Comment? = null

        for ((index, post) in posts.withIndex()) {
            if (post.id == postId) {
                newComment = comment.copy(id = getNextEntityId(), replyToUser = post.ownerId, replyToComment = post.id)
                comments += newComment

                val newComments : Comments = (posts[index].comments ?: Comments());
                posts[index] = posts[index].copy(comments = newComments.copy(count = newComments.count + 1))
            }
        }

        if (newComment == null) {
            throw PostNotFoundException("No posts with id = $postId")
        }

        return newComment!!
    }
    //---------------------------------------------------------------------

    fun reportComment(ownerId : Int, commentId : Int, reason : CommentReportReason) : ReportComment? {
        var report : ReportComment? = null
        var userID : Int = 0;

        for (post in posts) {
            if (post.id == commentId) {
                userID = post.fromId
                break
            }
        } // for post in posts
        if (userID == 0) {
            for (comment in comments) {
                if (comment.id == commentId) {
                    userID = comment.fromId
                    break
                }
            } // for comment in comments
        } // if userID == 0

        if (userID == 0) {
            throw PostNotFoundException("No posts with id = $commentId")
        }
        if (userID != ownerId) {
            throw NoSuchUserException("Author of posts with id = $commentId is not user with id = $ownerId")
        }

        report = ReportComment(getNextEntityId(), ownerId, commentId, reason)
        reports += report!!

        return report
    }
    //---------------------------------------------------------------------

    fun clear() {
        posts = emptyArray()
        currentEntityId = 0
    }
    //---------------------------------------------------------------------

    fun resetCommentOfPostById(id : Int) : Boolean {
        var result : Boolean = false
        for ((index, postToUpdate) in posts.withIndex()) {
            if (postToUpdate.id == id) {
                posts[index] = posts[index].copy(comments = null)
                result = true
            }
        }
        return result
    }
    //---------------------------------------------------------------------

    fun createCommentsOfPostById(id : Int) : Boolean {
        var result : Boolean = false
        for ((index, postToUpdate) in posts.withIndex()) {
            if (postToUpdate.id == id) {
                posts[index] = posts[index].copy(comments = Comments())
                result = true
            }
        }
        return result
    }
    //---------------------------------------------------------------------

    fun incrementCommentsCountOfPostById(id : Int) : Boolean {
        var result : Boolean = false
        for ((index, postToUpdate) in posts.withIndex()) {
            if (postToUpdate.id == id) {
                if (posts[index].comments != null) {
                    posts[index] = posts[index].copy(comments = posts[index].comments!!.copy(count = posts[index].comments!!.count + 1))
                    result = true
                }
            }
        }
        return result
    }
    //---------------------------------------------------------------------

    fun getCommentsCountOfPostById(id : Int) : Int {
        var result : Int = 0
        for ((index, postToUpdate) in posts.withIndex()) {
            if (postToUpdate.id == id) {
                result = if (posts[index].comments == null) 0 else posts[index].comments!!.count
            }
        }
        return result
    }
    //---------------------------------------------------------------------

    fun addAttachmentToPostById(id : Int, attachment : Attachment) : Boolean {
        var result : Boolean = false
        for ((index, postToUpdate) in posts.withIndex()) {
            if (postToUpdate.id == id) {
                var newAttachments : Array<Attachment> = posts[index].attachments ?: emptyArray<Attachment>()
                newAttachments += attachment
                posts[index] = posts[index].copy(attachments = newAttachments)
                result = true
            }
        }
        return result
    }
    //---------------------------------------------------------------------

    fun getAttachmentOfPostById(id : Int) : Array<Attachment>? {
        var result : Array<Attachment>? = null
        for ((index, postToUpdate) in posts.withIndex()) {
            if (postToUpdate.id == id) {
                result = posts[index].attachments
            }
        }
        return result
    }
    //---------------------------------------------------------------------

    final fun getVideoAttachmentData(attachment : Attachment) : VideoAttachmentData? {
        var result : VideoAttachmentData? = null
        if (attachment.type == AttachmentType.Video && (attachment is VideoAttachment)) {
            result = (attachment as VideoAttachment).data
        }
        return result
    }


}