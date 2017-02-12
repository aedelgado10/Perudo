package perudo;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;

public class Partie {

	private int id;
	private String status;
	private Hashtable<Socket,Joueur> listeJoueurs;
	private Joueur joueurCourant;
	private ArrayList<Couleur> listeCouleursDispo = new ArrayList<>();
	/**
	 * @return the joueurCourant
	 */
	public Joueur getJoueurCourant() {
		return joueurCourant;
	}

	private Joueur createur = null;
	
	public Partie() {
		this.id = 0;
		this.status = "Pas de partie";
		this.listeJoueurs = new Hashtable<Socket,Joueur>(6);
		
		this.listeCouleursDispo.add(new Red());
		this.listeCouleursDispo.add(new Blue());
		this.listeCouleursDispo.add(new Green());
		this.listeCouleursDispo.add(new Orange());
		this.listeCouleursDispo.add(new Purple());
		this.listeCouleursDispo.add(new Yellow());
	}
	
	/**
	 * v1 : on a un seul id
	 */
	public void creerPartie(){
		this.id = 1;
		this.setStatus(PDU.WAITING);
	}
	
	public void setCreateur(Joueur j){
		this.createur = j;
	}
	
	/**
	 * 
	 * @param j
	 * @return
	 */
	public boolean isCreateur(Joueur j){
		return (j.equals(this.getCreateur()));
	}
	
	public Joueur getCreateur(){
		return this.createur;
	}
	
	public String getStatus(){
		return this.status;
	}
	
	public void setStatus(String status){
		this.status = status;
	}
	
	public int getID(){
		return this.id;
	}
	
	public void associerJoueur(Socket client){ 
		int idJoueur = this.getListeJoueurs().size() + 1;
		this.listeJoueurs.put(client, new Joueur(idJoueur));
	}
	
	public boolean isDejaDansPartie(Socket client){
		return this.getListeJoueurs().containsKey(client);
	}
	
	/**
	 * 
	 * @param client
	 * @return
	 */
	public String rejoindrePartie(Socket client){
		
		if (this.isPleine()){
			return PDU.FULL;
		}else{
			if (!isDejaDansPartie(client)){
				this.ajouterJoueur(client);
				return PDU.JOIN_OK;
			}else{
				return PDU.ALREADY;
			}
		}
	}
	
	public boolean isFriendlyParty(){
		
		return (this.getNbJoueurs() < 2) ? false : true;
	}
	
	/**
	 * 
	 * @return le nombre de joueurs dans la partie en cours
	 */
	public int getNbJoueurs(){
		return getListeJoueurs().size();
	}
	
	/**
	 * détermine si la partie est pleine
	 * @return
	 */
	public boolean isPleine(){
		return (this.listeJoueurs.size() == 6);
	}
	
	public boolean isEnCours(){
		return (this.getStatus().equals(PDU.PARTYPLAYING));
	}
	
	public void setEnCours(){
		this.setStatus(PDU.PARTYPLAYING);
		this.demarrerPartie();
	}
	
	public void secouerTousGobelets(){
		Enumeration<Joueur> e = this.listeJoueurs.elements();
		
		while (e.hasMoreElements()){
			e.nextElement().secouerGobelet();
		}
	}
	
	private void demarrerPartie(){
		this.premierAJouer();
	}
	
	public Hashtable<Socket, Joueur> getListeJoueurs(){
		return this.listeJoueurs;
	}
	
	/**
	 * partieExists : pour la v1: on ne stocke qu'une partie chez le serveur
	 * @return false si id de la partie = 0 (non créée), true sinon
	 * 
	 */
	public boolean partieExists(){
		return (this.getID() != 0);
	}
	
	public Joueur ajouterJoueur(Socket client){
		this.associerJoueur(client);
		return this.getListeJoueurs().get(client);
	}
	
	public void dissocierJoueur(Socket client){
		this.getListeJoueurs().remove(client);
	}
	
	public Joueur getJoueur(Socket client){
		return this.getListeJoueurs().get(client);
	}
	
	//Choisir Joueur qui commence (aleatoire)
	public void premierAJouer(){
		ArrayList<Joueur> listeJoueurs = new ArrayList<Joueur>();
		Enumeration<Joueur> e = this.listeJoueurs.elements();
		
		while (e.hasMoreElements()){
			listeJoueurs.add(e.nextElement());
		}
		
		int j;
		if( listeJoueurs.size() > 1 ){
			Random r = new Random();
			j = r.nextInt(listeJoueurs.size());
			this.joueurCourant = listeJoueurs.get(j);
		}
	}
	
	public Couleur genererCouleurPourJoueur(){
		Random r = new Random();
		int i  = r.nextInt(6);
		
		Couleur c = this.listeCouleursDispo.remove(i);
		
		return c;
	}
	
	/**
	 * génération de couleur du client random
	 * @param j
	 * @return
	 */
	public String setCouleur(Joueur j){
		
		Couleur c = this.genererCouleurPourJoueur();
		j.setCouleur(c);
		return c.getCodeCouleur();
	}
	
	public boolean hasColor(Joueur j){
		return j.hasColor();
	}
}
