/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m;

import java.io.Serializable;
import java.util.Scanner;

/**
 *
 * @author vaurien
 */
public class Human extends Joueur implements Parametres, Serializable {

    private String pseudo;    
    private int nbUndo;
    protected Grille lastGrille;

    public Human(String pseudo, Partie partie, int id) {
        super(partie, id);
        this.pseudo = pseudo;
        this.lastGrille = (Grille) this.grille.clone();
        this.nbUndo = 5;
    }
    
    public String getPseudo(){
        return this.pseudo;
    }
    
    /**
     * Méthode qui permet de faire un undo
     * @return true si undo effectué, false sinon
     */
    public boolean undo() {
        if (this.nbUndo > 0) { // Si il lui reste des undo       
            this.nbUndo--; // On décrémente le nombre de undo disponible            
            this.grille = this.lastGrille; // La grille du joueur redevient la version précédente            
            return true;
        } else {
            System.out.println("Undo indisponible");
            return false;
        }
    }
    /**
     * Getter du nombre de undo disponibles
     * @return le nombre de undo (entre 5 et 0)
     */
    public int getNbUndo() {
        return this.nbUndo;
    }
    
    /**
     * Setter de la grille précédente
     * @param lastGrille nouvelle grille précédant la grille actuelle
     */
    public void setLastGrille(Grille lastGrille) {
        this.lastGrille = lastGrille;
    }

}