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
    int[][] m = {
            {3,1,1,1,1,1,1,1,1,1,1,1,1,1,4},
            {2,0,0,0,0,0,3,1,4,0,0,0,6,5,2},
            {2,0,0,0,0,0,5,1,6,0,0,0,4,3,2},
            {2,0,7,0,0,0,0,0,0,0,0,0,0,0,2},
            {2,0,0,3,1,4,0,0,0,0,3,1,4,0,2},
            {2,0,0,0,2,0,0,0,0,0,0,2,0,0,2},
            {2,0,0,0,2,0,0,1,1,0,0,2,0,0,2},
            {2,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
            {2,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
            {5,1,1,1,1,1,1,1,1,1,1,1,1,1,6},
    };

    int[][] m2 = {
            {3,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,4},
            {2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
            {2,0,3,1,4,0,0,0,0,7,0,0,0,0,0,0,2},
            {2,0,0,0,2,0,0,7,0,0,0,3,1,4,0,0,2},
            {2,0,0,0,5,1,4,0,0,0,0,0,2,2,0,0,2},
            {2,0,7,0,0,0,2,0,0,0,0,0,5,1,4,0,2},
            {2,0,0,0,0,0,2,0,7,0,3,0,0,0,2,0,2},
            {2,0,3,1,1,1,6,0,0,0,2,0,0,0,2,0,2},
            {2,0,0,0,0,0,0,0,0,0,5,1,0,0,6,0,2},
            {2,0,0,7,0,0,0,0,0,0,0,0,0,0,0,0,2},
            {5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,6},
    };

    // 17 columnas x 11 filas → 850x550px
    int[][] m3 = {
            {3,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,4},
            {2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
            {2,0,3,1,4,0,0,0,7,0,0,0,3,1,4,0,2},
            {2,0,2,0,0,0,7,0,0,0,7,0,0,0,2,0,2},
            {2,0,2,0,3,1,4,0,0,0,3,1,4,0,2,0,2},
            {2,0,5,0,2,0,0,0,0,0,0,0,2,0,6,0,2},
            {2,0,0,0,2,0,7,0,3,0,7,0,2,0,0,0,2},
            {2,0,3,0,5,1,4,0,2,0,3,1,6,0,4,0,2},
            {2,0,2,0,0,0,0,0,2,0,0,0,0,0,2,0,2},
            {2,0,5,1,1,1,4,0,5,0,3,1,1,1,6,0,2},
            {2,0,0,0,0,0,2,0,0,0,2,0,0,0,0,0,2},
            {2,0,7,0,0,0,5,1,1,1,6,0,0,0,7,0,2},
            {5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,6},
    };



    List<DotDrawing> puntos = new ArrayList<>();
    int puntaje = 0;

    PacmanDrawing pacman;
    int dx = 2;
    int dy = 0;

    List<GhostDrawing> fantasmas = new ArrayList<>();

    public Frame() throws HeadlessException {
        setTitle("Pacman");
        setSize(1000, 700);
//        getContentPane().setBackground(Color.BLACK);
        getContentPane().setBackground(new Color(178, 230, 198));
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

//        GhostDrawing f1 = new GhostDrawing(3 * WallDrawing.WIDTH, 1 * WallDrawing.WIDTH,  2,  0, "Amarillo");
//        GhostDrawing f2 = new GhostDrawing(8 * WallDrawing.WIDTH, 1 * WallDrawing.WIDTH, -2,  0, "Cyan");
//        GhostDrawing f3 = new GhostDrawing(3 * WallDrawing.WIDTH, 8 * WallDrawing.WIDTH,  2,  0, "Rojo");
//        GhostDrawing f4 = new GhostDrawing(8 * WallDrawing.WIDTH, 8 * WallDrawing.WIDTH,  0, -2, "Rosa");
//
//        for (GhostDrawing f : List.of(f1, f2, f3, f4)) {
//            fantasmas.add(f);
//            getContentPane().add(f);
//        }

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

        // en el Timer, reemplazá el movimiento actual por esto:
        Timer gameLoop = new Timer(16, e -> {
            int nextX = pacman.getX() + dx;
            int nextY = pacman.getY() + dy;

            // alineación a grilla al moverse horizontalmente
            if (dx != 0) {
                int resto = nextY % WallDrawing.WIDTH;
                if (resto != 0) {
                    // snap suave hacia la celda más cercana
                    nextY = (resto < WallDrawing.WIDTH / 2)
                            ? nextY - resto
                            : nextY + (WallDrawing.WIDTH - resto);
                }
            }

            // alineación a grilla al moverse verticalmente
            if (dy != 0) {
                int resto = nextX % WallDrawing.WIDTH;
                if (resto != 0) {
                    nextX = (resto < WallDrawing.WIDTH / 2)
                            ? nextX - resto
                            : nextX + (WallDrawing.WIDTH - resto);
                }
            }

            int tolerance = 2; // con snap, tolerancia mínima alcanza
            int maxF = m.length - 1;
            int maxC = m[0].length - 1;

            int celdaX1 = Math.max(0, Math.min((nextX + tolerance) / WallDrawing.WIDTH, maxC));
            int celdaX2 = Math.max(0, Math.min((nextX + PacmanDrawing.WIDTH - 1 - tolerance) / WallDrawing.WIDTH, maxC));
            int celdaY1 = Math.max(0, Math.min((nextY + tolerance) / WallDrawing.WIDTH, maxF));
            int celdaY2 = Math.max(0, Math.min((nextY + PacmanDrawing.WIDTH - 1 - tolerance) / WallDrawing.WIDTH, maxF));

            boolean choca = esWall(m[celdaY1][celdaX1]) || esWall(m[celdaY2][celdaX2])
                    || esWall(m[celdaY1][celdaX2]) || esWall(m[celdaY2][celdaX1]);

            if (!choca) {
                pacman.move(nextX - pacman.getX(), nextY - pacman.getY());
            }

            int celdaPacX = (pacman.getX() + PacmanDrawing.WIDTH / 2) / WallDrawing.WIDTH;
            int celdaPacY = (pacman.getY() + PacmanDrawing.WIDTH / 2) / WallDrawing.WIDTH;

            for (int i = 0; i < puntos.size(); i++) {
                DotDrawing dot = puntos.get(i);
                int celdaDotX = dot.getX() / WallDrawing.WIDTH;
                int celdaDotY = dot.getY() / WallDrawing.WIDTH;

                if (celdaPacX == celdaDotX && celdaPacY == celdaDotY) {
                    m[celdaDotY][celdaDotX] = -1;
                    getContentPane().remove(dot);
                    puntos.remove(i);
                    puntaje++;
                    break;
                }
            }
//            getContentPane().repaint();
//            for (GhostDrawing fantasma : fantasmas) {
//                fantasma.move(m);
//
//                boolean tocaPacman = Math.abs(fantasma.getX() - pacman.getX()) < GhostDrawing.WIDTH / 2
//                        && Math.abs(fantasma.getY() - pacman.getY()) < GhostDrawing.WIDTH / 2;
//                if (tocaPacman) {
//                    System.out.println("¡Game over!");
//                    // después acá va la pantalla de fin
//                }
//            }
        });
        gameLoop.start();
    }

    private boolean esWall(int celda) {
        return celda >= 1 && celda <= 7;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Puntaje: " + puntaje, 20, 20);
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
