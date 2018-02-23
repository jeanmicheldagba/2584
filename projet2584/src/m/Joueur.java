package m;

import java.util.Scanner;
import java.util.Random;

public abstract class Joueur implements Parametres {
    
    protected Grille grille;
    protected Grille lastGrille;
    protected int score;
    protected int nbUndo;
    protected boolean dejaUndo;
    
    public Joueur(){
        this.grille = new Grille();
        
        this.score = 0;
        this.lastGrille = (Grille) this.grille.clone();
        this.score=0;
        this.nbUndo = 5;
        this.dejaUndo = true;
    }
    
    public Grille getGrille(){
        return this.grille;
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
        if(grille.getDeplacement()==true){// pas forcément utile A REVOIR
            this.score=this.score+grille.getResDeplacement();
            grille.setResDeplacement(0);
        }
    }
    
}