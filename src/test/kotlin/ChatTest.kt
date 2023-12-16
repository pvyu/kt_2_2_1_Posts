import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ru.netology.posts.exceptions.NoSuchChatException
import ru.netology.vk.chats.Chat
import ru.netology.vk.chats.ChatService
import ru.netology.vk.chats.Message
import ru.netology.vk.common.Common


class ChatTest {
    @Before
    fun clearBeforeTest() {
        ChatService.clear()
        Common.clear()
    }
    //--------------------------------------------------------------------

    @Test
    fun testGetChats() {
        val msg_1 : String = "from_1_to_2_#1"
        ChatService.addMessage(2, 1, msg_1)
        val msg_2 : String = "from_2_to_1_#2"
        ChatService.addMessage(2, 2, msg_2)
        val msg_3 : String = "from_1_to_3_#1"
        ChatService.addMessage(3, 1, msg_3)
        val msg_4 : String = "from_1_to_4_#1"
        ChatService.addMessage(4, 1, msg_4)

        val chats : Map<Int, Chat> = ChatService.getChats()

        assertEquals(3, chats.count())
        assertEquals(2, chats[2]!!.messages.count())
        assertEquals(1, chats[3]!!.messages.count())
        assertEquals(1, chats[4]!!.messages.count())
    }
    //--------------------------------------------------------------------

    @Test
    fun getMessages() {
        val msg_1 : String = "from_1_to_2_#1"
        ChatService.addMessage(2, 1, msg_1)
        val msg_2 : String = "from_2_to_1_#2"
        ChatService.addMessage(2, 2, msg_2)
        val msg_3 : String = "from_1_to_3_#1"
        ChatService.addMessage(3, 1, msg_3)
        val msg_4 : String = "from_1_to_4_#1"
        ChatService.addMessage(4, 1, msg_4)

        val msgs : List<Message> = ChatService.getMessages(2, 5)

        assertEquals(2, msgs.count())
        assertEquals(true, msgs[0]!!.isRead)
        assertEquals(1, msgs[0]!!.fromId)
        assertEquals(msg_1, msgs[0]!!.text)
        assertEquals(msg_2, msgs[1]!!.text)
    }
    //--------------------------------------------------------------------

    @Test(expected = NoSuchChatException::class)
    fun getMessages_NoSuchChatException() {
        val msg_1 : String = "from_1_to_2_#1"
        ChatService.addMessage(2, 1, msg_1)
        val msg_2 : String = "from_2_to_1_#2"
        ChatService.addMessage(2, 2, msg_2)
        val msg_3 : String = "from_1_to_3_#1"
        ChatService.addMessage(3, 1, msg_3)
        val msg_4 : String = "from_1_to_4_#1"
        ChatService.addMessage(4, 1, msg_4)

        val msgs : List<Message> = ChatService.getMessages(111, 5)
    }
    //--------------------------------------------------------------------


    @Test
    fun deleteMessage() {
        val msg_1 : String = "from_1_to_2_#1"
        ChatService.addMessage(2, 1, msg_1)
        val msg_2 : String = "from_2_to_1_#2"
        val msg : Message = ChatService.addMessage(2, 2, msg_2)
        val msg_3 : String = "from_1_to_3_#1"
        ChatService.addMessage(3, 1, msg_3)
        val msg_4 : String = "from_1_to_4_#1"
        ChatService.addMessage(4, 1, msg_4)


        ChatService.deleteMessage(2, msg.id)

        val msgs : List<Message> = ChatService.getMessages(2, 5)

        assertEquals(1, msgs.count())
        assertEquals(true, msgs[0]!!.isRead)
        assertEquals(1, msgs[0]!!.fromId)
        assertEquals(msg_1, msgs[0]!!.text)
    }
    //--------------------------------------------------------------------

    @Test(expected = NoSuchChatException::class)
    fun deleteMessage_NoSuchChatException() {
        val msg_1 : String = "from_1_to_2_#1"
        ChatService.addMessage(2, 1, msg_1)
        val msg_2 : String = "from_2_to_1_#2"
        val msg : Message = ChatService.addMessage(2, 2, msg_2)
        val msg_3 : String = "from_1_to_3_#1"
        ChatService.addMessage(3, 1, msg_3)
        val msg_4 : String = "from_1_to_4_#1"
        ChatService.addMessage(4, 1, msg_4)


        ChatService.deleteMessage(111, msg.id)
    }
    //--------------------------------------------------------------------

    @Test
    fun deleteChat() {
        val msg_1 : String = "from_1_to_2_#1"
        ChatService.addMessage(2, 1, msg_1)
        val msg_2 : String = "from_2_to_1_#2"
        ChatService.addMessage(2, 2, msg_2)
        val msg_3 : String = "from_1_to_3_#1"
        ChatService.addMessage(3, 1, msg_3)
        val msg_4 : String = "from_1_to_4_#1"
        ChatService.addMessage(4, 1, msg_4)


        val result : Boolean = ChatService.deleteChat(2)
        val chats : Map<Int, Chat> = ChatService.getChats()

        assertEquals(true, result)

        assertEquals(2, chats.count())
    }
    //--------------------------------------------------------------------

    @Test
    fun deleteChat_NoChat() {
        val msg_1 : String = "from_1_to_2_#1"
        ChatService.addMessage(2, 1, msg_1)
        val msg_2 : String = "from_2_to_1_#2"
        ChatService.addMessage(2, 2, msg_2)
        val msg_3 : String = "from_1_to_3_#1"
        ChatService.addMessage(3, 1, msg_3)
        val msg_4 : String = "from_1_to_4_#1"
        ChatService.addMessage(4, 1, msg_4)


        val result : Boolean = ChatService.deleteChat(111)

        assertEquals(false, result)
    }
    //--------------------------------------------------------------------


}