public class Jeu 
{
    final static int NOIR = 1;          // Declare state of each square
    final static int BLANC = 2;
    final static int VIDE = 0;
    final static int LARGEUR = 10;
    final static int HAUTEUR = 10;
    final int PLATEAU[][] = new int[LARGEUR][HAUTEUR];
	
	/**
     *  Constructeur par defaut
     */
    public Jeu() 
	{
	}
	
	/**
     *  Créer une copie du jeu
     *
     *  @param    AUTRE    Le jeu qui doit etre copier
     *
     */
    public Jeu(Jeu AUTRE) 
	{
		for (int i = 0; i < HAUTEUR; i++)
		{
			for (int j = 0; j < LARGEUR; j++)
			{
				this.PLATEAU[i][j] = AUTRE.PLATEAU[i][j];
			}
		}
	}
	
    /**
     *  Décider si le coup est légale
     *
     *  @param    r          Ligne dans la matrice du jeu
     *  @param    c          colonne dans la matrice du jeu
     *  @param    couleur      couleur du joueur - Blanc ou Noir
     *  @param    inverser       true si le joueur veux inverser les disques 
     *
     *  @return              true si le coup est légale et false sinon
     */
    public boolean legal(int r, int c, int couleur, boolean inverser) 
	{
		// Initialise le boolean legal a false
		boolean legal = false;
		
		// si la case est vide on commence la recherche
		// si la case n'est pas vide on a pas besoin de rien vérifier 
		if (PLATEAU[r][c] == 0)
		{   //le cas la case est vide
			// Initialisation de variables
			int posX;
			int posY;
			boolean trouver;
			int courant;
			
			// Chercher dans chaque direction
			// x et y décrivent une direction donnée dans 9 directions
			// 0, 0 est redundante 
			for (int x = -1; x <= 1; x++)
			{
				for (int y = -1; y <= 1; y++)
				{
					// Variables pour garder une trace de l’emplacement de l’algorithme et
					 // s’il a trouvé un déplacement valide
					posX = c + x;
					posY = r + y;
					trouver = false;
					courant = PLATEAU[posY][posX];
					
					// Vérifiez la première cellule dans la direction spécifiée par x et y
					// Si la cellule est vide, hors limites ou contient la même couleur
					// ignorer le reste de l’algorithme pour commencer à vérifier une autre direction
					if (courant == -1 || courant == 0 || courant == couleur)
					{
						continue;
					}
					
					// Sinon, vérifiez le long de cette direction
					while (!trouver)
					{
						posX += x;
						posY += y;
						courant = PLATEAU[posY][posX];
						
						// Si l’algorithme trouve un autre élément de la même couleur le long d’une direction
						 // mettre fin à la boucle pour vérifier une nouvelle direction et définir legal sur true
						if (courant == couleur)
						{
							trouver = true;
							legal = true;
							
							// Si inverser est true, inversez les directions et commencez à retourner jusqu’à ce que 
							 // l’algorithme atteint l’emplacement d’origine
							if (inverser)
							{
								posX -= x;
								posY -= y;
								courant = PLATEAU[posY][posX];
								
								while(courant != 0)
								{
									PLATEAU[posY][posX] = couleur;
									posX -= x;
									posY -= y;
									courant = PLATEAU[posY][posX];
								}
							}
						}
						// Si l’algorithme atteint une zone hors limites ou un espace vide
						 // terminer la boucle pour vérifier une nouvelle direction, mais ne définissez pas encore legal sur true
						else if (courant == -1 || courant == 0)
						{
							trouver = true;
						}
					}
				}
			}
		}

        return legal;
    }
	
	
}
