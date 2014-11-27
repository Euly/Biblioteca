import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.util.Version;

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
	private LinkedList<Long> libri_letti = new LinkedList<Long>() ;
	private LinkedList<String> generi_preferiti = new LinkedList<String>() ;
	private int [] punteggio_generi = new int[9];
	private LinkedList<Document> libri_consigliati = new LinkedList<Document>();
	
	
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
		
		/* Quando mi registro incremento di 3 il punteggio dei generi selezionati */
		for(int j = 0 ; j < generi_preferiti.size() ; j++){
			for(int k = 0 ; k < 9 ; k++){
				if(ORDINE_GENERI[k].equals(generi_preferiti.get(j)))
					punteggio_generi[k] = punteggio_generi[k] + 3 ;
			}
		}
		
		/* Se l'utente ha messo degli hobbies incremento di 2 il punteggio dei 
		 * generi associati*/
		for(int j = 0 ; j < hobbies.size() ; j++){
			for(int k = 0 ; k < 9 ; k++){
				if(ORDINE_GENERI[k].equals(getGenereHobbies(hobbies.get(j)))){
					punteggio_generi[k] = punteggio_generi[k] + 2 ;
				}
			}
		}
		
		/* Se l'utente ha messo una professione incremento di 1 il genere 
		 * associato*/
		if(!professione.equals("")){
			for(int k = 0 ; k < 9 ; k++){
				if(ORDINE_GENERI[k].equals(getGenereProfessione()))
					punteggio_generi[k] = punteggio_generi[k] + 1 ;
			}
		}
	}
	
	public int[] getPunteggiGenere() {
		return punteggio_generi;
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

	public LinkedList<Long> getLibri_letti() {
		return libri_letti;
	}

	public void setLibri_letti(LinkedList<Long> libri_letti) {
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
	
	public String getGenereHobbies(String singleHobbies){
		switch(singleHobbies){
			case "Animali":
			case "Computer": return "Scientifico";
			case "Antiquariato e restauro":
			case "Giardinaggio":
			case "Origami": return "Classici";
			case "Armi e tiro":
			case "Caccia": return "Horror";
			case "Collezionismo":
			case "Modellismo": return "Storico";
			case "Ricamo e cucito":
			case "Cucina": return "Ricette";
			case "Disegno e pittura":
			case "Giochi": return "Fantasy";
			case "Automobili":
			case "Moto":
			case "Sport": return "Giallo e Thriller";
			case "Fotografia":
			case "Pesca":
			case "Lettura": return "Avventura";
			case "TV":
			case "Musica": return "Fantascienza";
		}
		return "" ;
	}
	
	public static String[] getOrdineGeneri() {
		return ORDINE_GENERI;
	}
	
	public LinkedList<Document> getLibriConsigliati(){
		LinkedList<String> autori = new LinkedList<String>();
		ArrayList<Integer> pt_a = new ArrayList<Integer>();
		
		LinkedList<String> generi = new LinkedList<String>();
		ArrayList<Integer> pt_g = new ArrayList<Integer>();
		
		Document d1 = null, d2 = null ;
		
		/* Calcolo i punteggi delle tabelle autori e generi */
		for(int i = libri_letti.size()-1, a = 0, g = 0 ; i >= 0 ; i--){
			d1 = SearchID(libri_letti.get(i).toString()) ;
			int occorrenze_autore = 0 , occorrenze_genere = 0 ;
			int punteggio_autore = 0 , punteggio_genere = 0 ;
			
			if(d1 != null && !autori.contains(d1.get(IndexItem.AUTHOR))){
				occorrenze_autore ++ ;
				punteggio_autore += (i+1)*10;
				autori.add(a, d1.get(IndexItem.AUTHOR));
			}
			
			if(d1 != null && !generi.contains(d1.get(IndexItem.KIND))){
				occorrenze_genere ++ ;
				punteggio_genere += (i+1)*10;
				generi.add(g, d1.get(IndexItem.KIND));
			}
			
			/* Controllo gli elementi (0,i-1) per cercare eventuali occorrenze 
			 * di autori o generi ed aggiornare il punteggio dell'elemento i */
			for(int j = 0 ; j < i ; j++){
				d2 = SearchID(libri_letti.get(j).toString()) ;
				
				if(d2 != null && d1.get(IndexItem.AUTHOR).equals(d2.get(IndexItem.AUTHOR))){
					occorrenze_autore ++ ;
					punteggio_autore += (j+1)*10;
				}
				
				if(d2 != null && d1.get(IndexItem.KIND).equals(d2.get(IndexItem.KIND))){
					occorrenze_genere ++ ;
					punteggio_genere += (j+1)*10;
				}	
			}
			/* Aggiungo nella stessa posizione di autore il suo punteggio */
			pt_a.add(a++, occorrenze_autore*punteggio_autore);
			
			/* Aggiungo nella stessa posizione di genere il suo punteggio */
			pt_g.add(g++, occorrenze_genere*punteggio_genere);
		}
		
		/* ---------------------------PER ANNA------------------------------
		 * Ora ho una LinkedList con gli autori ed un ArrayList dei punteggi
		 * nella stessa posizione per facilitarne l'uso. La stessa cosa vale
		 * per i generi.
		 * 
		 * Manca la ricerca degli elementi massimi tra autore e genere e la 
		 * scelta della proposta di libri da ritornare in libri_consigliati
		 * (ora e' ancora a null, non ho inserito nessun document). */
	
		return libri_consigliati ;
	}
	
	private Document SearchID(String idLibro){
		String[] matchField = new String[] {IndexItem.ID};
		Query q;

		int hitsPerPage = 100;
		IndexReader reader = null;
		Document d = null;
		
		try {
			q = new MultiFieldQueryParser(Version.LUCENE_42, matchField, Main.getPagina().getAnalyzer()).parse(idLibro);
			reader = DirectoryReader.open(Main.getPagina().getSimpleIndexLucene()); 
			IndexSearcher searcher = new IndexSearcher(reader);
			TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
			searcher.search(q, collector);
			ScoreDoc[] results = collector.topDocs().scoreDocs;
			
			if(results.length > 0) {
				int docId = results[0].doc;
				d = searcher.doc(docId);
			}
			reader.close();
		} 
		catch (IOException e1) {e1.getMessage();} 
		catch(ParseException e2) {e2.getMessage();} 
		
		return d ;
	}
}
