import java.io.IOException;

import javax.swing.table.AbstractTableModel;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;

@SuppressWarnings("serial")
public class MyTableModel extends AbstractTableModel{
	ScoreDoc[] hits = null ;
	int countSimpleHits;

	public MyTableModel(){
		super();
	}
	
	@Override
	public int getRowCount() {
		hits = Main.getPagina().getListener().getRisultati() ;

		if(hits != null && hits.length > 0)
		{
			return hits.length;
		}
		return 0;
	}

	@Override
	public int getColumnCount() {
		return 3 ;
	}

	@Override
	public Object getValueAt(int row, int col) {
		//hits = Main.getPagina().getListener().getRisultati() ;
		countSimpleHits = Main.getPagina().getListener().getLengthSimpleResult();
		IndexReader readerSimple;
		IndexReader readerStemming;
		if(hits != null && hits.length > 0) {
			try {
				readerSimple = DirectoryReader.open(Main.getPagina().getSimpleIndexLucene());
				IndexSearcher searcherSimple = new IndexSearcher(readerSimple);
				readerStemming = DirectoryReader.open(Main.getPagina().getStemmingIndexLucene());
				IndexSearcher searcherStemming = new IndexSearcher(readerStemming); 
				//int countSimpleHits = Main.getPagina().getListener().getLengthSimpleResult();
				Document d;
				int docId = hits[row].doc;
				if(row < countSimpleHits)
					d = searcherSimple.doc(docId);
				else
					d = searcherStemming.doc(docId);
				
				readerSimple.close();
				readerStemming.close();
				
				switch(col){
					case 0:	return d.get(IndexItem.AUTHOR) ;
					case 1: return d.get(IndexItem.TITLE_REAL) ;
					case 2:	return d.get(IndexItem.KIND) ;
				}
				
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
		
		return null;
	}
	
	public String getColumnName(int col){
		switch(col){
			case 0:	return "Autore" ;
			case 1:	return "Titolo" ;
			case 2:	return "Genere" ;
		}
		return null ;
	}

}
