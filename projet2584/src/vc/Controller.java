/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vc;

import m.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
//import javafx.scene.paint.Color;
//import javafx.scene.text.Font;

/**
 *
 * @author castagno
 */
public class Controller implements Initializable {

    /*
     * Variables globales correspondant à des objets définis dans la vue (fichier .fxml)
     * Ces variables sont ajoutées à la main et portent le même nom que les fx:id dans Scene Builder
     */
    @FXML
    private Label score; // value will be injected by the FXMLLoader
    @FXML
    private GridPane grille1;
    @FXML
    private GridPane grille2;
    @FXML
    private Pane fond; // panneau recouvrant toute la fenêtre
    @FXML
    private TextField name1; 
    @FXML
    private TextField name2; 
    @FXML
    private ChoiceBox type1; 
    @FXML
    private ChoiceBox type2;
    @FXML
    private Pane panneau_score1;
    @FXML
    private Pane panneau_score2;
    @FXML
    private Button undo1;
    @FXML
    private Button undo2;
    @FXML
    private Pane best1; // panneau qui affiche le best score
    @FXML
    private Pane best2; // panneau qui affiche le best score
    @FXML
    private Label console; // user information area
    @FXML
    private Button play;

    // variables globales non définies dans la vue (fichier .fxml)
    private final Pane p = new Pane(); // panneau utilisé pour dessiner une tuile "2"
    private final Label c = new Label("2");
    private int x = 24, y = 191;
    private int objectifx = 24, objectify = 191;
    private Partie partie; // modèle

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.partie = new Partie(); // crée la partie (modèle)
        this.initChoix(); // configuration paramètres

