/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vc;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import m.Parametres;


/**
 *
 * @author Hugo
 */
public class Main extends Application implements Parametres {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("View.fxml"));
        
        Scene scene = new Scene(root);
        boolean add = scene.getStylesheets().add("css/styles.css");
        
        stage.setScene(scene);
        stage.show();
        scene.getRoot().requestFocus();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
        /*int val = 1;
        Random r = new Random();
        Grille g = new Grille(null);
        for(int i=0;i<4; i++) {
            for(int j=0;j<4; j++) {
                Case c = new Case(i,j,val);
                c.setGrille(g);
                g.getCases().add(c);
                val+= 1;
            }
        }
        IA ia = new IA(null, 1);
        ia.setBot();
        System.out.println(ia.getChildren(g, KEYS[0]).isEmpty());
        System.out.println(g.getCases().size());
        System.out.println(g.bloquee());*/
    }
    
}
