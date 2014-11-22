import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedList;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class leggiXML {
	static final String UTENTE = "utente" ;
	static final String ID = "Id";
	static final String USERNAME = "Username";
	static final String PASSWORD = "Password";
	static final String PROFESSIONE = "Professione";
	static final String HOBBIES = "Hobbies";
	static final String GENERI = "Generi_preferiti";
	
	private LinkedList<utente> utenti = new LinkedList<utente>();
	
	public leggiXML(){}
	
	public LinkedList<utente> readConfig(){
		try{
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			InputStream in = new FileInputStream("Utenti_Registrati.xml");
			XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
			utente u = null;
			
			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();
				
				/* Controllo il tag di inizio */
				if (event.isStartElement()) {
					StartElement startElement = event.asStartElement();
					
					/* Se c'e' un nuovo utente creo un nuovo oggetto */
					if (startElement.getName().getLocalPart().equals(UTENTE)) {
						u = new utente() ;
					}
					
					/* Setto l'id dell'utente */
					if (event.asStartElement().getName().getLocalPart().equals(ID)) {
						event = eventReader.nextEvent();
						u.setID((int) Integer.parseInt(event.asCharacters().getData()));
						continue;
					}				
					
					/* Setto lo username dell'utente */
					if (event.asStartElement().getName().getLocalPart().equals(USERNAME)) {
						event = eventReader.nextEvent();
			            u.setUser(event.asCharacters().getData());
			            continue;
					}
					
					/* Setto la password dell'utente */
					if (event.asStartElement().getName().getLocalPart().equals(PASSWORD)) {
						event = eventReader.nextEvent();
			            u.setPW(event.asCharacters().getData());
			            continue;
					}
					
					/* Setto la professione dell'utente */
					if (event.asStartElement().getName().getLocalPart().equals(PROFESSIONE)) {
						event = eventReader.nextEvent();
			            u.setProfessione(event.asCharacters().getData());
			            continue;
					}
					
					/* Setto gli hobbies dell'utente */
					if (event.asStartElement().getName().getLocalPart().equals(HOBBIES)) {
						event = eventReader.nextEvent();
						
						String [] h = event.asCharacters().getData().split("-") ;
						LinkedList<String> lh = new LinkedList<String>();
						
						for(int i=0 ; i<h.length ; i++){
							lh.add(i, h[i]);
						}
						
						u.setHobbies(lh);
			            continue;
					}
					
					/* Setto i generi preferiti dell'utente */
					if (event.asStartElement().getName().getLocalPart().equals(GENERI)) {
						event = eventReader.nextEvent();
						
						String [] g = event.asCharacters().getData().split("-") ;
						LinkedList<String> lg = new LinkedList<String>();
						
						for(int i=0 ; i<g.length ; i++){
							lg.add(i, g[i]);
						}
						
						u.setGeneri_preferiti(lg);
						u.inizializzoPunteggi();
			            continue;
					}
				} //Fine tag inizio
				
				/* Controllo il tag di chiusura */
				if (event.isEndElement()) {
					EndElement endElement = event.asEndElement();
					
					/* Se e' il tag della fine di un utente lo aggiungo alla lista */
					if (endElement.getName().getLocalPart().equals(UTENTE)) {
						 utenti.add(u) ;
			        }
			    } //Fine tag chiusura
			} //Fine ciclo while
		}
		catch(XMLStreamException e){
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return utenti ;
	}
}
