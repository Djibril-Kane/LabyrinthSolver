# Labyrinth Solver — DFS & BFS

Résolution de labyrinthes avec algorithmes de graphe  

---

## Description

Programme Java pour **générer**, **charger** et **résoudre** des labyrinthes avec deux algorithmes :
- **DFS** (Depth First Search) — Recherche en profondeur
- **BFS** (Breadth First Search) — Recherche en largeur

Deux interfaces : **console** (`LabyrinthApp.java`) et **graphique** (`LabyrinthGUI.java`)

---

## Structure du projet

```
main/
├── Labyrinth.java              # Modèle du labyrinthe (grille 2D)
├── LabyrinthSolver.java        # Classe abstraite (base DFS/BFS)
├── DFSSolver.java              # Algorithme DFS
├── BFSSolver.java              # Algorithme BFS
├── LabyrinthLoader.java        # Chargement/génération
├── Comparateur.java            # Comparaison DFS vs BFS
├── LabyrinthApp.java           # Interface console
├── LabyrinthGUI.java           # Interface graphique (Swing)
├── .gitignore
└── README.md
```

---

## Compilation & Exécution

```bash
# Compiler tous les fichiers
javac *.java

# Console (menu texte)
java LabyrinthApp

# GUI (interface graphique)
java LabyrinthGUI
```

---

## Représentation

| Symbole | Signification |
|---------|--------------|
| `#` | Mur |
| `=` | Passage |
| `S` | Départ |
| `E` | Arrivée |
| `+` | Chemin solution |

---

## Fonctionnalités

• Charger un labyrinthe depuis fichier `.txt`  
• Générer un labyrinthe aléatoire (dimensions paramétrables)  
• Résoudre avec DFS  
• Résoudre avec BFS  
• Afficher statistiques (chemin, cases explorées, temps)  
• Comparer DFS vs BFS
