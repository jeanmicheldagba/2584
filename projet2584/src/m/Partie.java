package m;

import java.io.Serializable;
import java.util.Random;
import vc.Controller;

/**
 * Classe définissant une partie
 * @author vaurien
 * @author jmdag
 * @author apollo7
 */
public class Partie implements Parametres, Serializable{

    private Joueur[] joueurs;                   // tableaux des joueurs de la partie
    protected transient Controller controller;  //
    private boolean gameover;                   // détermine la fin de la partie : true=partie finie
    private ConnexionBDD connexionbdd;          // permet d'enregistrer la partie

    /**
     * Constructeur de la classe Partie
     * @param co le controlleur de la partie
     */
    public Partie(Controller co) {
        this.joueurs = new Joueur[2];
        this.controller = co;
        this.gameover = false;
        this.connexionbdd= new ConnexionBDD(HOST,PORT,DBNAME,USERNAME,PASSWORD);
    }
    
    /**
     * Getter des joueurs de la partie
     * @return le tableau des joueurs
     */
    public Joueur[] getJoueur(){
        return this.joueurs;
    }
    
    /**
     * Getter gameOver
     * @return true si la partie est finie, false sinon
     */
    public boolean getGameover() {
        return this.gameover;
    }
    
    /**
     * Setter de gameOver
     * @param go la nouvelle valeur pour la fin du jeu
     */
    public void setGameover(boolean go){
        this.gameover=go;
    }

    /**
     * ajoute les deux cases à chaque grille en suivant les règles d'initialisation
     */
    public void initGrilles() {
        System.out.println("initialisation des grilles");
        
        Random ra = new Random();
        //stocke les coordonnées x et y des 2 cases
        int[] x = new int[2];
        int[] y = new int[2];

        //coordonnées x et y de la première case
        x[0] = ra.nextInt(TAILLE);
        y[0] = ra.nextInt(TAILLE);

        x[1] = x[0];
        y[1] = y[0];

        //coordonnées de la seconde case
        do {
            x[1] = ra.nextInt(TAILLE);
            y[1] = ra.nextInt(TAILLE);
        } while (x[1] == x[0] && y[1] == y[0]); // cases à endroit différent

        int[] valeurs = new int[2]; // stocke les valeurs des 2 cases

        //règles de valeurs des cases
        if (ra.nextDouble() > 0.75) { // probabilité de 0.25 d'avoir un 2
            valeurs[0] = 2;
            valeurs[1] = 1; // la deuxième case est 1 car il ne peut pas y avoir 2x2
        } else { 
            valeurs[0] = 1; 
            if (ra.nextDouble() > 0.75) { // probabilité de 0.25 d'avoir un 2
                valeurs[1] = 2;
            } else {
                valeurs[1] = 1;
            }
        }
        
        //ajoute les deux cases à la grille de chaque joueur
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                this.joueurs[i].getGrille().nouvelleCase(valeurs[j], x[j], y[j]);
            }

        }
    }
    
    /**
     * met à jour la base de données avec les informations de la partie quand celle-ci se finit = historique des parties
     */
    public void majBDD(){
        String name1=new String(); //noms des 2 joueurs dans la base de données
        String name2=new String();
        if(joueurs[0] instanceof Human){//si le joueur est humain son nom correspond à son pseudo
            name1=((Human)joueurs[0]).getPseudo();
        }
        else if(joueurs[0] instanceof Dumb){//si le joueur est le programme jouant au hasard (Dumb) son nom est "Aléatoire"
            name1="Dumb";
        }
        else if(joueurs[0] instanceof IA){//si le joueur est le programme jouant intelligemment (IA) son nom est "Intelligence Artificielle" 
            name1="AI";
        }
        //mêmes instructions pour le 2ème joueur
        if(joueurs[1] instanceof Human){
            name2=((Human)joueurs[1]).getPseudo();
        }
        else if(joueurs[1] instanceof Dumb){
            name2="Dumb";
        }
        else if(joueurs[1] instanceof IA){
            name2="AI";
        }
        //Requête SQL qui permet d'insérer les informations de la partie dans la base de données
        String query="INSERT INTO historiqueparties VALUES(null,'"+name1+"','"+name2+"',"+joueurs[0].getScore()+","+joueurs[1].getScore()+","+joueurs[0].grille.getValeurMax()+","+joueurs[1].grille.getValeurMax()+","+joueurs[0].getNbDeplacements()+","+joueurs[1].getNbDeplacements()+")"; //1ère colonne à null car auto incrémentation dans la bdd
        connexionbdd.insertTuples(query);
    }
    
    public ConnexionBDD getConnexionBDD() {
        return this.connexionbdd;
    }

    /**
     * Setter du controller de la partie
     * @param co le nouveau controller de la partie
     */
    public void setController(Controller co) {
        this.controller = co;
    }
    
}
