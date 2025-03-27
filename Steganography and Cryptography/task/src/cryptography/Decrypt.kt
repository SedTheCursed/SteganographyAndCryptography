package cryptography

import java.awt.Color
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

fun showMsg() {
    try {
        println("Input image file:")
        val inputFile = File(readln())
        println("Password:")
        val pwdInput = readln().toBinary()

        val img = ImageIO.read(inputFile)
        val bits = mutableListOf<Int>()

        pixels@ for (y in 0 until img.height) {
            for (x in 0 until img.width) {
                if (bits.reachEndMsg()) break@pixels
                with(Color(img.getRGB(x, y))) {
                    bits.add(blue.getBit())
                }
            }
        }

        val msg = bits.decryptMsg(pwdInput)

        println("Message:\n$msg")


    } catch (_: IOException) {
        println("Can't read input file!")
    } catch (_: NullPointerException) {
        println("Wrong input")
    }
}

private fun Int.getBit() = this % 2

private fun List<Int>.reachEndMsg(): Boolean {
    if (this.size % 8 != 0 || this.size < 24) return false

    val endMsg = List (22) { 0 }.toMutableList().apply {
        repeat(2) { add(1)}
    }

    return this.takeLast(24) == endMsg
}

private fun List<Int>.decryptMsg(password: List<Int>): String = this.run { slice(0..lastIndex-24) }
    .mapIndexed { i, bin ->  bin xor password[i % password.size] }
    .windowed(8,8) { it.toByte() }
    .toByteArray()
    .toString(Charsets.UTF_8)

private fun List<Int>.toByte(): Byte  =
    listOf(128, 64, 32, 16, 8, 4, 2, 1).foldIndexed(0) { i, acc, num -> acc + num * this[i] }.toByte()
