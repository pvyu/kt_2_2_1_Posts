package ru.netology.notes

import ru.netology.posts.exceptions.CommentNotFoundException
import ru.netology.posts.exceptions.NoSuchUserException
import ru.netology.posts.exceptions.NoUserGivenException
import ru.netology.posts.exceptions.NoteNotFoundException
import ru.netology.vk.common.Common
import ru.netology.vk.common.Entity
import ru.netology.vk.common.EntityType
import ru.netology.vk.common.Privacy


data class Note (
    val ownerId : Int = 0,                          // ID автора заметки
    val title : String = "",                        // Заголовок заметки
    val text : String = "",                         // Текст заметки
    val privacy : Privacy = Privacy.Unknown,        // Уровень доступа к заметке
    val commentPrivacy : Privacy = Privacy.Unknown  // Уровень доступа к комментированию заметки
) : Entity(EntityType.Note)
//---------------------------------------------------------------------------------------------------------------

data class NoteComment(
    val noteId : Int = 0,                           // ID заметки, на которую оставлен комментарий
    val fromId : Int = 0,                           // ID автора заметки
    val message : String = "",                      // Текст комментария
) : Entity(EntityType.NoteComment)
//---------------------------------------------------------------------------------------------------------------

object NoteService {
    private var notes : MutableList<Note> = ArrayList<Note>()
    private var noteComments : MutableList<NoteComment> = ArrayList<NoteComment>()
    private var deleted : MutableList<Entity> = ArrayList<Entity>()

    private var currentUserId : Int = 0

    //-----------------------------------------------------------------

    fun setCurrentUserId(userId : Int) {
        currentUserId = userId
    }
    //-----------------------------------------------------------------

    fun add(title : String, text : String, privacy : Privacy, commentPrivacy : Privacy) : Int {
        if (currentUserId == 0) {
            throw NoUserGivenException("NoteService.add: user not given")
        }
        notes += Note(currentUserId, title, text, privacy, commentPrivacy)
        return notes.last().id
    }
    //-----------------------------------------------------------------

    fun createComment(noteId : Int, ownerId : Int, message : String) : Int {
        if (currentUserId == 0) {
            throw NoUserGivenException("NoteService.add: user not given")
        }
        var currentNote : Note? = null
        for ((index, note) in notes.withIndex()) {
            if (note.id == noteId) {
                currentNote = note
                break
            }
        }
        if (currentNote == null) {
            throw CommentNotFoundException("NoteService.createComment: Comment with id = $noteId not found")
        }
        if (currentNote!!.ownerId != ownerId) {
            throw NoSuchUserException("NoteService.createComment: Author of comment with id = $noteId is not user with id = $ownerId")
        }
        noteComments += NoteComment(noteId, currentUserId, message)
        return noteComments.last().id
    }
    //-----------------------------------------------------------------

    fun delete(noteId : Int) : Boolean {
        var wasFound : Boolean = false
        var noteIter : MutableIterator<Note> = notes.iterator()
        while (noteIter.hasNext()) {
            val note : Note = noteIter.next()
            if (note.id == noteId) {
                deleted += (note as Entity)
                noteIter.remove()
                wasFound = true
                break
            }
        }
        if (wasFound) {
            deleteNoteComments(noteId)
        }
        return wasFound
    }
    //-----------------------------------------------------------------

    fun deleteNoteComments(noteId : Int) : Boolean {
        var wasFound : Boolean = false
        var commentIter : MutableIterator<NoteComment> = noteComments.iterator()
        while (commentIter.hasNext()) {
            val comment : NoteComment = commentIter.next()
            if (comment.noteId == noteId) {
                deleted += (comment as Entity)
                commentIter.remove()
                wasFound = true
            }
        }
        return wasFound
    }
    //-----------------------------------------------------------------

    fun deleteComment(commentId : Int, noteOwnerId : Int) {
        var wasFound : Boolean = false
        var wasDeleted : Boolean = false
        var commentIter : MutableIterator<NoteComment> = noteComments.iterator()
        while (commentIter.hasNext()) {
            val comment : NoteComment = commentIter.next()
            if (comment.id == commentId) {
                wasFound = true
                for (note in notes) {
                    if (note.id == comment.noteId && note.ownerId == noteOwnerId) {
                       deleted += (comment as Entity)
                       commentIter.remove()
                       wasDeleted = true
                       break
                   }
                } // for note in notes
                break
            } // if comment.id == commentId
        } // while commentIter.hasNext()
        if (!wasFound) {
            throw CommentNotFoundException("NoteService.deleteComment: Comment with id = $commentId not found")
        }
        if (wasFound && !wasDeleted) {
            throw NoSuchUserException("NoteService.deleteComment: Author of comment`s note is not user with id = $noteOwnerId")
        }
    }
    //-----------------------------------------------------------------

