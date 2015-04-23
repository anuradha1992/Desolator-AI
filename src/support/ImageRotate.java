/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package support;

import java.applet.Applet;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;

/**
 *
 * @author ruchiranga
 */
public class ImageRotate extends Applet {

    Image img = null;
    Image rot = null;
    int imgHeight;
    int imgWidth;

    public Image rotate(Image src) {

        BufferedImage bi = ImageConverter.imageToBufferedImage(src);
        imgHeight = bi.getHeight();
        imgWidth = bi.getWidth();
        int buffer[] = new int[imgHeight * imgWidth];
        int rotate[] = new int[imgHeight * imgWidth];
        try {
            MediaTracker tracker = new MediaTracker(this);
            img = src;
            tracker.addImage(img, 0);
            tracker.waitForAll();
            PixelGrabber grabber = new PixelGrabber(img, 0, 0, imgWidth, imgHeight, buffer, 0, Math.max(imgHeight, imgWidth));
            try {
                grabber.grabPixels();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int y = 0; y < imgHeight; y++) {
                for (int x = 0; x < imgWidth; x++) {
                    rotate[((imgWidth - x - 1) * imgWidth) + y] = buffer[(y * imgHeight) + x];
                }
            }
            rot = createImage(new MemoryImageSource( imgWidth, imgHeight, rotate, 0, Math.max(imgHeight, imgWidth)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rot;
    }

}
