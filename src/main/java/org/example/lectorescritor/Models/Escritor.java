package org.example.lectorescritor.Models;

public class Escritor {
    private int escritoresActivos = 0;
    private int escritoresEsperando = 0;

    public Escritor(int escritoresActivos, int escritoresEsperando) {
        this.escritoresActivos = escritoresActivos;
        this.escritoresEsperando = escritoresEsperando;
    }

    public int getEscritoresActivos() {
        return escritoresActivos;
    }

    public void setEscritoresActivos(int escritoresActivos) {
        this.escritoresActivos = escritoresActivos;
    }

    public int getEscritoresEsperando() {
        return escritoresEsperando;
    }

    public void setEscritoresEsperando(int escritoresEsperando) {
        this.escritoresEsperando = escritoresEsperando;
    }
}
