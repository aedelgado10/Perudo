package perudoGameClient;

/**
 * 
 * @author Pereira-Delgado
 * @version 1
 *
 */
public abstract class Couleur {
	
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
