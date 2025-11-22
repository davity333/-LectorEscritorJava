package org.example.lectorescritor.Models;

import java.util.ArrayList;

public class Monitor {
        private boolean escritorActivo = false;
        private int lectoresActivos = 0;

        public synchronized void startRead() throws InterruptedException {
            while (escritorActivo) {
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
        if(escritorActivo || lectoresActivos > 0){
            wait();
        }
        escritorActivo = true;
    }

    public synchronized void endWriter() throws InterruptedException{
        escritorActivo = false;
        notifyAll();
    }
}


