package org.example.lectorescritor.Models;

public class Monitor {
    private boolean escritorActivo = false;
    private int lectoresActivos = 0;
    private int escritoresEnEspera = 0;
    private volatile boolean resetRequested = false; // Nueva bandera

    public synchronized void startRead() throws InterruptedException {
        while ((escritorActivo || lectoresActivos > 0) && !resetRequested) {
            if (resetRequested) {
                Thread.currentThread().interrupt();
                throw new InterruptedException("Reset solicitado");
            }
            wait();
        }
        if (resetRequested) {
            Thread.currentThread().interrupt();
            throw new InterruptedException("Reset solicitado");
        }
        lectoresActivos++;
    }

    public synchronized void endRead() {
        lectoresActivos--;
        if (lectoresActivos == 0) {
            notifyAll();
        }
    }

    public synchronized void startWriter() throws InterruptedException {
        while((escritorActivo || lectoresActivos > 0) && !resetRequested) {
            if (resetRequested) {
                Thread.currentThread().interrupt();
                throw new InterruptedException("Reset solicitado");
            }
            wait();
        }
        if (resetRequested) {
            Thread.currentThread().interrupt();
            throw new InterruptedException("Reset solicitado");
        }
        escritoresEnEspera--;
        escritorActivo = true;
    }

    public synchronized void endWriter() {
        escritorActivo = false;
        notifyAll();
    }

    // NUEVO MÉTODO: Resetear todo
    public synchronized void resetAll() {
        resetRequested = true;
        escritorActivo = false;
        lectoresActivos = 0;
        escritoresEnEspera = 0;
        notifyAll(); // Despierta a todos los hilos en wait()
        resetRequested = false; // Opcional: volver a false después
    }
}