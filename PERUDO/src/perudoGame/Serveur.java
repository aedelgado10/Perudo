package perudoGame;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;

public class Serveur {

	private Partie p;
	private ArrayList<Socket> listeClients;
	private Hashtable<Socket,Integer> listeJoueurs;
	private GestionProtocole gPServeur;
	private ConnexionServeur cxServeur;
	private ServerSocket socketService;
	private final int PORT = 27019;
	
	public Serveur() {
		p = null;
		gPServeur = new GestionProtocole();
		socketService = ConnexionServeur.ouvrir(PORT);
		this.cxServeur = new ConnexionServeur(socketService, gPServeur);
		listeClients = new ArrayList<Socket>();
	}

	public static void main(String[] args) {
		
		Serveur serveur = new Serveur();
		System.out.println("[Serveur v1] Monopartie stockée chez le serveur");
		
		while (serveur.getSocketService() != null){
			Thread t = new Thread(serveur.getCxServeur());
			t.start();
			serveur.getListeClients().add(serveur.getCxServeur().getSocket());
			System.out.println("Il y a " + serveur.getListeClients().size() + " clients");
		}
	}
	
	public Partie getPartie(){
		return this.p;
	}
	
	public ServerSocket getSocketService(){
		return this.socketService;
	}
	
	public GestionProtocole getGP(){
		return this.gPServeur;
	}
	
	public ConnexionServeur getCxServeur(){
		return this.cxServeur;
	}
	
	public ArrayList<Socket> getListeClients(){
		return this.listeClients;
	}
	
	public Hashtable<Socket,Integer> getListeJoueurs(){
		return this.listeJoueurs;
	}
}
