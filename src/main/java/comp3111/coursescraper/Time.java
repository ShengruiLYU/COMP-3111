package comp3111.coursescraper;
/**
 * 
 * 
 * A class for representing time. it implements the compare function. And it can be converted to a string representation.
 */
public class Time {
	private String original;
	private int half;
	private int hour;
	private int minute;
	public Time(String s) {
		original = s;
		half = s.substring(6).equals("PM")? 1:0;
		hour = Integer.parseInt(s.substring(0, 2));
		minute = Integer.parseInt(s.substring(3, 5));
	}
	
	/*
	 * compares with time is later, which is earlier
	 * @param a time object to be compared with the current one
	 * @return 0 if equal, -1 if the conrent one is earlier, 1 if the new one is earlier
	 */
	public int compares(Time t) {
		if (this.half>t.half){
			return 1; // this object is larger
		}
		else if (this.hour>t.hour) {
			return 1; // this object is larger
		}
		else if (this.hour<t.hour) {
			return -1; // this object is larger
		}
		else { // hours are the same
			if (this.minute>t.minute) 
				return 1;
			else if (this.minute<t.minute)
				return -1;
			else 
				return 0;
		}
	}
	
	/*
	 * retrieve the data in original string format
	 * @return the data in original string format
	 */
	public String getTime() {
		return original;
	}
}
