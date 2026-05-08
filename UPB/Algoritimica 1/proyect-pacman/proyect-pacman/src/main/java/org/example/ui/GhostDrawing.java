package org.example.ui;

import lombok.Getter;
import org.example.model.Enemigo;

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

    private Enemigo enemigo;  // <- referencia al modelo

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

    private BufferedImage biCongelado;    // fantasma azul (congelado)
    private BufferedImage biAdvertencia;  // fantasma parpadeante (por volver)
    private boolean parpadeoVisible = true;
    private long prevParpadeo;
    private long intervalParpadeo = 200;

    public GhostDrawing(Enemigo enemigo) {
        id = UUID.randomUUID().toString();
        this.enemigo = enemigo;
        this.x = enemigo.getX();
        this.y = enemigo.getY();
        setBounds(x, y, WIDTH, WIDTH);
        setOpaque(false);

        String base = "Fantasmas/" + enemigo.getColor() + "-";
        biDerecha   = BufferedImageUtil.readImage(base + "derecha.png",   getClass());
        biIzquierda = BufferedImageUtil.readImage(base + "izquierda.png", getClass());
        biArriba    = BufferedImageUtil.readImage(base + "arriba.png",    getClass());
        biAbajo     = BufferedImageUtil.readImage(base + "abajo.png",     getClass());

        biCongelado   = BufferedImageUtil.readImage("Fantasmas/fantasma-empieza.png", getClass());
        biAdvertencia = BufferedImageUtil.readImage("Fantasmas/fantasma-termina.png", getClass());

        actualizarImagen();
    }

    public void updateLocation() {
        setBounds(enemigo.getX(), enemigo.getY(), WIDTH, WIDTH);
        actualizarImagen();
    }
    public void paintComponent(Graphics g, int x, int y) {
        if (bi == null) return;
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
        int sx2 = sx1 + frameWidth;

        g2d.drawImage(bi, x, y, x + WIDTH, y + WIDTH, sx1, 0, sx2, frameHeight, null);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (bi == null) return;
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

    public void actualizarImagenPublic() {
        actualizarImagen();
    }

    private void actualizarImagen() {
        if (enemigo.isCongelado()) {
            long tiempoRestante = enemigo.getDuracionCong()
                    - (System.currentTimeMillis() - enemigo.getTiempoCongelado());

            if (tiempoRestante <= enemigo.getTiempoAdvertencia()) {
                // Modo advertencia: parpadeo entre congelado y advertencia
                long now = System.currentTimeMillis();
                if (now - prevParpadeo > intervalParpadeo) {
                    parpadeoVisible = !parpadeoVisible;
                    prevParpadeo = now;
                }
                bi = parpadeoVisible ? biCongelado : biAdvertencia;
            } else {
                bi = biCongelado;
            }
            return;
        }

        // Estado normal
        if (enemigo.getDx() > 0) bi = biDerecha;
        if (enemigo.getDx() < 0) bi = biIzquierda;
        if (enemigo.getDy() < 0) bi = biArriba;
        if (enemigo.getDy() > 0) bi = biAbajo;
    }
}