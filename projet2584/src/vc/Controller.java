/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vc;

import m.*;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.scene.Node;
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
import javafx.util.Duration;
//import javafx.scene.paint.Color;
//import javafx.scene.text.Font;

/**
 *
 * @author castagno
 */
public class Controller implements Initializable, Parametres {

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
    private Pane best1; // panneau qui affiche le best score
    @FXML
    private Pane best2; // panneau qui affiche le best score
    @FXML
    private Label console; // user information area
    @FXML
    private Button play;
    @FXML
    private Pane background;

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
    private HashSet<Case> toMove;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        this.partie = new Partie(this); // crée la partie (modèle)
        this.initChoix(); // configuration paramètres
        this.grille1.autosize();

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
    }

    public HashSet<Case> getToMove() {
        return this.toMove;
    }

    public void setToMove(HashSet<Case> toMove) {
        this.toMove = toMove;
    }

    public void blink() {
        /**
         * thread pour faire clignoter la console et attirer l'attention de
         * l'utilisateur en cas de message
         */
        Task blink_task = new Task<Void>() {
            @Override
            public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                System.out.println("blink");
                for (int i = 0; i < 6; i++) { //on effectue l'action 6 fois
                    // Platform.runLater est nécessaire en JavaFX car la GUI ne peut être modifiée que par le Thread courant, contrairement à Swing où on peut utiliser un autre Thread pour ça
                    Platform.runLater(new Runnable() { // classe anonyme
                        @Override
                        public void run() {
                            //javaFX operations should go here
                            console.setVisible(!console.visibleProperty().getValue()); // on inverse la visibilité de la console
                        }
                    }
                    );
                    Thread.sleep(170); //on met en pause le thread pendant 170 dt
                }
                return null; // la méthode call doit obligatoirement retourner un objet, donc on rend null
            }

        };
        Thread blink_thread = new Thread(blink_task); // on crée un contrôleur de Thread pour le blink de la console
        blink_thread.setDaemon(true); // le Thread qui fait blink la console s'exécutera en arrière-plan une fois appellé(démon informatique)
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

        System.out.println("Affichage instructions paramètres");
        console.setText("Please set parameters and press Play");
    }

    public void initPartie() {

        //on passe à la configuration partie
        play.setVisible(false);
        type1.setDisable(true);
        type2.setDisable(true);
        name1.setDisable(true);
        name2.setDisable(true);

        //on affiche les undos pour les humains en les désactivant (avant le premier mouvement)
        for (int i = 0; i < 2; i++) {
            if (types[i].getSelectionModel().getSelectedItem().equals("Human")) {
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

        //on crée les joueurs
        if (type1.getSelectionModel().getSelectedItem().equals("Human")) {
            this.partie.getJoueur()[0] = new Human(name1.getText().toLowerCase(), this.partie, 0);
        } else if (type1.getSelectionModel().getSelectedItem().equals("Dumb")) {
            this.partie.getJoueur()[0] = new Dumb(this.partie, 0);
        } else {
            this.partie.getJoueur()[0] = new IA(this.partie, 0);
        }

        if (type2.getSelectionModel().getSelectedItem().equals("Human")) {
            this.partie.getJoueur()[1] = new Human(name2.getText().toLowerCase(), this.partie, 1);
        } else if (type2.getSelectionModel().getSelectedItem().equals("Dumb")) {
            this.partie.getJoueur()[1] = new Dumb(this.partie, 1);
        } else {
            this.partie.getJoueur()[1] = new IA(this.partie, 1);
        }

        this.partie.initGrilles(); //initialise les grilles en ajoutant les premières cases
        this.syncGrilles(2); //synchronise les grilles Vues et les grilles Modèle

    }

    /**
     * synchronize the grid of the model and the view
     *
     * @param player the index of the player whose grid needs to be synchronized
     * : 0, 1 or 2 (both players)
     */
    public void syncGrilles(int player) {
        int i;
        i = player == 2 ? 0 : player;
        do {
            grilles[i].getChildren().clear();
            for (Case c : this.partie.getJoueur()[i].getGrille().getCases()) { //pour chaque case
                Pane pane_tuile = new Pane(); //crée conteneur
                Label label_tuile = new Label(String.valueOf(c.getValeur())); //crée label avec valeur de la case
                pane_tuile.getStyleClass().add("pane_tuile"); //ajoute classe pour css
                label_tuile.getStyleClass().add("label_tuile"); //ajoute classe pour css
                grilles[i].add(pane_tuile, c.getX(), c.getY()); //ajoute conteneur dans la case de la gridpane correspondant aux coordonnées de la case modèle
                pane_tuile.getChildren().add(label_tuile); //ajoute label au conteneur

                //affiche les éléments
                pane_tuile.setVisible(true);
                label_tuile.setVisible(true);

            }

            i++;
        } while (i < 2 && player == 2); //fait ça deux foix si player == 2
    }

    /**
     * synchronize the model score and the view
     *
     * @param player the index of the player whose score needs to be
     * synchronized
     */
    public void syncScores(int player) {
        System.out.println("syncScores à implémenter");
    }

    /**
     *
     * @param player the player whose pane needs to be moved
     * @param preX previous X position
     * @param preY previous Y position
     * @param postX wanted X position
     * @param postY wanted Y position
     */
    public void deplacerCaseGUI(int player) {
        grilles[player].getChildren().get(0).relocate(2, 2);

        /*Task task = new Task<Void>() { // on définit une tâche parallèle pour mettre à jour la vue
            @Override
            public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                while (!toMove.isEmpty()) { //tant qu'il reste des cases à déplacer                   
                    // Platform.runLater est nécessaire en JavaFX car la GUI ne peut être modifiée que par le Thread courant, contrairement à Swing où on peut utiliser un autre Thread pour ça
                    Platform.runLater(new Runnable() { // classe anonyme
                        @Override
                        public void run() {

                            for (Case move : toMove) { //pour chaque case à déplacer
                                Pane paneToMov = null;
                                ObservableList<Node> children = grilles[player].getChildren();
                                
                                //cherche le noeud correspondant
                                for (Node node : children) {
                                    //System.out.println(move.getGuiX()+" "+move.getGuiY());
                                    //System.out.println(grilles[0].getColumnIndex(node)+" "+grilles[0].getRowIndex(node));
                                    
                                    if (grilles[player].getRowIndex(node) == move.getGuiY() && grilles[player].getColumnIndex(node) == move.getGuiX()) {
                                        System.out.println("trouve");
                                        paneToMov = (Pane) node;
                                        break;
                                    }
                                }
                                if (paneToMov == null) {
                                    System.out.println("ERREUR : tile not found");
                                }

                                //cherche coordonnées du conteneur
                                Bounds boundsPane = paneToMov.getLayoutBounds();
                                Point2D coordinatesPane = paneToMov.localToScene(boundsPane.getMinX(), boundsPane.getMinY());
                                int paneX = (int) coordinatesPane.getX();
                                int paneY = (int) coordinatesPane.getY();

                                //cherche coordonnées de la grille
                                Bounds boundsGrid = grilles[move.getGrille().getJoueur().getID()].getLayoutBounds();
                                Point2D coordinatesGrid = grilles[move.getGrille().getJoueur().getID()].localToScene(boundsGrid.getMinX(), boundsGrid.getMinY());
                                int gridX = (int) coordinatesGrid.getX();
                                int gridY = (int) coordinatesGrid.getY();

                                //calcule objectif
                                int objectifX = gridX + (100 * move.getX()); //on calcule l'objectif de X
                                int objectifY = gridY + (100 * move.getY()); //on calcule l'objectif de Y

                                if (paneX != objectifX || paneY != objectifY) {
                                    if (paneX < objectifX) {
                                        paneX += 1; // si on va vers la droite
                                    } else if (paneX > objectifX) {
                                        paneX -= 1; // si on va vers la gauche
                                    } else if (paneY < objectifX) {
                                        paneY += 1; // si on va vers le bas
                                    } else {
                                        paneY -= 1; // si on va vers le haut
                                    }
                                    
                                    System.out.println("deplacement");
                                    paneToMov.relocate(2, 2); // on déplace la tuile d'un pixel sur la vue, on attend 5ms et on recommence jusqu'à atteindre l'objectif

                                } else {
                                    toMove.remove(move);
                                }
                            }
                        }
                    }
                    );
                    Thread.sleep(5);
                }
                return null; // la méthode call doit obligatoirement retourner un objet. Ici on n'a rien de particulier à retourner. Du coup, on utilise le type Void (avec un V majuscule) : c'est un type spécial en Java auquel on ne peut assigner que la valeur null
            } // end call

        };
        Thread th = new Thread(task); // on crée un contrôleur de Thread
        th.setDaemon(true); // le Thread s'exécutera en arrière-plan (démon informatique)
        th.start(); // et on exécute le Thread pour mettre à jour la vue (déplacement continu de la tuile horizontalement)
        */
    }

    /*
     * Méthodes listeners pour gérer les événements (portent les mêmes noms que
     * dans Scene Builder
     */
    /**
     * On refocus la fenêtre pour enlever le bug des entrées clavier
     */
    @FXML
    public void refocus() {
        System.out.println("Refocus de la fenêtre");
        background.getScene().addEventFilter(KeyEvent.KEY_PRESSED,
                event2 -> keyPressed(event2));
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
            initPartie();
        }
    }

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

        if (undone) {
            undos[playerInd].setText("Undo (" + String.valueOf(playerObj.getNbUndo()) + ")"); //actualise le nombre de undo restant
        } else {
            undos[playerInd].setVisible(false); //plus de undo, bouton devient invisible
        }

        //synchronise modèle et vue
        syncGrilles(playerInd);

        //on désactive bouton undo
        this.undos[playerInd].setDisable(true);

    }

    @FXML
    public void keyPressed(KeyEvent key) {
        //on cherche qui a pressé la touche
        int playerInd;
        if (Arrays.asList(KEYS[0]).contains(key.getText())) { // la touche est une touche du joueur 1
            playerInd = 0;
            System.out.println("player 1 key pressed");
        } else if (Arrays.asList(KEYS[1]).contains(key.getText())) { // la touche est une touche du joueur 2
            playerInd = 1;
            System.out.println("player 2 key pressed");
        } else { // la touche n'est pas une touche définie
            playerInd = -1;
            System.out.println("undefined key pressed");
        }

        if (this.play.visibleProperty().getValue()) { // on vérifie que la partie est commencée
            System.out.println("start game first");
        } else {

            if (playerInd != -1) { //si un des joueurs a pressé la touche
                Joueur playerObj = this.partie.getJoueur()[playerInd];
                if (playerObj instanceof Human) { //si le joueur est humain
                    Human human = (Human) playerObj;
                    human.setLastGrille((Grille) human.getGrille().clone()); // On sauvegarde la grille actuelle
                }

                this.partie.getJoueur()[playerInd].move(Parametres.keyToDirection(key.getText())); // on appelle la méthode pour bouger avec la direction (en utilisant la fonction de conversion de Parametres)
                
                syncGrilles(playerInd);
                //this.deplacerCaseGUI(playerInd);
                
                //actualise score interface
                syncScores(playerInd);

                //le joueur a bougé, il peut maintenant undo
                this.undos[playerInd].setDisable(false);

                
            }

        }
        

    }
}
