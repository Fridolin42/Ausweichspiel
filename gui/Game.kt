package gui

import Move
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.ImageIcon
import javax.swing.JPanel
import javax.swing.Timer
import kotlin.math.abs

class Game(reset: Runnable) : JPanel(), ActionListener {
    private val cube: Image
    private val strip: Image

    private val strips = ArrayList<Point>()
    private val timer: Timer
    private val positions: IntArray

    private var cubeX = 100
    private var cubeY = 100
    private var counter = 0
    private var speed: Double = 0.0
    private val gravity = 0.2
    private var score = -1

    companion object {
        @JvmStatic
        var running = false
    }

    init {
        addKeyListener(GameListener(reset))
        preferredSize = Dimension(800, 600)
        isFocusable = true
        background = Color.DARK_GRAY

        positions = IntArray(40)
        for (i in 0 until 40)
            positions[i] = i * 10 - 180

        val cubeLocation = this.javaClass.getResource("cube.png")
        val stripLocation = this.javaClass.getResource("strip.png")
        println(cubeLocation)
        println(stripLocation)
        val cubeIcon = ImageIcon(cubeLocation)
        val stripIcon = ImageIcon(stripLocation)
        cube = cubeIcon.image
        strip = stripIcon.image
        spawnStrip()
        running = true

        timer = Timer(25, this)
        timer.start()
    }

    private fun spawnStrip() {
        val random: Int = positions[(Math.random() * positions.size).toInt()]
        strips.add(Point(850, random - 400))
        strips.add(Point(850, random - 200))
        strips.add(Point(850, random))

        strips.add(Point(850, random + 300))
        strips.add(Point(850, random + 500))
        strips.add(Point(850, random + 700))
    }

    private fun checkDeath() {
        for (s in strips) {
            if (cubeX > s.x && cubeX < s.x + 40 || cubeX + 50 > s.x && cubeX + 50 < s.x + 40) if (cubeY > s.y && cubeY < s.y + 200 || cubeY + 50 > s.y && cubeY + 50 < s.y + 200)
                running = false
        }
    }

    private fun move() {
        if (cubeY > -5 && cubeY < 500) {
            if (GameListener.move == Move.UP && speed > -10) {
                speed -= 0.4
            }
        } else speed *= -0.8
        if (speed < 10) speed += gravity
        cubeY += speed.toInt()
        for (s in strips) s.x -= 5
    }

    private fun deleteOldStrips() {
        for (i in 0 until strips.size) {
            if (strips[i].x < -50) strips.removeAt(i)
            break
        }
    }

    override fun actionPerformed(e: ActionEvent?) {
        if (running) {
            checkDeath()
            move()
            deleteOldStrips()
            if (counter > 90) {
                spawnStrip()
                counter = 0
                score++
            } else counter++
        }
        repaint()
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponents(g)
        if (running) {
            g.color = Color.DARK_GRAY
            g.fillRect(0, 0, 800, 600)
            g.drawImage(cube, cubeX, cubeY, this)
            for (s in strips) g.drawImage(strip, s.x, s.y, this)
            g.color = Color.blue
            g.font = Font("Arial", Font.BOLD, 20)
            g.drawString("Score ${(score + abs(score)) / 2}", 20, 20)
            Toolkit.getDefaultToolkit().sync()
        } else {
            println("Game Over")
            timer.stop()
            val font = Font("Arial", Font.BOLD, 42)
            g.font = font
            g.color = Color.GREEN
            val metrics = getFontMetrics(font)
            g.drawString("Game Over", 400 - metrics.stringWidth("Game Over") / 2, 250)
        }
    }
}