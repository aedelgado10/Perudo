package perudo;

/**
 * 
 * @author Pereira-Delgado
 * @version 1
 *
 */
public abstract class CouleurAbstract {
	
	public static String listeCouleurs[] = {"RED","BLUE","GREEN","ORANGE","PURPLE","YELLOW"};
	
	private String codeCouleur;
	
	public CouleurAbstract(String code){
		this.codeCouleur = code;
	}
	
	public void setCodeCouleur(String code){
		this.codeCouleur = code;
	}
	
	public String getCodeCouleur(){
		return this.codeCouleur;
	}
	
	/*public static String listeCouleurs(){
		String listeCouleurs = "";
		
		for(int i=0;i<listeCouleurs.length();i++){
			listeCouleurs += Couleur.listeCouleurs[i];
		}
		
		return listeCouleurs;
	}*/

}