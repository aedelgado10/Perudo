package perudoGameClient;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * 
 * @author Pereira
 * @version 1
 *
 */
public class Partie {
	
	private int id;
	private String status;
	private String AdressePartie; /*c'est pour le p2p (v3)*/
	private String nom;
	private ArrayList<Joueur> listeJoueurs;
	private Joueur joueurCourant;
	private boolean partyLeader;
	
	public Partie(int id){
		this.id= id;
		this.listeJoueurs = new ArrayList<>();
	}
	
	public void setPartyLeader(boolean b){
		this.partyLeader = b;
	}
	
	public boolean getPartyLeader(){
		return this.partyLeader;
	}
	
	public int getIdPartie() {
		return id;
	}

	public void setIdPartie(int idPartie) {
		this.id = idPartie;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAdressePartie() {
		return AdressePartie;
	}

	public void setAdressePartie(String adressePartie) {
		AdressePartie = adressePartie;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public ArrayList<Joueur> getListeJoueurs() {
		return listeJoueurs;
	}

	public void setListeJoueurs(ArrayList<Joueur> listeJoueurs) {
		this.listeJoueurs = listeJoueurs;
	}
	
	public Joueur getJoueurCourrant() {
		return joueurCourant;
	}

	public void setJoueurCourrant(Joueur joueurCourrant) {
		this.joueurCourant = joueurCourrant;
	}
	
	//savoir si la partie est en cours
	public boolean isPartyRunning(){
		if(this.getStatus().equals(PDU.PARTYPLAYING)){
			return true;
		}
		else{
			return false;
		}
	}
	
	//Menu pour le Leader avant demarrage
	public void afficherMenuLeader(boolean estDemarre, boolean estPremier){
		if(!estDemarre){
			System.out.println("Vous êtes les Leader, la partie n'a pas demarré vous pouvez:");
			System.out.println("Taper 1: Lancer Partie");
			System.out.println("Taper 2: Voir Joueurs");
			System.out.println("Taper 3: Annuler Partie");
		}
		else if(estPremier){
			System.out.println("Vous êtes les Leader et le premier a jouer, la partie est en cours vous pouvez:");
			System.out.println("Taper 1: Voir Joueurs");
			System.out.println("Taper 2: Annuler Partie");
			System.out.println("Taper 3: Surencherir");
		}
		else{
			System.out.println("Vous êtes les Leader, la partie est en cours vous pouvez:");
			System.out.println("Taper 1: Voir Joueurs");
			System.out.println("Taper 2: Annuler Partie");
			System.out.println("Taper 3: Surencherir");
			System.out.println("Taper 4: Menteur");
			System.out.println("Taper 5: Tout Pile");
		}
	}
	
	
	public int menuChoixLeader(boolean estDemarre, boolean estPremier){
		
		Scanner scan = new Scanner(System.in);
		int choix = 0;
		if(!estDemarre || estPremier){
			do{
				this.afficherMenuLeader(estDemarre,estPremier);
				choix = scan.nextInt();
			}while(choix > 3 || choix < 1);
		}else{
			do{
				this.afficherMenuLeader(estDemarre,estPremier);
				choix = scan.nextInt();
			}while(choix > 5 || choix < 1);
		}
		
		return choix;
	}
	
	
	//ajouter joueur 1 par 1
	public void ajouterJoueur(Joueur j){
		if (this.listeJoueurs.size() < 6)
			this.listeJoueurs.add(j);
	}
	
	//traite la liste des joueurs reçu et stocke la liste
	public void traiterListeJoueurs(ArrayList<String> rep, Joueur j, GestionProtocoleClient gp){
		//if(this.listeJoueurs.size() > 1){
			ArrayList<String> mainParser = new ArrayList<>();
			ArrayList<String> parser = new ArrayList<>();
			Joueur j1;
			Couleur c;
			String pseudo;
			
			for( String infoJ : rep.subList(1, rep.size())){
				//if(infoJ != ""){
					mainParser = gp.parseLists(infoJ,";");
				//}
				
			}
			
			for( String parsed : mainParser ){
				//if(parsed != ""){
					parser = gp.parseLists(parsed,":");
					c = new Couleur(parser.get(2));
					j1 = new Joueur(Integer.parseInt(parser.get(0)), c , false);
					pseudo = parser.get(1);
					j1.setNom(pseudo);
					
					if( j.getId() != j1.getId() ){
						this.ajouterJoueur(j1);
					}
				//}
			}
		//}
	}
	
	//afficher liste des joueurs
	public void afficherListeJoueurs(){
		System.out.println("Voilà la liste des Joueurs présents dans la partie: ");
		for(Joueur j : this.listeJoueurs){
			System.out.println(j.getPseudo() + " (" + j.getCouleur().getCodeCouleur() + ")" );
		}
		System.out.println("");
	}
	
	public int nbrJoueursIn(){
		return this.listeJoueurs.size();
	}
	
	
	//METHODES POUR LA V3 CLIENT
	/*
	
	//Choisir Joueur qui commence (aleatoire)
	public void premierAJouer(){
		int j;
		if( listeJoueurs.size() > 1 ){
			Random r = new Random();
			j = r.nextInt(listeJoueurs.size());
			this.joueurCourant = listeJoueurs.get(j);
		}
	}
	
	
	//connaitre la valeur surencherie prÃ©cedement
	public ArrayList<Integer> derniereValeurJouee(){
		int i = this.listeJoueurs.indexOf(joueurCourant);
		int s = listeJoueurs.size() - 1 ;
		if( i == 0 )
			return this.listeJoueurs.get(s).dernieresValeursChoisies();
		else	
			return this.listeJoueurs.get(this.listeJoueurs.indexOf(this.joueurCourant)-1).dernieresValeursChoisies();
	}
	
	public Joueur connaitreJoueurPrecedant(){
		int i = this.listeJoueurs.indexOf(joueurCourant);
		int s = listeJoueurs.size() - 1 ;
		if (i == 0)
			return this.listeJoueurs.get(s);
		else
			return this.listeJoueurs.get(--i);
	}
	
	//passer le tour de annoncer au joueur suivant
	public void passerJoueurSuivant(){
		int courant;
		courant = this.listeJoueurs.indexOf(this.joueurCourant);
		if(courant == (this.listeJoueurs.size()-1))
			this.joueurCourant = this.listeJoueurs.get(0);
		else
			this.joueurCourant = this.listeJoueurs.get(courant + 1);
	}
	
	//Est ce que c'est plus judicieux, de faire secouerGobelets 1 par 1
	//Ou bien faire un for et secouer les gobelets de tout le monde?
	//(Demander à l'équipe)
	public void secouerGobelets(Joueur j){
		j.secouerGobelet();
	}
	
	//je fais la méthode au cas où
	public void secouerTousGobelets(){
		for (Joueur j : this.listeJoueurs){
			j.secouerGobelet();
		}
	}
	
	//Avoir le nom du joueur
	public String getNomJoueur(Joueur j){
		return j.getPseudo();
	}
	
	//Avoir la couleur d'un joueur
	public Couleur getCouleurJoueur(Joueur j){
		return j.getCouleur();
	}
	
	//Get pseudo et Couleur sous forme de string
	public String getPseudoCouleur(Joueur j){
		String s;
		s = j.getPseudo();
		s = "(" + j.getCouleur().getCodeCouleur() +") " + s;
		return s;
	}
	
	//Savoir le nombre de des d'un joueur
	public int nombreDesJoueur(Joueur j){
		return j.nombreDes();
	}
	
	//connaitre la valeur des dés du joueur courant
	public ArrayList<DePerudo> getValeurDesDeJoueurCourant(){
		return this.joueurCourant.connaitreValeurDes();
	}
	
	// Methode qui servira a devoiler les dés de tous
	// Privé car elle ne peut être utilisée que si on veut devoiler tous les dés.
	private ArrayList<DePerudo> getValeurDesDeJoueur(Joueur j){
		return j.connaitreValeurDes();
	}
	
	//devoiler les dés
	public ArrayList<ArrayList<DePerudo>> souleverGobelets(){
		ArrayList<ArrayList<DePerudo>> a = new ArrayList<>();
		for (Joueur j : listeJoueurs){
			a.add(this.getValeurDesDeJoueur(j));
		}
		return a;
	}
	
	//Connaitre combien de dés sont en jeu
	public int nombreTotalDeDes(){
		int cpt = 0;
		for(Joueur j : this.listeJoueurs){
			cpt = cpt + this.nombreDesJoueur(j);
		}
		return cpt;
	}
	
	//Le joueur courant surencherit et on passe le tour au joueur suivant
	public void surencher(int nbrDe, int valDe){
		if((nbrDe > 0 && nbrDe < this.nombreTotalDeDes()) && (valDe > 1 && valDe < 7)){
			if( this.verifSurencherir(nbrDe, valDe)){
				this.joueurCourant.majSurenchere(nbrDe, valDe);
				this.passerJoueurSuivant();
			}
		}	
	}
	
	//verifie que la saisie, du joueur courant est bien un surencherissement.
	private Boolean verifSurencherir(int nbrDe, int valDe){
		ArrayList<Integer> a = this.derniereValeurJouee();
		if( (nbrDe > a.get(0) && valDe >= a.get(1)) || (nbrDe >= a.get(0) && valDe > a.get(1)) )
			return true;
		else
			return false;
	}
	
	//savir combien de fois un dé est présent
	public int combienDeFois(String valeurDe){
		int cpt = 0;
		ArrayList<ArrayList<DePerudo>> a;
		a = this.souleverGobelets();
		for(ArrayList<DePerudo> b : a ){
			for (DePerudo d : b){
				if( (d.getPerudo().equals("Perudo")) || (d.getPerudo().equals(valeurDe)) )
					cpt++;
			}
		}
		return cpt;
	}
	
	//Retirer dé a J
	public void retirerDe(Joueur j){
		j.retirerDe();
	}
	
	//rendre dé a J
	public void recupererDe(Joueur j){
		j.recupererDe();
	}
	
	//retourne vrai ou faux a l'annonce tout pile
	public Boolean toutPile(){
		ArrayList<Integer> valeurs = this.derniereValeurJouee();
		int i = this.combienDeFois(valeurs.get(1).toString());
		if (i == valeurs.get(0))
			return true;
		else
			return false;
	}
	
	//retourne vrai si, le joueur precedant mentait, retourne faux si l'accuseur se trompe
	public Boolean menteur(){
		ArrayList<Integer> valeurs = this.derniereValeurJouee();
		int i = this.combienDeFois(valeurs.get(1).toString());
		if(i >= valeurs.get(0))
			return false;
		else
			return true;
			
	}
	
	*/
	
	

}
