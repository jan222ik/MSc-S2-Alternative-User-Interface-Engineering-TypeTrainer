import androidx.compose.foundation.ExperimentalFoundationApi
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ui.util.debug.ifDebug

@KtorExperimentalAPI
@ExperimentalCoroutinesApi
@ExperimentalStdlibApi
@ExperimentalFoundationApi
fun main() {
    System.setProperty("debug", "true")
    ifDebug {
        println("Debug Pre Checks")
        println("Check for translation keys:")
        ui.util.i18n.main()
        println("-".repeat(80))
    }
    DesktopApplication.start()
}
