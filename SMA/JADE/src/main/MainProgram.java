package main;

import jade.core.AID;
import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.lang.acl.ACLMessage;
import jade.wrapper.*;

import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import struct.Building;
import struct.Buildings;
import struct.ConflictBuildingList;


// < --- CLASS MAIN PROGRAM --- >

public class MainProgram {
	
	
	// < --- ! PROGRAMS (USEFUL) ! --- >
	// --- BEGIN ---
	
	@SuppressWarnings("unchecked")
	public static void showOnScreen(int where, int what, boolean acl, boolean send, ACLMessage msg, int b){
		// WHERE
		/*
		 * 1 --> Agents
		 * 2 --> Messages (Send/Receive)
		 * 3 --> Environnement (HCI)
		 */
		// WHAT
		/*
		 * 0 --> Génération/Building
		 * 1 --> Initialisation
		 * 2 --> Génération/Initialisation
		 * 3 --> Modification/Environement
		 * 4 --> Cooperation
		 * 5 --> Re initialisation
		 */
		String s="- [";
		switch(what){
			case 0: s=s+"Génération/Building"; break;
			case 1: s=s+"Initialisation"; break;
			case 2: s=s+"Génération/Initialisation"; break;
			case 3: s=s+"Modification/Environement"; break;
			case 4: s=s+"Coopération"; break;
			case 5: s=s+"Réinitialisation"; break;
		}
		s=s+"] ; ";
		if(acl){
			if(send){
				s=s+"Envoie de message ; ";
			}else{
				s=s+"Reception de message ; ";
			}
			s=s+"Type : ";
			switch(msg.getPerformative()){
				case ACLMessage.INFORM: s=s+"INFORM"; break;
				case ACLMessage.INFORM_IF: s=s+"INFORM_IF"; break;
				case ACLMessage.QUERY_IF: s=s+"QUERY_IF"; break;
				case ACLMessage.QUERY_REF: s=s+"QUERY_REF"; break;
				case ACLMessage.REFUSE: s=s+"REFUSE"; break;
				case ACLMessage.AGREE: s=s+"AGREE"; break;
				case ACLMessage.PROPOSE: s=s+"PROPOSE"; break;
				case ACLMessage.ACCEPT_PROPOSAL: s=s+"ACCEPT_PROPOSAL"; break;
				case ACLMessage.REJECT_PROPOSAL: s=s+"REJECT_PROPOSAL"; break;
			}
			s=s+" ; Source : ";
			if(send){
				switch(what){
					case 2: s=s+"'AgentGenerator'"; break;
					case 3: {
						if(b==-1){
						s=s+"'AgentEnv'";
						}else{
							s=s+"'AgentHCI'";
						}
					} break;
					case 4: s=s+"'AgentBuilding"+b+"'"; break;
				}
			}else{
				s=s+"'"+msg.getSender().getLocalName()+"'";
			}
			s=s+" ; Destination(s) : '";
			s=iterate_receivers(msg.getAllReceiver(), s);
			s=s+"' ; Protocole : '"+msg.getProtocol()+"' ; Contenu : '"+msg.getContent()+"'.";
		}else{
			switch(what){
				case 0: s=s+"N°"+(b+1)+" ; Position : ("+Bs.get(b).get_x()+", "+Bs.get(b).get_y()+") ; Taille : "+Bs.get(b).get_w()+"x"+Bs.get(b).get_h()+"."; break;
				case 1: s=s+"Calcul des Buildings en conflit avec le Building "+b+" terminé."; break;
				case 3: s=s+"Calcul du nouvel environement terminé. "+b+" agent(s) actif(s)."; break;
				case 5: s=s+"'AgentBuilding"+b+"' réinitialisé."; break;
			}
		}
		switch(where){
			case 1: MainInterface.append_gen(s); break;
			case 2: MainInterface.append_msg(s); break;
			case 3: MainInterface.append_ihm(s); break;
		}
		write_on_log_file(s);
	}
	
	private static String iterate_receivers(Iterator<AID> receivers, String s){
		String newS;
		newS=s;
		while(receivers.hasNext()) {
	         AID element = receivers.next();
	         newS=newS+element.getLocalName();
	         if(receivers.hasNext()){
	        	 newS=newS+"-";
	         }
	    }
		return newS;
	}
	
