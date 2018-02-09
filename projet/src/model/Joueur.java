package model;

import java.util.Scanner;

public class Joueur implements Parametres {
    
    private Grille g;
    private int score;
    private int nbUndo;
    
    public Joueur(Partie p){
        this.g = new Grille();
        this.g.nouvelleCase();
        this.g.nouvelleCase();
        this.score = 0;
        this.nbUndo = 5;
    }
    
    public void jouer(){
        System.out.println(this.g);
        Scanner sc = new Scanner(System.in);
        
        while (!this.g.partieBloquee()) {
            System.out.println("Déplacer vers la Droite "+TOUCHE_D_1+", Gauche "+TOUCHE_G_1+", Haut "+TOUCHE_H_1+", ou Bas "+TOUCHE_B_1+" ?");
            String s = sc.nextLine();
            s.toLowerCase();
            if (!(s.equals(TOUCHE_D_1) || s.equals("droite")
                    || s.equals(TOUCHE_G_1) || s.equals("gauche")
                    || s.equals(TOUCHE_H_1) || s.equals("haut")
                    || s.equals(TOUCHE_B_1) || s.equals("bas"))) {
                System.out.println("Vous devez écrire d pour Droite, q pour Gauche, z pour Haut ou s pour Bas");
            } else {
                int direction;
                if (s.equals(TOUCHE_D_1) || s.equals("droite")) {
                    direction = DROITE;
                } else if (s.equals(TOUCHE_G_1) || s.equals("gauche")) {
                    direction = GAUCHE;
                } else if (s.equals(TOUCHE_H_1) || s.equals("haut")) {
                    direction = HAUT;
                } else {
                    direction = BAS;
                }
                boolean b2 = this.g.lanceurDeplacerCases(direction);
                if (b2) {
                    if (!this.g.nouvelleCase()) this.g.gameOver();
                }
                System.out.println(this.g);
                if (this.g.getValeurMax()>=OBJECTIF) this.g.victory();
            }
        }
        this.g.gameOver();
    }
    
}