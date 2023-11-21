import org.junit.Test
import org.junit.Before
import org.junit.Assert.*
import ru.netology.posts.Post
import ru.netology.posts.WallService
import ru.netology.posts.attachments.*

class PostTest {
    @Before
    fun clearBeforeTest() {
        WallService.clear()
    }

    @Test
    fun addTest() {
        val post : Post = Post(text = "first post")
        val addedPost = WallService.add(post)
        assertNotEquals(addedPost.id, 0)
    }

    @Test
    fun updateKnownPostTest() {
        val post : Post = Post(text = "first post")
        val addedPost = WallService.add(post)
        val editedPost = addedPost.copy(text = "first post edited")
        val updateResult = WallService.update(editedPost)
        assertEquals(updateResult, true)
    }

    @Test
    fun updateUnknownPostTest() {
        val post : Post = Post(id = -1, text = "first post")
        val updateResult = WallService.update(post)
        assertEquals(updateResult, false)
    }

    @Test
    fun testResetCommentOfPostById() {
        val post : Post = Post(text = "first post")
        val addedPost = WallService.add(post)
        WallService.resetCommentOfPostById(addedPost.id)
        assertEquals(WallService.getCommentsCountOfPostById(addedPost.id), 0)
    }

    @Test
    fun testIncrementCommentsCountOfPostById() {
        val post : Post = Post(text = "first post")
        val addedPost = WallService.add(post)
        WallService.createCommentsOfPostById(addedPost.id)
        WallService.incrementCommentsCountOfPostById(addedPost.id)
        assertEquals(WallService.getCommentsCountOfPostById(addedPost.id), 1)
    }

    @Test
    fun testAddAttachmentToPostById_1() {
        val post : Post = Post(text = "first post")
        val addedPost = WallService.add(post)

        var photoAttachment : PhotoAttachment? = PhotoAttachment(PhotoAttachmentData(1, 1, "", ""))
        WallService.addAttachmentToPostById(addedPost.id, photoAttachment!!)
        photoAttachment = null

        val attachments : Array<Attachment>? = WallService.getAttachmentOfPostById(addedPost.id)

        assertEquals(attachments?.size ?: 0, 1)
        assertEquals(attachments?.get(0)?.type ?: AttachmentType.Unknown, AttachmentType.Photo)
    }

    @Test
    fun testAddAttachmentToPostById_2() {
        val post : Post = Post(text = "first post")
        val addedPost = WallService.add(post)

        val photoAttachment : PhotoAttachment = PhotoAttachment(PhotoAttachmentData(1, 1, "", ""))
        WallService.addAttachmentToPostById(addedPost.id, photoAttachment)
        val videoAttachment : VideoAttachment = VideoAttachment(VideoAttachmentData(1, 1, "", 0))
        WallService.addAttachmentToPostById(addedPost.id, videoAttachment)

        val attachments : Array<Attachment>? = WallService.getAttachmentOfPostById(addedPost.id)

        assertEquals(attachments?.size ?: 0, 2)
    }

    @Test
    fun testGetVideoAttachmentData() {
        val videoData : VideoAttachmentData = VideoAttachmentData(1, 1, "", 0)
        val attachment : Attachment = VideoAttachment(videoData)

        val attachmentData = WallService.getVideoAttachmentData(attachment)

        assertEquals(attachmentData?.id ?: 0, videoData.id)
        assertEquals(attachmentData?.owner_id ?: 0, videoData.owner_id)
        assertEquals(attachmentData?.title ?: "", videoData.title)
        assertEquals(attachmentData?.duration ?: 0, videoData.duration)
    }
}