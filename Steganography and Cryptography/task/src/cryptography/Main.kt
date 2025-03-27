package cryptography

enum class Action { HIDE, SHOW, EXIT, UNKNOWN }

fun main() {
    var action: Action

    do {
        println("Task (hide, show, exit):")
        val input = readln().lowercase()
        action = when (input) {
            "hide" -> makeAction(Action.HIDE, ::hideMsg)
            "show" -> makeAction(Action.SHOW, ::showMsg)
            "exit" -> makeAction(Action.EXIT) { println("Bye!") }
            else -> makeAction(Action.UNKNOWN) { println("Wrong task: $input") }
        }
    } while (action != Action.EXIT)
}

private fun makeAction(action: Action, doAction: () -> Unit): Action {
    doAction()
    return action
}