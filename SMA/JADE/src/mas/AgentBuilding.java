package mas;

import java.awt.Rectangle;

import struct.Building;
import struct.ConflictBuildingList;
import main.MainProgram;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

//< --- CLASS BEHAVIOUR REINIT --- >

class BehaviourReinit extends CyclicBehaviour{

	private static final long serialVersionUID = -7184392526583357542L;
	private ACLMessage receive;
	private int buildingnum;
	private MessageTemplate template= MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM), MessageTemplate.MatchProtocol(("reinit")));
	
	public BehaviourReinit(int bn){
		buildingnum=bn;
	}
	
	@Override
	public void action() {
		// TODO Auto-generated method stub
		receive=myAgent.receive(template);
		if(receive!=null){
			MainProgram.showOnScreen(2, 3, true, false, receive, -1);
			((AgentBuilding) myAgent).reinit();
			MainProgram.showOnScreen(1, 5, false, false, null, buildingnum);
		}else{
			block();
		}
		
	}
	
}

//< --- CLASS BEHAVIOUR ACCEPT PROPOSAL --- >

class BehaviourACCEPT extends CyclicBehaviour{

	private static final long serialVersionUID = -7184392526583357542L;
	private int buildingnum;
	private ACLMessage receive;
	private MessageTemplate template;
	
	public BehaviourACCEPT(int mynum){
		buildingnum=mynum;
		template = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
	}
	

	@Override
	public void action() {
		// TODO Auto-generated method stub
		receive=myAgent.receive(template);
		if(receive!=null){
			MainProgram.showOnScreen(2, 4, true, false, receive, -1);
			int sender=Integer.parseInt(receive.getContent());
			MainProgram.get_Bs().get(buildingnum).get_conflict_list().get_conflict_building(sender).set_in_cooperation(false);
			MainProgram.get_Bs().get(sender).get_conflict_list().get_conflict_building(buildingnum).set_in_cooperation(false);
		}else{
			block();
		}
	}
	
}

//< --- CLASS BEHAVIOUR REJECT PROPOSAL --- >

class BehaviourREJECT extends CyclicBehaviour{

	private static final long serialVersionUID = -184232362341400986L;
	private int buildingnum;
	private ACLMessage receive;
	private MessageTemplate template;
	
	public BehaviourREJECT(int mynum){
		buildingnum=mynum;
		template = MessageTemplate.MatchPerformative(ACLMessage.REJECT_PROPOSAL);
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		receive=myAgent.receive(template);
		if(receive!=null){
			MainProgram.showOnScreen(2, 4, true, false, receive, -1);
			if(receive.getProtocol().equals("get_out")){
				int conflict_with=Integer.parseInt(receive.getContent());
				MainProgram.get_Bs().get(buildingnum).get_conflict_list().get_conflict_building(conflict_with).set_in_cooperation(false);
				MainProgram.get_Bs().get(conflict_with).get_conflict_list().get_conflict_building(buildingnum).set_in_cooperation(false);
				MainProgram.get_Bs().get(buildingnum).set_visible(false);
				MainProgram.get_MI().repaint();
			}else{
				int v=Integer.parseInt(receive.getContent());
				int conflict_with=Integer.parseInt(receive.getSender().getLocalName().substring(13, receive.getSender().getLocalName().length()));
				ConflictBuildingList c=MainProgram.get_Bs().get(buildingnum).get_conflict_list();
				int before=MainProgram.nb_conflict(buildingnum, c, 0, 0);
				int after=before+1;
				if(receive.getProtocol().equals("move_x")){
					after=MainProgram.nb_conflict(buildingnum, c, v, 0);
					if(after<=before){
						int new_d_x=MainProgram.get_Bs().get(buildingnum).get_d_x()+v;
						if(new_d_x>MainProgram.get_max_d_x()){
							new_d_x=0;
							after=before+1;
						}
						if(new_d_x<-MainProgram.get_max_d_x()){
							new_d_x=0;
							after=before+1;
						}
						MainProgram.get_Bs().get(buildingnum).set_d_x(new_d_x);
					}
				}
				if(receive.getProtocol().equals("move_y")){
					after=MainProgram.nb_conflict(buildingnum, c, 0, v);
					if(after<=before){
						int new_d_y=MainProgram.get_Bs().get(buildingnum).get_d_y()+v;
						if(new_d_y>MainProgram.get_max_d_y()){
							new_d_y=0;
							after=before+1;
						}
						if(new_d_y<-MainProgram.get_max_d_y()){
							new_d_y=0;
							after=before+1;
						}
						MainProgram.get_Bs().get(buildingnum).set_d_y(new_d_y);
					}
				}
				if(after>before){
					ACLMessage m=new ACLMessage(ACLMessage.PROPOSE);
					m.addReceiver(new AID("AgentBuilding"+conflict_with, AID.ISLOCALNAME));
					m.setContent(""+buildingnum);
					m.setProtocol("get_out");
					myAgent.send(m);
					MainProgram.showOnScreen(2, 4, true, true, m, buildingnum);
				}else{
					MainProgram.get_MI().repaint();
					MainProgram.get_Bs().get(buildingnum).get_conflict_list().get_conflict_building(conflict_with).set_in_cooperation(false);
					MainProgram.get_Bs().get(conflict_with).get_conflict_list().get_conflict_building(buildingnum).set_in_cooperation(false);
				}
			}
		}else{
			block();
		}
	}
	
}

