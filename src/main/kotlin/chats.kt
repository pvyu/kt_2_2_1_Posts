package ru.netology.vk.chats

import ru.netology.posts.exceptions.NoSuchChatException
import ru.netology.vk.common.Common
import ru.netology.vk.common.Entity
import ru.netology.vk.common.EntityType

data class Message (
    val fromId : Int = 0,                       // ID автора сообщения
    var isRead : Boolean = false,               // Признак того, что сообщение прочитано
    val text : String = "",                     // Текст сообщения
    override val id : Int = Common.getNextId(), // ID сообщения
) : Entity(EntityType.Chat)
//----------------------------------------------------------------------------------------------------------------------

data class Chat (
    val userId : Int = 0,                                    // ID пользователя, с которым ведётся чат
    val messages : MutableList<Message> = mutableListOf(),   // Сообщения чата
    override val id : Int = Common.getNextId(),              // ID чата
) : Entity(EntityType.Chat)
//----------------------------------------------------------------------------------------------------------------------

object ChatService {
    private val chats : MutableMap<Int, Chat> = mutableMapOf()
    //-----------------------------------------------------------

    fun clear() = chats.clear()
    //-----------------------------------------------------------

    fun getUnreadChatsCount() = chats.values.count { chat : Chat -> chat.messages.any {!it.isRead} }
    //-----------------------------------------------------------

    fun getChats() : Map<Int, Chat> = chats
    //-----------------------------------------------------------

    fun getLastMessages() : List<Pair<Int, String>> = chats.values.map { Pair(it.userId, it.messages.lastOrNull()?.text ?: "No messages") }
    //-----------------------------------------------------------

    fun getMessages(userId : Int, count : Int) : List<Message> {
        val chat : Chat = chats[userId] ?: throw NoSuchChatException("ChatService.getMessages: No chat whit user with id = $userId")
        return chat.messages.filter { msg : Message -> !msg.isRead }.take(count).onEach { it.isRead = true }
    }
    //-----------------------------------------------------------

    fun addMessage(userChart : Int, userId : Int, message : String) : Message {
        chats.getOrPut(userChart, { Chat(userChart) }).messages += Message(userId, false, message)
        return chats[userChart]!!.messages.last()
    }
    //-----------------------------------------------------------

    fun deleteMessage(userId : Int, messageId : Int) : Boolean {
        val chat : Chat = chats[userId] ?: throw NoSuchChatException("ChatService.deleteMessage: No chat whit user with id = $userId")
        return chat.messages.removeIf { it.id == messageId }
    }
    //-----------------------------------------------------------

    fun deleteChat(userId : Int) : Boolean {
        return chats.remove(userId) != null
    }
    //-----------------------------------------------------------

    fun printChats() = println(chats)
    //-----------------------------------------------------------



}
//----------------------------------------------------------------------------------------------------------------------
