package org.example.lectorescritor.Models;

public class Monitor {
    private boolean escritorActivo = false;
    private int lectoresActivos = 0;
    private volatile boolean resetRequested = false;

    public synchronized void startRead() throws InterruptedException {
        while (escritorActivo || resetRequested) {
            if (resetRequested) {
                throw new InterruptedException("Reset solicitado");
            }
            wait();
        }
        lectoresActivos++;
        System.out.println("Lector entra. Lectores activos: " + lectoresActivos + ", Escritor activo: " + escritorActivo);
    }

    public synchronized void endRead() {
        lectoresActivos--;
        System.out.println("Lector sale. Lectores activos: " + lectoresActivos + ", Escritor activo: " + escritorActivo);

        if (lectoresActivos == 0) {
            notifyAll();
        }
    }

    public synchronized void startWrite() throws InterruptedException {
        while (escritorActivo || lectoresActivos > 0 || resetRequested) {
            if (resetRequested) {
                throw new InterruptedException("Reset solicitado");
            }
            wait();
        }

        escritorActivo = true;
        System.out.println("Escritor entra. Lectores activos: " + lectoresActivos + ", Escritor activo: " + escritorActivo);
    }

    public synchronized void endWrite() {
        escritorActivo = false;
        System.out.println("Escritor sale. Lectores activos: " + lectoresActivos + ", Escritor activo: " + escritorActivo);
        notifyAll();  // Notificar a todos (tanto lectores como otros escritores)
    }

    public synchronized void resetAll() {
        resetRequested = true;
        escritorActivo = false;
        lectoresActivos = 0;
        notifyAll();
        resetRequested = false;
        System.out.println("Monitor reseteado");
    }


    public synchronized int getLectoresActivos() {
        return lectoresActivos;
    }

    public synchronized boolean isEscritorActivo() {
        return escritorActivo;
    }
}