import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Permet de charger un labyrinthe depuis un fichier texte
 * ou d'en générer un aléatoirement.
 */
public class LabyrinthLoader {

    /**
     * Charge un labyrinthe depuis un fichier texte.
     * Chaque ligne du fichier = une ligne de la grille.
     * @param chemin chemin vers le fichier .txt
     * @return un objet Labyrinthe prêt à l'emploi
     */
    public static Labyrinth chargerDepuisFichier(String chemin) throws IOException {
        List<String> lignes = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(chemin))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                if (!ligne.isEmpty()) lignes.add(ligne);
            }
        }

        // Calcule la largeur max pour uniformiser les lignes
        int largeurMax = lignes.stream().mapToInt(String::length).max().orElse(0);

        char[][] grille = new char[lignes.size()][largeurMax];
        for (int i = 0; i < lignes.size(); i++) {
            String l = lignes.get(i);
            for (int j = 0; j < largeurMax; j++) {
                grille[i][j] = j < l.length() ? l.charAt(j) : '#';
            }
        }

        return new Labyrinth(grille);
    }

    /**
     * Génère un labyrinthe aléatoire de taille (lignes x colonnes).
     * Garantit que S et E existent et sont accessibles sur les bords.
     * La génération utilise une approche simple : couloirs aléatoires.
     *
     * @param lignes   nombre de lignes (impair recommandé)
     * @param colonnes nombre de colonnes (impair recommandé)
     * @return un objet Labyrinthe généré aléatoirement
     */
    public static Labyrinth genererAleatoire(int lignes, int colonnes) {
        // S'assure que les dimensions sont impaires pour que l'algorithme fonctionne bien
        if (lignes % 2 == 0)   lignes++;
        if (colonnes % 2 == 0) colonnes++;

        char[][] grille = new char[lignes][colonnes];
        Random rand = new Random();

        // Remplit tout de murs d'abord
        for (int i = 0; i < lignes; i++)
            for (int j = 0; j < colonnes; j++)
                grille[i][j] = '#';

        // Creuse des passages avec un DFS aléatoire (recursive backtracking)
        creuserPassages(grille, 1, 1, rand, lignes, colonnes);

        // Place le départ S en haut à gauche et l'arrivée E en bas à droite
        grille[1][1]                       = 'S';
        grille[lignes - 2][colonnes - 2]   = 'E';

        return new Labyrinth(grille);
    }

    /**
     * Algorithme de recursive backtracking pour creuser des passages.
     * Part d'une cellule et creuse dans 4 directions aléatoires.
     */
    private static void creuserPassages(char[][] grille, int ligne, int col,
                                        Random rand, int lignes, int colonnes) {
        // Directions : haut, bas, gauche, droite (par pas de 2)
        int[][] directions = {{-2, 0}, {2, 0}, {0, -2}, {0, 2}};

        // Mélange les directions pour un résultat aléatoire
        for (int i = directions.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int[] tmp = directions[i];
            directions[i] = directions[j];
            directions[j] = tmp;
        }

        grille[ligne][col] = '=';

        for (int[] dir : directions) {
            int nouvLigne = ligne + dir[0];
            int nouvCol   = col   + dir[1];

            if (nouvLigne > 0 && nouvLigne < lignes - 1
             && nouvCol   > 0 && nouvCol   < colonnes - 1
             && grille[nouvLigne][nouvCol] == '#') {

                // Perce le mur entre les deux cellules
                grille[ligne + dir[0] / 2][col + dir[1] / 2] = '=';
                creuserPassages(grille, nouvLigne, nouvCol, rand, lignes, colonnes);
            }
        }
    }
}