	public static void play(){
		playing=true;
		max_nb=MainInterface.get_max_nb();
		min_l=MainInterface.get_min_w();
		max_l=MainInterface.get_max_w();
		min_h=MainInterface.get_min_h();
		max_h=MainInterface.get_max_h();
		max_d_x=MainInterface.get_max_d_x();
		max_d_y=MainInterface.get_max_d_y();
		//
		x=0;
		y=0;
		Zoom=1;
		//
		String format = "dd-MM-yy_H-mm-ss";
		java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat( format );
		java.util.Date date = new java.util.Date();
		FileName="sma_"+ formater.format( date )+".jlog";
		try {
			fw = new FileWriter(dir+FileName, false);
			output = new BufferedWriter(fw);
			output.write("--- Fichier Log ---\n<<< Projet Jade >>>");
			output.write("\n*** Configuration ***");
			output.write("\n* Nombre de batiments à générer : "+max_nb+".");
			output.write("\n* Longueur minimale : "+min_l+".");
			output.write("\n* Longueur maximale : "+max_l+".");
			output.write("\n* Hauteur minimale : "+min_h+".");
			output.write("\n* Hauteur minimale : "+max_h+".");
			output.write("\n* Déplacement horizontal maximal : "+max_d_x+".");
			output.write("\n* Déplacement Vertical maximal : "+max_d_y+".");
			output.write("\n*** Configuration ***");
			output.write("\n///// DEBUT /////");
			output.flush();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//
		try {
			AgentController abg=mc.createNewAgent("AgentGenerator", "mas.AgentBuildingGenerator", null);
			AgentController ahci=mc.createNewAgent("AgentHCI", "mas.AgentHCI", null);
			ahci.start();
			MainProgram.write_on_log_file("- [Lancement] ; 'AgentHCI' lancé.");
			abg.start();
			MainProgram.write_on_log_file("- [Lancement] ; 'AgentGenerator' lancé.");
		} catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void stop(){
		write_on_log_file("!!! VERIF !!!");
		String s="";
		for(int j=0;j<Bs.size();j++){
			s="";
			for(int k=0; k<Bs.get(j).get_conflict_list().size();k++){
				s=s+" / "+Bs.get(j).get_conflict_list().get(k).get_in_cooperation();
			}
			write_on_log_file("--> "+s);
		}
		write_on_log_file("!!! VERIF !!!");
		try {
			mc.getAgent("AgentHCI").kill();
			MainProgram.write_on_log_file("- [Arrêt] ; 'AgentHCI' tué avec succès.");
			mc.getAgent("AgentGenerator").kill();
			MainProgram.write_on_log_file("- [Arrêt] ; 'AgentGenerator' tué avec succès.");
			for(int i=0;i<MainInterface.get_max_nb();i++){
				mc.getAgent("AgentBuilding"+i).kill();
				MainProgram.write_on_log_file("- [Arrêt] ; 'AgentBuilding"+i+"' tué avec succès.");
			}
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		write_on_log_file("///// FIN /////");
		//
		try {
			output.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//
		MainInterface.clear_logs();
		//
		Bs=new Buildings();
		MainInterface.repaint();
		playing=false;
	}
	
	public static int nb_conflict(int bn, ConflictBuildingList conflict, int d_x, int d_y){
		if(conflict.isEmpty()){
			return 0;
		}else{
			Building nB;
			if((d_x==0)&&(d_y==0)){
				nB=Bs.get(bn);
			}else{
				nB= new Building(Bs.get(bn).get_x()+d_x, Bs.get(bn).get_y()+d_x, Bs.get(bn).get_w(), Bs.get(bn).get_h());
			}
			int count=0, current_conflict_building;
			for(int i=0; i<conflict.size(); i++){
				current_conflict_building=conflict.get(i).get_conflict_building_num();
				if((MainProgram.get_Bs().get(current_conflict_building).get_enabled())&&(MainProgram.get_Bs().get(current_conflict_building).get_visible())&&(MainProgram.collision(nB, MainProgram.get_Bs().get(current_conflict_building)))){
					count++;
				}
			}
			return count;
		}
	}
	
	public static int myRandom(int min_val, int max_val){
		return (int) (Math.random()*(max_val-min_val)+min_val);
	}
	
	public static Building generate(int max_x, int max_y, int min_l, int max_l, int min_h, int max_h){
		int l=myRandom(min_l, max_l);
		int h=myRandom(min_h, max_h);
		int x=myRandom(0, max_x-l);
		int y=myRandom(0, max_y-h);
		Building H=new Building(x, y, l, h);
		return H;
	}
	
	public static boolean collision(Building B1, Building B2){
		Rectangle r= new Rectangle(B1.get_x(), B1.get_y(), B1.get_w(), B1.get_h());
		Rectangle p = new Rectangle(B2.get_x(), B2.get_y(), B2.get_w(), B2.get_h());
		if(r.intersects(p)){
			return true;
		}else{
			return false;
		}
		
	}
	
	public static Rectangle building_collision(Building B1, Building B2){
		Rectangle r = new Rectangle(B1.get_x(), B1.get_y(), B1.get_w(), B1.get_h());
		Rectangle p = new Rectangle(B2.get_x(), B2.get_y(), B2.get_w(), B2.get_h());
		return r.intersection(p);
	}
	
	public static void Close(){
		if(JOptionPane.showConfirmDialog(null, "Vous être sure de vouloir fermer l'application ?", "Quitter l'application ?", 0)==0){
			if(playing){
			stop();
			}
			try {
				mc.kill();
			} catch (StaleProxyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.exit(0);
		}
	}
	
	public static void AddOnClose(JFrame F){
		F.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent arg0) {
				// TODO Auto-generated method stub
				Close();
			}
			
			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	
	// --- END ---
	// < --- ! PROGRAMS (USEFUL) ! --- >
	
	private static int Width;
	private static int Height;
	private static Buildings Bs=new Buildings();
	private static MainFrame MainInterface;
	private static AgentContainer mc;
	private static int current_i=-1;
	private static int current_j=-1;
	private static String dir=System.getProperty("user.dir")+"/";
	private static String FileName;
	private static FileWriter fw;
	private static BufferedWriter output;
	private static int Zoom;
	private static int x;
	private static int y;
	private static int max_nb;
	private static int min_l;
	private static int max_l;
	private static int min_h;
	private static int max_h;
	private static int max_d_x;
	private static int max_d_y;
	private static boolean playing=false;
	
	public static int get_max_nb(){
		return max_nb;
	}
	
	public static int get_min_l(){
		return min_l;
	}
	
	public static int get_max_l(){
		return max_l;
	}
	
	public static int get_min_h(){
		return min_h;
	}
	
	public static int get_max_h(){
		return max_h;
	}
	
	public static int get_max_d_x(){
		return max_d_x;
	}
	
	public static int get_max_d_y(){
		return max_d_y;
	}
	
	public static AgentContainer get_MC() {
		return mc;
	}
	
	public static Buildings get_Bs(){
		return Bs;
	}
	
	public static int get_Width(){
		return Width;
	}
	
	public static int get_Height(){
		return Height;
	}
	
	public static MainFrame get_MI(){
		return MainInterface;
	}
	
	public static int get_i(){
		return current_i;
	}
	
	public static int get_j(){
		return current_j;
	}
	
	public static int get_zoom(){
		return Zoom;
	}
	
	public static int get_G_x(){
		return x;
	}
	
	public static int get_G_y(){
		return y;
	}
	
	public static boolean up(){
		y=y-10*Zoom;
		if(y<0){
			y=0;
		}
		inform_change_env();
		return y==0;
	}
	
	public static boolean down(){
		int G_Height=MainInterface.get_G_Heigth();
		y=y+10*Zoom;
		if(y+(G_Height/Zoom)>=G_Height){
			y=G_Height-(G_Height/Zoom);
			inform_change_env();
			return true;
		}else{
			inform_change_env();
			return false;
		}
	}
	
	public static boolean left(){
		x=x-10*Zoom;
		if(x<0){
			x=0;
		}
		inform_change_env();
		return x==0;
	}
	
	public static boolean right(){
		int G_Width=MainInterface.get_G_Width();
		x=x+10*Zoom;
		if(x+(G_Width/Zoom)>=G_Width){
			x=G_Width-(G_Width/Zoom);
			inform_change_env();
			return true;
		}else{
			inform_change_env();
			return false;
		}
	}
	
	public static void set_i(int i){
		current_i=i;
	}
	
	public static void set_j(int j){
		current_j=j;
	}
	
	public static void set_zoom(int z){
		Zoom=z;
	}
	
	public static void compute_new_position(boolean b){
		int G_Width=MainInterface.get_G_Width();
		int G_Height=MainInterface.get_G_Heigth();
		if(b){
			x=x+ G_Width/ (2*Zoom);
			y=y+ G_Height/ (2*Zoom);
		}else{
			x=x- G_Width/ (4*Zoom);
			y=y- G_Height/ (4*Zoom);
		}
		if(x<0){
			x=0;
		}
		if(y<0){
			y=0;
		}
		if(x+(G_Width/Zoom)>=G_Width){
			x=G_Width-(G_Width/Zoom)-1;
		}
		if(y+(G_Height/Zoom)>=G_Height){
			y=G_Height-(G_Height/Zoom)-1;
		}
		inform_change_env();
	}
	
	public static void inform_change_env(){
		try {
			AgentController ae=mc.createNewAgent("AgentEnv", "mas.AgentEnv", null);
			ae.start();
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void write_on_log_file(String s){
		s="\n"+s;
		try {
			output.write(s);
			output.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String [] args){
		Width=(int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()*99)/100;
		Height=(int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()*94)/100;
		//
		Runtime rt=Runtime.instance();
		rt.setCloseVM(true);
		Profile p=new ProfileImpl(null, 9898, null);
		mc=rt.createMainContainer(p);
		//
		MainInterface=new MainFrame(Width, Height);
		MainInterface.setVisible(true);
		MainInterface.pack();
	}

}
