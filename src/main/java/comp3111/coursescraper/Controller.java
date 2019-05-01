package comp3111.coursescraper;


import java.awt.event.ActionEvent;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Callback;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
//import javafx.event.ActionEvent;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
/**
 * 
 * Controller class of the whole project.
 * 
 * It implements search function, all subject search function, timetable display funtion, find sfq function, course listing and enrollment function and course filtering function.
 * All the following functions (exception initialization of the controller)are private, therefore they won't show in the javadoc. (Acoording to piazza Kevin's reply, we only show the public and protected functions)
 * search() is for the search tab, click search button will call this function and display the search result in the ui console.
 * allSubjectSearch() is for the all subject search tab, click all subject search buttion will display the all subject search result in the console and update progress bar.
 * printTimetable2() is for displaying the timeslots passed in on the timetable.
 * updateTimetable() will be called if timetable tab is clicked. It will fetech all the enrolled section list and display them.
 * checkBox() will update the filter selection and the console display whe the user change the filter options.
 * printTimetable() is for displaying the courses passed in on the timetable.
 * checkFlag() will check whether a course will be put in the updated course list according to the filter options.
 * findInstructorSfq() find the average sfq for the instructor
 * findSfqEnrollCourse() find the average sfq scores for the enrolled course
 * selectAll() select all the checkboxes
 * enrollUpdate() will enroll my courses and sections
 * updateMyEnrollment() will trigger the enrollment and display all the enrolled courses
 * switchToList() will trigger the console update when switch to the tab "list"
 */
public class Controller implements Initializable{

    @FXML
    private Tab tabMain;

    @FXML
    private TextField textfieldTerm;

    @FXML
    private TextField textfieldSubject;

    @FXML
    private Button buttonSearch;

    @FXML
    private TextField textfieldURL;

    @FXML
    private Tab tabStatistic;

    @FXML
    private ComboBox<?> comboboxTimeSlot;

    @FXML
    private Tab tabFilter;

    @FXML
    private Tab tabList;

    @FXML
    private Tab tabTimetable;

    @FXML
    private Tab tabAllSubject;
    
    @FXML
    private Button buttonAllSubjectSearch;

    @FXML
    private ProgressBar progressbar;
    
    @FXML
    private Tab tabSfq;
    
    @FXML
    private TextField textfieldSfqUrl;

    @FXML
    private Button buttonSfqEnrollCourse;

    @FXML
    private Button buttonInstructorSfq;
    
    @FXML
    private Button buttonSelectAll;
    
    @FXML
    private CheckBox checkboxAM;

    @FXML
    private CheckBox checkboxPM;

    @FXML
    private CheckBox checkboxMonday;

    @FXML
    private CheckBox checkboxTuesday;
    
    @FXML
    private CheckBox checkboxWednesday;

    @FXML
    private CheckBox checkboxThursday;
    
    @FXML
    private CheckBox checkboxFriday;

    @FXML
    private CheckBox checkboxSaturday;
    
    @FXML
    private CheckBox checkboxCC;

    @FXML
    private CheckBox checkboxNoExclusion;
    
    @FXML
    private CheckBox checkboxWithLabTut;
    
    @FXML
    private TextArea textAreaConsole;
    
    @FXML
    private TableView<CourseList> tableListCourse;
    
    @FXML
    private TableColumn<CourseList, String> courseCode;

    @FXML
    private TableColumn<CourseList, String> section;

    @FXML
    private TableColumn<CourseList, String> courseName;

    @FXML
    private TableColumn<CourseList, String> instructor;

    @FXML
    private TableColumn<CourseList, CheckBox> enroll;
    //private TableColumn<CourseList, String> enroll;
    
    @FXML
    private Button buttonEnroll;
    
    private Scraper scraper = new Scraper();
    
