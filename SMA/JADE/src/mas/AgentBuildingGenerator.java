package mas;

import main.MainProgram;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

//< --- CLASS BEHAVIOUR BUILDING GENERATOR --- >

class BehaviourBuildingGenerator extends Behaviour{
	
	private static final long serialVersionUID = -2691138728639675703L;
	private int count=0;
	private ACLMessage inform_generated;

	
	public BehaviourBuildingGenerator(){
		inform_generated=new ACLMessage(ACLMessage.INFORM);
		inform_generated.setProtocol("generation");
		inform_generated.addReceiver(new AID("AgentHCI", AID.ISLOCALNAME));
	}
	
	@Override
	public void action() {
		// TODO Auto-generated method stub
		MainProgram.get_Bs().add(MainProgram.generate(MainProgram.get_MI().get_G_Width(), MainProgram.get_MI().get_G_Heigth(), MainProgram.get_min_l(), MainProgram.get_max_l(), MainProgram.get_min_h(), MainProgram.get_max_h()));
		MainProgram.showOnScreen(1, 0, false, false, null, count);
		MainProgram.get_MI().repaint();
		//
		inform_generated.setContent(""+count);
		myAgent.send(inform_generated);
		MainProgram.showOnScreen(2, 2, true, true, inform_generated, 0);
		//
		try {
			AgentController ab=MainProgram.get_MC().createNewAgent("AgentBuilding"+count, "mas.AgentBuilding", new Object[]{count});
			ab.activate();
		} catch (StaleProxyException st) {
			// TODO Auto-generated catch block
			st.printStackTrace();
			MainProgram.write_on_log_file("- [Erreur] ; Création agent ; StaleProxyEception");
		}
		//
		count++;
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		if(count<MainProgram.get_max_nb()){
			return false;
		}else{
			return true;
		}
	}
	
}


//< --- CLASS AGENT BUILDING GENERATOR --- >

public class AgentBuildingGenerator extends Agent{
	

	private static final long serialVersionUID = 3127976688665697990L;
	
	
	public void setup(){
		addBehaviour(new BehaviourBuildingGenerator());
	}
	
	public void takeDown(){
		doDelete();
	}

}
