package jeu2584;

public class Case implements Parametres {

    private int x, y, valeur;
    private Grille grille;

    public Case(int abs, int ord, int v) {
        this.x = abs;
        this.y = ord;
        this.valeur = v;
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

    public void setValeur(int valeur) {
        this.valeur = valeur;
    }

    public int getValeur() {
        return this.valeur;
    }

    @Override
    public boolean equals(Object obj) { // la méthode equals est utilisée lors de l'ajout d'une case à un ensemble pour vérifier qu'il n'y a pas de doublons (teste parmi tous les candidats qui ont le même hashcode)
        if (obj instanceof Case) {
            Case c = (Case) obj;
            return (this.x == c.x && this.y == c.y);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() { // détermine le hashcode
        return this.x * 7 + this.y * 13;
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
                for (Case c : grille.getGrille()) {
                    if (c.getX() == this.x && c.getY() == i) {
                        return c;
                    }
                }
            }
        } else if (direction == BAS) {
            for (int i = this.y + 1; i < TAILLE; i++) {
                for (Case c : grille.getGrille()) {
                    if (c.getX() == this.x && c.getY() == i) {
                        return c;
                    }
                }
            }
        } else if (direction == GAUCHE) {
            for (int i = this.x - 1; i >= 0; i--) {
                for (Case c : grille.getGrille()) {
                    if (c.getX() == i && c.getY() == this.y) {
                        return c;
                    }
                }
            }
        } else if (direction == DROITE) {
            for (int i = this.x + 1; i < TAILLE; i++) {
                for (Case c : grille.getGrille()) {
                    if (c.getX() == i && c.getY() == this.y) {
                        return c;
                    }
                }
            }
        }
        return null;
    }

    public boolean fibonacciVoisin(Case c){
        x=this.valeur;
        y=c.getValeur();
        
        //Si les deux cases valent 1, elles sont voisines car F(1)=F(2)=1
        if(x==y){
            if(x==1) return true;
            else return false;
        }
        
        boolean trouve=false;
        //on commence avec a : F(n-2)=F(1)=1 et b : F(n-1)=F(2)=1
        int a=1;
        int b=1;
        int fib=a+b;
        
        while(!trouve){ 
            //Si fib est supérieur à une des valeurs, c'est qu'elle n'appartient à la suite
            if(fib>x || fib>y) return false;
            
            //Si on trouve une des valeurs, on sort à la fin de la boucle
            else if(x==fib || y==fib){
                trouve=true;
            } 
            
            //on itère fibonacci
            b=a;
            a=fib;  
            fib=a+b;            
        }
        
        //on a trouvé une des valeurs dans Fibonacci, on rend vrai ssi l'autre valeur est F(n+1)
        return x==fib || y==fib;
        
        
    }

    @Override
    public String toString() {
        return "Case(" + this.x + "," + this.y + "," + this.valeur + ")";
    }

}
