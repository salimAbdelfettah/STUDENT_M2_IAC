package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.text.DefaultCaret;


//< --- CLASS GRAPH PANEL --- >

class GraphPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8846224527026125404L;
	
	
	public void paintComponent(Graphics g){
		int x, y, g_x, g_y, z;
		//
		g_x=MainProgram.get_G_x();
		g_y=MainProgram.get_G_y();
		z=MainProgram.get_zoom();
		//Déciner le carré de contour
		g.drawRect(0, 0, this.getWidth()-1, this.getHeight()-1);
		if(MainProgram.get_Bs()!=null){
			for (int i=0;i<MainProgram.get_Bs().size();i++){
				if((MainProgram.get_Bs().get(i).get_enabled())&&(MainProgram.get_Bs().get(i).get_visible())){
					x=MainProgram.get_Bs().get(i).get_x();
					y=MainProgram.get_Bs().get(i).get_y();
					//Pour déciner le contour en noir
					g.setColor(Color.black);
					g.drawRect((x-g_x)*z, (y-g_y)*z, MainProgram.get_Bs().get(i).get_w()*z, MainProgram.get_Bs().get(i).get_h()*z);
					//Pour déciner le contenu en gris clair
					g.setColor(Color.LIGHT_GRAY);
					g.fillRect(((x-g_x)*z)+1, ((y-g_y)*z)+1, MainProgram.get_Bs().get(i).get_w()*z-1, MainProgram.get_Bs().get(i).get_h()*z-1);
				}
			}
		}
	}
}


//< --- CLASS MY LISTENER --- >

class MyListener implements ActionListener{
	
	private MainFrame MF;
	private MyMenuBar MMB;
	
	public MyListener(MainFrame mf){
		MF=mf;
	}
	
	public void set_MMB(MyMenuBar mmb){
		MMB=mmb;
	}
	
	private void canplay(boolean can){
		MMB.play.setEnabled(can);
		MF.play.setEnabled(can);
		MMB.stop.setEnabled(!can);
		MF.stop.setEnabled(!can);
		MMB.zoomIn.setEnabled(!can);
		MF.zoomIn.setEnabled(!can);
	}
	
