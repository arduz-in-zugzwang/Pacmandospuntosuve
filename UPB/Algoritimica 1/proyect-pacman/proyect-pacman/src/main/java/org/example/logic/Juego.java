package org.example.logic;

import org.example.model.*;
import org.example.ui.CopDrawing;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Juego {

    private List<Nivel> niveles = new ArrayList<>();
    private Nivel nivelActual;
    private int posInitX = 0;
    private int posInitY = 0;
    public static final int FIGURA_WIDTH = 50;

    private Copito copito;
    private long prevSpawnCopito = System.currentTimeMillis();
    private final long INTERVALO_SPAWN = 20_000L;

    private IJuego iJuego;

    public Juego(IJuego iJuego) {
        this.iJuego = iJuego;

        niveles.add(getNivel1());
        niveles.add(getNivel2());
        niveles.add(getNivel3());

        nivelActual = niveles.get(0);
        createUINivel(nivelActual);

        copito = new Copito(0, 0);
    }

    public Nivel getNivelActual() {
        return nivelActual;
    }

    public void createUINivel(Nivel nivel) {
        for (Bloque bloque : nivel.getBloques()) {
            iJuego.drawBloque(bloque);
        }
        iJuego.drawPacman(nivel.getPacman());
        for (Comida comida : nivel.getComidas()) {
            iJuego.drawComida(comida);
        }
        for (Enemigo enemigo : nivel.getEnemigos()) {
            iJuego.drawEnemigo(enemigo);
        }
    }

    // ─── COPITO ───────────────────────────────────────────

    public void actualizarCopito() {
        long now = System.currentTimeMillis();
        if (!copito.isVisible() && now - prevSpawnCopito >= INTERVALO_SPAWN) {
            spawnCopito();
            prevSpawnCopito = now;
        }
    }

    private void spawnCopito() {
        Random rnd = new Random();
        int[][] m = nivelActual.getM();
        int cols = m[0].length;
        int filas = m.length;

        for (int intento = 0; intento < 300; intento++) {
            int col = rnd.nextInt(cols);
            int fila = rnd.nextInt(filas);

            if (m[fila][col] == 0) {
                copito.setX(col * FIGURA_WIDTH + posInitX);
                copito.setY(fila * FIGURA_WIDTH + posInitY);
                copito.setVisible(true);
                iJuego.drawCopito(copito);
                return;
            }
        }
    }

    public void activarCongelamiento() {
        copito.setVisible(false);
        iJuego.removeCopito();
        prevSpawnCopito = System.currentTimeMillis();

        long now = System.currentTimeMillis();
        for (Enemigo e : nivelActual.getEnemigos()) {
            e.setCongelado(true);
            e.setTiempoCongelado(now);
        }
    }

    public void verificarDescongelamiento() {
        long now = System.currentTimeMillis();
        for (Enemigo e : nivelActual.getEnemigos()) {
            if (e.isCongelado()) {
                if (now - e.getTiempoCongelado() >= e.getDuracionCong()) {
                    e.setCongelado(false);
                }
            }
        }
    }

    // ─── NIVELES ──────────────────────────────────────────

    public Nivel getNivel1() {
        Nivel nivel = new Nivel();
        nivel.setNroNivel(1);
        int[][] m = {
                {3,1,1,1,1,1,1,1,1,1,4},
                {2,0,0,0,0,0,0,0,0,0,2},
                {2,0,3,1,1,1,1,1,4,0,2},
                {2,0,2,0,0,0,0,0,2,0,2},
                {2,0,0,0,6,5,7,0,2,0,2},
                {2,0,7,0,4,3,0,0,5,0,6},
                {2,0,0,0,0,0,0,0,0,0,2},
                {2,0,3,1,1,1,1,1,4,0,2},
                {2,0,0,0,0,0,0,0,0,0,2},
                {5,1,1,1,1,1,1,1,1,1,6},
        };
        nivel.setM(m);
        nivel.setPacman(new Pacman(1 * FIGURA_WIDTH, 1 * FIGURA_WIDTH, FIGURA_WIDTH, 0));
        cargarBloques(nivel, m);
        nivel.setTotalPuntos(100);
        nivel.getEnemigos().add(new Enemigo(3 * FIGURA_WIDTH, 3 * FIGURA_WIDTH, 1, 0, "Rojo"));
        return nivel;
    }

    public Nivel getNivel2() {
        Nivel nivel = new Nivel();
        nivel.setNroNivel(2);
        int[][] m = {
                {3,1,1,1,1,1,1,1,1,1,1,4},
                {2,0,0,0,0,0,2,0,0,0,0,2},
                {2,0,3,1,4,0,2,0,3,1,0,2},
                {2,0,0,0,0,0,0,0,0,0,0,2},
                {2,0,5,1,6,0,7,0,5,1,6,2},
                {2,0,0,0,0,0,2,0,0,0,0,2},
                {2,0,3,1,1,0,0,0,1,1,4,2},
                {2,0,2,0,0,0,2,0,0,0,2,2},
                {2,0,5,1,1,1,1,1,1,1,6,2},
                {5,1,1,1,1,1,1,1,1,1,1,6},
        };
        nivel.setM(m);
        nivel.setPacman(new Pacman(1 * FIGURA_WIDTH, 1 * FIGURA_WIDTH, FIGURA_WIDTH, 15));
        cargarBloques(nivel, m);
        nivel.setTotalPuntos(100);
        nivel.getEnemigos().add(new Enemigo(3 * FIGURA_WIDTH, 3 * FIGURA_WIDTH, 1, 0, "Rojo"));
        nivel.getEnemigos().add(new Enemigo(8 * FIGURA_WIDTH, 6 * FIGURA_WIDTH, 0, 1, "Cyan"));
        return nivel;
    }

    public Nivel getNivel3() {
        Nivel nivel = new Nivel();
        nivel.setNroNivel(3);
        int[][] m = {
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
        nivel.setM(m);
        nivel.setPacman(new Pacman(1 * FIGURA_WIDTH, 1 * FIGURA_WIDTH, FIGURA_WIDTH, 15));
        cargarBloques(nivel, m);
        nivel.setTotalPuntos(100);
        nivel.getEnemigos().add(new Enemigo(3 * FIGURA_WIDTH, 3 * FIGURA_WIDTH, 1, 0, "Rojo"));
        nivel.getEnemigos().add(new Enemigo(8 * FIGURA_WIDTH, 6 * FIGURA_WIDTH, 0, 1, "Cyan"));
        nivel.getEnemigos().add(new Enemigo(5 * FIGURA_WIDTH, 9 * FIGURA_WIDTH, 1, 0, "Amarillo"));
        nivel.getEnemigos().add(new Enemigo(11 * FIGURA_WIDTH, 3 * FIGURA_WIDTH, 0, 1, "Rosa"));
        return nivel;
    }

    // ─── LÓGICA ───────────────────────────────────────────

    private void cargarBloques(Nivel nivel, int[][] m) {
        int pacCeldaX = nivel.getPacman() != null ? nivel.getPacman().getX() / FIGURA_WIDTH : -1;
        int pacCeldaY = nivel.getPacman() != null ? nivel.getPacman().getY() / FIGURA_WIDTH : -1;

        for (int f = 0; f < m.length; f++) {
            int y = f * FIGURA_WIDTH + posInitY;
            for (int c = 0; c < m[f].length; c++) {
                int x = c * FIGURA_WIDTH + posInitX;
                if (m[f][c] >= 1 && m[f][c] <= 7) {
                    nivel.getBloques().add(new Bloque(x, y, FIGURA_WIDTH, m[f][c], f, c));
                }
                if (m[f][c] == 0) {
                    // no poner flor donde inicia el pacman
                    if (f == pacCeldaY && c == pacCeldaX) continue;
                    nivel.getComidas().add(new Comida(x, y, FIGURA_WIDTH, false));
                }
            }
        }
    }

    private void comerComida() {
        Pacman p = nivelActual.getPacman();

        int celdaPacX = (p.getX() + p.getWidth() / 2 - posInitX) / FIGURA_WIDTH;
        int celdaPacY = (p.getY() + p.getWidth() / 2 - posInitY) / FIGURA_WIDTH;

        for (int i = 0; i < nivelActual.getComidas().size(); i++) {
            Comida comida = nivelActual.getComidas().get(i);
            int celdaComidaX = (comida.getX() - posInitX) / FIGURA_WIDTH;
            int celdaComidaY = (comida.getY() - posInitY) / FIGURA_WIDTH;

            if (celdaPacX == celdaComidaX && celdaPacY == celdaComidaY) {
                nivelActual.getComidas().remove(i);
                iJuego.removeComida(comida);

                if (comida.isEsPremioMayor()) {
                    // fruta = 10 puntos y pasa de nivel
                    nivelActual.setPuntosAcum(nivelActual.getPuntosAcum() + 10);
                    iJuego.updateScore(nivelActual.getPuntosAcum());
                    pasarSiguienteNivel();
                } else {
                    // flor normal = 1 punto
                    nivelActual.setPuntosAcum(nivelActual.getPuntosAcum() + 1);
                    iJuego.updateScore(nivelActual.getPuntosAcum());
                    iJuego.playEatSound();
                    boolean quedanNormales = nivelActual.getComidas().stream()
                            .anyMatch(c -> !c.isEsPremioMayor());
                    if (!quedanNormales) {
                        mostrarPremioMayor();
                    }
                }
                break;
            }
        }
    }

    public void mostrarPremioMayor() {
        int[][] m = nivelActual.getM();
        int centroF = m.length / 2;
        int centroC = m[0].length / 2;

        int celdaF = -1, celdaC = -1;
        int maxRadio = Math.max(m.length, m[0].length);

        for (int radio = 0; radio <= maxRadio && celdaF == -1; radio++) {
            for (int df = -radio; df <= radio && celdaF == -1; df++) {
                for (int dc = -radio; dc <= radio && celdaF == -1; dc++) {
                    if (Math.abs(df) != radio && Math.abs(dc) != radio) continue;
                    int f = centroF + df;
                    int c = centroC + dc;
                    if (f >= 0 && f < m.length && c >= 0 && c < m[0].length) {
                        if (m[f][c] == 0) {
                            celdaF = f;
                            celdaC = c;
                        }
                    }
                }
            }
        }

        if (celdaF == -1) return;

        int x = celdaC * FIGURA_WIDTH + posInitX;
        int y = celdaF * FIGURA_WIDTH + posInitY;

        Comida premio = new Comida(x, y, FIGURA_WIDTH, true);
        nivelActual.getComidas().add(premio);
        iJuego.drawComida(premio);
    }

    public void avanzarPacman() {
        long currentTime = System.currentTimeMillis();
        Pacman p = nivelActual.getPacman();

        if (currentTime - p.getPrevTimeAdvance() > p.getTimeAdvance()) {

            int nextX = p.getX();
            int nextY = p.getY();

            if (p.getDirAdvance() == DireccionEnum.DERECHA || p.getDirAdvance() == DireccionEnum.IZQUIERDA) {
                int resto = nextY % FIGURA_WIDTH;
                if (resto != 0) {
                    nextY = (resto < FIGURA_WIDTH / 2)
                            ? nextY - resto
                            : nextY + (FIGURA_WIDTH - resto);
                }
            }

            if (p.getDirAdvance() == DireccionEnum.ARRIBA || p.getDirAdvance() == DireccionEnum.ABAJO) {
                int resto = nextX % FIGURA_WIDTH;
                if (resto != 0) {
                    nextX = (resto < FIGURA_WIDTH / 2)
                            ? nextX - resto
                            : nextX + (FIGURA_WIDTH - resto);
                }
            }

            if (p.getDirAdvance() == DireccionEnum.DERECHA)        nextX += 2;
            else if (p.getDirAdvance() == DireccionEnum.IZQUIERDA) nextX -= 2;
            else if (p.getDirAdvance() == DireccionEnum.ARRIBA)    nextY -= 2;
            else if (p.getDirAdvance() == DireccionEnum.ABAJO)     nextY += 2;

            boolean hayColision = false;
            int tolerance = 2;
            int maxF = nivelActual.getM().length - 1;
            int maxC = nivelActual.getM()[0].length - 1;

            int celdaX1 = Math.max(0, Math.min((nextX + tolerance) / FIGURA_WIDTH, maxC));
            int celdaX2 = Math.max(0, Math.min((nextX + p.getWidth() - 1 - tolerance) / FIGURA_WIDTH, maxC));
            int celdaY1 = Math.max(0, Math.min((nextY + tolerance) / FIGURA_WIDTH, maxF));
            int celdaY2 = Math.max(0, Math.min((nextY + p.getWidth() - 1 - tolerance) / FIGURA_WIDTH, maxF));

            int[][] m = nivelActual.getM();
            if (esWall(m[celdaY1][celdaX1]) || esWall(m[celdaY2][celdaX2])
                    || esWall(m[celdaY1][celdaX2]) || esWall(m[celdaY2][celdaX1])) {
                hayColision = true;
            }

            if (!hayColision) {
                p.setX(nextX);
                p.setY(nextY);
                p.setPrevTimeAdvance(currentTime);
                iJuego.updateLocationPacman();
                comerComida();
            }
        }
    }

    public void moverEnemigos() {
        // actualizar copito y descongelamiento en cada tick
        actualizarCopito();
        verificarDescongelamiento();

        int[][] m = nivelActual.getM();
        for (Enemigo enemigo : nivelActual.getEnemigos()) {
            // NUEVO: fantasmas congelados no se mueven
            if (enemigo.isCongelado()) continue;
            moverEnemigo(enemigo, m);
        }
        iJuego.updateEnemigos();
    }

    private void moverEnemigo(Enemigo enemigo, int[][] m) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - enemigo.getPrevTimeAdvance() <= enemigo.getTimeAdvance()) return;

        int maxF = m.length - 1;
        int maxC = m[0].length - 1;
        Pacman p = nivelActual.getPacman();

        if (!enemigo.isIniciado()) {
            int cX = enemigo.getX() / FIGURA_WIDTH;
            int cY = enemigo.getY() / FIGURA_WIDTH;
            enemigo.setCeldaDestX(cX);
            enemigo.setCeldaDestY(cY);
            enemigo.setIniciado(true);
        }

        int destPixelX = enemigo.getCeldaDestX() * FIGURA_WIDTH;
        int destPixelY = enemigo.getCeldaDestY() * FIGURA_WIDTH;

        int paso = 2;
        if (enemigo.getX() < destPixelX) enemigo.setX(Math.min(enemigo.getX() + paso, destPixelX));
        else if (enemigo.getX() > destPixelX) enemigo.setX(Math.max(enemigo.getX() - paso, destPixelX));

        if (enemigo.getY() < destPixelY) enemigo.setY(Math.min(enemigo.getY() + paso, destPixelY));
        else if (enemigo.getY() > destPixelY) enemigo.setY(Math.max(enemigo.getY() - paso, destPixelY));

        if (enemigo.getX() == destPixelX && enemigo.getY() == destPixelY) {
            int celdaX = enemigo.getX() / FIGURA_WIDTH;
            int celdaY = enemigo.getY() / FIGURA_WIDTH;

            if (puedeVerPacman(enemigo, p, m)) {
                int difX = p.getX() - enemigo.getX();
                int difY = p.getY() - enemigo.getY();
                if (Math.abs(difX) > Math.abs(difY)) {
                    enemigo.setDx(difX > 0 ? 1 : -1);
                    enemigo.setDy(0);
                } else {
                    enemigo.setDx(0);
                    enemigo.setDy(difY > 0 ? 1 : -1);
                }
            }

            int nextCX = Math.max(0, Math.min(celdaX + enemigo.getDx(), maxC));
            int nextCY = Math.max(0, Math.min(celdaY + enemigo.getDy(), maxF));

            if (esWall(m[nextCY][nextCX])) {
                if (enemigo.getDx() != 0) {
                    int abajoCY = Math.min(celdaY + 1, maxF);
                    int arribaCY = Math.max(celdaY - 1, 0);
                    if (!esWall(m[abajoCY][celdaX])) {
                        enemigo.setDx(0); enemigo.setDy(1);
                    } else if (!esWall(m[arribaCY][celdaX])) {
                        enemigo.setDx(0); enemigo.setDy(-1);
                    } else {
                        enemigo.setDx(-enemigo.getDx());
                    }
                } else {
                    int derechaCX = Math.min(celdaX + 1, maxC);
                    int izquierdaCX = Math.max(celdaX - 1, 0);
                    if (!esWall(m[celdaY][derechaCX])) {
                        enemigo.setDx(1); enemigo.setDy(0);
                    } else if (!esWall(m[celdaY][izquierdaCX])) {
                        enemigo.setDx(-1); enemigo.setDy(0);
                    } else {
                        enemigo.setDy(-enemigo.getDy());
                    }
                }
                nextCX = Math.max(0, Math.min(celdaX + enemigo.getDx(), maxC));
                nextCY = Math.max(0, Math.min(celdaY + enemigo.getDy(), maxF));
                if (esWall(m[nextCY][nextCX])) {
                    nextCX = celdaX;
                    nextCY = celdaY;
                }
            }

            enemigo.setCeldaDestX(nextCX);
            enemigo.setCeldaDestY(nextCY);
        }

        if (colisionConPacman(enemigo, p)) {
            iJuego.gameOver();
            return;
        }

        enemigo.setPrevTimeAdvance(currentTime);
    }

    private boolean colisionConPacman(Enemigo enemigo, Pacman p) {
        // NUEVO: congelados no matan
        if (enemigo.isCongelado()) return false;

        int margen = 20;
        return Math.abs(enemigo.getX() - p.getX()) < margen
                && Math.abs(enemigo.getY() - p.getY()) < margen;
    }

    private boolean puedeVerPacman(Enemigo enemigo, Pacman p, int[][] m) {
        int eCeldaX = enemigo.getX() / FIGURA_WIDTH;
        int eCeldaY = enemigo.getY() / FIGURA_WIDTH;
        int pCeldaX = p.getX() / FIGURA_WIDTH;
        int pCeldaY = p.getY() / FIGURA_WIDTH;

        // misma fila — sin cambios
        if (eCeldaY == pCeldaY) {
            int c1 = Math.min(eCeldaX, pCeldaX);
            int c2 = Math.max(eCeldaX, pCeldaX);
            for (int c = c1; c <= c2; c++) {
                if (esWall(m[eCeldaY][c])) return false;
            }
            return true;
        }

        // misma columna — sin cambios
        if (eCeldaX == pCeldaX) {
            int f1 = Math.min(eCeldaY, pCeldaY);
            int f2 = Math.max(eCeldaY, pCeldaY);
            for (int f = f1; f <= f2; f++) {
                if (esWall(m[f][eCeldaX])) return false;
            }
            return true;
        }

        // NUEVO: fila cercana (+-1 celda) — el fantasma va horizontal y pacman está una fila arriba/abajo
        if (Math.abs(eCeldaY - pCeldaY) == 1) {
            int c1 = Math.min(eCeldaX, pCeldaX);
            int c2 = Math.max(eCeldaX, pCeldaX);
            boolean hayPared = false;
            for (int c = c1; c <= c2; c++) {
                if (esWall(m[eCeldaY][c])) { hayPared = true; break; }
            }
            if (!hayPared) return true;
        }

        // NUEVO: columna cercana (+-1 celda) — el fantasma va vertical y pacman está una columna al lado
        if (Math.abs(eCeldaX - pCeldaX) == 1) {
            int f1 = Math.min(eCeldaY, pCeldaY);
            int f2 = Math.max(eCeldaY, pCeldaY);
            boolean hayPared = false;
            for (int f = f1; f <= f2; f++) {
                if (esWall(m[f][eCeldaX])) { hayPared = true; break; }
            }
            if (!hayPared) return true;
        }

        return false;
    }

    private boolean esWall(int celda) {
        return celda >= 1 && celda <= 7;
    }

    private void pasarSiguienteNivel() {
        int idx = niveles.indexOf(nivelActual);
        if (idx < niveles.size() - 1) {
            iJuego.clearUI();
            nivelActual = niveles.get(idx + 1);
            createUINivel(nivelActual);
        } else {
            iJuego.mostrarVictoria();
        }
    }
}