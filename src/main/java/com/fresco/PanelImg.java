package com.fresco;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JComponent;

public class PanelImg extends JComponent {

	private static final long serialVersionUID = 1L;
	private final static int FPS = 60;
	private final static int TARGET_TIME = 1_000_000_000 / FPS;
	private Graphics2D g2;
	private BufferedImage image;
	private int widthPanel;
	private int heightPanel;
	private boolean start = true;
	private Random random;

	public void start() {
		random = new Random();
		widthPanel = getWidth();
		heightPanel = getHeight();
		image = new BufferedImage(widthPanel, heightPanel, BufferedImage.TYPE_INT_ARGB);
		g2 = image.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		var thread = new Thread(() -> {
			while (start) {
				long startTime = System.nanoTime();
				drawBackGround();
				drawImg();
				render();
				long endTime = System.nanoTime();
				long time = endTime - startTime;
				if (time < TARGET_TIME) {
					long sleep = (TARGET_TIME - time) / 1_000_000;
					sleep(sleep);
				}
			}
		});
		thread.start();
	}

	private void drawImg() {
		int min = 0;
		int max = 255;
		int i, j;
		int r, g, b;
		
		for (i = 0; i < widthPanel; i++) {
			for (j = 0; j < heightPanel; j++) {
				r = random.nextInt(max + min) + min;
				g = random.nextInt(max + min) + min;
				b = random.nextInt(max + min) + min;
				Color color = new Color(r, g, b);
				g2.setColor(color);
				image.setRGB(i, j, color.getRGB());
			}
		}
	}

	private void drawBackGround() {
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, widthPanel, heightPanel);
	}

	private void render() {
		var g = getGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
	}

	private void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException ex) {
			System.out.println(ex);
		}
	}
}
