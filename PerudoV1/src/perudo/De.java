package perudo;

import java.util.Random;

/**
 * 
 * @author Pereira-Delgado
 * @version 1
 *
 */
public class De {
	
	private CouleurAbstract color;
	private int derniereValeur;
	
	public De(CouleurAbstract c){
		this.color = c;
	}
	
	public void genererValeur(){
		
		Random r = new Random();
		this.derniereValeur = r.nextInt(6) + 1;
		
	}
	
	public int getDerniereValeur(){
		return this.derniereValeur;
	}
	
	public CouleurAbstract getCouleur(){
		return color;
	}
}