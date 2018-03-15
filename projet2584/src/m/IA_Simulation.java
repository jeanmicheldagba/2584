/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m;

import java.util.HashSet;

/**
 *
 * @author jmdag
 */
public class IA_Simulation implements Runnable, Parametres, Parametres_IA {

    private int direction;
    private IA ia;
    
    public IA_Simulation(int dir, IA ia) {
        this.direction = dir;
        this.ia = ia;
    }
    
    @Override
    public void run() {
        String[] dirsInd = new String[1];
        int depth = DEPTH;
        System.out.println("dir : "+this.direction);
        dirsInd[0] = KEYS[0][this.direction];
        float probability; //the probability of the configuration
        
        //set root
        Grille root = (Grille) this.ia.getGrille().clone();
        root.setJoueur(this.ia.getBot());
        this.ia.getBot().setGrille(root);
        
        int evaluated_children;
        
        HashSet<Grille> children = this.ia.getChildren(root, dirsInd);
        this.ia.getDirsEval()[direction] = 0; // init the score of this direction
        if (!children.isEmpty()) {
            evaluated_children = 0;
            for (Grille child : children) {
                if(Math.random()>PRUNE){
                    evaluated_children++;
                    if (child.getSpawn() == 1) {
                        probability = (float) 0.75;
                    } else if (child.getSpawn() == 2) {
                        probability = (float) 0.25;
                    } else {
                        System.out.println("ERREUR spawn");
                        probability = -1;
                    }
                    this.ia.getDirsEval()[direction] += probability * this.ia.getDirection_recursif(child, depth - 1);
                }

            }
            if(evaluated_children != 0){
                this.ia.getDirsEval()[direction] /= evaluated_children;
            }

        }
    }
    
}
