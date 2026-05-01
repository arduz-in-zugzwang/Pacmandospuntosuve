package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.UUID;

public class DotDrawing extends JComponent {
    private String id;
    public static final int WIDTH = 50; //crear constante
    private int x;
    private int y;

    private BufferedImage bi;

    public DotDrawing(int x, int y) {
        id = UUID.randomUUID().toString();
        this.x = x;
        this.y = y;
        setBounds(x, y, WIDTH, WIDTH); // obligatorio

//        bi = BufferedImageUtil.readImage("wall.jpg", getClass());
    }

    @Override
//    public void paintComponent(Graphics g) {
//        super.paintComponent(g);
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

//        g2d.drawImage(bi, 0, 0, WIDTH, WIDTH, this);
        g2d.setColor(Color.darkGray);
        g2d.fillOval(20, 20, 10, 10); // circulito en el centro
    }
}
