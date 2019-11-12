/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigos;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

/**
 * FXML Controller class
 *
 * @author Nahuel
 */
public class FilosofosController implements Initializable {

    @FXML
    Label filosofo;
    @FXML
    Circle circulo;  
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    public void setFilosofo(String filosofo){
        this.filosofo.setText(filosofo);
    }
    
    public void setColor(String color){
        circulo.setFill(Paint.valueOf(color));
    }
    
}
