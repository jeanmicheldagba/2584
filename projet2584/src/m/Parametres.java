package m;

public interface Parametres {

    //définit les 4 touches des deux joueurs : String[2][4]
    static final String[][] KEYS = new String[][]{new String[]{"z", "q", "s", "d"}, new String[]{"o", "k", "l", "m"}};

    static final int HAUT = 1;
    static final int DROITE = 2;
    static final int BAS = -1;
    static final int GAUCHE = -2;

    static final int TAILLE = 4;
    static final int OBJECTIF = 2584;

    public static int keyToDirection(String key) {
        int direction;
        if(key.equals(KEYS[0][0]) || key.equals(KEYS[1][0])) direction = HAUT;
        else if(key.equals(KEYS[0][1]) || key.equals(KEYS[1][1])) direction = GAUCHE;
        else if(key.equals(KEYS[0][2]) || key.equals(KEYS[1][2])) direction = BAS;
        else direction = DROITE;
        return direction;
    }
}
