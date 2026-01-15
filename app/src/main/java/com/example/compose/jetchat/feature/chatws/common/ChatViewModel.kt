package com.example.compose.jetchat.feature.chatws.common

import ChatRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharedFlow

class ChatViewModel : ViewModel() {

    private val repo = ChatRepository()

    val messages: SharedFlow<String> = repo.messages

    init {
        repo.connect()
    }

    fun send(text: String) {
        repo.send(text)
    }
}
