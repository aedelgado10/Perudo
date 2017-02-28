import java.util.ArrayList;

public class GestionProtocoleClient extends PDU{

	public GestionProtocoleClient() {
	}
	
	/** TRAITER PDU : Traitement de l'ensemble des PDU reçues en fonction de l'état de la partie
	 * 
	 * @param recu  String issu de la fonction recevoir
	 * @param c     Classe Client 
	 * @param cx    Classe ConnextionClient
	 * @return IMPORTANT : TOUS les returns de cette fonction servent a debugger
	 */
	public String traiter(String recu, Client c, ConnexionClient cx){
		ArrayList<String> req_args = this.decomposer(recu);  //separation PDU et Arguments
		String requete = req_args.get(0);  //extraction PDU
		String partie = c.getPartie().getStatus();  // Calcul du statut de la partie
		int choix = 0;
		
		// Les evenements sont géres en fonction du statut de la partie
		switch(partie){
			case NOPARTIES:
				switch(requete){
					case OKPARTY:
						c.getPartie().setIdParty(Integer.parseInt(req_args.get(1)));
						c.getPartie().setStatus(CREATINGPARTY);
						c.getPartie().setIsLeader(true);
						System.out.println("Vous avez rejoint la Partie: " + c.getPartie().getId());
						c.envoyer(cx, this.getColor());
						return "Partie cree";
					case REJPARTY:
						System.out.println("Creation de Partie Rejeté");
						c.traiterMenuBienvenue(cx);
						return "Creation de partie rejeté";
					case ROOMS:
						c.afficherPartiesDisponibles(req_args);
						choix = c.choixMenuClient(0, req_args.size());
						if (choix == req_args.size()){
							c.traiterMenuBienvenue(cx);
							return "Ne veut pas joindre";
						}else{
							c.getPartie().setStatus(JOININGPARTY);
							c.getPartie().setIdParty(choix);
							c.envoyer(cx, this.joinParty(choix));
							return "Join une salle";
						}
					case NO_ROOMS:
						System.out.println("Pas de Parties Disponibles.");
						c.traiterMenuBienvenue(cx);
						return "Pas de Parties en Cours";
					default:
						return recu + "\nErreur: Pas de parties initialisées";
				}
			case JOININGPARTY:
				switch(requete){
					case JOIN_OK:
						c.getPartie().setStatus(CREATINGPARTY);
						c.getPartie().setIsLeader(false);
						System.out.println("Vous avez rejoint la Partie: " + c.getPartie().getId());
						c.envoyer(cx, this.getColor());
						return "Joindre reussi";
					case JOIN_REJ:
						c.restartClient();
						System.out.println("Demande de Joindre Partie Rejeté");
						c.traiterMenuBienvenue(cx);
						return "Joindre partie rejeté";
					default:
						return recu + "\nErreur: Problème avec l'essai de joindre une partie";
				}
			case CREATINGPARTY:
				switch(requete){
					case ID:
						c.getJoueur().setIdJoueur(Integer.parseInt(req_args.get(1)));
						System.out.println("Quel est votre pseudo?:");
						String pseudo = c.choixTexte();
						c.getJoueur().setNomJoueur(pseudo);
						c.envoyer(cx, this.pseudoP(pseudo));
						return "ID trouvé";
					case COLOR:
						Couleur coul = new Couleur(req_args.get(1));
						c.getJoueur().setCouleurJoueur(coul);
				        c.envoyer(cx, this.getId());
						return "Couleur Trouvé";
					case PSEUDO_OK:
						c.getJoueur().setMyTurn(false);
						System.out.println("Bienvenue dans la partie (" + c.getJoueur().getCodeCouleurJoueur()+ ") " + c.getJoueur().getNomJoueur());
						c.getPartie().setStatus(WAITING);
						c.getPartie().ajouterJoueur(c.getJoueur());
						if(c.getPartie().getIsLeader()){
							this.traiterMenuLeaderNonDemarre(c, cx);
							return "Menu Leader Non Demarré";
						}
						else{
							System.out.println("Le Leader va bientôt lancer la Partie, patientez...");
							return "En attente du leader pour commencer";
						}
					case PSEUDO_KO:
						System.out.println("Erreur lors de la saisie du pseudo...");
						c.restartClient();
						c.traiterMenuBienvenue(cx);
						c.envoyer(cx, this.leaveParty());
						return "Probleme de creation de Pseudo: " + this.leaveParty();
					default:
						return recu + "\nErreur pendant la preparation pour joindre partie";
						
				}
			case WAITING:
				switch(requete){
					case PLAYERSIN:
						c.getPartie().traiterListeJoueurs(req_args, c);
						c.getPartie().afficherListeJoueurs();
						this.traiterMenuLeaderNonDemarre(c, cx);
						return "Liste des Joueurs: " + recu;
					case BEGIN_PARTY:
						c.getPartie().setStatus(PARTYPLAYING);
						System.out.println("La Partie est désormais lancée...");
						c.getPartie().setIsBeginning(true);
						c.envoyer(cx, this.listPlayers());
						return "la partie est lancée";
					case NOFNP:
						System.out.println("Vous êtes seul, impossible de Lancer");
						this.traiterMenuLeaderNonDemarre(c, cx);
						return "No Friends no Party";
					case PLS:
						System.out.println("Il y a des Clients qui ne sont pas prêts");
						this.traiterMenuLeaderNonDemarre(c, cx);
						return "Les clients ne sont pas prets";
					case PARTY_CANCELLED:
						c.restartClient();
						System.out.println("Partie Annulée!");
						c.traiterMenuBienvenue(cx);
						return "Partie annulé par le leader avant lancement";
					case RAGEQUIT:
						System.out.println("Le Leader a quitté, partie Annulé!");
						c.restartClient();
						c.traiterMenuBienvenue(cx);
						return "Ragequit du leader avant demarrage";
					default:
						return recu + "\nErreur pendant l'attente de lancement";
				}
			case PARTYPLAYING:
				switch(requete){
					case PLAYERSIN:
						if(c.getPartie().getIsBeginning()){
							c.getPartie().traiterListeJoueurs(req_args, c);
							c.getPartie().afficherListeJoueurs();
							c.envoyer(cx, this.who1st());
							return "Liste des joueurs quand la partie commence";
						}
						else{
							if(c.getJoueur().getMyTurn()){
								c.getPartie().traiterListeJoueurs(req_args, c);
								c.getPartie().afficherListeJoueurs();
								return "Liste des joueurs quand a mon tour";
							}
							else{
								return "Probleme players in";
							}
						}
					case PLAY1ST:
						c.getPartie().setIsBeginning(true);
						if(req_args.get(1).equals(c.getJoueur().getCodeCouleurJoueur())){
							c.getJoueur().setMyTurn(true);
							System.out.println("Vous êtes le premier à jouer");
							c.envoyer(cx, this.rollDices());
							return "Vous êtes le premier a jouer";
						}
						else{
							c.getJoueur().setMyTurn(false);
							System.out.println("Vous n'êtes pas le premier à jouer");
							c.envoyer(cx, this.rollDices());
							return "Vous n'êtes pas la premier a jouer";
						}
					case PLAY: 
						if(req_args.get(1).equals(c.getJoueur().getCodeCouleurJoueur())){
							c.getJoueur().setMyTurn(true);
							if(c.getPartie().getIsLeader()){
								this.traiterMenuLeaderDemarre(c, cx);
								return "Tour du leader";
							}
							else{
								this.traiterMenuJoueurDemarre(c, cx);
								return "Tour du joueur";
							}
						}else{
							c.getJoueur().setMyTurn(false);
							System.out.println("Tour du joueur: " + req_args.get(1));
							return "Pas mon tour";
						}
					case DICES:
						c.getJoueur().stockerDes(recu,c);
						if(c.getJoueur().getMyTurn()){
							System.out.println("Voici vos dés:\n" + c.getJoueur().voirDes());
							if(c.getPartie().getIsLeader()){
								this.traiterMenuLeaderPremier(c, cx);
								return "Des du leader";
							}
							else{
								this.traiterMenuJoueurPremier(c, cx);
								return "Des du joueur";
							}
						}
						else{
							System.out.println("Voici vos dés p:\n" + c.getJoueur().voirDes());
							System.out.println("En attente du premier joueur");
							return "Dés Joueur en Attente";
						}
					case APLUS:
						c.getPartie().traiterValeursAnnonces(c, recu);
						c.getPartie().afficherNomCouleurDernierJoue();
						System.out.println("Valeurs Annoncées: " 
								+ c.getPartie().getDernierNbrAnnonce() + " Dés de valeur " 
								+ c.getPartie().getDernierDeAnnonce() +"\n");
						c.envoyer(cx, WHO);
						return "Reception des valeurs d'un joueur";
					case TTPILE_OK:
						if(c.getJoueur().getMyTurn()){
							c.getPartie().traiterToutPile(c, recu, true);
							c.getJoueur().setMyTurn(false);
						}
						c.envoyer(cx, this.who1st());
						return "Tout pile success";
					case TTPILE_NOK:
						if(c.getJoueur().getMyTurn()){
							c.getPartie().traiterToutPile(c, recu, false);
							c.getJoueur().setMyTurn(false);
						}
						c.envoyer(cx, this.who1st());
						return "Tout Pile fail";
					case LIAR_OK:
							c.getPartie().traiterLiar(c, recu, true);
							c.getJoueur().setMyTurn(false);
						c.envoyer(cx, this.who1st());
						return "Liar success";
					case LIAR_NOK:
							c.getPartie().traiterLiar(c, recu, false);
							c.getJoueur().setMyTurn(false);
						c.envoyer(cx, this.who1st());
						return "Liar fail";
					case PARTY_CANCELLED:
						System.out.println("Partie Annulée!");
						c.restartClient();
						c.traiterMenuBienvenue(cx);
						return "Partie annulé par le leader pendant le jeu";
					case RAGEQUIT:
						System.out.println("Le Leader a quitté, partie Annulé!");
						c.restartClient();
						c.traiterMenuBienvenue(cx);
						return "Ragequit du leader apres demarrage";
					default:
						return recu + "\nErreur: Problème traitement de partie en cours";
				}
			default:
				return recu + "\nErreur: Le statut de la partie est bizarre!";
		}
	}
	/****************************************************************/
	
	
	/*TRAITER MENUS DANS PARTIE*/
	public void traiterMenuLeaderNonDemarre(Client c, ConnexionClient cx){
		int choix = c.choixMenuClient(3,3); // 3: menu leader non demarré, 3: nombre choix max
		switch(choix){
			case 1:
				c.envoyer(cx, this.launchParty());
				//c.envoyer(cx, this.who1st());
				break;
			case 2:
				c.envoyer(cx, this.listPlayers());
				break;
			case 3:
				c.envoyer(cx, this.stopParty());
				c.traiterMenuBienvenue(cx);
				break;
			default:
				System.out.println("Erreur Saisie Menu Leader non demarré");
				break;
		}
	}
	
