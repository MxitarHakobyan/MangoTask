package com.mango.task.ui.screens.dummies

import com.mango.task.ui.screens.chat.ChatMessage
import com.mango.task.ui.screens.chatList.Chat

fun getDummyChats(): List<Chat> {
    return listOf(
        Chat(
            id = "1",
            userName = "Alice",
            lastMessage = "Hey, how are you?",
            time = "10:00 AM",
            unreadCount = 2,
            messages = mutableListOf(
                ChatMessage(
                    id = "1",
                    content = "Hey, how are you?",
                    isSentByUser = false,
                    timestamp = "10:00 AM"
                ),
                ChatMessage(
                    id = "2",
                    content = "???",
                    isSentByUser = false,
                    timestamp = "10:00 AM"
                ),
                ChatMessage(
                    id = "3",
                    content = "I'm good, thank you! How about you?",
                    isSentByUser = true,
                    timestamp = "10:01 AM"
                ),
                ChatMessage(
                    id = "4",
                    content = "Doing great! Thanks for asking.",
                    isSentByUser = false,
                    timestamp = "10:02 AM"
                )
            )
        ),
        Chat(
            id = "2",
            userName = "Bob",
            lastMessage = "See you soon!",
            time = "09:45 AM",
            messages = mutableListOf(
                ChatMessage(
                    id = "1",
                    content = "See you soon!",
                    isSentByUser = false,
                    timestamp = "09:45 AM"
                )
            )
        ),
        Chat(
            id = "3",
            userName = "Alan",
            lastMessage = "Hey!",
            time = "09:46 AM",
            unreadCount = 0,
            messages = mutableListOf(
                ChatMessage(
                    id = "1",
                    content = "Hey!",
                    isSentByUser = false,
                    timestamp = "09:46 AM"
                )
            )
        ),
        Chat(
            id = "4",
            userName = "Charlie",
            lastMessage = "Don't forget the meeting",
            time = "Yesterday",
            messages = mutableListOf(
                ChatMessage(
                    id = "1",
                    content = "Don't forget the meeting tomorrow at 10.",
                    isSentByUser = false,
                    timestamp = "Yesterday"
                ),
                ChatMessage(
                    id = "2",
                    content = "Got it. I'll be there.",
                    isSentByUser = true,
                    timestamp = "Yesterday"
                )
            )
        ),
        Chat(
            id = "5",
            userName = "Diana",
            lastMessage = "Let's grab lunch",
            time = "Yesterday",
            unreadCount = 1,
            messages = mutableListOf(
                ChatMessage(
                    id = "1",
                    content = "Let's grab lunch tomorrow.",
                    isSentByUser = false,
                    timestamp = "Yesterday"
                )
            )
        ),
        Chat(
            id = "6",
            userName = "Eve",
            lastMessage = "Happy Birthday!",
            time = "2 days ago",
            messages = mutableListOf(
                ChatMessage(
                    id = "1",
                    content = "Happy Birthday!",
                    isSentByUser = false,
                    timestamp = "2 days ago"
                ),
                ChatMessage(
                    id = "2",
                    content = "Thanks a lot!",
                    isSentByUser = true,
                    timestamp = "2 days ago"
                )
            )
        ),
        Chat(
            id = "7",
            userName = "Arno",
            lastMessage = "",
            time = "",
            messages = mutableListOf()
        ),
        Chat(
            id = "8",
            userName = "Emp",
            lastMessage = "Alright!",
            time = "2 days ago",
            messages = mutableListOf(
                ChatMessage(
                    id = "1",
                    content = "Alright!",
                    isSentByUser = false,
                    timestamp = "2 days ago"
                ),
                ChatMessage(
                    id = "2",
                    content = "Thanks a lot!",
                    isSentByUser = true,
                    timestamp = "2 days ago"
                )
            )
        )
    )
}