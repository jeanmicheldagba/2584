    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m;

import java.io.Serializable;
import java.util.HashSet;

/**
 * Classe définissant un joueur IA
 * @author vaurien
 */
public class IA extends Joueur implements Parametres, Serializable {

    private IA bot; // l'IA sur laquelle on va faire les simulations
    private int[] dirsEval; //

    /**
     * Getter de dirsEval
     * @return un tableau d'entiers représentant des directions
     */
    public int[] getDirsEval() {
        return this.dirsEval;
    }

    /**
     * Constructeur de l'IA
     * @param partie la partie dans laquelle l'IA joue
     * @param id son numéro de joueur dans la partie
     */
    public IA(Partie partie, int id) {
        super(partie, id);
    }

    /**
     * Setter de bot qui permet de faire des simulations
     */
    public void setBot() {
        this.bot = new IA(this.partie, -1);
    }

    /**
     * Getter du bot de l'IA
     * @return le bot de l'IA
     */
    public IA getBot() {
        return this.bot;
    }

    /**
     * Méthode qui choisit une direction optimale 
     * @return la meilleure direction (c'est-à-dire celle qui augmente le plus les chances de perdre)
     */
    public int getDirection() {
        this.dirsEval = new int[4];

        IA_Simulation sim0 = new IA_Simulation(0, this);
        IA_Simulation sim1 = new IA_Simulation(1, this);
        IA_Simulation sim2 = new IA_Simulation(2, this);
        IA_Simulation sim3 = new IA_Simulation(3, this);
        Thread sim_thread0 = new Thread(sim0);
        Thread sim_thread1 = new Thread(sim1);
        Thread sim_thread2 = new Thread(sim2);
        Thread sim_thread3 = new Thread(sim3);
        sim_thread0.start();
        sim_thread1.start();
        sim_thread2.start();
        sim_thread3.start();
        
        try {
            sim_thread0.join();
        } catch (InterruptedException ex) {
            System.out.println("exception");
        }
        
        try {
            sim_thread1.join();
        } catch (InterruptedException ex) {
            System.out.println("exception");
        }
        
        try {
            sim_thread2.join();
        } catch (InterruptedException ex) {
            System.out.println("exception");
        }
        
        try {
            sim_thread3.join();
        } catch (InterruptedException ex) {
            System.out.println("exception");
        }
        
        System.out.println("joined");
        
        int largest = 0;
        for (int i = 1; i < dirsEval.length; i++) {
            if (dirsEval[i] > dirsEval[largest]) {
                largest = i;
            }
        }
        
        int[] directions = {HAUT, GAUCHE, BAS, DROITE};
        return directions[largest];

    }

    /**
     * Développement récursif des chemins possibles pour obtenir la meilleure action
     * @param node un noeud de l'arbre des possibilités
     * @param depth la profondeur de l'arbre
     * @return la meilleure direction
     */
    public int getDirection_recursif(Grille node, int depth) {
        if (depth == 0 || node.getCases().size() >= TAILLE * TAILLE) {
            return evaluate(node);
        } else {
            int evaluation = 0;
            int evaluated_children = 0;
            float probability;
            HashSet<Grille> children = getChildren(node, KEYS[0]);
            if (children.isEmpty()) {
                return evaluate(node);
            } else {
                evaluated_children = 0;
                for (Grille child : children) {

                    evaluated_children++;
                    if (child.getSpawn() == 1) {
                        probability = (float) 0.75;
                    } else if (child.getSpawn() == 2) {
                        probability = (float) 0.25;
                    } else {
                        System.out.println("ERREUR spawn");
                        probability = -1;
                    }
                    evaluation += probability * getDirection_recursif(child, depth - 1);


                }
                if (evaluated_children != 0) {
                    evaluation /= evaluated_children;
                }
                return evaluation;
            }
        }
    }

    /**
     * evaluate the given node according to the goal
     * @param node the node to evaluate
     * @return une valeur indiquant si c'est une bonne situation
     */
    public int evaluate(Grille node) {
        int evaluation = 0;
        if (node.getCases().size() >= TAILLE * TAILLE) {
            evaluation += 999999999; //la partie est perdue.
        } else {
            evaluation += 5* node.getCases().size();
        }
        return evaluation;
    }

    /**
     * generate all children of the node
     * @param node la grille dont on veut voir tous les enfants possibles
     * @return children of the node
     */
    public HashSet<Grille> getChildren(Grille node, String[] keys) {
        if(this.id == 9) System.out.println("TEST");
        HashSet<Grille> children = new HashSet();
        Grille child_reference;
        Grille child;

        for (int dir = 0; dir < keys.length; dir++) { //itère les 4 directions
            child_reference = (Grille) node.clone();
            if(botMove(Parametres.keyToDirection(keys[dir]), child_reference)) { //bouge les cases du child dans la direction
                if(this.id == 9) System.out.println("MOVEEEE");

                // on crée toutes les cases encore libres
                for (int x = 0; x < TAILLE; x++) {
                    for (int y = 0; y < TAILLE; y++) {
                        child = (Grille) child_reference.clone();
                        Case c = new Case(x, y, 1);
                        if (!child.getCases().contains(c)) { // contains utilise la méthode equals dans Case
                            //ajoute le 1
                            child.nouvelleCase(c);
                            children.add(child);

                            //ajoute le 2
                            c = (Case) c.clone();
                            c.setValeur(2);
                            child = (Grille) child_reference.clone();
                            child.nouvelleCase(c);
                            children.add(child);
                        }
                    }
                }
            }

        }

        return children;
    }

    /**
     * bouge les cases dans la grille du joueur IA
     * @param direction direction dans laquelle les cases doivent bouger
     * @param node la grille dans laquelle on déplace les cases
     */
    public boolean botMove(int direction, Grille node) {
        // On déplace les cases
        return node.lanceurDeplacerCases(direction, true);
    }

}
