package network

import com.github.jan222ik.common.network.ServerConfig
import io.ktor.application.Application
import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.DefaultHeaders
import io.ktor.http.cio.websocket.CloseReason
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.close
import io.ktor.http.cio.websocket.pingPeriod
import io.ktor.http.cio.websocket.readText
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.ktor.sessions.set
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.generateNonce
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.consumeEach
import java.time.Duration

class ServerApplication(private val server: Server) {

    @KtorExperimentalAPI
    @ExperimentalCoroutinesApi
    fun Application.create() {
        install(DefaultHeaders)
        install(CallLogging)
        install(WebSockets) {
            pingPeriod = Duration.ofMinutes(1)
        }
        install(Sessions) {
            cookie<SocketSession>("SESSION")
        }
        intercept(ApplicationCallPipeline.Features) {
            if (call.sessions.get<SocketSession>() == null) {
                call.sessions.set(SocketSession(generateNonce()))
            }
        }

        routing {
            webSocket(ServerConfig.ROUTE_WEBSOCKET) {
                val session = call.sessions.get<SocketSession>()

                if (session == null) {
                    close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No session"))
                    return@webSocket
                }
                server.connected(session.id, this)
                try {
                    incoming.consumeEach { frame ->
                        when (frame) {
                            is Frame.Text ->
                                server.receivedMessage(session.id, frame.readText())
                            is Frame.Binary ->
                                server.receivedMessage(session.id, frame.data)
                            else -> {
                            }
                        }
                    }
                } finally {
                    server.disconnected(session.id, this)
                }
            }
            get(ServerConfig.ROUTE_TEST) {
                println("Requested test")
                val testConnect = server.testConnect("test")
                call.respond(if (!testConnect) "Connected" else "Disconnected")
            }
        }
    }

    data class SocketSession(val id: String)
}
