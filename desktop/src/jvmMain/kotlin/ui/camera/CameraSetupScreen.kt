package ui.camera

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.imageFromResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.jan222ik.common.HasDoc
import com.github.jan222ik.common.ui.dashboard.BaseDashboardCard
import ui.util.i18n.i18n
import ui.util.span_parse.parseForSpans

@HasDoc
@Composable
fun CameraSetupScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        BaseDashboardCard(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(vertical = 24.dp)
                .fillMaxHeight()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(36.dp)
            ) {

                Column {
                    val mod = Modifier.padding(start = 12.dp)
                    val boldHighlightSpan =
                        SpanStyle(color = MaterialTheme.colors.primary, fontWeight = FontWeight.Bold)
                    Text(
                        text = +i18n.str.camera.setup.instruction_header,
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        modifier = mod.padding(top = 8.dp),
                        text = "- " + (+i18n.str.camera.setup.first_point).parseForSpans(boldHighlightSpan),
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        modifier = mod,
                        text = "- " + (+i18n.str.camera.setup.second_point).parseForSpans(boldHighlightSpan),
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        modifier = mod,
                        text = "- " + (+i18n.str.camera.setup.third_point).parseForSpans(boldHighlightSpan),
                        style = MaterialTheme.typography.body1
                    )
                }
                Image(
                    bitmap = imageFromResource("keyboard_png.png"),
                    contentDescription = "Illustration for Camera Setup"
                )
            }
        }
    }
}
