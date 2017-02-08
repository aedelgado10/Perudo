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
	
	//Recup�rer la gestion protocole client
	public GestionProtocoleClient getGPC(){
		return this.gPc;
	}
	
	//r�cuperer la derni�re demande effectu� aupr�s serveur
	public String getLastPduSent(){
		return this.lastPduSent;
	}
	
	//enregistrer la derni�re demande effectu� aupres du serveur
	public void setLastPduSent(String ipdu){
		this.lastPduSent = ipdu;
	}
	
	//Menu de choix d'action apr�s connexion au serveur
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
	
	//Menu affich� a l'ouverture du Client
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
	
	//Affichage Menu Connect�
	public void afficherMenuClientConnecte(){
		System.out.println("Vous pouvez soit:");
		System.out.println("Taper 1 : Cr�er Partie");
		System.out.println("Taper 2 : Rejoindre Partie");
		System.out.println("Taper 3 : Quitter serveur");
	}
	
	//Affichage Menu d'ouverture Client
	public void afficherMenuClientNonConnecte(){
		System.out.println("Vous pouvez soit:");
		System.out.println("Taper 1 : Se Connecter");
		System.out.println("Taper 2 : Quitter");
	}
	
	//m�thode de connexion client - serveur
	public void connecterServeur(ConnexionClient cx) throws IOException{
		cx.ConnecterServeur();
	}
	
	//Envoie la demande, stocke celle ci
	public void envoyer(ConnexionClient cx, String ipdu){
		cx.envoyer(ipdu);
		this.setLastPduSent(ipdu);
	}
	
	//traite la r�ponse et reinitialise la derni�re demande a vide
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
	
	//M�thode qui est lanc�e par le main si l'utilisateur choisit cr�er partie
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
