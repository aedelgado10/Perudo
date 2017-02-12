package perudoGameClient;

import java.util.ArrayList;

/**
 * 
 * @author Pereira
 * @version 1 
 *
 */

public class GestionProtocoleClient extends PDU{
	
	ArrayList<String> positiveAnswers;
	
	public GestionProtocoleClient(){
		this.positiveAnswers = new ArrayList<String>();
		this.positiveAnswers.add(PDU.ROOMS);
		this.positiveAnswers.add(JOIN_OK);
		this.positiveAnswers.add(OK_LEAVE);
		this.positiveAnswers.add(BEGIN_PARTY);
		this.positiveAnswers.add(COLOR);
		this.positiveAnswers.add(PSEUDO_OK);
		this.positiveAnswers.add(TTPILE_OK);
		this.positiveAnswers.add(LIAR_OK);
		this.positiveAnswers.add(APLUS);
		this.positiveAnswers.add(ID);
		this.positiveAnswers.add(DICES);
		this.positiveAnswers.add(PARTY_CANCELLED);
		this.positiveAnswers.add(OKPARTY);
	}
	
	//après utilisation repAttendue, cette methode sert a savoir
	//si la reponse cohérente est donc positive ou negative
	public Boolean isPositive(String ipdu){
		int test;
		test = this.positiveAnswers.indexOf(ipdu);
		if(test == -1){
			return false;
		}
		else{
			return true;
		}
			
	}
	
	//Teste si la reponse a la demande est coherente
	public int repCoherente(ArrayList<String> rep, String ipdu){
		
		switch(ipdu){
			case PDU.LISTROOMS:
				//reponse compatible
				if(rep.get(0).equals(PDU.NO_ROOMS) || rep.get(0).equals(PDU.ROOMS)){
					return 1;
				}
				break;
			case PDU.JOIN_PARTY:
				//reponse compatible
				if(rep.get(0).equals(PDU.JOIN_OK) || rep.get(0).equals(PDU.JOIN_REJ)){
					return 1;
				}
				break;
			case PDU.LEAVE:
				//reponse compatible
				if(rep.get(0).equals(PDU.OK_LEAVE)){
					return 1;
				}
				break;
			case PDU.LAUNCH:
				//reponse compatible
				if(rep.get(0).equals(PDU.BEGIN_PARTY) || rep.get(0).equals(PDU.NOFNP) || rep.get(0).equals(PDU.PLS)){
					return 1;
				}
				break;
			case PDU.WHICH_COLOR:
				//reponse compatible
				if(rep.get(0).equals(PDU.COLOR)){
					return 1;
				}
				break;
			case PDU.PSEUDOP:
				//reponse compatible
				if(rep.get(0).equals(PDU.PSEUDO_OK) || rep.get(0).equals(PDU.PSEUDO_KO)){
					return 1;
				}
				break;
			case PDU.TTPILE:
				//reponse compatible
				if(rep.get(0).equals(PDU.TTPILE_OK) || rep.get(0).equals(PDU.TTPILE_NOK)){
					return 1;
				}
				break;
			case PDU.LIAR:
				//reponse compatible
				if(rep.get(0).equals(PDU.LIAR_OK) || rep.get(0).equals(PDU.LIAR_NOK)){
					return 1;
				}
				break;
			case PDU.PLUS:
				//reponse compatible
				if(rep.get(0).equals(PDU.APLUS)){
					return 1;
				}
				break;
			case PDU.GET_ID:
				//reponse compatible
				if(rep.get(0).equals(PDU.ID)){
					return 1;
				}
				break;
			case PDU.ROLL:
				//reponse compatible
				if(rep.get(0).equals(PDU.DICES)){
					return 1;
				}
				break;
			case PDU.STOP_PARTY:
				//reponse compatible
				if(rep.get(0).equals(PDU.PARTY_CANCELLED)){
					return 1;
				}
				break;
			case PDU.CREATE_PARTY:
				//reponse compatible
				if(rep.get(0).equals(PDU.OKPARTY) || rep.get(0).equals(PDU.REJPARTY)){
					return 1;
				}
				break;
			default:
				return 0;
		}
		return 0;
	}
	
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
	
	public String menteur(){
		return PDU.LIAR;
	}
	
	public String surencherir(){
		return PDU.PLUS;
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
	
}