//< --- CLASS BEHAVIOUR INFORM GET OUT --- >

class BehaviourInformGO extends CyclicBehaviour{

	private static final long serialVersionUID = -4153171074190956847L;
	private int buildingnum;
	private ACLMessage receive;
	private MessageTemplate template;
	
	public BehaviourInformGO(int mynum){
		buildingnum=mynum;
		template = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM), MessageTemplate.MatchProtocol("get_out"));
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		receive=myAgent.receive(template);
		if(receive!=null){
			MainProgram.showOnScreen(2, 4, true, false, receive, -1);
			int i=0;
			while(receive.getContent().charAt(i)!=' '){
				i++;
			}
			int mine=MainProgram.nb_conflict(buildingnum, MainProgram.get_Bs().get(buildingnum).get_conflict_list(), 0, 0);
			int his=Integer.parseInt(receive.getContent().substring(0, i));
			int mys=MainProgram.get_Bs().get(buildingnum).get_x()*MainProgram.get_Bs().get(buildingnum).get_y();
			int hiss=Integer.parseInt(receive.getContent().substring(i+1, receive.getContent().length()));
			ACLMessage response=new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
			if(mine==0){
				mine=1;
			}
			if(his==0){
				his=1;
			}
			if((mys/Math.log10(mine))>(hiss/Math.log10(his))){
				response.setPerformative(ACLMessage.REJECT_PROPOSAL);
			}else{
				MainProgram.get_Bs().get(buildingnum).set_visible(false);
				MainProgram.get_MI().repaint();
			}
			response.setProtocol(receive.getProtocol());
			response.setContent(""+buildingnum);
			response.addReceiver(receive.getSender());
			myAgent.send(response);
			MainProgram.showOnScreen(2, 4, true, true, response, buildingnum);
		}else{
			block();
		}
	}
	
}


//< --- CLASS BEHAVIOUR QUERY --- >

class BehaviourQUERYREF extends CyclicBehaviour{

	private static final long serialVersionUID = -5066687511508715585L;
	private int buildingnum;
	private ACLMessage receive;
	private MessageTemplate template;
	
	public BehaviourQUERYREF(int mynum){
		buildingnum=mynum;
		template = MessageTemplate.MatchPerformative(ACLMessage.QUERY_REF);
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		receive=myAgent.receive(template);
		if(receive!=null){
			MainProgram.showOnScreen(2, 4, true, false, receive, -1);
			int mine;
			ConflictBuildingList my_cl=MainProgram.get_Bs().get(buildingnum).get_conflict_list();
			mine=MainProgram.nb_conflict(buildingnum, my_cl, 0, 0);
			ACLMessage response=new ACLMessage(ACLMessage.INFORM);
			response.setProtocol(receive.getProtocol());
			response.setContent(""+mine+" "+MainProgram.get_Bs().get(buildingnum).get_x()*MainProgram.get_Bs().get(buildingnum).get_y());
			response.addReceiver(new AID("AgentBuilding"+Integer.parseInt(receive.getContent()), AID.ISLOCALNAME));
			myAgent.send(response);
			MainProgram.showOnScreen(2, 4, true, true, response, buildingnum);
		}else{
			block();
		}
	}
	
}

