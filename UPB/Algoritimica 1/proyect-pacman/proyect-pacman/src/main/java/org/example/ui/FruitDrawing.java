package org.example.ui;

import lombok.Getter;
import org.example.model.Comida;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.UUID;

@Getter
public class FruitDrawing extends JComponent {

    private String id;
    public static final int WIDTH = 50;
    private int x;
    private int y;
    private Comida comida;
    private BufferedImage bi;


    public FruitDrawing(int x, int y, int nroNivel, Comida comida, ClassLoader classLoader) {
        id = UUID.randomUUID().toString();
        this.x = x;
        this.y = y;
        this.comida=comida;
        setBounds(x, y, WIDTH, WIDTH);


        String[] frutas = {
                "",
                "Frutas/cherry.png",   // nivel 1
                "Frutas/fresa.png", // nivel 2
                "Frutas/melocoton.png",     // nivel 3
        };

//        bi = BufferedImageUtil.readImage(frutas[nroNivel], getClass());
        bi = BufferedImageUtil.readImage(frutas[nroNivel], classLoader);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(bi, 0, 0, WIDTH, WIDTH, this);
    }

}
