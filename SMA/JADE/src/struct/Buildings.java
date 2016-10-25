package struct;

import java.util.ArrayList;

public class Buildings extends ArrayList<Building> {

	private static final long serialVersionUID = -7199551819708363110L;
	
	public void add(int x, int y, int l, int h){
		Building newBuilding=new Building(x, y, l, h);
		add(newBuilding);
	}

}
