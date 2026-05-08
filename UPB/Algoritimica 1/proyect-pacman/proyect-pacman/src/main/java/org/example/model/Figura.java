package org.example.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Figura {
    protected int x, y, width;
    protected DireccionEnum dirAdvance;
    protected int timeAdvance;
    protected int timeChFrame;
    protected long prevTimeAdvance;

    public Figura(int x, int y, int width, int timeAdvance, int timeChFrame) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.timeAdvance = timeAdvance;
        this.timeChFrame = timeChFrame;
    }
}