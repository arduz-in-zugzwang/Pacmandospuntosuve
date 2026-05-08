package org.example.ui;

import org.example.model.Comida;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.UUID;

public class DotDrawing extends JComponent {
    private String id;
    public static final int WIDTH = 50;
    private Comida comida;  // <- referencia al modelo
    private BufferedImage bi;

    public DotDrawing(Comida comida) {
        this.comida = comida;
        id = UUID.randomUUID().toString();
        setBounds(comida.getX(), comida.getY(), WIDTH, WIDTH);
        bi = BufferedImageUtil.readImage("paredes/girasol2.png", getClass());
        setOpaque(false);
    }

    public Comida getComida() {
        return comida;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(bi, 0, 0, WIDTH, WIDTH, this);
    }
}