package org.example.lectorescritor.Models;

import java.util.ArrayList;

public class Monitor {
        private boolean escritorActivo = false;
        private int lectoresActivos = 0;
        private int escritoresEnEspera = 0;

        public synchronized void startRead() throws InterruptedException {
            while (escritorActivo || lectoresActivos > 0) {
                wait();
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
        while(escritorActivo || lectoresActivos > 0){
            wait();
        }
        escritoresEnEspera--;
        escritorActivo = true;
    }

    public synchronized void endWriter() throws InterruptedException{
        escritorActivo = false;
        notifyAll();
    }
}