    private static List<Course> myCourseList;
    private List<Course> myUpdatedCourseList;
    private List<String> myEnrolledCourseList = new ArrayList<String>();
    private List<String> myDupCourseList = new ArrayList<String>();
    
    private ObservableList<CourseList> listInTable = FXCollections.observableArrayList();
    private List<Label> slotList = new ArrayList<Label>();
    
    private List<String> backUpList = new ArrayList<String>();
    
    @FXML
    void updateMyEnrollment() {
    	enrollUpdate();
    	textAreaConsole.clear();
    	textAreaConsole.setText("The following courses and sections are enrolled:" + "\n");
    	for(int i = 0; i < myEnrolledCourseList.size(); i++) {
    		textAreaConsole.setText(textAreaConsole.getText()+myEnrolledCourseList.get(i)+"\n");
    	}
    }
    
    @FXML
    void switchToList() {
    	updateCheckBox();
    }
    
    @FXML
    private void enrollUpdate() {
    	if(myUpdatedCourseList == null) {
    		listInTable.clear();
        	listInTable.add(new CourseList("N/A","N/A","N/A","N/A"));
        	return;
    	}
    	//get the list of checked courses
    	//course code + section code = unique id
    	for(int i =0; i < tableListCourse.getItems().size(); i++) {
    		String uniqueID = tableListCourse.getItems().get(i).getCourseCode() + "-" + tableListCourse.getItems().get(i).getSection();
    		
    		if(tableListCourse.getItems().get(i).getEnroll().isSelected()) {
    			if(!(myEnrolledCourseList.contains(uniqueID))) {
    				myEnrolledCourseList.add(uniqueID);
    			}
    		}
    		else {
    			if(myEnrolledCourseList.contains(uniqueID)) {
    				myEnrolledCourseList.remove(uniqueID);
    			}
    		}
    		
    	}
    }
    
    private List<String> getEnrolledCourseCode() {
    	List<String> result = new ArrayList<String>();
    	for(int i = 0; i < myEnrolledCourseList.size(); i++) {
    		String[] s = myEnrolledCourseList.get(i).split("\\-");
    		result.add(s[0]);
    	}
    	return result;
    }
    
    private void updateList() {
    	
    	this.listInTable.clear();
    	if(myUpdatedCourseList == null) {
    		listInTable.clear();
        	listInTable.add(new CourseList("N/A","N/A","N/A","N/A"));
        	return;
    	}
    	
    	for (Course c : this.myUpdatedCourseList) {
    		String [] tempL = c.getTitle().split("\\ - ");
    		
    		for (int i = 0; i < c.getNumSlots(); i++) {
    			CourseList currentChoice = new CourseList(tempL[0], c.getSlot(i).getSectionCode(), tempL[1], c.getSlot(i).getInstructor());
    			String currentID = tempL[0] + "-" +c.getSlot(i).getSectionCode();
    			
    			if(myEnrolledCourseList.contains(currentID)) {
    				currentChoice.getEnroll().setSelected(true);
    			}
    			if(i > 0) {
    				if(c.getSlot(i).getSectionCode().equals(c.getSlot(i-1).getSectionCode())) {
    					continue;
    				}
    			}
    			listInTable.add(currentChoice);
    		}
    		
    	}
    	
    	courseCode.setEditable(false);
    	section.setEditable(false);
    	courseName.setEditable(false);
    	instructor.setEditable(false);
    	enroll.setEditable(true);
    	return;
    }

	
	@FXML
    private void allSubjectSearch() {
    	buttonSfqEnrollCourse.setDisable(false);
    	 final Task <Void> allSubjectThread = new Task <Void>() {
    	    	
    	    	@Override
    	    	protected Void call() throws Exception {
    	    		System.out.println("start all subject thread");
    	    		List<String> allSubject = scraper.getAllSubjectName(textfieldURL.getText(), textfieldTerm.getText());
    	        	textAreaConsole.setText(textAreaConsole.getText() + "\n" + "Total Number of Categories/Code Prefix: " +allSubject.size());
    	        	
    	        	int totalNumOfCourses = 0;
    	        	
    	        	int subjectCount = 0;
    	        	for (String subject : allSubject) {
    	        		System.out.println(subject+" starts");
    	        		List<Course> v = scraper.scrape(textfieldURL.getText(), textfieldTerm.getText(), subject);
    	        		Controller.myCourseList = v;
    	        		String newline = "";
    	        		for (Course c : v) {
    	            		newline += c.getTitle() + "\n";
    	            		for (int i = 0; i < c.getNumSlots(); i++) {
    	            			Slot t = c.getSlot(i);
    	            			newline += "Section " + t.getSectionCode() + " Slot " + i + ":" + t + "\n";
    	            		}
    	            		
    	            	}
    	        		textAreaConsole.setText(textAreaConsole.getText() + "\n" + newline);
    	        		totalNumOfCourses += v.size();
    	        		subjectCount +=1;
    	        		updateProgress(subjectCount+1,allSubject.size());
    	        		
    	        		System.out.println(subject+" is done");
    	        	}
    	        	textAreaConsole.setText(textAreaConsole.getText() + "\n" + "Total Number of Courses fetched: " +totalNumOfCourses);
    	    		return null;
    	    	}
    	    };
    	    progressbar.progressProperty().bind(allSubjectThread.progressProperty());
    	Thread ssthread = new Thread(allSubjectThread,"all subject search thread");
    	ssthread.setDaemon(true);
    	ssthread.start();
    		
    	this.myUpdatedCourseList = scraper.scrape(textfieldURL.getText(), textfieldTerm.getText(),textfieldSubject.getText());
    }

