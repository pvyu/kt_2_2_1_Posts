package ru.netology.posts.attachments

enum class AttachmentType { Unknown, Photo, Video }

abstract class Attachment (val type : AttachmentType = AttachmentType.Unknown)

data class PhotoAttachmentData (
    val id : Int,
    val owner_id : Int,
    val photo_130 : String,
    val photo_604 : String
)

data class VideoAttachmentData (
    val id : Int,
    val owner_id : Int,
    val title : String,
    val duration : Int
)

class PhotoAttachment(val data : PhotoAttachmentData) : Attachment(AttachmentType.Photo)

class VideoAttachment(val data : VideoAttachmentData) : Attachment(AttachmentType.Video)