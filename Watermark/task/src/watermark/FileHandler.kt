package watermark

import java.awt.image.BufferedImage
import java.io.File
import java.io.FileNotFoundException
import javax.imageio.IIOException
import javax.imageio.ImageIO

class FileHandler {

    companion object {
        fun getImage(fileName: String): BufferedImage? {
            val file = File(fileName)
            try {
                return ImageIO.read(file)
            } catch (_: FileNotFoundException) {
                println("The file $fileName doesn't exist.")
            } catch (_: IIOException) {
                println("The file $fileName doesn't exist.")
            }
            return null
        }

        fun writeImage(image: BufferedImage,fileName: String) {
            val file = File(fileName)
            ImageIO.write(image, fileName.substringAfter("."), file)
            println("The watermarked image $fileName has been created.")
        }
    }
}