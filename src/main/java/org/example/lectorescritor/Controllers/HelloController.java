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

        if (writerWait){
            System.out.println(quantityWait + " Lectores esperando");
            Platform.runLater(() -> numLectoresEsperando.setText(String.valueOf(quantityWait)));
        }

        new Thread(() -> {
            try {
                monitor.startRead();
                for (int i = 1; i <= cantidadInput; i++) {
                    if (limpiar) break;
                    int valor = i;
                    Platform.runLater(() -> {
                        lectoresActivos.setText(String.valueOf(valor));
                        textStatus.setText("Lectores ejecutándose");
                        System.out.println("Lector: "+valor);
                        estadoCircle.setFill(Color.GREEN);
                    });
                    Thread.sleep(800); // medio segundo entre cada paso
                }
                Thread.sleep(2000);
                monitor.endRead();
                writerWait = false;
                Platform.runLater(() -> {
                    textStatus.setText("ESPERANDO");
                    numEscritoresEsperando.setText("0");
                    lectoresActivos.setText("0");
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

        if(readWait){
            System.out.println(quantityWait + " Escritores esperando");
            Platform.runLater(() -> numEscritoresEsperando.setText(String.valueOf(quantityWait)));
        }

        new Thread(() -> {
            try {

                monitor.startWriter();
                for (int i = 1; i <= cantidadInput; i++) {
                    if (limpiar) break;
                    int valor = i;
                    Platform.runLater(() -> {
                        numEscritor.setText(String.valueOf(valor));
                        textStatus.setText("Escritor ejecutándose");
                        System.out.println("Escritor: "+valor);
                        estadoCircle.setFill(Color.BLUE);
                    });
                    Thread.sleep(800);
                }
                Thread.sleep(2000);
                monitor.endWriter();
                writerWait = false;
                Platform.runLater(() -> {
                    textStatus.setText("ESPERANDO");
                    numLectoresEsperando.setText("0");
                    numEscritor.setText("0");
                    estadoCircle.setFill(Color.GRAY);
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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