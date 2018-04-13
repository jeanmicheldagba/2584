package m;

import java.io.Serializable;

public class Case implements Parametres, Serializable {

    private int x, y, valeur;
    private int guiX, guiY; //coordonnées sur l'interface (avant transition)
    private Grille grille;
    private double objectifTranslateX, objectifTranslateY;
    

    /**
     * Constructeur de la classe case
     * @param abs, l'abscisse de la case
     * @param ord, l'ordonnée de la case
     * @param v, la valeur de la case
     */
    public Case(int abs, int ord, int v) {
        this.x = abs;
        this.y = ord;
        this.guiX = this.x;
        this.guiY= this.y;
        this.valeur = v;
    }
    /**
     * Getter de la grille à laquelle appartient la case
     * @return la grille qui contient la case
     */
    public Grille getGrille() {
        return this.grille;
    }
    
    /**
     * Méthode qui permet de copier une case
     * @return un object qui est une copie de la case
     */
    @Override
    public Object clone(){
        Case c = new Case(this.x, this.y, this.valeur);
        return (Object) c;
    }

    /**
     * Setter de la grille à laquelle appartient la case
     * @param g , la nouvelle grille de la case
     */
    public void setGrille(Grille g) {
        this.grille = g;
    }

    /**
     * Getter de l'abscisse de la case
     * @return un entier 
     */
    public int getX() {
        return this.x;
    }

    /**
     * Getter de l'ordonnée de la case
     * @return un entier 
     */
    public int getY() {
        return this.y;
    }

    /**
     * Setter de l'abscisse
     * @param x la nouvelle abscisse de la case
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Setter de l'ordonnée de la case
     * @param y la nouvelle ordonnée de la case
     */
    public void setY(int y) {
        this.y = y;
    }
    
    /**
     * Setter de la l'abscisse de la case dans la GUI
     * @param guiX nouvelle abscisse de la case dans la GUI
     */
    public void setGuiX(int guiX){
        this.guiX = guiX;
    }
    
    /**
     * Setter de la l'ordonnée de la case dans la GUI
     * @param guiY nouvelle ordonnée de la case dans la GUI
     */
    public void setGuiY(int guiY){
        this.guiY = guiY;
    }
    
    /**
     * Getter de l'abscisse dans la GUI
     * @return l'abscisse de la case dans la GUI
     */
    public int getGuiX() {
        return this.guiX;
    }
    
    /**
     * Getter de l'abscisse dans la GUI
     * @return l'abscisse de la case dans la GUI
     */
    public int getGuiY() {
        return this.guiY;
    } 

    /**
     * Getter de l'objectif de la case en abscisse lors d'un déplacement
     * @return une abscisse de la GUI en pixel
     */
    public double getObjectifTranslateX() {
        return objectifTranslateX;
    }

    /**
     * Getter de l'objectif de la case en ordonnée lors d'un déplacement
     * @return une ordonnée de la GUI en pixel
     */
    public double getObjectifTranslateY() {
        return objectifTranslateY;
    }

    /**
     * Setter de l'objectif de la case en abscisse dans la GUI
     * @param objectifTranslateX la nouvelle abscisse à atteindre dans la GUI en pixel
     */
    public void setObjectifTranslateX(double objectifTranslateX) {
        this.objectifTranslateX = objectifTranslateX;
    }

    /**
     * Setter de l'objectif de la case en ordonnée dans la GUI
     * @param objectifTranslateY la nouvelle ordonnée à atteindre dans la GUI en pixel
     */
    public void setObjectifTranslateY(double objectifTranslateY) {
        this.objectifTranslateY = objectifTranslateY;
    }

    /**
     * Setter de la valeur de la case
     * @param valeur la nouvelle valeur de la case
     */
    public void setValeur(int valeur) {
        this.valeur = valeur;
    }

    /**
     * Getter de la valeur d'une case
     * @return la valeur actuelle de la case
     */
    public int getValeur() {
        return this.valeur;
    }

    /**
     * Méthode utilisée lors de l'ajout d'une case à un ensemble pour vérifier qu'il n'y a pas de doublons 
     * (teste parmi tous les candidats qui ont le même hashcode)
     * @param o objet case dont on veut comparer les coordonées
     * @return true si ce sont deux cases qui ont les mêmes coordonnées, false sinon
     */
    @Override
    public boolean equals(Object o) { 
        if (o instanceof Case) {
            Case c = (Case) o;
            return (this.x == c.x && this.y == c.y);
        } else {
            return false;
        }
    }

