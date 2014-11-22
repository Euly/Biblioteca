import java.util.LinkedList;

public class utente { 
	
	private static final String [] ORDINE_GENERI = {"Ricette", 
													"Classici",
													"Fantascienza",
													"Fantasy",
													"Avventura",
													"Giallo e Thriller",
													"Horror",
													"Storico",
													"Scientifico"} ;
	private int id = 0 ;
	private String username = "";
	private String password = "";
	private String professione = "";
	private LinkedList<String> hobbies = new LinkedList<String>();
	private LinkedList<String> libri_letti = new LinkedList<String>() ;
	private LinkedList<String> generi_preferiti = new LinkedList<String>() ;
	private int [] punteggio_generi = new int[9];
	
	
	/* Costruttore usato se carico gli utenti registrati da file*/
	public utente(){}
	
	/* Costruttore usato quando aggiungo a runtime un utente */
	public utente(String username, String password, int id){
		this.username = username ;
		this.password = password ;
		this.id = id ;
	}
	
	public void inizializzoPunteggi(){
		for(int i = 0 ; i < 9 ; i++){
			punteggio_generi[i] = 0 ;
		}
		
		/* Quando mi registro incremento di 1 il punteggio dei generi selezionati */
		for(int j = 0 ; j < generi_preferiti.size() ; j++){
			String s = generi_preferiti.get(j) ;
			for(int k = 0 ; k < 9 ; k++){
				if(ORDINE_GENERI[k].equals(s))
					punteggio_generi[k] = punteggio_generi[k] + 3 ;
			}
		}
		
		for(int j = 0 ; j < hobbies.size() ; j++){
			String s = hobbies.get(j) ;
			for(int k = 0 ; k < 9 ; k++){
				
			}
		}
	}
	
	public int getID(){
		return id ;
	}
	
	public void setID(int id){
		this.id = id ;
	}
	
	public String getUser(){
		return username ;
	}
	
	public void setUser(String username){
		this.username = username ;
	}
	
	public String getPW(){
		return password ;
	}
	
	public void setPW(String password){
		this.password = password ;
	}

	public String getProfessione() {
		return professione;
	}

	public void setProfessione(String professione) {
		this.professione = professione;
	}

	public LinkedList<String> getHobbies() {
		return hobbies;
	}
	
	public void addHobby(String s){
		hobbies.add(s);
	}
	
	public void setHobbies(LinkedList<String> hobbies){
		this.hobbies = hobbies ;
	}

	public LinkedList<String> getLibri_letti() {
		return libri_letti;
	}

	public void setLibri_letti(LinkedList<String> libri_letti) {
		this.libri_letti = libri_letti;
	}

	public LinkedList<String> getGeneri_preferiti() {
		return generi_preferiti;
	}
	
	public void addGeneri_preferiti(String s){
		generi_preferiti.add(s);
	}

	public void setGeneri_preferiti(LinkedList<String> generi_preferiti) {
		this.generi_preferiti = generi_preferiti;
	}
	
	public String getGenereProfessione(){
		switch(professione){
			case "Archeologo":
			case "Matematico":
			case "Informatico":
			case "Docente":
			case "Fisico": return "Scientifico";
			case "Studente":
			case "Disegnatore":
			case "Baby sitter": return "Fantasy";
			case "Cuoco":
			case "Barista":
			case "Pasticciere": return "Ricette";
			case "Regista":
			case "Cantante": return "Fantascienza";
			case "Cassiere":
			case "Commesso": return "Avventura";
			case "Agente di polizia":
			case "Direttore": return "Giallo e Thriller";
			case "Fotografo":
			case "Giornalista": return "Classici";
			case "Impiegato":
			case "Operaio": return "Horror";
			case "Politico":
			case "Storico": return "Storico";
		}
		return "";
	}
	
	public LinkedList<String> getGenereHobbies(String singleHobbies){
		LinkedList<String> genereHobbies = new LinkedList<String>();
			
			switch(singleHobbies){
				case "Animali":
				case "Computer": genereHobbies.add("Scientifico");
				case "Antiquariato e restauro":
				case "Giardinaggio":
				case "Origami": genereHobbies.add("Classici");
				case "Armi e tiro":
				case "Caccia": genereHobbies.add("Horror");
				case "Collezionismo":
				case "Modellismo": genereHobbies.add("Storico");
				case "Ricamo e cucito":
				case "Cucina": genereHobbies.add("Ricette");
				case "Disegno e pittura":
				case "Giochi": genereHobbies.add("Fantasy");
				case "Automobili":
				case "Moto":
				case "Sport": genereHobbies.add("Giallo e Thriller");
				case "Fotografia":
				case "Pesca":
				case "Lettura": genereHobbies.add("Avventura");
				case "TV":
				case "Musica": genereHobbies.add("Fantascienza");
			}
		return genereHobbies;
	}
}
