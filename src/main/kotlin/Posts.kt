package ru.netology.posts

class Comments (
    val count  : Int = 0,
    val canPost : Boolean = false,
    val groupsCanPost : Boolean = true,
    val canClose : Boolean = true,
    val canOpen : Boolean = true
)

class Likes (
    val count : Int = 0,
    val userLikes : Boolean = false,
    val canLike : Boolean = true,
    val canPublish : Boolean = true
)

data class Post (
    val id : Int = 0,
    val ownerId : Int = 0,
    val fromId : Int = 0,
    val date : Int = 0,
    val text : String = "",
    val friendsOnly : Boolean = false,
    val comments : Comments? = null,
    val likes : Likes = Likes()
)