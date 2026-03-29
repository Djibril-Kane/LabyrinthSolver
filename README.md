# Labyrinth Solver

Projet de résolution de Labyrinthe - Master 1 GLSI & SRT  
École Supérieure Polytechnique de Dakar  
Programmation et Algorithmique Avancée

## Description

Programme permettant de générer, charger et résoudre un labyrinthe en utilisant les algorithmes DFS et BFS.

## Représentation du labyrinthe

Le labyrinthe est représenté sous forme de matrice 2D :
- `#` : mur
- `=` : passage
- `S` : point de départ
- `E` : point d'arrivée

Exemple :
```
#######
#S===E#
#=###=#
#=====# 
#######
```

## Structure du projet

```
LabyrinthSolver/main/
├── src/
│   └── LabyrinthSolver.java
├── labyrinthes/
│   └── exemple1.txt
├── .gitignore
└── README.md
```

## Compilation et exécution

### Compiler
```bash
cd src
javac LabyrinthSolver.java
```

### Exécuter
```bash
java LabyrinthSolver
```

## Fonctionnalités

- [ ] Charger un labyrinthe depuis un fichier
- [ ] Générer un labyrinthe aléatoire
- [ ] Résoudre avec DFS (Depth First Search)
- [ ] Résoudre avec BFS (Breadth First Search)
- [ ] Afficher le chemin trouvé
- [ ] Comparer les performances

## Membres du groupe

À remplir

## Livrables

- Code source documenté sur GitHub
- Vidéo de présentation (max 10 minutes)
- Envoyer à : envoitp@gmail.com
- Sujet : `Projet_Labyrinthe_Gx` (x = numéro du groupe)