    @FXML
    private void findInstructorSfq() {
    	buttonInstructorSfq.setDisable(true); //
    	
    	// scrape the list from website
    	List<String>results = scraper.scrapeSFQInstructors(textfieldSfqUrl.getText());
    	textAreaConsole.setText(""); // clear the console for new output.
    	// handle 404 error. 
    	if (results == null) {
    		textfieldSfqUrl.setText("Some errors occurred when scraping " + textfieldSfqUrl.getText());
    	}
    	
    	else if (results.size()==1 & results.get(0).substring(0,13).equals("404 Not Found")) {
			textAreaConsole.setText(results.get(0));
			return;
		}
    	else {
	    	// output results.
	    	for (String result: results) {
	    		//output something in certain format
	    		textAreaConsole.setText(textAreaConsole.getText() + result + "\n");
	    	}
    	}
    }

    @FXML
    private void findSfqEnrollCourse() {
    	// create dummy list of course strings, need to use results from task 4 later.
    	List<String> TMP_COURSES = new ArrayList<String>();
    	TMP_COURSES.add("COMP 2012");
    	TMP_COURSES.add("CENG 4913");
    	TMP_COURSES.add("MECH 4000K");
    	TMP_COURSES.add("ENGG 4930A"); 
    	TMP_COURSES.add("CIVL 1100");
    	TMP_COURSES.add("LABU 1100");
    	TMP_COURSES.add("ELEC 1095A");
    	
    	enrollUpdate();
    	List<String> REAL_COURSES = getEnrolledCourseCode();
    	// scrape the list from website
    	List<String>results = scraper.scrapeSFQEnrolledCourses(textfieldSfqUrl.getText(), backUpList);
    	textAreaConsole.setText("Task3 failed, using the first five courses...\n\n"); // clear the console for new output.
    	// handle 404 error (or other types of errors).
    	if (results == null) {
			textAreaConsole.setText(textAreaConsole.getText() + "Some errors occurred when scraping " + textfieldSfqUrl.getText());
    	}
    	
    	else if (results.size()==0) {
    		textAreaConsole.setText(textAreaConsole.getText() + "No courses are found.");
    	}
    	
    	else if (results.size()==1 & results.get(0).substring(0,13).equals("404 Not Found")) {
			textAreaConsole.setText(textAreaConsole.getText() + results.get(0));
			return;
		}
    	else {
	    	// output results.
	    	for (String result: results) {
	    		//output something in certain format
	    		textAreaConsole.setText(textAreaConsole.getText() + result + "\n");
	    	}
    	}
    	
    	
    }
    
