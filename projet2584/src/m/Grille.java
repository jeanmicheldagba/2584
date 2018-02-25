package m;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

public class Grille implements Parametres {

    private HashSet<Case> cases;
    private int valeurMax = 0;
    private boolean deplacement;
    private int resDeplacement; //stocke les points générés par la fusion des cases
    private Joueur joueur;

    public Grille(Joueur joueur) {
        this.cases = new HashSet<>();
        this.deplacement=false;
        this.resDeplacement=0;
        this.joueur = joueur;
    }
    
    // Methode qui copie une grille par valeur et la renvoie sous forme d'objet
    public Object clone(){
        Grille g = new Grille(this.joueur);
        Case cAdd;
        // On parcourt la grille pour copier toutes les cases dans la nouvelle grille
        for (Case c : this.cases){
            cAdd = (Case) c.clone(); //on clone la case
            cAdd.setGrille(g); //on spécifie la grille de la case
            g.cases.add(cAdd); //on ajoute la case à g
        }
        g.valeurMax = this.valeurMax;
        g.deplacement = this.deplacement;
        return (Object) g;
    }
    
    public void setValeurMax(int v){
        this.valeurMax = v;
    }
    
    public void setDeplacement(boolean b){
        this.deplacement = b;
    }
    
    public boolean getDeplacement(){
        return this.deplacement;
    }
    
    public Joueur getJoueur(){
        return this.joueur;
    }
    
    public void setResDeplacement(int rd){
        this.resDeplacement=rd;
    }
    public int getResDeplacement(){
        return this.resDeplacement;
    }
    
    public void setGrille(HashSet<Case> h){
        this.cases = h;
    }

    @Override
    public String toString() {
        int[][] tableau = new int[TAILLE][TAILLE];
        for (Case c : this.cases) {
            tableau[c.getY()][c.getX()] = c.getValeur();
        }
        String result = "";
        for (int i = 0; i < tableau.length; i++) {
            result += Arrays.toString(tableau[i]) + "\n";
        }
        return result;
    }

    public HashSet<Case> getCases() {
        return this.cases;
    }

    public int getValeurMax() {
        return valeurMax;
    }

