import org.junit.Test
import org.junit.Before
import org.junit.Assert.*
import ru.netology.posts.Post
import ru.netology.posts.WallService

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
}