import com.example.compose.jetchat.feature.webSocketCode.ChatWebSocketManager
import kotlinx.coroutines.flow.SharedFlow

class ChatRepository {
    // ✅ MUST MATCH THE SOCKET TYPE
    val messages: SharedFlow<String> =
        ChatWebSocketManager.messages

    fun connect() {
        ChatWebSocketManager.connect()
    }

    fun send(text: String) {
        ChatWebSocketManager.send(text)
    }
}
