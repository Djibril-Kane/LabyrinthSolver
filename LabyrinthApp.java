import java.util.List;
import java.util.Scanner;

/**
 * Point d'entrée du programme.
 * Propose un menu console pour charger/générer un labyrinthe,
 * le résoudre avec DFS ou BFS, et comparer les deux algorithmes.
 */
public class LabyrinthApp {

    private static Labyrinth labyrinthe = null;
    private static final Scanner scanner  = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║       RÉSOLVEUR DE LABYRINTHE        ║");
        System.out.println("║     DFS & BFS — Master 1 GLSI/SRT    ║");
        System.out.println("╚══════════════════════════════════════╝");

        boolean quitter = false;

        while (!quitter) {
            afficherMenu();
            int choix = lireEntier();

            switch (choix) {
                case 1 -> chargerDepuisFichier();
                case 2 -> genererAleatoire();
                case 3 -> resoudre(new DFSSolver(), "DFS");
                case 4 -> resoudre(new BFSSolver(), "BFS");
                case 5 -> comparerAlgorithmes();
                case 6 -> afficherLabyrinthe();
                case 0 -> { quitter = true; System.out.println("\nAu revoir !"); }
                default -> System.out.println("  [!] Choix invalide, réessayez.");
            }
        }

        scanner.close();
    }

    /** Affiche le menu principal. */
    private static void afficherMenu() {
        System.out.println("\n┌─────────────────────────────────┐");
        System.out.println("│            MENU                 │");
        System.out.println("├─────────────────────────────────┤");
        System.out.println("│  1. Charger depuis un fichier   │");
        System.out.println("│  2. Générer aléatoirement       │");
        System.out.println("│  3. Résoudre avec DFS           │");
        System.out.println("│  4. Résoudre avec BFS           │");
        System.out.println("│  5. Comparer DFS vs BFS         │");
        System.out.println("│  6. Afficher le labyrinthe      │");
        System.out.println("│  0. Quitter                     │");
        System.out.println("└─────────────────────────────────┘");
        System.out.print("  Votre choix : ");
    }

    /** Option 1 : charge un labyrinthe depuis un fichier texte. */
    private static void chargerDepuisFichier() {
        System.out.print("  Chemin du fichier : ");
        String chemin = scanner.nextLine().trim();
        try {
            labyrinthe = LabyrinthLoader.chargerDepuisFichier(chemin);
            System.out.println("  [OK] Labyrinthe chargé ("
                + labyrinthe.getLignes() + " lignes x "
                + labyrinthe.getColonnes() + " colonnes).");
            labyrinthe.afficher();
        } catch (Exception e) {
            System.out.println("  [ERREUR] Impossible de lire le fichier : " + e.getMessage());
        }
    }

    /** Option 2 : génère un labyrinthe aléatoire. */
    private static void genererAleatoire() {
        System.out.print("  Nombre de lignes   (ex: 15) : ");
        int lignes = lireEntier();
        System.out.print("  Nombre de colonnes (ex: 21) : ");
        int colonnes = lireEntier();

        if (lignes < 5 || colonnes < 5) {
            System.out.println("  [!] Dimensions trop petites, minimum 5x5.");
            return;
        }

        labyrinthe = LabyrinthLoader.genererAleatoire(lignes, colonnes);
        System.out.println("  [OK] Labyrinthe généré ("
            + labyrinthe.getLignes() + " lignes x "
            + labyrinthe.getColonnes() + " colonnes).");
        labyrinthe.afficher();
    }

    /** Options 3 & 4 : résout le labyrinthe avec l'algorithme choisi. */
    private static void resoudre(LabyrinthSolver solveur, String nomAlgo) {
        if (labyrinthe == null) {
            System.out.println("  [!] Aucun labyrinthe chargé. Utilisez l'option 1 ou 2.");
            return;
        }

        labyrinthe.reinitialiser();

        long debut = System.nanoTime();
        List<int[]> chemin = solveur.solve(labyrinthe);
        long fin = System.nanoTime();
        solveur.setTempsExecution(fin - debut);

        if (chemin.isEmpty()) {
            System.out.println("  [!] Aucun chemin trouvé par " + nomAlgo + " !");
            return;
        }

        labyrinthe.marquerChemin(chemin);

        System.out.println("\n  === Résultat " + nomAlgo + " ===");
        labyrinthe.afficher();

        System.out.println("  Cases explorées  : " + solveur.getCasesExplorees());
        System.out.println("  Longueur chemin  : " + chemin.size() + " cases");
        System.out.printf( "  Temps d'exécution: %.4f ms%n", solveur.getTempsExecution() / 1_000_000.0);

        labyrinthe.reinitialiser();
    }

    /** Option 5 : compare DFS et BFS sur le même labyrinthe. */
    private static void comparerAlgorithmes() {
        if (labyrinthe == null) {
            System.out.println("  [!] Aucun labyrinthe chargé. Utilisez l'option 1 ou 2.");
            return;
        }
        Comparateur.comparer(labyrinthe);
    }

    /** Option 6 : affiche le labyrinthe actuel. */
    private static void afficherLabyrinthe() {
        if (labyrinthe == null) {
            System.out.println("  [!] Aucun labyrinthe chargé.");
            return;
        }
        labyrinthe.afficher();
    }

    /** Lit un entier depuis la console, gère les erreurs de saisie. */
    private static int lireEntier() {
        while (true) {
            try {
                String ligne = scanner.nextLine().trim();
                return Integer.parseInt(ligne);
            } catch (NumberFormatException e) {
                System.out.print("  [!] Entier attendu, réessayez : ");
            }
        }
    }
}