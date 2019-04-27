package comp3111.coursescraper;

import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collections;

public class Instructor {
	static private HashSet<String> toRemove = new HashSet<String>();  // stores instructors to remove
	static private HashSet<String> all = new HashSet<String>();  // stores all instructors
	public Instructor() {
		
	}
	
	static public void reset() {
		toRemove.clear();
		all.clear();
	}
	
	// collect instructors to remove from the final list
	static public void addToRemove(String name) {
		toRemove.add(name);
	}
	
	// collect all the names of instructors
	static public void addAll(String name) {
		all.add(name);
	}
	
	static public HashSet<String> getToKeep() {
		HashSet<String> toKeep = new HashSet<String>();
		toKeep.addAll(all);
		toKeep.removeAll(toRemove);
		return toKeep;
	}
	
	static public ArrayList<String> getSortedToKeepList() {
		HashSet<String> toKeep = getToKeep();
		ArrayList<String> sortedToKeepList = new ArrayList<String>();
		
		sortedToKeepList.addAll(toKeep);
		Collections.sort(sortedToKeepList);
		return sortedToKeepList;
	}
	
	static public HashSet<String> getToRemove() {
		return toRemove;
	}
}
