import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class login extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JPasswordField passwordField;
	private JTextField textField;
	private ascoltatore listener = new ascoltatore(this);
	private utente utente_loggato;
	
	public login() {
		initialize();
	}
	
	private void initialize(){
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(new Dimension(350, 260));
		setLocation(screenSize.width/2 - 350/2, screenSize.height/2 - 300/2);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[90.00][241.00,grow]", "[][][]"));
		
		JLabel lblLogin = new JLabel("Login");
		lblLogin.setFont(new Font("Seravek", Font.BOLD, 30));
		contentPanel.add(lblLogin, "cell 0 0 2 1,alignx center,aligny center");
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setFont(new Font("Seravek", Font.PLAIN, 20));
		contentPanel.add(lblUsername, "cell 0 1,alignx trailing");
			
		textField = new JTextField();
		contentPanel.add(textField, "cell 1 1,growx");
		textField.setColumns(10);		
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setFont(new Font("Seravek", Font.PLAIN, 20));
		contentPanel.add(lblPassword, "cell 0 2,alignx trailing");
		
		passwordField = new JPasswordField(10);
		contentPanel.add(passwordField, "cell 1 2,growx");
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
			
		JButton loginButton = new JButton("Login");
		loginButton.setActionCommand("Accedi");
		loginButton.addActionListener(listener) ;

		buttonPane.add(loginButton);
		getRootPane().setDefaultButton(loginButton);
		
		JPanel registrazione = new JPanel();
		registrazione.setPreferredSize(new Dimension(350, 65));
		getContentPane().add(registrazione, BorderLayout.NORTH);
		registrazione.setLayout(new MigLayout("", "[250.00][][150.00]", "[][]"));
		
		JLabel lblmsg = new JLabel("Non sei ancora registrato?");
		lblmsg.setFont(new Font("Seravek", Font.PLAIN, 18));
		registrazione.add(lblmsg, "flowy,cell 0 0");
		
		JButton btnRegistrati = new JButton("Registrati");
		btnRegistrati.addActionListener(listener);
		registrazione.add(btnRegistrati, "cell 1 0,alignx right");
		
		JLabel separatore = new JLabel("______________________________________________");
		registrazione.add(separatore, "cell 0 1 2 1");
	}
	
	public boolean autenticazione(String nome, String password){
		for(int i = 0; i < Main.getPagina().getUtenti().size(); i++){
			if (Main.getPagina().getUtenti().get(i).getUser().equals(nome) && 
					Main.getPagina().getUtenti().get(i).getPW().equals(password)){
				System.out.println("Sei un utente registrato.");
				System.out.println("Password inserita: "+ password);
				utente_loggato = Main.getPagina().getUtenti().get(i);
				return true ;
			}
		}
		
		return false;
	}
	
	public utente getUtenteLoggato() {
		return utente_loggato;
	}
	public JTextField getUser(){
		return textField ;
	}
	
	public JPasswordField getPW(){
		return passwordField ;
	}
}
