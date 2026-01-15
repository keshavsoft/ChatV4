package com.example.compose.jetchat.feature.chatws.forMessages

enum class WsMessageType {
    IS_STUDENT,
    PHONE,
    UNKNOWN,
    SYSTEM,
    STUDENT_CONNECTED
}

data class WsChatMessage(
    val author: String,
    val content: String,
    val timestamp: String,
    val type: WsMessageType
)
