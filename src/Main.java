import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQExpression;
import javax.xml.xquery.XQResultSequence;

import net.xqj.sedna.SednaXQDataSource;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.PDFTextStripper;
import org.tartarus.snowball.ext.ItalianStemmer;

public class Main {
	private static String fileUtenti = "Utenti_Registrati.xml";
	private static File file = new File(fileUtenti) ;
	private static pagina window ;
	private static final String INDEX_DIR_SIMPLE = "./simple_index";
	private static final String INDEX_DIR_STEMMING = "./stemming_index";
	private static LinkedList<String> allAuthors = new LinkedList<String>();
	
	public static void main(String[] args) {
		window = new pagina();
		inizializeBook();
		window.getRdbtnTutto().doClick();
		window.frame.setVisible(true);
				
		if(file.isFile()){
			System.out.println("Il file esiste.") ;
			leggiXML xml = new leggiXML() ;
			window.setUtenti(xml.readConfig());
		}
		else
			System.out.println("Il file non esiste.") ;

		//cercaLibriPiuLetti();
	}
	
	public static pagina getPagina(){
		return window ;
	}
	
	private static void inizializeBook() {
		/* Indico il path di dove si trovano i miei libri: */
		File pdfDirectory = new File("./Libri");
		stopwords s = new stopwords();
		File pathSimpleDirectory = new File(INDEX_DIR_SIMPLE);
		File pathStemmingDirectory = new File(INDEX_DIR_STEMMING);
		window.setAnalyzer(new StandardAnalyzer(Version.LUCENE_42, s.getStopWords())); // creo l'analyzer
		try {
			window.setSimpleIndexLucene(FSDirectory.open(pathSimpleDirectory));
			window.setStemmingIndexLucene(FSDirectory.open(pathStemmingDirectory));
		} catch (IOException e1) {e1.printStackTrace();} 
		
		/* Creo l'Index */
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_42, window.getAnalyzer());
		if(!pathSimpleDirectory.exists()) {
		//Scorro tutta la lista di libri all'interno della cartella
			for(File pdfFile : pdfDirectory.listFiles()){ 
				if(pdfFile.getName().endsWith(".pdf")){
					IndexWriter writerSimple;
					IndexWriter writerStemming;
					PDDocument doc;
					try {
						writerSimple = new IndexWriter(window.getSimpleIndexLucene(), config);
						writerStemming = new IndexWriter(window.getStemmingIndexLucene(), config);
						doc = PDDocument.load(pdfFile);
					
						if(!doc.isEncrypted()) {
							System.out.println("Sto elaborando... " + pdfFile.getName());
							
							String content = new PDFTextStripper().getText(doc);
							IndexItem pdfBookSimple = new IndexItem((long)pdfFile.getName().hashCode(), 
															pdfFile.getName(), content,
															pdfFile.getAbsolutePath());
							pdfBookSimple.setTitleReal(pdfBookSimple.getTitle());
							content = stemming(content);
							IndexItem pdfBookStemming = new IndexItem((long)pdfFile.getName().hashCode(), 
															pdfFile.getName(), content,
															pdfFile.getAbsolutePath());
							pdfBookStemming.setTitleReal(pdfBookStemming.getTitle());
							pdfBookStemming.setTitle(stemming(pdfBookStemming.getTitle()));
							addDoc(writerSimple, pdfBookSimple);
							addDoc(writerStemming, pdfBookStemming);
												
						}
					
						doc.close();
						writerSimple.close();
						writerStemming.close();
					} catch (IOException e) {e.getMessage();} 
				}
			}	 
		}
	}
	
	/* Crea l'index del documento */
	@SuppressWarnings("static-access")
	private static void addDoc(IndexWriter w, IndexItem pdfBook) throws IOException {
		Document doc = new Document();
		int spia = 0;
		
		doc.add(new LongField(pdfBook.ID, pdfBook.getId(), Field.Store.YES));
		doc.add(new StringField(pdfBook.PATH, pdfBook.getPath(), Field.Store.YES));
		doc.add(new TextField(pdfBook.TITLE, pdfBook.getTitle(), Field.Store.YES));
		doc.add(new StringField(pdfBook.TITLE_REAL, pdfBook.getTitleReal(), Field.Store.YES));
		doc.add(new TextField(pdfBook.AUTHOR, pdfBook.getAuthor(), Field.Store.YES));
		doc.add(new TextField(pdfBook.KIND, pdfBook.getKind(), Field.Store.YES));
		doc.add(new TextField(pdfBook.CONTENT, pdfBook.getContent(), Field.Store.YES));
		
		//Creo una lista di tutti gli autori presenti
		for(int i = 0; i < allAuthors.size(); i++)
		{
			if(allAuthors.get(i).equals(pdfBook.getAuthor()))
			{
				spia = 1;
				break;
			}
		}
		if(spia == 0)
			allAuthors.add(pdfBook.getAuthor());
		
		w.addDocument(doc);
	}
	
	private static String stemming(String content)
	{
		ItalianStemmer obj = new ItalianStemmer();
		String noPunteggiatura = content.replaceAll("[\\-][\\s]{1,2}", "");
		//noTrattino = content.replaceAll("[\\n]", " ");
		noPunteggiatura = noPunteggiatura.replaceAll("[^a-zA-Zèéòàìù\\s]", " ");
		noPunteggiatura = noPunteggiatura.replaceAll("[\\s+]", " ");
		String[] allWords = noPunteggiatura.split(" ");
		String textReturn = "";
		for(int i = 0; i < allWords.length; i++)
		{
			obj.setCurrent(allWords[i].toLowerCase());
			obj.stem();
			textReturn = textReturn + " " + obj.getCurrent();
		}
		return textReturn;
	}
	
	public static String getCostanteIndex(){
		return INDEX_DIR_SIMPLE ;
	}
	
