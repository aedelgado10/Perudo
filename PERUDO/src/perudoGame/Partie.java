package perudoGame;

import java.util.ArrayList;
import java.util.Random;

/**
 * 
 * @author Pereira
 * @version 1
 *
 */
public class Partie {
	
	private int id;
	private String status;
	private String AdressePartie; /*c pour le p2p*/
	private String nom;
	private ArrayList<Joueur> listeJoueurs;
	private Joueur joueurCourant;
	
	public Partie(int id, String nom, String stat){
		this.id= id;
		this.nom = nom;
		this.status = stat;
		this.listeJoueurs = new ArrayList<>();
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
	
	//Choisir Joueur qui commence (aleatoire)
	public void premierAJouer(){
		int j;
		if( listeJoueurs.size() > 1 ){
			Random r = new Random();
			j = r.nextInt(listeJoueurs.size());
			this.joueurCourant = listeJoueurs.get(j);
		}
	}
	
	//ajouter joueur 1 par 1
	public void ajouterJoueur(Joueur j){
		if (this.listeJoueurs.size() < 6)
			this.listeJoueurs.add(j);
	}
	
	//Demander la liste des joueurs au Gestionneur de Protocole
	/*public ArrayList<Joueur> demanderListeJoueurs(GestionProtocole gP){
		return gP.getListeJoueur();
	}*/
	
	//connaitre la valeur surencherie précedement
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
	//(Demander � l'�quipe)
	public void secouerGobelets(Joueur j){
		j.secouerGobelet();
	}
	
	//je fais la m�thode au cas o�
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
	
	//connaitre la valeur des d�s du joueur courant
	public ArrayList<DePerudo> getValeurDesDeJoueurCourant(){
		return this.joueurCourant.connaitreValeurDes();
	}
	
	// Methode qui servira a devoiler les d�s de tous
	// Priv� car elle ne peut �tre utilis�e que si on veut devoiler tous les d�s.
	private ArrayList<DePerudo> getValeurDesDeJoueur(Joueur j){
		return j.connaitreValeurDes();
	}
	
	//devoiler les d�s
	public ArrayList<ArrayList<DePerudo>> souleverGobelets(){
		ArrayList<ArrayList<DePerudo>> a = new ArrayList<>();
		for (Joueur j : listeJoueurs){
			a.add(this.getValeurDesDeJoueur(j));
		}
		return a;
	}
	
	//Connaitre combien de d�s sont en jeu
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
	
	//savir combien de fois un d� est pr�sent
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
	
	//Retirer d� a J
	public void retirerDe(Joueur j){
		j.retirerDe();
	}
	
	//rendre d� a J
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

}
