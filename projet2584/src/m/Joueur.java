package m;

import java.util.Scanner;
import java.util.Random;

public abstract class Joueur implements Parametres {
    
    protected Grille grille;
    protected int score;
    
    public Joueur(){
        this.grille = new Grille();
        
        this.score = 0;
        this.score=0;
    }
    
    public Grille getGrille(){
        return this.grille;
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
    
    /**
     * bouge les cases
     * @param direction direction dans laquelle les cases doivent bouger
     * @return game over ?
     */
    public boolean move(int direction) {
        
        // On déplace les cases
        boolean casesMov = this.grille.lanceurDeplacerCases(direction);
        if (casesMov) {
            if (!this.grille.nouvelleCase()) { //la grille est pleine
                System.out.println("partie terminée, score : "+this.score);
                return true; //game over
            }
        }
        if (this.grille.getValeurMax() >= OBJECTIF) { // le joueur a atteint l'objectif
            System.out.println("Bravo ! Vous avez atteint " + this.grille.getValeurMax());
            return true; //game over
        }
        this.calculScore(); //on met à jour le score

        return false; //game not over
    }
    
}