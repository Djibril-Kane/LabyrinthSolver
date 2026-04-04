import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;

/**
 * Interface graphique Swing pour le résolveur de labyrinthe.
 * Affiche la grille, permet de charger/générer, résoudre et comparer DFS vs BFS.
 */
public class LabyrinthGUI extends JFrame {

    // ── Constantes visuelles ──────────────────────────────────────
    private static final int CELL_SIZE    = 30;   // taille d'une case en pixels
    private static final Color COULEUR_MUR      = new Color(40,  40,  40);
    private static final Color COULEUR_PASSAGE  = new Color(240, 240, 230);
    private static final Color COULEUR_DEPART   = new Color(39,  174, 96);
    private static final Color COULEUR_ARRIVEE  = new Color(231, 76,  60);
    private static final Color COULEUR_CHEMIN   = new Color(52,  152, 219);
    private static final Color COULEUR_FOND     = new Color(30,  30,  30);

    // ── État ─────────────────────────────────────────────────────
    private Labyrinth labyrinthe = null;

    // ── Composants UI ────────────────────────────────────────────
    private GrillePanel grillePanel;
    private JLabel      labelStatut;
    private JTextArea   zoneStats;
    private JButton     btnDFS, btnBFS, btnComparer, btnCharger, btnGenerer;

    // ── Constructeur ─────────────────────────────────────────────
    public LabyrinthGUI() {
        setTitle("Résolveur de Labyrinthe — DFS & BFS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));
        getContentPane().setBackground(COULEUR_FOND);

        construireUI();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ── Construction de l'interface ───────────────────────────────
    private void construireUI() {

        // ── Panneau supérieur : boutons de chargement ─────────────
        JPanel panneauHaut = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        panneauHaut.setBackground(new Color(45, 45, 45));

        btnCharger = creerBouton("📂 Charger fichier", new Color(52, 73, 94));
        btnGenerer = creerBouton("⚙ Générer aléatoire", new Color(39, 60, 117));

        panneauHaut.add(btnCharger);
        panneauHaut.add(btnGenerer);

        // champ dimensions pour la génération
        JLabel lblL = new JLabel("Lignes:");
        lblL.setForeground(Color.LIGHT_GRAY);
        JSpinner spinLignes   = new JSpinner(new SpinnerNumberModel(15, 5, 51, 2));
        JLabel lblC = new JLabel("Colonnes:");
        lblC.setForeground(Color.LIGHT_GRAY);
        JSpinner spinColonnes = new JSpinner(new SpinnerNumberModel(21, 5, 71, 2));

        panneauHaut.add(Box.createHorizontalStrut(12));
        panneauHaut.add(lblL);
        panneauHaut.add(spinLignes);
        panneauHaut.add(lblC);
        panneauHaut.add(spinColonnes);

        // ── Panneau central : grille ──────────────────────────────
        grillePanel = new GrillePanel();
        JScrollPane scroll = new JScrollPane(grillePanel);
        scroll.setBackground(COULEUR_FOND);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60), 2));

        // ── Panneau droit : actions + stats ──────────────────────
        JPanel panneauDroit = new JPanel();
        panneauDroit.setLayout(new BoxLayout(panneauDroit, BoxLayout.Y_AXIS));
        panneauDroit.setBackground(new Color(45, 45, 45));
        panneauDroit.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        panneauDroit.setPreferredSize(new Dimension(240, 0));

        btnDFS     = creerBouton("▶ Résoudre DFS",    new Color(22, 160, 133));
        btnBFS     = creerBouton("▶ Résoudre BFS",    new Color(41, 128, 185));
        btnComparer = creerBouton("⇌ Comparer DFS/BFS", new Color(142, 68, 173));

        btnDFS.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnBFS.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnComparer.setAlignmentX(Component.CENTER_ALIGNMENT);

        desactiverBoutonsSolveurs();

