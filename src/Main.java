import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
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
		System.out.println("PESCIOLINO\n");
		System.out.println("sono la Diana\n");
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
		
		doc.add(new StringField(pdfBook.ID, pdfBook.getId().toString(), Field.Store.YES));
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
			obj.setCurrent(allWords[i]);
			obj.stem();
			textReturn = textReturn + " " + obj.getCurrent();
		}
		return textReturn;
	}
	
	public static LinkedList<String> getAllAuthors()
	{
		return allAuthors;
	}
	
	public static String getCostanteIndex(){
		return INDEX_DIR_SIMPLE ;
	}
}
