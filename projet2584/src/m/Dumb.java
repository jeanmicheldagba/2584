/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m;

import java.util.Random;

/**
 *
 * @author vaurien
 */
public class Dumb extends Joueur implements Parametres {
    
    public Dumb(Partie partie, int id) {
        super(partie, id);
    }
    
    /**
     * 
     * @return a random direction for dumb player 
     */
    public int getDirection(){
        int[] directions = {HAUT, BAS, GAUCHE, DROITE};
        Random r = new Random();
        return (directions[r.nextInt(directions.length)]);
    }
    
    
}