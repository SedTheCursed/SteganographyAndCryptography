package cryptography

enum class Action { HIDE, SHOW, EXIT, UNKNOWN }

fun main() {
    var action: Action

    do {
        println("Task (hide, show, exit):")
        val input = readln().lowercase()
        action = when (input) {
            "hide" -> doAction(Action.HIDE, ::hideMsg)
            "show" -> doAction(Action.SHOW, ::showMsg)
            "exit" -> doAction(Action.EXIT) { println("Bye!") }
            else -> doAction(Action.UNKNOWN) { println("Wrong task: $input") }
        }
    } while (action != Action.EXIT)
}

private fun doAction(actionType: Action, action: () -> Unit): Action {
    action()
    return actionType
}