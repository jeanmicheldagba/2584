package m;

public abstract class Joueur implements Parametres {
    
    protected Grille grille;
    protected int score;
    protected Partie partie;
    protected int id; //indique si c'est le joueur 1 ou 2
    
    public Joueur(Partie partie, int id){
        this.grille = new Grille(this);        
        this.score = 0;
        this.partie = partie;
        this.id = id;
    }
    
    public Grille getGrille(){
        return this.grille;
    }
    
    public int getScore(){
        return this.score;
    }
    
    public int getID(){
        return this.id;
    }
    
    // Méthode qui permet de calculer le du score du joueur
    protected void calculScore(){
        if(grille.getDeplacement()==true){// pas forcément utile A REVOIR
            this.score+= grille.getResDeplacement();
            grille.setResDeplacement(0);
        }
    }
    
    /**
     * bouge les cases
     * @param direction direction dans laquelle les cases doivent bouger
     * @return game over ?
     */
    public boolean move(int direction) {
        
        // On déplace les cases
        boolean casesMov = this.grille.lanceurDeplacerCases(direction);
        
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