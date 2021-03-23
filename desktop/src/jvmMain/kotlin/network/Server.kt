package network

import io.ktor.websocket.*
import kotlin.properties.Delegates
import kotlin.random.Random

typealias ConnectionChangeListener = (Boolean) -> Unit

class Server {
    private val connectionStatusListeners = mutableMapOf<Int, ConnectionChangeListener>()
    private val rng = Random(1L)

    fun addConnectionStatusListener(listener: ConnectionChangeListener): Int {
        val key = rng.nextInt()
        connectionStatusListeners[key] = listener
        return key
    }

    fun removeConnectionStatusListener(listenerKey: Int) {
        connectionStatusListeners.remove(listenerKey)
    }

    private var _connection: String? = null
    var connection: String? by Delegates.observable(_connection) { _, _, newValue ->
        _connection = newValue
        val status = newValue != null
        connectionStatusListeners.values.forEach { it.invoke(status) }
    }

    fun connected(id: String, defaultWebSocketServerSession: DefaultWebSocketServerSession) {
        connection = id
    }

    fun receivedMessage(id: String, readText: String) {
        TODO("Not yet implemented")
    }

    fun disconnected(id: String, defaultWebSocketServerSession: DefaultWebSocketServerSession) {
        TODO("Not yet implemented")
    }

    fun testConnect(id: String): Boolean {
        val connected = connection != null
        connection = id.takeUnless { connected }
        return connected
    }
}
