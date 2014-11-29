import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import net.miginfocom.swing.MigLayout;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.store.FSDirectory;

public class pagina {
	JFrame frame;
	private JPanel panel, panel_1, panel_2 ;
	private JTextField campo_cerca;
	private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private ascoltatore listener ;
	private JLabel ciao, utente, accesso ;
	private JButton btnLogin ;
	private JRadioButton rdbtnTutto, rdbtnTitolo, rdbtnAutore ;
	private LinkedList<utente> utenti = new LinkedList<utente>() ;
	private int id = 0 ;
	private StandardAnalyzer analyzer;
	private FSDirectory simpleIndex, stemmingIndex;
	private JTable risultati;
	private JLabel info_ricerca;
	private JLabel num_risultati;
	private JLabel label_consigli;
	private JPanel consiglio1, consiglio2, consiglio3, consiglio4, consiglio5;
	private JLabel num_cons1, num_cons2, num_cons3, num_cons4, num_cons5;
	private JLabel c1_genere, c1_autore, c1_titolo;
	private JLabel c2_genere, c2_autore, c2_titolo;
	private JLabel c3_genere, c3_autore, c3_titolo;
	private JLabel c4_genere, c4_autore, c4_titolo;
	private JLabel c5_genere, c5_autore, c5_titolo;
	 
	
	public pagina() {
		listener = new ascoltatore() ;
		initialize();
	}

	private void initialize() {
		int dim [] = dimensioni() ;
		frame = new JFrame();
		frame.setBounds(20, 10, dim[0], dim[1]) ;
        frame.setLocation(screenSize.width/2 - dim[0]/2, screenSize.height/2 - (dim[1]/2 + 15));
        frame.setPreferredSize(new Dimension(dim[0],dim[1]));
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		initializePanel();
		initializePanel1();
		initializePanel2();
	}
	
	private void initializePanel(){
		panel = new JPanel();
		panel.setPreferredSize(new Dimension(1150, 200));
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new MigLayout("", "[][grow][][][][][]", "[][][79.00][40.00]"));
		
		ImageIcon titolo = new ImageIcon(this.getClass().getResource("titolo.png"));
		JLabel imglabel = new JLabel();
		imglabel.setIcon(titolo);
		panel.add(imglabel, "cell 1 0");
		
		ImageIcon trova = new ImageIcon(this.getClass().getResource("find.gif"));
		JButton find = new JButton("");
		find.setIcon(trova);
		panel.add(find, "cell 2 2");
		find.setActionCommand("Cerca");
		find.addActionListener(listener);
		
		btnLogin = new JButton("Login");
		btnLogin.setFont(new Font("Seravek", Font.PLAIN, 16));
		btnLogin.addActionListener(listener);
		panel.add(btnLogin, "cell 6 0,alignx center,aligny center");
		
		ciao = new JLabel("");
		ciao.setFont(new Font("Seravek", Font.PLAIN, 20));
		panel.add(ciao, "cell 4 0,alignx right,growy");
		
		utente = new JLabel("");
		utente.setForeground(new Color(0, 102, 255));
		utente.setFont(new Font("Seravek", Font.PLAIN, 20));
		panel.add(utente, "cell 5 0,alignx left,growy");
		
		accesso = new JLabel("Devi effettuare l'accesso.");
		accesso.setFont(new Font("Seravek", Font.PLAIN, 16));
		accesso.setForeground(Color.RED);
		panel.add(accesso, "cell 4 1 3 1,alignx right,aligny top");
		
		JLabel lblCerca = new JLabel("Cerca:");
		lblCerca.setFont(new Font("Seravek", Font.PLAIN, 16));
		panel.add(lblCerca, "cell 0 2");
		
		campo_cerca = new JTextField();
		panel.add(campo_cerca, "cell 1 2,growx");
		campo_cerca.setColumns(10);
		
		ButtonGroup group = new ButtonGroup();
		rdbtnTutto = new JRadioButton("Tutto");
		rdbtnTutto.setFont(new Font("Seravek", Font.PLAIN, 16));
		rdbtnTutto.addActionListener(listener);