        JLabel lblActions = new JLabel("ACTIONS");
        lblActions.setForeground(new Color(180, 180, 180));
        lblActions.setFont(new Font("Arial", Font.BOLD, 11));
        lblActions.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblStatsTitle = new JLabel("RÉSULTATS");
        lblStatsTitle.setForeground(new Color(180, 180, 180));
        lblStatsTitle.setFont(new Font("Arial", Font.BOLD, 11));
        lblStatsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        zoneStats = new JTextArea(12, 18);
        zoneStats.setEditable(false);
        zoneStats.setFont(new Font("Monospaced", Font.PLAIN, 12));
        zoneStats.setBackground(new Color(30, 30, 30));
        zoneStats.setForeground(new Color(200, 200, 200));
        zoneStats.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        zoneStats.setText("Chargez ou générez\nun labyrinthe\npour commencer.");

        // Légende
        JPanel legende = construireLegende();

        panneauDroit.add(lblActions);
        panneauDroit.add(Box.createVerticalStrut(8));
        panneauDroit.add(btnDFS);
        panneauDroit.add(Box.createVerticalStrut(6));
        panneauDroit.add(btnBFS);
        panneauDroit.add(Box.createVerticalStrut(6));
        panneauDroit.add(btnComparer);
        panneauDroit.add(Box.createVerticalStrut(16));
        panneauDroit.add(new JSeparator());
        panneauDroit.add(Box.createVerticalStrut(12));
        panneauDroit.add(lblStatsTitle);
        panneauDroit.add(Box.createVerticalStrut(8));
        panneauDroit.add(new JScrollPane(zoneStats));
        panneauDroit.add(Box.createVerticalStrut(12));
        panneauDroit.add(new JSeparator());
        panneauDroit.add(Box.createVerticalStrut(8));
        panneauDroit.add(legende);

        // ── Barre de statut ───────────────────────────────────────
        labelStatut = new JLabel("  Bienvenue — chargez ou générez un labyrinthe.");
        labelStatut.setForeground(Color.LIGHT_GRAY);
        labelStatut.setBackground(new Color(35, 35, 35));
        labelStatut.setOpaque(true);
        labelStatut.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        labelStatut.setFont(new Font("Arial", Font.PLAIN, 12));

        // ── Ajout au frame ────────────────────────────────────────
        add(panneauHaut,  BorderLayout.NORTH);
        add(scroll,       BorderLayout.CENTER);
        add(panneauDroit, BorderLayout.EAST);
        add(labelStatut,  BorderLayout.SOUTH);

        // ── Listeners ─────────────────────────────────────────────
        btnCharger.addActionListener(e -> chargerFichier());

        btnGenerer.addActionListener(e -> {
            int l = (int) spinLignes.getValue();
            int c = (int) spinColonnes.getValue();
            genererAleatoire(l, c);
        });

