package org.example.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Enemigo {
    private int x;
    private int y;
    private int dx;
    private int dy;
    private long prevTimeAdvance;
    private int timeAdvance = 16; // vel. ajustable, menor = más rápido
    private String color;
    private boolean persiguiendo = false;
    private int celdaDestX;  // celda destino X
    private int celdaDestY;  // celda destino Y
    private boolean iniciado = false;

    private boolean congelado = false;
    private long tiempoCongelado = 0;   // cuando se congeló
    private long duracionCong = 8000;   // 8 segundos congelado
    private long tiempoAdvertencia = 3000; // últimos 3s parpadea

    public Enemigo(int x, int y, int dx, int dy, String color) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.color = color;
    }
}