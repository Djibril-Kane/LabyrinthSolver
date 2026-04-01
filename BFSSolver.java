import java.util.*;

/**
 * Résout le labyrinthe avec une recherche en largeur (BFS).
 * Utilise une Queue (file) pour explorer les cases niveau par niveau.
 * GARANTIT le chemin le plus court (en nombre de cases).
 */
public class BFSSolver extends LabyrinthSolver {

    // Les 4 directions possibles : haut, bas, gauche, droite
    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    @Override
    public List<int[]> solve(Labyrinth lab) {
        reinitialiserCompteur();

        int[] depart  = lab.getDepart();
        int[] arrivee = lab.getArrivee();
        int lignes    = lab.getLignes();
        int colonnes  = lab.getColonnes();

        // Tableau pour savoir si une case a déjà été visitée
        boolean[][] visite = new boolean[lignes][colonnes];

        // parent[i][j] = case d'où on vient pour reconstruire le chemin
        int[][][] parent = new int[lignes][colonnes][];

        // La file du BFS : on explore d'abord les cases les plus proches
        Queue<int[]> file = new LinkedList<>();

        file.add(depart);
        visite[depart[0]][depart[1]] = true;

        boolean trouve = false;

        while (!file.isEmpty()) {
            int[] courant = file.poll();
            casesExplorees++;

            // Si on a atteint l'arrivée, on arrête
            if (courant[0] == arrivee[0] && courant[1] == arrivee[1]) {
                trouve = true;
                break;
            }

            // Explorer les 4 voisins
            for (int[] dir : DIRECTIONS) {
                int nl = courant[0] + dir[0];
                int nc = courant[1] + dir[1];

                if (lab.estDansLaGrille(nl, nc)
                 && !visite[nl][nc]
                 && !lab.estMur(nl, nc)) {

                    visite[nl][nc] = true;
                    parent[nl][nc] = courant;
                    file.add(new int[]{nl, nc});
                }
            }
        }

        if (!trouve) return new ArrayList<>(); // Aucun chemin

        return reconstruireChemin(parent, depart, arrivee);
    }

    /**
     * Remonte le tableau parent depuis l'arrivée jusqu'au départ
     * pour reconstruire le chemin dans le bon sens.
     */
    private List<int[]> reconstruireChemin(int[][][] parent, int[] depart, int[] arrivee) {
        LinkedList<int[]> chemin = new LinkedList<>();
        int[] courant = arrivee;

        while (courant != null) {
            chemin.addFirst(courant);
            if (courant[0] == depart[0] && courant[1] == depart[1]) break;
            courant = parent[courant[0]][courant[1]];
        }

        return chemin;
    }
}