public static void cercaLibriPiuLetti() {
		
		XQDataSource xqs = new SednaXQDataSource();
		String libri_letti = "";
		try {
			xqs.setProperty("serverName", "localhost");
			xqs.setProperty("databaseName", "test");
			XQConnection conn = xqs.getConnection("SYSTEM", "MANAGER");
			XQExpression xqe = conn.createExpression();
			
			String xqueryString = "for $x in doc('Utenti_Registrati.xml')//utente return $x/Libri_letti/text()";
			XQResultSequence rs = xqe.executeQuery(xqueryString);
			
			while(rs.next())
			{
				String libri = rs.getItemAsString(null);
				libri = libri.replace("{ ", "").replace("\"", "").replace(" }", "").replaceAll("(text )", "");
				libri_letti += " " + libri;
			}
			libri_letti = libri_letti.replaceAll("^[\\s]", "");
			System.out.println("Libri letti: " + libri_letti);
			String[] all_libri_letti = libri_letti.split(" ");
			Set<Long> id_all_libri_letti = new HashSet<Long>();
			for(int i = 0; i < all_libri_letti.length; i++) {
				id_all_libri_letti.add(Long.parseLong(all_libri_letti[i]));
			}
			Object[] id_libri_letti = id_all_libri_letti.toArray();
			LinkedList<Integer> count_libri_letti = new LinkedList<Integer>();
			
			for(int i = 0; i < id_libri_letti.length; i++) {
				xqe.bindString(new QName("idLibroLetto"), id_libri_letti[i].toString() , null);
				xqueryString = "declare variable $idLibroLetto external; " +
					    "for $x in doc('Utenti_Registrati.xml')//utente " +
					    "return $x/Libri_letti[contains(., $idLibroLetto)]/text()";
				
				rs = xqe.executeQuery(xqueryString);
				int count = 0;
				while(rs.next()) {
				      System.out.println(rs.getItemAsString(null));
				      count++;
				}
				count_libri_letti.add(count);	
			}
			
			
			/* ----------------------PER DIANA --------------------------
			 * Il for qui sotto serve per ordinare i libri letto per numero di libri letti uguali fra gli utenti.
			 * L'array id_libri_letti contiene l'id dei libri che hanno letto gli utenti e
			 * la LinkedList count_libri_letti contiene il numero di volte che compare quel libro (fa riferimento al libro alla stessa
			 * posizione dell'array detto prima.
			 * Quindi bisognerebbe ordinare l'array e la lista secondo count_libri_letti cosi' poi possiamo restituire
			 * tipo i primi 4 come libri del tipo "gli utenti registrati hanno letto bla bla bla bla".
			 * Al momento questa funzione viene chiamata SOLO nel Main. Poi dovremmo metterla sia nel Main sia nel Logout.
			 * Per il momento la riga nel Main dove viene richiamata (riga 52) l'ho commentata perchè per usare le XQuery devi fare
			 * un procedimento particolare che ti devo spiegare a voce altrimenti non si riesce ad usare le XQuery. 
			 * Però intanto se riesci ad ordinare i valori prima che lo faccio io se per caso ci lavori nel week end ti ho lascio questo
			 * commento, se non ci lavori lo faremo poi. :)
			 * Ah per il momento la funziona non ritorna nulla perchè devo ancora fare la parte di ricerca che dato l'id ti trova
			 * il Document. Dopo abbiamo già i 4 libri con più "Letto" da parte dell'utente.
			 */
			for(int i = 0; i < count_libri_letti.size(); i++) {
				System.out.println("Count di " + id_libri_letti[i] + " e' " + count_libri_letti.get(i));
			}
		} 
		catch (XQException e) { e.getMessage(); }
	}
}