    /*
    * update the filter selection and the console display whether the user change the filter options.
    */
    private void updateCheckBox() {
    	
    	textAreaConsole.clear();
    	//if the course list is empty, there are 2 possibilities. And we need to update the list
    	//courses have not scraped
    	//scraper has no matching results
    	
    	/*
    	if(myCourseList.isEmpty()) {
    		myCourseList = scraper.scrape(textfieldURL.getText(), textfieldTerm.getText(),textfieldSubject.getText());
    	}
    	*/
    	
    	//make sure course list is valid now
    	
    	this.myUpdatedCourseList.clear();
    	
    	for (Course c : this.myCourseList) {
    		int flagNE = 0;
    		int flagCC = 0;
    		int flagWLT = 0;
    		int flagAM = 0;
    		int flagPM = 0;
    		int flagMon = 0;
    		int flagTue = 0;
    		int flagWed = 0;
    		int flagThu = 0;
    		int flagFri = 0;
    		int flagSat = 0;
    		
    		if(checkboxNoExclusion.isSelected())
    			flagNE = 1;
    		if(checkboxCC.isSelected())
    			flagCC = 1;
    		if(checkboxWithLabTut.isSelected())
    			flagWLT = 1;
    		if(checkboxAM.isSelected())
    			flagAM = 1;
    		if(checkboxPM.isSelected())
    			flagPM = 1;
    		if(checkboxMonday.isSelected())
    			flagMon = 1;
    		if(checkboxTuesday.isSelected())
    			flagTue = 1;
    		if(checkboxWednesday.isSelected())
    			flagWed = 1;
    		if(checkboxThursday.isSelected())
    			flagThu = 1;
    		if(checkboxFriday.isSelected())
    			flagFri = 1;
    		if(checkboxSaturday.isSelected())
    			flagSat = 1;
    		
    		int thisNE = 0;
    		int thisCC = 0;
    		int thisWLT = 0;
    		int thisAM = 0;
    		int thisPM = 0;
    		int thisMon = 0;
    		int thisTue = 0;
    		int thisWed = 0;
    		int thisThu = 0;
    		int thisFri = 0;
    		int thisSat = 0;
    		int invalidFlag = 0;
    		
        	if(c.getExclusion() == "null") {
        		//textAreaConsole.setText(textAreaConsole.getText() + c.getExclusion() + "\n");
        		thisNE = 1;
        	}
    		
        	if(c.getCommonCore() != "null") {
        		thisCC = 1;
        		//textAreaConsole.setText(textAreaConsole.getText() + c.getCommonCore() + "\n");
        		
        	}
        	
    		int numSlots = c.getNumSlots();
    		
    		if(numSlots == 0)
    			invalidFlag = 1;
    		
    		for (int i = 0; i<numSlots; i++) {
    			int courseDay = c.getSlot(i).getDay();
    			
    			//test instructor
            	//textAreaConsole.setText(textAreaConsole.getText() + c.getSlot(i).getInstructor() + "\n");
    			
    			if(courseDay == 0)
    				thisMon = 1;
    			else if(courseDay == 1)
    				thisTue = 1;
    			else if(courseDay == 2)
    				thisWed = 1;
    			else if(courseDay == 3)
    				thisThu = 1;
    			else if(courseDay == 4)
    				thisFri = 1;
    			else if(courseDay == 5)
    				thisSat = 1;
    			
    			if(c.getSlot(i).getStartHour() < 12)
    				thisAM = 1;
    			if(c.getSlot(i).getEndHour() >= 12)
    				thisPM = 1;
    				
    			String thisSectionCode = c.getSlot(i).getSectionCode();
    			if(thisSectionCode.startsWith("LA") || thisSectionCode.startsWith("T"))
    				thisWLT = 1;
    			
    			if(thisSectionCode.startsWith("R"))
    				invalidFlag = 1;
    		}
    		
    		if(invalidFlag == 1)
    			continue;
    		
    		//all tags are satisfied, then add this course
    		if(checkFlag(flagNE, flagCC, flagWLT, flagAM, flagPM, flagMon, flagTue, flagWed, flagThu, flagFri, flagSat, 
    				thisNE, thisCC, thisWLT, thisAM, thisPM, thisMon, thisTue, thisWed, thisThu, thisFri, thisSat)) {
    			this.myUpdatedCourseList.add(c);
    		}
    	}
    	
    	//display all matched courses
    	for (Course c : this.myUpdatedCourseList) {
    		String newline = c.getTitle() + "\n";
    		for (int i = 0; i < c.getNumSlots(); i++) {
    			Slot t = c.getSlot(i);
    			newline += "Section " + t.getSectionCode() + " Slot " + i + ":" + t + "\n";
    		}
    		textAreaConsole.setText(textAreaConsole.getText() + "\n" + newline);
    	}
    	enrollUpdate();
    	updateList();
    	return;
    	
    }
    
