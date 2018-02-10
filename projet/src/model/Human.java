/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Scanner;

/**
 *
 * @author vaurien
 */
public class Human extends Joueur implements Parametres {

    String pseudo;

    public Human(String pseudo) {
        super();
        this.pseudo = pseudo;
    }

    public void jouer() {
        System.out.println("Grille :");
        System.out.println(this.g);
        //System.out.println("Sauvegarde :");
        //System.out.println(this.sauvegardeG);
        System.out.println("undo :");
        System.out.println(this.lastG);
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
                this.g = this.lastG;
                // this.sauvegardeG = (Grille) this.lastG.clone();
                // On signale qu'undo a été utilisé
                this.dejaUndo = true;
                System.out.println("Grille après undo :");
                System.out.println(this.g);
                System.out.println("Last grille après undo :");
                System.out.println(this.lastG);
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
            this.lastG = (Grille) this.g.clone();
            // On déplace les cases
            boolean casesMov = this.g.lanceurDeplacerCases(direction);
            if (casesMov) {
                if (!this.g.nouvelleCase()) {
                    this.gameOver();
                }
            }
            if (this.g.getValeurMax() >= OBJECTIF) {
                this.g.victory();
            }
            // On dit que la derniere action n'était pas déjà un undo
            this.dejaUndo = false;
            // la grille du undo devient la grille sauvegardée
            //this.lastG = (Grille) this.sauvegardeG.clone();
            //this.sauvegardeG = (Grille) this.g.clone();
        }

    }

    // methode qui gère la grille quand un joueur utilise undo
    private void undoActive() {
        // On décrémente le nombre de undo disponible
        this.nbUndo--;
        // La grille du joueur redevient la version précédente
        this.g = (Grille) this.lastG.clone();
        // this.sauvegardeG = (Grille) this.lastG.clone();
        // On signale qu'undo a été utilisé
        this.dejaUndo = true;
    }

}