	public void traiterMenuLeaderPremier(Client c, ConnexionClient cx){
		int choix = c.choixMenuClient(5,4); // 5: menu leader 1er a jouer, 4: nombre choix max
		switch(choix){
			case 1:
				c.getPartie().setIsBeginning(false);
				c.getJoueur().setMyTurn(false);
				c.envoyer(cx,this.surencherirPremier(c.getJoueur().choisirValeursPremier()));
				break;
			case 2:
				System.out.println("Voici vos dés:\n" + c.getJoueur().voirDes());
				this.traiterMenuLeaderPremier(c, cx);
				break;
			case 3:
				c.envoyer(cx, this.listPlayers());
				break;
			case 4:
				c.envoyer(cx, this.stopParty());
				break;
			default:
				System.out.println("Erreur Saisie Menu Leader Premier");
				break;
		}
	}
	
	public void traiterMenuJoueurPremier(Client c, ConnexionClient cx){
		int choix = c.choixMenuClient(7,3); // 7: menu joueur 1er a jouer, 3: nombre choix max
		switch(choix){
			case 1:
				c.getPartie().setIsBeginning(false);
				c.getJoueur().setMyTurn(false);
				c.envoyer(cx,this.surencherirPremier(c.getJoueur().choisirValeursPremier()));
				break;
			case 2:
				System.out.println("Voici vos dés:\n" + c.getJoueur().voirDes());
				this.traiterMenuJoueurPremier(c, cx);
				break;
			case 3:
				c.envoyer(cx, this.listPlayers());
				break;
			case 4:
				c.envoyer(cx, this.leaveParty());
				break;
			default:
				System.out.println("Erreur Saisie Menu Joueur Premier");
				break;
		}
	}
	
