import java.util.ArrayList;

public class Partie {

	private int idPartie;
	private String status;
	private boolean isLeader;
	private boolean isBeginning;
	private ArrayList<Joueur> listeJoueurs;
	private int dernierDeAnnonce;
	private int dernierNbrAnnonce;
	private Joueur dernierJoue;
	
	public Partie() {
		this.idPartie = 0;
		this.status = PDU.NOPARTIES;
		this.listeJoueurs = new ArrayList<>();
		this.isLeader = true;
		this.isBeginning = true;
	}
	
	public Partie(int id, String stat) {
		this.idPartie = id;
		this.status = stat;
	}
	
	/*GETTERS*/
	public String getStatus(){
		return this.status;
	}
	
	public int getId(){
		return this.idPartie;
	}
	
	public void setStatus(String stat){
		this.status = stat;
	}
	
	public void setIdParty(int id){
		this.idPartie = id;
	}
	
	public void setIsLeader(boolean b){
		this.isLeader = b;
	}
	
	public boolean getIsLeader(){
		return this.isLeader;
	}
	
	public ArrayList<Joueur> getListeJoueurs(){
		return this.listeJoueurs;
	}
	
	public void setIsBeginning(boolean b){
		this.isBeginning = b;
	}
	
	public boolean getIsBeginning(){
		return this.isBeginning;
	}
	
	public int getDernierDeAnnonce(){
		return this.dernierDeAnnonce;
	}
	
	public int getDernierNbrAnnonce(){
		return this.dernierNbrAnnonce;
	}
	
	public void setDernierNbrAnnonce(int b){
		this.dernierNbrAnnonce = b;
	}
	
	public void setDernierValAnnonce(int b){
		this.dernierDeAnnonce = b;
	}
	
	public void setDernierJoue(Joueur j){
		this.dernierJoue = j;
	}
	
	public Joueur getDernierJoueur(){
		return this.dernierJoue;
	}
	/***************************************************************/
	
	/* MENUS DANS PARTIE */
	public void afficherMenuLeaderNonDemarre(){
		System.out.println("Vous êtes le leader, la partie n'a pas commencé:");
		System.out.println("Tapper 1: Lancer Partie");
		System.out.println("Tapper 2: Voir Liste Joueurs");
		System.out.println("Tapper 3: Annuler Partie");
		System.out.println("");
	}
	
	public void afficherMenuLeaderDemarre(){
		System.out.println("Vous êtes le leader, à votre tour:");
		System.out.println("Tapper 1: Surencherir");
		System.out.println("Tapper 2: Menteur");
		System.out.println("Tapper 3: Tout Pile");
		System.out.println("Tapper 4: Voir Dés");
		System.out.println("Tapper 5: Voir Joueurs");
		System.out.println("Tapper 6: Annuler Partie");
		System.out.println("");
	}
	
	public void afficherMenuLeaderPremier(){
		System.out.println("Vous êtes le leader et le premier à jouer:");
		System.out.println("Tapper 1: Surencherir");
		System.out.println("Tapper 2: Voir Dés");
		System.out.println("Tapper 3: Voir Joueurs");
		System.out.println("Tapper 4: Annuler Partie");
		System.out.println("");
	}
	
	public void afficherMenuJoueurPremier(){
		System.out.println("Vous êtes le premier, C'est votre tour:");
		System.out.println("Tapper 1: Surencherir");
		System.out.println("Tapper 2: Voir Dés");
		System.out.println("Tapper 3: Voir Joueurs");
		System.out.println("Tapper 4: Quitter");
		System.out.println("");
	}
	
	public void afficherMenuJoueurJeu(){
		System.out.println("C'est votre tour:");
		System.out.println("Tapper 1: Surencherir");
		System.out.println("Tapper 2: Menteur");
		System.out.println("Tapper 3: Tout Pile");
		System.out.println("Tapper 4: Voir Dés");
		System.out.println("Tapper 5: Voir Joueurs");
		System.out.println("Tapper 6: Quitter");
		System.out.println("");
	}
	
	/*****************************************************************/
	
	
	/* AFFICHAGES */
	public void afficherListeJoueurs(){
		System.out.println("Joueurs présents dans la partie: ");
		for(Joueur j : this.listeJoueurs){
			System.out.println(j.getNomJoueur() + " (" + j.getCodeCouleurJoueur() + ")" );
		}
		System.out.println("");
	}
	
	public void afficherNomCouleurDernierJoue(){
		System.out.println("Joueur: " + this.dernierJoue.getNomJoueur() + " (" + this.dernierJoue.getCodeCouleurJoueur() +")");
	} 
	
