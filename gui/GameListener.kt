package gui

import Move
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import kotlin.system.exitProcess

class GameListener(private val reset: Runnable) : KeyAdapter() {
    private var up = false

    companion object {
        @JvmStatic
        var move: Move = Move.NONE
    }

    private fun updateMove() {
        move = if (up) Move.UP
        else Move.NONE
    }

    override fun keyPressed(e: KeyEvent) {
        when (e.keyCode) {
            KeyEvent.VK_SPACE -> up = true
            KeyEvent.VK_ENTER -> if (!Game.running) reset.run()
            KeyEvent.VK_ESCAPE -> if (!Game.running) exitProcess(1)
        }
        updateMove()
    }

    override fun keyReleased(e: KeyEvent) {
        when (e.keyCode) {
            KeyEvent.VK_SPACE -> up = false
        }
        updateMove()
    }
}