package comp3111.coursescraper;
/**
 * Used for recording a section of a course, including section code, id and type.
 */
public class Section {
	private String sectionCode;
	private String sectionId;
	private String sectionType;
	static private int numUnique = 0;
	
	/*
	 * Constructor of section, accepting a String representing section id
	 * @param code_id the section's id 
	 */
	public Section(String code_id){
		sectionCode = code_id.split(" ")[0];
		sectionId = code_id.split(" ")[1].substring(1, 5);
		if (sectionCode.substring(0).equals("T")){
			sectionType = "Tutorial";
		}
		else if (sectionCode.substring(0).equals("LA")){
			sectionType = "Lab";
		}
		else if (sectionCode.substring(0).equals("L")){
			sectionType = "Lecture";
		}
		else {
			sectionType = "Invalid";
		}
	}
	
	/* 
	 * Check whether the section is valid
	 * @return whether the section is valid
	 */
	public boolean isValid() {
		return sectionType == "Invalid";
	}
	
	/*
	 * @return the section's code
	 */
	public String getSectionCode() {
		return this.sectionCode;
	}
	
	/*
	 * @return the section's type
	 */
	public String getSectionType() {
		return this.sectionType;
	}
	
	/*
	 * increase the number of unique sections by 1
	 */
	static public void increaseNumUnique() {
		numUnique++;
	}
	
	/*
	 * get the number of unique sections
	 */
	static public int getNumUnique() {
		return numUnique;
	}
	
	/*
	 * reset the number of unique sections
	 */
	static public void resetNumUnique() {
		numUnique=0;
	}
	
}
