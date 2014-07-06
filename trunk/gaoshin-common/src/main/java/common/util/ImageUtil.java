/**
 * Copyright (c) 2011 SORMA
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Author: sorma@gaoshin.com
 */
package common.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

public class ImageUtil {
    public static void resize(InputStream in, OutputStream out, int width, int height) throws IOException {
        BufferedImage img = ImageIO.read(in);
        int w = img.getWidth();
        int h = img.getHeight();

        int newWidth = 0;
        int newHeight = 0;

        if (width <= 0) {
            float ratio = (float) height / (float) h;
            newWidth = (int) ((float) w * ratio);
            newHeight = (int) ((float) h * ratio);

        } else if (height <= 0) {
            float ratio = (float) width / (float) w;
            newWidth = (int) ((float) w * ratio);
            newHeight = (int) ((float) h * ratio);

        } else {
            float wratio = (float) width / (float) w;
            float hratio = (float) height / (float) h;
            newWidth = (int) (w * wratio);
            newHeight = (int) (h * hratio);
        }

        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(img, 0, 0, newWidth, newHeight, null);
        g.dispose();

        ImageIO.write(resizedImage, "png", out);
    }

}
