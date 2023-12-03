package ru.netology.posts

import ru.netology.posts.attachments.Attachment

data class Comment(
    val id  : Int = 0,                              // Идентификатор записи
    val fromId  : Int = 0,                          // Идентификатор автора записи
    val date : Long = 0,                            // Время публикации записи в формате unixtime
    val text : String = "",                         // Текст записи
    val replyToUser  : Int = 0,                     // Идентификатор пользователя или сообщества, в ответ которому оставлен текущий комментарий
    val replyToComment  : Int = 0,                  // Идентификатор комментария, в ответ на который оставлен текущий
    val attachments : Array<Attachment>? = null,    // Медиавложения комментария (фотографии, ссылки и т.п.)
    val parentsStack : Array<Int>? = null,          // Массив идентификаторов родительских комментариев
)

enum class CommentReportReason(val Code : Int) { Unknown(-1), Spam(0), ChildPorn(1), Extremism(2),
                                                 Violation(3), Drugs(4), Porn(5),
                                                 Affront(5), Suicide(6)
}
data class ReportComment (
    val id  : Int = 0,                                               // Идентификатор жалобы
    val ownerId  : Int = 0,                                          // Идентификатор пользователя или сообщества, которому принадлежит комментарий
    val commentId  : Int = 0,                                        // Идентификатор комментария
    val reason : CommentReportReason = CommentReportReason.Unknown   // Причина жалобы
)


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