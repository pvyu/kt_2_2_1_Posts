import junit.framework.Assert.assertEquals
import org.junit.Assert
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import ru.netology.notes.Note
import ru.netology.notes.NoteComment
import ru.netology.notes.NoteService
import ru.netology.posts.exceptions.NoSuchUserException
import ru.netology.posts.exceptions.NoUserGivenException
import ru.netology.posts.exceptions.NoteNotFoundException
import ru.netology.vk.common.Common
import ru.netology.vk.common.Privacy


class NoteTest {
    @Before
    fun clearBeforeTest() {
        NoteService.clear()
        Common.clear()
    }
    //--------------------------------------------------------------------

    @Test
    fun testAdd() {
        val ownerId : Int = 1
        NoteService.setCurrentUserId(ownerId)

        val note : Note = NoteService.add("title", "text", Privacy.All, Privacy.All)


        assertNotEquals(note.id, 0)
        assertEquals(note.ownerId, ownerId)
        assertEquals(note.title, "title")
        assertEquals(note.text, "text")
        assertEquals(note.privacy, Privacy.All)
        assertEquals(note.commentPrivacy, Privacy.All)
    }
    //--------------------------------------------------------------------

    @Test(expected = NoUserGivenException::class)
    fun testAdd_NoUserGivenException() {
        val note : Note = NoteService.add("title", "text", Privacy.All, Privacy.All)
    }
    //--------------------------------------------------------------------

    @Test
    fun testCreateComment() {
        val ownerId : Int = 1
        NoteService.setCurrentUserId(ownerId)

        val note : Note = NoteService.add("title", "text", Privacy.All, Privacy.All)
        val comment : NoteComment = NoteService.createComment(note.id, note.ownerId, "message")

        assertNotEquals(comment.id, 0)
        assertEquals(comment.noteId, note.id)
        assertEquals(comment.message, "message")
    }
    //--------------------------------------------------------------------

    @Test(expected = NoUserGivenException::class)
    fun testCreateComment_NoUserGivenException() {
        val comment : NoteComment = NoteService.createComment(1, 1, "message")
    }
    //--------------------------------------------------------------------

    @Test(expected = NoteNotFoundException::class)
    fun testCreateComment_NoteNotFoundException() {
        val ownerId : Int = 1
        NoteService.setCurrentUserId(ownerId)

        val note : Note = NoteService.add("title", "text", Privacy.All, Privacy.All)
        val comment : NoteComment = NoteService.createComment(111, note.ownerId, "message")
    }
    //--------------------------------------------------------------------

    @Test(expected = NoSuchUserException::class)
    fun testCreateComment_NoSuchUserException() {
        val ownerId : Int = 1
        NoteService.setCurrentUserId(ownerId)

        val note : Note = NoteService.add("title", "text", Privacy.All, Privacy.All)
        val comment : NoteComment = NoteService.createComment(note.id, 111, "message")
    }
    //--------------------------------------------------------------------

    @Test
    fun testDelete() {
        val ownerId : Int = 1
        NoteService.setCurrentUserId(ownerId)

        val note : Note = NoteService.add("title", "text", Privacy.All, Privacy.All)

        val noteCountBefore : Int = NoteService.getNotesCount()
        val deletionsCountBefore : Int = NoteService.getDeletionsCount()

        val result : Boolean = NoteService.delete(note.id)

        val noteCountAfter : Int = NoteService.getNotesCount()
        val deletionsCountAfter : Int = NoteService.getDeletionsCount()

        assertEquals(result, true)
        assertEquals(noteCountAfter - noteCountBefore, -1)
        assertEquals(deletionsCountAfter - deletionsCountBefore, 1)
    }
    //--------------------------------------------------------------------

   @Test
    fun testDelete_WithComment() {
        val ownerId : Int = 1
        NoteService.setCurrentUserId(ownerId)

        val note : Note = NoteService.add("title", "text", Privacy.All, Privacy.All)
        NoteService.createComment(note.id, note.ownerId, "message")

        val noteCountBefore : Int = NoteService.getNotesCount()
        val commentCountBefore : Int = NoteService.getNoteCommentsCount()
        val deletionsCountBefore : Int = NoteService.getDeletionsCount()

        val result : Boolean = NoteService.delete(note.id)

        val noteCountAfter : Int = NoteService.getNotesCount()
        val commentCountAfter : Int = NoteService.getNoteCommentsCount()
        val deletionsCountAfter : Int = NoteService.getDeletionsCount()

        assertEquals(result, true)
        assertEquals(noteCountAfter - noteCountBefore, -1)
        assertEquals(commentCountAfter - commentCountBefore, -1)
        assertEquals(deletionsCountAfter - deletionsCountBefore, 2)
    }
    //--------------------------------------------------------------------

    @Test
    fun testDelete_couldNot() {
        val ownerId : Int = 1
        NoteService.setCurrentUserId(ownerId)

        val note : Note = NoteService.add("title", "text", Privacy.All, Privacy.All)

        val noteCountBefore : Int = NoteService.getNotesCount()
        val deletionsCountBefore : Int = NoteService.getDeletionsCount()

        val result : Boolean = NoteService.delete(111)

        val noteCountAfter : Int = NoteService.getNotesCount()
        val deletionsCountAfter : Int = NoteService.getDeletionsCount()

        assertEquals(result, false)
        assertEquals(noteCountAfter, noteCountBefore)
        assertEquals(deletionsCountAfter, deletionsCountBefore)
    }
    //--------------------------------------------------------------------

    @Test
    fun testDeleteComment() {
        val ownerId : Int = 1
        NoteService.setCurrentUserId(ownerId)

        val note : Note = NoteService.add("title", "text", Privacy.All, Privacy.All)
        val comment : NoteComment = NoteService.createComment(note.id, note.ownerId, "message")

        val commentCountBefore : Int = NoteService.getNoteCommentsCount()
        val deletionsCountBefore : Int = NoteService.getDeletionsCount()

        NoteService.deleteComment(comment.id, note.ownerId)

        val commentCountAfter : Int = NoteService.getNoteCommentsCount()
        val deletionsCountAfter : Int = NoteService.getDeletionsCount()

        assertEquals(commentCountAfter - commentCountBefore, -1)
        assertEquals(deletionsCountAfter - deletionsCountBefore, 1)
    }
    //--------------------------------------------------------------------

    @Test
    fun testEdit() {
        val ownerId : Int = 1
        NoteService.setCurrentUserId(ownerId)

        val note : Note = NoteService.add("title", "text", Privacy.All, Privacy.All)
        val editedNote : Note = NoteService.edit(note.id, "new_title", "new_text", Privacy.OnlyUsers, Privacy.OnlyFriends)

        assertEquals(editedNote.id, note.id)
        assertEquals(editedNote.title, "new_title")
        assertEquals(editedNote.text, "new_text")
        assertEquals(editedNote.privacy, Privacy.OnlyUsers)
        assertEquals(editedNote.commentPrivacy, Privacy.OnlyFriends)
    }
    //--------------------------------------------------------------------





}