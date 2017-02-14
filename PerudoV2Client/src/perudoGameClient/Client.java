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
	private int cptTry;
	private boolean clientTourne;


	
	//Constructeur Client
	public Client(){
		this.gPc = new GestionProtocoleClient();
		this.lastPduSent = "";
		this.cptTry = 0;
		this.clientTourne = true;
	}
	
	public boolean getClientTourne(){
		return this.clientTourne;
	}
	
	public void setClientTourneFalse(){
		this.clientTourne = false;
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
	
	//méthode de connexion client - serveur
	public void connecterServeur(ConnexionClient cx) throws IOException{
		cx.ConnecterServeur();
	}
	
	//Envoie la demande, stocke celle ci
	public void envoyer(ConnexionClient cx, String ipdu){
		cx.envoyer(ipdu);
		String[] splited = ipdu.split("\\s+");
		System.out.println("plit: " + splited[0] + " lel: " +ipdu);
		this.setLastPduSent(splited[0]);
	}
	
	//reset le compteur de tentatives apres erreur
	public void resetCptTry(){
		this.cptTry = 0;
	}
	
	public void cptTryPlusUn(){
		this.cptTry++;
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
	
	//Menu affiché a l'ouverture du Client
	public int menuPrincipal(){
		
		int choix;
		Scanner scan = new Scanner(System.in);
		do{
		  this.afficherMenuClientNonConnecte();
		  choix = scan.nextInt();
		}while(choix > 2 || choix < 1);	
		return choix;
	}
	
	//Menu de choix d'action après connexion au serveur
	public int menuChoix(){
		
		Scanner scan = new Scanner(System.in);
		int choix;
		do{
		  this.afficherMenuClientConnecte();
		  choix = scan.nextInt();
		}while(choix > 3 || choix < 1);	
		
		return choix;
	}
	
		
	
	//traite la réponse et reinitialise la dernière demande a vide
	public void traiter(String recu, ConnexionClient cx){
		ArrayList<String> repDecompose;
		System.out.println("recu: " + recu);
		repDecompose = this.getGPC().decomposer(recu);
		System.out.println("rep: " + repDecompose.get(0));
		System.out.println("last sent: " + this.getLastPduSent());
		
		if(this.getGPC().repCoherente(repDecompose, this.getLastPduSent()) == 1 ){
			
			if(this.getGPC().isPositive(repDecompose.get(0))){
				this.setLastPduSent("");
				this.gererJeu(repDecompose,cx);
			}
			else{
				switch(this.getLastPduSent()){
					case PDU.CREATE_PARTY:
						System.out.println("Impossible de creer la partie.");
						this.traiterMenuChoix(cx);
						break;
					case PDU.WHICH_COLOR:
						//cas improbable
						System.out.println("Il n'y a plus de couleurs disponibles, jeté?");
						this.traiterMenuChoix(cx);
						break;
					case PDU.GET_ID:
						System.out.println("Pas d'ID attribué, jeté?");
						this.traiterMenuChoix(cx);
						break;
					case PDU.PSEUDOP:
						System.out.println("Ce pseudo n'est pas disponible!");
						this.j1.setNom(this.saisirPseudo());
						this.demanderPseudo(this.j1.getPseudo(), cx);
						break;
					case PDU.LISTROOMS:
						System.out.println("Pas de parties trouvées pour l'instant!");
						this.traiterMenuChoix(cx);
						break;
					case PDU.JOIN_PARTY:
						System.out.println("Impossible de rejoindre cette partie");
						this.p1 = null;
						this.traiterMenuChoix(cx);
						break;
					case PDU.LAUNCH:
						System.out.println("Impossible de lancer la partie (Clients non prêts / vous êtes seul)");
						this.traiterDemandeJoueur(j1, cx);
						break;
					case PDU.STOP_PARTY:
						System.out.println("Demande d'arrêt rejeté");
						this.traiterDemandeJoueur(j1, cx);
						break;
					default:
						break;
				}
				this.resetCptTry();
				//this.setLastPduSent("");
			}
		}
		else{
			if(cptTry < 3){
				this.cptTryPlusUn();
				this.envoyer(cx, this.getLastPduSent());
			}else{
				//Ce cas ne devrait jamais arriver
				System.out.println("Le serveur est Fou! Ce client va être tué");
				this.setClientTourneFalse();
				cx.FermerConnexionServeur();
			}
		}
	}
	
	//Si la reponse du serveur est positive, on traite la suite
	public void gererJeu(ArrayList<String> rep, ConnexionClient cx){
		
		switch(rep.get(0)){
		  case PDU.OKPARTY:
			  this.p1 = new Partie(Integer.parseInt(rep.get(1)));
			  p1.setStatus(PDU.WAITING);
			  p1.setPartyLeader(true);
			  System.out.println("Vous venez de créer une Partie.");
			  this.demanderCouleur(cx);
			  break;
		  case PDU.ROOMS:
			  if (!(this.traiterChoixPartie(this.choixPartie(rep), rep, cx))){
				  break;
			  }
			  //this.p1 = new Partie(this.choixPartie(rep));
			  p1.setStatus(PDU.WAITING);
			  p1.setPartyLeader(false);
			  this.demanderRejoindre(p1.getIdPartie(), cx);
			  break;
		  case PDU.JOIN_OK:
			  System.out.println("Vous avez rejoin la partie: " + p1.getIdPartie());
			  this.demanderCouleur(cx);
			  break;
		  case PDU.COLOR:
			  this.c1 = new Couleur(rep.get(1));
			  System.out.println("Votre couleur est: " + c1.getCodeCouleur());
			  this.demandeIDJoueur(cx);
			  break;
		  case PDU.ID:
			  this.j1 = new Joueur(Integer.parseInt(rep.get(1)),this.c1,p1.getPartyLeader());
			  this.j1.setNom(this.saisirPseudo());
			  this.demanderPseudo(this.j1.getPseudo(), cx);
			  break;
		  case PDU.PSEUDO_OK:
			  System.out.println("Votre pseudo est validé: " + this.j1.getPseudo());
			  this.p1.ajouterJoueur(this.j1);
			  if (j1.getLeader()){
				  // Vous êtes le leader, voulez vous lancer?
				  System.out.println("Vous êtes le Leader, c'est a vous de démarrer le jeu!");
				  this.traiterDemandeJoueur(j1, cx);
			  }
			  else{
				  System.out.println("Le Leader va bientôt démarrer la partie, patienter...");
			  }
			  break;
		  case PDU.BEGIN_PARTY:
			  System.out.println("La Partie commence, un joueur va être désigné pour commencer!!");
			  //this.setLastPduSent("");
			  p1.setStatus(PDU.PARTYPLAYING);
			  //roll dices
			  break;
			  
		  case PDU.PARTY_CANCELLED:
			  System.out.println("La partie a été annulé...");
			  this.setLastPduSent("");
			  this.p1=null;
			  this.j1=null;
			  this.traiterMenuChoix(cx);
			  break;
		}
	}
	
	// traitement demande des joueurs en fonction du status de la partie
	public void traiterDemandeJoueur(Joueur j, ConnexionClient cx){
		int choix;
		//if party began?
	    if(!(p1.isPartyRunning())){
			if(j.getLeader()){
				choix = j.menuChoixLeaderAvantDemarrer();
				switch(choix){
				   case 1: 
					   this.demandeDemarrer(cx);
					   break;
				   case 2: 
					   break;
				   case 3:
					   this.demandeAnnuler(cx);
					   //b = false;
					   break;
				   default:
					   //ça ne devrait jamais arriver, la fonction est bornée
					   System.out.println("Erreur, saisie traitement leader");
					   break;
				}
			}
		}else{
			//cas 1er a jouer
			//cas leader
		}
	}
	
	//traiter le choix de la partie
	public boolean traiterChoixPartie(int i, ArrayList<String> rep, ConnexionClient cx){
		if( i == rep.size() ){
			this.traiterMenuChoix(cx);
			return false;
		}
		else{
			this.p1 = new Partie(i);
			return true;
		}
	}
	
	//affiche la liste des parties
	public void afficherListeParties(ArrayList<String> s){
		System.out.println("Voici les parties: ");
		ArrayList<String> parser = new ArrayList<>();
		for( String partie : s.subList(1, s.size()) ){
			parser = this.gPc.parseListRooms(partie);
			System.out.println("Tapper 1 pour rejoindre la partie: " + parser.get(0)
			+ " avec status: " + parser.get(1) + " ("+parser.get(2)+")");
		}
		System.out.println("Tapper " + s.size() + " pour ne pas rejoindre");
	}
	
	//Demande a l'utilisateur de rentrer un pseudo
	public String saisirPseudo(){
		Scanner scan = new Scanner(System.in);
		System.out.println("Saisir votre pseudo:");
		String pseudo = "";
		pseudo = scan.next();
		return pseudo;
	}
	
	//choix partie a rejoindre
	public int choixPartie(ArrayList<String> s){
		int choix;
		Scanner scan = new Scanner(System.in);
		do{
		  this.afficherListeParties(s);
		  choix = scan.nextInt();
		}while(choix > s.size() || choix < 1);	
		return choix;
	}
	
	public void demandeAnnuler(ConnexionClient cx){
		String ipdu = this.getGPC().stopParty();
		this.envoyer(cx,ipdu);
	}
	
	//Effectuer demande de démarer partie
	public void demandeDemarrer(ConnexionClient cx){
		String ipdu = this.getGPC().launchParty();
		this.envoyer(cx,ipdu);
	}
	
	//demande de joindre partie
	public void demanderRejoindre(int i, ConnexionClient cx){
		String ipdu = this.getGPC().joinParty(i);
		this.envoyer(cx,ipdu);
	}
	
	//demande d'envoi de pseudo
	public void demanderPseudo(String s, ConnexionClient cx){
		String ipdu = this.getGPC().pseudoP(s);
		this.envoyer(cx,ipdu);
	}
	
	
	//demande d'identifiant de joueur
	public void demandeIDJoueur(ConnexionClient cx){
		String ipdu = this.getGPC().getId();
		this.envoyer(cx,ipdu);
	}
	
	//après créer ou rejoindre une partie, demande de la liste des couleur
	public void demanderCouleur(ConnexionClient cx){
		String ipdu = this.getGPC().getColor();
		this.envoyer(cx,ipdu);
	}
	
	
	//Méthode qui est lancée par le main si l'utilisateur choisit créer partie
	public void creerPartie(ConnexionClient cx){
		String ipdu = this.getGPC().createParty();
		this.envoyer(cx,ipdu);
	}
	
	public void demanderListeParties(ConnexionClient cx){
		String ipdu = this.getGPC().listRooms();
		this.envoyer(cx,ipdu);
	}
	
	//traitement de la selection du menu choix
	public void traiterMenuChoix(ConnexionClient cx){
		int choix;
			choix = this.menuChoix();
			switch(choix){
			case 1:
				this.creerPartie(cx);
				break;
			case 2:
				this.demanderListeParties(cx);
				break;
			default:
				System.out.println("Fermeture Client Perudo!");
				this.setClientTourneFalse();
				cx.FermerConnexionServeur();
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
				client.traiterMenuChoix(cx);
				while( client.getClientTourne()); // maintient le client ouvert
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
