package network

import com.github.jan222ik.common.HandLandmark
import io.ktor.websocket.DefaultWebSocketServerSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.decodeFromByteArray

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
        //TODO("Not yet implemented")
    }

    @OptIn(ExperimentalSerializationApi::class)
    fun receivedMessage(id: String, readData: ByteArray) {
        val landmarks = Cbor.decodeFromByteArray<List<HandLandmark>>(readData)
        landmarks.forEach(::println)
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
