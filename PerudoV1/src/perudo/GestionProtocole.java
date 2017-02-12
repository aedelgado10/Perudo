package perudo;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;

public class GestionProtocole extends PDU{
	
	public String traiter(String cmd,Socket client,Partie p){
		
		/* Décomposition de la commande en requete et arguments */
		ArrayList<String> req_args = this.decomposer(cmd);
		String requete = req_args.get(0);
		
		if (p.partieExists()){
			switch(p.getStatus()){
			
				case PDU.WAITING:
					
					switch(requete){
						
						case JOIN_PARTY:
							
							String resultat = p.rejoindrePartie(client);
							if (resultat.equals("0")){
								return JOIN_REJ;
							}else{
								return resultat;
							}
							
						case GET_ID:
							if (p.isDejaDansPartie(client)){
								return ID + " " + p.getListeJoueurs().get(client).getID();
							}else{
								return WHATSUP + " il faut d'abord rejoindre la partie!";
							}
						
						case CREATE_PARTY:
							return REJPARTY;
						
						case PSEUDOP:
							if (req_args.size() != 2){
								return PSEUDO_KO;
							}else{
								p.getJoueur(client).setPseudo(req_args.get(1));
								return PSEUDO_OK;
							}
							
						case LISTROOMS:
							return ROOMS + " " + p.getID()+ ":"+ p.getStatus() + ":"+ p.getNbJoueurs()+ "/6";
							
						case WHICH_COLOR:
							if (p.isDejaDansPartie(client)){
								if (!p.hasColor(p.getJoueur(client))){
									return COLOR + " " + p.setCouleur(p.getJoueur(client));	
								}else{
									return COLOR + " " + p.getJoueur(client).getCouleur().getCodeCouleur();
								}
							}else{
								return WHATSUP + " Vous n'êtes pas dans la partie !";
							}
							
						case LAUNCH:
							if (p.isCreateur(p.getJoueur(client))){
								if ((p.isFriendlyParty())){	// Il y a Joueurs > 1 dans la partie
									Enumeration<Joueur> e = p.getListeJoueurs().elements();
									
									while (e.hasMoreElements()){
										if (e.nextElement().getCouleur().getCodeCouleur().equals("undefined")){
											return PLS;	//Joueur non prêt !
										}
									}
									return BEGIN_PARTY;
								}else{
									return NOFNP;	// <1 Joueur dans la partie
								}
							}else{
								return WHATSUP + " Vous n'avez pas le droit d'effectuer cette action!";
							}
						case QUIT:
							return QUIT;
						default:
							return WHATSUP;
					}
					
				case FULL:			//La partie est pleine
					return GestionProtocole.FULL;
				
				case PDU.PARTYPLAYING:// partie en cours!
					
					return GestionProtocole.PARTYPLAYING;
				
				default:
					return GestionProtocole.WHATSUP;
			}
		}else{	// Aucune partie n'a été créée
			switch(requete){
				case PDU.LISTROOMS:
					return GestionProtocole.NO_ROOMS;
				case PDU.CREATE_PARTY:
					p.creerPartie();
					p.ajouterJoueur(client);
					p.setCreateur(p.getJoueur(client));
					System.out.println("[GestionProtocole] Une partie vient d'être créée!");
					return OKPARTY + " " + p.getID();

				case PDU.GET_ID:
					System.out.println("[GestionProtocole] Un id a été demandé mais le client n'est pas dans une partie");
					return WHATSUP + " Il faut d'abord créer ou rejoindre la partie !";
				case QUIT:
					System.out.println("[GestionProtocole] Le client se déconnecte");
					return QUIT;

				default:
					System.out.println("[GestionProtocole] La demande n'a pas été comprise; aucune partie en cours");
					return GestionProtocole.WHATSUP;
			}
		}
	}
}
