package network

import io.ktor.websocket.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random

class Server {
    private var _connection: String? = null

    private val _connectionStatus = MutableStateFlow<Boolean>(_connection != null)
    val connectionStatus: StateFlow<Boolean>
        get() = _connectionStatus


    suspend fun connected(id: String, defaultWebSocketServerSession: DefaultWebSocketServerSession) {
        _connection = id
        _connectionStatus.emit(true)
    }

    fun receivedMessage(id: String, readText: String) {
        println(readText)
        TODO("Not yet implemented")
    }

    suspend fun disconnected(id: String, defaultWebSocketServerSession: DefaultWebSocketServerSession) {
        _connection = null
        _connectionStatus.emit(false)
    }

    suspend fun testConnect(id: String): Boolean {
        val connected = !connectionStatus.value
        _connection = when (connected) {
            false -> null
            true -> id
        }
        _connectionStatus.emit(connected)
        return connected
    }

}
