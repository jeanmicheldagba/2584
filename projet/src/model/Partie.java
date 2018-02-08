package model;

public class Partie implements Parametres {
    
    private Jeu jeu1;
    private Jeu jeu2;
    
    public Partie(){
        this.jeu1 = new Jeu();
        this.jeu2 = new Jeu();

    }
    
}