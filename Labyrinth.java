import java.util.List;

/**
 * Représente un labyrinthe sous forme de grille 2D.
 * '#' = mur, '=' = passage, 'S' = départ, 'E' = arrivée, '+' = chemin solution
 */
public class Labyrinth {

    private char[][] grille;
    private int lignes;
    private int colonnes;
    private int[] depart;   
    private int[] arrivee;  

    /**
     * Construit un labyrinthe à partir d'une grille de caractères.
     * Détecte automatiquement les positions S et E.
     */
    public Labyrinth(char[][] grille) {
        this.grille = grille;
        this.lignes = grille.length;
        this.colonnes = grille[0].length;
        detecterDepartArrivee();
    }

    public int getLignes() { 
        return lignes; 
    }

    public int getColonnes() { 
        return colonnes; 
    }
    
    public int[] getDepart() { 
        return depart; 
    }
    
    public int[] getArrivee() { 
        return arrivee; 
    }
    
    public char[][] getGrille() { 
        return grille; 
    }

    /** Parcourt la grille pour trouver S et E. */
    private void detecterDepartArrivee() {
        for (int i = 0; i < lignes; i++) {
            for (int j = 0; j < colonnes; j++) {
                if (grille[i][j] == 'S') depart  = new int[]{i, j};
                if (grille[i][j] == 'E') arrivee = new int[]{i, j};
            }
        }
    }

    /** Retourne le caractère à la position (ligne, col). */
    public char getCell(int ligne, int col) {
        return grille[ligne][col];
    }

    /** Vérifie si la case est un mur. */
    public boolean estMur(int ligne, int col) {
        return grille[ligne][col] == '#';
    }

    /** Vérifie si (ligne, col) est dans les bornes de la grille. */
    public boolean estDansLaGrille(int ligne, int col) {
        return ligne >= 0 && ligne < lignes && col >= 0 && col < colonnes;
    }

    /**
     * Affiche le labyrinthe dans la console.
     * Utilisée avant et après résolution.
    */
    public void afficher() {
        System.out.println();
        for (int i = 0; i < lignes; i++) {
            for (int j = 0; j < colonnes; j++) {
                System.out.print(grille[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Marque les cases du chemin trouvé avec '+', sans écraser S et E.
    */
    public void marquerChemin(List<int[]> chemin) {
        for (int[] case_ : chemin) {
            int l = case_[0];
            int c = case_[1];
            if (grille[l][c] != 'S' && grille[l][c] != 'E') {
                grille[l][c] = '+';
            }
        }
    }

    /**
     * Remet le labyrinthe à son état initial (efface les '+').
    */
    public void reinitialiser() {
        for (int i = 0; i < lignes; i++) {
            for (int j = 0; j < colonnes; j++) {
                if (grille[i][j] == '+') grille[i][j] = '=';
            }
        }
    }

}