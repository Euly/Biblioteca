import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.LinkedList;

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
		
		panel = new JPanel();
		panel.setPreferredSize(new Dimension(1150, 200));
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new MigLayout("", "[][grow][][][][][]", "[][][][]"));
		
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
		
		panel_1 = new JPanel();
		panel_1.setPreferredSize(new Dimension(900, 500));
		frame.getContentPane().add(panel_1, BorderLayout.WEST);
		panel_1.setLayout(new MigLayout("", "[grow]", "[grow]"));
		
	
		risultati = new JTable(new MyTableModel());
		risultati.setRowHeight(20);
		risultati.setPreferredSize(new Dimension(873, 450));
		risultati.setPreferredScrollableViewportSize(risultati.getPreferredSize());
		risultati.setFont(new Font("Seravek", Font.PLAIN, 18));
		risultati.addMouseListener(listener);
		
		JScrollPane scrollPane = new JScrollPane(risultati);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		panel_1.add(scrollPane, "cell 0 0,grow");
		
		
		panel_2 = new JPanel();
		panel_2.setPreferredSize(new Dimension(250,100));
		frame.getContentPane().add(panel_2, BorderLayout.EAST);
		panel_2.setLayout(new MigLayout("", "[grow]", "[grow]"));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		panel_2.add(scrollPane_1, "cell 0 0,grow");
	}
	
	public JFrame getFrameParent()
	{
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
}
