package org.example.ui;

import lombok.Getter;
import org.example.model.DireccionEnum;
import org.example.model.Pacman;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.UUID;

@Getter
public class PacmanDrawing extends JComponent {
    private String id;
    private Pacman pacman;  // <- referencia al modelo

    private BufferedImage biDerecha;
    private BufferedImage biIzquierda;
    private BufferedImage biArriba;
    private BufferedImage biAbajo;
    private BufferedImage bi;

    private long prevTime;
    private long changeFrameTime = 250;
    int totalFrames = 2;
    int nroFilas = 1;
    int nroColumnas = 2;
    int frame = 0;

    public static final int WIDTH = 50;

    public PacmanDrawing(Pacman pacman) {
        this.pacman = pacman;
        id = UUID.randomUUID().toString();
        setBounds(pacman.getX(), pacman.getY(), pacman.getWidth(), pacman.getWidth());
        setOpaque(false);

        biDerecha   = BufferedImageUtil.readImage("Bubble/este.png", getClass());
        biIzquierda = BufferedImageUtil.readImage("Bubble/oeste.png", getClass());
        biArriba    = BufferedImageUtil.readImage("Bubble/norte.png", getClass());
        biAbajo     = BufferedImageUtil.readImage("Bubble/sur.png", getClass());
        bi = biDerecha;
    }

    public void updateLocation() {
        setLocation(pacman.getX(), pacman.getY());
        repaint();
    }

    public void setDireccion(int dx, int dy) {
        if (dx > 0) bi = biDerecha;
        if (dx < 0) bi = biIzquierda;
        if (dy < 0) bi = biArriba;
        if (dy > 0) bi = biAbajo;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        long currentTime = System.currentTimeMillis();
        if (currentTime - prevTime > changeFrameTime) {
            frame = (frame + 1) % totalFrames;
            prevTime = currentTime;
        }

        int f = frame / nroColumnas;
        int c = frame % nroColumnas;

        int frameWidth  = bi.getWidth()  / nroColumnas;
        int frameHeight = bi.getHeight() / nroFilas;

        int sx1 = c * frameWidth;
        int sy1 = f * frameHeight;
        int sx2 = sx1 + frameWidth;
        int sy2 = sy1 + frameHeight;

        g.drawImage(bi, 0, 0, pacman.getWidth(), pacman.getWidth(),
                sx1, sy1, sx2, sy2, this);
    }
}