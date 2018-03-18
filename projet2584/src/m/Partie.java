package m;

import java.util.Random;
import vc.Controller;

public class Partie implements Parametres {

    private Joueur[] joueurs;
    protected Controller controller;
    private boolean gameover; //variable qui détermine la fin de la partie : true=partie finie

    public Partie(Controller controller) {
        this.joueurs = new Joueur[2];
        this.controller = controller;
        this.gameover = false;
    }
    
    public Joueur[] getJoueur(){
        return this.joueurs;
    }
    
    public boolean getGameover() {
        return this.gameover;
    }
    
    public void setGameover(boolean go){
        this.gameover=go;
    }

    /**
     * ajoute les deux cases à chaque grille en suivant les règles d'initialisation
     * 
     */
    public void initGrilles() {
        System.out.println("initialisation des grilles");
        
        Random ra = new Random();
        //stocke les coordonnées x et y des 2 cases
        int[] x = new int[2];
        int[] y = new int[2];

        //coordonnées x et y de la première case
        x[0] = ra.nextInt(TAILLE);
        y[0] = ra.nextInt(TAILLE);

        x[1] = x[0];
        y[1] = y[0];

        //coordonnées de la seconde case
        do {
            x[1] = ra.nextInt(TAILLE);
            y[1] = ra.nextInt(TAILLE);
        } while (x[1] == x[0] && y[1] == y[0]); // cases à endroit différent

        int[] valeurs = new int[2]; // stocke les valeurs des 2 cases

        //règles de valeurs des cases
        if (ra.nextDouble() > 0.75) { // probabilité de 0.25 d'avoir un 2
            valeurs[0] = 2;
            valeurs[1] = 1; // la deuxième case est 1 car il ne peut pas y avoir 2x2
        } else { 
            valeurs[0] = 1; 
            if (ra.nextDouble() > 0.75) { // probabilité de 0.25 d'avoir un 2
                valeurs[1] = 2;
            } else {
                valeurs[1] = 1;
            }
        }
        
        //ajoute les deux cases à la grille de chaque joueur
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                this.joueurs[i].getGrille().nouvelleCase(valeurs[j], x[j], y[j]);
            }

        }
    }
    /* ON GERE CE CAS DANS LA METHODE move DE LA CLASSE Joueur
    //détermine si un des joueurs est bloqué
    public boolean partieBloquee() {
        boolean bloque = false;
        for (int i = 0; i < 2; i++) {
            if (this.joueur[i].getGrille().bloquee()) {
                bloque = true;
                System.out.println("Le joueur " + (i + 1) + " a perdu !");
            }
        }
        return bloque;
    }
    */
}
