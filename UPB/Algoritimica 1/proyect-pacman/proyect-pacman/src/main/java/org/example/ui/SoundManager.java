package org.example.ui;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SoundManager {
    private static Thread loopThread;
    private static volatile boolean loopRunning = false;
    private static Clip clipLoop; // para sonidos en loop
    private static Clip[] eatPool;
    private static int eatIndex = 0;

    public static void initEatPool(String nombreArchivo, int size) {
        eatPool = new Clip[size];
        for (int i = 0; i < size; i++) {
            try {
                URL url = SoundManager.class.getClassLoader().getResource("Audios/" + nombreArchivo);
                AudioInputStream ais = AudioSystem.getAudioInputStream(url);
                eatPool[i] = AudioSystem.getClip();
                eatPool[i].open(ais);
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    public static void playEat() {
        if (eatPool == null) return;
        Clip clip = eatPool[eatIndex % eatPool.length];
        eatIndex++;
        clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }

    public static void play(String nombreArchivo) {
        try {
            URL url = SoundManager.class.getClassLoader()
                    .getResource("Audios/" + nombreArchivo);
            if (url == null) {
                System.err.println("Audio no encontrado: " + nombreArchivo);
                return;
            }
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void playLoopConDelay(String nombreArchivo, int delayMs) {
        stopLoop(); // detiene clip anterior
        if (loopThread != null) {
            loopRunning = false;
            loopThread.interrupt();
        }

        loopRunning = true;
        loopThread = new Thread(() -> {
            while (loopRunning) {
                try {
                    URL url = SoundManager.class.getClassLoader()
                            .getResource("Audios/" + nombreArchivo);
                    if (url == null) return;

                    AudioInputStream ais = AudioSystem.getAudioInputStream(url);
                    Clip clip = AudioSystem.getClip();
                    clip.open(ais);
                    clip.start();

                    // Espera a que termine + el delay extra
                    Thread.sleep(clip.getMicrosecondLength() / 1000 + delayMs);
                    clip.close();

                } catch (Exception e) {
                    break;
                }
            }
        });
        loopThread.setDaemon(true);
        loopThread.start();
    }



    private static AudioInputStream resampleAudio(AudioInputStream ais, float factor) {
        AudioFormat original = ais.getFormat();
        float newSampleRate = original.getSampleRate() * factor; // factor < 1.0 = más lento
        AudioFormat nuevoFormato = new AudioFormat(
                original.getEncoding(),
                newSampleRate,
                original.getSampleSizeInBits(),
                original.getChannels(),
                original.getFrameSize(),
                newSampleRate,
                original.isBigEndian()
        );
        return new AudioInputStream(ais, nuevoFormato, ais.getFrameLength());
    }

    public static void playLoop(String nombreArchivo) {
        playLoop(nombreArchivo, 1.0f); // sin cambio
    }

    public static void playLoop(String nombreArchivo, float velocidad) {
        stopLoop();
        try {
            URL url = SoundManager.class.getClassLoader()
                    .getResource("Audios/" + nombreArchivo);
            if (url == null) {
                System.err.println("Audio no encontrado: " + nombreArchivo);
                return;
            }
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);

            // Aplicar resample si la velocidad es distinta de 1.0
            if (velocidad != 1.0f) {
                ais = resampleAudio(ais, velocidad);
            }

            clipLoop = AudioSystem.getClip();
            clipLoop.open(ais);
            clipLoop.loop(Clip.LOOP_CONTINUOUSLY);
            clipLoop.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void stopLoop() {
        loopRunning = false;
        if (loopThread != null) {
            loopThread.interrupt();
            loopThread = null;
        }
        if (clipLoop != null && clipLoop.isRunning()) {
            clipLoop.stop();
            clipLoop.close();
            clipLoop = null;
        }
    }
}