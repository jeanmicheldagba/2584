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
    private boolean dejaUndo;
    protected Grille lastGrille;

    public Human(String pseudo) {
        super();
        this.pseudo = pseudo;
        this.lastGrille = (Grille) this.grille.clone();
        this.nbUndo = 5;
        this.dejaUndo = true;
    }

    public void jouer() {
        System.out.println("Grille :");
        System.out.println(this.grille);
        //System.out.println("Sauvegarde :");
        //System.out.println(this.sauvegardeG);
        /*System.out.println("undo :");
        System.out.println(this.lastG);*/
        System.out.println("Votre score : "+this.score);
        Scanner sc = new Scanner(System.in);

        // On affiche les touches à utiliser et on récupere l'entrée clavier du joueur
        System.out.println("Déplacer vers la Droite " + TOUCHE_D_1 + ", Gauche " + TOUCHE_G_1 + ", Haut " + TOUCHE_H_1 + ", Bas " + TOUCHE_B_1 + " ou Undo " + TOUCHE_UNDO + " ?");
        String s = sc.nextLine();
        s.toLowerCase();
        
        // si l'entrée clavier n'est pas valable on redemande 
        if (!(s.equals(TOUCHE_D_1) || s.equals(TOUCHE_G_1) || s.equals(TOUCHE_H_1) || s.equals(TOUCHE_B_1) || s.equals(TOUCHE_UNDO))) {
            System.out.println("Vous devez écrire d pour Droite, q pour Gauche, z pour Haut ou s pour Bas ou u pour Undo");

        } else if (s.equals(TOUCHE_UNDO)) { // Si le joueur veut faire un undo...
            if (this.nbUndo > 0 && !this.dejaUndo) { // Si il lui reste des undo et qu'il n'a pas déjà fait un undo au tour précédent
                // On décrémente le nombre de undo disponible
                this.nbUndo--;
                // La grille du joueur redevient la version précédente
                this.grille = this.lastGrille;
                // this.sauvegardeG = (Grille) this.lastG.clone();
                // On signale qu'undo a été utilisé
                this.dejaUndo = true;
                System.out.println("Grille après undo :");
                System.out.println(this.grille);
                System.out.println("Last grille après undo :");
                System.out.println(this.lastGrille);
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
            // On sauvegarde la grille actuelle
            this.lastGrille = (Grille) this.grille.clone();
            // On déplace les cases
            boolean casesMov = this.grille.lanceurDeplacerCases(direction);
            if (casesMov) {
                if (!this.grille.nouvelleCase()) {
                    this.gameOver();
                }
            }
            if (this.grille.getValeurMax() >= OBJECTIF) {
                this.grille.victory();
            }
            this.calculScore(); //on met à jour le score
            // On dit que la derniere action n'était pas déjà un undo
            this.dejaUndo = false;
            // la grille du undo devient la grille sauvegardée
            //this.lastG = (Grille) this.sauvegardeG.clone();
            //this.sauvegardeG = (Grille) this.g.clone();
        }

    }
    
    /**
     * 
     */
    public void undo() {
        if (this.nbUndo > 0 && !this.dejaUndo) { // Si il lui reste des undo et qu'il n'a pas déjà fait un undo au tour précédent            
            this.nbUndo--; // On décrémente le nombre de undo disponible            
            this.grille = this.lastGrille; // La grille du joueur redevient la version précédente            
            this.dejaUndo = true; // On signale qu'undo a été utilisé
            System.out.println("Grille après undo :\n"+this.grille);
            System.out.println("Last grille après undo :\n"+this.lastGrille);
        } else {
            System.out.println("Undo indisponible");
        }
    }

}