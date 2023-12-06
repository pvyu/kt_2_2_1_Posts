import junit.framework.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import ru.netology.notes.Note
import ru.netology.notes.NoteComment
import ru.netology.notes.NoteService
import ru.netology.posts.exceptions.CommentNotFoundException
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

    @Test(expected = NoteNotFoundException::class)
    fun testEdit_NoteNotFoundException() {
        val ownerId : Int = 1
        NoteService.setCurrentUserId(ownerId)

        val note : Note = NoteService.add("title", "text", Privacy.All, Privacy.All)
        NoteService.edit(111, "new_title", "new_text", Privacy.OnlyUsers, Privacy.OnlyFriends)
    }
    //--------------------------------------------------------------------

    @Test
    fun testEditComment() {
        val ownerId : Int = 1
        NoteService.setCurrentUserId(ownerId)

        val note : Note = NoteService.add("title", "text", Privacy.All, Privacy.All)
        val comment : NoteComment = NoteService.createComment(note.id, note.ownerId, "message")
        val editedComment : NoteComment = NoteService.editComment(comment.id, note.ownerId,  "new_message")

        assertEquals(comment.id, editedComment.id)
        assertEquals(comment.noteId, editedComment.noteId)
        assertEquals("new_message", editedComment.message)
    }
    //--------------------------------------------------------------------

    @Test(expected = CommentNotFoundException::class)
    fun testEditComment_CommentNotFoundException() {
        val ownerId : Int = 1
        NoteService.setCurrentUserId(ownerId)

        val note : Note = NoteService.add("title", "text", Privacy.All, Privacy.All)
        val comment : NoteComment = NoteService.createComment(note.id, note.ownerId, "message")
        val editedComment : NoteComment = NoteService.editComment(111, note.ownerId,  "new_message")
    }
    //--------------------------------------------------------------------


    @Test(expected = NoSuchUserException::class)
    fun testEditComment_NoSuchUserException() {
        val ownerId : Int = 1
        NoteService.setCurrentUserId(ownerId)

        val note : Note = NoteService.add("title", "text", Privacy.All, Privacy.All)
        val comment : NoteComment = NoteService.createComment(note.id, note.ownerId, "message")
        val editedComment : NoteComment = NoteService.editComment(comment.id, 111,  "new_message")
    }
    //--------------------------------------------------------------------

    @Test
    fun testGet_OneNote() {
        val ownerId: Int = 1
        NoteService.setCurrentUserId(ownerId)

        val note: Note = NoteService.add("title", "text", Privacy.OnlyUsers, Privacy.OnlyFriends)
        val notes: List<Note> = NoteService.get(listOf(note.id), ownerId)

        assertEquals(1, notes.count())
        assertEquals(note.id, notes[0].id)
        assertEquals(note.title, "title")
        assertEquals(note.text, "text")
        assertEquals(note.privacy, Privacy.OnlyUsers)
        assertEquals(note.commentPrivacy, Privacy.OnlyFriends)
    }
    //--------------------------------------------------------------------

    @Test
    fun testGet_NoNotes() {
        val ownerId: Int = 1
        NoteService.setCurrentUserId(ownerId)

        val notes: List<Note> = NoteService.get(listOf(1), ownerId)

        assertEquals(0, notes.count())
    }
    //--------------------------------------------------------------------

    @Test
    fun testGet_WrongNoteId() {
        val ownerId: Int = 1
        NoteService.setCurrentUserId(ownerId)

        val note: Note = NoteService.add("title", "text", Privacy.OnlyUsers, Privacy.All)
        val notes: List<Note> = NoteService.get(listOf(111), ownerId)

        assertEquals(0, notes.count())
    }
    //--------------------------------------------------------------------

    @Test
    fun testGet_WrongOwnerId() {
        val ownerId: Int = 1
        NoteService.setCurrentUserId(ownerId)

        val note: Note = NoteService.add("title", "text", Privacy.OnlyUsers, Privacy.All)
        val notes: List<Note> = NoteService.get(listOf(note.id), 111)

        assertEquals(0, notes.count())
    }
    //--------------------------------------------------------------------

    @Test
    fun testGetById_OneNote() {
        val ownerId: Int = 1
        NoteService.setCurrentUserId(ownerId)

        val note: Note = NoteService.add("title", "text", Privacy.OnlyUsers, Privacy.OnlyFriends)
        val notes: List<Note> = NoteService.getById(note.id, ownerId)

        assertEquals(1, notes.count())
        assertEquals(note.id, notes[0].id)
        assertEquals(note.title, "title")
        assertEquals(note.text, "text")
        assertEquals(note.privacy, Privacy.OnlyUsers)
        assertEquals(note.commentPrivacy, Privacy.OnlyFriends)
    }
    //--------------------------------------------------------------------

    @Test(expected = NoteNotFoundException::class)
    fun testGetById_WrongNoteId() {
        val ownerId: Int = 1
        NoteService.setCurrentUserId(ownerId)

        val note: Note = NoteService.add("title", "text", Privacy.OnlyUsers, Privacy.All)
        val notes: List<Note> = NoteService.getById(111, ownerId)
    }
    //--------------------------------------------------------------------

    @Test(expected = NoSuchUserException::class)
    fun testGetById_WrongOwnerId() {
        val ownerId: Int = 1
        NoteService.setCurrentUserId(ownerId)

        val note: Note = NoteService.add("title", "text", Privacy.OnlyUsers, Privacy.All)
        val notes: List<Note> = NoteService.getById(note.id, 111)
    }
    //--------------------------------------------------------------------

    @Test
    fun testGetComments() {
        val ownerNoteId : Int = 1
        val ownerCommentId : Int = 1

        NoteService.setCurrentUserId(ownerNoteId)
        val note : Note = NoteService.add("title", "text", Privacy.All, Privacy.All)
        NoteService.setCurrentUserId(ownerCommentId)
        val comment_1 : NoteComment = NoteService.createComment(note.id, note.ownerId, "message_1")
        val comment_2 : NoteComment = NoteService.createComment(note.id, note.ownerId, "message_2")

        val comments : List<NoteComment> = NoteService.getComments(note.id, note.ownerId)

        assertEquals(2, comments.count())
        assertNotEquals(comments[0].id, comments[1].id)
        assertEquals(note.id, comments[0].noteId)
        assertEquals("message_1", comments[0].message)
        assertEquals(note.id, comments[1].noteId)
        assertEquals("message_2", comments[1].message)
    }
    //--------------------------------------------------------------------

    @Test(expected = NoteNotFoundException::class)
    fun testGetComments_NoteNotFoundException() {
        val ownerNoteId : Int = 1
        val ownerCommentId : Int = 1

        NoteService.setCurrentUserId(ownerNoteId)
        val note : Note = NoteService.add("title", "text", Privacy.All, Privacy.All)
        NoteService.setCurrentUserId(ownerCommentId)
        val comment_1 : NoteComment = NoteService.createComment(note.id, note.ownerId, "message_1")
        val comment_2 : NoteComment = NoteService.createComment(note.id, note.ownerId, "message_2")

        val comments : List<NoteComment> = NoteService.getComments(111, note.ownerId)
    }
    //--------------------------------------------------------------------

    @Test(expected = NoSuchUserException::class)
    fun testGetComments_NoSuchUserException() {
        val ownerNoteId : Int = 1
        val ownerCommentId : Int = 1

        NoteService.setCurrentUserId(ownerNoteId)
        val note : Note = NoteService.add("title", "text", Privacy.All, Privacy.All)
        NoteService.setCurrentUserId(ownerCommentId)
        val comment_1 : NoteComment = NoteService.createComment(note.id, note.ownerId, "message_1")
        val comment_2 : NoteComment = NoteService.createComment(note.id, note.ownerId, "message_2")

        val comments : List<NoteComment> = NoteService.getComments(note.id, 111)
    }
    //--------------------------------------------------------------------

    @Test
    fun testRestoreComment() {
        val ownerId : Int = 1
        NoteService.setCurrentUserId(ownerId)

        val note : Note = NoteService.add("title", "text", Privacy.All, Privacy.All)
        val comment : NoteComment = NoteService.createComment(note.id, note.ownerId, "message")
        NoteService.deleteComment(comment.id, note.ownerId)

        val commentCountBefore : Int = NoteService.getNoteCommentsCount()
        val deletionsCountBefore : Int = NoteService.getDeletionsCount()

        NoteService.restoreComment(comment.id, ownerId)
        val comments : List<NoteComment> = NoteService.getComments(note.id, note.ownerId)

        val commentCountAfter : Int = NoteService.getNoteCommentsCount()
        val deletionsCountAfter : Int = NoteService.getDeletionsCount()

        assertEquals(1, commentCountAfter - commentCountBefore)
        assertEquals(-1, deletionsCountAfter - deletionsCountBefore)
        assertEquals(1, comments.count())
        assertEquals(comment, comments[0])
    }
    //--------------------------------------------------------------------

    @Test(expected = NoteNotFoundException::class)
    fun testRestoreComment_NoteNotFoundException() {
        val ownerId : Int = 1
        NoteService.setCurrentUserId(ownerId)

        val note : Note = NoteService.add("title", "text", Privacy.All, Privacy.All)
        val comment : NoteComment = NoteService.createComment(note.id, note.ownerId, "message")
        NoteService.deleteComment(comment.id, note.ownerId)

        NoteService.restoreComment(111, ownerId)
    }
    //--------------------------------------------------------------------

    @Test(expected = NoSuchUserException::class)
    fun testRestoreComment_NoSuchUserException() {
        val ownerId : Int = 1
        NoteService.setCurrentUserId(ownerId)

        val note : Note = NoteService.add("title", "text", Privacy.All, Privacy.All)
        val comment : NoteComment = NoteService.createComment(note.id, note.ownerId, "message")
        NoteService.deleteComment(comment.id, note.ownerId)

        NoteService.restoreComment(comment.id, 111)
    }
    //--------------------------------------------------------------------




}