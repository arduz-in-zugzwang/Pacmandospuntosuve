// src/main/java/org/example/model/Copito.java
package org.example.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Copito {
    private int x;
    private int y;
    private boolean visible = false;

    public Copito(int x, int y) {
        this.x = x;
        this.y = y;
    }
}