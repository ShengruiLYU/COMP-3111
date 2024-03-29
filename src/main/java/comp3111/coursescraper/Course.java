package comp3111.coursescraper;

/**
 * 
 * 
 * A course can be identified by a unique course code. COMP3111 and COMP3111H are two different courses althought they are co-listed.
 * It records title,description, exclusion, slots, number of slots, whether it is a common core and the enrollment status.
 *
 */

public class Course {
	private static final int DEFAULT_MAX_SLOT = 20;
	private static int numValidUnique = 0;
	
	private String title ; 
	private String description ;
	private String exclusion;
	private Slot [] slots;
	private int numSlots;
	private String commonCore;
	private int enrollStatus;
 
	
	public Course() {
		slots = new Slot[DEFAULT_MAX_SLOT];
		for (int i = 0; i < DEFAULT_MAX_SLOT; i++) slots[i] = null;
		numSlots = 0;
	}
	
	public void addSlot(Slot s) {
		if (numSlots >= DEFAULT_MAX_SLOT)
			return;
		slots[numSlots++] = s.clone();
	}
	public Slot getSlot(int i) {
		if (i >= 0 && i < numSlots)
			return slots[i];
		return null;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the exclusion
	 */
	public String getExclusion() {
		return exclusion;
	}

	/**
	 * @param exclusion the exclusion to set
	 */
	public void setExclusion(String exclusion) {
		this.exclusion = exclusion;
	}
	
	/**
	 * @return the enroll status
	 */
	public int getEnrollStatus() {
		return enrollStatus;
	}
	
	/**
	 * @param enrollStatus the enroll status to set
	 */
	public void setEnrollStatus(int enrollStatus) {
		this.enrollStatus = enrollStatus;
	}
	
	/**
	 * @return the common core
	 */
	public String getCommonCore() {
		return commonCore;
	}

	/**
	 * @param commonCore the commonCore to set
	 */
	public void setCommonCore(String commonCore) {
		this.commonCore = commonCore;
	}

	/**
	 * @return the numSlots
	 */
	public int getNumSlots() {
		return numSlots;
	}

	/**
	 * @param numSlots the numSlots to set
	 */
	public void setNumSlots(int numSlots) {
		this.numSlots = numSlots;
	}
	
	static public void increaseNumValidUnique() {
		numValidUnique++;
	}
	
	static public int getNumValidUnique() {
		return numValidUnique;
	}
	
	static public void resetNumValidUnique() {
		numValidUnique=0;
	}
}
