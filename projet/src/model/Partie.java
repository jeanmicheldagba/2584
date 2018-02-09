package model;

import java.util.Scanner;

public class Partie implements Parametres {
    
    private Joueur[] joueur;
    private boolean gui;
    
    public Partie(){
        this.joueur = new Joueur[2];
    }
    
    public boolean getPartie(){
        return this.gui;
    }
    
    public void init(){
        this.initJeu();
        this.jouer();
    }
    
    public void initJeu(){
        Scanner sc = new Scanner(System.in);
        String s;
        String pseudo = "";
        
        System.out.println("Bienvenue !");
        
        for(int i=0;i<2;i++){
            s = "blabla";
            do {
                System.out.println("Le joueur "+(i+1)+" est-il un humain ? Tape o pour oui et n pour non.");
                s = sc.nextLine().toLowerCase();
            } while(!(s.equals("o") || s.equals("n")));
            
            

            if(s.equals("o")){
                s=pseudo;
                do {
                    System.out.println("Veuillez entrer son pseudo :");
                    s = sc.nextLine();
                    if(s.equals(pseudo)){
                        System.out.println("Erreur : pseudo indisponible !");
                    }
                } while(s.equals(pseudo) || s.equals(""));
                pseudo = s;
                
                this.joueur[i] = new Human(pseudo);
            } else {
                s="blabla";
                do {
                    System.out.println("Est-ce un bête programme aléatoire ? Tape o pour oui et n pour non.");
                    s = sc.nextLine().toLowerCase();
                } while(!(s.equals("o") || s.equals("n")));
                
                if(s.equals("o")){
                    this.joueur[i] = new Dumb();
                }
                else {
                    System.out.println("OK ! Le joueur "+(i+1)+" est une IA.");
                    this.joueur[i] = new IA();
                }
                
            }
        }
        
        s="blabla";
        do {
            System.out.println("Jouer avec l'interface graphique ? Tape o pour oui et n pour non :");
            s=sc.nextLine().toLowerCase();           
        }while(!(s.equals("o") || s.equals("n")));
        this.gui = s.equals("o");
        
    }
    
    public void jouer(){
        Scanner sc = new Scanner(System.in);
        
        while (!this.partieBloquee()) {
            for(int i=0;i<2;i++){
                if(this.joueur[i] instanceof Human){
                    this.joueur[i].jouer();
                }
            }
        }
        for(int i=0;i<2;i++) this.joueur[i].gameOver();
        System.exit(1);

    }
    
    //détermine si un des joueurs est bloqué
    public boolean partieBloquee(){
        boolean bloque=false;
        for(int i=0;i<2;i++){
            if(this.joueur[i].getGrille().bloquee()){
                bloque = true;
                System.out.println("Le joueur "+(i+1)+" a perdu !");
            }
        }
        return bloque;
    }
    
}
