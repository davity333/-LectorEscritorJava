package org.example.lectorescritor.Models;

public class Lector {
    private int lectoresActivos = 0;
    private int lectoresEsperando = 0;

    public Lector(int lectoresActivos, int lectoresEsperando) {
        this.lectoresActivos = lectoresActivos;
        this.lectoresEsperando = lectoresEsperando;
    }

    public int getLectoresActivos() {
        return lectoresActivos;
    }

    public void setLectoresActivos(int lectoresActivos) {
        this.lectoresActivos = lectoresActivos;
    }

    public int getLectoresEsperando() {
        return lectoresEsperando;
    }

    public void setLectoresEsperando(int lectoresEsperando) {
        this.lectoresEsperando = lectoresEsperando;
    }
}
