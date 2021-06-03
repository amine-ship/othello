public class Blanc 
{

	
    /** 
     *  Cette méthode appelle la stratégie appropriée.
     *
     *  @param    game    l’état actuel du jeu
     *  @param    done    true si le joueur ne bouger nul part
     *  @param    couleur   la couleur (Blanc ou Noir) du joueur
     *
     *  @return   game    l’état résultant du jeu
     */
    public Jeu strategie(Jeu game, boolean done, int couleur) {
        
        return randStrategie(game,done,couleur);
    }

    /**
     *  Prend le tour en utilisant une strategie aléatoir.
     *
      *  @param    game    l’état actuel du jeu
     *  @param    done    true si le joueur ne bouger nul part
     *  @param    couleur   la couleur (Blanc ou Noir) du joueur
     *
     *  @return   game    l’état résultant du jeu
     */
    public Jeu randStrategie(Jeu game, boolean done, int couleur) {

        int ligne = (int)(Math.random()*(game.HAUTEUR-2)) + 1;
        int colonne = (int)(Math.random()*(game.LARGEUR-2)) + 1;
        
        while (!done && !game.legal(ligne,colonne,couleur,true)) {
            ligne = (int)(Math.random()*(game.HAUTEUR-2)) + 1;
            colonne = (int)(Math.random()*(game.LARGEUR-2)) + 1;
        }
        
        if (!done) 
            game.PLATEAU[ligne][colonne] = couleur;

        return game;
    }

  
}
