package m;

public abstract class Joueur implements Parametres {
    
    protected Grille grille;
    protected int score;
    protected Partie partie;
    protected int id; //indique si c'est le joueur 1 ou 2
    
    /**
     * Constructeur de Joueur
     * @param partie La partie à laquelle le joueur participe
     * @param id l'indice du joueur dans la partie (1 ou 2) 
     */
    public Joueur(Partie partie, int id){
        this.grille = new Grille(this);        
        this.score = 0;
        this.partie = partie;
        this.id = id;
    }
    
    /**
     * getter de la grille
     * @return la grille du Joueur
     */
    public Grille getGrille(){
        return this.grille;
    }
    /**
     * setter de la grille
     * @param grille nouvelle grille du joueur
     */
    public void setGrille(Grille grille){
        this.grille = grille;
    }
    /**
     * getter du score du joueur
     * @return le score du joueur
     */
    public int getScore(){
        return this.score;
    }
    /**
     * getteur de l'indice du joueur dans la partie
     * @return l'indice du joueur (1 ou 2)
     */
    public int getID(){
        return this.id;
    }
    
    /**
     * Calcule le score du joueur
     */
    protected void calculScore(){
        if(grille.getDeplacement()==true){// pas forcément utile A REVOIR
            this.score+= grille.getResDeplacement();
            grille.setResDeplacement(0);
        }
    }
    
    /**
     * Méthode qui permet de savoir si la partie est finie et qui déplace les cases si c'est possible
     * @param direction direction dans laquelle les cases doivent bouger
     * @return true si la partie est terminée, false si la partie continue
     */
    public boolean move(int direction) {
        
        // On déplace les cases
        boolean casesMov = this.grille.lanceurDeplacerCases(direction, false);
        
        //On vérifie l'état de la partie
        if (casesMov) {
            if (!this.grille.nouvelleCase()) { //la grille est pleine
                System.out.println("partie terminée, score : "+this.score);
                return true; //game over
            }
        }
        if (this.grille.getValeurMax() >= OBJECTIF) { // le joueur a atteint l'objectif
            System.out.println("Bravo ! Vous avez atteint " + this.grille.getValeurMax());
            return true; //game over
        }
        this.calculScore(); //on met à jour le score

        return false; //game not over
    }
    
}