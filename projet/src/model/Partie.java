package model;

import java.util.Scanner;

public class Partie implements Parametres {
    
    private Joueur[] joueur;
    
    public Partie(){
        this.joueur = new Joueur[2];
    }
    
    public void init(){
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
                    this.joueur[i] = new Random();
                }
                else {
                    System.out.println("OK ! Le joueur "+(i+1)+" est une IA.");
                    this.joueur[i] = new IA();
                }
                
            }
        }
            
        
    }
    
}