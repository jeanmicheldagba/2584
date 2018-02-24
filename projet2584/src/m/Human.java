/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m;

import java.util.Scanner;

/**
 *
 * @author vaurien
 */
public class Human extends Joueur implements Parametres {

    private String pseudo;    
    private int nbUndo;
    protected Grille lastGrille;

    public Human(String pseudo, Partie partie, int id) {
        super(partie, id);
        this.pseudo = pseudo;
        this.lastGrille = (Grille) this.grille.clone();
        this.nbUndo = 5;
    }
    
    /**
     * 
     */
    public boolean undo() {
        if (this.nbUndo > 0) { // Si il lui reste des undo       
            this.nbUndo--; // On décrémente le nombre de undo disponible            
            this.grille = this.lastGrille; // La grille du joueur redevient la version précédente            
            System.out.println("Grille après undo :\n"+this.grille);
            System.out.println("Last grille après undo :\n"+this.lastGrille);
            
            return true;
        } else {
            System.out.println("Undo indisponible");
            return false;
        }
    }
    
    public int getNbUndo() {
        return this.nbUndo;
    }
    
    public void setLastGrille(Grille lastGrille) {
        this.lastGrille = lastGrille;
    }
    
    public Grille getGrille() {
        return this.grille;
    }

}