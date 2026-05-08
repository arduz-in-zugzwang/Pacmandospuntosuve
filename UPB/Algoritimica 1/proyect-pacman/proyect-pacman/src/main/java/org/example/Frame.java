package org.example;

import org.example.logic.IJuego;
import org.example.logic.Juego;
import org.example.model.Bloque;
import org.example.model.Comida;
import org.example.model.DireccionEnum;
import org.example.model.Pacman;
import org.example.model.Enemigo;
import java.util.List;
import org.example.ui.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Frame extends JFrame implements IJuego {

    private Juego juego;
    private PacmanDrawing pacmanDrawing;
    private Timer gameLoop;
    private List<GhostDrawing> ghostDrawings = new ArrayList<>();
    private Timer timerJuego;
    private JLabel lblTimer;
    private int segundosRestantes;
    private static final int TIEMPO_POR_NIVEL = 180; // 3 minutos
    private GamePanel panel;
    JLabel lblScore;
    private JPanel panelHUD; // <- campo nuevo

    private class GamePanel extends JPanel {
        public GamePanel() {
            setLayout(null);
            setBackground(new Color(178, 230, 198));
            setDoubleBuffered(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (GhostDrawing gd : ghostDrawings) {
                gd.paintComponent(g, gd.getEnemigo().getX(), gd.getEnemigo().getY());
            }
        }
    }

    public Frame() throws HeadlessException {
        setTitle("Pacman");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new GamePanel();
        setContentPane(panel);

        // HUD
        panelHUD = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        panelHUD.setBackground(new Color(69,169,101, 120));
        //69,169,101
        panelHUD.setOpaque(true);
        panelHUD.setBounds(0, 0, 500, 35);

        lblScore = new JLabel("Score: 0");
        lblScore.setForeground(Color.WHITE);
        lblScore.setFont(new Font("Comic Sans", Font.BOLD, 20));
        lblScore.setOpaque(false);

        lblTimer = new JLabel("Tiempo: 3:00");
        lblTimer.setForeground(Color.WHITE);
        lblTimer.setFont(new Font("Comic Sans", Font.BOLD, 20));
        lblTimer.setOpaque(false);

        panelHUD.add(lblScore);
        panelHUD.add(lblTimer);
        panel.add(panelHUD);

        setVisible(true);
        juego = new Juego(this);

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_RIGHT: case KeyEvent.VK_D:
                        juego.getNivelActual().getPacman().setDirAdvance(DireccionEnum.DERECHA);
                        pacmanDrawing.setDireccion(1, 0);
                        break;
                    case KeyEvent.VK_LEFT: case KeyEvent.VK_A:
                        juego.getNivelActual().getPacman().setDirAdvance(DireccionEnum.IZQUIERDA);
                        pacmanDrawing.setDireccion(-1, 0);
                        break;
                    case KeyEvent.VK_UP: case KeyEvent.VK_W:
                        juego.getNivelActual().getPacman().setDirAdvance(DireccionEnum.ARRIBA);
                        pacmanDrawing.setDireccion(0, -1);
                        break;
                    case KeyEvent.VK_DOWN: case KeyEvent.VK_S:
                        juego.getNivelActual().getPacman().setDirAdvance(DireccionEnum.ABAJO);
                        pacmanDrawing.setDireccion(0, 1);
                        break;
                }
            }
        });
        setFocusable(true);
        requestFocus();

        gameLoop = new Timer(16, e -> {
            juego.avanzarPacman();
            juego.moverEnemigos();
        });
        gameLoop.start();
        iniciarTimer();
    }

    private void iniciarTimer() {
        if (timerJuego != null) timerJuego.stop();
        segundosRestantes = TIEMPO_POR_NIVEL;
        timerJuego = new Timer(1000, e -> { //ms
            segundosRestantes--;
            int min = segundosRestantes / 60;
            int seg = segundosRestantes % 60;
            lblTimer.setText(String.format("Tiempo: %d:%02d", min, seg));
            if (segundosRestantes <= 0) {
                timerJuego.stop();
                gameOverConMensaje("La has liado");
            }
        });
        timerJuego.start();
    }

    @Override
    public void drawBloque(Bloque bloque) {
        WallDrawing wd = new WallDrawing(bloque.getX(), bloque.getY(), bloque.getTipo());
        panel.add(wd);
        panel.repaint();
    }

    @Override
    public void drawPacman(Pacman pacman) {
        pacmanDrawing = new PacmanDrawing(pacman);
        panel.add(pacmanDrawing);
        panel.repaint();
    }

    @Override
    public void updateLocationPacman() {
        pacmanDrawing.updateLocation();
    }

    @Override
    public void removeComida(Comida comida) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof DotDrawing dot && dot.getComida() == comida) {
                panel.remove(dot);
                panel.repaint();
                return;
            }
            if (comp instanceof FruitDrawing fruit && fruit.getComida() == comida) {
                panel.remove(fruit);
                panel.repaint();
                return;
            }
        }
    }

    @Override
    public void drawComida(Comida comida) {
        if (comida.isEsPremioMayor()) {
            FruitDrawing fruit = new FruitDrawing(
                    comida.getX(), comida.getY(),
                    juego.getNivelActual().getNroNivel(),
                    comida, getClass().getClassLoader()
            );
            panel.add(fruit);
            panel.setComponentZOrder(fruit, 0);
        } else {
            DotDrawing dot = new DotDrawing(comida);
            panel.add(dot);
            panel.setComponentZOrder(dot, panel.getComponentCount() - 1);
        }
        panel.repaint();
    }

    @Override
    public void updateScore(int puntaje) {
        lblScore.setText("Score: " + puntaje);
    }

    @Override
    public void clearUI() {
        panel.removeAll();
        panel.add(panelHUD);
        ghostDrawings.clear();
        panel.repaint();
        iniciarTimer();
    }

    @Override
    public void mostrarVictoria() {
        gameLoop.stop();
        if (timerJuego != null) timerJuego.stop();
        JOptionPane.showMessageDialog(this, "¡Ganaste! Completaste todos los niveles.",
                "Victoria", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void gameOver() {
        gameOverConMensaje("¡Game Over! Un fantasma te atrapó.");
    }

    private void gameOverConMensaje(String mensaje) {
        gameLoop.stop();
        if (timerJuego != null) timerJuego.stop();
        JOptionPane.showMessageDialog(this, mensaje, "Game Over", JOptionPane.ERROR_MESSAGE);
        System.exit(0);
    }

    @Override
    public void drawEnemigo(Enemigo enemigo) {
        GhostDrawing gd = new GhostDrawing(enemigo);
        ghostDrawings.add(gd);
        // sin add al panel
    }

    @Override
    public void updateEnemigos() {
        for (GhostDrawing gd : ghostDrawings) {
            gd.actualizarImagenPublic();
        }
        panel.repaint(); // GamePanel.paintComponent pinta los fantasmas
    }

    // SIN override de paint

    public static void main(String[] args) {
        new Frame();
        System.out.println("Frame creado :D");
    }
}