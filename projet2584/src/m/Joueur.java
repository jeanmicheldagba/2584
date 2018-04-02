package m;

public abstract class Joueur implements Parametres {
    
    protected Grille grille;
    protected int score;
    protected Partie partie;//UTILE????
    protected int id; //indique si c'est le joueur 1 ou 2
    protected int nbDeplacements; //nombre de déplacements effectué par un joueur
    
    /**
     * Constructeur de Joueur
     * @param partie La partie à laquelle le joueur participe
     * @param id l'indice du joueur dans la partie (1 ou 2) 
     */
    public Joueur(Partie partie, int id){
        this.grille = new Grille(this);        
        this.score = 0;
        this.nbDeplacements=0;
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
     * getter du nombre de déplacements du joueur
     * @return le nombre de déplacemnts du joueur
     */
    public int getNbDeplacements(){
        return this.nbDeplacements;
    }
    
    /**
     * Calcule le score du joueur
     */
    protected void calculScore(){
        //if(grille.getDeplacement()==true){//on calcule le score uniquement s'il y a eu un déplacement
            this.score+= grille.getResDeplacement();
            grille.setResDeplacement(0);
        //}
    }
    
    /**
     * Méthode qui déplace les cases si c'est possible et qui permet de savoir si la partie est finie
     * @param direction direction dans laquelle les cases doivent bouger
     * @return true si la partie est terminée, false si la partie continue
     */
    public boolean move(int direction) {
        
        //On déplace les cases
        boolean casesMov = this.grille.lanceurDeplacerCases(direction, false);
        
        //Si un déplacement a été effectué, on incrémente de +1 le nombre de déplacements
        if (casesMov) {
            this.nbDeplacements++;
        }    
            
        //On vérifie si le joueur a gagné
        if(this.grille.getValeurMax() >= OBJECTIF){ // le joueur a atteint l'objectif
            System.out.println("You Win! : Vous avez atteint " + this.grille.getValeurMax()+"\n Score : "+this.score);
            System.out.println("Le joueur "+this.id+" a gagné !");
            this.partie.majBDD();//fin de la partie : on entre les informations dans la base de données
            this.partie.setGameover(true);
            return true; //la partie est finie
        }
        
        //On test si la grille est bloquée(=aucun déplacement possible)
        if(this.getGrille().bloquee()){
            System.out.println("Game Over : Aucun déplacement possible \n Score : "+this.score);
            System.out.println("Le joueur "+this.id+" a perdu !");
            this.partie.majBDD();//fin de la partie : on entre les informations dans la base de données
            this.partie.setGameover(true);
            return true; //la partie est finie
            }
        /*if (!this.grille.nouvelleCase()) { //la grille est pleine
            System.out.println("Partie terminée, score : "+this.score);
            return true; //game over
        }*/ //PB : grille peut être pleine tout en ayant déplacement possible
        
        this.calculScore(); //on met à jour le score
        return false; //on continue à jouer
    }
    
}