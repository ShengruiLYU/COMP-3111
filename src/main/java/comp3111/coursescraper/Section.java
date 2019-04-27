package comp3111.coursescraper;

public class Section {
	private String sectionCode;
	private String sectionId;
	private String sectionType;
	static private int numUnique = 0;
	
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
	
	public boolean isValid() {
		return sectionType == "Invalid";
	}
	
	public String getSectionCode() {
		return this.sectionCode;
	}
	
	public String getSectionType() {
		return this.sectionType;
	}
	
	static public void increaseNumUnique() {
		numUnique++;
	}
	
	static public int getNumUnique() {
		return numUnique;
	}
	
	static public void resetNumUnique() {
		numUnique=0;
	}
	
}
