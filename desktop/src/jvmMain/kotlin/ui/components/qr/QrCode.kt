package ui.components.qr

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import boofcv.alg.fiducial.qrcode.QrCodeEncoder
import boofcv.alg.fiducial.qrcode.QrCodeGeneratorImage
import boofcv.kotlin.asBufferedImage
import org.jetbrains.skija.IRect
import util.ImageUtils.bitmapFromByteArray
import util.NetworkUtils
import java.awt.image.BufferedImage


@Composable
fun QRCode(link: String = NetworkUtils.getAddress()) {
    val qr = QrCodeEncoder().addAutomatic(link).fixate()
    val gen = QrCodeGeneratorImage(15).render(qr)
    val img: BufferedImage = gen.gray.asBufferedImage()

    val bitmap = remember { bitmapFromByteArray(img) }
    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        drawIntoCanvas { canvas ->
            canvas.nativeCanvas.drawBitmapRect(
                bitmap,
                IRect(0, 0, bitmap.getWidth(), bitmap.getHeight()).toRect()
            )
        }
    }
}