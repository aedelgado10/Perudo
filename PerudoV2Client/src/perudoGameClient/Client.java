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
			this.afficherMenuClientConnecte();
			choix = reader.nextInt();
		}while(choix > 3 || choix < 1);
		//reader.close();
		return choix;
	}
	
	//Menu affiché a l'ouverture du Client
	public int menuPrincipal(){
		Scanner reader = new Scanner(System.in);
		int choix;
		do{
			this.afficherMenuClientNonConnecte();
			choix = reader.nextInt();
		}while(choix > 2 || choix < 1);
		//reader.close();
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
	public void traiter(String recu, ConnexionClient cx){
		ArrayList<String> repDecompose;
		System.out.println("recu: " + recu);
		repDecompose = this.getGPC().decomposer(recu);
		System.out.println("rep: " + repDecompose.get(0) + " ");
		if(this.getGPC().isPositive(repDecompose.get(0))){
			this.setLastPduSent("");
			this.gererJeu(repDecompose,cx);
		}
		else{
			switch(this.getLastPduSent()){
				case PDU.CREATE_PARTY:
					System.out.println("Impossible de creer la partie.");
					this.traiterMenuChoix(cx, true);
					break;
				default:
					break;
			}
			this.setLastPduSent("");
		}
	}
	
	//Si la reponse du serveur est positive, on traite la suite
	public void gererJeu(ArrayList<String> rep, ConnexionClient cx){
		//System.out.println("YEAH partie crée .l.");
		//System.out.println("ID partie: " + rep.get(1));
		this.envoyer(cx, PDU.LAUNCH);
		System.out.println("vaina: " + rep.get(0));
	}
	
	//Méthode qui est lancée par le main si l'utilisateur choisit créer partie
	public void creerPartie(ConnexionClient cx){
		String ipdu = this.getGPC().createParty();
		this.envoyer(cx,ipdu);
	}
	
	//traitement de la selection du menu choix
	public void traiterMenuChoix(ConnexionClient cx, boolean b){
		int choix;
		
		while(b){
			choix = this.menuChoix();
			switch(choix){
			case 1:
				this.creerPartie(cx);
				break;
			case 2:
				//client.rejoindrePartie();
				break;
			default:
				b = false;
				break;
			}
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
				client.traiterMenuChoix(cx, true);
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
