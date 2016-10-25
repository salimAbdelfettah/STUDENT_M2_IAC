package struct;

public class Building {
	private int position_x;
	private int position_y;
	private int dimension_w;
	private int dimension_h;
	private int distance_x;
	private int distance_y;
	private boolean enable;
	private boolean visible;
	private ConflictBuildingList BuildingConflictList;
	
	public int get_x(){
		return position_x+distance_x;
	}
	
	public int get_y(){
		return position_y+distance_y;
	}
	
	public int get_w(){
		return dimension_w;
	}
	
	public int get_h(){
		return dimension_h;
	}
	
	public int get_d_x(){
		return distance_x;
	}
	
	public int get_d_y(){
		return distance_y;
	}
	
	public boolean get_visible(){
		return visible;
	}
	
	public boolean get_enabled(){
		return enable;
	}
	
	public ConflictBuildingList get_conflict_list(){
		return BuildingConflictList;
	}
	
	public void set_d_x(int d_x){
		distance_x=d_x;
	}
	
	public void set_d_y(int d_y){
		distance_y=d_y;
	}
	
	public void set_visible(boolean v){
		visible=v;
	}
	
	public void set_enabled(boolean e){
		enable=e;
	}
	
	public void clear_conflict_list(){
		BuildingConflictList=new ConflictBuildingList();
	}
	
	public Building(int x, int y, int w, int h){
		position_x=x;
		position_y=y;
		dimension_w=w;
		dimension_h=h;
		distance_x=0;
		distance_y=0;
		enable=true;
		visible=true;
		clear_conflict_list();
	}


}
