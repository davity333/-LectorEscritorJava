package org.example.lectorescritor.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import org.example.lectorescritor.Models.Lector;
import org.example.lectorescritor.Models.Monitor;

public class HelloController {
    @FXML
    private Label welcomeText;
    @FXML
    private Label lectoresActivos;
    @FXML
    private Text numEscritor;
    @FXML
    private Text numEscritoresEsperando;
    @FXML
    private Text numLectoresEsperando;
    @FXML
    private Circle estadoCircle;
    @FXML
    private TextField textField;
    @FXML
    private Text textStatus;

    Monitor monitor = new Monitor();
    Lector lector = new Lector(0,0);
    boolean readWait = false;
    boolean writerWait = false;
    int quantityWait = 0;
    boolean limpiar = false;


    @FXML
    protected void clickAddLector() {
        readWait = true;
        int cantidadInput = Integer.parseInt(textField.getText());
        quantityWait = cantidadInput;

        Platform.runLater(() -> {
            numLectoresEsperando.setText(String.valueOf(cantidadInput));
            lectoresActivos.setText("0");
        });

        new Thread(() -> {
            try {
                monitor.startRead();

                for (int i = 1; i <= cantidadInput; i++) {
                    if (limpiar) break;
                    int valor = i;

                    Platform.runLater(() -> {
                        lectoresActivos.setText(String.valueOf(valor));
                        textStatus.setText("Lectores ejecutándose");

                        int restantes = cantidadInput - valor;
                        numLectoresEsperando.setText(String.valueOf(restantes));

                        System.out.println("Lector: " + valor);
                        estadoCircle.setFill(Color.GREEN);
                    });

                    Thread.sleep(2000); // wait dentro
                }

                Thread.sleep(500); // wait afuera
                monitor.endRead();
                writerWait = false;

                Platform.runLater(() -> {
                    textStatus.setText("ESPERANDO");
                    numEscritoresEsperando.setText("0");
                    lectoresActivos.setText("0");
                    numLectoresEsperando.setText("0");
                    estadoCircle.setFill(Color.GRAY);
                });

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    protected void clickAddEscritor() {
        writerWait = true;
        int cantidadInput = Integer.parseInt(textField.getText());
        quantityWait = cantidadInput;


        Platform.runLater(() -> {
            numEscritoresEsperando.setText(String.valueOf(cantidadInput));
            numEscritor.setText("0");
        });

        new Thread(() -> {
            for (int i = 1; i <= cantidadInput; i++) {
                if (limpiar) break;

                final int escritorId = i;

                try {
                    monitor.startWriter();

                    Platform.runLater(() -> {
                        numEscritor.setText("1");
                        textStatus.setText("Escritor ejecutándose");
                        estadoCircle.setFill(Color.BLUE);

                        int restantes = cantidadInput - escritorId;
                        numEscritoresEsperando.setText(String.valueOf(restantes));
                    });

                    System.out.println("Escritor activo: " + escritorId);
                    Thread.sleep(3000); // dentro
                    monitor.endWriter();

                    Platform.runLater(() -> {
                        numEscritor.setText("0");
                        if (escritorId < cantidadInput) {
                            textStatus.setText("ESPERANDO próximo escritor");
                            System.out.println("Escritor " + escritorId +" saliendo");
                            estadoCircle.setFill(Color.GRAY);
                        }
                    });
                    Thread.sleep(700); //fuera

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

            // Limpiar estado final
            Platform.runLater(() -> {
                writerWait = false;
                textStatus.setText("ESPERANDO");
                estadoCircle.setFill(Color.GRAY);
                numEscritor.setText("0");
                numEscritoresEsperando.setText("0");
            });

        }).start();
    }
    @FXML
    protected void clickReset() throws InterruptedException{
        limpiar = true;
        monitor.endWriter();
        lectoresActivos.setText(String.valueOf(0));
        numEscritor.setText(String.valueOf(0));
        numEscritoresEsperando.setText("0");
        numLectoresEsperando.setText("0");
        System.out.println("REINICIANDO TODO");
        estadoCircle.setFill(Color.GRAY);
        Platform.runLater(() -> textStatus.setText("ESPERANDO"));
        limpiar = false;
    }
}