    private boolean checkFlag(int f1, int f2, int f3, int f4, int f5, int f6, int f7, int f8, int f9, int f10, int f11, 
    		int t1, int t2, int t3, int t4, int t5, int t6, int t7, int t8, int t9, int t10, int t11) {
    	if(f1>t1)
    		return false;
    	else if(f2>t2)
    		return false;
    	else if(f3>t3)
    		return false;
    	else if(f4>t4)
    		return false;
    	else if(f5>t5)
    		return false;
    	else if(f6>t6)
    		return false;
    	else if(f7>t7)
    		return false;
    	else if(f8>t8)
    		return false;
    	else if(f9>t9)
    		return false;
    	else if(f10>t10)
    		return false;
    	else if(f11>t11)
    		return false;
    	else
    		return true;
    	// true means meeting all filter requirements
    }
    
    @FXML
    private void selectAllBoxes() {
    	if(buttonSelectAll.getText() != "De-select All") {
    		buttonSelectAll.setText("De-select All");
    		checkboxAM.setSelected(true);
    		checkboxPM.setSelected(true);
    		checkboxMonday.setSelected(true);
    		checkboxTuesday.setSelected(true);
    		checkboxWednesday.setSelected(true);
    		checkboxThursday.setSelected(true);
    		checkboxFriday.setSelected(true);
    		checkboxSaturday.setSelected(true);
    		checkboxCC.setSelected(true);
    		checkboxNoExclusion.setSelected(true);
    		checkboxWithLabTut.setSelected(true);
    		updateCheckBox();
    		return;
    	}
    	
    	buttonSelectAll.setText("Select All");
		checkboxAM.setSelected(false);
		checkboxPM.setSelected(false);
		checkboxMonday.setSelected(false);
		checkboxTuesday.setSelected(false);
		checkboxWednesday.setSelected(false);
		checkboxThursday.setSelected(false);
		checkboxFriday.setSelected(false);
		checkboxSaturday.setSelected(false);
		checkboxCC.setSelected(false);
		checkboxNoExclusion.setSelected(false);
		checkboxWithLabTut.setSelected(false);
		updateCheckBox();
		return;
    }
    
    

    @FXML
    private void checkAM() {
    	updateCheckBox();
    }

    @FXML
    private void checkCC() {
    	updateCheckBox();
    }

    @FXML
    private void checkFri() {
    	updateCheckBox();
    }

    @FXML
    private void checkMon() {
    	updateCheckBox();
    }