//< --- CLASS BEHAVIOUR PROPOSE --- >

class BehaviourPROPOSE extends CyclicBehaviour{

	private static final long serialVersionUID = -7184392526583357542L;
	private int buildingnum;
	private ACLMessage receive;
	private MessageTemplate template;
	
	public BehaviourPROPOSE(int mynum){
		buildingnum=mynum;
		template = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);
	}
	
	private void reject(AID rec, String protocol, String content){
		ACLMessage msg=new ACLMessage(ACLMessage.REJECT_PROPOSAL);
		msg.setProtocol(protocol);
		msg.setContent(content);
		msg.addReceiver(rec);
		myAgent.send(msg);
		MainProgram.showOnScreen(2, 4, true, true, msg, buildingnum);
	}
	
	private void accept(AID rec, String protocol, String content){
		ACLMessage msg=new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
		msg.setProtocol(protocol);
		msg.setContent(content);
		msg.addReceiver(rec);
		myAgent.send(msg);
		MainProgram.showOnScreen(2, 4, true, true, msg, buildingnum);
		MainProgram.get_MI().repaint();
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		receive=myAgent.receive(template);
		if(receive!=null){
			MainProgram.showOnScreen(2, 4, true, false, receive, -1);
			if(receive.getProtocol().equals("get_out")){
				int sender=Integer.parseInt(receive.getContent());
				ACLMessage msg=new ACLMessage(ACLMessage.QUERY_REF);
				msg.setProtocol(receive.getProtocol());
				msg.setContent(""+buildingnum);
				msg.addReceiver(new AID("AgentBuilding"+sender, AID.ISLOCALNAME));
				myAgent.send(msg);
				MainProgram.showOnScreen(2, 4, true, true, msg, buildingnum);
			}else if(receive.getProtocol().equals("move_x")){
				int v=Integer.parseInt(receive.getContent());
				if((Math.abs(MainProgram.get_Bs().get(buildingnum).get_d_x())==MainProgram.get_max_d_x())&&(Math.abs(MainProgram.get_Bs().get(buildingnum).get_d_x()+v)>MainProgram.get_max_d_x())||(v==0)||(v==-0)){
					reject(receive.getSender(), "move_x", ""+v);
				}else{
					ConflictBuildingList c=MainProgram.get_Bs().get(buildingnum).get_conflict_list();
					int before=MainProgram.nb_conflict(buildingnum, c, 0, 0);
					int after=MainProgram.nb_conflict(buildingnum, c, v, 0);
					if(after<=before){
						int new_d_x=MainProgram.get_Bs().get(buildingnum).get_d_x()+v;
						if(new_d_x>MainProgram.get_max_d_x()){
							new_d_x=MainProgram.get_max_d_x();
						}
						if(new_d_x<-MainProgram.get_max_d_x()){
							new_d_x=-MainProgram.get_max_d_x();
						}
						MainProgram.get_Bs().get(buildingnum).set_d_x(new_d_x);
						accept(receive.getSender(), "move_x", ""+buildingnum);
					}else{
						reject(receive.getSender(), "move_x", ""+v);
					}
				}
			}else if(receive.getProtocol().equals("move_y")){
				int v=Integer.parseInt(receive.getContent());
				if((Math.abs(MainProgram.get_Bs().get(buildingnum).get_d_y())==MainProgram.get_max_d_y())&&(Math.abs(MainProgram.get_Bs().get(buildingnum).get_d_y()+v)>MainProgram.get_max_d_y())||(v==0)||(v==-0)){
					reject(receive.getSender(), "move_y", ""+v);
				}else{
					ConflictBuildingList c=MainProgram.get_Bs().get(buildingnum).get_conflict_list();
					int before=MainProgram.nb_conflict(buildingnum, c, 0, 0);
					int after=MainProgram.nb_conflict(buildingnum, c, 0, v);
					if(after<=before){
						int new_d_y=MainProgram.get_Bs().get(buildingnum).get_d_y()+v;
						if(new_d_y>MainProgram.get_max_d_y()){
							new_d_y=MainProgram.get_max_d_y();
						}
						if(new_d_y<-MainProgram.get_max_d_y()){
							new_d_y=-MainProgram.get_max_d_y();
						}
						MainProgram.get_Bs().get(buildingnum).set_d_y(new_d_y);
						accept(receive.getSender(), "move_y", ""+buildingnum);
					}else{
						reject(receive.getSender(), "move_y", ""+v);
					}
				}
			}
		}else{
			block();
		}
	}
	
}

