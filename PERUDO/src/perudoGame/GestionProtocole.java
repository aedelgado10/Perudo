package perudoGame;

import java.io.IOException;

public class GestionProtocole extends PDU{

	public GestionProtocole() {
	}
	
	public String traiter(String recu){
		
		switch (recu){
		
			case "GET_ID":
			
				break;
			
			default:
				return "HEY";
		}
		return "";
	}

}
