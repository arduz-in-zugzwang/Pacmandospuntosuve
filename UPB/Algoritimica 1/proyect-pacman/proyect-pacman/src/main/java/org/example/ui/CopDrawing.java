package org.example.ui;

import org.example.model.Copito;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class CopDrawing extends JComponent {
    public static final int SIZE = 40;
    private final Copito copito;
    private final BufferedImage imagen;
    private Runnable onClickCallback;

    public CopDrawing(Copito copito) {
        this.copito = copito;
        this.imagen = BufferedImageUtil.readImage("copito.png", getClass());
        setBounds(copito.getX(), copito.getY(), SIZE, SIZE);
        setOpaque(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (onClickCallback != null) onClickCallback.run();
            }
        });
    }

    public void setOnClick(Runnable callback) {
        this.onClickCallback = callback;
    }

    public void updateLocation() {
        setBounds(copito.getX(), copito.getY(), SIZE, SIZE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagen != null) {
            g.drawImage(imagen, 0, 0, SIZE, SIZE, this);
        }
    }
}