//< --- CLASS BEHAVIOUR CONFLICT --- >

class BehaviourConlifct extends CyclicBehaviour{

	private static final long serialVersionUID = 1546136779770588011L;
	private int buildingnum;
	private ACLMessage receive;
	private int conflict_with;
	private MessageTemplate template;
	
	public BehaviourConlifct(int mynum){
		buildingnum=mynum;
		template = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM), MessageTemplate.MatchProtocol("conflict"));
	}
	
	private void compute_moving(Agent a, int br,Rectangle r, Building b, boolean w){
		ACLMessage m=new ACLMessage(ACLMessage.PROPOSE);
		m.addReceiver(new AID("AgentBuilding"+br, AID.ISLOCALNAME));
		if(w){
			m.setProtocol("move_x");
			int wi=Math.abs(r.width);
			if(wi>MainProgram.get_max_d_x()){
				wi=MainProgram.get_max_d_x();
			}
			if(b.get_x()==r.x){
				m.setContent(""+wi);
			}else{
				m.setContent("-"+wi);
			}
		}else{
			int h=Math.abs(r.height);
			if(h>MainProgram.get_max_d_y()){
				h=MainProgram.get_max_d_y();
			}
			m.setProtocol("move_y");
			if(b.get_y()==r.y){
				m.setContent(""+h);
			}else{
				m.setContent("-"+h);
			}
		}
		a.send(m);
		MainProgram.showOnScreen(2, 4, true, true, m, buildingnum);
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		receive=myAgent.receive(template);
		if(receive!=null){
			conflict_with=Integer.parseInt(receive.getContent());
			//MainProgram.get_Bs().get(buildingnum).get_conflict_list().get_conflict_building(conflict_with).set_in_cooperation(true);
			MainProgram.showOnScreen(2, 4, true, false, receive, -1);
			Rectangle r=MainProgram.building_collision(MainProgram.get_Bs().get(buildingnum), MainProgram.get_Bs().get(conflict_with));
			//
			if((r.width<=2*MainProgram.get_max_d_x())&&(r.height<=2*MainProgram.get_max_d_y())){
				if((r.width<=MainProgram.get_max_d_x())&&(r.height<=MainProgram.get_max_d_y())){
					if(Math.random()<=0.5){
						compute_moving(myAgent, conflict_with, r, MainProgram.get_Bs().get(buildingnum), true);
					}else{
						compute_moving(myAgent, conflict_with, r, MainProgram.get_Bs().get(buildingnum), false);
					}
				}else if(r.width<=MainProgram.get_max_d_x()){
					compute_moving(myAgent, conflict_with, r, MainProgram.get_Bs().get(buildingnum), true);
				}else if(r.height<=MainProgram.get_max_d_y()){
					compute_moving(myAgent, conflict_with, r, MainProgram.get_Bs().get(buildingnum), false);
				}else{
					int new_d_x=0, new_d_y=0, before, after, i;
					ConflictBuildingList c=MainProgram.get_Bs().get(buildingnum).get_conflict_list();
					before=MainProgram.nb_conflict(buildingnum, c, 0, 0);
					//
					if(MainProgram.get_Bs().get(buildingnum).get_x()==r.x){
						i=MainProgram.get_max_d_x();
						after=MainProgram.nb_conflict(buildingnum, c, i, 0);
						while((i>0)&&(after>before)){
							i--;
							after=MainProgram.nb_conflict(buildingnum, c, i, 0);
						}
					}else{
						i=-MainProgram.get_max_d_x();
						after=MainProgram.nb_conflict(buildingnum, c, i, 0);
						while((i<0)&&(after>before)){
							i++;
							after=MainProgram.nb_conflict(buildingnum, c, i, 0);
						}
					}
					if(after>before){
						new_d_x=0;
					}else{
						new_d_x=i+MainProgram.get_Bs().get(buildingnum).get_d_x();
						if(new_d_x>MainProgram.get_max_d_x()){
							new_d_x=MainProgram.get_max_d_x();
						}
						if(new_d_x<-MainProgram.get_max_d_x()){
							new_d_x=-MainProgram.get_max_d_x();
						}
					}
					//
					if(MainProgram.get_Bs().get(buildingnum).get_y()==r.y){
					i=MainProgram.get_max_d_y();
					after=MainProgram.nb_conflict(buildingnum, c, 0, i);
						while((i>0)&&(after>before)){
							i--;
							after=MainProgram.nb_conflict(buildingnum, c, 0, i);
						}
					}else{
						i=-MainProgram.get_max_d_y();
						after=MainProgram.nb_conflict(buildingnum, c, 0, i);
						while((i<0)&&(after>before)){
							i++;
							after=MainProgram.nb_conflict(buildingnum, c, 0, i);
						}
					}
					if(after>before){
						new_d_y=0;
					}else{
						new_d_y=i+MainProgram.get_Bs().get(buildingnum).get_d_y();
						if(new_d_x>MainProgram.get_max_d_y()){
							new_d_x=MainProgram.get_max_d_y();
						}
					}
					MainProgram.get_Bs().get(buildingnum).set_d_x(new_d_x);
					MainProgram.get_Bs().get(buildingnum).set_d_y(new_d_y);
					r=MainProgram.building_collision(MainProgram.get_Bs().get(buildingnum), MainProgram.get_Bs().get(conflict_with));
					if((r.width<=MainProgram.get_max_d_x())||(r.height<=MainProgram.get_max_d_y())){
						MainProgram.get_MI().repaint();
						if(Math.random()<=0.5){
							compute_moving(myAgent, conflict_with, r, MainProgram.get_Bs().get(buildingnum), true);
						}else{
							compute_moving(myAgent, conflict_with, r, MainProgram.get_Bs().get(buildingnum), false);
						}
					}else{
						ACLMessage m=new ACLMessage(ACLMessage.PROPOSE);
						m.addReceiver(new AID("AgentBuilding"+conflict_with, AID.ISLOCALNAME));
						m.setContent(""+buildingnum);
						m.setProtocol("get_out");
						myAgent.send(m);
						MainProgram.showOnScreen(2, 4, true, true, m, buildingnum);
					}
				}
			}else{
				ACLMessage m=new ACLMessage(ACLMessage.PROPOSE);
				m.addReceiver(new AID("AgentBuilding"+conflict_with, AID.ISLOCALNAME));
				m.setContent(""+buildingnum);
				m.setProtocol("get_out");
				myAgent.send(m);
				MainProgram.showOnScreen(2, 4, true, true, m, buildingnum);
			}
		}else{
			block();
		}
	}
	
}

