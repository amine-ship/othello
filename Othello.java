import java.awt.*;        
import java.awt.event.*;
import javax.swing.*;



public class Othello extends JPanel 
{
    final static int NOIR = 1;          // Declare state of each square
    final static int BLANC = 2;
    final static int VIDE = 0;
    final static int HORSJEU = -1;

    Noir black = new Noir();          // The players
    Blanc white = new Blanc();

    private Jeu game = new Jeu();     // Game state
    private int tour = NOIR;
    private boolean black_done = false; 
    private boolean white_done = false;
    
    

    /**
     *  Ce constructeur configure la configuration initiale du jeu, 
     * et démarre la minuterie.
     *
     */
    public Othello() {

       //initialise le jeu
        initGame(game);
       
       //mettre la couleur du tableau en vert
            setBackground(Color.GREEN);
            //pour récuperer la case cliqué par le joueur noir et effectuer le move
            addMouseListener( new MouseAdapter() {
                    public void mousePressed(MouseEvent evt) {
                        // Découvrez quel carré a été cliqué
                        int x = evt.getX();
                        int y = evt.getY();
                        int screenWidth = getWidth();
                        int screenHeight = getHeight();
                        int colonne = (x*(game.LARGEUR-2))/screenWidth+1;
                        int ligne = (y*(game.HAUTEUR-2))/screenHeight+1;
                        
                        //si la case cliqué ne correspond pas à un légal move
                        if (!game.legal(ligne,colonne,NOIR,true)) 
                           System.out.println("Coup non permis - essaiez encore!");
                        else {
                            game.PLATEAU[ligne][colonne] = NOIR;
                            //on fait la mise à jour du tableau
                            repaint();
                            //on test s'il reste encore des move possible pour le noir
                            black_done = true;
                            for (int i=1; i<game.HAUTEUR-1; i++)
                                for (int j=1; j<game.LARGEUR-1; j++)
                                    if (game.legal(i,j,NOIR,false) )
                                    black_done=false;
                            //on done la main au blanc
                                    CoupBlanc();
                        }
                    }
                });
        

      
    }
   
    /** 
     *  Initialise le plateau de jeu
     *
     *  @param    game    Le plateau du jeu
     */
    public void initGame(Jeu game) {

        //le noire qui commence toujours
        tour = NOIR;
        
        
         // Initialiser les carrés hors plateau
        for (int i=0; i<game.LARGEUR ; i++) {     
            game.PLATEAU[i][0] = HORSJEU;
            game.PLATEAU[i][game.LARGEUR-1] = HORSJEU;
            game.PLATEAU[0][i] = HORSJEU;
            game.PLATEAU[game.HAUTEUR-1][i] = HORSJEU;
        }

        // Initialiser le plateau de jeu pour qu’il soit vide à l’exception de la configuration initiale
        for (int i=1; i<game.HAUTEUR-1; i++)        
            for (int j=1; j<game.LARGEUR-1; j++)
			   game.PLATEAU[i][j] = VIDE;
       
               //on initialise les case du milieu
        game.PLATEAU[game.HAUTEUR/2-1][game.LARGEUR/2-1] = BLANC;        
        game.PLATEAU[game.HAUTEUR/2][game.LARGEUR/2-1] = NOIR;
        game.PLATEAU[game.HAUTEUR/2-1][game.LARGEUR/2] = NOIR;
        game.PLATEAU[game.HAUTEUR/2][game.LARGEUR/2] = BLANC;
    }



    

    /**
     *  Le tour du Blanc
     */
    public void CoupBlanc() {

        // on vérifie d'abord si le blanc pour faire un move encore
        white_done = true;
        for (int i=1; i<game.HAUTEUR-1; i++)
            for (int j=1; j<game.LARGEUR-1; j++)
                if (game.legal(i,j,BLANC,false) )
                    white_done=false;
        
        //bouger le blanc            
        game = white.strategie(game, white_done, BLANC);
    }

   /**
    *  Dessinez le plateau et l’état actuel du jeu. 
    *
    *  @param    g    le contexte graphique du jeu
    */
   public void paintComponent(Graphics g) {
      
       super.paintComponent(g);  // Panneau De remplissage avec couleur d’arrière-plan
       
       int width = getWidth();
       int height = getHeight();
       int xoff = width/(game.LARGEUR-2);
       int yoff = height/(game.HAUTEUR-2);

       int bCount=0;                     
       int wCount=0;                        

       // dessiner lines noires qui séparent les cases
       g.setColor(Color.BLACK);
       for (int i=1; i<=game.HAUTEUR-2; i++) {        
           g.drawLine(i*xoff, 0, i*xoff, height);
           g.drawLine(0, i*yoff, width, i*yoff);
       }

       // dessiner les discs noirs et blancs et les petits point qui indique qu'on peut bouger vers telle case
       for (int i=1; i<game.HAUTEUR-1; i++) {        
           for (int j=1; j<game.LARGEUR-1; j++) {
               // dessiner les discs noirs et blancs
               if (game.PLATEAU[i][j] == NOIR) {       
                   g.setColor(Color.BLACK);
                   g.fillOval((j*yoff)-yoff+7,(i*xoff)-xoff+7,50,50); 
                   bCount++;
               }
               else if (game.PLATEAU[i][j] == BLANC) {  
                   g.setColor(Color.WHITE);
                   g.fillOval((j*yoff)-yoff+7,(i*xoff)-xoff+7,50,50);
                   wCount++;
               }
               // dessiner les petits points qui indique qu'on peut bouger vers telle case
               if (tour == NOIR && game.legal(i,j,NOIR,false)) {
                   g.setColor(Color.BLACK);
                   g.fillOval((j*yoff+29)-yoff,(i*xoff+29)-xoff,6,6);
               }
               // Si un autre joueur ne peut pas bouger, le joueur actuel nettoie
               if (tour == BLANC && game.legal(i,j,BLANC,false)) {
                   g.setColor(Color.WHITE);
                   g.fillOval((j*yoff+29)-yoff,(i*xoff+29)-xoff,6,6);
               }
           }
       }
 
       // on vérifie si l'un des deux peut bouger encore 
       boolean done = true;
       for (int i=1; i<game.HAUTEUR-1; i++)
           for (int j=1; j<game.LARGEUR-1; j++)
               if ((game.legal(i,j,NOIR,false)) ||
                   (game.legal(i,j,BLANC,false)))
                   done=false;

                   //afficher le resultat avec le rouge selon les cas possible
       g.setColor(Color.RED);
       if (done) {
           if (wCount > bCount)
               g.drawString("Blanc a gagne avec " + wCount + " discs.",10,20);
           else if (bCount > wCount)
               g.drawString("Noir a gagne avec  " + bCount + " discs.",10,20);
           else g.drawString("egalite",10,20);
       }
       else {     
           if (wCount > bCount)
               g.drawString("Blanc est entrain de gagner avec " + wCount+" discs",10,20);
           else if (bCount > wCount)
               g.drawString("Noir est entrain de gagner avec " + bCount+" discs",10,20);
           else g.drawString("Actuellement a egalite",10,20);
       }
      
   }

    /**
     * Le programme principale.
     *
     */
    public static void main(String[] args) {
		
        Othello contenu;

        
           
            contenu = new Othello();
            
                JFrame window = new JFrame("Othello Game");
                window.setContentPane(contenu);
                window.setSize(530,557);
                window.setLocation(100,100);
                window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
                window.setVisible(true);
        
    }
}  
