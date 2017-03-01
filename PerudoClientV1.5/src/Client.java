import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Pereira
 * @version 1
 */

public class Client {

	private GestionProtocoleClient gPc;
	private Partie p;
	private Joueur j;
	private Scanner scan; // pour g�rer inputs
	
	public Client() {
		this.gPc = new GestionProtocoleClient();
		this.p = new Partie();
		this.j = new Joueur();
	}
	
	/* GETTERS */
	public Partie getPartie(){
		return this.p;
	}
	
	public Joueur getJoueur(){
		return this.j;
	}
	
	public GestionProtocoleClient getGPC(){
		return this.gPc;
	}
	/*********************************************************************/
	
	
	
	
	/* CONNEXION CLIENT - ENVOIS*/
	
	//m�thode de connexion client - serveur
	public void connecterServeur(ConnexionClient cx) throws IOException{
		cx.ConnecterServeur();
	}
	
	//Envoie la demande
	public void envoyer(ConnexionClient cx, String ipdu){
		cx.envoyer(ipdu);
	}
	/************************************************************************/
	
	
	
	
	/* TRAITER Receptions*/
	
	/*Lien entre le thread de reception et le client*/
	public String traiter(String recu, ConnexionClient cx){
		System.out.println("MainClient.Debugger] Recu: " + recu);  //Debuger
		String resultat = this.getGPC().traiter(recu, this, cx);
		System.out.println("[MainClient.Debugger] resultat: " + resultat); // Debugger  
		return resultat;         // debbuger
	}
	/***********************************************************************/
	
	/* REINITIALISER PARAMETRES CLIENT */
	public void restartClient(){
		this.p = new Partie();
		this.j = new Joueur();
	}
	
	/* MENUS */
	// Menu avant d'ouvrir socket
	public void afficherMenuClientOuvert(){
		System.out.println("Vous avez les choix suivants:");
		System.out.println("Tapper 1: Connecter au serveur de jeu");
		System.out.println("Tapper 2: Quitter");
		System.out.println("");
		
	}
	
	// Premier menu, apres ouverture de Socket
	public void afficherMenuClientConnecte(){
		System.out.println("Vous �tes maintenant connect�s au serveur de jeu:");
		System.out.println("Tapper 1: Creer Partie");
		System.out.println("Tapper 2: Lister Parties");
		System.out.println("Tapper 3: Quitter");
		System.out.println("");
	}
	
	public void traiterMenuBienvenue(ConnexionClient cx){
		int choix;
		choix = this.choixMenuClient(2,3);
		switch(choix){
		case 1:
			this.envoyer(cx, this.getGPC().createParty());
			break;
		case 2:
			this.envoyer(cx, this.getGPC().listRooms());
			break;
		default:
			this.envoyer(cx, this.getGPC().quitter());
			System.out.println("****************************");
			System.out.println("*[Fermeture Client Perudo!]*");
			System.out.println("****************************\n");
			cx.FermerConnexionServeur();
			break;
		}
	}
	
	// Affichage des Parties apr�s selection de l'option 2 dans le menu
	public void afficherPartiesDisponibles(ArrayList<String> s){
		System.out.println("Liste de Parties: ");
		ArrayList<String> parser = new ArrayList<>();
		int i = 1 ;
		//sublist 1 car le 0 est la PDU
		for( String partie : s.subList(1, s.size()) ){
			parser = this.gPc.parseLists(partie,":");
			System.out.println("Tapper " + i +" pour rejoindre la Partie: " + parser.get(0)
			+ " avec status: " + parser.get(1) + " ("+parser.get(2)+")");
			i++;
		}
		System.out.println("Tapper " + s.size() + " pour ne pas rejoindre");
	}
	
	//Methode qui sert a traiter tous les menus en fonction de l'input
	public int choixMenuClient(int menu, int possibilites){
		int choix;
		scan = new Scanner(System.in);
		do{
		  switch(menu){
		   case 1:
			  this.afficherMenuClientOuvert();
			  break;
		   case 2:
			  this.afficherMenuClientConnecte();
			  break;
		   case 3:
			   this.getPartie().afficherMenuLeaderNonDemarre();
			   break;
		   case 4:
			   this.getPartie().afficherMenuLeaderDemarre();
			   break;
		   case 5:
			   this.getPartie().afficherMenuLeaderPremier();
			   break;
		   case 6:
			   this.getPartie().afficherMenuJoueurJeu();
			   break;
		   case 7:
			   this.getPartie().afficherMenuJoueurPremier();
			   break;
		   default:
			  break;
		  }
		  choix = scan.nextInt();
		}while(choix > possibilites || choix < 1);	
		return choix;
	}
	
	// pour saisir le pseudo
	public String choixTexte(){
	    scan = new Scanner(System.in);
		String pseudo = "";
		pseudo = scan.next();
		return pseudo;
	}
	/******************************************************************/
	
	
	/**
	 * Main du Code partie Client
	 */
	public static void main(String[] args) {
		Client client = new Client();
		ConnexionClient cx = new ConnexionClient(client);
		Thread t = new Thread(cx);
		
		System.out.println("****************************");
		System.out.println("* Client Perudo Initialis� *");
		System.out.println("****************************\n");
		int choix = client.choixMenuClient(1,2);
		if(choix == 1){
			try{
				client.connecterServeur(cx);
				t.start();
				client.traiterMenuBienvenue(cx);
				/*while(cx.getSocket() != null);  client toujours Ouvert?*/
			}catch(IOException e){
				cx.FermerConnexionServeur();
				System.err.println("Erreur : " + e);
				//e.printStackTrace();
			}
		}
		else{
			System.out.println("****************************");
			System.out.println("* Fermeture Client Perudo! *");
			System.out.println("****************************\n");
			System.out.println("****************************");
			System.out.println("*Provided by:              *\n* Andr�s E. DELGADO ANDRADE* \n* Nady KERAGHEL            * \n* William PEREIRA DO CARMO *");
			System.out.println("*                          *\n* M1 STRI 2017�            *");
			System.out.println("****************************\n");
		}
	}

}
