import java.util.List;

public abstract class LabyrinthSolver {

    protected int casesExplorees = 0;
    protected long tempsExecution = 0;

    public abstract List<int[]> solve(Labyrinth lab);

    protected void reinitialiserCompteur() {
        casesExplorees = 0;
    }

    public int getCasesExplorees() { 
        return casesExplorees; 
    }

    public long getTempsExecution() { 
        return tempsExecution; 
    }

    public void setTempsExecution(long t) { 
        this.tempsExecution = t; 
    }
}