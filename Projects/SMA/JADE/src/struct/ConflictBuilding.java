package struct;

public class ConflictBuilding {
	
	private int conflictBuildingNum;
	private boolean inCooperation=false;
	
	public ConflictBuilding(int cbn){
		conflictBuildingNum=cbn;
	}
	
	public int get_conflict_building_num(){
		return conflictBuildingNum;
	}
	
	public boolean get_in_cooperation(){
		return inCooperation;
	}
	
	public void set_in_cooperation(boolean b){
		inCooperation=b;
	}

}
