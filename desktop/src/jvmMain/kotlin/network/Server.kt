package network

import com.github.jan222ik.common.FingerEnum
import com.github.jan222ik.common.FingerTipLandmark
import com.github.jan222ik.common.HandLandmark
import com.github.jan222ik.common.dto.MobileStatsData
import com.github.jan222ik.common.dto.toPointDTO
import com.github.jan222ik.compose_mpp_charts.core.data.DataPoint
import io.ktor.websocket.DefaultWebSocketServerSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import textgen.database.DatabaseFactory
import ui.dashboard.content.DashboardStatsIntent
import kotlin.concurrent.fixedRateTimer

class Server {
    private var _connection: String? = null

    private val _connectionStatus = MutableStateFlow<Boolean>(_connection != null)
    val connectionStatus: StateFlow<Boolean>
        get() = _connectionStatus

    private val handLandMarkChannel: Channel<List<HandLandmark>> =
        Channel(capacity = 5, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    val handLandMarks: ReceiveChannel<List<HandLandmark>>
        get() = handLandMarkChannel

    fun usage() {
        val poll = handLandMarkChannel.poll()
        if (poll != null) {
            // Add try if channel closed
            poll.forEach(::println)
        } else {
            // No data in channel
        }
    }

    suspend fun connected(id: String, defaultWebSocketServerSession: DefaultWebSocketServerSession) {
        _connection = id
        _connectionStatus.emit(true)
    }

    fun receivedMessage(id: String, readText: String) {
        TODO("Not yet implemented")
    }

    @OptIn(ExperimentalSerializationApi::class)
    suspend fun receivedMessage(id: String, readData: ByteArray) {
        val landmarks = Cbor.decodeFromByteArray<List<HandLandmark>>(readData)
        handLandMarkChannel.send(landmarks)

        // TODO something with the data
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

    private val jsonParser = Json() {
        serializersModule = DatabaseFactory.serializer
    }

    fun getStatsForMobile(): String {
        val statsIntent = DashboardStatsIntent()
            .also { runBlocking(Dispatchers.IO) { it.init(this) } }
        val weeklyData = statsIntent
            .weekData.value
            .also { println(it) }
            .map { it.toPointDTO() }
        val totalExercises = statsIntent.totalExercises()
        val streakDays = statsIntent.streakDays()
        return jsonParser.encodeToString(MobileStatsData(weeklyData, totalExercises, streakDays))
    }

}


@OptIn(ExperimentalSerializationApi::class)
fun main() {
    Server().let {
        var idx = 0f
        fixedRateTimer(period = 500L) {
            val list = listOf(
                HandLandmark().also {
                    it.fingerLandmarks[FingerEnum.INDEX] = FingerTipLandmark(
                        FingerEnum.INDEX, idx, idx, idx
                    )
                }
            )
            idx++
            GlobalScope.launch(Dispatchers.IO) {
                it.receivedMessage("", Cbor.encodeToByteArray(list))
                println("Sent")
            }
        }
        fixedRateTimer(period = 1000L) {
            it.usage()
        }
    }
}
