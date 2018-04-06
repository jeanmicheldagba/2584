/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m;

import java.io.Serializable;
import java.util.Random;

/**
 *
 * @author vaurien
 */
public class Dumb extends Joueur implements Parametres, Serializable {
    
    /**
     * Constructeur joueur aléatoire
     * @param partie Partie à laquelle le joueur participe
     * @param id 
     */
    public Dumb(Partie partie, int id) {
        super(partie, id);
    }
    
    /**
     * Méthode qui choisit une direction au hasard
     * @return a random direction 
     */
    public int getDirection(){
        int[] directions = {HAUT, BAS, GAUCHE, DROITE};
        Random r = new Random();        
        return (directions[r.nextInt(directions.length)]);
    }
    
    
}