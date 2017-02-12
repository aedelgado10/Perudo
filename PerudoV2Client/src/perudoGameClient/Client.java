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
	private Joueur j1;
	private Partie p1;
	private Couleur c1;
	
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
				case PDU.WHICH_COLOR:
					//cas improbable
					System.out.println("Il n'y a plus de couleurs disponibles, jeté?");
					this.traiterMenuChoix(cx, true);
					break;
				case PDU.COLOR:
					System.out.println("Cette couleur n'est plus disponible!");
					this.demanderListeCouleur(cx);
					break;
				case PDU.GET_ID:
					System.out.println("Pas d'ID attribué, jeté?");
					this.traiterMenuChoix(cx, true);
					break;
				case PDU.PSEUDO_KO:
					System.out.println("Ce pseudo n'est pas disponible!");
					this.j1.setNom(this.saisirPseudo());
					this.demanderPseudo(this.j1.getPseudo(), cx);
					break;
				default:
					break;
			}
			this.setLastPduSent("");
		}
	}
	
	//Si la reponse du serveur est positive, on traite la suite
	public void gererJeu(ArrayList<String> rep, ConnexionClient cx){
		
		switch(rep.get(0)){
		  case PDU.OKPARTY:
			  this.p1 = new Partie(Integer.parseInt(rep.get(1)));
			  p1.setStatus("WAITING_PLAYERS");
			  p1.setPartyLeader(true);
			  System.out.println("Vous venez de créer une Partie.");
			  this.demanderListeCouleur(cx);
			  break;
		  case PDU.COLORLIST:
			  this.c1 = new Couleur(rep);
			  this.c1 = new Couleur(c1.getCouleurChoisie(this.choixMenuCouleur(c1)));
			  this.demanderCouleur(c1.getCodeCouleur(), cx);
			  break;
		  case PDU.COLOR_OK:
			  System.out.println("Votre couleurs est: " + c1.getCodeCouleur());
			  this.demandeIDJoueur(cx);
			  break;
		  case PDU.ID:
			  this.j1 = new Joueur(Integer.parseInt(rep.get(1)),this.c1);
			  this.j1.setNom(this.saisirPseudo());
			  this.demanderPseudo(this.j1.getPseudo(), cx);
			  break;
		  case PDU.PSEUDO_OK:
			  System.out.println("Votre pseudo est validé: " + this.j1.getPseudo());
			  this.p1.ajouterJoueur(this.j1);
			  if (p1.getPartyLeader()){
				  // Vous êtes le leader, voulez vous lancer?
			  }
			  else{
				  System.out.println("Le Leader va bientôt lancer la partie, patienter...");
			  }
			  break;
		}
	}
	
	
	//demande d'envoi de pseudo
	public void demanderPseudo(String s, ConnexionClient cx){
		String ipdu = this.getGPC().pseudoP(s);
		this.envoyer(cx,ipdu);
	}
	
	//Demande a l'utilisateur de rentrer un pseudo
	public String saisirPseudo(){
		Scanner reader = new Scanner(System.in);
		String choix;
		System.out.println("Saisir votre pseudo:");
		choix = reader.nextLine();
		//reader.close();
		return choix;
	}
	
	//demande d'identifiant de joueur
	public void demandeIDJoueur(ConnexionClient cx){
		String ipdu = this.getGPC().getId();
		this.envoyer(cx,ipdu);
	}
	
	//saisie client du couleur qui désire parmi les disponibilités
	public int choixMenuCouleur(Couleur c){
		Scanner reader = new Scanner(System.in);
		int choix;
		do{
			c.afficherCouleursDispo();
			choix = reader.nextInt();
		}while(choix < 1 || choix > c.nombreCouleursDispo());
		//reader.close();
		return choix;
	}
	
	//après créer ou rejoindre une partie, demande de la liste des couleur
	public void demanderListeCouleur(ConnexionClient cx){
		String ipdu = this.getGPC().getColor();
		this.envoyer(cx,ipdu);
	}
	
	//demande d'une couleurs specifique
	public void demanderCouleur(String c, ConnexionClient cx){
		String ipdu = this.getGPC().choisirCouleur(c);
		this.envoyer(cx,ipdu);
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
		System.out.println("Provided by: Andrés E. DELGADO ANDRADE, Nady KERAGHEL, William PEREIRA DO CARMO©");
	}
}
