import java.awt.Color;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.tartarus.snowball.ext.ItalianStemmer;

public class ascoltatore implements ActionListener{
	private String [] message = {"Ricerca libera all'interno dei documenti.",
								 "Ricerca libera sul titolo del libro. Con \"\" ricerca esatta.",
								 "Ricerca libera sull'autore (nome o cognome) con suggerimenti in caso di errore di digitazione."} ;
	private login dialog = null ;
	private registrazione form = null ;
	private String password, username ;
	private LinkedList<JCheckBox> generi_preferiti = new LinkedList<JCheckBox>() ;
	private ScoreDoc[] hitsSimple = null ;
	private ScoreDoc[] hitsStemming = null ;
	private ScoreDoc[] hitsUnion;
	private String anna = "Patata" ;

	public ascoltatore(){
	}
	
	public ascoltatore(login dialog){
		this.dialog = dialog ;
	}
	
	public ascoltatore(registrazione form){
		this.form = form ;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand().equals("Login")){
			dialog = new login();
			dialog.setAlwaysOnTop(true);
			dialog.setVisible(true);
		}
		
		if(e.getActionCommand().equals("Logout")){
			Main.getPagina().setLabelCiao("");
			Main.getPagina().setLabelUtente("");
			Main.getPagina().setLabelAccesso("Devi effettuare l'accesso.");
			Main.getPagina().setLabelButton("Login");
		}
		
		if(e.getActionCommand().equals("Accedi")){
			username = dialog.getUser().getText() ;
			password = new String(dialog.getPW().getPassword()) ;
		
			if (dialog.autenticazione(username, password)){
				Main.getPagina().setLabelCiao("Ciao ");
				Main.getPagina().setLabelUtente(username+"!");
				Main.getPagina().setLabelAccesso("");
				Main.getPagina().setLabelButton("Logout");
				closeDialog();
			}
			else ; //manca finestra di errore
		}
		
		if(e.getActionCommand().equals("Registrati")){
			closeDialog();
			form = new registrazione(dialog) ;
			form.setAlwaysOnTop(true);
			form.setVisible(true);
		}
		
		if(e.getActionCommand().equals("RegistratiForm")){
			/* Controllo che le password non combacino */
			String p1, p2 ;
			p1 = new String(form.getPassword1().getPassword());
			p2 = new String(form.getPassword2().getPassword());
			if(!p2.equals(p1)){
				form.getInvalidPassword().setForeground(Color.RED);
				form.getCampiObbligatori().setForeground(Color.RED);
			}
			else{
				form.getInvalidPassword().setForeground(SystemColor.window);
			}
			
			/* Controllo il nome utente */
			boolean valid_name = true ;
			for(int i = 0 ; i < Main.getPagina().getUtenti().size() ; i++) {
				if(Main.getPagina().getUtenti().get(i).getUser().equals(form.getUsername().getText())){
					valid_name = false ;
					break ;
				}
			}
			
			if(!valid_name){
				form.getInvalidUsername().setForeground(Color.RED);
				form.getCampiObbligatori().setForeground(Color.RED);
			}
			else{
				form.getInvalidUsername().setForeground(SystemColor.window);
			}
			
			/* Controllo i generi preferiti */
			if(generi_preferiti.size()<3){
				form.getGeneri3().setForeground(Color.RED);
				form.getCampiObbligatori().setForeground(Color.RED);
			}
			else{
				form.getGeneri3().setForeground(Color.BLACK);
			}
			
			/* Se invece va tutto bene devo registrare l'utente */
			if(p2.equals(p1) && valid_name && (generi_preferiti.size() > 2)){
				form.getCampiObbligatori().setForeground(Color.BLACK);
				
				utente u = new utente(form.getUsername().getText(), 
									new String(form.getPassword1().getPassword()), 
									Main.getPagina().getID());
				
				/* Se l'utente ha inserito una professione la aggiungo */
				if(!form.getProfessione().equals("-- nessuna --"))
					u.setProfessione(form.getProfessione());
				
				/* Se l'utente ha inserito degli hobbies li aggiungo */
				for(Integer i = 1; i <= 5 ; i++){
					String hobby = form.getHobby("hobby"+i.toString()).getSelectedItem().toString() ;
					if(!hobby.equals("-- nessuno --"))
						u.addHobby(hobby) ;
				}
				
				/* Aggiungo i generi prefertiti dell'utente */
				for(int i = 0; i < generi_preferiti.size() ; i++){
					u.addGeneri_preferiti(generi_preferiti.get(i).getText());
				}
				
				/* Aggiorno la tabella dei punteggi per i consigli */
				u.inizializzoPunteggi();
				
				/* Aggiungo l'utente alla lista degli utenti e lo scrivo sul file XML*/
				Main.getPagina().addUtente(u);
				
				new scriviXML(Main.getPagina().getUtenti());
				
				closeDialog();
			}
		}
		
