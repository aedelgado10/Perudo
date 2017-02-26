import java.util.ArrayList;



/**
 * 
 * @author Pereira-Delgado
 * @version 1
 * 
 */

public class Gobelet{
	
	private Couleur color;
	ArrayList<DePerudo> listeDe;
	
	public Gobelet(Couleur c){
		this.color = c;
		this.listeDe = new ArrayList<DePerudo>();
		
		for(int i = 0; i < 5; i++){
			listeDe.add(new DePerudo(c));
		}
	}
	
	/*public void secouer(){
		for(DePerudo d : listeDe){
			d.genererValeur();
			d.setPerudo();
		}
	}*/
	
	public ArrayList<String> afficherDes(){
		
		ArrayList<String> des = new ArrayList<String>();
		
		for(DePerudo d : listeDe){
			des.add(d.getPerudo());
		}
		return des;
	}
	
	public Couleur getCouleur(){
		return this.color;
	}
	
	

	public ArrayList<DePerudo> getListeDe() {
		return this.listeDe;
	}
	
	public void setListeDe(ArrayList<DePerudo> p) {
		this.listeDe = p;
	}
	
	public void retirerDe(){
		int i = this.listeDe.size();
		if( i > 0 )
			this.listeDe.remove(i-1);
	}
	
	public void recupererDe(){
		int i = this.listeDe.size();
		if(i < 5)
			listeDe.add(new DePerudo(this.color));
	}
}
