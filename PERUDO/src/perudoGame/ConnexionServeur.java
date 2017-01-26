package perudoGame;

import java.net.ServerSocket; 
import java.net.Socket; 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class ConnexionServeur implements Runnable{

	Socket client = null;
	
	/**
	 * @return socketService s
	 */
	public static ServerSocket ouvrir(int port){	
		try{
			ServerSocket sS = new ServerSocket(port);
			return sS;
		}
		catch(IOException e){
			e.printStackTrace();
			System.out.println("[Serveur] Probl�me au d�marrage du service");
			return null;
		}
	}
	
	public ConnexionServeur(ServerSocket s){
		super();
		this.attendreClient(s);
	}
	
	/**
	 * 
	 * @param s
	 * @return Socket client avec client connect�
	 */
	public void attendreClient(ServerSocket service){
			
		while (client == null){
			try {
				this.client = service.accept();
				System.out.println("[Serveur] Service actif");
			} catch (IOException e) {
				System.out.println("[Serveur] Impossible d'�tablir la cx avec un client. Il doit etre à La Poste");
			}
		}
	}
	
	/**
	 * 
	 * @param s
	 * @return String la requete
	 */
	public String recevoir(Socket client){
		
		BufferedReader in;
		
		if (client != null){
			try {
				in = new BufferedReader(new InputStreamReader(client.getInputStream()));
				String req = in.readLine();
				return req;
			} catch (IOException e) {
				System.out.println("[Serveur] Erreur lors de la r�ception ou � l'instantiation du flux d'entr�e");
				return "quit";
			}
		}
		return "quit";
	}
	
	public void envoyer(Socket client,String reponse){
		
		 try {
			PrintStream out = new PrintStream(client.getOutputStream());
			
			if (reponse != null){
				out.println(reponse);	
			}
		} catch (IOException e) {
			System.out.println("[Serveur] Erreur lors de l'envoi");
			try {
				this.client.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}catch(NullPointerException n){
			System.out.println("[Serveur] Il n'y a rien � r�pondre...");
		}
	}

	@Override
	public void run() {
		while (true){
			String recu = this.recevoir(this.client);
			if (recu.equals("quit")){
				try {
					this.client.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
			String reponse = "hey";
			this.envoyer(client,reponse);
		}
	}
}
