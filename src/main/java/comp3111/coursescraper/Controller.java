package comp3111.coursescraper;


import java.awt.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.control.CheckBox;
//import javafx.event.ActionEvent;

import java.util.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
public class Controller {

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
    
    private Scraper scraper = new Scraper();
    
    private List<Course> myCourseList;
    private List<Course> myUpdatedCourseList;
    
    @FXML
    void allSubjectSearch() {
    	buttonSfqEnrollCourse.setDisable(false);
    	BarThread bthread = new BarThread (progressbar);
    	bthread.start();
    	textAreaConsole.setText(textAreaConsole.getText() + "\n" + "starting all subject search");
    	
    	
    	List<String> allSubject = scraper.getAllSubjectName(textfieldURL.getText(), textfieldTerm.getText());
    	textAreaConsole.setText(textAreaConsole.getText() + "\n" + "Total Number of Categories/Code Prefix: " +allSubject.size());
    	
    	int totalNumOfCourses = 0;
    	double progressVal = 0;
    	double progressInterval = 100.0/allSubject.size();
    	for (String subject : allSubject) {
    		System.out.println(subject+" starts");
    		List<Course> v = scraper.scrape(textfieldURL.getText(), textfieldTerm.getText(), subject);
    		this.myCourseList = v;
    		for (Course c : v) {
        		String newline = c.getTitle() + "\n";
        		for (int i = 0; i < c.getNumSlots(); i++) {
        			Slot t = c.getSlot(i);
        			newline += "Section " + t.getSectionCode() + " Slot " + i + ":" + t + "\n";
        		}
        		textAreaConsole.setText(textAreaConsole.getText() + "\n" + newline);
        	}
    		totalNumOfCourses += v.size();
    		progressVal += progressInterval;
    		bthread.setPercentage(progressVal);
    		
    		System.out.println(subject+" is done");
    	}
    	textAreaConsole.setText(textAreaConsole.getText() + "\n" + "Total Number of Courses fetched: " +totalNumOfCourses);
    	
    	this.myUpdatedCourseList = scraper.scrape(textfieldURL.getText(), textfieldTerm.getText(),textfieldSubject.getText());
    }

