package comp3111.coursescraper;
/**
 * 
 * 
 * A claas for representing time. it implement the compare function. And it can be coverted to a string representation.
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
	
	public String getTime() {
		return original;
	}
}
