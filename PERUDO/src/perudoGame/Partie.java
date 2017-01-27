package perudoGame;

/**
 * 
 * @author Pereira
 * extended by Delgado
 * @version 1
 *
 */
public class Partie {
	
	private int id;
	private String status;
	private String nom;
	
	public Partie(){
		
	}
	
	public int getIdPartie() {
		return id;
	}

	public void setIdPartie(int idPartie) {
		this.id = idPartie;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}
}