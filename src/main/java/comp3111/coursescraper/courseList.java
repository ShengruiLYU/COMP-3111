package comp3111.coursescraper;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class courseList {
	private SimpleStringProperty courseCode;
	private SimpleStringProperty section;
	private SimpleStringProperty courseName;
	private SimpleStringProperty instructor;
	private SimpleStringProperty enroll;
	
	public courseList(String courseCode, String section, String courseName, String instructor, String enroll){
        this.setCourseCode(new SimpleStringProperty(courseCode));
        this.setSection(new SimpleStringProperty(section));
        this.setCourseName(new SimpleStringProperty(courseName));
        this.setInstructor(new SimpleStringProperty(instructor));
        this.setEnroll(new SimpleStringProperty(enroll));
    }

	public SimpleStringProperty getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(SimpleStringProperty courseCode) {
		this.courseCode = courseCode;
	}

	public SimpleStringProperty getSection() {
		return section;
	}

	public void setSection(SimpleStringProperty section) {
		this.section = section;
	}

	public SimpleStringProperty getCourseName() {
		return courseName;
	}

	public void setCourseName(SimpleStringProperty courseName) {
		this.courseName = courseName;
	}

	public SimpleStringProperty getInstructor() {
		return instructor;
	}

	public void setInstructor(SimpleStringProperty instructor) {
		this.instructor = instructor;
	}

	public SimpleStringProperty getEnroll() {
		return enroll;
	}

	public void setEnroll(SimpleStringProperty enroll) {
		this.enroll = enroll;
	}


}
