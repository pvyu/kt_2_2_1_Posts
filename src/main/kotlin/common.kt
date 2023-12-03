package ru.netology.vk.common

enum class Privacy(val code : Int) { Unknown(-1), All(0),
                                     OnlyFriends(1), FriendsAndTheirFriends(2), OnlyUsers(3) }
//---------------------------------------------------------------------------------------------------------------
object Common {
    private var currentID : Int = 0

    fun getNextId() : Int = ++currentID

    fun reset() {
        currentID = 0
    }

}
