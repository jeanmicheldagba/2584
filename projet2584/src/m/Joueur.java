package m;

import java.util.Scanner;
import java.util.Random;

public abstract class Joueur implements Parametres {
    
    protected Grille g;
    protected Grille lastG;
    protected int score;
    protected int nbUndo;
    protected boolean dejaUndo;
    
    public Joueur(){
        this.g = new Grille();
        
        this.score = 0;
        this.lastG = (Grille) this.g.clone();
        this.score=0;
        this.nbUndo = 5;
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
    
    /*public void setScore(int s){
        this.score=s;
    }
    
    public int getScore(){
        return this.score;
    }*/
    
    // Méthode qui permet de calculer le du score du joueur
    protected void calculScore(){
        if(g.getDeplacement()==true){// pas forcément utile A REVOIR
            this.score=this.score+g.getResDeplacement();
            g.setResDeplacement(0);
        }
    }
    
}