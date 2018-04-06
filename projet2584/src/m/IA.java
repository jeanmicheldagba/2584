    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m;

import java.io.Serializable;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vaurien
 */
public class IA extends Joueur implements Parametres, Parametres_IA, Serializable {

    private IA bot; // l'IA sur laquelle on va faire les simulations
    private int[] dirsEval;
    private HashSet<Thread> threads;

    public int[] getDirsEval() {
        return this.dirsEval;
    }

    public IA(Partie partie, int id) {
        super(partie, id);
    }

    public void setBot() {
        this.bot = new IA(this.partie, -1);
    }

    public IA getBot() {
        return this.bot;
    }

    /**
     *
     * @return the best direction
     */
    public int getDirection() {
        this.threads = new HashSet();
        this.dirsEval = new int[4];

        IA_Simulation sim;
        Thread sim_thread;

        for (int dir = 0; dir < KEYS[0].length; dir++) {
            sim = new IA_Simulation(dir, this);
            sim_thread = new Thread(sim);
            this.threads.add(sim_thread);
            sim_thread.start();
        }
        
        System.out.println("wait");
        for(Thread th : threads) {
            try {
                th.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

        }
        System.out.println("waited");
        
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
     *
     * @param node
     * @param depth
     * @return
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
                    if (Math.random() > PRUNE) {
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
     *
     * @param node the node to evaluate
     * @param goal_is_loose is the goal loosing or winning ?
     * @return
     */
    public int evaluate(Grille node) {
        int evaluation = 0;
        if (node.getCases().size() >= TAILLE * TAILLE) {
            evaluation += 999999999; //la partie est perdue.
        } else {
            evaluation += 50 * Math.exp(node.getCases().size());
        }

        if (TOWIN) {
            evaluation *= -1;
        }
        return evaluation;
    }

    /**
     * generate all children of the node
     *
     * @param node
     * @return children of the node
     */
    public HashSet<Grille> getChildren(Grille node, String[] keys) {
        HashSet<Grille> children = new HashSet();
        Grille child_reference;
        Grille child;

        for (int dir = 0; dir < keys.length; dir++) { //itère les 4 directions
            child_reference = (Grille) node.clone();
            botMove(Parametres.keyToDirection(keys[dir]), child_reference); //bouge les cases du child dans la direction

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

        return children;
    }

    /**
     * bouge les cases
     *
     * @param direction direction dans laquelle les cases doivent bouger
     * @param node
     */
    public void botMove(int direction, Grille node) {
        // On déplace les cases
        boolean casesMov = node.lanceurDeplacerCases(direction, true);
    }

}
