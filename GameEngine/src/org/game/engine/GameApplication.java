
package org.game.engine;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class GameApplication {
	public static JFrame frame;

	public static void start(final Game game) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				frame = new JFrame(game.getTitle());
				frame.setSize(game.getWidth(), game.getHeight());
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				GameCanvas canvas = new GameCanvas();
				canvas.setGame(game);
				frame.add(canvas);
				frame.setVisible(true);
				canvas.requestFocus();
				new GameLoop(game, canvas).start();
			}
		});
	}
}