		/* Aggiorno la lista dei generi preferiti del candidato */
		if(e.getActionCommand().equals("Ricette") || 
		   e.getActionCommand().equals("Classici") ||
		   e.getActionCommand().equals("Fantasy") ||
		   e.getActionCommand().equals("Fantascienza") ||
		   e.getActionCommand().equals("Avventura") ||
		   e.getActionCommand().equals("Giallo e Thriller") ||
		   e.getActionCommand().equals("Horror") ||
		   e.getActionCommand().equals("Storico") ||
		   e.getActionCommand().equals("Scientifico")){
			
			JCheckBox jcb = form.getCheckBox(e.getActionCommand()) ;
			
			if(jcb.isSelected() && !generi_preferiti.contains(jcb))
				generi_preferiti.add(jcb) ;
				
			if(generi_preferiti.contains(jcb) && (jcb.isSelected()==false))
				generi_preferiti.remove(jcb) ;
	
		}
		
		/* Gestisco gli hobbies per evitare ripetizioni */
		if(e.getActionCommand().equals("hobby1") || 
		   e.getActionCommand().equals("hobby2") ||
		   e.getActionCommand().equals("hobby3") ||
		   e.getActionCommand().equals("hobby4") ||
		   e.getActionCommand().equals("hobby5")){
			
			JComboBox<String> jcb = form.getHobby(e.getActionCommand()) ;
			String scelta = (String) jcb.getSelectedItem() ;
			LinkedList<String> hobbies_disponibili = form.getHobbiesDisponibili() ;
			hobbies_disponibili.remove(scelta); 
			
			for(Integer i=1 ; i<=5 ; i++){
				JComboBox<String> altro_jcb = form.getHobby("hobby"+ i.toString()) ;
				if(altro_jcb != jcb){
					altro_jcb.removeItem(scelta);
				}
			}
		}
		
		if(e.getActionCommand().equals("Tutto") ||
		   e.getActionCommand().equals("Titolo") ||
		   e.getActionCommand().equals("Autore")){
			
			if(Main.getPagina().getRdbtnTutto().isSelected())
				Main.getPagina().setInfo_ricerca(message[0]);
			
			if(Main.getPagina().getRdbtnTitolo().isSelected())
				Main.getPagina().setInfo_ricerca(message[1]);
			
			if(Main.getPagina().getRdbtnAutore().isSelected())
				Main.getPagina().setInfo_ricerca(message[2]);
			
		}
		
