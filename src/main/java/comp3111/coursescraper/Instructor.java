package comp3111.coursescraper;

import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collections;

/*
 * This class has two functions:
 * 1. The static attributes and methods basically implement the requirements in task 1, which asks to find instructors teaching at a specific time;
 * 2. the instantiable (i.e., dynamic) parts implements the requirements in task 6, which asks to calculate each instructor's SFQ scores averaged over sections
 */
public class Instructor {
	static private HashSet<String> toRemove = new HashSet<String>();  // stores instructors to remove
	static private HashSet<String> all = new HashSet<String>();  // stores all instructors
	private float totalSFQScore;
	private int numSections;
	public Instructor(float score) {
		this.totalSFQScore = score;
		this.numSections = 1;
	}
	
	// implement copy constructor to be use it in stl;
	public Instructor(Instructor another) {
		this.totalSFQScore = another.totalSFQScore;
		this.numSections = another.numSections;
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
	
	public float getAverageSFQScore() {
		return this.totalSFQScore/this.numSections;
	}
	
	public int getNumSections() {
		return this.numSections;
	}
	
	public void addSFQScore(float score) {
		this.totalSFQScore += score;
		this.numSections += 1;
	}
	
	
}