        btnDFS.addActionListener(e -> resoudre(new DFSSolver(), "DFS", COULEUR_CHEMIN));
        btnBFS.addActionListener(e -> resoudre(new BFSSolver(), "BFS", new Color(230, 126, 34)));
        btnComparer.addActionListener(e -> comparer());
    }

    // ── Actions ───────────────────────────────────────────────────

    private void chargerFichier() {
        JFileChooser fc = new JFileChooser(".");
        fc.setFileFilter(new FileNameExtensionFilter("Fichiers texte (*.txt)", "txt"));
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            try {
                labyrinthe = LabyrinthLoader.chargerDepuisFichier(f.getAbsolutePath());
                grillePanel.setLabyrinth(labyrinthe, null);
                activerBoutonsSolveurs();
                setStatut("✔ Labyrinthe chargé : " + f.getName()
                    + " (" + labyrinthe.getLignes() + "×" + labyrinthe.getColonnes() + ")");
                zoneStats.setText("Labyrinthe chargé.\nLancez DFS ou BFS.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement :\n" + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void genererAleatoire(int lignes, int colonnes) {
        try {
            labyrinthe = LabyrinthLoader.genererAleatoire(lignes, colonnes);
            grillePanel.setLabyrinth(labyrinthe, null);
            activerBoutonsSolveurs();
            setStatut("✔ Labyrinthe généré : "
                + labyrinthe.getLignes() + "×" + labyrinthe.getColonnes());
            zoneStats.setText("Labyrinthe généré.\nLancez DFS ou BFS.");
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Erreur lors de la génération :\n" + ex.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resoudre(LabyrinthSolver solveur, String nom, Color couleurChemin) {
        if (labyrinthe == null) return;
        labyrinthe.reinitialiser();

        long debut = System.nanoTime();
        List<int[]> chemin = solveur.solve(labyrinthe);
        long fin = System.nanoTime();
        solveur.setTempsExecution(fin - debut);

        if (chemin.isEmpty()) {
            setStatut("✘ " + nom + " : aucun chemin trouvé !");
            zoneStats.setText(nom + " : aucun chemin\ntrouvé.");
            grillePanel.setLabyrinth(labyrinthe, null);
            return;
        }

        labyrinthe.marquerChemin(chemin);
        grillePanel.setLabyrinth(labyrinthe, couleurChemin);

        double ms = solveur.getTempsExecution() / 1_000_000.0;
        setStatut("✔ " + nom + " — chemin : " + chemin.size()
            + " cases | exploré : " + solveur.getCasesExplorees()
            + " | " + String.format("%.3f", ms) + " ms");

        zoneStats.setText(
            "Algorithme : " + nom + "\n" +
            "─────────────────\n" +
            "Chemin      : " + chemin.size() + " cases\n" +
            "Explorées   : " + solveur.getCasesExplorees() + " cases\n" +
            "Temps       : " + String.format("%.3f ms", ms) + "\n"
        );

        labyrinthe.reinitialiser();
    }

    private void comparer() {
        if (labyrinthe == null) return;
        labyrinthe.reinitialiser();

        DFSSolver dfs = new DFSSolver();
        BFSSolver bfs = new BFSSolver();

        long d = System.nanoTime();
        List<int[]> chDFS = dfs.solve(labyrinthe);
        dfs.setTempsExecution(System.nanoTime() - d);

        d = System.nanoTime();
        List<int[]> chBFS = bfs.solve(labyrinthe);
        bfs.setTempsExecution(System.nanoTime() - d);

        double msDFS = dfs.getTempsExecution() / 1_000_000.0;
        double msBFS = bfs.getTempsExecution() / 1_000_000.0;

        String vainqueurChemin = chBFS.size() <= chDFS.size() ? "BFS ✔" : "DFS ✔";
        String vainqueurTemps  = msDFS <= msBFS ? "DFS ✔" : "BFS ✔";

        String rapport =
            "══ COMPARAISON ══\n\n" +
            "          DFS      BFS\n" +
            "Chemin  " + String.format("%5d  %5d", chDFS.size(), chBFS.size()) + "\n" +
            "Explor. " + String.format("%5d  %5d", dfs.getCasesExplorees(), bfs.getCasesExplorees()) + "\n" +
            "Temps   " + String.format("%5.2f  %5.2f ms", msDFS, msBFS) + "\n\n" +
            "Plus court  : " + vainqueurChemin + "\n" +
            "Plus rapide : " + vainqueurTemps;

        zoneStats.setText(rapport);
        setStatut("⇌ Comparaison terminée — chemin le plus court : " + vainqueurChemin);

        // Affiche le chemin BFS (optimal) en orange sur la grille
        labyrinthe.marquerChemin(chBFS);
        grillePanel.setLabyrinth(labyrinthe, new Color(230, 126, 34));
        labyrinthe.reinitialiser();
    }

    // ── Helpers UI ────────────────────────────────────────────────

    private JButton creerBouton(String texte, Color couleur) {
        JButton btn = new JButton(texte);
        btn.setBackground(couleur);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(220, 38));
        btn.setPreferredSize(new Dimension(220, 38));
        return btn;
    }

    private JPanel construireLegende() {
        JPanel p = new JPanel(new GridLayout(5, 1, 2, 2));
        p.setBackground(new Color(45, 45, 45));
        p.add(entreeLegende(COULEUR_MUR,     "Mur"));
        p.add(entreeLegende(COULEUR_PASSAGE, "Passage"));
        p.add(entreeLegende(COULEUR_DEPART,  "Départ (S)"));
        p.add(entreeLegende(COULEUR_ARRIVEE, "Arrivée (E)"));
        p.add(entreeLegende(COULEUR_CHEMIN,  "Chemin"));
        return p;
    }

    private JPanel entreeLegende(Color couleur, String label) {
        JPanel ligne = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
        ligne.setBackground(new Color(45, 45, 45));
        JPanel carre = new JPanel();
        carre.setBackground(couleur);
        carre.setPreferredSize(new Dimension(14, 14));
        JLabel lbl = new JLabel(label);
        lbl.setForeground(Color.LIGHT_GRAY);
        lbl.setFont(new Font("Arial", Font.PLAIN, 11));
        ligne.add(carre);
        ligne.add(lbl);
        return ligne;
    }

    private void setStatut(String msg) {
        labelStatut.setText("  " + msg);
    }

    private void activerBoutonsSolveurs() {
        btnDFS.setEnabled(true);
        btnBFS.setEnabled(true);
        btnComparer.setEnabled(true);
    }

    private void desactiverBoutonsSolveurs() {
        btnDFS.setEnabled(false);
        btnBFS.setEnabled(false);
        btnComparer.setEnabled(false);
    }

    // ── Panneau de dessin de la grille ────────────────────────────

    /**
     * Composant qui dessine la grille du labyrinthe case par case.
     */
    private static class GrillePanel extends JPanel {

        private Labyrinth lab      = null;
        private Color couleurChemin = COULEUR_CHEMIN;

        public void setLabyrinth(Labyrinth lab, Color couleurChemin) {
            this.lab = lab;
            if (couleurChemin != null) this.couleurChemin = couleurChemin;
            if (lab != null) {
                setPreferredSize(new Dimension(
                    lab.getColonnes() * CELL_SIZE,
                    lab.getLignes()   * CELL_SIZE
                ));
            }
            revalidate();
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(COULEUR_FOND);

            if (lab == null) {
                g.setColor(Color.GRAY);
                g.setFont(new Font("Arial", Font.ITALIC, 14));
                g.drawString("Aucun labyrinthe chargé.", 20, 40);
                return;
            }

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            char[][] grille = lab.getGrille();

            for (int i = 0; i < lab.getLignes(); i++) {
                for (int j = 0; j < lab.getColonnes(); j++) {
                    char c = grille[i][j];
                    Color fond = switch (c) {
                        case '#' -> COULEUR_MUR;
                        case 'S' -> COULEUR_DEPART;
                        case 'E' -> COULEUR_ARRIVEE;
                        case '+' -> couleurChemin;
                        default  -> COULEUR_PASSAGE;
                    };

                    int x = j * CELL_SIZE;
                    int y = i * CELL_SIZE;

                    g2.setColor(fond);
                    g2.fillRect(x, y, CELL_SIZE, CELL_SIZE);

                    // Bordure fine entre cases
                    g2.setColor(new Color(0, 0, 0, 60));
                    g2.drawRect(x, y, CELL_SIZE, CELL_SIZE);

                    // Lettre S et E
                    if (c == 'S' || c == 'E') {
                        g2.setColor(Color.WHITE);
                        g2.setFont(new Font("Arial", Font.BOLD, CELL_SIZE / 2));
                        FontMetrics fm = g2.getFontMetrics();
                        int tx = x + (CELL_SIZE - fm.stringWidth(String.valueOf(c))) / 2;
                        int ty = y + (CELL_SIZE + fm.getAscent() - fm.getDescent()) / 2;
                        g2.drawString(String.valueOf(c), tx, ty);
                    }
                }
            }
        }
    }

    // ── main ──────────────────────────────────────────────────────

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LabyrinthGUI::new);
    }
}