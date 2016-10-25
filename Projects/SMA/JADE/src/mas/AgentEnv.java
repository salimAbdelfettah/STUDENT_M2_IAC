package mas;

import main.MainProgram;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

class BehaviourEnv extends OneShotBehaviour{

	private static final long serialVersionUID = -5069208144649850962L;

	@Override
	public void action() {
		// TODO Auto-generated method stub
		ACLMessage inform_change_env=new ACLMessage(ACLMessage.INFORM);
		inform_change_env.setProtocol("change_env");
		inform_change_env.setContent("");
		inform_change_env.addReceiver(new AID("AgentHCI", AID.ISLOCALNAME));
		myAgent.send(inform_change_env);
		MainProgram.showOnScreen(2, 3, true, true, inform_change_env, -1);
		try {
			myAgent.getContainerController().getAgent("AgentEnv").kill();
		} catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

public class AgentEnv extends Agent{
	
	private static final long serialVersionUID = 4185554885143549163L;

	public void setup(){
		addBehaviour(new BehaviourEnv());
	}
	
	public void takeDown(){
		doDelete();
	}

}