	public void canZoom(boolean can){
		MMB.zoomOut.setEnabled(can);
		MMB.up.setEnabled(can);
		MMB.down.setEnabled(can);
		MMB.left.setEnabled(can);
		MMB.right.setEnabled(can);
		MF.zoomOut.setEnabled(can);
		MF.up.setEnabled(can);
		MF.down.setEnabled(can);
		MF.left.setEnabled(can);
		MF.right.setEnabled(can);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		Object S=arg0.getSource();
		if(S.equals(MMB.exit)){
			MainProgram.Close();
		}else if(S.equals(MMB.help)){
			String s="Pour lancer la simulation :\n";
			s=s+"1 - \t Ajuster la configuration* (nombre de batiments à générer, leur tailles, etc.)\n";
			s=s+"2 - \t Lancer la simulation (bouton \"Lancer\")\n";
			s=s+"3 - \t Zommer (\"+\", \"-\") et se déplacer (\"<\", \">\", \"^\", \"v\")\n";
			s=s+"4 - \t Terminer la simulation** (bouton \"Arrêter\")\n\n\n";
			s=s+"Remarques :\n";
			s=s+"* - \t Les valeurs doivent être entières positives et non nulles\n";
			s=s+"** - \t Un fichier .jlog est crée lors de la simulation et est disponible à la fin\n";
			s=s+"\t Il est de la forme 'sma_%Date%_%Heure%.jlog'\n";
			JOptionPane.showMessageDialog(null, s, "Aide", 1);
		}else if(S.equals(MMB.about)){
			JOptionPane.showMessageDialog(null, "TP Jade - SMA\nPar : ABDELFETTAH Salim\nM2R IAC U.Paris-Sud\n2013-2014", "A propos de l'application", 1);
		}else if((S.equals(MMB.play))||(S.equals(MF.play))){
			if((MF.get_min_w()>0)&&(MF.get_max_w()>0)&&(MF.get_min_h()>0)&&(MF.get_max_h()>0)&&(MF.get_max_nb()>0)&&(MF.get_max_d_x()>0)&&(MF.get_max_d_y()>0)){
				canplay(false);
				MainProgram.play();
				MF.canEdit(false);
			}
		}else if((S.equals(MMB.stop))||(S.equals(MF.stop))){
			MF.canEdit(true);
			canplay(true);
			canZoom(false);
			MainProgram.stop();
		}else if((S.equals(MMB.zoomIn))||(S.equals(MF.zoomIn))){
			MainProgram.set_zoom(MainProgram.get_zoom()*2);
			canZoom(true);
			MainProgram.compute_new_position(true);
			if(MainProgram.get_zoom()==4){
				MMB.zoomIn.setEnabled(false);
				MF.zoomIn.setEnabled(false);
			}
		}else if((S.equals(MMB.zoomOut))||(S.equals(MF.zoomOut))){
			MainProgram.set_zoom(MainProgram.get_zoom()/2);
			MMB.zoomIn.setEnabled(true);
			MF.zoomIn.setEnabled(true);
			MainProgram.compute_new_position(false);
			if(MainProgram.get_zoom()==1){
				canZoom(false);
			}
		}else if((S.equals(MMB.up))||(S.equals(MF.up))){
			MMB.down.setEnabled(true);
			MF.down.setEnabled(true);
			boolean b=MainProgram.up();
			MMB.up.setEnabled(!b);
			MF.up.setEnabled(!b);
		}else if((S.equals(MMB.down))||(S.equals(MF.down))){
			MMB.up.setEnabled(true);
			MF.up.setEnabled(true);
			boolean b=MainProgram.down();
			MMB.down.setEnabled(!b);
			MF.down.setEnabled(!b);
		}else if((S.equals(MMB.left))||(S.equals(MF.left))){
			MMB.right.setEnabled(true);
			MF.right.setEnabled(true);
			boolean b=MainProgram.left();
			MMB.left.setEnabled(!b);
			MF.left.setEnabled(!b);
		}else if((S.equals(MMB.right))||(S.equals(MF.right))){
			MMB.left.setEnabled(true);
			MF.left.setEnabled(true);
			boolean b=MainProgram.right();
			MMB.right.setEnabled(!b);
			MF.right.setEnabled(!b);
		}
	}
	
}


//< --- CLASS MY MENU BAR --- >

class MyMenuBar extends JMenuBar{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9155876696531288582L;
	
	private JMenu files=new JMenu("Fichier");
	private JMenu actions=new JMenu("Actions");
	private JMenu intero=new JMenu("?");
	JMenuItem exit=new JMenuItem("Quitter");
	JMenuItem play=new JMenuItem("Lancer");
	JMenuItem stop=new JMenuItem("Arrêter");
	JMenuItem zoomIn=new JMenuItem("Agrandir");
	JMenuItem zoomOut=new JMenuItem("Réduire");
	JMenuItem up=new JMenuItem("Aller en haut");
	JMenuItem down=new JMenuItem("Aller en bas");
	JMenuItem left=new JMenuItem("Aller à gauche");
	JMenuItem right=new JMenuItem("Aller à droite");
	JMenuItem help=new JMenuItem("Aide");
	JMenuItem about=new JMenuItem("A propos");
	
	public MyMenuBar(ActionListener A){
		exit.addActionListener(A);
		exit.setAccelerator(KeyStroke.getKeyStroke('Q', 2));
		files.add(exit);
		add(files);
		play.setAccelerator(KeyStroke.getKeyStroke('L', 2));
		stop.setAccelerator(KeyStroke.getKeyStroke('T', 2));
		play.addActionListener(A);
		stop.addActionListener(A);
		zoomIn.addActionListener(A);
		zoomOut.addActionListener(A);
		up.addActionListener(A);
		down.addActionListener(A);
		left.addActionListener(A);
		right.addActionListener(A);
		actions.add(play);
		actions.add(stop);
		actions.add(new JSeparator());
		actions.add(zoomIn);
		actions.add(zoomOut);
		actions.add(new JSeparator());
		actions.add(up);
		actions.add(down);
		actions.add(left);
		actions.add(right);
		add(actions);
		help.setAccelerator(KeyStroke.getKeyStroke('I', 2));
		about.setAccelerator(KeyStroke.getKeyStroke('P', 2));
		help.addActionListener(A);
		about.addActionListener(A);
		intero.add(help);
		intero.add(about);
		add(intero);
		stop.setEnabled(false);
		zoomIn.setEnabled(false);
	}
	
}