    @FXML
    private void checkNE() {
    	updateCheckBox();
    }

    @FXML
    private void checkPM() {
    	updateCheckBox();
    }

    @FXML
    private void checkSat() {
    	updateCheckBox();
    }

    @FXML
    private void checkThu() {
    	updateCheckBox();
    }

    @FXML
    private void checkTue() {
    	updateCheckBox();
    }

    @FXML
    private void checkWLabTut() {
    	updateCheckBox();
    	
    }

    @FXML
    private void checkWed() {
    	updateCheckBox();
    }
    
    
    @FXML
    private void search() {
    	buttonSfqEnrollCourse.setDisable(false);
    	textAreaConsole.setText("");// clear any console output legacies
    	Section.resetNumUnique(); // reset Section count
    	Course.resetNumValidUnique(); // reset Course count
    	Instructor.reset(); // reset instructor list
    	backUpList.clear();
    	List<Course> v = scraper.scrape(textfieldURL.getText(), textfieldTerm.getText(),textfieldSubject.getText());
    	backUpList = convertToFirst5CourseCodes(v);
    	//added by jr, for use of filtering
    	this.myCourseList = v;
    	
    	// check if the scraper encountered 404 error (or other errors)
		if (v == null) {
			textAreaConsole.setText("Some errors occurred when scraping " + textfieldURL.getText());
		}
		else if (v.size()==1 & v.get(0).getTitle().substring(0,13).equals("404 Not Found")) {
			textAreaConsole.setText(v.get(0).getTitle());
		}
		else {
			String stats1 = "Total Number of difference sections in this search: " +  Integer.toString(Section.getNumUnique()) + "\n";
			String stats2 = "Total Number of Course in this search: " +  Integer.toString(Course.getNumValidUnique()) + "\n";
			String stats3 = "Instructors who has teaching assignment this term but does not need to teach at Tu 3:10pm: ";
			for (String name: Instructor.getSortedToKeepList()) {
				stats3 += name + ", ";
			}
			stats3 = stats3.substring(0, stats3.length()-1) + "\n";
			textAreaConsole.setText(textAreaConsole.getText() + "\n" + stats1 + stats2 + stats3);
	    	for (Course c : v) {
	
	    		String newline = c.getTitle() + "\n";
	    		for (int i = 0; i < c.getNumSlots(); i++) {
	    			Slot t = c.getSlot(i);
	    			newline += "Section " + t.getSectionCode() + " Slot " + i + ":" + t + "\n";
	    		}
	    		textAreaConsole.setText(textAreaConsole.getText() + "\n" + newline);
	    	}
		}
    	
		this.myUpdatedCourseList = scraper.scrape(textfieldURL.getText(), textfieldTerm.getText(),textfieldSubject.getText());
		
    	
    	
    }
   