    @FXML
    void findInstructorSfq() {
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
    void findSfqEnrollCourse() {
    	// create dummy list of course strings, need to use results from task 4 later.
    	List<String> TMP_COURSES = new ArrayList<String>();
    	TMP_COURSES.add("COMP 2012");
    	TMP_COURSES.add("CENG 4913");
    	TMP_COURSES.add("MECH 4000K");
    	TMP_COURSES.add("ENGG 4930A"); 
    	TMP_COURSES.add("CIVL 1100");
    	TMP_COURSES.add("LABU 1100");
    	TMP_COURSES.add("ELEC 1095A");
    	// scrape the list from website
    	List<String>results = scraper.scrapeSFQEnrolledCourses(textfieldSfqUrl.getText(), TMP_COURSES);
    	textAreaConsole.setText(""); // clear the console for new output.
    	// handle 404 error (or other types of errors).
    	if (results == null) {
			textAreaConsole.setText("Some errors occurred when scraping " + textfieldSfqUrl.getText());
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
    
    void updateCheckBox() {
    	
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
    		
        	if(c.getExclusion() == null)
        		thisNE = 1;
    		
        	if(c.getCommonCore() != null) {
        		thisCC = 1;
        		//textAreaConsole.setText(textAreaConsole.getText() + c.getCommonCore() + "\n");
        	}
        		thisCC = 1;
        	
    		int numSlots = c.getNumSlots();
    		
    		if(numSlots == 0)
    			invalidFlag = 1;
    		
    		for (int i = 0; i<numSlots; i++) {
    			int courseDay = c.getSlot(i).getDay();
    			
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
    	
    	return;
    	
    }
    
    boolean checkFlag(int f1, int f2, int f3, int f4, int f5, int f6, int f7, int f8, int f9, int f10, int f11, 
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
    void selectAllBoxes() {
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
    }
    
    

    @FXML
    void checkAM() {
    	updateCheckBox();
    }

    @FXML
    void checkCC() {
    	updateCheckBox();
    }

    @FXML
    void checkFri() {
    	updateCheckBox();
    }

    @FXML
    void checkMon() {
    	updateCheckBox();
    }

    @FXML
    void checkNE() {
    	updateCheckBox();
    }

    @FXML
    void checkPM() {
    	updateCheckBox();
    }

    @FXML
    void checkSat() {
    	updateCheckBox();
    }

    @FXML
    void checkThu() {
    	updateCheckBox();
    }

    @FXML
    void checkTue() {
    	updateCheckBox();
    }

    @FXML
    void checkWLabTut() {
    	updateCheckBox();
    	
    }

    @FXML
    void checkWed() {
    	updateCheckBox();
    }
    
    
    @FXML
    void search() {
    	buttonSfqEnrollCourse.setDisable(false);
    	textAreaConsole.setText("");// clear any console output legacies
    	Section.resetNumUnique(); // reset Section count
    	Course.resetNumValidUnique(); // reset Course count
    	Instructor.reset(); // reset instructor list
    	List<Course> v = scraper.scrape(textfieldURL.getText(), textfieldTerm.getText(),textfieldSubject.getText());
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
		
    	//Add a random block on Saturday
		
    	AnchorPane ap = (AnchorPane)tabTimetable.getContent();
    	List<Course> toDisplay = new ArrayList<Course>();
    	for (int i = 10; i< 11; i++) {
    		toDisplay.add(v.get(i));
    	}
    	List<List<Slot>> daySlots = new ArrayList<List<Slot>>();
    	
    	for (int i =0; i<6 ;i++) {
    		daySlots.add(new ArrayList<Slot>());
    	}
    	
    	Comparator<Slot> compareByStart = (Slot o1, Slot o2) ->
        o1.getAbsStartTime().compareTo( o2.getAbsStartTime() );
        
        Comparator<Slot> compareByEnd = (Slot o1, Slot o2) ->
        o1.getAbsEndTime().compareTo( o2.getAbsEndTime() );
        
		for (Course c: toDisplay) {
			for (int i = 0; i < c.getNumSlots(); i++) {
    			Slot t = c.getSlot(i);
    			t.setCourseName(c.getTitle());
    			int day = t.getDay();
    			daySlots.get(day).add(t);
    		}
		}
		List <Slot> overlappings = new ArrayList<Slot>();
		for (List<Slot> sls : daySlots) {
			Collections.sort(sls,compareByStart);
			for (int i = 0; i<sls.size();i++) {
				Slot curr = sls.get(i);
				if (i<sls.size()-1) {
				Slot next = sls.get(i+1);
				if (next.getAbsStartTime()<curr.getAbsEndTime()) {
					Slot overlap = next.clone();
					overlap.setLocalTimeEnd(curr.getEnd());
					overlappings.add(overlap);
					
				}
				}
				Label randomLabel = new Label(curr.getCourseName());
				
		    	double start = (curr.getAbsStartTime() + 1)/10;
		    	double height = curr.getAbsEndTime() - curr.getAbsStartTime();
		    	System.out.println(start);
		    	double hight = curr.getAbsEndTime() - curr.getAbsStartTime();
		    	randomLabel.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
		    	randomLabel.setLayoutX(curr.getDay()*100.0+100.0);
		    	randomLabel.setLayoutY(start);
		    	randomLabel.setMinWidth(100.0);
		    	randomLabel.setMaxWidth(100.0);
		    	randomLabel.setMinHeight(height);
		    	randomLabel.setMaxHeight(height);
		    	randomLabel.setOpacity(0.5);
		    
		    	ap.getChildren().addAll(randomLabel);
			}
			for (Slot overlap : overlappings) {
				Label randomLabel = new Label("");
				
		    	double start = (overlap.getAbsStartTime() + 1)/10;
		    	System.out.println(start);

		    	randomLabel.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
		    	randomLabel.setOpacity(0.5);
		    	randomLabel.setLayoutX(overlap.getDay()*100.0+100.0);
		    	randomLabel.setLayoutY(start);
		    	randomLabel.setMinWidth(100.0);
		    	randomLabel.setMaxWidth(100.0);
		    	double height = overlap.getAbsEndTime() - overlap.getAbsStartTime();
		    	randomLabel.setMinHeight(height);
		    	randomLabel.setMaxHeight(height);
		    
		    	//ap.getChildren().addAll(randomLabel);
			}
			
	    	
		}
    	
    	
    }

}

class BarThread extends Thread {
	  private static int DELAY = 50;

	  ProgressBar progressBar;
	  double percentage;

	  public BarThread(ProgressBar bar) {
		  progressBar = bar;
	  }
	  public void setPercentage(double p) {
		  percentage = p;
	  }

	  public void run() {
	   System.out.println("running barthread");
	   while (percentage < 99.9){
		   try {
			   progressBar.setProgress(percentage);
			   Thread.sleep(DELAY);
	      } catch (InterruptedException ignoredException) {
	      }
	    }
	  }
	}