//< --- CLASS MAIN FRAME --- >

public class MainFrame extends JFrame{
	
private static final long serialVersionUID = -6582920691652011396L;
	
	private JTextArea log_gen=new JTextArea("");
	private JTextArea log_msg=new JTextArea("");
	private JTextArea log_ihm=new JTextArea("");
	private JScrollPane scroll_gen=new JScrollPane(log_gen);
	private JScrollPane scroll_msg=new JScrollPane(log_msg);
	private JScrollPane scroll_ihm=new JScrollPane(log_ihm);
	JButton play=new JButton("Lancer");
	JButton stop=new JButton("Arrêter");
	JButton zoomIn=new JButton("+");
	JButton zoomOut=new JButton("-");
	JButton up=new JButton("^");
	JButton down=new JButton("v");
	JButton left=new JButton("<");
	JButton right=new JButton(">");
	private JTextField tf_min_w=new JTextField("30");
	private JTextField tf_max_w=new JTextField("60");
	private JTextField tf_min_h=new JTextField("20");
	private JTextField tf_max_h=new JTextField("50");
	private JTextField tf_max_nb=new JTextField("150");
	private JTextField tf_max_d_x=new JTextField("5");
	private JTextField tf_max_d_y=new JTextField("5");
	private GraphPanel gp=new GraphPanel();
	
	public void canEdit(boolean b){
		tf_min_h.setFocusable(b);
		tf_max_h.setFocusable(b);
		tf_min_w.setFocusable(b);
		tf_max_w.setFocusable(b);
		tf_max_nb.setFocusable(b);
		tf_max_d_x.setFocusable(b);
		tf_max_d_y.setFocusable(b);
	}
	
	private void addTextField(JPanel p, JTextField tf, String s){
		p.add(new JLabel(s+" : "));
		p.add(tf);
		tf.setPreferredSize(new Dimension(40, 20));
	}
	
	private void addTextField(JPanel p, JTextField tf, String s1, String s2){
		p.add(new JLabel(s1));
		addTextField(p, tf, s2);
	}
	
	private int myParseInt(String s, String err){
		int v=-1;
		try{
			v=Integer.parseInt(s);
		}catch(NumberFormatException e){
			JOptionPane.showMessageDialog(null, err+" incorrect !", "Erreur de format", 0);
		}
		if(v<=0){
			JOptionPane.showMessageDialog(null, err+" nul(le), négatif(ve) ou erroné(e) !", "Erreur de valeur", 0);
		}
		return v;
	}
	
	public int get_min_w(){
		return myParseInt(tf_min_w.getText(), "Longeur minimale");
	}
	
	public int get_max_w(){
		return myParseInt(tf_max_w.getText(), "Longeur maximale");
	}
	
	public int get_min_h(){
		return myParseInt(tf_min_h.getText(), "Hauteur minimale");
	}
	
	public int get_max_h(){
		return myParseInt(tf_max_h.getText(), "Hauteur maximale");
	}
	
	public int get_max_nb(){
		return myParseInt(tf_max_nb.getText(), "Nombre de batiment maximum");
	}
	
	public int get_max_d_x(){
		return myParseInt(tf_max_d_x.getText(), "Distance horizontale de déplacement maximale");
	}
	
	public int get_max_d_y(){
		return myParseInt(tf_max_d_y.getText(), "Distance verticale de déplacement maximale");
	}
	
	public int get_G_Width(){
		return gp.getWidth();
	}
	
	public int get_G_Heigth(){
		return gp.getHeight();
	}
	
	private void myAppend(JTextArea ta, String s){
		if(ta.getText().equals("")){
			ta.append(s);
		}else{
			ta.append("\n"+s);
		}
	}
	
	public void append_gen(String s){
		myAppend(log_gen, s);
	}
	
	public void append_msg(String s){
		myAppend(log_msg, s);
	}
	
	public void append_ihm(String s){
		myAppend(log_ihm, s);
	}
	
	public void clear_logs(){
		log_gen.setText("");
		log_msg.setText("");
		log_ihm.setText("");
	}
	
