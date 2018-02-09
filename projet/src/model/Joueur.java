package model;

import java.util.Scanner;

public abstract class Joueur implements Parametres {
    
    private Grille g;
    private Grille lastG, sauvegardeG;
    private int score;
    private int nbUndo;
    private boolean dejaUndo;
    
    public Joueur(){
        this.g = new Grille();
        this.g.nouvelleCase();
        this.g.nouvelleCase();
        this.score = 0;
        this.nbUndo = 5;
        this.lastG = (Grille) this.g.clone();
        this.sauvegardeG = (Grille) this.g.clone();
        this.dejaUndo = true;
    }
     //methode jouer
    public void jouer(){
        System.out.println(this.g);
        Scanner sc = new Scanner(System.in);
        
        while (!this.g.partieBloquee()) {
            // On sauvegarde la grille actuelle
            this.sauvegardeG = (Grille) this.g.clone();
            System.out.println("Déplacer vers la Droite "+TOUCHE_D_1+", Gauche "+TOUCHE_G_1+", Haut "+TOUCHE_H_1+", Bas "+TOUCHE_B_1+" ou Undo"+ TOUCHE_UNDO+" ?");
            String s = sc.nextLine();
            s.toLowerCase();
            if (!(s.equals(TOUCHE_D_1) || s.equals(TOUCHE_G_1) || s.equals(TOUCHE_H_1) || s.equals(TOUCHE_B_1) || s.equals(TOUCHE_UNDO))) {
                System.out.println("Vous devez écrire d pour Droite, q pour Gauche, z pour Haut ou s pour Bas ou u pour Undo");
                
            } else if(s.equals(TOUCHE_UNDO)) { // Si le joueur veut faire un undo...
                if (this.nbUndo > 0 && !this.dejaUndo){ // Si il lui reste des undo et qu'il n'a pas déjà fait un undo au tour précédent
                    undoActive(); // On fait un undo
                } else {
                    System.out.println("Undo indisponible, vous devez choisir choisir une direction");
                }
            } else {
                int direction;
                if (s.equals(TOUCHE_D_1)) {
                    direction = DROITE;
                } else if (s.equals(TOUCHE_G_1)) {
                    direction = GAUCHE;
                } else if (s.equals(TOUCHE_H_1)) {
                    direction = HAUT;
                } else {
                    direction = BAS;
                }
                boolean b2 = this.g.lanceurDeplacerCases(direction);
                if (b2) {
                    if (!this.g.nouvelleCase()) this.g.gameOver();
                }
                if (this.g.getValeurMax()>=OBJECTIF) this.g.victory();
                this.dejaUndo = false;
                this.lastG = (Grille) this.sauvegardeG.clone();
            }
            System.out.println(this.g);
        }
        this.g.gameOver();
    }
    
    // methode qui gère la grille quand un joueur utilise undo
    private void undoActive(){
        // On décrémente le nombre de undo disponible
        this.nbUndo--;
        // La grille du joueur redevient la version précédente
        this.g = (Grille) this.lastG.clone();
        // On signale qu'undo a été utilisé
        this.dejaUndo = true;
    }
    
}