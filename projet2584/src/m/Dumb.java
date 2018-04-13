/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m;

import java.io.Serializable;
import java.util.Random;

/**
 * @author jmd
 * @author vaurien
 * Classe qui définit un joueur aléatoire
 * Hérite de la classe abstraite Joueur
 */
public class Dumb extends Joueur implements Parametres, Serializable {
    
    /**
     * Constructeur joueur aléatoire
     * @param partie Partie à laquelle le joueur participe
     * @param id le numéro du joueur dans la partie (1 ou 2)
     */
    public Dumb(Partie partie, int id) {
        super(partie, id);
    }
    
    /**
     * Méthode qui choisit une direction au hasard
     * @return un entier correspondant à une direction 
     */
    public int getDirection(){
        int[] directions = {HAUT, BAS, GAUCHE, DROITE};
        Random r = new Random();        
        return (directions[r.nextInt(directions.length)]);
    }
    
    
}