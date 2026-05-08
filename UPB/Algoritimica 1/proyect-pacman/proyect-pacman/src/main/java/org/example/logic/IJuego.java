package org.example.logic;

import org.example.model.*;

/**
 * IJuego
 *
 * @author Marcos Quispe
 * @since 1.0
 */
public interface IJuego {

    void drawBloque(Bloque bloque);

    void drawPacman(Pacman pacman);

    void updateLocationPacman();

    void drawComida(Comida comida);

    void removeComida(Comida comida);

    void updateScore(int puntaje);

    void clearUI();

    void mostrarVictoria();

    void drawEnemigo(Enemigo enemigo);

    void updateEnemigos();

    void gameOver();

    void drawCopito(Copito copito);
    void removeCopito();
}
