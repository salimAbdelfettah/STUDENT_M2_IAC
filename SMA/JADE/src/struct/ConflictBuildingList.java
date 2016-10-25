package struct;

import java.util.ArrayList;

public class ConflictBuildingList extends ArrayList<ConflictBuilding>{

	private static final long serialVersionUID = 4939155122954373429L;
	
	public ConflictBuilding get_conflict_building(int cb){
		boolean b=false;
		int i=0;
		while((i<this.size())&&(!b)){
			if(get(i).get_conflict_building_num()==cb){
				b=true;
			}else{
				i++;
			}
		}
		if(b){
			return get(i);
		}else{
			return null;
		}
	}
	
	public void add_conflict_building(int cb){
		ConflictBuilding new_cb=new ConflictBuilding(cb);
		add(new_cb);
	}
	
	public boolean is_in_conflict_list(int m){
		boolean b=false;
		int i=0;
		while((i<this.size())&&(!b)){
			if(get(i).get_conflict_building_num()==m){
				b=true;
			}else{
				i++;
			}
		}
		return b;
	}
	
	public void remove_conflict_building(int cb){
		boolean b=false;
		int i=0;
		while((i<this.size())&&(!b)){
			if(get(i).get_conflict_building_num()==cb){
				b=true;
				remove(i);
			}else{
				i++;
			}
		}
	}
	

}
