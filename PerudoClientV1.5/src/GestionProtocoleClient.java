import java.util.ArrayList;

public class GestionProtocoleClient extends PDU{

	public GestionProtocoleClient() {
	}
	
	/** TRAITER PDU : Traitement de l'ensemble des PDU re�ues en fonction de l'�tat de la partie
	 * 
	 * @param recu  String issu de la fonction recevoir
	 * @param c     Classe Client 
	 * @param cx    Classe ConnextionClient
	 * @return IMPORTANT : TOUS les returns de cette fonction servent a debugger, penser enlever les
	 * 					   commentaires des println de Client.traiter() 
	 */
	public String traiter(String recu, Client c, ConnexionClient cx){
		ArrayList<String> req_args = this.decomposer(recu);  //separation PDU et Arguments
		String requete = req_args.get(0);  //extraction PDU
		String partie = c.getPartie().getStatus();  // Calcul du statut de la partie
		int choix = 0;
		
		if(requete.equals(OK_LEAVE)){
			System.out.println("Vous avez quitt� la partie");
			c.restartClient();
			c.traiterMenuBienvenue(cx);
			return "Leaving Party";
		}
		
		// Les evenements sont g�res en fonction du statut de la partie
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
						System.out.println("Creation de Partie Rejet�");
						c.traiterMenuBienvenue(cx);
						return "Creation de partie rejet�";
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
					case WHATSUP:
						System.out.println("Erreur, message: " + recu);
						//c.traiterMenuBienvenue(cx);
						return "Erreur serveur n'a pas compris: " + recu;
					default:
						return recu + "\nErreur: Pas de parties initialis�es";
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
						System.out.println("Demande de Joindre Partie Rejet�");
						c.traiterMenuBienvenue(cx);
						return "Joindre partie rejet�";
					case WHATSUP:
						System.out.println("Erreur, message: " + recu);
						c.restartClient();
						c.traiterMenuBienvenue(cx);
						return "Erreur serveur n'a pas compris: " + recu;
					default:
						return recu + "\nErreur: Probl�me avec l'essai de joindre une partie";
				}
			case CREATINGPARTY:
				switch(requete){
					case ID:
						c.getJoueur().setIdJoueur(Integer.parseInt(req_args.get(1)));
						System.out.println("Quel est votre pseudo?:");
						String pseudo = c.choixTexte();
						c.getJoueur().setNomJoueur(pseudo);
						c.envoyer(cx, this.pseudoP(pseudo));
						return "ID trouv�";
					case COLOR:
						Couleur coul = new Couleur(req_args.get(1));
						c.getJoueur().setCouleurJoueur(coul);
				        c.envoyer(cx, this.getId());
						return "Couleur Trouv�";
					case PSEUDO_OK:
						c.getJoueur().setMyTurn(false);
						System.out.println("\nBienvenue dans la partie (" + c.getJoueur().getCodeCouleurJoueur()+ ") " + c.getJoueur().getNomJoueur());
						c.getPartie().setStatus(WAITING);
						c.getPartie().ajouterJoueur(c.getJoueur());
						if(c.getPartie().getIsLeader()){
							this.traiterMenuLeaderNonDemarre(c, cx);
							return "Menu Leader Non Demarr�";
						}
						else{
							System.out.println("Le Leader va bient�t lancer la Partie, patientez...");
							return "En attente du leader pour commencer";
						}
					case PSEUDO_KO:
						System.out.println("Erreur lors de la saisie du pseudo...");
						c.restartClient();
						c.envoyer(cx, this.leaveParty());
						return "Probleme de creation de Pseudo: " + this.leaveParty();
					case WHATSUP:
						System.out.println("Erreur, message: " + recu);
						c.restartClient();
						c.envoyer(cx, this.leaveParty());
						return "Erreur serveur n'a pas compris: " + recu;
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
						System.out.println("La Partie est d�sormais lanc�e...");
						c.getPartie().setIsBeginning(true);
						c.envoyer(cx, this.listPlayers());
						return "la partie est lanc�e";
					case NOFNP:
						System.out.println("Vous �tes seul, impossible de Lancer");
						this.traiterMenuLeaderNonDemarre(c, cx);
						return "No Friends no Party";
					case PLS:
						System.out.println("Il y a des Clients qui ne sont pas pr�ts");
						this.traiterMenuLeaderNonDemarre(c, cx);
						return "Les clients ne sont pas prets";
					case PARTY_CANCELLED:
						c.restartClient();
						System.out.println("\n\nPartie Annul�e!");
						c.traiterMenuBienvenue(cx);
						return "Partie annul� par le leader avant lancement";
					case RAGEQUIT:
						System.out.println("Le Leader a quitt�, partie Annul�!");
						c.restartClient();
						c.traiterMenuBienvenue(cx);
						return "Ragequit du leader avant demarrage";
					case WHATSUP:
						System.out.println("Erreur, message: " + recu);
						if(c.getPartie().getIsLeader()){
							this.traiterMenuLeaderNonDemarre(c, cx);
						}
						return "Erreur serveur n'a pas compris: " + recu;
					
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
								//System.out.println(c.getJoueur().getCodeCouleurJoueur() + ": � vous de jouer");
								this.traiterMenuJoueurDemarre(c, cx);
								return "Liste des joueurs quand a mon tour";
							}
							else{
								c.getPartie().traiterListeJoueurs(req_args, c);
								return "Mise a jour: liste joueurs";
							}
						}
					case PLAY1ST:
						c.getPartie().setIsBeginning(true);
						if(req_args.get(1).equals(c.getJoueur().getCodeCouleurJoueur())){
							c.getJoueur().setMyTurn(true);
							System.out.println(c.getJoueur().getCodeCouleurJoueur() + " : Vous �tes le premier � jouer");
							c.envoyer(cx, this.rollDices());
							return "Vous �tes le premier a jouer";
						}
						else{
							c.getJoueur().setMyTurn(false);
							System.out.println(req_args.get(1) + ": Est le premier a jouer!");
							c.envoyer(cx, this.rollDices());
							return "Vous n'�tes pas la premier a jouer";
						}
					case PLAY: 
						c.getPartie().setIsBeginning(false);
						if(req_args.get(1).equals(c.getJoueur().getCodeCouleurJoueur())){
							c.getJoueur().setMyTurn(true);
							System.out.println(req_args.get(1) + ": � vous de jouer!");
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
							System.out.println(c.getJoueur().getCodeCouleurJoueur() + " voici vos d�s:\n" + c.getJoueur().voirDes());
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
							System.out.println("Voici vos d�s :\n" + c.getJoueur().voirDes());
							System.out.println("En attente du premier joueur\n");
							return "D�s Joueur en Attente";
						}
					case APLUS:
						c.getPartie().traiterValeursAnnonces(c, recu);
						c.getPartie().afficherNomCouleurDernierJoue();
						System.out.println("Valeurs Annonc�es: " 
								+ c.getPartie().getDernierNbrAnnonce() + " D�s de valeur " 
								+ c.getPartie().getDernierDeAnnonce() +"\n");
						c.envoyer(cx, WHO);
						return "Reception des valeurs d'un joueur";
					case TTPILE_OK:
						if(c.getJoueur().getMyTurn()){
							c.getPartie().traiterToutPile(c, recu, true);
							c.getJoueur().setMyTurn(false);
						}
						else{
							System.out.println("Tout Pile!\n");
						}
						c.getPartie().setIsBeginning(true);
						c.envoyer(cx, this.listPlayers());
						return "Tout pile success";
					case TTPILE_NOK:
						if(c.getJoueur().getMyTurn()){
							c.getPartie().traiterToutPile(c, recu, false);
							c.getJoueur().setMyTurn(false);
						}
						else{
							System.out.println("Ce n'est pas Tout Pile!\n");
						}
						c.getPartie().setIsBeginning(true);
						c.envoyer(cx, this.listPlayers());
						return "Tout Pile fail";
					case LIAR_OK:
						c.getPartie().traiterLiar(c, recu, true);
						c.getJoueur().setMyTurn(false);
						c.getPartie().setIsBeginning(true);
						c.envoyer(cx, this.listPlayers());
						return "Liar success";
					case LIAR_NOK:
						c.getPartie().traiterLiar(c, recu, false);
						c.getJoueur().setMyTurn(false);
						c.getPartie().setIsBeginning(true);
						c.envoyer(cx, this.listPlayers());
						return "Liar fail";
					case LOSER:
						if( req_args.get(1).equals(c.getJoueur().getCodeCouleurJoueur())){
							System.out.println("Vous �tes elimin�!");
							c.restartClient();
							c.traiterMenuBienvenue(cx);
							return "Joueur Elimin�";
						}else{
							System.out.println("Le joueur: " + req_args.get(1) + " est elimin�");
							return "Joueur Elimin� 2";
						}
					case WINNER:
						System.out.println("\n");
						System.out.println(req_args.get(1) + " vous �tes le vainqueur de cette Partie!");
						switch(req_args.get(2)){
							case TTPILE_OK:
								System.out.println("puisque vous avez reussi votre Tout Pile");
			                    break;
							case TTPILE_NOK:
								System.out.println("puisque le joueur pr�cedant n'a pas reussi son Tout Pile");
								break;
							case LIAR_OK:
								System.out.println("puisque vous avez devoil� le Menteur");
							    break;
							case LIAR_NOK:
								System.out.println("puisque vous n'avez devoil� le Menteur");
								break;
						}
						c.restartClient();
						c.traiterMenuBienvenue(cx);
						return "Vainqueur";
					case NO_DICES:
						System.out.println(c.getJoueur().getNomJoueur() + " " + c.getJoueur().getCodeCouleurJoueur() + " Vous n'avez plus de d�s!");
						return "Plus de d�s";
					case PARTY_CANCELLED:
						System.out.println("\n\nPartie Annul�e!");
						c.restartClient();
						c.traiterMenuBienvenue(cx);
						return "Partie annul� par le leader pendant le jeu";
					case RAGEQUIT:
						System.out.println("Le Leader a quitt�, partie Annul�!");
						if(c.getJoueur().getMyTurn()){
							c.restartClient();
							return "La partie n'est plus en cours";
						}
						c.restartClient();
						c.traiterMenuBienvenue(cx);
						return "Ragequit du leader apres demarrage";
					case WHATSUP:
						System.out.println("Erreur, message: " + recu);
						if(c.getJoueur().getMyTurn()){
							if(c.getPartie().getIsLeader()){
								if(c.getPartie().getIsBeginning()){
									this.traiterMenuLeaderPremier(c, cx);
								}
								else{
									this.traiterMenuLeaderDemarre(c, cx);
								}
							}else{
								if(c.getPartie().getIsBeginning()){
									this.traiterMenuJoueurPremier(c, cx);
								}
								else{
									this.traiterMenuJoueurDemarre(c, cx);
								}
							}
						}
						return "Erreur serveur n'a pas compris: " + recu;
					default:
						return recu + "\nErreur: Probl�me traitement de partie en cours";
				}
			default:
				return recu + "\nErreur: Le statut de la partie est bizarre!";
		}
	}
	/****************************************************************/
	
	
	/*TRAITER MENUS DANS PARTIE*/
	
	/**
	 * traite le choix du leader de la partie avant de commancer celle ci
	 * @param c Client � traiter
	 * @param cx Connexion avec le serveur
	 */
	public void traiterMenuLeaderNonDemarre(Client c, ConnexionClient cx){
		System.out.print(c.getJoueur().getNomJoueur() + " ("+c.getJoueur().getCodeCouleurJoueur()+") : ");
		int choix = c.choixMenuClient(3,3); // 3: menu leader non demarr�, 3: nombre choix max
		switch(choix){
			case 1:
				c.envoyer(cx, this.launchParty());   
				break;
			case 2:
				c.envoyer(cx, this.listPlayers());
				break;
			case 3:
				c.envoyer(cx, this.stopParty()); // annuler partie
				break;
			default:
				this.traiterMenuLeaderNonDemarre(c, cx); //on recommence
				break;
		}
	}
	
	/**
	 * traite le choix du leader si celui ci est le premier � jouer
	 * @param c
	 * @param cx
	 */
	public void traiterMenuLeaderPremier(Client c, ConnexionClient cx){
		System.out.print(c.getJoueur().getNomJoueur() + " ("+c.getJoueur().getCodeCouleurJoueur()+") : ");
		int choix = c.choixMenuClient(5,4); // 5: menu leader 1er a jouer, 4: nombre choix max
		switch(choix){
			case 1:  //Surencher
				ArrayList<Integer> arr = new ArrayList<>();
				arr = c.getJoueur().choisirValeursPremier();        // choix des valeurs � jouer
				if(arr.get(0) == 99){                               // si le client rentre une lettre
					System.out.println("\nErreur Saisie Valeurs");
					this.traiterMenuLeaderPremier(c, cx);
					break;
				}
				c.getPartie().setIsBeginning(false); 
				c.getJoueur().setMyTurn(false);
				c.envoyer(cx,this.surencherirPremier(arr));
				break;
			case 2:  // voir d�s
				System.out.println("Voici vos d�s:\n" + c.getJoueur().voirDes());
				this.traiterMenuLeaderPremier(c, cx);
				break;
			case 3:   // lister joueurs
				c.envoyer(cx, this.listPlayers());
				break;
			case 4:   // annuler partie
				c.envoyer(cx, this.stopParty());
				break;
			default:
				this.traiterMenuLeaderPremier(c, cx);  //on recommence
				break;
		}
	}
	
	/**
	 * traite le choix pour le joueur si celui ci est le premier
	 * @param c
	 * @param cx
	 */
	public void traiterMenuJoueurPremier(Client c, ConnexionClient cx){
		System.out.print(c.getJoueur().getNomJoueur() + " ("+c.getJoueur().getCodeCouleurJoueur()+") : ");
		int choix = c.choixMenuClient(7,4); // 7: menu joueur 1er a jouer, 3: nombre choix max
		switch(choix){
			case 1:
				ArrayList<Integer> arr = new ArrayList<>();
				arr = c.getJoueur().choisirValeursPremier();
				if(arr.get(0) == 99){
					System.out.println("\nErreur Saisie Valeurs");
					this.traiterMenuJoueurPremier(c, cx);
					break;
				}
				c.getPartie().setIsBeginning(false);
				c.getJoueur().setMyTurn(false);
				c.envoyer(cx,this.surencherirPremier(arr));
				break;
			case 2:
				System.out.println("Voici vos d�s:\n" + c.getJoueur().voirDes());
				this.traiterMenuJoueurPremier(c, cx);
				break;
			case 3:
				c.envoyer(cx, this.listPlayers());
				break;
			case 4:
				c.envoyer(cx, this.leaveParty());   //quitter partie
				break;
			default:
				this.traiterMenuJoueurPremier(c, cx);
				break;
		}
	}
	
	/**
	 * traite le choix du leader de la partie si celui ci n'est plus le premier
	 * @param c
	 * @param cx
	 */
	public void traiterMenuLeaderDemarre(Client c, ConnexionClient cx){
		System.out.print(c.getJoueur().getNomJoueur() + " ("+c.getJoueur().getCodeCouleurJoueur()+") : ");
		int choix = c.choixMenuClient(4,6); // 4: menu leader demarr�, 6: nombre choix max
		switch(choix){
			case 1:
				ArrayList<Integer> arr = new ArrayList<>();
				arr = c.getJoueur().choisirValeursEnsuite(c.getPartie().getDernierNbrAnnonce(), c.getPartie().getDernierDeAnnonce());
				if(arr.get(0) == 99){
					System.out.println("\nErreur Saisie Valeurs");
					this.traiterMenuLeaderDemarre(c, cx);
					break;
				}
				c.getJoueur().setMyTurn(false);
				c.envoyer(cx,this.surencherir(arr));
				break;
			case 2:
				c.envoyer(cx, this.liar());  //dire menteur
				break;
			case 3:
				c.envoyer(cx, this.ttPile()); // dire tout pile
				break;
			case 4:
				System.out.println("Voici vos d�s:\n" + c.getJoueur().voirDes());
				this.traiterMenuLeaderDemarre(c, cx);
				break;
			case 5:
				c.envoyer(cx, this.listPlayers());
				break;
			case 6:
				c.envoyer(cx, this.stopParty());   // annuler partie
				break;
			default:
				this.traiterMenuLeaderDemarre(c, cx);  // on recommence
				break;
		}
	}
	
	
	/**
	 * traite le choix du joueur si celui ci n'est pas le premier � jouer
	 * @param c
	 * @param cx
	 */
	public void traiterMenuJoueurDemarre(Client c, ConnexionClient cx){
		System.out.print(c.getJoueur().getNomJoueur() + " ("+c.getJoueur().getCodeCouleurJoueur()+") : ");
		int choix = c.choixMenuClient(6,6); // 6: menu joueur demarr�, 6: nombre choix max
		switch(choix){
			case 1:
				ArrayList<Integer> arr = new ArrayList<>();
				arr = c.getJoueur().choisirValeursEnsuite(c.getPartie().getDernierNbrAnnonce(), c.getPartie().getDernierDeAnnonce());
				if(arr.get(0) == 99){
					System.out.println("\nErreur Saisie Valeurs");
					this.traiterMenuJoueurDemarre(c, cx);
					break;
				}
				c.getJoueur().setMyTurn(false);
				c.envoyer(cx,this.surencherir(arr));
				break;
			case 2:
				c.envoyer(cx, this.liar());
				break;
			case 3:
				c.envoyer(cx, this.ttPile());
				break;
			case 4:
				System.out.println("Voici vos d�s:\n" + c.getJoueur().voirDes());
				this.traiterMenuJoueurDemarre(c, cx);
				break;
			case 5:
				c.envoyer(cx, this.listPlayers());
				break;
			case 6:
				c.envoyer(cx, this.leaveParty());  //quitter partie
				break;
			default:
				this.traiterMenuJoueurDemarre(c, cx);
				break;
		}
	}
	/******************************************************************/
	
	/*  PDUS pr�tes pour envoyer � l'appel de m�thode */
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