    /**
     * détermine le hashcode de la case
     * @return un entier qui est le hascode de la case
     */
    @Override
    public int hashCode() {
        return (this.x+1)*10 + (this.y+1);
    }

    /**
     * Permet de savoir si deux cases ont la même valeur
     * @param c la case à comparer
     * @return true si les deux cases ont la même valeur, faux sinon
     */
    public boolean valeurEgale(Case c) {
        if (c != null) {
            return this.valeur == c.valeur;
        } else {
            return false;
        }
    }

    /**
     * Retourne la case la plus proche dans la direction donnée
     * @param direction la direction dans laquelle on cherche un voisin
     * @return la case la plus proche s'il y en a une, null sinon
     */
    public Case getVoisinDirect(int direction) {
        if (direction == HAUT) {
            for (int i = this.y - 1; i >= 0; i--) {
                for (Case c : grille.getCases()) {
                    if (c.getX() == this.x && c.getY() == i) {
                        return c;
                    }
                }
            }
        } else if (direction == BAS) {
            for (int i = this.y + 1; i < TAILLE; i++) {
                for (Case c : grille.getCases()) {
                    if (c.getX() == this.x && c.getY() == i) {
                        return c;
                    }
                }
            }
        } else if (direction == GAUCHE) {
            for (int i = this.x - 1; i >= 0; i--) {
                for (Case c : grille.getCases()) {
                    if (c.getX() == i && c.getY() == this.y) {
                        return c;
                    }
                }
            }
        } else if (direction == DROITE) {
            for (int i = this.x + 1; i < TAILLE; i++) {
                for (Case c : grille.getCases()) {
                    if (c.getX() == i && c.getY() == this.y) {
                        return c;
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * Détermine si deux cases sont voisines dans la suite de Fibonacci
     * Non définie pour les valeurs 0 et 1.
     * @param c la case dont on veut tester la valeur
     * @return true si les cases sont voisines dans la suite de Fibonacci, false sinon
     */
    /*public boolean fibonacciVoisin(Case c){
        int[] fibo=new int[2];
        fibo[0] = this.valeur;
        fibo[1] = c.getValeur();
        
        //Si les deux cases valent 1, elles sont voisines car F(1)=F(2)=1
        if(fibo[0] == fibo[1]){
            if(fibo[0]==1) return true;
            else return false;
        }
        else{
            int n;
            double phi = 1.61803398874989484820;
            int[] index = new int[2];
            
            for(int i=0;i<2;i++){
                index[i] = Math.round((float) (Math.log(fibo[i] * Math.sqrt(5) + 0.5) / Math.log(phi)));
            }
            
            return Math.abs(index[0] - index[1]) == 1;
        }         
        
    }

    /**
     * Détermine si la valeur de c est voisine de la valeur de this dans la suite de Fibonacci
     * @param c
     * @return 
     */
    public boolean fibonacciVoisin(Case c){
        int val_1=this.valeur;
        int val_2=c.getValeur();
        
        
        //Si les deux cases valent 1, elles sont voisines car F(1)=F(2)=1
        if(val_1==val_2){
            if(val_1==1) return true;
            else return false;
        }
        
        boolean trouve=false;
        //on commence avec a : F(n-2)=F(1)=1 et b : F(n-1)=F(2)=1
        int a=1;
        int b=0;
        int fib=a+b;
        
        while(!trouve){ 
            //Si fib est supérieur à une des valeurs, c'est qu'elle n'appartient à la suite
            if(fib>val_1 || fib>val_2) return false;
            
            //Si on trouve une des valeurs, on sort à la fin de la boucle
            else if(val_1==fib || val_2==fib){
                trouve=true;
            } 
            
            //on itère fibonacci
            b=a;
            a=fib;  
            fib=a+b;            
        }
        
        //on a trouvé une des valeurs dans Fibonacci, on rend vrai ssi l'autre valeur est F(n+1)
        return val_1==fib || val_2==fib;
        
        
    }

    /**
     * Permet d'afficher textuellement la case
     * @return un string avec les coordonées et la valeur de la case
     */
    @Override
    public String toString() {
        return "Case(" + this.x + "," + this.y + "," + this.valeur + ")";
    }

}