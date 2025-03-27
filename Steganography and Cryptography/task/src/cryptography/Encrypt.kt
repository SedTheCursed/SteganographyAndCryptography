package cryptography

import java.awt.Color
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

fun hideMsg() {
    try {
        println("Input image file:")
        val inputFile = File(readln())
        println("Output image file:")
        val outputFile = File(readln())
        println("Message to hide:")
        val msgInput = readln().toBinary()
        println("Password:")
        val pwdInput = readln().toBinary()

        val msg = msgInput.encryptMsg(pwdInput)
        val img = ImageIO.read(inputFile)

        if (msg.size > img.width * img.height) {
            println("The input image is not large enough to hold this message.")
            return
        }

        val bits = msg.listIterator()

        pixels@ for (y in 0 until img.height) {
            for (x in 0 until img.width) {
                if (!bits.hasNext()) break@pixels

                with (Color(img.getRGB(x, y))) {
                    val cryptoColor = Color(red, green, colorEncrypt(blue, bits.next()))
                    img.setRGB(x, y, cryptoColor.rgb)
                }
            }
        }

        ImageIO.write(img, "png", outputFile)
        println("Message saved in ${outputFile.name} image.")

    } catch (_: IOException) {
        println("Can't read input file!")
    } catch (_: NullPointerException) {
        println("Wrong input")
    }
}

fun String.toBinary(): List<Int> = this.encodeToByteArray().flatMap { it.toBinaryList() }

private fun colorEncrypt(color: Int, bit: Int) = when (bit) {
    color % 2 -> color
    0 -> color - 1
    1 -> color + 1
    else -> color
}

private fun Byte.toBinaryList(): List<Int> {
    val list = List(8) { 0 }.toMutableList()
    var quotient = this.toInt()

    for (i in 7 downTo 0) {
        if (quotient == 0) break
        list[i] = quotient % 2
        quotient /= 2
    }

    return list
}

private fun List<Int>.encryptMsg(password: List<Int>): List<Int> = this
    .mapIndexed { i, bin ->  bin xor password[i % password.size] }
    .toMutableList().apply {
        repeat(22) { add(0)}
        repeat(2) { add(1)}
    }