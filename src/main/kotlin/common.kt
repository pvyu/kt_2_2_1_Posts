package ru.netology.vk.common

enum class Privacy(val code : Int) { Unknown(-1), All(0),
                                     OnlyFriends(1), FriendsAndTheirFriends(2), OnlyUsers(3) }
//---------------------------------------------------------------------------------------------------------------

enum class EntityType { Unknown, Post, PostComment, Note, NoteComment }
//---------------------------------------------------------------------------------------------------------------

abstract class Entity (entityType : EntityType) {
    val id : Int = Common.getNextId()
    val type : EntityType = entityType
}
//---------------------------------------------------------------------------------------------------------------

object Common {
    private var currentID : Int = 0

    fun getNextId() : Int = ++currentID

    fun reset() {
        currentID = 0
    }

}
