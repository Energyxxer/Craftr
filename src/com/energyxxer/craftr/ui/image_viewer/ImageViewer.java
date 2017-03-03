package com.energyxxer.craftr.ui.image_viewer;

import com.energyxxer.craftr.global.Console;
import com.energyxxer.craftr.main.window.Window;
import com.energyxxer.craftr.ui.Tab;
import com.energyxxer.craftr.ui.display.DisplayModule;
import com.energyxxer.craftr.ui.theme.change.ThemeChangeListener;
import com.energyxxer.craftr.util.ImageManager;
import com.energyxxer.craftr.util.MathUtil;
import com.energyxxer.craftr.util.StringUtil;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by User on 2/8/2017.
 */
public class ImageViewer extends JPanel implements DisplayModule, MouseWheelListener, MouseMotionListener {

    private static final double MIN_SCALE = 0.1;
    private static final double MAX_SCALE = 50;

    private BufferedImage img;
    private double scale = 1;

    private static final int CHECK_PATTERN_SIZE = 5;

    private int offsetX = 0;
    private int offsetY = 0;
    private Point cursorPoint = new Point(0,0);
    private Point posOnImage = new Point(0,0);

    private Dimension imgSize;

    private boolean initialized = false;
    private boolean crosshairVisible = false;

    private Color crosshairColor = new Color(0, 0, 0, 64);

