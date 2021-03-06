/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vc;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import m.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;


/**
 * Classe Controller qui permet de mettre en relation la vue et les méthodes du jeu
 * @author jmdag
 * @author apollo7
 * @author vaurien
 */
public class Controller extends Thread implements Initializable, Parametres {

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
    private Pane fond1;
    @FXML
    private Pane fond2;
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
    private Pane best1; // panneau qui affiche le meilleur score
    @FXML
    private Pane best2; // panneau qui affiche le meilleur score
    @FXML
    private Label console; // user information area
    @FXML
    private Button play;
    @FXML
    private Button start;
    @FXML
    private Pane background;
    @FXML
    private Label score1;
    @FXML
    private Label score2;

    // variables globales non définies dans la vue (fichier .fxml)
    /*
    private final Pane p = new Pane(); // panneau utilisé pour dessiner une tuile "2"
    private final Label c = new Label("2");
    private int x = 24, y = 191;
    private int objectifx = 24, objectify = 191;
     */
    private Partie partie; // modèle
    private GridPane[] grilles;
    private Button[] undos;
    private ChoiceBox[] types;
    private Label[] scores;
    private HashSet<Thread>[] transitions;

    /**
     * 
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        start.setVisible(false);
        boolean retrieve = retrieve();
        
        if(!retrieve) {
        
            this.partie = new Partie(this); // crée la partie (modèle)
            this.initChoix(); // configuration paramètres
        }
        
        this.grille1.autosize();
        
        this.scores = new Label[]{this.score1, this.score2};

        // on ajoute les grilles au tableau de grilles
        this.grilles = new GridPane[2];
        this.grilles[0] = this.grille1;
        this.grilles[1] = this.grille2;

        //on ajoute la classe pour le css
        for (int i = 0; i < 2; i++) {
            grilles[i].getStyleClass().add("gridpane");
        }

        //on ajoute les undos au tableau de boutons
        this.undos = new Button[]{this.undo1, this.undo2};

        //on ajoute les types au tableau de types
        this.types = new ChoiceBox[]{this.type1, this.type2};

        //initialise les threads de transitions pour chaque joueur
        this.transitions = new HashSet[2];
        for(int i=0;i<2;i++) this.transitions[i] = new HashSet();
        
        
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
        
        if(retrieve) {
            initPartie(retrieve);
        }
    }

    /**
     * 
     */
    public void blink() {
        /**
         * thread pour faire clignoter la console et attirer l'attention de
         * l'utilisateur en cas de message
         */
        Task blink_task = new Task<Void>() {
            @Override
            public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                for (int i = 0; i < 6; i++) { //on effectue l'action 6 fois
                    // Platform.runLater est nécessaire en JavaFX car la GUI ne peut être modifiée que par le Thread courant, contrairement à Swing où on peut utiliser un autre Thread pour ça
                    Platform.runLater(new Runnable() { // classe anonyme
                        @Override
                        public void run() {
                            //javaFX operations should go here
                            console.setVisible(!console.visibleProperty().getValue()); // on inverse la visibilité de la console
                        }
                    });
                    Thread.sleep(170); //on met en pause le thread pendant 170 dt
                }
                return null; // la méthode call doit obligatoirement retourner un objet, donc on rend null
            }

        };
        Thread blink_thread = new Thread(blink_task); // on crée un contrôleur de Thread pour le blink de la console
        blink_thread.setDaemon(false);
        blink_thread.start();
    }

    /**
     * active la configuration pour que l'utilisateur entre les paramètres de la
     * partie
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
        
        console.setText("Please set parameters and press play");
        
    }

    /**
     * 
     * @param retrieve 
     */
    public void initPartie(boolean retrieve) {

        //on passe à la configuration partie
        play.setVisible(false);
        type1.setDisable(true);
        type2.setDisable(true);
        name1.setDisable(true);
        name2.setDisable(true);

        boolean undo;
        //on affiche les undos pour les humains en les désactivant (avant le premier mouvement)
        for (int i = 0; i < 2; i++) {
            undo = false;
        
            if (retrieve) {
                if(this.partie.getJoueur()[i] instanceof Human) {
                    undo = true;
                }
            } else if (types[i].getSelectionModel().getSelectedItem().equals("Human")) {
                undo = true;
            }
            
            if(undo) {
                undos[i].setVisible(true);
                undos[i].setDisable(true);
            }
        }

        grille1.setVisible(true);
        grille2.setVisible(true);
        panneau_score1.setVisible(true);
        panneau_score2.setVisible(true);

        //on clear la console
        console.setText("");
        
        if(!retrieve) {
            //on crée les joueurs
            if (type1.getSelectionModel().getSelectedItem().equals("Human")) {
                this.partie.getJoueur()[0] = new Human(name1.getText().toLowerCase(), this.partie, 0);
            } else if (type1.getSelectionModel().getSelectedItem().equals("Dumb")) {
                this.partie.getJoueur()[0] = new Dumb(this.partie, 0);
            } else {
                IA ia = new IA(this.partie, 0);
                ia.setBot();
                this.partie.getJoueur()[0] = ia;
            }

            if (type2.getSelectionModel().getSelectedItem().equals("Human")) {
                this.partie.getJoueur()[1] = new Human(name2.getText().toLowerCase(), this.partie, 1);
            } else if (type2.getSelectionModel().getSelectedItem().equals("Dumb")) {
                this.partie.getJoueur()[1] = new Dumb(this.partie, 1);
            } else {
                IA ia = new IA(this.partie, 1);
                ia.setBot();
                this.partie.getJoueur()[1] = ia;
            }
            
            
            this.partie.initGrilles(); //initialise les grilles en ajoutant les premières cases
        }
        
        syncGrilles(2);
        
        if(!(this.partie.getJoueur()[0] instanceof Human || this.partie.getJoueur()[1] instanceof Human)) {
            start.setVisible(true);
        }

    }

    /**
     * synchronize the grid of the model and the view
     *
     * @param player the index of the player whose grid needs to be synchronized
     * : 0, 1 or 2 (both players)
     */
    public void syncGrilles(final int player) {
       
        
        try {
            Task delete_node = new Task<Void>() { // on définit la tâche confiée au processus principal de parcourir la liste des noeuds à supprimer et de les supprimer
                @Override
                public Void call() throws Exception { 
                    Platform.runLater(new Runnable() { 
                        @Override
                        public void run() {
                            //javaFX operations should go here
                            int i;
                            i = (player == 2) ? 0 : player;
                            do {
                                Node sauv = grilles[i].getChildren().get(0); //sauvegarde l'élément visuel des grilles (traits) de la gridpane
                                grilles[i].getChildren().clear();
                                grilles[i].getChildren().add(sauv);
                                for (Case c : partie.getJoueur()[i].getGrille().getCases()) { //pour chaque case
                                    nouvelleCaseGUI(c, i);
                                }

                                i++;
                            } while (i < 2 && player == 2); //fait ça deux foix si player == 2
                        }
                    });

                    return null; 
                }

            };
            Thread delete_node_thread = new Thread(delete_node); 
            delete_node_thread.setDaemon(false);
            delete_node_thread.start(); // on exécute la tâche de suppression des nodes
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        
    }

    /**
     * synchronize the model score and the view
     *
     * @param playerInd the index of the player whose score needs to be synchronized
     */
    public void syncScores(int playerInd) {
        if (playerInd == 0 || playerInd == 1){
            this.scores[playerInd].setText(this.partie.getJoueur()[playerInd].getScore()+"");
        }
    }

    /**
     * ajoute une case à l'interface
     * @param new_case nouvelle case de l'interface
     * @param playerInd le joueur a qui appartient la case
     */
    public void nouvelleCaseGUI(Case new_case, int playerInd) {
        int val = new_case.getValeur();
        int x = new_case.getX();
        int y = new_case.getY();
        
        final Pane pane_tuile = new Pane(); //crée conteneur
        final Label label_tuile = new Label(val + ""); //crée label avec valeur de la case
        pane_tuile.getStyleClass().add("pane_tuile"); //ajoute classe pour css
        label_tuile.getStyleClass().add("label_tuile"); //ajoute classe pour css
        pane_tuile.getChildren().add(label_tuile); //ajoute label au conteneur
        
        try {
            Task delete_node = new Task<Void>() { // on définit la tâche confiée au processus principal de parcourir la liste des noeuds à supprimer et de les supprimer
                @Override
                public Void call() throws Exception { 
                    Platform.runLater(new Runnable() { 
                        @Override
                        public void run() {
                            //javaFX operations should go here
                            
                            grilles[playerInd].add(pane_tuile, x, y); //ajoute conteneur dans la case de la gridpane correspondant aux coordonnées de la case modèle
                            //affiche les éléments
                            pane_tuile.setVisible(true);
                            label_tuile.setVisible(true);
                        }
                    });

                    return null; 
                }

            };
            Thread delete_node_thread = new Thread(delete_node); 
            delete_node_thread.setDaemon(false);
            delete_node_thread.start(); // on exécute la tâche de suppression des nodes
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**
     * enlève la case de l'interface
     *
     * @param enlev case à enlever
     */
    public void enleverCaseGUI(Case enlev) {
        int playerInd = enlev.getGrille().getJoueur().getID();
        final ObservableList<Node> children = this.grilles[playerInd].getChildren();
        Pane paneCase;
        boolean found = false;
        ArrayList<Node> toRemove = new ArrayList<>(); // on récupère la liste de tous les noeuds à supprimer

        Iterator<Node> it = children.iterator();

        //cherche le noeud correspondant        
        while (!found && it.hasNext()) { //itère noeuds pour trouver case
            Node node = it.next();
            if (!node.equals(children.get(0))) { //on n'itère pas les lignes de la grille
                if (grilles[playerInd].getRowIndex(node) == enlev.getGuiY() && grilles[playerInd].getColumnIndex(node) == enlev.getGuiX()) {
                    paneCase = (Pane) node;

                    Label labelCase = (Label) paneCase.getChildren().get(0); 

                    if (enlev.getValeur() == Integer.valueOf(labelCase.getText())) {

                        found = true;
                        toRemove.add(node); // on ajoute le noeud à supprimer dans la liste

                    }

                }
            }
        }
        if(found) {
            try {
                Task delete_node = new Task<Void>() { // on définit la tâche confiée au processus principal de parcourir la liste des noeuds à supprimer et de les supprimer
                    @Override
                    public Void call() throws Exception { 
                        Platform.runLater(new Runnable() { 
                            @Override
                            public void run() {
                                //javaFX operations should go here
                                for (Node n : toRemove) {
                                    children.remove(n);
                                }
                            }
                        });

                        return null; 
                    }

                };
                Thread delete_node_thread = new Thread(delete_node); 
                delete_node_thread.setDaemon(false);
                delete_node_thread.start(); // on exécute la tâche de suppression des nodes
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * change la case de cellule de la grille
     *
     * @param move la case à déplacer
     */
    public void deplacerCaseGUI(Case move) {
        int playerInd = move.getGrille().getJoueur().getID();
        
        enleverCaseGUI(move); //enlève la case de la grille
        
        this.nouvelleCaseGUI(move, playerInd); //ajoute la case aux nouvelles coordonnées
        
        
    }

    /**
     * actualise la valeur d'une case
     *
     * @param c la case à modifier
     * @param newVal la nouvelle valeur
     */
    public void updateValueGUI(Case c, int newVal) {
        Pane paneCase;
        int playerInd = c.getGrille().getJoueur().getID(); //cherche à quel joueur est la case
        ObservableList<Node> children = this.grilles[playerInd].getChildren(); //enfants de la grille
        

        //cherche le noeud correspondant
        for (Node node : children) {//itère noeuds de la grille
            if(!node.equals(children.get(0))){ //on n'itère pas les lignes de la grille
                if (grilles[playerInd].getRowIndex(node) == c.getGuiY() && grilles[playerInd].getColumnIndex(node) == c.getGuiX()) { //si noeud correspond à la case
                    paneCase = (Pane) node;
                    Label labelCase = (Label) paneCase.getChildren().get(0); //cherche label dans pane

                    if(c.getValeur() == Integer.valueOf(labelCase.getText())){
                        labelCase.setText("" + (newVal)); //actualise le label avec la nouvelle valeur
                        break;
                    }
                }
            }
                
        }
    }
    
    @Override
    public String toString() {
        String s = "";
        Node node;
        Pane p;
        Label l;

        for (int i = 0; i < 2; i++) {
            s += "Cases du joueur " + (i + 1) + " :\n";
            for (int j = 1; j < grilles[i].getChildren().size(); j++) {
                node = grilles[i].getChildren().get(j);
                p = (Pane) grilles[i].getChildren().get(j);
                l = (Label) p.getChildren().get(0);
                s += "valeur " + l.getText() + " en " + (grilles[i].getColumnIndex(node) + 1) + "," + (3 - grilles[i].getRowIndex(node) + 1);
                s += "\n";

            }

            s += "\n";

        }

        return s;
    }

    /**
     * move smoothly the pane to the new tile position
     *
     * @param move la case à bouger. final pour pouvoir l'utiliser dans une task
     *
     */
    public void transition(final Case move, final boolean remove) {      
        int playerInd = move.getGrille().getJoueur().getID();
        Pane paneToMov = null;
        ObservableList<Node> children = grilles[playerInd].getChildren();

        //cherche le noeud correspondant
        for (Node node : children) { //itère noeuds
            if (!node.equals(children.get(0))) { //on n'itère pas les lignes de la grille
                if (grilles[playerInd].getRowIndex(node) == move.getGuiY() && grilles[playerInd].getColumnIndex(node) == move.getGuiX()) { //si noeud correspond à la case
                    paneToMov = (Pane) node;
                    Label labelCase = (Label) paneToMov.getChildren().get(0); //cherche label dans pane

                    if (move.getValeur() == Integer.valueOf(labelCase.getText())) { //check value
                        break;
                    }

                }
            }
        }

        if (paneToMov == null) {
            System.out.println("ERREUR : tile not found");
        } else {
            int toMovX = 100 * (move.getX() - move.getGuiX());
            int toMovY = 100 * (move.getY() - move.getGuiY());
            move.setObjectifTranslateX(toMovX);
            move.setObjectifTranslateY(toMovY);
            
            final Pane final_pane = paneToMov;
            final_pane.setTranslateX(0);
            final_pane.setTranslateY(0);        
            
            final Controller controller = this;

            Task transition_task = new Task<Void>() {
                @Override
                public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                    while (final_pane.getTranslateX() != move.getObjectifTranslateX() || final_pane.getTranslateY() != move.getObjectifTranslateY()) {
                        // Platform.runLater est nécessaire en JavaFX car la GUI ne peut être modifiée que par le Thread courant, contrairement à Swing où on peut utiliser un autre Thread pour ça
                        Platform.runLater(new Runnable() { // classe anonyme
                            @Override
                            public void run() {
                                if(final_pane.getTranslateX() != move.getObjectifTranslateX()) {
                                    if(final_pane.getTranslateX() > move.getObjectifTranslateX()) {
                                        final_pane.setTranslateX(final_pane.getTranslateX() - 5);
                                    } else final_pane.setTranslateX(final_pane.getTranslateX() + 5);
                                    
                                }
                                if(final_pane.getTranslateY() != move.getObjectifTranslateY()) {
                                    if(final_pane.getTranslateY() > move.getObjectifTranslateY()) {
                                        final_pane.setTranslateY(final_pane.getTranslateY() - 5);
                                    } else final_pane.setTranslateY(final_pane.getTranslateY() + 5);
                                    
                                }
                            }
                        });
                        Thread.sleep(2);
                        
                    }
                    
                    if(remove) {
                        enleverCaseGUI(move);
                    }
                    
                    else{
                        controller.deplacerCaseGUI(move);
                        
                        //actualise coordonnées GUI
                        move.setGuiX(move.getX());
                        move.setGuiY(move.getY());
                    }
                    return null;
                }

            };
            Thread transition_thread = new Thread(transition_task); // on crée un contrôleur de Thread
            this.transitions[playerInd].add(transition_thread);
            transition_thread.start();
        }
    }
    
    /**
    * listens if all transitions are finished
    * @param playerInd 
    */
    public void transitionsFinishedListener(final int playerInd) {        
        Task finished_task = new Task<Void>() {
            @Override
            public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                while (!transitions[playerInd].isEmpty()) {
                    // Platform.runLater est nécessaire en JavaFX car la GUI ne peut être modifiée que par le Thread courant, contrairement à Swing où on peut utiliser un autre Thread pour ça
                    Platform.runLater(new Runnable() { // classe anonyme
                        @Override
                        public void run() {
                            Iterator<Thread> it = transitions[playerInd].iterator();
                            while (it.hasNext()) {
                                Thread transition = it.next();
                                if(!transition.isAlive()){
                                    it.remove();
                                }
                            }
                        }
                    });
                    Thread.sleep(40);

                }
                return null;
            }

        };
        Thread finished_thread = new Thread(finished_task); // on crée un contrôleur de Thread
        finished_thread.start();
    }
    
    /**
     * jeu automatique quand il n'y a pas de joueurs humain
     */
    public void automaticPlay() {
        Task automatic_play_task = new Task<Void>() {
            @Override
            public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                while(!partie.getGameover()){
                    
                        // Platform.runLater est nécessaire en JavaFX car la GUI ne peut être modifiée que par le Thread courant, contrairement à Swing où on peut utiliser un autre Thread pour ça
                        Platform.runLater(new Runnable() { // classe anonyme
                            @Override
                            public void run() {
                                automaticMove();
                            }
                        });
                        syncGrilles(2);
                    
                    Thread.sleep(600);
                    
                }
                // Platform.runLater est nécessaire en JavaFX car la GUI ne peut être modifiée que par le Thread courant, contrairement à Swing où on peut utiliser un autre Thread pour ça
                Platform.runLater(new Runnable() { // classe anonyme
                    @Override
                    public void run() {
                        console.setText("Game Over");
                    }
                });
                return null;
            }
        };
        Thread automatic_play_thread = new Thread(automatic_play_task); // on crée un contrôleur de Thread
        automatic_play_thread.start();
    }
    
    /**
     * fait bouger les joueurs non-humain du plateau
     */
    public void automaticMove(){
        for(int i=0; i<2; i++) {
            if(this.partie.getJoueur()[i] instanceof IA){
                IA ia = (IA) this.partie.getJoueur()[i];
                int dir = ia.getDirection();
                partie.getJoueur()[i].move(dir);
                syncScores(i);
            } else if(this.partie.getJoueur()[i] instanceof Dumb){
                Dumb dumb = (Dumb) this.partie.getJoueur()[i];
                partie.getJoueur()[i].move(dumb.getDirection());
                syncScores(i);
            }
        }
        
    }
    
    /**
     * On refocus la fenêtre pour enlever le bug des entrées clavier
     */
    @FXML
    public void refocus() {
        System.out.println(this);
        System.out.println("Refocus de la fenêtre");
        background.getScene().addEventFilter(KeyEvent.KEY_PRESSED,
                event2 -> keyPressed(event2));
    }
    
    /**
     * lancement du jeu automatique
     */
    @FXML
    public void start() {
        start.setVisible(false);
        automaticPlay();
    }

    /**
     * Le joueur appuie sur play, on vérifie si les paramètres sont corrects. Si
     * incorrect on explique pourquoi dans la console. Si correct on appelle la
     * méthode initPartie
     */
    @FXML
    public void playRequest() {
        // au moins un des joueurs n'a pas de type
        if (type1.getSelectionModel().getSelectedItem() == null || type2.getSelectionModel().getSelectedItem() == null) {
            console.setText("Please select the type of both players");
            blink(); // on fait clignoter la console
        } // les joueurs sont humains et ont le même nom
        else if (name1.getText().toLowerCase().equals(name2.getText().toLowerCase()) && type1.getSelectionModel().getSelectedItem().equals("Human") && type2.getSelectionModel().getSelectedItem().equals("Human")) {
            console.setText("Error : two players can't have the same name");
            blink(); // on fait clignoter la console
        } // le joueur 1 est humain et n'a pas de nom
        else if (name1.getText().equals("") && type1.getSelectionModel().getSelectedItem().equals("Human")) {
            console.setText("Error : player 1 is a human and must have a name");
            blink(); // on fait clignoter la console
        } // le joueur 2 est humain et n'a pas de nom
        else if (name2.getText().equals("") && type2.getSelectionModel().getSelectedItem().equals("Human")) {
            console.setText("Error : player 2 is a human and must have a name");
            blink(); // on fait clignoter la console
        } // tout va bien
        else {
            initPartie(false);
        }
    }

    /**
     * Undo GUI
     * @param mouse quand un joueur clique sur undo
     */
    @FXML
    public void undo(Event mouse) {
        //trouve le joueur qui a appuyé sur undo
        Button butn = (Button) mouse.getSource();
        int playerInd = butn.getId().equals("undo1") ? 0 : 1;
        boolean undone = false;
        Human playerObj = null;
        try {
            playerObj = (Human) this.partie.getJoueur()[playerInd]; //va chercher joueur      
            undone = playerObj.undo();//appelle la méthode undo de Human
        } catch (Exception e) {
            System.out.println("FATAL ERROR : undo from non-human player");
            System.exit(0);
        }

        if (playerObj.getNbUndo() > 0) {
            undos[playerInd].setText("Undo (" + playerObj.getNbUndo() + ")"); //actualise le nombre de undo restant
        } else {
            undos[playerInd].setVisible(false); //plus de undo, bouton devient invisible
        }

        //synchronise modèle et vue
        syncGrilles(playerInd);

        //on désactive bouton undo
        this.undos[playerInd].setDisable(true);

    }
    
    /**
     * Déplacement des cases à chaque appuie d'une touche
     * @param key la touche pressée
     */
    @FXML
    public void keyPressed(KeyEvent key) {
        //on cherche qui a pressé la touche
        int playerInd;
        if (Arrays.asList(KEYS[0]).contains(key.getText()) && this.partie.getJoueur()[0] instanceof Human) { // la touche est une touche du joueur 1
            playerInd = 0;
        } else if (Arrays.asList(KEYS[1]).contains(key.getText()) && this.partie.getJoueur()[1] instanceof Human) { // la touche est une touche du joueur 2
            playerInd = 1;
        } else { // la touche n'est pas une touche définie
            playerInd = -1;
            System.out.println("undefined key pressed");
        }

        if (this.play.visibleProperty().getValue()) { // on vérifie que la partie est commencée
            System.out.println("start game first");
        } else {
            if (playerInd != -1){ //si un des joueurs a pressé la touche  
                if(!this.partie.getGameover()) { //si la partie n'est pas finie          
                    Joueur playerObj = this.partie.getJoueur()[playerInd]; // on cherche le joueur

                    Grille sauv = null;

                    if (playerObj instanceof Human) { //si le joueur est humain
                        Human human = (Human) playerObj;
                        sauv = (Grille) human.getGrille().clone(); // On sauvegarde la grille actuelle pour undo
                    }

                    this.partie.getJoueur()[playerInd].move(Parametres.keyToDirection(key.getText())); // on appelle la méthode pour bouger avec la direction (en utilisant la fonction de conversion de Parametres)                
                    transitionsFinishedListener(playerInd);

                    syncScores(playerInd);

                    if (playerObj instanceof Human && this.partie.getJoueur()[playerInd].getMoved()) {
                        //le joueur a bougé, il peut maintenant undo
                        this.undos[playerInd].setDisable(false);
                        Human human = (Human) playerObj;
                        human.setLastGrille(sauv);

                        automaticMove();
                    }
                } else {
                    console.setText("Game Over");
                    blink();
                }
            }

        }

    }
    
    /**
     * Permet la sérialisation d'une partie
     */
    public void serialize() {
        System.out.println("serialize");
        
        ObjectOutputStream oos = null;
        try {
            final FileOutputStream fichier = new FileOutputStream("partie.ser");
            oos = new ObjectOutputStream(fichier);
            oos.writeObject(this.partie);
            oos.flush();
        } catch (final java.io.IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (oos != null) {
                    oos.flush();
                    oos.close();
                }
            } catch (final IOException ex) {
                ex.printStackTrace();
            
            }
        }
        
        System.exit(0);
    
    }
    
    /**
     * 
     * @return 
     */
    public boolean retrieve() {
        
        boolean success = true;
        
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try {
            final FileInputStream fichierIn = new FileInputStream("partie.ser");
            ois = new ObjectInputStream(fichierIn);
            partie = (Partie) ois.readObject();
            partie.setController(this);
        } catch (final java.io.IOException e) {
            //e.printStackTrace();
            success = false;
        } catch (final ClassNotFoundException e) {
            //e.printStackTrace();
            success = false;
        } catch (final Exception e) {
            success = false;
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (final IOException ex) {
                ex.printStackTrace();
            }

        }
        
        System.out.println("retrieve : "+success);
        return success;
    }
    
    /**
     * Montre les 10 dernières parties
     */
    public void showBDD() {
        ArrayList<String> tuples;
        String display = "";
        try{
            tuples = this.partie.getConnexionBDD().getTuples("SELECT * FROM historiqueparties WHERE Id > (SELECT MAX(Id) - 10 FROM historiqueparties)");
            for(String s : tuples) {
                display+=s+"\n";
            }
        } catch(Exception e) {
            display = "No game stored";
        }
        
            
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Stored games");
        alert.setHeaderText("Stored games");
        alert.setContentText(display);

        alert.showAndWait();
    }
}
