package org.example.ui;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * BufferedImageUtil
 *
 * @author Marcos Quispe
 * @since 1.0
 */
public class BufferedImageUtil {

    public static BufferedImage readImage(String fileName, Class class1) {
        BufferedImage bi = null;

        InputStream inputStream = null;
        try {
            // si no reconoce la imagen, ejecutar mvn clean compile para que sea agregado a la carpeta target
            //inputStream = this.getClass().getClassLoader().getResourceAsStream("plants/sunflower.png"); // no funciona bien con webp
            inputStream = class1.getClassLoader().getResourceAsStream(fileName); // no funciona bien con webp
            bi = ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bi;
    }

    public static BufferedImage readImage(String fileName, ClassLoader cl) {
        BufferedImage bi = null;
        InputStream inputStream = null;
        try {
            System.out.println("Buscando: " + fileName);
            System.out.println("Stream resultado: " + cl.getResourceAsStream(fileName));
            inputStream = cl.getResourceAsStream(fileName);
            bi = ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try { inputStream.close(); } catch (IOException e) { e.printStackTrace(); }
            }
        }
        return bi;
    }
}