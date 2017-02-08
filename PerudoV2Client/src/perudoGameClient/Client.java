package perudoGameClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * 
 * @author Pereira
 * @version 1
 *
 */
public class Client {
	
	private GestionProtocoleClient gPc;
	private String lastPduSent;
	
	//Constructeur Client
	public Client(){
		this.gPc = new GestionProtocoleClient();
		this.lastPduSent = "";
	}
	
	//Recupérer la gestion protocole client
	public GestionProtocoleClient getGPC(){
		return this.gPc;
	}
	
	//récuperer la dernière demande effectué auprès serveur
	public String getLastPduSent(){
		return this.lastPduSent;
	}
	
	//enregistrer la dernière demande effectué aupres du serveur
	public void setLastPduSent(String ipdu){
		this.lastPduSent = ipdu;
	}
	
	//Menu de choix d'action après connexion au serveur
	public int menuChoix(){
		Scanner reader = new Scanner(System.in);
		int choix;
		do{
			choix = reader.nextInt();
			this.afficherMenuClientConnecte();
			reader.close();
		}while(choix > 3 || choix < 1);
		return choix;
	}
	
	//Menu affiché a l'ouverture du Client
	public int menuPrincipal(){
		Scanner reader = new Scanner(System.in);
		int choix;
		do{
			choix = reader.nextInt();
			this.afficherMenuClientNonConnecte();
			reader.close();
		}while(choix > 2 || choix < 1);
		return choix;
	}
	
	//Affichage Menu Connecté
	public void afficherMenuClientConnecte(){
		System.out.println("Vous pouvez soit:");
		System.out.println("Taper 1 : Créer Partie");
		System.out.println("Taper 2 : Rejoindre Partie");
		System.out.println("Taper 3 : Quitter serveur");
	}
	
	//Affichage Menu d'ouverture Client
	public void afficherMenuClientNonConnecte(){
		System.out.println("Vous pouvez soit:");
		System.out.println("Taper 1 : Se Connecter");
		System.out.println("Taper 2 : Quitter");
	}
	
	//méthode de connexion client - serveur
	public void connecterServeur(ConnexionClient cx) throws IOException{
		cx.ConnecterServeur();
	}
	
	//Envoie la demande, stocke celle ci
	public void envoyer(ConnexionClient cx, String ipdu){
		cx.envoyer(ipdu);
		this.setLastPduSent(ipdu);
	}
	
	//traite la réponse et reinitialise la dernière demande a vide
	public void traiter(String recu){
		ArrayList<String> repDecompose = new ArrayList<String>();
		repDecompose = this.getGPC().decomposer(recu);
		if(this.getGPC().isPositive(this.getLastPduSent())){
			this.setLastPduSent("");
			this.gererJeu(repDecompose);
		}
		else{
			switch(this.getLastPduSent()){
				case PDU.CREATE_PARTY:
					System.out.println("Impossible de creer la partie.");
					this.traiterMenuChoix();
					break;
				default:
					break;
			}
			this.setLastPduSent("");
		}
	}
	
	//Si la reponse du serveur est positive, on traite la suite
	public void gererJeu(ArrayList<String> rep){
		
	}
	
	//Méthode qui est lancée par le main si l'utilisateur choisit créer partie
	public void creerPartie(ConnexionClient cx){
		String ipdu = this.getGPC().createParty();
		this.envoyer(cx,ipdu);
	}
	
	//traitement de la selection du menu choix
	public void traiterMenuChoix(){
		int choix = this.menuChoix();
		
		switch(choix){
		case 1:
			//client.creerPartie();
			break;
		case 2:
			//client.rejoindrePartie();
			break;
		default:
			break;
		}
	}
	
	// Debut du client
	public static void main (String args[]) {
		
		
		Client client = new Client();
		ConnexionClient cx = new ConnexionClient(client);
		Thread t = new Thread(cx);
		
		
		System.out.println("Client Perudo Ouvert!");
		int choix;
		choix = client.menuPrincipal();
		if(choix == 1){
			try{
				client.connecterServeur(cx);
				t.start();
				client.traiterMenuChoix();
				//RajouterFonctionFermer Connexion
				cx.FermerConnexionServeur();
			}catch(IOException e){
				System.err.println("Erreur : " + e);
				//e.printStackTrace();
			}
		}
		else{
			System.out.println("Fermeture Client Perudo!");
		}
	}
}
