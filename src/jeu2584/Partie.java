/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jeu2584;


/**
 *
 * @author jmdag
 */
public class Partie implements Parametres {
    
    private Jeu jeu1;
    private Jeu jeu2;
    
    public Partie(){
        this.jeu1 = new Jeu();
        this.jeu2 = new Jeu();

    }
    
}
