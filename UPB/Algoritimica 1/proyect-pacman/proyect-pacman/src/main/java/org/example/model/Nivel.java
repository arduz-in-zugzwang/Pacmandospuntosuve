package org.example.model;

import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Nivel {
    private int nroNivel;
    private int totalPuntos;
    private int puntosAcum;
    private int[][] m;
    private List<Bloque> bloques = new ArrayList<>();
    private List<Comida> comidas = new ArrayList<>();
    private List<Enemigo> enemigos = new ArrayList<>();
    private Pacman pacman;

    public Nivel() {
    }
}