    public ImageViewer(Tab tab) {
        try {
            this.img = ImageIO.read(new File(tab.path));
        } catch(IOException x) {
            this.img = ImageManager.load("null");
        }
        this.imgSize = new Dimension(img.getWidth(), img.getHeight());
        this.addMouseWheelListener(this);
        this.addMouseMotionListener(this);
        ThemeChangeListener.addThemeChangeListener(t -> {
            this.setBackground(t.getColor("ImageViewer.background",Color.WHITE));
            this.crosshairColor = t.getColor("ImageViewer.crosshair",new Color(0,0,0,64));
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        if(scale == 0) scale = 1;
        g.setColor(this.getBackground());
        g.fillRect(0,0,this.getWidth(), this.getHeight());

        if(!initialized) {
            this.adjustToMiddle();
            initialized = true;
        }

        Rectangle rect = centerDimension();
        Shape originalBounds = g.getClipBounds();
        g.setClip(rect);

        g.setColor(Color.WHITE);
        g.fillRect(rect.x, rect.y, rect.width, rect.height);
        g.setColor(Color.GRAY);

        for(int x = rect.x; x < rect.x + rect.width; x += CHECK_PATTERN_SIZE * 2) {
            for(int y = rect.y; y < rect.y + rect.height; y += CHECK_PATTERN_SIZE * 2) {
                g.fillRect(x + CHECK_PATTERN_SIZE, y, CHECK_PATTERN_SIZE, CHECK_PATTERN_SIZE);
                g.fillRect(x, y + CHECK_PATTERN_SIZE, CHECK_PATTERN_SIZE, CHECK_PATTERN_SIZE);
            }
        }

        g.drawImage(img, rect.x, rect.y, rect.width, rect.height, null);

        g.setClip(originalBounds);

        if(crosshairVisible) {
            g.setColor(this.crosshairColor);

            Point pixelOnCursor = this.getPositionOnScreen(new Point.Double(posOnImage.x, posOnImage.y));
            g.fillRect(pixelOnCursor.x - 1, 0, 2, this.getHeight());
            g.fillRect(0, pixelOnCursor.y - 1, this.getWidth(), 2);
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int rotation = e.getWheelRotation();
        if(rotation == 0) return;
        rotation = Math.max(-4,Math.min(4,rotation));

        double newScale = scale * ((rotation < 0) ? 1.25 : 0.8);
        if(newScale >= MIN_SCALE && newScale <= MAX_SCALE) {

            Point.Double posOnImage = this.getPositionOnImage(cursorPoint, scale);
            Point.Double newPosOnImage = this.getPositionOnImage(cursorPoint, newScale);

            this.offsetX += (newPosOnImage.x - posOnImage.x) * newScale;
            this.offsetY += (newPosOnImage.y - posOnImage.y) * newScale;

            scale = newScale;
        }
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        this.offsetX += e.getX() - cursorPoint.getX();
        this.offsetY += e.getY() - cursorPoint.getY();
        this.cursorPoint = e.getPoint();
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        this.cursorPoint = e.getPoint();
        Point.Double fullPosOnImage = this.getPositionOnImage();
        this.posOnImage = new Point((int) Math.round(fullPosOnImage.x), (int) Math.round(fullPosOnImage.y));
        this.crosshairVisible = e.isControlDown();
        displayCaretInfo();
        repaint();
    }

    @Override
    public void displayCaretInfo() {
        Window.statusBar.setCaretInfo("UV pos: " + StringUtil.stripDecimals(MathUtil.truncateDecimals((double) (posOnImage.x  * 16) / imgSize.width, 4)) + ", " + StringUtil.stripDecimals(MathUtil.truncateDecimals(((double) (posOnImage.y  * 16) / imgSize.height),4)));
        Window.statusBar.setSelectionInfo("Pixel pos: " + posOnImage.x + ", " + posOnImage.y);
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public boolean canSave() {
        return false;
    }

    @Override
    public void focus() {
        this.requestFocus();
    }

    private void adjustToMiddle() {
        Dimension middlePanelSize = new Dimension(this.getWidth()/2, this.getHeight()/2);
        Dimension middleImageSize = getScaledDimension(imgSize, middlePanelSize);
        if(middleImageSize.width == 0) {
            Console.warn.println("Image couldn't be adjusted.");
            return;
        }

        scale = (double) middleImageSize.width / imgSize.width;
    }

    private Point.Double getPositionOnImage() {
        return getPositionOnImage(cursorPoint, scale);
    }

    private Point.Double getPositionOnImage(Point p, double forScale) {
        Point center = this.getCenterPoint();

        return new Point.Double(
                ((p.x - (center.x - ((imgSize.width * forScale) / 2)) - offsetX) / forScale),
                ((p.y - (center.y - ((imgSize.height * forScale) / 2)) - offsetY) / forScale)
        );
    }

    private Point getPositionOnScreen(Point.Double p) {
        Point center = this.getCenterPoint();

        return new Point(
                (int) (p.x * scale) + (center.x - ((int) (imgSize.width * scale)/2)) + offsetX,
                (int) (p.y * scale) + (center.y - ((int) (imgSize.height * scale)/2)) + offsetY
        );
    }

    private Point getCenterPoint() {
        return new Point(this.getWidth()/2, this.getHeight()/2);
    }

    private Rectangle centerDimension() {
        Dimension dim = new Dimension(imgSize);
        dim.width *= scale;
        dim.height *= scale;
        Dimension bounds = new Dimension(this.getWidth(), this.getHeight());
        return new Rectangle(
                (bounds.width/2)-(dim.width/2) + offsetX,
                (bounds.height/2)-(dim.height/2) + offsetY,
                dim.width,
                dim.height
        );
    }

    private static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {

        int original_width = imgSize.width;
        int original_height = imgSize.height;
        int bound_width = boundary.width;
        int bound_height = boundary.height;
        int new_width = original_width;
        int new_height = original_height;

        // first check if we need to scale width
        if (original_width > bound_width) {
            //scale width to fit
            new_width = bound_width;
            //scale height to maintain aspect ratio
            new_height = (new_width * original_height) / original_width;
        }

        // then check if we need to scale even with the new height
        if (new_height > bound_height) {
            //scale height to fit instead
            new_height = bound_height;
            //scale width to maintain aspect ratio
            new_width = (new_height * original_width) / original_height;
        }

        float aspect_ratio = (float) new_width / new_height;
        float bound_ratio = (float) bound_width / bound_height;

        if(aspect_ratio > bound_ratio) {
            new_width = bound_width;
            new_height = (new_width * original_height) / original_width;
        } else {
            new_height = bound_height;
            new_width = (new_height * original_width) / original_height;
        }
        new_width = (int) Math.floor(new_width);
        new_height = (int) Math.floor(new_height);
        return new Dimension(new_width, new_height);
    }


}