	public void traiterMenuLeaderDemarre(Client c, ConnexionClient cx){
		int choix = c.choixMenuClient(4,6); // 4: menu leader demarré, 6: nombre choix max
		switch(choix){
			case 1:
				c.getJoueur().setMyTurn(false);
				c.envoyer(cx,this.surencherir(c.getJoueur().choisirValeursEnsuite(c.getPartie().getDernierNbrAnnonce(), c.getPartie().getDernierDeAnnonce())));
				break;
			case 2:
				c.envoyer(cx, this.liar());
				break;
			case 3:
				c.envoyer(cx, this.ttPile());
				break;
			case 4:
				System.out.println("Voici vos dés:\n" + c.getJoueur().voirDes());
				this.traiterMenuLeaderDemarre(c, cx);
				break;
			case 5:
				c.envoyer(cx, this.listPlayers());
				break;
			case 6:
				c.envoyer(cx, this.stopParty());
				break;
			default:
				System.out.println("Erreur Saisie Menu Leader Demarré");
				break;
		}
	}
	
	public void traiterMenuJoueurDemarre(Client c, ConnexionClient cx){
		int choix = c.choixMenuClient(6,6); // 6: menu joueur demarré, 6: nombre choix max
		switch(choix){
			case 1:
				c.getJoueur().setMyTurn(false);
				c.envoyer(cx,this.surencherir(c.getJoueur().choisirValeursEnsuite(c.getPartie().getDernierNbrAnnonce(), c.getPartie().getDernierDeAnnonce())));
				break;
			case 2:
				c.envoyer(cx, this.liar());
				break;
			case 3:
				c.envoyer(cx, this.ttPile());
				break;
			case 4:
				System.out.println("Voici vos dés:\n" + c.getJoueur().voirDes());
				this.traiterMenuJoueurDemarre(c, cx);
				break;
			case 5:
				c.envoyer(cx, this.listPlayers());
				break;
			case 6:
				c.envoyer(cx, this.leaveParty());
				break;
			default:
				System.out.println("Erreur Saisie Menu Joueur Demarré");
				break;
		}
	}
	/******************************************************************/
	
