package ru.netology.posts.attachments

enum class AttachmentType { Unknown, Photo, Video, Audio, Graffiti, Gift }

sealed class Attachment (val type : AttachmentType = AttachmentType.Unknown)

data class PhotoAttachmentData (
    val id : Int,
    val owner_id : Int,
    val photo_130 : String,
    val photo_604 : String
)
class PhotoAttachment(val data : PhotoAttachmentData) : Attachment(AttachmentType.Photo)

data class VideoAttachmentData (
    val id : Int,
    val owner_id : Int,
    val title : String,
    val duration : Int
)
class VideoAttachment(val data : VideoAttachmentData) : Attachment(AttachmentType.Video)

data class GraffitiAttachmentData (
    val id : Int,
    val owner_id : Int,
    val url : String,
    val width : Int,
    val height : Int
)
class GraffitiAttachment(val data : GraffitiAttachmentData) : Attachment(AttachmentType.Graffiti)

data class GiftAttachmentData (
    val id : Int,
    val thumb_256 : String,
    val thumb_96 : String,
    val thumb_48 : String
)
class GiftAttachment(val data : GiftAttachmentData) : Attachment(AttachmentType.Gift)

data class AudioAttachmentData (
    val id : Int,
    val owner_id : Int,
    val artist : String,
    val title : String,
    val duration : Int,
    val url : String,
    val lyrics_id : Int,
    val album_id : Int,
    val genre_id : Int,
    val date : Long,
    val no_search : Boolean,
    val is_hq : Boolean
)
class AudioAttachment(val data : AudioAttachmentData) : Attachment(AttachmentType.Audio)

