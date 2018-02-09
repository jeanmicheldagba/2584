package model;

import java.util.Scanner;

public class Partie implements Parametres {
    
    private Joueur[] joueur;
    
    public Partie(){
        this.joueur = new Joueur[2];
    }
    
    public void init(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Bienvenue !\nLe joueur 1 est-il un humain ? Tape o pour oui et n pour non.");
        String s = sc.nextLine().toLowerCase();
        if()
        
    }
    
}