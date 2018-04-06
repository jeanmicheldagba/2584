package m;

import java.io.Serializable;

public class Case implements Parametres, Serializable {

    private int x, y, valeur;
    private int guiX, guiY; //coordonnées sur l'interface (avant transition)
    private Grille grille;
    private double objectifTranslateX, objectifTranslateY;
    

    public Case(int abs, int ord, int v) {
        this.x = abs;
        this.y = ord;
        this.guiX = this.x;
        this.guiY= this.y;
        this.valeur = v;
    }
    
    public Grille getGrille() {
        return this.grille;
    }
    
    @Override
    public Object clone(){
        Case c = new Case(this.x, this.y, this.valeur);
        return (Object) c;
    }

    public void setGrille(Grille g) {
        this.grille = g;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    public void setGuiX(int guiX){
        this.guiX = guiX;
    }
    
    public void setGuiY(int guiY){
        this.guiY = guiY;
    }
    
    public int getGuiX() {
        return this.guiX;
    }

    public int getGuiY() {
        return this.guiY;
    } 

    public double getObjectifTranslateX() {
        return objectifTranslateX;
    }

    public double getObjectifTranslateY() {
        return objectifTranslateY;
    }

    public void setObjectifTranslateX(double objectifTranslateX) {
        this.objectifTranslateX = objectifTranslateX;
    }

    public void setObjectifTranslateY(double objectifTranslateY) {
        this.objectifTranslateY = objectifTranslateY;
    }

    public void setValeur(int valeur) {
        this.valeur = valeur;
    }

    public int getValeur() {
        return this.valeur;
    }

    @Override
    public boolean equals(Object o) { // la méthode equals est utilisée lors de l'ajout d'une case à un ensemble pour vérifier qu'il n'y a pas de doublons (teste parmi tous les candidats qui ont le même hashcode)
        if (o instanceof Case) {
            Case c = (Case) o;
            return (this.x == c.x && this.y == c.y);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() { // détermine le hashcode
        return (this.x+1)*10 + (this.y+1);
    }

    public boolean valeurEgale(Case c) {
        if (c != null) {
            return this.valeur == c.valeur;
        } else {
            return false;
        }
    }

    public Case getVoisinDirect(int direction) { // retourne la case la plus proche dans la direction donnée
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
    
    //détermine si la valeur de c est voisine de la valeur de this dans la suite de Fibonacci
    //NB : les cas 0 et 1 ne sont pas supportés
    public boolean fibonacciVoisin(Case c){
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

    //détermine si la valeur de c est voisine de la valeur de this dans la suite de Fibonacci
    //NB : les cas 0 et 1 ne sont pas supportés
    public boolean fibonacciVoisinBis(Case c){
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

    @Override
    public String toString() {
        return "Case(" + this.x + "," + this.y + "," + this.valeur + ")";
    }

}