import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import javax.swing.JLabel;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingConstants;


@SuppressWarnings("serial")
public class leggiLibro extends JDialog implements ActionListener{
	private final JPanel contentPanel = new JPanel();
	private JLabel titolo ;
	private JLabel autore ;
	private JLabel genere ;
	private Boolean letto = false;

	public leggiLibro() {
		super(Main.getPagina().getFrameParent(), Dialog.ModalityType.DOCUMENT_MODAL);
		initialize() ;
	}
	
	private void initialize(){
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(new Dimension(500, 160));
		setLocation(screenSize.width/2 - 500/2, screenSize.height/2 - 200/2);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[207.00,grow]", "[][][]"));
		
		titolo = new JLabel("");
		titolo.setHorizontalAlignment(SwingConstants.CENTER);
		titolo.setFont(new Font("Seravek", Font.PLAIN, 16));
		contentPanel.add(titolo, "cell 0 0");
		
		autore = new JLabel("");
		autore.setHorizontalAlignment(SwingConstants.CENTER);
		autore.setFont(new Font("Seravek", Font.PLAIN, 16));
		contentPanel.add(autore, "cell 0 1");
		
		genere = new JLabel("");
		genere.setHorizontalAlignment(SwingConstants.CENTER);
		genere.setFont(new Font("Seravek", Font.PLAIN, 16));
		contentPanel.add(genere, "cell 0 2");
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		JButton cancelButton = new JButton("Indietro");
		cancelButton.setFont(new Font("Seravek", Font.PLAIN, 16));
		cancelButton.setActionCommand("Indietro");
		cancelButton.addActionListener(this);
		buttonPane.add(cancelButton);
		
		JButton leggiButton = new JButton("Leggi");
		leggiButton.setFont(new Font("Seravek", Font.PLAIN, 16));
		leggiButton.setActionCommand("Leggi");
		leggiButton.addActionListener(this);
		buttonPane.add(leggiButton);
		getRootPane().setDefaultButton(leggiButton);
	}
	
	public void setLibro(String titolo, String autore, String genere){
		this.titolo.setText(titolo);
		this.autore.setText(autore);
		this.genere.setText(genere);
	}

	public Boolean isLibroLetto()
	{
		return this.letto;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand().equals("Leggi")) {
			this.letto = true;
			this.dispose();
		}
		
		if(e.getActionCommand().equals("Indietro")) {
			this.dispose();
		}
	}

}
