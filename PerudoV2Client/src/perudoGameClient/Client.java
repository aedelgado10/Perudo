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

	
	//Constructeur Client
	public Client(){
		this.gPc = new GestionProtocoleClient();
		this.lastPduSent = "";
		this.cptTry = 0;
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
		
		Scanner scan = new Scanner(System.in);
		int choix;
		do{
		  this.afficherMenuClientConnecte();
		  choix = scan.nextInt();
		}while(choix > 3 || choix < 1);	
		
		return choix;
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
		String[] splited = ipdu.split("\\s+");
		this.setLastPduSent(splited[0]);
	}
	
	//reset le compteur de tentatives apres erreur
	public void resetCptTry(){
		this.cptTry = 0;
	}
	
	public void cptTryPlusUn(){
		this.cptTry++;
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
						this.traiterMenuChoix(cx, true);
						break;
					case PDU.WHICH_COLOR:
						//cas improbable
						System.out.println("Il n'y a plus de couleurs disponibles, jeté?");
						this.traiterMenuChoix(cx, true);
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
					case PDU.LISTROOMS:
						System.out.println("Pas de parties trouvées pour l'instant!");
						this.traiterMenuChoix(cx, true);
						break;
					case PDU.JOIN_PARTY:
						System.out.println("Impossible de rejoindre cette partie");
						this.traiterMenuChoix(cx, true);
					default:
						break;
				}
				this.resetCptTry();
				this.setLastPduSent("");
			}
		}
		else{
			if(cptTry < 3){
				this.cptTryPlusUn();
				this.envoyer(cx, this.getLastPduSent());
			}else{
				//Ce cas ne devrait jamais arriver
				System.out.println("Le serveur est Fou! Ce client va être tué");
				this.traiterMenuChoix(cx, false);
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
			  this.p1 = new Partie(this.choixPartie(rep));
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
			  this.j1 = new Joueur(Integer.parseInt(rep.get(1)),this.c1);
			  this.j1.setNom(this.saisirPseudo());
			  this.demanderPseudo(this.j1.getPseudo(), cx);
			  break;
		  case PDU.PSEUDO_OK:
			  System.out.println("Votre pseudo est validé: " + this.j1.getPseudo());
			  this.p1.ajouterJoueur(this.j1);
			  if (p1.getPartyLeader()){
				  // Vous êtes le leader, voulez vous lancer?
				  System.out.println("Vous êtes le leader, c'est a vous de Lancer!");
			  }
			  else{
				  System.out.println("Le Leader va bientôt lancer la partie, patienter...");
			  }
			  break;
		}
	}
	
	//affiche la liste des parties
	public void afficherListeParties(ArrayList<String> s){
		System.out.println("Voici les parties: ");
		for(String partie : s.subList(1, s.size())){
			System.out.println(partie);
		}
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
	
	//Demande a l'utilisateur de rentrer un pseudo
	public String saisirPseudo(){
		Scanner scan = new Scanner(System.in);
		System.out.println("Saisir votre pseudo:");
		String pseudo = "";
		pseudo = scan.next();
		return pseudo;
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
	public void traiterMenuChoix(ConnexionClient cx, boolean b){
		int choix;
		
		if(b){
			choix = this.menuChoix();
			switch(choix){
			case 1:
				this.creerPartie(cx);
				break;
			case 2:
				this.demanderListeParties(cx);
				break;
			default:
				b = false;
				System.out.println("Fermeture Client Perudo!");
				break;
			}
		}
		else{
			System.out.println("Fermeture Client Perudo!");
		}
		while(b){
			//pour que le programme ne s'arrête pas :)
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
