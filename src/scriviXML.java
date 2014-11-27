import java.io.FileOutputStream;
import java.util.LinkedList;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

public class scriviXML {
	private String fileUtenti = "Utenti_Registrati.xml";
	private LinkedList<utente> lista = new LinkedList<utente>();
	
	public scriviXML(LinkedList<utente> utenti){
		this.lista = utenti;
		try {
			saveConfig() ;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createNode(XMLEventWriter eventWriter, String name, String value) throws XMLStreamException {
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createDTD("\n");
		XMLEvent node_tab = eventFactory.createDTD("\t\t");
		
		eventWriter.add(node_tab);
		eventWriter.add(eventFactory.createStartElement("", "", name));
		eventWriter.add(eventFactory.createCharacters(value));
		eventWriter.add(eventFactory.createEndElement("", "", name));
		eventWriter.add(end);
	}
	
	public void saveConfig() throws Exception {
	    // create an XMLOutputFactory
	    XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
	    
	    // create XMLEventWriter
	    XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(new FileOutputStream(this.fileUtenti));
	    
	    // create an EventFactory
	    XMLEventFactory eventFactory = XMLEventFactory.newInstance();
	    XMLEvent end = eventFactory.createDTD("\n");
	    XMLEvent tab = eventFactory.createDTD("\t");
	    
	    // create and write Start Tag
	    eventWriter.add(eventFactory.createStartDocument());
	    eventWriter.add(end);
	    
	    eventWriter.add(eventFactory.createStartElement("", "", "Utenti_Registrati"));
	    eventWriter.add(end);
	    
	    for(int i=0; i<this.lista.size(); i++){
	    	utente u = this.lista.get(i) ;
	    	
	    	eventWriter.add(tab);
	        eventWriter.add(eventFactory.createStartElement("", "", "utente"));
	        eventWriter.add(end);
	        
	        // Write the different nodes
	        createNode(eventWriter, "Id", new Integer(u.getID()).toString());
	        createNode(eventWriter, "Username", u.getUser());
	        createNode(eventWriter, "Password", u.getPW());
	        
	        if(!u.getProfessione().equals(""))
	        	createNode(eventWriter, "Professione", u.getProfessione());
	        
	        if(u.getHobbies().size() > 0){
	        	String lista_hobbies = "" ;
	        	int j = 0 ;
	        	for(j=0 ; j < u.getHobbies().size()-1 ; j++){
	        		lista_hobbies = lista_hobbies + u.getHobbies().get(j) + "-";
	        	}
	        	lista_hobbies += u.getHobbies().get(j) ;
	        	createNode(eventWriter, "Hobbies", lista_hobbies);
	        }
	        
	        {
	        	String lista_generi = "" ;
	        	int k = 0 ;
	        	for(k=0 ; k < u.getGeneri_preferiti().size()-1 ; k++){
	        		lista_generi = lista_generi + u.getGeneri_preferiti().get(k) + "-" ;
	        	}
	        	lista_generi += u.getGeneri_preferiti().get(k) ;
	        	createNode(eventWriter, "Generi_preferiti", lista_generi);
	        }
	        
	        if(u.getLibri_letti().size() > 0){
	        	String lista_libri_letti = "" ;
	        	int j = 0;
	        	for(j = 0; j < u.getLibri_letti().size()-1 ; j++){
	        		lista_libri_letti = lista_libri_letti + new Long(u.getLibri_letti().get(j)).toString() + " " ;
	        	}
	        	lista_libri_letti += new Long(u.getLibri_letti().get(j)).toString() ;
	        	createNode(eventWriter, "Libri_letti", lista_libri_letti);
	        }
	        	
	        
	        eventWriter.add(tab);
	        eventWriter.add(eventFactory.createEndElement("", "", "utente"));
	        eventWriter.add(end);
	    }
	    
	    eventWriter.add(eventFactory.createEndElement("", "", "Utenti_Registrati"));
	    eventWriter.add(end);
	    
	    eventWriter.add(eventFactory.createEndDocument());
	    eventWriter.close();
	  }
}