/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigos;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author Nahuel
 */
public class InterfaceController implements Initializable {

    @FXML
    TextField field;
    @FXML
    HBox hbox;

    List list;

    int n;

    Semaphore table;
    Semaphore[] sticks;
    Philosopher[] philosophers;
    FilosofosController[] controllers;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    public void salir(ActionEvent evento) {
        System.exit(0);
    }

    @FXML
    public void ejecutar(ActionEvent evento) throws IOException {
        TextField b = (TextField) evento.getSource();
        n = Integer.valueOf(b.getText());
        field.setDisable(true);
        
        table = new Semaphore(n - 1);
        sticks = new Semaphore[n];
        philosophers = new Philosopher[n];
        list = new ArrayList<>();
        controllers = new FilosofosController[n];

        //Init Sticks
        for (int i = 0; i < n; i++) {
            sticks[i] = new Semaphore(1);

            FXMLLoader fxml = new FXMLLoader(
                    getClass().getResource("/codigos/filosofos.fxml"));
            Parent nodo = fxml.load();

            //Carga los datos
            //controllers[i] = 
            FilosofosController c = fxml.getController();
            c.setColor("green");
            c.setFilosofo(String.valueOf(i + 1));
            this.controllers[i] = c;

            //Cargo el nuevo objeto
            list.add(nodo);
        }

        hbox.getChildren().addAll(list);

        //Init Philosophers
        for (int i = 0; i < n; i++) {
            philosophers[i] = new Philosopher(i);
            Thread t = new Thread(philosophers[i], "Philosopher " + (i + 1));
            t.start();
        }
    }

    class Philosopher extends Thread {

        Integer philosopher;

        Philosopher(Integer i) {
            this.philosopher = i;
        }

        void think() throws InterruptedException {
            doAction("thinking");
        }

        void eat() throws InterruptedException {
            doAction("eating");
        }

        void doAction(String action) throws InterruptedException {
            System.out.println("Philosopher " + (philosopher + 1) + " is : " + action);
            FilosofosController c = controllers[philosopher];

            if (action.equals("eating")) {
                c.setColor("red");
            } else {
                c.setColor("green");
            }

            Thread.sleep(((int) (Math.random() * 100)));
        }

        @Override
        public void run() {
            try {
                Integer left = philosopher;
                Integer right = (philosopher + 1) % n;
                while (true) {

                    think();
                    synchronized (table) {
                        table.acquire();
                        synchronized (left) {
                            sticks[left].acquire();
                            synchronized (right) {
                                sticks[right].acquire();
                                eat();
                                sticks[right].release();
                            }
                            sticks[left].release();
                        }
                        table.release();
                    }

                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