	/*  PDUS prêtes pour envoyer  */
	public String createParty(){
		return CREATE_PARTY;
	}
	
	public String joinParty(int i){
		return JOIN_PARTY + " " + i;
	}
	
	public String getColor(){
		return PDU.WHICH_COLOR;
	}
	
	public String launchParty(){
		return PDU.LAUNCH;
	}
	
	public String ttPile(){
		return PDU.TTPILE;
	}
	
	public String getId(){
		return PDU.GET_ID;
	}
	
	public String liar(){
		return PDU.LIAR;
	}
	
	public String surencherir(ArrayList<Integer> vals){
		return PDU.PLUS +" "+ vals.get(1) +" " + vals.get(0) ;
	}
	
	public String surencherirPremier(ArrayList<Integer> vals){
		return PDU.FANNOUNCE +" "+ vals.get(1) +" " + vals.get(0) ;
	}
	
	public String rollDices(){
		return PDU.ROLL;
	}
	
	public String listRooms(){
		return PDU.LISTROOMS;
	}
	
	public String pseudoP(String s){
		return PDU.PSEUDOP + " " + s;
	}
	
	public String leaveParty(){
		return PDU.LEAVE;
	}
	
	public String stopParty(){
		return PDU.STOP_PARTY;
	}
	
	public String listPlayers(){
		return PDU.LISTPLAYERS;
	}
	
	public String quitter(){
		return PDU.QUIT;
	}
	
	public String who1st(){
		return PDU.WHO1;
	}
	/****************************************************************/
}
