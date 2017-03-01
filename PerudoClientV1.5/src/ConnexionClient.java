
import java.io.*;
import java.net.*;

/**
 * 
 * @author Pereira
 *
 */

public class ConnexionClient implements Runnable{
	
	//Variable pour stocker le Socket
	private Socket sock;
	private Client cli;
	
	public ConnexionClient(Client cli){
		this.cli = cli;
	}
	
	public Socket getSocket(){
		return this.sock;
	}
	
	public Client getClient(){
		return this.cli;
	}
	
	//Cr√©e une connexion avec le serveur
	public void ConnecterServeur() throws IOException {
		this.sock = new Socket("172.16.48.92", 27016);
	}
	
	//Ferme la connexion de fa√ßon propre
	public void FermerConnexionServeur(){
		
		try{
			this.sock.close();
			
		 } 
		 catch (IOException ex){
			 System.err.println("Erreur : "+ex);
			 ex.printStackTrace();
		 }
	}
	
	//Recevoir des donn√©es from serveur
	public String recevoir(){
		
		BufferedReader fluxEntreeSocket;
		String reponse;
		if(this.sock != null){
			try{
				fluxEntreeSocket = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
				reponse = fluxEntreeSocket.readLine();
				return reponse;
			}
			catch(SocketException se){
				System.out.println("Socket fermÈe");
				return null;
			}
			catch (IOException ex){
				System.err.println("Erreur : "+ex);
				ex.printStackTrace();
			}
		}

		
		return null;
	}
	
	//Envoi un String au serveur
	public void envoyer(String pdu){
		
		PrintStream fluxSortieSocket;
		
		if(this.sock != null){
			try{
				fluxSortieSocket = new PrintStream(this.sock.getOutputStream());
				fluxSortieSocket.println(pdu);
			}
			catch (IOException ex){
				System.err.println("Erreur : "+ex);
				ex.printStackTrace();
			}
		}
	}

	@Override
	public void run(){
		String recu = null;
		while (this.sock != null){ 
			recu = this.recevoir();
			if(recu != null ){
				this.getClient().traiter(recu,this);
			}
			else{
				System.out.println("****************************");
				System.out.println("*Provided by:              *\n* AndrÈs E. DELGADO ANDRADE* \n* Nady KERAGHEL            * \n* William PEREIRA DO CARMO *");
				System.out.println("*                          *\n* M1 STRI 2017©            *");
				System.out.println("****************************\n");
				break;
			}
		}
	}
	
}