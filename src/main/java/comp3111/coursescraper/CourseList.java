package comp3111.coursescraper;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class CourseList {
	private SimpleStringProperty courseCode;
	private SimpleStringProperty section;
	private SimpleStringProperty courseName;
	private SimpleStringProperty instructor;
	private SimpleStringProperty enroll;
	
	public CourseList(String courseCode, String section, String courseName, String instructor, String enroll){
        this.setCourseCode(courseCode);
        this.setSection(section);
        this.setCourseName(courseName);
        this.setInstructor(instructor);
        this.setEnroll(enroll);
    }

	public String getCourseCode() {
		return courseCode.get();
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = new SimpleStringProperty(courseCode);
	}
	
	public StringProperty courseCodeProperty() { 
        if (courseCode == null) courseCode = new SimpleStringProperty(this, "courseCode");
        return courseCode; 
    }

	public String getSection() {
		return section.get();
	}

	public void setSection(String section) {
		this.section = new SimpleStringProperty(section);
	}

	public StringProperty sectionProperty() { 
        if (section == null) section = new SimpleStringProperty(this, "section");
        return section; 
    }
	
	public String getCourseName() {
		return courseName.get();
	}

	public void setCourseName(String courseName) {
		this.courseName = new SimpleStringProperty(courseName);
	}
	
	public StringProperty courseNameProperty() { 
        if (courseName == null) courseName = new SimpleStringProperty(this, "courseName");
        return courseName; 
    }

	public String getInstructor() {
		return instructor.get();
	}

	public void setInstructor(String instructor) {
		this.instructor = new SimpleStringProperty(instructor);
	}

	public StringProperty instructorProperty() { 
        if (instructor == null) instructor = new SimpleStringProperty(this, "instructor");
        return instructor; 
    }
	
	public String getEnroll() {
		return enroll.get();
	}

	public void setEnroll(String enroll) {
		this.enroll = new SimpleStringProperty(enroll);
	}
	
	public StringProperty enrollProperty() { 
        if (enroll == null) enroll = new SimpleStringProperty(this, "enroll");
        return enroll; 
    }


}