        //ajoute listener pour changement d'items dans type
        this.type1.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {

                //on cherche la valeur du nouvel item
                if (observableValue.getValue().intValue() == 0) { // si c'est un humain
                    //on active les paramètres humain
                    best1.setVisible(true);
                    undo1.setDisable(false);
                    name1.setVisible(true);

                } else {
                    //on désactive les paramètres humain
                    best1.setVisible(false);
                    undo1.setDisable(true);
                    name1.setVisible(false);
                }
            }
        });
        this.type2.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                //on cherche la valeur du nouvel item
                if (observableValue.getValue().intValue() == 0) { // si c'est un humain
                    //on active les paramètres humain
                    best2.setVisible(true);
                    undo2.setDisable(false);
                    name2.setVisible(true);

                } else {
                    //on désactive les paramètres humain
                    best2.setVisible(false);
                    undo2.setDisable(true);
                    name2.setVisible(false);
                }
            }
        });

        /*System.out.println("le contrôleur initialise la vue");
        // utilisation de styles pour la grille et la tuile (voir styles.css)
        p.getStyleClass().add("pane"); 
        c.getStyleClass().add("tuile");
        grille.getStyleClass().add("gridpane");
        GridPane.setHalignment(c, HPos.CENTER);
        fond.getChildren().add(p);
        p.getChildren().add(c);

        // on place la tuile en précisant les coordonnées (x,y) du coin supérieur gauche
        p.setLayoutX(x);
        p.setLayoutY(y);
        p.setVisible(true);
        c.setVisible(true);*/
    }

    /**
     * active la configuration pour que l'utilisateur entre les paramètres de la partie
     */
    public void initChoix() {
        //masque éléments inutiles
        undo1.setVisible(false);
        undo2.setVisible(false);
        grille1.setVisible(false);
        grille2.setVisible(false);
        panneau_score1.setVisible(false);
        panneau_score2.setVisible(false);
        name1.setVisible(false);
        name2.setVisible(false);

        //ajoute options de type
        type1.getItems().add("Human");
        type1.getItems().add("AI");
        type1.getItems().add("Dumb");
        type2.getItems().add("Human");
        type2.getItems().add("AI");
        type2.getItems().add("Dumb");

        System.out.println("Affichage instructions paramètres");
        console.setText("Please select parameters and press Play");
    }
    
    public void initPartie(){
        //on passe à la configuration partie
            play.setVisible(false);
            type1.setDisable(true);
            type2.setDisable(true);
            name1.setDisable(true);
            name2.setDisable(true);
            
            //on affiche les undos pour les humains
            if(type1.getSelectionModel().getSelectedItem().equals("Human")) undo1.setVisible(true);
            if(type2.getSelectionModel().getSelectedItem().equals("Human")) undo2.setVisible(true);
            
            grille1.setVisible(true);
            grille2.setVisible(true);
            panneau_score1.setVisible(true);
            panneau_score2.setVisible(true);
            
            //on clear la console
            console.setText("");
            
            //on crée les joueurs
            if(type1.getSelectionModel().getSelectedItem().equals("Human")){
                this.partie.getJoueur()[0] = new Human(name1.getText().toLowerCase());
            } else if(type1.getSelectionModel().getSelectedItem().equals("Dumb")){
                this.partie.getJoueur()[0] = new Dumb();
            } else {
                this.partie.getJoueur()[0] = new IA();
            }
            
            if(type2.getSelectionModel().getSelectedItem().equals("Human")){
                this.partie.getJoueur()[1] = new Human(name2.getText().toLowerCase());
            } else if(type2.getSelectionModel().getSelectedItem().equals("Dumb")){
                this.partie.getJoueur()[1] = new Dumb();
            } else {
                this.partie.getJoueur()[1] = new IA();
            }
            
    }

    /*
     * Méthodes listeners pour gérer les événements (portent les mêmes noms que
     * dans Scene Builder
     */
    @FXML
    public void mouseClicked() {
        System.out.println("test");
    }

    /**
     * Le joueur appuie sur play, on vérifie si les paramètres sont corrects
     */
    @FXML
    public void playRequest() {
        // au moins un des joueurs n'a pas de type
        if (type1.getSelectionModel().getSelectedItem() == null || type2.getSelectionModel().getSelectedItem() == null) {
            console.setText("Please select the type of both players");
        } 
        // les joueurs sont humains et ont le même nom
        else if (name1.getText().toLowerCase().equals(name2.getText().toLowerCase()) && type1.getSelectionModel().getSelectedItem().equals("Human") && type2.getSelectionModel().getSelectedItem().equals("Human")) {
            console.setText("Error : two players can't have the same name");
        } 
        // le joueur 1 est humain et n'a pas de nom
        else if (name1.getText().equals("") && type1.getSelectionModel().getSelectedItem().equals("Human")) {
            console.setText("Error : player 1 is a human and must have a name");
        } 
        // le joueur 2 est humain et n'a pas de nom
        else if (name2.getText().equals("") && type2.getSelectionModel().getSelectedItem().equals("Human")) {
            console.setText("Error : player 2 is a human and must have a name");
        } 
        // tout va bien
        else {
            initPartie();
        }
    }

    @FXML
    public void keyPressed(KeyEvent ke) {
        System.out.println("touche appuyée");
        String touche = ke.getText();
        if (touche.compareTo("q") == 0) { // utilisateur appuie sur "q" pour envoyer la tuile vers la gauche
            if (objectifx > 24) { // possible uniquement si on est pas dans la colonne la plus à gauche
                objectifx -= (int) 397 / 4; // on définit la position que devra atteindre la tuile en abscisse (modèle). Le thread se chargera de mettre la vue à jour
                score.setText(Integer.toString(Integer.parseInt(score.getText()) + 1)); // mise à jour du compteur de mouvement
            }
        } else if (touche.compareTo("d") == 0) { // utilisateur appuie sur "d" pour envoyer la tuile vers la droite
            if (objectifx < (int) 445 - 2 * 397 / 4 - 24) { // possible uniquement si on est pas dans la colonne la plus à droite (taille de la fenêtre - 2*taille d'une case - taille entre la grille et le bord de la fenêtre)
                objectifx += (int) 397 / 4;
                score.setText(Integer.toString(Integer.parseInt(score.getText()) + 1));
            }
        }
        System.out.println("objectifx=" + objectifx);
        Task task = new Task<Void>() { // on définit une tâche parallèle pour mettre à jour la vue
            @Override
            public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                while (x != objectifx) { // si la tuile n'est pas à la place qu'on souhaite attendre en abscisse
                    if (x < objectifx) {
                        x += 1; // si on va vers la droite, on modifie la position de la tuile pixel par pixel vers la droite
                    } else {
                        x -= 1; // si on va vers la gauche, idem en décrémentant la valeur de x
                    }
                    // Platform.runLater est nécessaire en JavaFX car la GUI ne peut être modifiée que par le Thread courant, contrairement à Swing où on peut utiliser un autre Thread pour ça
                    Platform.runLater(new Runnable() { // classe anonyme
                        @Override
                        public void run() {
                            //javaFX operations should go here
                            p.relocate(x, y); // on déplace la tuile d'un pixel sur la vue, on attend 5ms et on recommence jusqu'à atteindre l'objectif
                            p.setVisible(true);
                        }
                    }
                    );
                    Thread.sleep(5);
                } // end while
                return null; // la méthode call doit obligatoirement retourner un objet. Ici on n'a rien de particulier à retourner. Du coup, on utilise le type Void (avec un V majuscule) : c'est un type spécial en Java auquel on ne peut assigner que la valeur null
            } // end call

        };
        Thread th = new Thread(task); // on crée un contrôleur de Thread
        th.setDaemon(true); // le Thread s'exécutera en arrière-plan (démon informatique)
        th.start(); // et on exécute le Thread pour mettre à jour la vue (déplacement continu de la tuile horizontalement)
    }
}
