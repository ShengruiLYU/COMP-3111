package comp3111.coursescraper;

import java.util.Map;

import javafx.beans.property.StringProperty;

import java.util.HashMap;
import java.util.HashSet;
import java.time.LocalTime;
import java.util.Locale;
import java.time.format.DateTimeFormatter;



public class Slot {
	private int day;
	private LocalTime start;
	private LocalTime end;
	private String venue;
	private String instructor;
	public static final String DAYS[] = {"Mo", "Tu", "We", "Th", "Fr", "Sa"};
	public static final Map<String, Integer> DAYS_MAP = new HashMap<String, Integer>();
	private String sectionCode; 
	static {
		for (int i = 0; i < DAYS.length; i++)
			DAYS_MAP.put(DAYS[i], i);
	}
	private String CourseName;
	private double colorHue;
	private int enrollment = 0;

	@Override
	public Slot clone() {
		Slot s = new Slot();
		s.day = this.day;
		s.start = this.start;
		s.end = this.end;
		s.venue = this.venue;
		s.instructor = this.instructor;
		s.sectionCode = this.sectionCode;
		s.CourseName = this.CourseName;
		s.colorHue = this.colorHue;
		s.enrollment = this.enrollment;
		return s;
	}
	public String toString() {
		return DAYS[day] + start.toString() + "-" + end.toString() + ":" + venue;
	}
	public int getStartHour() {
		return start.getHour();
	}
	public int getStartMinute() {
		return start.getMinute();
	}
	public int getEndHour() {
		return end.getHour();
	}
	public int getEndMinute() {
		return end.getMinute();
	}
	/**
	 * @return the start
	 */
	public LocalTime getStart() {
		return start;
	}
	/**
	 * @param start the start to set
	 */
	public void setStart(String start) {
		this.start = LocalTime.parse(start, DateTimeFormatter.ofPattern("hh:mma", Locale.US));
	}
	/**
	 * @return the end
	 */
	public LocalTime getEnd() {
		return end;
	}
	/**
	 * @param end the end to set
	 */
	public void setEnd(String end) {
		this.end = LocalTime.parse(end, DateTimeFormatter.ofPattern("hh:mma", Locale.US));
	}
	/**
	 * @return the venue
	 */
	public String getVenue() {
		return venue;
	}
	/**
	 * @param venue the venue to set
	 */
	public void setVenue(String venue) {
		this.venue = venue;
	}
	
	/**
	 * @return the instructor
	 */
	public String getInstructor() {
		return instructor;
	}

	/**
	 * @param instructor the instructor to set
	 */
	public void setInstructor(String instructor) {
		this.instructor = instructor;
	}
	
	/**
	 * @return the enrollment
	 */
	public int getEnrollment() {
		return enrollment;
	}

	/**
	 * @param enrollment the enrollment to set
	 */
	public void setEnrollment(int enrollment) {
		this.enrollment = enrollment;
	}

	/**
	 * @return the day
	 */
	public int getDay() {
		return day;
	}
	/**
	 * @param day the day to set
	 */
	public void setDay(int day) {
		this.day = day;
	}
	
	public void setSectionCode(String code) {
		this.sectionCode = code;
	}
	
	public String getSectionCode() {
		return this.sectionCode;
	}
	
	public Integer getAbsStartTime() {
		return getStartHour()*60+getStartMinute();
	}
	public Integer getAbsEndTime() {
		return getEndHour()*60+getEndMinute();
	}
	public void setCourseName(String s) {
		this.CourseName = s;
	}
	public String getCourseName() {
		return this.CourseName;
	}
	public void setLocalTimeStart(LocalTime s) {
		this.start = s;
	}
	public void setLocalTimeEnd(LocalTime s) {
		this.end = s;
	}
	public void setColor(double d) {
		this.colorHue = d;
	}
	public double getColor() {
		return this.colorHue;
	}

}