    @FXML
    private void updateTimetable() {
    	List<Course> toDisplay = new ArrayList<Course>();
    	List<Slot> toDSlot = new ArrayList<Slot>();
    	double colorInterval = 360/(myEnrolledCourseList.size()+1);
        double color = 0;
        Set<String> hash_Set = new HashSet<String>(); 
        String lasname = "";
    	for (String s : myEnrolledCourseList) {
    		int spliter = s.indexOf('-');
    		String coursename = s.substring(0, spliter);
    		
    		if (!coursename.equals(lasname)) {
    			color+=colorInterval;
    		}
    		lasname = coursename;
    		String section = s.substring(spliter+1);
    		System.out.println("coursename"+coursename);
    		System.out.println("section"+section);
    		
    		for (Course c : myCourseList) {
    			
    			String name2  = c.getTitle();
    			int temp = name2.indexOf('-');
        		String coursename2 = name2.substring(0, temp-1);
        		System.out.println("name2 "+ coursename2);
    			for (int i = 0; i < c.getNumSlots(); i++) {
        			Slot t = c.getSlot(i);
        			t.setCourseName(c.getTitle());
        			String section2 = t.getSectionCode();
        			
            		System.out.println("section2 "+section2);
            		
            		if (coursename2.equals(coursename)&& section2.equals(section)) {
            			System.out.println("matched "+coursename2+section2);
            			t.setColor(color);
            			toDSlot.add(t);
            		}
        		}
    			
    		}
    	}
    	printTimeTable2(toDSlot);
    }
    @FXML 
    private void printTimeTable2(List<Slot> toDSlot) {
    	AnchorPane ap = (AnchorPane)tabTimetable.getContent();
    	ap.getChildren().removeAll(slotList);
    	slotList.clear();
    	
    	for (int i = 0; i<toDSlot.size();i++) {
			Slot curr = toDSlot.get(i);
			
			int temp = curr.getCourseName().indexOf('-');
			String LabelName = curr.getCourseName().substring(0, temp)+'\n'+curr.getSectionCode();
			Label randomLabel = new Label(LabelName);
			randomLabel.setFont (new Font(5.0));
			//randomLabel.setWrapText(true);
	    	double start = 40+(curr.getStartHour()+(curr.getStartMinute()+0.0)/60.0-9)*20;
	    	double height = (curr.getAbsEndTime() - curr.getAbsStartTime())/3.0;
	    
	    	
	    	randomLabel.setBackground(new Background(new BackgroundFill(Color.hsb(curr.getColor(), 1, 1), CornerRadii.EMPTY, Insets.EMPTY)));
	    	randomLabel.setLayoutX(curr.getDay()*100.0+100.0);
	    	randomLabel.setLayoutY(start);
	    	randomLabel.setMinWidth(100.0);
	    	randomLabel.setMaxWidth(100.0);
	    	randomLabel.setMinHeight(height);
	    	randomLabel.setMaxHeight(height);
	    	randomLabel.setOpacity(0.5);
	    	slotList.add(randomLabel);
	    
	    	
		}
    	ap.getChildren().addAll(slotList);
    	
    }
    


	@FXML
	public void initialize() {
		/*
		instructor.setCellValueFactory(new PropertyValueFactory<CourseList,String>("instructor"));
    	section.setCellValueFactory(new PropertyValueFactory<CourseList,String>("section"));
    	courseName.setCellValueFactory(new PropertyValueFactory<CourseList,String>("courseName"));
    	courseCode.setCellValueFactory(new PropertyValueFactory<CourseList,String>("courseCode"));
    	enroll.setCellValueFactory(new PropertyValueFactory<CourseList,String>("enroll"));
    	textAreaConsole.setText("hello");
    	tableListCourse.setItems(listInTable);
    	*/
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		courseCode.setCellValueFactory(new PropertyValueFactory<CourseList,String>("courseCode"));
    	section.setCellValueFactory(new PropertyValueFactory<CourseList,String>("section"));
    	courseName.setCellValueFactory(new PropertyValueFactory<CourseList,String>("courseName"));
    	instructor.setCellValueFactory(new PropertyValueFactory<CourseList,String>("instructor"));
    	enroll.setCellValueFactory(new PropertyValueFactory<CourseList,CheckBox>("enroll"));
    	
    	listInTable.clear();
    	listInTable.add(new CourseList("N/A","N/A","N/A","N/A"));
    	//myEnrolledCourseList = Arrays.asList("one", "two", "three");
    	//myEnrolledCourseList.clear();
    	//myEnrolledCourseList.add("start");
    	tableListCourse.setItems(listInTable);
	}
	
	private List<String> convertToFirst5CourseCodes(List<Course> courses){
		List<String> codes = new ArrayList<String>();
		int count = 0;
		for (Course course: courses) {
			if (count >= 5)
				break;
			String code = course.getTitle().split("\\-")[0].trim();
			if (codes.contains(code))
				continue;
			codes.add(code);
			count += 1;
		}
		return codes;
	}


}



