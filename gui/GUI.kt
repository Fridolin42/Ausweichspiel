package gui

import java.awt.EventQueue
import java.awt.Toolkit
import javax.swing.JButton
import javax.swing.JFrame

class GUI {
    private val frame: JFrame = JFrame("Ausweichspiel")
    private val start: JButton = JButton("Starten")

    init {
        frame.isResizable = false
        frame.pack()
        frame.setLocationRelativeTo(null)
        frame.setSize(800, 600)
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        val screenSize = Toolkit.getDefaultToolkit().screenSize
        frame.setLocation(screenSize.width / 2 - 800 / 2, screenSize.height / 2 - 600 / 2)
        frame.layout = null

        start.setBounds(350, 225, 100, 50)
        start.addActionListener { startGame() }
        frame.add(start)

        frame.isVisible = true
    }

    private fun startGame() {
        start.isVisible = false
        EventQueue.invokeLater {
            var game: Game? = null
            game = Game {
                game?.isVisible = false
                frame.remove(game)
                start.isVisible = true
            }
            game.setBounds(0, 0, 800, 600)
            frame.add(game)
            game.requestFocusInWindow()
            println("Start Game")
        }
    }
}