	/*********************************************************************/
	
	/* TRAITEMENTS Liste Joueurs */
	public void ajouterJoueur(Joueur j){
		if (this.listeJoueurs.size() < 6)
			this.listeJoueurs.add(j);
	}
	
	public void restartListe(){
		this.listeJoueurs = new ArrayList<>();
	}
	
	public void traiterListeJoueurs(ArrayList<String> rep, Client cli){
			ArrayList<String> mainParser = new ArrayList<>();
			ArrayList<String> parser = new ArrayList<>();
			Joueur j1;
			Couleur c;
			String pseudo;
			
			for( String infoJ : rep.subList(1, rep.size())){
					mainParser = cli.getGPC().parseLists(infoJ,";");
			}
			
			this.listeJoueurs = new ArrayList<>();
			
			for( String parsed : mainParser ){
					parser = cli.getGPC().parseLists(parsed,":");
					c = new Couleur(parser.get(2));
					j1 = new Joueur(Integer.parseInt(parser.get(0)), c);
					pseudo = parser.get(1);
					j1.setNomJoueur(pseudo);
					
					if( cli.getJoueur().getCodeCouleurJoueur() != j1.getCodeCouleurJoueur() ){
						this.ajouterJoueur(j1);
					}
					else{
						this.ajouterJoueur(cli.getJoueur());
					}
			}
	}
	/**********************************************************************/
	
	/*Traiter Dés*/
	public void traiterValeursAnnonces(Client c, String rep){
		ArrayList<String> parser = new ArrayList<>();
		
		parser = c.getGPC().parseLists(rep," ");
		
		for(Joueur j : this.listeJoueurs){
			if(j.getIdJoueur() == Integer.parseInt(parser.get(1))){
				this.setDernierJoue(j);
			}
		}
		this.setDernierNbrAnnonce(Integer.parseInt(parser.get(2)));
		this.setDernierValAnnonce(Integer.parseInt(parser.get(3)));
		
	}
	/**********************************************************************/
	
	/*TRAITER TT PILE*/
	public void traiterToutPile(Client c, String rep, boolean ttpile){
	   
	   ArrayList<String> parser = new ArrayList<>();
	   ArrayList<String> parser2 = new ArrayList<>();
	   
	   parser = c.getGPC().parseLists(rep," ");
	   for(String s : parser.subList(1, parser.size())){
		   parser2 = c.getGPC().parseLists(s,":");
		   System.out.println("Dés de: " + parser2.get(0));
		   for(String s2 : parser2.subList(1, parser2.size())){
			   System.out.println(s2);
		   }
		   System.out.println("");
	   }
	   
	   if( ttpile && c.getJoueur().getNbrDes() < 5){
		   System.out.println("Vous recuperez un dé");
		   c.getJoueur().recupererDe();
	   }
	   else if(ttpile){
		   System.out.println("Vous avez le nombre maximal de dés");
	   }
	   
	   if( !ttpile && c.getJoueur().getNbrDes() > 1){
		   System.out.println("Vous perdez un dé");
		   c.getJoueur().enleverDe();
	   }
	   else if(c.getJoueur().getNbrDes() == 0){
		   System.out.println("Vous êtes eliminé! (plus de dés)");
	   }
	}
	
	
	public void traiterLiar(Client c, String rep, boolean liar){
		   
		   ArrayList<String> parser = new ArrayList<>();
		   ArrayList<String> parser2 = new ArrayList<>();
		   
		   parser = c.getGPC().parseLists(rep," ");
		   for(String s : parser.subList(1, parser.size())){
			   parser2 = c.getGPC().parseLists(s,":");
			   System.out.println("Dés de: " + parser2.get(0));
			   for(String s2 : parser2.subList(1, parser2.size())){
				   System.out.println(s2);
			   }
			   System.out.println("");
		   }
		   
		   /*if( liar && c.getJoueur().getNbrDes() < 5){
			   System.out.println("Vous recuperez un dé");
			   c.getJoueur().recupererDe();
		   }
		   else if(liar){
			   System.out.println("Vous avez le nombre maximal de dés");
		   }*/
		   
		   if( !liar && c.getJoueur().getNbrDes() > 1){
			   System.out.println("Vous perdez un dé");
			   c.getJoueur().enleverDe();
		   }
		   else if(c.getJoueur().getNbrDes() == 0){
			   System.out.println("Vous êtes eliminé! (plus de dés)");
		   }
		}
	/**********************************************************************/
}