//< --- CLASS BEHAVIOUR BUILDING --- >

class BehaviourBuilding extends CyclicBehaviour{

	private static final long serialVersionUID = -4844398900162433365L;
	private int buildingnum;
	private int current_conflict_building;
	private ACLMessage message;
	private ConflictBuildingList conflict;
	
	public BehaviourBuilding(int bn){
		buildingnum=bn;
		conflict=MainProgram.get_Bs().get(buildingnum).get_conflict_list();
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		if(conflict!=null){
			if((MainProgram.get_Bs().get(buildingnum).get_enabled())&&(!MainProgram.get_Bs().get(buildingnum).get_visible())&&(MainProgram.nb_conflict(buildingnum, MainProgram.get_Bs().get(buildingnum).get_conflict_list(), 0, 0)==0)){
				MainProgram.get_Bs().get(buildingnum).set_visible(true);
				MainProgram.get_MI().repaint();
			}
			for(int i=0;i<conflict.size();i++){
				current_conflict_building=conflict.get(i).get_conflict_building_num();
				if( MainProgram.get_Bs().get(buildingnum).get_enabled() && MainProgram.get_Bs().get(buildingnum).get_visible() && MainProgram.get_Bs().get(current_conflict_building).get_enabled() && MainProgram.get_Bs().get(current_conflict_building).get_visible() && !MainProgram.get_Bs().get(buildingnum).get_conflict_list().get_conflict_building(current_conflict_building).get_in_cooperation() && !MainProgram.get_Bs().get(current_conflict_building).get_conflict_list().get_conflict_building(buildingnum).get_in_cooperation() && MainProgram.collision(MainProgram.get_Bs().get(buildingnum), MainProgram.get_Bs().get(current_conflict_building)) ){
					message=new ACLMessage(ACLMessage.INFORM);
					message.setProtocol("conflict");
					message.setContent(buildingnum+"");
					message.addReceiver(new AID("AgentBuilding"+current_conflict_building,AID.ISLOCALNAME));
					myAgent.send(message);
					MainProgram.get_Bs().get(buildingnum).get_conflict_list().get(i).set_in_cooperation(true);
					MainProgram.get_Bs().get(current_conflict_building).get_conflict_list().get_conflict_building(buildingnum).set_in_cooperation(true);
					MainProgram.showOnScreen(2, 4, true, true, message, buildingnum);
				}
			}
		}
	}
	
}

