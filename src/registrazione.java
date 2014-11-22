import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class registrazione extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private ascoltatore listener = new ascoltatore(this) ;
	private login dialog = null ;
	private JTextField username;
	private JPasswordField password1;
	private JPasswordField password2;
	private JLabel invalid_username;
	private JLabel invalid_password;
	private JLabel generi3;
	private JLabel campi_obbligatori ;
	private JCheckBox ricette ;
	private JCheckBox classici ;
	private JCheckBox avventura ;
	private JCheckBox giallo_thriller ;
	private JCheckBox fantascienza ;
	private JCheckBox fantasy ;
	private JCheckBox horror ;
	private JCheckBox storico ;
	private JCheckBox scientifico ;
	private JComboBox<String> professione ;
	private JComboBox<String> hobby1;
	private JComboBox<String> hobby2;
	private JComboBox<String> hobby3;
	private JComboBox<String> hobby4;
	private JComboBox<String> hobby5;
	private LinkedList<String> hobbies_disponibili = new LinkedList<String>();

	/**
	 * Create the dialog.
	 */
	public registrazione(login dialog) {
		this.dialog = dialog ;
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(new Dimension(650, 550));
		setLocation(screenSize.width/2 - 650/2, screenSize.height/2 - 650/2);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][327.00,grow][132.00]", "[][][][][][][][][][grow]"));
		
		JLabel lblNome = new JLabel("* Scegli un nome utente:");
		lblNome.setFont(new Font("Seravek", Font.PLAIN, 18));
		contentPanel.add(lblNome, "flowx,cell 0 0");
		
		username = new JTextField();
		contentPanel.add(username, "cell 1 0,growx");
		username.setColumns(10);
		
		invalid_username = new JLabel("Username non disponibile");
		invalid_username.setForeground(SystemColor.window);
		invalid_username.setFont(new Font("Seravek", Font.PLAIN, 14));
		contentPanel.add(invalid_username, "cell 2 0");
		
		JLabel lblPassword1 = new JLabel("* Scegli una password:");
		lblPassword1.setFont(new Font("Seravek", Font.PLAIN, 18));
		contentPanel.add(lblPassword1, "cell 0 1");
		
		password1 = new JPasswordField();
		contentPanel.add(password1, "cell 1 1,growx");
		
		JLabel lblmaxCaratteri = new JLabel("(max 10 caratteri)");
		lblmaxCaratteri.setFont(new Font("Seravek", Font.PLAIN, 14));
		contentPanel.add(lblmaxCaratteri, "cell 2 1");
		
		JLabel lblPassword2 = new JLabel("* Ripeti password:");
		lblPassword2.setFont(new Font("Seravek", Font.PLAIN, 18));
		contentPanel.add(lblPassword2, "cell 0 2");
		
		password2 = new JPasswordField();
		contentPanel.add(password2, "cell 1 2,growx");
		
		invalid_password = new JLabel("La password non coincide");
		invalid_password.setForeground(SystemColor.window);
		invalid_password.setFont(new Font("Seravek", Font.PLAIN, 14));
		contentPanel.add(invalid_password, "cell 2 2");
		
		JLabel lblProfessione = new JLabel("Professione:");
		lblProfessione.setFont(new Font("Seravek", Font.PLAIN, 18));
		contentPanel.add(lblProfessione, "cell 0 3");
		
		professione = new JComboBox<String>();
		addProfessioni();
		professione.setEditable(false);
		professione.setFont(new Font("Seravek", Font.PLAIN, 18));
		contentPanel.add(professione, "cell 1 3,growx");
		
		JLabel lblHobby1 = new JLabel("Hobby 1:");
		lblHobby1.setFont(new Font("Seravek", Font.PLAIN, 18));
		contentPanel.add(lblHobby1, "cell 0 4");
		
		CreateListHobbies() ;
		
		hobby1 = new JComboBox<String>();
		hobby1.setActionCommand("hobby1");
		addHobbies(hobby1);
		hobby1.addActionListener(listener);
		contentPanel.add(hobby1, "cell 1 4,growx");
		
		JLabel lblHobby2 = new JLabel("Hobby 2:");
		lblHobby2.setFont(new Font("Seravek", Font.PLAIN, 18));
		contentPanel.add(lblHobby2, "cell 0 5");
		
		hobby2 = new JComboBox<String>();
		hobby2.setActionCommand("hobby2");
		addHobbies(hobby2);
		hobby2.addActionListener(listener);
		contentPanel.add(hobby2, "cell 1 5,growx");
		
		JLabel lblHobby3 = new JLabel("Hobby 3:");
		lblHobby3.setFont(new Font("Seravek", Font.PLAIN, 18));
		contentPanel.add(lblHobby3, "cell 0 6");
		
		hobby3 = new JComboBox<String>();
		hobby3.setActionCommand("hobby3");
		addHobbies(hobby3);
		hobby3.addActionListener(listener);
		contentPanel.add(hobby3, "cell 1 6,growx");
		
		JLabel lblHobby4 = new JLabel("Hobby 4:");
		lblHobby4.setFont(new Font("Seravek", Font.PLAIN, 18));
		contentPanel.add(lblHobby4, "cell 0 7");
		
		hobby4 = new JComboBox<String>();
		hobby4.setActionCommand("hobby4");
		addHobbies(hobby4);
		hobby4.addActionListener(listener);
		contentPanel.add(hobby4, "cell 1 7,growx");
		
		JLabel lblHobby5 = new JLabel("Hobby 5:");
		lblHobby5.setFont(new Font("Seravek", Font.PLAIN, 18));
		contentPanel.add(lblHobby5, "cell 0 8");
		
		hobby5 = new JComboBox<String>();
		hobby5.setActionCommand("hobby5");
		addHobbies(hobby5);
		hobby5.addActionListener(listener);
		contentPanel.add(hobby5, "cell 1 8,growx");
		
		JPanel generi = new JPanel();
		generi.setBorder(new EmptyBorder(0,0,0,0));
		contentPanel.add(generi, "cell 0 9 3 1,grow");
		generi.setLayout(new MigLayout("insets 0, wrap", "[][150.00,shrink 0][150.00,shrink 0][150.00]", "[][][]"));
		
		
		JLabel lblGeneri = new JLabel("* Generi preferiti:");
		generi.add(lblGeneri, "cell 0 0");
		lblGeneri.setFont(new Font("Seravek", Font.PLAIN, 18));
		
		ricette = new JCheckBox("Ricette");
		ricette.setFont(new Font("Seravek", Font.PLAIN, 18));
		ricette.addActionListener(listener);
		generi.add(ricette, "cell 1 0");
		
		classici = new JCheckBox("Classici");
		classici.setFont(new Font("Seravek", Font.PLAIN, 18));
		classici.addActionListener(listener);
		generi.add(classici, "cell 2 0");
		
		fantasy = new JCheckBox("Fantasy");
		fantasy.setFont(new Font("Seravek", Font.PLAIN, 18));
		fantasy.addActionListener(listener);
		generi.add(fantasy, "cell 3 0");
		
		generi3 = new JLabel("(sceglierne almeno 3)");
		generi3.setFont(new Font("Seravek", Font.PLAIN, 14));
		generi.add(generi3, "cell 0 1");
		
		fantascienza = new JCheckBox("Fantascienza");
		fantascienza.setFont(new Font("Seravek", Font.PLAIN, 18));
		fantascienza.addActionListener(listener);
		generi.add(fantascienza, "cell 1 1");
		
		giallo_thriller = new JCheckBox("Giallo e Thriller");
		giallo_thriller.setFont(new Font("Seravek", Font.PLAIN, 18));
		giallo_thriller.addActionListener(listener);
		generi.add(giallo_thriller, "cell 2 1");
		
		storico = new JCheckBox("Storico");
		storico.setFont(new Font("Seravek", Font.PLAIN, 18));
		storico.addActionListener(listener);
		generi.add(storico, "cell 3 1");
		
		avventura = new JCheckBox("Avventura");
		avventura.setFont(new Font("Seravek", Font.PLAIN, 18));
		avventura.addActionListener(listener);
		generi.add(avventura, "cell 1 2");
		
		horror = new JCheckBox("Horror");
		horror.setFont(new Font("Seravek", Font.PLAIN, 18));
		horror.addActionListener(listener);
		generi.add(horror, "cell 2 2");
		
		scientifico = new JCheckBox("Scientifico");
		scientifico.setFont(new Font("Seravek", Font.PLAIN, 18));
		scientifico.addActionListener(listener);
		generi.add(scientifico, "cell 3 2");
		
		JPanel buttonPane = new JPanel();
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		buttonPane.setLayout(new MigLayout("", "[][104px,grow]", "[29px]"));
		
		campi_obbligatori = new JLabel("* Campi obbligatori");
		campi_obbligatori.setForeground(Color.BLACK);
		campi_obbligatori.setFont(new Font("Seravek", Font.PLAIN, 14));
		buttonPane.add(campi_obbligatori, "cell 0 0");
			
		JButton regButton = new JButton("Registrati");
		regButton.setFont(new Font("Seravek", Font.PLAIN, 18));
		regButton.setActionCommand("RegistratiForm");
		regButton.addActionListener(listener);
		buttonPane.add(regButton, "cell 1 0,alignx right,aligny top");
		getRootPane().setDefaultButton(regButton);	
		
		JPanel titlePanel = new JPanel();
		getContentPane().add(titlePanel, BorderLayout.NORTH);
		titlePanel.setLayout(new MigLayout("", "[577.00,grow,center]", "[]"));
		
		JLabel lblRegistrazione = new JLabel("Registrazione");
		lblRegistrazione.setFont(new Font("Seravek", Font.BOLD, 30));
		titlePanel.add(lblRegistrazione, "cell 0 0");
	}
	
	private void addProfessioni(){
		String p[] = {"-- nessuna --", "Agente di Polizia", "Archeologo",
				"Avvocato", "Baby sitter", "Barista", "Cantante", "Cassiere/a",
				"Commesso/a", "Cuoco/a", "Direttore", "Disegnatore", "Docente",
				"Fisico","Fotografo", "Giornalista", "Impiegato/a", "Informatico/a",
				"Matematico", "Operaio/a", "Pasticcere/a", "Politico", "Regista",
				"Storico", "Studente", "Altro" } ;
		
		for(int i=0 ; i<p.length ; i++)
			professione.addItem(p[i]);
	}
	
	public String getProfessione(){
		return professione.getSelectedItem().toString() ;
	}
	
	private void CreateListHobbies(){
		String h[] = {"-- nessuno --","Animali", "Antiquariato e restauro",
				"Armi e tiro", "Automobili", "Caccia", "Collezionismo", "Cucina",
				"Computer", "Disegno e pittura", "Fotografia", "Giardinaggio",
				"Giochi", "Lettura", "Modellismo", "Moto", "Musica", "Origami",
				"Pesca", "Ricamo e cucito", "Sport", "TV"};
		
		for(int i=0 ; i<h.length ; i++)
			hobbies_disponibili.add(i, h[i]);
	}
	
	private void addHobbies(JComboBox<String> jcb){
		for(int i=0 ; i<hobbies_disponibili.size() ; i++)
			jcb.addItem(hobbies_disponibili.get(i));
	}
	
	public LinkedList<String> getHobbiesDisponibili(){
		return hobbies_disponibili ;
	}
	
	public JLabel getInvalidUsername(){
		return invalid_username ;
	}

	public JLabel getInvalidPassword(){
		return invalid_password ;
	}
	
	public JLabel getGeneri3(){
		return generi3 ;
	}
	
	public JLabel getCampiObbligatori(){
		return campi_obbligatori ;
	}
	
	public JPasswordField getPassword1(){
		return password1 ;
	}
	
	public JPasswordField getPassword2(){
		return password2 ;
	}
	
	public login getDialog(){
		return dialog ;
	}
	
	public JTextField getUsername(){
		return username ;
	}
	
	public JCheckBox getCheckBox(String b){
		switch(b){
			case "Ricette" : return ricette ;
			case "Classici" : return classici ;
			case "Fantascienza" : return fantascienza ;
			case "Fantasy" : return fantasy ;
			case "Avventura" : return avventura ;
			case "Giallo e Thriller" : return giallo_thriller ;
			case "Horror" : return horror ;
			case "Storico" : return storico ;
			case "Scientifico" : return scientifico ;
		}
		return null ;
	}
	
	public JComboBox<String> getHobby(String h){
		switch(h){
			case "hobby1" : return hobby1 ;
			case "hobby2" : return hobby2 ;
			case "hobby3" : return hobby3 ;
			case "hobby4" : return hobby4 ;
			case "hobby5" : return hobby5 ;
		}
		return null ;
	}
	
}
