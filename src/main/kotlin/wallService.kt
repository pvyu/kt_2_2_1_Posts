package ru.netology.posts


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
                    //TODO: Очень мне это не нравится, как сделать иначе?
                    val newComment : Comments = Comments(posts[index].comments!!.count +1,
                                                         posts[index].comments!!.canPost,
                                                         posts[index].comments!!.groupsCanPost,
                                                         posts[index].comments!!.canClose,
                                                         posts[index].comments!!.canOpen)
                    posts[index] = posts[index].copy(comments = newComment)
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

}