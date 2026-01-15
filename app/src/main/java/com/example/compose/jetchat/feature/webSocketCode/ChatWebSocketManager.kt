package com.example.compose.jetchat.feature.webSocketCode

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

object ChatWebSocketManager {

    private val _messages = MutableSharedFlow<String>(extraBufferCapacity = 64)
    val messages: SharedFlow<String> = _messages

    fun connect() {
        // open websocket ONCE
    }

    fun send(text: String) {
        // send to websocket
    }

    fun onMessageReceived(text: String) {
        _messages.tryEmit(text)
    }
}
