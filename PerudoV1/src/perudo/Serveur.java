package perudo;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Serveur {
	
	private GestionProtocole gP;
	private ServerSocket socketService;
	private ArrayList<Socket> listeClients;
	private Partie p;
	
	public Serveur(GestionProtocole gP,ServerSocket s){
		this.gP = gP;
		this.socketService = s;
		this.listeClients = new ArrayList<Socket>();
		this.p = new Partie();
	}
	
	public GestionProtocole getGP(){
		return this.gP;
	}
	
	public ServerSocket getSocketService(){
		return this.socketService;
	}
	/**
	 * Indexe la socket d'un client connecté
	 * @param client
	 */
	public void indexerClient(Socket client){
		this.listeClients.add(client);
		//this.p.ajouterJoueur(client); // a deplacer ! Et a faire que lorsqu'on crée / rejoint une partie (idem pour supprimer)
	}
	
	public void afficherNombreClients(){
		System.out.println("Actuellement en service: " + this.listeClients.size() + " client(s)");
	}
	
	/**
	 * méthode qui traite une commande envoyée au serveur
	 * @return String produit de GestionProtocole
	 */
	public String traiter(String cmd,Socket client){
		System.out.println("[Serveur.traiter] Recu: " + cmd);
		String resultat = this.getGP().traiter(cmd,client,p);
		System.out.println("[Serveur.traiter] Réponse: "+ resultat);
		if (p.getStatus() == PDU.PARTYPLAYING){
			
		}else if (resultat.equals(GestionProtocole.BEGIN_PARTY)){
			//On débute la partie
			p.setEnCours();
			for (Socket client_i: this.getListeClients()){
				if (!client.equals(client_i)){
					ConnexionServeur.repondre(client_i,resultat);
				}
			}
		}
		
		return resultat;
	}
	
	public ArrayList<Socket> getListeClients(){
		return this.listeClients;
	}
	
	public void supprimerJoueur(Socket client){
		this.p.dissocierJoueur(client);
	}
	
	public Partie getPartie(){
		return this.p;
	}
	
	public static void main(String[] args) {
		ConnexionServeur cx;
		GestionProtocole gP;
		
		gP = new GestionProtocole();
		ServerSocket s = ConnexionServeur.ouvrir();
		Serveur serveur = new Serveur(gP,s);
		
		System.out.println("[Serveur PERUDO v1] Serveur monopartie multijoueurs!");
		while (s != null){
			cx = new ConnexionServeur(serveur);
			Thread t = new Thread(cx);
			t.start();
			serveur.afficherNombreClients();
		}
	}

}

