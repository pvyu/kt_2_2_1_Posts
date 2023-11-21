package ru.netology.posts

import ru.netology.posts.attachments.*


object WallService {
    private var currentPostId : Int = 0
    private var posts = emptyArray<Post>()

    fun add(post: Post): Post {
        currentPostId ++
        val newPost : Post = post.copy(id = currentPostId)

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

    fun getCurrentPostId() : Int {
        return currentPostId
    }
    //---------------------------------------------------------------------

    fun clear() {
        posts = emptyArray()
        currentPostId = 0
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
                //TODO: не ясно, корректно ли происходит работа со ссылками, т.к. новые объекты, по всей видимости, не создаются
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
        //TODO: как вернуть ссылку, объект по которой нельза менять?
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