package ru.netology.posts

import ru.netology.posts.attachments.Attachment

data class Comments (
    val count  : Int = 0,
    val canPost : Boolean = false,
    val groupsCanPost : Boolean = true,
    val canClose : Boolean = true,
    val canOpen : Boolean = true
)

data class Likes (
    val count : Int = 0,
    val userLikes : Boolean = false,
    val canLike : Boolean = true,
    val canPublish : Boolean = true
)

data class Reposts (
    val Count : Int = 0,
    val userReposted : Int = 0
)

data class Copyright (
    val id : Int = 0,
    val link : String = "",
    val name : String = "",
    val type : String = ""
)

data class Post (
    val id : Int = 0,
    val ownerId : Int = 0,
    val fromId : Int = 0,
    val created_by : Int = 0,
    val date : Int = 0,
    val text : String = "",
    val replyOwnerId : Int = 0,
    val replyPostId : Int = 0,
    val friendsOnly : Boolean = false,
    val comments : Comments? = null,
    val copyright : Copyright = Copyright(),
    val likes : Likes = Likes(),
    val reposts : Reposts = Reposts(),
    val postType : String = "post",
    val attachments : Array<Attachment>? = null,
    val signerId : Int = 0,
    val canPin : Boolean = true,
    val canDelete : Boolean = true,
    val canEdit : Boolean = true,
    val isPined : Boolean = false,
    val markedAsAds : Boolean = false,
    val isFavorite : Boolean = false,
    val postponedId : Int = 0
)