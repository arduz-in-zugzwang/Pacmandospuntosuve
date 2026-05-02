package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Frame
 *
 * @author Marcos Quispe
 * @since 1.0
 */
public class Frame extends JFrame {

    //nivel 1
    //nivel 1 - Mapa tradicional de Pacman
    //nivel 1 - Mapa simple y simétrico
    int[][] m = {
            {3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4},
            {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
            {2, 0, 3, 1, 4, 0, 0, 0, 0, 0, 3, 1, 4, 0, 2},
            {2, 0, 5, 1, 6, 0, 0, 0, 0, 0, 5, 1, 6, 0, 2},
            {2, 0, 0, 0, 0, 0, 3, 1, 4, 0, 0, 0, 0, 0, 2},
            {2, 0, 0, 0, 0, 0, 5, 1, 6, 0, 0, 0, 0, 0, 2},
            {2, 0, 3, 1, 4, 0, 0, 0, 0, 0, 3, 1, 4, 0, 2},
            {2, 0, 5, 1, 6, 0, 0, 0, 0, 0, 5, 1, 6, 0, 2},
            {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
            {5, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 6},
    };

    List<DotDrawing> puntos = new ArrayList<>();

    PacmanDrawing pacman;
    int dx = 2;
    int dy = 0;

    public Frame() throws HeadlessException {
        setTitle("Pacman");
        setSize(1000, 700);
        getContentPane().setBackground(Color.BLACK);
        getContentPane().setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        getContentPane().setLayout(null);
        setVisible(true);

//        WallDrawing wd = new WallDrawing(0, 100); //el muro ese
//        getContentPane().add(wd); //panel, lienzo donde esta todo(?
//        repaint();
        //pintando con matrices para pintar mas en el lienzo
        /// Los bordes no ma


        for (int f = 0; f < m.length; f++) {
            for (int c = 0; c < m[f].length; c++) {
                if (esWall(m[f][c])) {
                    WallDrawing wd = new WallDrawing(c * WallDrawing.WIDTH, f * WallDrawing.WIDTH, m[f][c]);
                    getContentPane().add(wd);
                }

                if (m[f][c] == 0) {
                    DotDrawing dot = new DotDrawing(c * WallDrawing.WIDTH, f * WallDrawing.WIDTH);
                    getContentPane().add(dot);
                    puntos.add(dot); // lo guardás en la lista
                }
            }
        }

        pacman = new PacmanDrawing(1 * WallDrawing.WIDTH, 1 * WallDrawing.WIDTH);
        getContentPane().add(pacman);
        repaint();

        System.out.println("frame creado");
        getContentPane().repaint();

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_RIGHT: case KeyEvent.VK_D:
                        dx = 2; dy = 0;
                        pacman.setDireccion(dx, dy);
                        break;
                    case KeyEvent.VK_LEFT: case KeyEvent.VK_A:
                        dx = -2; dy = 0;
                        pacman.setDireccion(dx, dy);
                        break;
                    case KeyEvent.VK_UP: case KeyEvent.VK_W:
                        dx = 0; dy = -2;
                        pacman.setDireccion(dx, dy);
                        break;
                    case KeyEvent.VK_DOWN: case KeyEvent.VK_S:
                        dx = 0; dy = 2;
                        pacman.setDireccion(dx, dy);
                        break;
                }
            }
        });
        setFocusable(true);
        requestFocus();

        Timer gameLoop = new Timer(16, e -> {
            int nextX = pacman.getX() + dx;
            int nextY = pacman.getY() + dy;

            int tolerance = 12;  //margen
            int checkX1 = nextX + tolerance;
            int checkY1 = nextY + tolerance;
            int checkX2 = nextX + PacmanDrawing.WIDTH - 1 - tolerance;
            int checkY2 = nextY + PacmanDrawing.WIDTH - 1 - tolerance;

            int celdaX1 = checkX1 / WallDrawing.WIDTH;
            int celdaX2 = checkX2 / WallDrawing.WIDTH;
            int celdaY1 = checkY1 / WallDrawing.WIDTH;
            int celdaY2 = checkY2 / WallDrawing.WIDTH;

            int maxF = m.length - 1;
            int maxC = m[0].length - 1;
            celdaX1 = Math.max(0, Math.min(celdaX1, maxC));
            celdaX2 = Math.max(0, Math.min(celdaX2, maxC));
            celdaY1 = Math.max(0, Math.min(celdaY1, maxF));
            celdaY2 = Math.max(0, Math.min(celdaY2, maxF));

            boolean choca = esWall(m[celdaY1][celdaX1]) || esWall(m[celdaY2][celdaX2])
                    || esWall(m[celdaY1][celdaX2]) || esWall(m[celdaY2][celdaX1]);

            if (!choca) {
                pacman.move(dx, dy);
            }

            // celda donde está el centro del pacman
            int celdaPacX = (pacman.getX() + PacmanDrawing.WIDTH / 2) / WallDrawing.WIDTH;
            int celdaPacY = (pacman.getY() + PacmanDrawing.WIDTH / 2) / WallDrawing.WIDTH;

// buscás en la lista si hay un punto en esa celda
            for (int i = 0; i < puntos.size(); i++) {
                DotDrawing dot = puntos.get(i);
                int celdaDotX = dot.getX() / WallDrawing.WIDTH;
                int celdaDotY = dot.getY() / WallDrawing.WIDTH;

                if (celdaPacX == celdaDotX && celdaPacY == celdaDotY) {
                    m[celdaDotY][celdaDotX] = -1; // ya no es punto
                    getContentPane().remove(dot); // lo quitás del panel
                    puntos.remove(i);            // lo quitás de la lista
                    break;
                }
            }

            getContentPane().repaint();
        });
        gameLoop.start();
    }

    private boolean esWall(int celda) {
        return celda >= 1 && celda <= 6;
    }

    public static void main(String[] args) {
        Frame frame = new Frame();
    }

//    public int getIndexImageDrawing(String id) {
//        for (int i = 0; i < getContentPane().getComponents().length; i++) {
//            if (getContentPane().getComponents()[i] instanceof BigShapeDrawing imgd) {
//                if (id.equals(imgd.getId())) {
//                    return i;
//                }
//            }
//        }
//        return -1;
//    }

//    @Override
//    public void delete(String id) {
//        for (int i = 0; i < getContentPane().getComponents().length; i++) {
//            if (getContentPane().getComponents()[i] instanceof ShotDrawing sd) {
//                if (sd.getId().equals(id)) {
//                    getContentPane().remove(getContentPane().getComponents()[i]);
//                    System.out.println("eliminado id: " + id);
//                    getContentPane().revalidate(); // Reorganiza los componentes quitando lo eliminado
//                    getContentPane().repaint();
//                    return;
//                }
//            }
//        }
//    }

}
