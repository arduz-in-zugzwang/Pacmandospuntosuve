package org.example.model;

import lombok.Getter;

@Getter
public class Comida {
    private int x, y, width;
    private boolean esPremioMayor;

    public Comida(int x, int y, int width, boolean esPremioMayor) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.esPremioMayor = esPremioMayor;
    }
}