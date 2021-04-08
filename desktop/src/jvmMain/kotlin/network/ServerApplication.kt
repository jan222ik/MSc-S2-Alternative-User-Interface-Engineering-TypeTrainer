package network

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.cio.websocket.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.util.*
import io.ktor.websocket.*
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
            webSocket("/ws") {
                val session = call.sessions.get<SocketSession>()

                if (session == null) {
                    close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No session"))
                    return@webSocket
                }
                server.connected(session.id, this)
                try {
                    incoming.consumeEach { frame ->
                        if (frame is Frame.Text) {
                            server.receivedMessage(session.id, frame.readText())
                        }
                    }
                } finally {
                    server.disconnected(session.id, this)
                }
            }
            get("/test") {
                println("Requested test")
                val testConnect = server.testConnect("test")
                call.respond(if (!testConnect) "Connected" else "Disconnected")
            }
        }
    }

    data class SocketSession(val id: String)
}
