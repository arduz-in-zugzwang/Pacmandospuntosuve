package org.example.ui;

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
public class WallDrawing extends JComponent {
    private String id;
    public static final int WIDTH = 50; //crear constante
    private int x;
    private int y;

    private BufferedImage bi; // esto no cambia

    public WallDrawing(int x, int y, int tipo) {
        id = UUID.randomUUID().toString();
        this.x = x;
        this.y = y;
        setBounds(x, y, WIDTH, WIDTH);

        String[] imagenes = {
                "",
                "paredes/horizontal.png",
                "paredes/vertical.png",
                "paredes/esq-izq-sup.png",
                "paredes/esq-der-sup.png",
                "paredes/esq-izq-inf.png",
                "paredes/esq-der-inf.png",
                "paredes/bloque.png",
        };

        bi = BufferedImageUtil.readImage(imagenes[tipo], getClass());
    }

    @Override
//    public void paintComponent(Graphics g) {
//        super.paintComponent(g);
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(bi, 0, 0, WIDTH, WIDTH, this);
    }
}