		if(e.getActionCommand().equals("Cerca")){
			String[] matchField = new String[] {IndexItem.TITLE, IndexItem.AUTHOR, IndexItem.KIND, IndexItem.CONTENT};
			JTextField textField = Main.getPagina().getCampo_cerca() ;
			System.out.println("Testo cercato: " + textField.getText());
			String querystrTuttoSimple = textField.getText(); //Stringa semplice per la ricerca su Tutto
			String querystrStemming = getStemmingQuery(querystrTuttoSimple); //Stringa con stemming per la ricerca su Tutto e Titolo
			String querystrTitleSimple = textField.getText().replace("\"", ""); //Stringa semplice per la ricerca su Titolo
			int lengthSimple = 0;
			hitsSimple = null;
			hitsStemming = null;
			
			/* Per prima cosa devo capire se la ricerca e' da fare su tutto il testo
			 * oppure solo sul titolo o autore dei libri. */
			
			/* Ricerca su Tutto */
			if(Main.getPagina().getRdbtnTutto().isSelected()){
				Main.getPagina().setInfo_ricerca(message[0]);
				matchField = new String[] {IndexItem.TITLE, IndexItem.AUTHOR, IndexItem.KIND, IndexItem.CONTENT};
				
				hitsSimple = searchQuery(querystrTuttoSimple, Main.getPagina().getSimpleIndexLucene(), matchField, 0);
				hitsStemming = searchQuery(querystrStemming, Main.getPagina().getStemmingIndexLucene(), matchField, 0);
				
				System.out.println("hitsSimple: " + hitsSimple.length);
				System.out.println("hitsStemming: " + hitsStemming.length);
				hitsUnion = new ScoreDoc[0];
		
				if(hitsSimple != null){
					hitsUnion = hitsSimple;
					lengthSimple = hitsSimple.length;
				}
				if(hitsStemming != null){
					ScoreDoc[] hitsUnionNew = new ScoreDoc[hitsUnion.length+hitsStemming.length];
					for (int w = 0; w < hitsUnion.length; w++){
						hitsUnionNew[w] = hitsUnion[w];
					}
					
					hitsUnion = hitsUnionNew;
					int count = 0;
					for(int j = 0; j < hitsStemming.length; j++){
						IndexReader readerSimple;
						try {
							readerSimple = DirectoryReader.open(Main.getPagina().getSimpleIndexLucene());
							IndexSearcher searcherSimple = new IndexSearcher(readerSimple);
							
							IndexReader readerStemming = DirectoryReader.open(Main.getPagina().getStemmingIndexLucene());
							IndexSearcher searcherStemming = new IndexSearcher(readerStemming);
							
							int spia = 0;
							for(int k = 0; k < lengthSimple; k++) {
								if(searcherSimple.doc(k).get(IndexItem.TITLE_REAL)
										.equals(searcherStemming.doc(j)
										.get(IndexItem.TITLE_REAL))){
									spia = 1;
									break;
								}
							}
							
							if(spia == 0){
								hitsUnion[count+lengthSimple] = hitsStemming[j];
								count++;
							}
							
						} catch (IOException e1) {e1.getMessage();}
					}
					hitsUnionNew = new ScoreDoc[lengthSimple+count];
					for (int w = 0; w < hitsUnionNew.length; w++){
						hitsUnionNew[w] = hitsUnion[w];
					}
					hitsUnion = hitsUnionNew;
				}
				System.out.println("Lunghezza Union: " + hitsUnion.length);
			}
			/* Fine ricerca su Tutto */
			
			/* ************************ */
			
			/* Ricerca su Titolo */
			if(Main.getPagina().getRdbtnTitolo().isSelected()){
				Main.getPagina().setInfo_ricerca(message[1]);
				matchField = new String[] {IndexItem.TITLE};
				if(querystrTuttoSimple.equals(querystrTitleSimple)){
					//Ricerca senza virgolette, cioe' ricerca libera (con stemming) sul Titotlo
					hitsStemming = searchQuery(querystrStemming, Main.getPagina().getStemmingIndexLucene(), matchField, 0);
					System.out.println("hitsStemming: " + hitsStemming.length);
					if (hitsStemming != null)
						hitsUnion = hitsStemming;
				}
				else{
					//Ricerca con virgolette, cioe' ricerca esatta (senza stemming) su Titolo
					hitsSimple = searchQuery(querystrTitleSimple, Main.getPagina().getSimpleIndexLucene(), matchField, 1);
					System.out.println("hitsSimple: " + hitsSimple.length);
					
					if(hitsSimple != null)
						hitsUnion = hitsSimple;
				}
			}
			/* Fine ricerca su Titolo */
			
			/* ************************ */
			
			/* Ricerca su Autore */
			if(Main.getPagina().getRdbtnAutore().isSelected()){
				Main.getPagina().setInfo_ricerca(message[2]);
				matchField = new String[] {IndexItem.AUTHOR};
				hitsSimple = searchQuery(querystrTuttoSimple, Main.getPagina().getSimpleIndexLucene(), matchField, 2);
				hitsUnion = hitsSimple;
			}
			/* Fine ricerca su Autore */
			Main.getPagina().getTableRisultati().setPreferredSize(new Dimension(Main.getPagina().getTableRisultati().getPreferredSize().width, 20*getRisultati().length));
			Main.getPagina().getTableRisultati().setSize(new Dimension(Main.getPagina().getTableRisultati().getPreferredSize().width, 20*getRisultati().length));
			Main.getPagina().getTableRisultati().repaint();
		}
	}
	
	public void closeDialog(){
		if(dialog != null)
			dialog.dispose();
		
		if(form != null)
			form.dispose();
	}
	
	public int getLengthSimpleResult()
	{
		if(hitsSimple != null)
			return hitsSimple.length;
		return 0;
	}
	
	public ScoreDoc[] getRisultati(){
		if(hitsUnion != null){
			return hitsUnion ;
		}
		return null ;
	}
	
	public String getStemmingQuery(String textQuery){
		textQuery = textQuery.replace("'", " ");
		String[] testoCercato = textQuery.split(" ");
		String querystr = "";
		for(int i = 0; i < testoCercato.length; i++)
		{
			ItalianStemmer obj = new ItalianStemmer();
			obj.setCurrent(testoCercato[i].toLowerCase());
			obj.stem();
			querystr = querystr + " " + obj.getCurrent();
		}
		return querystr;
	}
	
	public ScoreDoc[] searchQuery(String querystr, FSDirectory dirMatch, String[] matchField, int tipoRicerca){
		ScoreDoc[] results = null;
		ScoreDoc[] resultsTitleSimple = null;
		int count = 0;
		try {
			Query q;
			if(tipoRicerca == 2) //Se sono qui sto facendo la ricerca per Autore e uso l'edit distance.
				q = new FuzzyQuery(new Term(matchField[0], querystr));
			else
				q = new MultiFieldQueryParser(Version.LUCENE_42, matchField, Main.getPagina().getAnalyzer()).parse(querystr);

			// 3. search
			int hitsPerPage = 100;
			IndexReader reader = null;
			try {
				reader = DirectoryReader.open(dirMatch); 
				IndexSearcher searcher = new IndexSearcher(reader);
				TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
				searcher.search(q, collector);
				results = collector.topDocs().scoreDocs;
				resultsTitleSimple = results;
				
				// 4. display results
				System.out.println("Found " + results.length + " hits.");
				
				for(int i=0; i < results.length; ++i) {
					int docId = results[i].doc;
					Document d = searcher.doc(docId);
					if(tipoRicerca == 1){
						System.out.println("Titolo reale: " + d.get(IndexItem.TITLE_REAL).toLowerCase());
						System.out.println("Query cercata: " + querystr.toLowerCase());
						//Se sono qui sto facendo la ricerca esatta per Titolo
						if((d.get(IndexItem.TITLE_REAL).toLowerCase()).contains(querystr.toLowerCase())){
							resultsTitleSimple[count] = results[i];
							count++;
						}
					}
					System.out.println((i + 1) + ". " + d.get(IndexItem.ID) + "\t" + d.get(IndexItem.TITLE_REAL));
				}
				
				// searcher can only be closed when there
				// is no need to access the documents any more. 
				
				reader.close();
			} catch (IOException e1) {e1.getMessage();} 
		} catch (ParseException e2) {e2.getMessage();}
		
		if(tipoRicerca == 1){
			results = new ScoreDoc[count];
			for(int i = 0; i < count; i++)
				results[i] = resultsTitleSimple[i];
		}
		return results;
	}
	
}
