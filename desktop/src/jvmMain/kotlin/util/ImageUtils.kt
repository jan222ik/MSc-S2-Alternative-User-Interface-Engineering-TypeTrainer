package util

import org.jetbrains.skija.Bitmap
import org.jetbrains.skija.ColorAlphaType
import org.jetbrains.skija.ImageInfo
import java.awt.image.BufferedImage

object ImageUtils {

    fun bitmapFromByteArray(bufferedImage: BufferedImage): Bitmap {
        var image: BufferedImage? = bufferedImage

        if (image == null) {
            image = BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)
        }
        val pixels = getBytes(image) // assume we only have raw pixels

        // allocate and fill skija Bitmap
        val bitmap = Bitmap()
        bitmap.allocPixels(ImageInfo.makeS32(image.width, image.height, ColorAlphaType.PREMUL))
        bitmap.installPixels(bitmap.getImageInfo(), pixels, (image.width * 4).toLong())

        return bitmap
    }

    // creating byte array from BufferedImage
    fun getBytes(image: BufferedImage): ByteArray {
        val width = image.width
        val height = image.height

        val buffer = IntArray(width * height)
        image.getRGB(0, 0, width, height, buffer, 0, width)

        val pixels = ByteArray(width * height * 4)

        var index = 0
        for (y in 0 until height) {
            for (x in 0 until width) {
                val pixel = buffer[y * width + x]
                pixels[index++] = ((pixel and 0xFF)).toByte() // Blue component
                pixels[index++] = (((pixel shr 8) and 0xFF)).toByte() // Green component
                pixels[index++] = (((pixel shr 16) and 0xFF)).toByte() // Red component
                pixels[index++] = (((pixel shr 24) and 0xFF)).toByte() // Alpha component
            }
        }

        return pixels
    }
}
