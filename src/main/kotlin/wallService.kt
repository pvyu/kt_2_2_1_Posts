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

}