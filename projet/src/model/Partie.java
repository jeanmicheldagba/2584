package model;

import java.util.Random;
import java.util.Scanner;

public class Partie implements Parametres {

    private Joueur[] joueur;

    public Partie() {
        this.joueur = new Joueur[2];
    }

    public void init(boolean skip) {
        //skip sert à passer la phase d'initialisation pour accélerer le développement
        if (!skip) {
            this.initJeu();
        } else {
            this.joueur[0] = new Human("test1");
            this.joueur[1] = new Human("test2");
        }
        this.initGrilles();
        this.jouer();
    }

    public void initJeu() {
        Scanner sc = new Scanner(System.in);
        String s;
        String pseudo = "";

        System.out.println("Bienvenue !");

        //demande les paramètres du jeu (IA ou non, pseudo,...)
        for (int i = 0; i < 2; i++) {
            s = "blabla";
            do {
                System.out.println("Le joueur " + (i + 1) + " est-il un humain ? Tape o pour oui et n pour non.");
                s = sc.nextLine().toLowerCase();
            } while (!(s.equals("o") || s.equals("n")));

            if (s.equals("o")) {
                s = pseudo;
                do {
                    System.out.println("Veuillez entrer son pseudo :");
                    s = sc.nextLine();
                    if (s.equals(pseudo)) {
                        System.out.println("Erreur : pseudo indisponible !");
                    }
                } while (s.equals(pseudo) || s.equals(""));
                pseudo = s;

                this.joueur[i] = new Human(pseudo);
            } else {
                s = "blabla";
                do {
                    System.out.println("Est-ce un bête programme aléatoire ? Tape o pour oui et n pour non.");
                    s = sc.nextLine().toLowerCase();
                } while (!(s.equals("o") || s.equals("n")));

                if (s.equals("o")) {
                    this.joueur[i] = new Dumb();
                } else {
                    System.out.println("OK ! Le joueur " + (i + 1) + " est une IA.");
                    this.joueur[i] = new IA();
                }

            }
        }

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
            }
        }

        //ajoute les deux cases à la grille de chaque joueur
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                this.joueur[i].getGrille().nouvelleCase(valeurs[j], x[j], y[j]);
            }

        }
    }

    public void jouer() {
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
