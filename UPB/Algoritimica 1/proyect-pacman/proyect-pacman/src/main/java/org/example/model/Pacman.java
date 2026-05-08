package org.example.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pacman extends Figura {

    public Pacman(int x, int y, int width, int timeAdvance) {
        super(x, y, width, timeAdvance, 100);
        this.dirAdvance = DireccionEnum.DERECHA;
    }
}