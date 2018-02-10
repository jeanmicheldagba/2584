package model;

import java.util.Scanner;
import java.util.Random;

public abstract class Joueur implements Parametres {
    
    protected Grille g;
    protected Grille lastG, sauvegardeG;
    private int score;
    protected int nbUndo;
    protected boolean dejaUndo;
    
    public Joueur(){
        this.g = new Grille();
        
        this.score = 0;
        this.nbUndo = 5;
        this.lastG = (Grille) this.g.clone();
        this.sauvegardeG = (Grille) this.g.clone();
        this.dejaUndo = true;
    }
    
    public Grille getGrille(){
        return this.g;
    }
    
    //methode jouer
    public abstract void jouer();
    
    public void gameOver(){
        System.out.println("La partie est terminée, votre score est "+this.score);
    }
    
}