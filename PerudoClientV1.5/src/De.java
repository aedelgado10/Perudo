import java.util.Random;



/**
 * 
 * @author Pereira-Delgado
 * @version 1
 *
 */
public class De {
	
	private Couleur color;
	private int derniereValeur;
	
	public De(Couleur c){
		this.color = c;
	}
	
	/*public void genererValeur(){
		
		Random r = new Random();
		this.derniereValeur = r.nextInt(6) + 1;
		
	}*/
	
	public void setDerniereValeur(int val){
		this.derniereValeur = val;
	}
	
	public int getDerniereValeur(){
		return this.derniereValeur;
	}
	
	public Couleur getCouleur(){
		return color;
	}
}
