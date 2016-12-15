package com.energyxxer.cbe.util;

import com.energyxxer.cbe.global.Commons;
import com.energyxxer.cbe.main.Window;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.imageio.ImageIO;

/**
 * Loads images and remembers them.
 */
public class ImageManager {
	public static HashMap<String, BufferedImage> loadedImages = new HashMap<String, BufferedImage>();

	static {
		BufferedImage nullTexture = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
		Graphics2D gr = nullTexture.createGraphics();
		gr.setColor(new Color(0, 120, 255));
		gr.fillRect(0, 0, 2, 2);
		gr.setColor(Color.BLACK);
		gr.fillRect(1, 0, 1, 1);
		gr.fillRect(0, 1, 1, 1);
		loadedImages.put("null", nullTexture);
	}

	public static void clear() {
		loadedImages.clear();
	}

	public static BufferedImage load(String path) {
		if (!loadedImages.containsKey(path)) {
			try {
				InputStream is = Class.class.getResourceAsStream(path);
				if (is != null) {
					loadedImages.put(path, ImageIO.read(is));
				} else {
					System.err.println("<span color=\"" + ColorUtil.toCSS(Commons.warningColor) + "\">[WARN] File \"" + path + "\" not found.</span>");
				}

			} catch (IOException e) {
				System.err.println("<span color=\"" + ColorUtil.toCSS(Commons.warningColor) + "\">[WARN] File \"" + path + "\" not found.</span>");
			}
		}
		if (loadedImages.get(path) != null) {
			return loadedImages.get(path);
		} else {
			return loadedImages.get("null");
		}
	}
}
