package perudoGameClient;



/**
 * 
 * @author Pereira
 * @version 1
 *
 */
public class Couleur {
	
	private String codeCouleur;
	
	public Couleur(String code){
		this.codeCouleur = code;
	}
	
	
	public void setCodeCouleur(String code){
		this.codeCouleur = code;
	}
	
	public String getCodeCouleur(){
		return this.codeCouleur;
	}
}
