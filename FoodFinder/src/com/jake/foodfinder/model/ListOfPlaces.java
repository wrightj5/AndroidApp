package com.jake.foodfinder.model;

import java.io.Serializable;
import java.util.ArrayList;


public class ListOfPlaces implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8554531125687177620L;

	ArrayList<Place> placeList = new ArrayList<Place>();
	
	public void put(Place place){
		
		
		placeList.add(place);
	}
	
	public Place getPlace(int i){
		
		
		return placeList.get(i);
	}
	
	public int getSize(){
		
		
		return placeList.size();
	}
	
	@SuppressWarnings("rawtypes")
	public ArrayList getAll(){
		
		return placeList;
	}



	

	

}
