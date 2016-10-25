package mas;

import struct.Building;
import main.MainProgram;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

//< --- CLASS BEHAVIOUR HCI (CYCLIC)  --- >

class BehaviourHCI extends CyclicBehaviour{

	private static final long serialVersionUID = 626386022529898384L;
	private ACLMessage receive;
	private ACLMessage reinit;
	private MessageTemplate template = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM), MessageTemplate.MatchProtocol("change_env"));
	
	
	private void computeBuildingsIn(){
		Building G=new Building(MainProgram.get_G_x(), MainProgram.get_G_y(), MainProgram.get_Width()/MainProgram.get_zoom(), MainProgram.get_Height()/MainProgram.get_zoom());
		int actif=0;
		for(int i=0;i<MainProgram.get_Bs().size();i++){
			if(MainProgram.collision(G, MainProgram.get_Bs().get(i))){
				MainProgram.get_Bs().get(i).set_enabled(true);
				MainProgram.get_Bs().get(i).set_visible(true);
				reinit=new ACLMessage(ACLMessage.INFORM);
				reinit.setProtocol("reinit");
				reinit.addReceiver(new AID("AgentBuilding"+i, AID.ISLOCALNAME));
				myAgent.send(reinit);
				MainProgram.showOnScreen(2, 3, true, true, reinit, -2);
				actif++;
			}else{
				MainProgram.get_Bs().get(i).set_enabled(false);
				MainProgram.get_Bs().get(i).set_visible(false);
			}
		}
		MainProgram.showOnScreen(3, 3, false, false, null, actif);
		MainProgram.get_MI().repaint();
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		receive=myAgent.receive(template);
		if(receive!=null){
			MainProgram.showOnScreen(2, 3, true, false, receive, -1);
			computeBuildingsIn();
		}else{
			block();
		}
	}
	
}

//< --- CLASS BEHAVIOUR HCI INITIALISATION --- >

class BehaviourHCIInitialisation extends Behaviour{

	private static final long serialVersionUID = 281273181411954328L;
	private int margin=1;
	private int count=-1;
	private ACLMessage receive;
	
	private void computeConflict(){
		int i;
		MainProgram.set_i(count);
		Building Bc=new Building(MainProgram.get_Bs().get(count).get_x()-(2*MainProgram.get_max_d_x()+margin), MainProgram.get_Bs().get(count).get_y()-(2*MainProgram.get_max_d_y()+margin), MainProgram.get_Bs().get(count).get_w()+(4*MainProgram.get_max_d_x()+margin), MainProgram.get_Bs().get(count).get_h()+(4*MainProgram.get_max_d_y()+margin));
		for(i=0;i<count;i++){
			MainProgram.set_j(i);
			if(MainProgram.collision(Bc, MainProgram.get_Bs().get(i))){
				MainProgram.get_Bs().get(i).get_conflict_list().add_conflict_building(count);
				MainProgram.get_Bs().get(count).get_conflict_list().add_conflict_building(i);
			}
		}
		MainProgram.showOnScreen(3, 1, false, false, null, (count+1));
		MainProgram.set_i(-1);
		MainProgram.set_j(-1);
		if(count==MainProgram.get_MI().get_max_nb()-1){
			for(i=0;i<MainProgram.get_MI().get_max_nb();i++){
				try {
					MainProgram.get_MC().getAgent("AgentBuilding"+i).start();
				} catch (StaleProxyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ControllerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				MainProgram.write_on_log_file("- [Lancement] ; 'AgentBuilding"+i+"' lancé.");
			}
		}
	}
	

	@Override
	public void action() {
		// TODO Auto-generated method stub
		MessageTemplate template = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM), MessageTemplate.MatchProtocol("generation"));
		receive=myAgent.receive(template);
		if(receive!=null){
			MainProgram.showOnScreen(2, 2, true, false, receive, -1);
			count=Integer.parseInt(receive.getContent());
			computeConflict();
		}else{
			block();
		}
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		if(count!=MainProgram.get_max_nb()-1){
			return false;
		}else{
			myAgent.addBehaviour(new BehaviourHCI());
			return true;
		}
	}
	
}


//< --- CLASS AGENT HCI --- >

public class AgentHCI extends Agent{

	private static final long serialVersionUID = -5036491679001113785L;
	
	public void setup(){
		addBehaviour(new BehaviourHCIInitialisation());
	}
	
	public void takeDown(){
		doDelete();
	}

}
