package org.example.model;

import lombok.Getter;

@Getter
public class Bloque {
    private int x, y, width;
    private int tipo;  // 1-7, los mismos que usás en tu matriz
    private int f, c;  // fila y columna en la matriz

    public Bloque(int x, int y, int width, int tipo, int f, int c) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.tipo = tipo;
        this.f = f;
        this.c = c;
    }
}