//< --- CLASS AGENT BUILDING --- >

public class AgentBuilding extends Agent{

	private static final long serialVersionUID = 3859749305552545512L;
	private int buildingnum=-1;
	private BehaviourReinit b8;
	private BehaviourREJECT b7;
	private BehaviourInformGO b6;
	private BehaviourQUERYREF b5;
	private BehaviourACCEPT b4;
	private BehaviourPROPOSE b3;
	private BehaviourConlifct b2;
	private BehaviourBuilding b1;
	
	
	public void setup(){
		Object[] args=getArguments();
		if(args!=null){
			buildingnum=(int) args[0];
		}
		b8= new BehaviourReinit(buildingnum);
		b7= new BehaviourREJECT(buildingnum);
		b6= new BehaviourInformGO(buildingnum);
		b5= new BehaviourQUERYREF(buildingnum);
		b4= new BehaviourACCEPT(buildingnum);
		b3= new BehaviourPROPOSE(buildingnum);
		b2= new BehaviourConlifct(buildingnum);
		b1= new BehaviourBuilding(buildingnum);
		addBehaviour(b8);
		addBehaviour(b4);
		addBehaviour(b7);
		addBehaviour(b6);
		addBehaviour(b5);
		addBehaviour(b3);
		addBehaviour(b2);
		addBehaviour(b1);
	}
	
	public void reinit() {
		b1.block();
		b2.block();
		b3.block();
		b4.block();
		b5.block();
		b6.block();
		b7.block();
		MainProgram.get_Bs().get(buildingnum).set_d_x(0);
		MainProgram.get_Bs().get(buildingnum).set_d_y(0);
		int current_conflict_with;
		for(int i=0;i<MainProgram.get_Bs().get(buildingnum).get_conflict_list().size();i++){
			current_conflict_with=MainProgram.get_Bs().get(buildingnum).get_conflict_list().get(i).get_conflict_building_num();
			MainProgram.get_Bs().get(buildingnum).get_conflict_list().get(i).set_in_cooperation(false);
			MainProgram.get_Bs().get(current_conflict_with).get_conflict_list().get_conflict_building(buildingnum).set_in_cooperation(false);
		}
		b7.restart();
		b6.restart();
		b5.restart();
		b4.restart();
		b3.restart();
		b2.restart();
		b1.restart();
	}

	public void takeDown (){
		doDelete();
	}

}
