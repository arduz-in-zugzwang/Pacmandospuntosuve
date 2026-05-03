package org.example;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.UUID;

@Getter
public class GhostDrawing extends JComponent {
    private String id;
    public static final int WIDTH = 50;
    private int x;
    private int y;
    private int dx;
    private int dy;

    private BufferedImage biDerecha;
    private BufferedImage biIzquierda;
    private BufferedImage biArriba;
    private BufferedImage biAbajo;
    private BufferedImage bi;

    private long prevTime;
    private long changeFrameTime = 200;
    int totalFrames = 2;
    int nroColumnas = 2;
    int nroFilas = 1;
    int frame = 0;

    // color: "Amarillo", "Cyan", "Rojo", "Rosa"
    public GhostDrawing(int x, int y, int dx, int dy, String color) {
        id = UUID.randomUUID().toString();
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        setBounds(x, y, WIDTH, WIDTH);
        setOpaque(false);

        String base = "Fantasmas/" + color + "-";
        biDerecha   = BufferedImageUtil.readImage(base + "derecha.png",   getClass());
        biIzquierda = BufferedImageUtil.readImage(base + "izquierda.png", getClass());
        biArriba    = BufferedImageUtil.readImage(base + "arriba.png",    getClass());
        biAbajo     = BufferedImageUtil.readImage(base + "abajo.png",     getClass());

        // imagen inicial según dirección
        actualizarImagen();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        long currentTime = System.currentTimeMillis();
        if (currentTime - prevTime > changeFrameTime) {
            frame = (frame + 1) % totalFrames;
            prevTime = currentTime;
        }

        int frameWidth  = bi.getWidth()  / nroColumnas;
        int frameHeight = bi.getHeight() / nroFilas;
        int c   = frame % nroColumnas;
        int sx1 = c * frameWidth;
        int sy1 = 0;
        int sx2 = sx1 + frameWidth;
        int sy2 = frameHeight;

        g2d.drawImage(bi, 0, 0, WIDTH, WIDTH, sx1, sy1, sx2, sy2, this);
    }

    public void move(int[][] m) {
        int nextX = x + dx;
        int nextY = y + dy;

        int tolerance = 4;
        int maxF = m.length - 1;
        int maxC = m[0].length - 1;

        int celdaX1 = Math.max(0, Math.min((nextX + tolerance)           / WallDrawing.WIDTH, maxC));
        int celdaX2 = Math.max(0, Math.min((nextX + WIDTH - 1 - tolerance) / WallDrawing.WIDTH, maxC));
        int celdaY1 = Math.max(0, Math.min((nextY + tolerance)           / WallDrawing.WIDTH, maxF));
        int celdaY2 = Math.max(0, Math.min((nextY + WIDTH - 1 - tolerance) / WallDrawing.WIDTH, maxF));

        boolean choca = esWall(m[celdaY1][celdaX1]) || esWall(m[celdaY2][celdaX2])
                || esWall(m[celdaY1][celdaX2]) || esWall(m[celdaY2][celdaX1]);

        if (choca) {
            dx = -dx;
            dy = -dy;
        } else {
            x = nextX;
            y = nextY;
            setBounds(x, y, WIDTH, WIDTH);
        }

        actualizarImagen();
    }

    private void actualizarImagen() {
        if (dx > 0) bi = biDerecha;
        if (dx < 0) bi = biIzquierda;
        if (dy < 0) bi = biArriba;
        if (dy > 0) bi = biAbajo;
    }

    private boolean esWall(int celda) {
        return celda >= 1 && celda <= 6;
    }
}