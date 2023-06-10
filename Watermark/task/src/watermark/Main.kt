package watermark

import java.awt.Color
import java.awt.Transparency
import java.awt.image.BufferedImage
import java.lang.NumberFormatException

var isWorkinig = true
var alphaTransluent = false


fun main() {
    var fileNameWatermark = ""
    var percentage = ""
    var outputFileName = ""

    println("Input the image filename:")
    val fileNameImage = readln()
    checkImage(fileNameImage)
    if (isWorkinig) {
        println("Input the watermark image filename:")
        fileNameWatermark = readln()
        checkWatermark(fileNameWatermark, fileNameImage)
        if (alphaTransluent) {
            println("Do you want to use the watermark's Alpha channel?")
            val answer = readln().lowercase()
            if (answer != "yes") {
                alphaTransluent = false
            }
        }
    }
    if (isWorkinig) {
        println("Input the watermark transparency percentage (Integer 0-100):")
        percentage = readln()
        checkTransparency(percentage)
    }

    if (isWorkinig) {
        println("Input the output image filename (jpg or png extension):")
        outputFileName = readln()
        checkOutputFormat(outputFileName)
    }

    if (isWorkinig) {
        createWaterMark(fileNameImage, fileNameWatermark, percentage.toInt(), outputFileName, alphaTransluent)
    }
}

fun checkImage(fileName: String) {
    val image = FileHandler.getImage(fileName)
    if (image == null) {
        isWorkinig = false
        return
    }
    if (image.colorModel.numColorComponents != 3) {
        println("The number of image color components isn't 3.")
        isWorkinig = false
        return
    }
    if (image.colorModel.pixelSize != 24 && image.colorModel.pixelSize != 32) {
        println("The image isn't 24 or 32-bit.")
        isWorkinig = false
        return
    }
}

fun checkOutputFormat(fileName: String) {
    if (!fileName.endsWith(".jpg") && !fileName.endsWith(".png")) {
        println("The output file extension isn't \"jpg\" or \"png\".")
        isWorkinig = false
        return
    }
}

fun checkTransparency(percentage: String) {
    val transparency: Int
    try {
        transparency = percentage.toInt()
    } catch (_: NumberFormatException) {
        println("The transparency percentage isn't an integer number.")
        isWorkinig = false
        return
    }
    if (transparency !in 0..100) {
        println("The transparency percentage is out of range.")
        isWorkinig = false
        return
    }
}

fun checkWatermark(fileNameWatermark: String, fileNameImage: String) {
    val watermark = FileHandler.getImage(fileNameWatermark)
    val image = FileHandler.getImage(fileNameImage)
    if (watermark == null) {
        isWorkinig = false
        return
    }
    if (watermark.colorModel.numColorComponents != 3) {
        println("The number of watermark color components isn't 3.")
        isWorkinig = false
        return
    }
    if (watermark.colorModel.pixelSize != 24 && watermark.colorModel.pixelSize != 32) {
        println("The watermark isn't 24 or 32-bit.")
        isWorkinig = false
        return
    }
    if (image?.width != watermark.width || image.height != watermark.height) {
        println("The image and watermark dimensions are different.")
        isWorkinig = false
    }
    if (watermark.transparency == Transparency.TRANSLUCENT) {
        alphaTransluent = true
    }
}

fun createWaterMark(fileNameImage: String, fileNameWatermark: String, weight: Int, outputFileName: String, alpha: Boolean) {
    val image = FileHandler.getImage(fileNameImage)
    val watermark = FileHandler.getImage(fileNameWatermark)
    val output = BufferedImage(image!!.width, image.height, BufferedImage.TYPE_INT_RGB)
    for (x in 0 until output.width) {
        for (y in 0 until output.height) {
            val i = Color(image.getRGB(x, y))
            val w = if (alpha) {
                Color(watermark!!.getRGB(x, y), true)
            } else {
                Color(watermark!!.getRGB(x, y))
            }
            var color: Color
            if (w.alpha == 0) {
                output.setRGB(x, y, Color(image.getRGB(x, y)).rgb)
            } else {
                color = Color(
                    (weight * w.red + (100 - weight) * i.red) / 100,
                    (weight * w.green + (100 - weight) * i.green) / 100,
                    (weight * w.blue + (100 - weight) * i.blue) / 100)
                output.setRGB(x, y, color.rgb)
            }
        }
    }

    FileHandler.writeImage(output, outputFileName)
}

