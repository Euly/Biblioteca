
public class IndexItem {
	
	private Long id;
    private String titleFile;
    private String content;
    private String[] parts; //Lista contenente i diversi "campi" del titolo del file
    //Il titolo del file contiene: Autore, Titolo (da aggiungere il genere)
    private String author;
    private String title_real;
    private String title;
    private String kind;
    private String path;

    public static final String ID = "id";
    public static final String AUTHOR = "author";
    public static final String TITLE = "title";
    public static final String TITLE_REAL = "title_real";
    public static final String CONTENT = "content";
    public static final String KIND = "kind";
    public static final String PATH = "path";
	
	public IndexItem(Long id, String title, String content, String path) {
        this.id = id;
        this.path = path;
        this.titleFile = title;
        this.content = content;
        parts = titleFile.split("-");
        this.author = parts[0];
        this.title = parts[1];
        this.kind = parts[2];
        String[] kindExtension = this.kind.split("\\.");
        this.kind = kindExtension[0];
	}
	
	public Long getId(){
		return id;
	}
	 
	public String getPath(){
		return path;
	}

	public void setTitleReal(String real){
		this.title_real = real;
	}
	
	public String getTitleReal(){
		return title_real;
	}
	
	public String getTitle(){
		return title;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
    public String getContent(){
        return content;
    }
    
    public void setContent(String content){
		this.content = content;
	}
    
    public String getAuthor(){
        return author;
    }
    
    public void setAuthor(String author){
		this.author = author;
	}
    
    public String getKind(){
        return kind;
    }
    
    @Override
    public String toString(){
        return "IndexItem{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", title='" + titleFile + '\'' +
                ", kind='" + kind + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}