    //détermine si un mouvement est possible
    public boolean bloquee() {
        if (this.cases.size() < TAILLE * TAILLE) {
            return false;
        } else {
            for (Case c : this.cases) {
                for (int i = 1; i <= 2; i++) {
                    if (c.getVoisinDirect(i) != null) {
                        //on teste si la case voisine est voisine dans la suite de Fibonacci
                        if (c.fibonacciVoisin(c.getVoisinDirect(i))) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean lanceurDeplacerCases(int direction) {
        Case[] extremites = this.getCasesExtremites(direction);
        this.deplacement = false; // pour vérifier si on a bougé au moins une case après le déplacement, avant d'en rajouter une nouvelle
        for (int i = 0; i < TAILLE; i++) {
            this.deplacerCasesRecursif(extremites, i, direction, 0);
        }
        return this.deplacement;
    }

    //Incrémente c de la valeur add
    private void fusion(Case c, int add) {
        int sommeCases=c.getValeur()+add;
        this.joueur.grille.joueur.partie.controller.fusionGUI(c, sommeCases);
        c.setValeur(sommeCases);
        this.resDeplacement=this.resDeplacement+sommeCases;
        
        //actualise valeur max
        if (this.valeurMax < c.getValeur()) {
            this.valeurMax = c.getValeur();
        }
        this.deplacement = true;
    }

    private void deplacerCasesRecursif(Case[] extremites, int rangee, int direction, int compteur) {
        int objectif;
        Case c = extremites[rangee];
        
        if (c != null) {
            if ((direction == HAUT && c.getY() != compteur)
                    || (direction == BAS && c.getY() != TAILLE - 1 - compteur)
                    || (direction == GAUCHE && c.getX() != compteur)
                    || (direction == DROITE && c.getX() != TAILLE - 1 - compteur)) {
                switch (direction) {
                    case HAUT:
                        objectif = compteur; //la coordonnée à atteindre
                        this.joueur.partie.controller.getToMove()[this.joueur.getID()].add(c);
                        c.setY(objectif); //change coordonnées
                        break;
                    case BAS:
                        objectif = TAILLE - 1 - compteur;
                        this.joueur.partie.controller.getToMove()[this.joueur.getID()].add(c);
                        c.setGuiX(c.getX());
                        c.setGuiY(c.getY());
                        c.setY(objectif); //change coordonnées
                        break;
                    case GAUCHE:
                        objectif = compteur;
                        this.joueur.partie.controller.getToMove()[this.joueur.getID()].add(c);
                        c.setGuiX(c.getX());
                        c.setGuiY(c.getY());
                        c.setX(objectif); //change coordonnées
                        break;
                    default:
                        objectif = TAILLE - 1 - compteur;
                        this.joueur.partie.controller.getToMove()[this.joueur.getID()].add(c);
                        c.setGuiX(c.getX());
                        c.setGuiY(c.getY());
                        c.setX(objectif); //change coordonnées
                        break;
                }
                this.deplacement = true;
            }
            Case voisin = c.getVoisinDirect(-direction);
            if (voisin != null) {
                if (c.fibonacciVoisin(voisin)) {
                    this.fusion(c, voisin.getValeur()); //fusionne les voisines dans Fibonacci (somme des 2 cases)
                    extremites[rangee] = voisin.getVoisinDirect(-direction);
                    this.cases.remove(voisin);
                    System.out.println(this.joueur.partie.controller.enleverCaseGUI(voisin)+"ment enlevé");
                    this.joueur.partie.controller.getToMove()[this.joueur.getID()].remove(voisin);
                    this.deplacerCasesRecursif(extremites, rangee, direction, compteur + 1);
                } else {
                    extremites[rangee] = voisin;
                    this.deplacerCasesRecursif(extremites, rangee, direction, compteur + 1);
                }
            }
        }
    }

    /*
    * Si direction = HAUT : retourne les 4 cases qui sont le plus en haut (une pour chaque colonne)
    * Si direction = DROITE : retourne les 4 cases qui sont le plus à droite (une pour chaque ligne)
    * Si direction = BAS : retourne les 4 cases qui sont le plus en bas (une pour chaque colonne)
    * Si direction = GAUCHE : retourne les 4 cases qui sont le plus à gauche (une pour chaque ligne)
    * Attention : le tableau retourné peut contenir des null si les lignes/colonnes sont vides
     */
    public Case[] getCasesExtremites(int direction) {
        Case[] result = new Case[TAILLE];
        for (Case c : this.cases) {
            switch (direction) {
                case HAUT:
                    if ((result[c.getX()] == null) || (result[c.getX()].getY() > c.getY())) { // si on n'avait pas encore de case pour cette rangée ou si on a trouvé un meilleur candidat
                        result[c.getX()] = c;
                    }
                    break;
                case BAS:
                    if ((result[c.getX()] == null) || (result[c.getX()].getY() < c.getY())) {
                        result[c.getX()] = c;
                    }
                    break;
                case GAUCHE:
                    if ((result[c.getY()] == null) || (result[c.getY()].getX() > c.getX())) {
                        result[c.getY()] = c;
                    }
                    break;
                default:
                    if ((result[c.getY()] == null) || (result[c.getY()].getX() < c.getX())) {
                        result[c.getY()] = c;
                    }
                    break;
            }
        }
        return result;
    }

    public boolean nouvelleCase() {
        if (this.cases.size() < TAILLE*TAILLE) {
            ArrayList<Case> casesLibres = new ArrayList<>();
            Random ra = new Random();
            
            //la case a une probabilité de 0.75 d'avoir la valeur 1 et 0.25 d'avoir 2
            int valeur = ra.nextDouble()>0.75 ? 2 : 1;
            
            // on crée toutes les cases encore libres
            for (int x = 0; x < TAILLE; x++) {
                for (int y = 0; y < TAILLE; y++) {
                    Case c = new Case(x, y, valeur);
                    if (!this.cases.contains(c)) { // contains utilise la méthode equals dans Case
                        casesLibres.add(c);
                    }
                }
            }
            // on en choisit une au hasard et on l'ajoute à la grille
            Case ajout = casesLibres.get(ra.nextInt(casesLibres.size()));
            ajout.setGrille(this);
            this.cases.add(ajout);
            
            //actualise valeurMax
            if (this.valeurMax < ajout.getValeur()) {
                this.valeurMax = ajout.getValeur();
            }
            this.joueur.partie.controller.nouvelleCaseGUI(ajout.getX(), ajout.getY(), ajout.getValeur(), ajout.getGrille().getJoueur().getID());
            return true;
        } else {
            return false;
        }
    }
    
    //ajoute une case de valeur et de coordonnées données
    //attention : vérifier que l'emplacement est libre
    public void nouvelleCase(int valeur, int x, int y) {

        Case ajout = new Case(x, y, valeur);
        ajout.setGrille(this);
        this.cases.add(ajout);

        //actualise valeurMax
        if (this.valeurMax < ajout.getValeur()) {
            this.valeurMax = ajout.getValeur();
        }
        
    }
}