		panel.add(rdbtnTutto, "cell 3 2");
		group.add(rdbtnTutto);
		
		rdbtnTitolo = new JRadioButton("Titolo");
		rdbtnTitolo.setFont(new Font("Seravek", Font.PLAIN, 16));
		rdbtnTitolo.addActionListener(listener);
		panel.add(rdbtnTitolo, "cell 4 2");
		group.add(rdbtnTitolo);
		
		rdbtnAutore = new JRadioButton("Autore");
		rdbtnAutore.setFont(new Font("Seravek", Font.PLAIN, 16));
		rdbtnAutore.addActionListener(listener);
		panel.add(rdbtnAutore, "cell 5 2");
		group.add(rdbtnAutore);
		
		info_ricerca = new JLabel("");
		info_ricerca.setForeground(new Color(0, 51, 0));
		info_ricerca.setFont(new Font("Seravek", Font.PLAIN, 14));
		panel.add(info_ricerca, "cell 1 3");
	}
	
	private void initializePanel1(){
		panel_1 = new JPanel();
		panel_1.setPreferredSize(new Dimension(900, 500));
		panel_1.setLayout(new MigLayout("insets 0 15 15 15", "[868px]", "[27.00][466px]"));
		frame.getContentPane().add(panel_1, BorderLayout.WEST);
		
		risultati = new JTable(new MyTableModel());
		risultati.setRowHeight(20);
		risultati.setPreferredSize(new Dimension(873, 450));
		risultati.setPreferredScrollableViewportSize(risultati.getPreferredSize());
		risultati.setFont(new Font("Seravek", Font.PLAIN, 18));
		risultati.addMouseListener(listener);
		
		num_risultati = new JLabel("");
		num_risultati.setFont(new Font("Seravek", Font.PLAIN, 16));
		panel_1.add(num_risultati, "cell 0 0,alignx right");
		
		JScrollPane scrollPane = new JScrollPane(risultati);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		panel_1.add(scrollPane, "cell 0 1,grow");
	}
	
	private void initializePanel2(){
		Border border = BorderFactory.createLineBorder(Color.black) ;
		
		panel_2 = new JPanel();
		panel_2.setPreferredSize(new Dimension(250,100));
		frame.getContentPane().add(panel_2, BorderLayout.EAST);
		panel_2.setLayout(new MigLayout("insets 0 15 15 15", "[233.00,grow]", "[20.00][grow][grow][grow][grow][grow]"));
		
		label_consigli = new JLabel("Altri utenti hanno letto");
		label_consigli.setForeground(Color.BLACK);
		label_consigli.setHorizontalAlignment(SwingConstants.CENTER);
		label_consigli.setFont(new Font("Seravek", Font.PLAIN, 20));
		panel_2.add(label_consigli, "cell 0 0,alignx center");
		
		consiglio1 = new JPanel();
		consiglio1.setBackground(new Color(204, 255, 204));
		consiglio1.setBorder(border);
		panel_2.add(consiglio1, "cell 0 1,grow");
		consiglio1.setLayout(new MigLayout("", "[25.00][]", "[][][]"));
		
		c1_genere = new JLabel("genere");
		consiglio1.add(c1_genere, "cell 1 0");
		
		c1_autore = new JLabel("autore");
		consiglio1.add(c1_autore, "cell 1 1");
		
		c1_titolo = new JLabel("titolo");
		consiglio1.add(c1_titolo, "cell 1 2");
		
		num_cons1 = new JLabel("1");
		num_cons1.setFont(new Font("Seravek", Font.PLAIN, 25));
		consiglio1.add(num_cons1, "cell 0 0 1 3,alignx left,aligny center");
		
		consiglio2 = new JPanel();
		consiglio2.setBackground(new Color(204, 255, 204));
		consiglio2.setBorder(border);
		panel_2.add(consiglio2, "cell 0 2,grow");
		consiglio2.setLayout(new MigLayout("", "[25.00][]", "[][][]"));
		
		c2_genere = new JLabel("genere");
		consiglio2.add(c2_genere, "cell 1 0");
		
		c2_autore = new JLabel("autore");
		consiglio2.add(c2_autore, "cell 1 1");
		
		c2_titolo = new JLabel("titolo");
		consiglio2.add(c2_titolo, "cell 1 2");
		
		num_cons2 = new JLabel("2");
		num_cons2.setFont(new Font("Seravek", Font.PLAIN, 25));
		consiglio2.add(num_cons2, "cell 0 0 1 3,aligny center");
		
		consiglio3 = new JPanel();
		consiglio3.setBackground(new Color(204, 255, 204));
		consiglio3.setBorder(border);
		panel_2.add(consiglio3, "cell 0 3,grow");
		consiglio3.setLayout(new MigLayout("", "[25.00][]", "[][][]"));
		
		c3_genere = new JLabel("genere");
		consiglio3.add(c3_genere, "cell 1 0");
		
		c3_autore = new JLabel("autore");
		consiglio3.add(c3_autore, "cell 1 1");
		
		c3_titolo = new JLabel("titolo");
		consiglio3.add(c3_titolo, "cell 1 2");
		
		num_cons3 = new JLabel("3");
		num_cons3.setFont(new Font("Seravek", Font.PLAIN, 25));
		consiglio3.add(num_cons3, "cell 0 0 1 3,aligny center");
		
		consiglio4 = new JPanel();
		consiglio4.setBackground(new Color(204, 255, 204));
		consiglio4.setBorder(border);
		panel_2.add(consiglio4, "cell 0 4,grow");
		consiglio4.setLayout(new MigLayout("", "[25.00][]", "[][][]"));
		
		c4_genere = new JLabel("genere");
		consiglio4.add(c4_genere, "cell 1 0");
		
		c4_autore = new JLabel("autore");
		consiglio4.add(c4_autore, "cell 1 1");
		
		c4_titolo = new JLabel("titolo");
		consiglio4.add(c4_titolo, "cell 1 2");
		
		num_cons4 = new JLabel("4");
		num_cons4.setFont(new Font("Seravek", Font.PLAIN, 25));
		consiglio4.add(num_cons4, "cell 0 0 1 3,aligny center");
		
		consiglio5 = new JPanel();
		consiglio5.setBackground(new Color(204, 255, 204));
		consiglio5.setBorder(border);
		panel_2.add(consiglio5, "cell 0 5,grow");
		consiglio5.setLayout(new MigLayout("", "[25.00][]", "[][][]"));
		
		c5_genere = new JLabel("genere");
		consiglio5.add(c5_genere, "cell 1 0");
		
		c5_autore = new JLabel("autore");
		consiglio5.add(c5_autore, "cell 1 1");
		
		c5_titolo = new JLabel("titolo");
		consiglio5.add(c5_titolo, "cell 1 2");
		
		num_cons5 = new JLabel("5");
		num_cons5.setFont(new Font("Seravek", Font.PLAIN, 25));
		consiglio5.add(num_cons5, "cell 0 0 1 3,aligny center");
	}
	
	public JFrame getFrameParent(){
		return frame;
	}
	
	public JPanel getPanel(){
		return panel ;
	}
	
	public JPanel getPanel_1(){
		return panel_1 ;
	}
	
	public JPanel getPanel_2(){
		return panel_2 ;
	}

	public static int [] dimensioni(){
		int dim[] = new int[2] ;
		dim[0] = (screenSize.width *90)/100;
		dim[1] = (screenSize.height *90)/100;
		return dim ;
	}
	
	public JLabel getLabelCiao(){
		return ciao ;
	}
	
	public void setLabelCiao(String s){
		ciao.setText(s);
	}
	
	public JLabel getLabelUtente(){
		return utente ;
	}
	
	public void setLabelUtente(String s){
		utente.setText(s);
	}
	
	public void setLabelAccesso(String s){
		accesso.setText(s);
	}
	
	@SuppressWarnings("deprecation")
	public void setLabelButton(String s){
		btnLogin.setLabel(s);
	}
	
	public JButton getLoginButton(){
		return btnLogin ;
	}
	
	public int getID(){
		return id ;
	}
	
	public void setID(int id){
		this.id = id ;
	}

	public LinkedList<utente> getUtenti() {
		return utenti;
	}
	
	public void addUtente(utente u){
		this.utenti.add(u);
		this.id++ ;
	}
	
	public void setUtenti(LinkedList<utente> utenti){
		this.utenti = utenti ;
	}

	public StandardAnalyzer getAnalyzer() {
		return analyzer;
	}

	public void setAnalyzer(StandardAnalyzer analyzer) {
		this.analyzer = analyzer;
	}

	public FSDirectory getSimpleIndexLucene() {
		return simpleIndex;
	}
	
	public FSDirectory getStemmingIndexLucene() {
		return stemmingIndex;
	}

	public void setSimpleIndexLucene(FSDirectory index) {
		this.simpleIndex = index;
	}
	
	public void setStemmingIndexLucene(FSDirectory index) {
		this.stemmingIndex = index;
	}
	
	public JTextField getCampo_cerca(){
		return campo_cerca ;
	}
	
	public JRadioButton getRdbtnTutto(){
		return rdbtnTutto ;
	}
	
	public JRadioButton getRdbtnTitolo(){
		return rdbtnTitolo ;
	}
	
	public JRadioButton getRdbtnAutore(){
		return rdbtnAutore ;
	}
	
	public ascoltatore getListener(){
		return listener ;
	}
	
	public JTable getTableRisultati(){
		return risultati ;
	}
	
	public JLabel getInfo_ricerca(){
		return info_ricerca ;
	}
	
	public void setInfo_ricerca(String text){
		this.info_ricerca.setText(text);
	}
	
	public void setNumeroRisultati(String text){
		this.num_risultati.setText(text);
	}
	
	public void setLabelConsigli(String text){
		this.label_consigli.setText(text);
	}
	
	/* Funzione per settare i consigli nei vari pannelli */
	public void setConsiglio(int consiglio, String genere, String autore, String titolo){
		switch(consiglio){
			case 1:
					this.c1_genere.setText(genere);
					this.c1_autore.setText(autore);
					this.c1_titolo.setText(titolo);
					break;
			case 2:
					this.c2_genere.setText(genere);
					this.c2_autore.setText(autore);
					this.c2_titolo.setText(titolo);
					break;
			case 3:
					this.c3_genere.setText(genere);
					this.c3_autore.setText(autore);
					this.c3_titolo.setText(titolo);
					break;
			case 4:
					this.c4_genere.setText(genere);
					this.c4_autore.setText(autore);
					this.c4_titolo.setText(titolo);
					break;
			case 5:
					this.c5_genere.setText(genere);
					this.c5_autore.setText(autore);
					this.c5_titolo.setText(titolo);
					break;
		}
	}
	
	/* Metto visible o no i pannelli dei consigli in base a quanti ne ho */
	public void setNumeroConsigli(int num){
		switch(num){
			case 1:	
					consiglio1.setVisible(true);
					consiglio2.setVisible(false);
					consiglio3.setVisible(false);
					consiglio4.setVisible(false);
					consiglio5.setVisible(false);
					break;
			case 2:
					consiglio1.setVisible(true);
					consiglio2.setVisible(true);
					consiglio3.setVisible(false);
					consiglio4.setVisible(false);
					consiglio5.setVisible(false);
					break;
			case 3:	
					consiglio1.setVisible(true);
					consiglio2.setVisible(true);
					consiglio3.setVisible(true);
					consiglio4.setVisible(false);
					consiglio5.setVisible(false);
					break;
			case 4:
					consiglio1.setVisible(true);
					consiglio2.setVisible(true);
					consiglio3.setVisible(true);
					consiglio4.setVisible(true);
					consiglio5.setVisible(false);
					break;
			case 5: 
					consiglio1.setVisible(true);
					consiglio2.setVisible(true);
					consiglio3.setVisible(true);
					consiglio4.setVisible(true);
					consiglio5.setVisible(true);
					break;
		}
	}
	
}
