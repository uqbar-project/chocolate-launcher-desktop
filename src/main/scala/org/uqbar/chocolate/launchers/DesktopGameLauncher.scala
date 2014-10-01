package org.uqbar.chocolate.launchers

import java.awt.event.WindowEvent
import java.awt.event.WindowListener
import scala.reflect.runtime.universe.runtimeMirror
import org.uqbar.math.vectors._
import org.uqbar.chocolate.core.Game
import org.uqbar.chocolate.core.GamePlayer
import org.uqbar.chocolate.core.exceptions.GameException
import javax.swing.JFrame
import java.awt.Toolkit
import org.uqbar.chocolate.core.loaders.SimpleResourceLoader
import org.uqbar.cacao.awt.AWTResourceLoader
import org.uqbar.chocolate.core.loaders.CachedResourceLoader
import java.awt.Canvas
import org.uqbar.chocolate.core.GamePlayer
import java.awt.image.MemoryImageSource
import java.awt.Point
import org.uqbar.chocolate.core.reactions.events.adapters.KeyboardAdapter
import org.uqbar.chocolate.core.reactions.events.adapters.MouseAdapter
import org.uqbar.cacao.awt.AWTRenderer
import java.awt.Graphics2D
import org.uqbar.chocolate.core.loaders.ResourceLoader
import java.awt.image.BufferStrategy
import java.awt.Dimension

object DesktopGameLauncher {

	def main(args: Array[String]): Unit = {
		if (args.isEmpty) throw new GameException("Wrong amount of arguments! Use: gameClassName [executionMode]")

		ResourceLoader.setUp(new CachedResourceLoader(new SimpleResourceLoader(new AWTResourceLoader)))

		val mirror = runtimeMirror(getClass.getClassLoader)
		val game = mirror.reflectModule(mirror.moduleSymbol(Class.forName(args(0)))).instance.asInstanceOf[Game]
		game.setMode(if (args.isDefinedAt(1)) args(1) else Game.DEFAULT_GAME_MODE)

		val frame = new JFrame {
			val canvas = new Canvas {
				setFocusTraversalKeysEnabled(false)
				setFocusable(true)
				setIgnoreRepaint(true)

				setPreferredSize(game.displaySize)
				setMinimumSize(game.displaySize)
				setMaximumSize(game.displaySize)

				val eventAdapter = new KeyboardAdapter with MouseAdapter { val target = game }
				addMouseListener(eventAdapter)
				addMouseMotionListener(eventAdapter)
				addKeyListener(eventAdapter)

				val image = createImage(new MemoryImageSource(16, 16, new Array[Int](16 * 16), 0, 16))
				setCursor(Toolkit.getDefaultToolkit.createCustomCursor(image, new Point(0, 0), ""))
			}

			add(canvas)

			addWindowListener(new WindowListener {
				val player = new GamePlayer(game, new AWTRenderer(canvas))

				def windowActivated(e: WindowEvent) {
					canvas.createBufferStrategy(2)
					player.resume
				}
				def windowDeactivated(e: WindowEvent) = player.pause
				def windowOpened(e: WindowEvent) {}
				def windowIconified(e: WindowEvent) {}
				def windowDeiconified(e: WindowEvent) {}
				def windowClosing(e: WindowEvent) {}
				def windowClosed(e: WindowEvent) {}
			})

			pack
			setLocationRelativeTo(null)
			setLocation((Toolkit.getDefaultToolkit.getScreenSize - game.displaySize) / 2)
			setResizable(false)
			setTitle(game.title)

			setVisible(true)
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
		}
	}
}