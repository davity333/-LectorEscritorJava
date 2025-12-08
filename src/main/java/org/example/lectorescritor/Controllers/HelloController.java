package org.example.lectorescritor.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import org.example.lectorescritor.Models.Monitor;

public class HelloController {
    @FXML private Label welcomeText;
    @FXML private Label lectoresActivos;
    @FXML private Text numEscritor;
    @FXML private Text numEscritoresEsperando;
    @FXML private Text numLectoresEsperando;
    @FXML private Circle estadoCircle;
    @FXML private TextField textField;
    @FXML private Text textStatus;

    Monitor monitor = new Monitor();

    // Guardar referencias a los hilos para poderlos interrumpir
    private Thread hiloLectores = null;
    private Thread hiloEscritores = null;

    @FXML
    protected void clickAddLector() {
        // Si hay un hilo anterior, interrúmpelo
        if (hiloLectores != null && hiloLectores.isAlive()) {
            hiloLectores.interrupt();
        }

        int cantidadInput = Integer.parseInt(textField.getText());

        Platform.runLater(() -> {
            numLectoresEsperando.setText(String.valueOf(cantidadInput));
            lectoresActivos.setText("0");
        });

        hiloLectores = new Thread(() -> {
            try {
                for (int i = 1; i <= cantidadInput; i++) {
                    // Verificar si el hilo fue interrumpido
                    if (Thread.currentThread().isInterrupted()) {
                        throw new InterruptedException("Hilo interrumpido");
                    }

                    monitor.startRead();

                    final int valor = i;
                    Platform.runLater(() -> {
                        lectoresActivos.setText(String.valueOf(valor));
                        textStatus.setText("Lectores ejecutándose");

                        int restantes = cantidadInput - valor;
                        numLectoresEsperando.setText(String.valueOf(restantes));

                        System.out.println("Lector: " + valor);
                        estadoCircle.setFill(Color.GREEN);
                    });

                    Thread.sleep(2000);

                    monitor.endRead();

                    Thread.sleep(500);
                }

                Platform.runLater(() -> {
                    textStatus.setText("ESPERANDO");
                    numLectoresEsperando.setText("0");
                    lectoresActivos.setText("0");
                    estadoCircle.setFill(Color.GRAY);
                });

            } catch (InterruptedException e) {
                System.out.println("Hilo lectores interrumpido");
                // Limpiar estado
                Platform.runLater(() -> {
                    textStatus.setText("INTERRUMPIDO");
                    estadoCircle.setFill(Color.RED);
                });
            }
        });

        hiloLectores.start();
    }

    @FXML
    protected void clickAddEscritor() {
        // Si hay un hilo anterior, interrúmpelo
        if (hiloEscritores != null && hiloEscritores.isAlive()) {
            hiloEscritores.interrupt();
        }

        int cantidadInput = Integer.parseInt(textField.getText());

        Platform.runLater(() -> {
            numEscritoresEsperando.setText(String.valueOf(cantidadInput));
            numEscritor.setText("0");
        });

        hiloEscritores = new Thread(() -> {
            try {
                for (int i = 1; i <= cantidadInput; i++) {
                    // Verificar si el hilo fue interrumpido
                    if (Thread.currentThread().isInterrupted()) {
                        throw new InterruptedException("Hilo interrumpido");
                    }

                    monitor.startWriter();

                    final int escritorId = i;
                    Platform.runLater(() -> {
                        numEscritor.setText("1");
                        textStatus.setText("Escritor ejecutándose");
                        estadoCircle.setFill(Color.BLUE);

                        int restantes = cantidadInput - escritorId;
                        numEscritoresEsperando.setText(String.valueOf(restantes));
                    });

                    System.out.println("Escritor activo: " + escritorId);
                    Thread.sleep(3000);

                    monitor.endWriter();

                    Platform.runLater(() -> {
                        numEscritor.setText("0");
                        if (escritorId < cantidadInput) {
                            textStatus.setText("ESPERANDO próximo escritor");
                            estadoCircle.setFill(Color.GRAY);
                        }
                    });

                    Thread.sleep(700);
                }

                Platform.runLater(() -> {
                    textStatus.setText("ESPERANDO");
                    estadoCircle.setFill(Color.GRAY);
                    numEscritor.setText("0");
                    numEscritoresEsperando.setText("0");
                });

            } catch (InterruptedException e) {
                System.out.println("Hilo escritores interrumpido");
                Platform.runLater(() -> {
                    textStatus.setText("INTERRUMPIDO");
                    estadoCircle.setFill(Color.RED);
                });
            }
        });

        hiloEscritores.start();
    }

    @FXML
    protected void clickReset() {
        System.out.println("REINICIANDO TODO");

        // 1. Interrumpir hilos activos
        if (hiloLectores != null && hiloLectores.isAlive()) {
            hiloLectores.interrupt();
            hiloLectores = null;
        }

        if (hiloEscritores != null && hiloEscritores.isAlive()) {
            hiloEscritores.interrupt();
            hiloEscritores = null;
        }

        // 2. Resetear el monitor
        monitor.resetAll();

        // 3. Actualizar UI
        Platform.runLater(() -> {
            lectoresActivos.setText("0");
            numEscritor.setText("0");
            numEscritoresEsperando.setText("0");
            numLectoresEsperando.setText("0");
            estadoCircle.setFill(Color.GRAY);
            textStatus.setText("RESETEADO");

            // Limpiar campo de texto
            textField.clear();
        });
    }
}