    fun edit(noteId : Int, title : String, text : String, privacy : Privacy, commentPrivacy : Privacy) {
        var wasEdited : Boolean = false
        for ((index, note) in notes.withIndex()) {
            if (note.id == noteId) {
                notes[index] = notes[index].copy(title = title, text = text, privacy = privacy, commentPrivacy = commentPrivacy)
                wasEdited = true
                break
            }
        }
        if (!wasEdited) {
            throw NoteNotFoundException("NoteService.edit: Note with id = $noteId not found")
        }
    }
    //-----------------------------------------------------------------

    fun editComment(commentId : Int, noteOwnerId : Int,  message : String) {
        var wasFound : Boolean = false
        var wasEdited : Boolean = false

        for ((index, comment) in noteComments.withIndex()) {
            if (comment.id == commentId) {
                wasFound = true
                for (note in notes) {
                    if (note.id == comment.noteId) {
                        if (note.ownerId == noteOwnerId) {
                            noteComments[index] = noteComments[index].copy(message = message)
                            wasEdited = true
                        }
                        break
                    } // if note.id == comment.noteId
                } // for note in notes
            } // if comment.id == commentId
        } // for (index, comment) in noteComments.withIndex

        if (!wasFound) {
            throw CommentNotFoundException("NoteService.editComment: Comment with id = $commentId not found")
        }
        if (wasFound && !wasEdited) {
            throw NoSuchUserException("NoteService.editComment: Author of comment`s note is not user with id = $noteOwnerId")
        }
    }
    //-----------------------------------------------------------------

    fun get(noteIds : List<Int>, userId : Int) : List<Note> {
        var resultNodes : MutableList<Note> = mutableListOf<Note>()

        for (note in notes) {
            if (noteIds.contains(note.id) && note.ownerId == userId) {
                resultNodes += note
            }
        }
        return (resultNodes as List<Note>)
    }
    //-----------------------------------------------------------------

    fun getById(noteId : Int, userId : Int) : List<Note> {
        var resultNodes : MutableList<Note> = mutableListOf<Note>()
        var wasFound : Boolean = false
        var isUserIdCorrect : Boolean = false

        for (note in notes) {
            if (note.id == noteId) {
                wasFound = true
                if (note.ownerId == userId) {
                    resultNodes += note
                    isUserIdCorrect = true
                }
                break
            }
        }
        if (!wasFound) {
            throw NoteNotFoundException("NoteService.getById: Note with id = $noteId not found")
        }
        if (wasFound && !isUserIdCorrect) {
            throw NoSuchUserException("NoteService.getById: Author of note is not user with id = $userId")
        }
        return (resultNodes as List<Note>)
    }
    //-----------------------------------------------------------------

    fun getComments(noteId : Int, userId : Int) : List<NoteComment> {
        var resultComments : MutableList<NoteComment> = mutableListOf<NoteComment>()
        var noteForComments : Note? = null

        for (note in notes) {
            if (note.id == noteId) {
                noteForComments = note
            }
        }
        if (noteForComments == null) {
            throw NoteNotFoundException("NoteService.getComments: Note with id = $noteId not found")
        }
        if (noteForComments!!.ownerId != userId) {
            throw NoSuchUserException("NoteService.getComments: Author of note is not user with id = $userId")
        }

        for (comment in noteComments) {
            if (comment.noteId == noteId) {
                resultComments +=comment
            }
        }
        return (resultComments as List<NoteComment>)
    }
    //-----------------------------------------------------------------

    fun restoreComment(commentId : Int, ownerId : Int) : NoteComment {
        var restoredComment : NoteComment? = null
        var deletedIter : MutableIterator<Entity> = deleted.iterator()
        var wasCommentFound : Boolean = false
        var wasCommentRestored : Boolean = false

        while (deletedIter.hasNext()) {
            val entity : Entity = deletedIter.next()
            if (entity.id == commentId && entity.type == EntityType.NoteComment && (entity is NoteComment)) {
                wasCommentFound = true
                restoredComment = (entity as NoteComment)
                for (note in notes) {
                    if (note.id == restoredComment.noteId) {
                        if (note.ownerId == ownerId) {
                            wasCommentRestored = true
                            noteComments += restoredComment!!
                            deletedIter.remove()
                            break
                        } // if note.ownerId == ownerId
                    } // if note.id == comment.noteId
                } // for note in notes
                if (!wasCommentRestored) {
                    restoredComment = null
                }
                break
            } // if entity.id == commentId
        } // while deletedIter.hasNext()

        if (!wasCommentFound) {
            throw NoteNotFoundException("NoteService.restoreComment: Comment with id = $commentId not found")
        }
        if (!wasCommentRestored || restoredComment == null) {
            throw NoSuchUserException("NoteService.restoreComment: Author of comment`s note is not user with id = $ownerId")
        }

        return restoredComment!!
    }
    //-----------------------------------------------------------------

}
//---------------------------------------------------------------------------------------------------------------
