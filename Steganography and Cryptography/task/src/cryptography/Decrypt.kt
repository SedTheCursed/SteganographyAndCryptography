package cryptography

import java.awt.Color
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

/**
 * Show a message hidden in the blue tint of an image
 */
fun showMsg() {
    try {
        // Ask for the image with message and the encryption password
        println("Input image file:")
        val inputFile = File(readln())
        println("Password:")
        val pwdInput = readln().toBinary()

        val img = ImageIO.read(inputFile)
        val bits = mutableListOf<Int>()

        // Look for the message bits in the pixels' blue tint till the end marker is found.
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

/**
 * Recover the least bits of an int
 * -
 *      0: the int value is even
 *      1: the int value is odd
 * @receiver Int which least bit value is needed
 * @return The least bit
 */
private fun Int.getBit() = this % 2

/**
 * Check if the recovered bits have reached the end marker (consisting in twenty-two 0 and two 1)
 * @receiver Recovered bits
 * @return If the end marker has been reached
 */
private fun List<Int>.reachEndMsg(): Boolean {
    // if the pointer is currently in the middle of a byte or if there are fewer bits recovered that the length of the
    // end marker, the end can't have been reached.
    if (this.size % 8 != 0 || this.size < 24) return false

    val endMsg = List (22) { 0 }.toMutableList().apply {
        repeat(2) { add(1)}
    }

    return this.takeLast(24) == endMsg
}

/**
 * Decrypt a message (in binary form) by comparing its value to a given password
 * after removing an end marker.
 * @receiver message to be decrypted
 * @param password encryption key (in binary form)
 * @return Decrypted message
 */
private fun List<Int>.decryptMsg(password: List<Int>): String = this.run { slice(0..lastIndex-24) }
    .mapIndexed { i, bin ->  bin xor password[i % password.size] }
    .windowed(8,8) { it.toByte() }
    .toByteArray()
    .toString(Charsets.UTF_8)

/**
 * Transform a list of bits into a Byte value
 * @receiver 8-Bits to be converted
 * @return Byte value
 */
private fun List<Int>.toByte(): Byte  =
    listOf(128, 64, 32, 16, 8, 4, 2, 1).foldIndexed(0) { i, acc, num -> acc + num * this[i] }.toByte()
