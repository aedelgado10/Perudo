package perudo;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * 
 * @author Pereira William
 * @author Delgado Andr�s
 * @author Keraghel Nady
 * @version : 2
 * @
 */

public class PDU {
	
	/*PDU de cr�ation de partie */
	public static final String CREATE_PARTY = "CREATE_PARTY"; 		// Cr�er une partie
	public static final String OKPARTY = "OK_PARTY";					// Cr�ation partie avec succ�s(serveur)
	public static final String REJPARTY = "REJ_PARTY";				// Refuser cr�ation partie
	
	/* Rejoindre une partie */
	public static final String JOIN_PARTY = "JOIN_PARTY";			// Join partie accept�
	public static final String JOIN_OK = "JOIN_OK";					// Joindre partie
	public static final String JOIN_REJ = "JOIN_REJ";				// Join partie refus�
	public static final String PARTYPLAYING = "PARTYPLAYING";		// informer que la partie est en cours
	public static final String WAITING = "WAITING_PLAYERS";			// En attente de joueurs
	public static final String FULL = "FULL_PARTY";					// La partie est compl�te
	public static final String ALREADY = "ALREADY_IN";				// A d�j� rejoint la partie
	
	/* Annuler une partie */
	public static final String STOP_PARTY = "STOP_PARTY";			// Annuler la partie (� tout moment);
	public static final String PARTY_CANCELLED = "PARTY_CANCELLED"; // La partie a �t� annul�e
	
	/* Quitter une partie (en cours ou non) */
	public static final String LEAVE = "LEAVE_PARTY";				// Quitter la partie
	public static final String OK_LEAVE = "OK_LEAVE";				// RECU !
	
	/* Lister les salles */
	public static final String LISTROOMS = "LIST_ROOMS";				// Demander liste des salles
	public static final String ROOMS = "ROOMS"; 					// Retour de la liste des salles disponibles
	public static final String NO_ROOMS = "NO_ROOMS";
	
	/* Lancer une partie */
	public static final String LAUNCH = "LAUNCH";					// D�marrer partie
	public static final String BEGIN_PARTY = "BEGIN_PARTY";			//Partie lanc�e !
	public static final String NOFNP = "NO_FRIENDS_NO_PARTY";		// On d�marre pas une partie avec <2 joueurs
	public static final String PLS = "PLS";							// Le client n'est pas pr�t
	/* Lister les couleurs */
	public static final String WHICH_COLOR = "COLOR?";				// demander sa couleur
	public static final String COLOR = "COLOR";
	
	/* Lancer les d�s */
	public static final String ROLL = "ROLL";						// Lancer les d�s
	public static final String DICES = "DICES";						// valeurs des d�s
	
	/* Faire des annonces */
	public static final String TTPILE = "TTPILE";					// Dire un toutpile
	public static final String TTPILE_OK = "TTPILE_OK";				// R�pondre tout pile reussi
	public static final String TTPILE_NOK = "TTPILE_NOK";			// R�pondre ce n'est pas tout pile
	public static final String LIAR = "LIAR";						// Dire Menteur
	public static final String LIAR_OK = "LIAR_OK";					// Il ment 
	public static final String LIAR_NOK = "LIAR_NOK";				// Il mentait pas 
	public static final String PLUS = "PLUS";						// Surencherir
	public static final String APLUS = "APLUS";						// Annoncer quel jouer surench�re et de combien 
	
	/* Cr�er un pseudo */
	public static final String PSEUDOP = "PSEUDO";					// Etablir pseudo
	public static final String PSEUDO_OK = "PSEUDO_OK";				// Demande pseudo r�ussie
	public static final String PSEUDO_KO = "PSEUDO_KO";
	
	/* ID */
	public static final String GET_ID = "GET_ID"; 					// R�cup�rer ID	
	public static final String ID = "ID";							// ID x
	
	/* Demande incomprise */
	public static final String WHATSUP = "WHATSUP";					// Le serveur n'a pas compris
	
	/* Demande mal-plac�e */
	public static final String DEBUG_IT = "DEBUG_IT";				// Ca ne devrait pas arriver
	public static final String QUIT = "QUIT";
	
	public ArrayList<String> decomposer(String cmd){
		ArrayList<String> requete = new ArrayList<String>();
		StringTokenizer s = new StringTokenizer(cmd);
		
		while (s.hasMoreTokens()){
			requete.add(s.nextToken());
		}
		
		return requete;
	}
	
	public ArrayList<String> parseListRooms(String liste){
		
		ArrayList<String> roomInfo = new ArrayList<String>();
		StringTokenizer s = new StringTokenizer(liste, ":");
		
		while (s.hasMoreTokens()){
			roomInfo.add(s.nextToken());
		}
		
		return roomInfo;
	}
}
