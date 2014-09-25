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

object DesktopGameLauncher {
	def main(args: Array[String]): Unit = {
		if (args.size < 1) throw new GameException("Wrong amount of arguments! Use: gameClassName [debugMode]")

		val mirror = runtimeMirror(getClass.getClassLoader)

		val game = mirror.reflectModule(mirror.moduleSymbol(Class.forName(args(0)))).instance.asInstanceOf[Game]
		game.setMode(if (args.size > 1) args(1) else Game.DEFAULT_GAME_MODE)

		launch(game)
	}

	def launch(game: Game) {
		new JFrame {
			val player = new GamePlayer(game, null) //TODO: Agarrar el renderer
			add(player)

			setLocationRelativeTo(null)
			setLocation((Toolkit.getDefaultToolkit.getScreenSize - game.displaySize) / 2)

			setVisible(true)
			setResizable(false)
			pack
			setTitle(game.title)
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

			addWindowListener(new WindowListener {
				def windowActivated(e: WindowEvent) = player.resume
				def windowDeactivated(e: WindowEvent) = player.pause
				def windowOpened(e: WindowEvent) {}
				def windowIconified(e: WindowEvent) {}
				def windowDeiconified(e: WindowEvent) {}
				def windowClosing(e: WindowEvent) {}
				def windowClosed(e: WindowEvent) {}
			})
		}
	}
}
