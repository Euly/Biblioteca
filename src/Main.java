import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

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
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
		FindBooks();
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
	
	
	public static void FindBooks(){
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		String libri_letti = "";
		String [] tutti_libri_letti ;
		HashMap<Long, Integer> dictionary = new HashMap<Long, Integer>();
		
		try {
		    builder = factory.newDocumentBuilder();
		    org.w3c.dom.Document document = builder.parse(new FileInputStream("Utenti_Registrati.xml"));
		    XPath xPath =  XPathFactory.newInstance().newXPath();
		    String expression = "/Utenti_Registrati/utente/Libri_letti";
		    NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);
		    for (int i = 0; i < nodeList.getLength(); i++) {
		        libri_letti += (nodeList.item(i).getFirstChild().getNodeValue() + " ");
		    }
		    
		} catch (ParserConfigurationException e) {
		    e.printStackTrace();  
		}
		catch (SAXException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		
		tutti_libri_letti = libri_letti.split(" ");
		System.out.println("Lunghezza: "+ tutti_libri_letti.length);
		
		for(int i = 0 ; i < tutti_libri_letti.length ; i++){
			Integer value = dictionary.get(Long.parseLong(tutti_libri_letti[i])) ;
			
			if(value == null)
				dictionary.put(Long.parseLong(tutti_libri_letti[i]), 1) ;
			else
				dictionary.put(Long.parseLong(tutti_libri_letti[i]), ++value) ;
		}
		
		TreeMap<Long, Integer> sortedDictionary = SortByValue(dictionary); 
		System.out.println("Libri: "+sortedDictionary);
		
		Object[] id =  sortedDictionary.keySet().toArray() ;
		
		if(sortedDictionary.size() <= 5)
			getPagina().setNumeroConsigli(sortedDictionary.size());
		else
			getPagina().setNumeroConsigli(5);
		
		for(int i = 0 ; i < sortedDictionary.size() && i < 5 ; i++) {
			Document d = utente.SearchID(Long.parseLong(id[i].toString()));
			
			getPagina().setConsiglio(i+1, d.get(IndexItem.KIND), d.get(IndexItem.AUTHOR), d.get(IndexItem.TITLE_REAL));
		
		}
		
	}
	
	public static TreeMap<Long, Integer> SortByValue (HashMap<Long, Integer> dictionary) {
		ValueComparator vc =  new ValueComparator(dictionary);
		TreeMap<Long,Integer> sortedMap = new TreeMap<Long,Integer>(vc);
		sortedMap.putAll(dictionary);
		return sortedMap;
	}
}


class ValueComparator implements Comparator<Long> {
	 
    HashMap<Long, Integer> map;
 
    public ValueComparator(HashMap<Long, Integer> base) {
        this.map = base;
    }
 
    public int compare(Long a, Long b) {
        if (map.get(a) >= map.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys 
    }
}
