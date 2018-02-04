package jeu2584;

import java.util.Scanner;

public class Main implements Parametres {

    public static void main(String[] args) {

        Grille g = new Grille();
        boolean b = g.nouvelleCase();
        System.out.println(g);
        Scanner sc = new Scanner(System.in);
        
        while (!g.partieBloquee()) {
            System.out.println("Déplacer vers la Droite "+TOUCHE_D_1+", Gauche "+TOUCHE_G_1+", Haut "+TOUCHE_H_1+", ou Bas "+TOUCHE_B_1+" ?");
            String s = sc.nextLine();
            s.toLowerCase();
            if (!(s.equals(TOUCHE_D_1) || s.equals("droite")
                    || s.equals(TOUCHE_G_1) || s.equals("gauche")
                    || s.equals(TOUCHE_H_1) || s.equals("haut")
                    || s.equals(TOUCHE_B_1) || s.equals("bas"))) {
                System.out.println("Vous devez écrire d pour Droite, g pour Gauche, h pour Haut ou b pour Bas");
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
                boolean b2 = g.lanceurDeplacerCases(direction);
                if (b2) {
                    b = g.nouvelleCase();
                    if (!b) g.gameOver();
                }
                System.out.println(g);
                if (g.getValeurMax()>=OBJECTIF) g.victory();
            }
        }
        g.gameOver();
    }

}
