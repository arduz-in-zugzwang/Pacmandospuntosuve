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

    int[][] m= {{1,1,1,1,1,1,1,1,1,1,1,1,1,1}
            ,{1,0,0,0,0,0,0,0,0,0,0,0,0,1}
            ,{1,0,0,0,0,0,0,0,0,0,0,0,0,1}
            ,{1,0,0,0,0,0,0,0,0,0,0,0,0,1}
//                ,{1,0,0,0,0,0,0,0,0,0,0,0,0,1}
            ,{0,0,0,0,0,0,0,0,0,0,0,0,0,1}
            ,{0,0,0,0,0,0,0,0,0,0,0,0,0,1}
            ,{0,0,0,0,0,0,0,0,0,0,0,0,0,1}
//                ,{0,0,0,0,0,0,0,0,0,0,0,0,0,0}
//                ,{0,0,0,0,0,0,0,0,0,0,0,0,0,0}
            ,{1,0,0,0,0,0,0,0,0,0,0,0,0,1}
            ,{1,0,0,0,0,0,0,0,0,0,0,0,0,1}
            ,{1,1,1,1,1,1,1,1,1,1,1,1,1,1}};

    PacmanDrawing pacman;
    int dx = 2;
    int dy = 0;

    public Frame() throws HeadlessException {
        setTitle("Pacman");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //getContentPane().setLayout(null);
        setVisible(true);

//        WallDrawing wd = new WallDrawing(0, 100); //el muro ese
//        getContentPane().add(wd); //panel, lienzo donde esta todo(?
//        repaint();
        //pintando con matrices para pintar mas en el lienzo
        /// Los bordes no ma


        for (int f = 0; f < m.length; f++) { //cuantas filas
            for (int c = 0; c < m[f].length; c++) { //cuantas columnas
                if (m[f][c] ==1){
                    WallDrawing wd = new WallDrawing(c* WallDrawing.WIDTH, f* WallDrawing.WIDTH); //el muro ese
                    getContentPane().add(wd);
                }
            }
        }

        pacman = new PacmanDrawing(0, 250);
        getContentPane().add(pacman);
        repaint();

        System.out.println("frame creado");
        getContentPane().repaint();

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_RIGHT: case KeyEvent.VK_D:
                        dx=2; dy=0;
                        pacman.setDireccion(dx, dy);
                        break;
                    case KeyEvent.VK_LEFT:  case KeyEvent.VK_A: dx=-2; dy=0;
                        pacman.setDireccion(dx, dy);
                        break;
                    case KeyEvent.VK_UP:    case KeyEvent.VK_W: dx=0;  dy=-2;
                        pacman.setDireccion(dx, dy);
                        break;
                    case KeyEvent.VK_DOWN:  case KeyEvent.VK_S: dx=0;  dy=2;
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

            // borde derecho si va a la derecha, izquierdo si va a la izquierda
            int bordeX = dx > 0 ? nextX + PacmanDrawing.WIDTH - 1 : nextX;
            // borde inferior si va abajo, superior si va arriba
            int bordeY = dy > 0 ? nextY + PacmanDrawing.WIDTH - 1 : nextY;

            int celdaX = bordeX / WallDrawing.WIDTH;
            int celdaY = bordeY / WallDrawing.WIDTH;

            if (m[celdaY][celdaX] != 1) {
                pacman.move(dx, dy);
            }
            getContentPane().repaint();
        });
        gameLoop.start();

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



    public static void main(String[] args) {
        Frame frame = new Frame();
    }
}