	private void set_TextArea(JTextArea ta){
		ta.setEditable(false);
		ta.setBackground(Color.black);
		ta.setForeground(Color.white);
	}
	
	
	public MainFrame(int w, int h){
		setTitle("TP Jade - SMA - 2013");
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		MainProgram.AddOnClose(this);
		setResizable(false);
		setMaximumSize(new Dimension(w, h));
		setPreferredSize(new Dimension(w, h));
		setSize(w, h);
		setLocationRelativeTo(null);
		MyListener ml=new MyListener(this);
		MyMenuBar mmb=new MyMenuBar(ml);
		ml.set_MMB(mmb);
		setJMenuBar(mmb);
		//
		getContentPane().setLayout(new BorderLayout(2, 2));
		JPanel optionsp=new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 1));
		optionsp.setMaximumSize(new Dimension(MainProgram.get_Width()/6, (MainProgram.get_Height()*3)/4));
		optionsp.setPreferredSize(new Dimension(175, (MainProgram.get_Height()*3)/4));
		//
		play.addActionListener(ml);
		stop.addActionListener(ml);
		zoomIn.addActionListener(ml);
		zoomOut.addActionListener(ml);
		up.addActionListener(ml);
		down.addActionListener(ml);
		left.addActionListener(ml);
		right.addActionListener(ml);
		//
		optionsp.add(play);
		optionsp.add(stop);
		//
		optionsp.add(new JLabel("--- --- --- --- --- --- --- --- --- --- ---"));
		addTextField(optionsp, tf_max_nb, "Nombre de batiments", "à génerer");
		optionsp.add(new JLabel("--- --- --- --- --- --- --- --- --- --- ---"));
		addTextField(optionsp, tf_min_w, "Longeur minimale");
		addTextField(optionsp, tf_max_w, "Longeur maximale");
		addTextField(optionsp, tf_min_h, "Hauteur minimale");
		addTextField(optionsp, tf_max_h, "Hauteur maximale");
		optionsp.add(new JLabel("--- --- --- --- --- --- --- --- --- --- ---"));
		optionsp.add(new JLabel("Distance maximale de"));
		optionsp.add(new JLabel("dépalcement des batiments"));
		addTextField(optionsp, tf_max_d_x, "Verticale");
		addTextField(optionsp, tf_max_d_y, "Horizontale");
		optionsp.add(new JLabel("--- --- --- --- --- --- --- --- --- --- ---"));
		optionsp.add(new JLabel("Zoom : "));
		optionsp.add(zoomIn);
		optionsp.add(zoomOut);
		optionsp.add(new JLabel("--- --- --- --- --- --- --- --- --- --- ---"));
		//
		JPanel directp=new JPanel(new BorderLayout());
		JPanel pup=new JPanel(new FlowLayout(FlowLayout.CENTER));
		pup.add(up);
		directp.add(pup, "North");
		JPanel pdown=new JPanel(new FlowLayout(FlowLayout.CENTER));
		pdown.add(down);
		directp.add(pdown, "South");
		directp.add(left, "West");
		directp.add(right, "East");
		directp.add(new JLabel("  Déplacements  "), "Center");
		optionsp.add(directp);
		//
		stop.setEnabled(false);
		zoomIn.setEnabled(false);
		ml.canZoom(false);
		//
		((DefaultCaret) log_gen.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);;
		((DefaultCaret) log_msg.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);;
		((DefaultCaret) log_ihm.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);;
		//
		add(optionsp,"East");
		set_TextArea(log_gen);
		set_TextArea(log_msg);
		set_TextArea(log_ihm);
		JTabbedPane tp=new JTabbedPane();
		tp.addTab("Batiments (&Agents)", scroll_gen);
		tp.addTab("Messages (Envoie/Reception)", scroll_msg);
		tp.addTab("Environnement (IHM)", scroll_ihm);
		JPanel logp=new JPanel(new BorderLayout(1, 1));
		logp.setMaximumSize(new Dimension(MainProgram.get_Width(), MainProgram.get_Height()/4));
		logp.setPreferredSize(new Dimension(MainProgram.get_Width(), MainProgram.get_Height()/4));
		logp.add(tp,"Center");
		add(logp, "South");
		//
		add(gp, "Center");
	}

}
