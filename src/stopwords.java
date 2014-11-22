import java.util.Arrays;
import java.util.List;

import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.util.Version;

public class stopwords {
	private List<String> stopwords ;
	private CharArraySet prova ;
	
	public stopwords(){
		String all_stopwords = "a b c d e f g h i j k l m n o p q r s t u v w x "
				+ "y z il lo la gli le un uno una di da in su per con tra fra al "
				+ "allo alla ai agli alle dal dallo dalla dai dagli dalle del "
				+ "dello della dei degli delle nel nello nella nei negli nelle "
				+ "sul sullo sulla sui sugli sulle davanti dietro stante durante "
				+ "sopra sotto salvo accanto avanti verso presso contro circa "
				+ "intorno fuori malgrado vicino lontano dentro indietro insieme "
				+ "assieme oltre senza attraverso nondimeno mio mia miei mie tuo "
				+ "tua tuoi tue suo sua suoi sue nostro nostra nostri nostre "
				+ "vostro vostra vostri vostre loro questo codesto cotesto quello "
				+ "ciò questa codesta cotesta quella io tu egli esso ella essa "
				+ "noi voi essi esse me mi te ti lui lei ce ci ve vi se si ne "
				+ "che colui colei cui chi sono sei è siamo siete sarebbe sarà "
				+ "essendo ho hai ha abbiamo avete hanno avrebbe avrà avendo "
				+ "avuto l un all dall dell sull nell quell c v po "
				+ "può potrà potrebbe potuto deve dovrà dovrebbe dovuto due tre "
				+ "quattro cinque sette otto nove dieci venti trenta quaranta "
				+ "cinquanta sessanta settanta ottanta novanta cento primo "
				+ "secondo terzo quarto quinto sesto settimo ottavo nono decimo "
				+ "ma però anzi tuttavia pure invece perciò quindi dunque "
				+ "pertanto ebbene orbene né neppure neanche nemmeno sia oppure "
				+ "ossia altrimenti cioè infatti invero difatti perché perc poiché "
				+ "giacché quando mentre finché finc affinché acciocché qualora purché "
				+ "sebbene quantunque benché nonostante come quasi fuorché tranne "
				+ "eccetto laddove ah oh eh orsù urrà ahimè suvvia basta insomma "
				+ "così qui qua lì là già allora prima dopo ora poi sempre mai "
				+ "presto tardi intanto frattanto talvolta spesso molto troppo "
				+ "poco più meno assai niente nulla alquanto altrettanto anche "
				+ "perfino persino altresì finanche abbastanza almeno ancora "
				+ "appunto attualmente certamente comunque altrove dove dovunque "
				+ "effettivamente forse generalmente inoltre insufficientemente "
				+ "inutilmente naturalmente no non nuovamente ovunque ovviamente "
				+ "piuttosto precedentemente probabilmente realmente realmente "
				+ "semplicemente sì solitamente soprattutto specificamente "
				+ "successivamente sufficientemente veramente lunedì martedì "
				+ "mercoledì giovedì venerdì sabato domenica gennaio febbraio "
				+ "marzo aprile maggio giugno luglio agosto settembre ottobre "
				+ "novembre dicembre alcune alcuni alcuno altri altro certo "
				+ "chiunque ciascuno molti nessun nessuno ogni ognuno parecchi "
				+ "parecchio pochi qualche qualcosa qualcuno qualunque tanto "
				+ "tutti tutto qual quale quali quanto anno bene cosa cose data "
				+ "esempio male scelta vero via aperto attuale breve chiuso corto "
				+ "differente difficile dissimile diverso entrambe entrambi esterno "
				+ "facile falso grande inusuale inutile lungo impossibile "
				+ "improbabile insolito insufficiente maggiore maggior minore "
				+ "minor piccolo pieno possibile probabile pronto semplice "
				+ "siffatto simile sufficiente usuale utile vuoto interno mediante "
				+ "modo ovvio precedente primi propri proprio prossimo reale "
				+ "scelto soli solito solo soltanto specifico stessi stesso "
				+ "subito successivo super tale totale totali uguale uguali "
				+ "ulteriore ultimi ultimo vari vario verso";
		
		String []s = all_stopwords.split(" ");
		stopwords = Arrays.asList(s) ;
		prova = new CharArraySet(Version.LUCENE_42, stopwords, true);
	}
	
	public CharArraySet getStopWords(){
		return prova ;
	}
}
