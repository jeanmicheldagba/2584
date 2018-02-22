package m;

import java.util.Random;
import java.util.Scanner;
import vc.Controller;

public class Partie implements Parametres {

    private Joueur[] joueur;
    private Controller controller;

    public Partie() {
        this.joueur = new Joueur[2];
    }
    
    public Joueur[] getJoueur(){
        return this.joueur;
    }

    public void init() {
        this.initGrilles();
        this.play();
    }

    public void initGrilles() {
        Random ra = new Random();
        int[] x = new int[2];
        int[] y = new int[2];

        //coordonnées de la première case
        x[0] = ra.nextInt(TAILLE);
        y[0] = ra.nextInt(TAILLE);

        x[1] = x[0];
        y[1] = y[0];

        //coordonnées de la seconde case
        do {
            x[1] = ra.nextInt(TAILLE);
            y[1] = ra.nextInt(TAILLE);
        } while (x[1] == x[0] && y[1] == y[0]);

        int[] valeurs = new int[2];

        //règles de valeurs des cases
        if (ra.nextDouble() > 0.75) {
            valeurs[0] = 2;
            valeurs[1] = 1;
        } else {
            valeurs[0] = 1;
            if (ra.nextDouble() > 0.75) {
                valeurs[1] = 2;
            } else {
                valeurs[1] = 1;
            }
        }
        
        //ajoute les deux cases à la grille de chaque joueur
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                this.joueur[i].getGrille().nouvelleCase(valeurs[j], x[j], y[j]);
            }

        }
    }

    public void play() {
        Scanner sc = new Scanner(System.in);

        while (!this.partieBloquee()) {
            int nbJoueurs = 1; // provisoire pour tests
            for (int i = 0; i < nbJoueurs; i++) {
                if (this.joueur[i] instanceof Human) {
                    this.joueur[i].jouer();
                }
            }
        }
        for (int i = 0; i < 2; i++) {
            this.joueur[i].gameOver();
        }
        System.exit(1);

    }

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

}