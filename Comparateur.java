import java.util.List;

/**
 * Lance les deux algorithmes sur le même labyrinthe et compare leurs performances :
 * - Temps d'exécution (nanosecondes → millisecondes)
 * - Nombre de cases explorées
 * - Longueur du chemin trouvé
 */
public class Comparateur {

    /**
     * Exécute DFS et BFS sur le labyrinthe donné,
     * affiche les résultats côte à côte, et affiche les deux solutions.
     */
    public static void comparer(Labyrinth lab) {
        DFSSolver dfs = new DFSSolver();
        BFSSolver bfs = new BFSSolver();

        // --- Exécution DFS ---
        long debut = System.nanoTime();
        List<int[]> cheminDFS = dfs.solve(lab);
        long fin = System.nanoTime();
        dfs.setTempsExecution(fin - debut);

        // --- Exécution BFS ---
        debut = System.nanoTime();
        List<int[]> cheminBFS = bfs.solve(lab);
        fin = System.nanoTime();
        bfs.setTempsExecution(fin - debut);

        // --- Affichage comparatif ---
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║          COMPARAISON DFS vs BFS                  ║");
        System.out.println("╠══════════════════════════════════════════╦═══════╣");
        System.out.printf( "║ %-40s ║  DFS  ║%n", "Critère");
        System.out.println("╠══════════════════════════════════════════╬═══════╣");
        System.out.printf( "║ %-40s ║ %5d ║%n", "Cases explorées (DFS)",  dfs.getCasesExplorees());
        System.out.printf( "║ %-40s ║ %5d ║%n", "Cases explorées (BFS)",  bfs.getCasesExplorees());
        System.out.printf( "║ %-40s ║ %5d ║%n", "Longueur chemin DFS",    cheminDFS.size());
        System.out.printf( "║ %-40s ║ %5d ║%n", "Longueur chemin BFS",    cheminBFS.size());
        System.out.printf( "║ %-40s ║ %4.2f ms ║%n", "Temps DFS", dfs.getTempsExecution() / 1_000_000.0);
        System.out.printf( "║ %-40s ║ %4.2f ms ║%n", "Temps BFS", bfs.getTempsExecution() / 1_000_000.0);
        System.out.println("╚══════════════════════════════════════════╩═══════╝");

        // --- Conclusion ---
        System.out.println("\n>>> Analyse :");
        if (cheminBFS.size() <= cheminDFS.size()) {
            System.out.println("    BFS trouve le chemin le PLUS COURT (" + cheminBFS.size() + " pas).");
        }
        if (dfs.getTempsExecution() < bfs.getTempsExecution()) {
            System.out.println("    DFS est plus RAPIDE en temps d'exécution.");
        } else {
            System.out.println("    BFS est plus RAPIDE en temps d'exécution.");
        }
        if (dfs.getCasesExplorees() < bfs.getCasesExplorees()) {
            System.out.println("    DFS explore MOINS de cases (va droit au but).");
        } else {
            System.out.println("    BFS explore plus de cases (explore tous les niveaux).");
        }

        // --- Affichage des labyrinthes résolus ---
        System.out.println("\n=== Solution DFS ===");
        lab.reinitialiser();
        lab.marquerChemin(cheminDFS);
        lab.afficher();

        System.out.println("=== Solution BFS (chemin optimal) ===");
        lab.reinitialiser();
        lab.marquerChemin(cheminBFS);
        lab.afficher();

        lab.reinitialiser();
    }
}
