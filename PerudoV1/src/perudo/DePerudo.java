package perudo;

/**
 * 
 * @author Pereira
 * @version 1
 *
 */
public class DePerudo extends De{
	
	private String perudoDe;
	
	public DePerudo(CouleurAbstract c){
		super(c);
	}
	
	public void setPerudo(){
		
		int valeur;
		valeur = this.getDerniereValeur();
		
		this.perudoDe = (valeur == 1) ? "Perudo" : Integer.toString(valeur);		
	}
	
	public String getPerudo(){
		return this.perudoDe;
	}
	
}
