package org.example;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.UUID;

/**
 * ShapeDrawing
 *
 * @author Marcos Quispe
 * @since 1.0
 */
@Getter
public class PacmanDrawing extends JComponent {
    private String id;
    public static final int WIDTH = 50;
    private int x;
    private int y;
    //imagenes
    private BufferedImage biDerecha;
    private BufferedImage biIzquierda;
    private BufferedImage biArriba;
    private BufferedImage biAbajo;

    private BufferedImage bi;
    private long prevTime;
    private long changeFrameTime = 500; // ms, cada tiempo yo quiero que pase al cambiar de sprite
    int totalFrames = 2;
    int nroFilas = 1;
    int nroColumnas = 2;
    int frame = 0; //numero de cortes que voy a sacar

    public PacmanDrawing(int x, int y) {
        id = UUID.randomUUID().toString();
        this.x = x;
        this.y = y;
        setBounds(x, y, WIDTH, WIDTH); // obligatorio
        biDerecha   = BufferedImageUtil.readImage("pacman/pacaman-derecha.png", getClass());
        biIzquierda = BufferedImageUtil.readImage("pacman/pacman-izquierda.png", getClass());
        biArriba    = BufferedImageUtil.readImage("pacman/pacman-arriba.png", getClass());
        biAbajo     = BufferedImageUtil.readImage("pacman/pacman-abajo.png", getClass());
        bi = biDerecha; // por defecto

//        bi = BufferedImageUtil.readImage("pacman/pacman-e.png", getClass());
    }

    @Override
//    public void paintComponent(Graphics g) {
//        super.paintComponent(g);
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        long currentTime = System.currentTimeMillis();
        if (currentTime - prevTime > changeFrameTime) {
            frame = (frame + 1) % totalFrames;
            prevTime = currentTime;
        }

        int f = frame / nroColumnas;
        int c = frame % nroColumnas;

        int frameWidth = bi.getWidth() / nroColumnas;
        int frameHeight = bi.getHeight() / nroFilas;

        int sx1 = c * frameWidth;
        int sy1 = f * frameHeight;
        int sx2 = sx1 + frameWidth;
        int sy2 = sy1 + frameHeight;

        g.drawImage(bi, 0, 0, WIDTH, WIDTH
                , sx1, sy1, sx2, sy2, this);
    }
    //para moverse
    public void move(int dx, int dy) {
        x += dx;
        y += dy;
        setBounds(x, y, WIDTH, WIDTH);
    }

    public void setDireccion(int dx, int dy) {
        if (dx > 0) bi = biDerecha;
        if (dx < 0) bi = biIzquierda;
        if (dy < 0) bi = biArriba;
        if (dy > 0) bi = biAbajo;
    }
}
