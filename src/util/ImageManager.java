package util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class ImageManager {
	public static HashMap<String, BufferedImage> loadedImages = new HashMap<String, BufferedImage>();
	private static boolean initialized = false;
	
	public static void initialize() {
		BufferedImage nullTexture = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
		Graphics2D gr = nullTexture.createGraphics();
		gr.setColor(new Color(0,120,255));
		gr.fillRect(0, 0, 2, 2);
		gr.setColor(Color.BLACK);
		gr.fillRect(1, 0, 1, 1);
		gr.fillRect(0, 1, 1, 1);
		loadedImages.put("null", nullTexture);
		initialized = true;
	}
	
	public static void clear() {
		loadedImages.clear();
		initialize();
	}
	
	public static BufferedImage load(String path) {
		if(!initialized) {
			initialize();
		}
		if(!loadedImages.containsKey(path)) {
			try {
				if(Class.class.getResourceAsStream(path) != null) {
					loadedImages.put(path, ImageIO.read(Class.class.getResourceAsStream(path)));
				}
				
			} catch (IOException e) {
				System.err.println("[WARN] File \"" + path + "\" not found.");
			}
		}
		if(loadedImages.get(path) != null) {
			return loadedImages.get(path);
		} else {
			return loadedImages.get("null");
		}
	}
}
