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
    
    @FXML
    void allSubjectSearch() {

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
    	
    }

    @FXML
    void findInstructorSfq() {
    	buttonInstructorSfq.setDisable(true);
    }

    @FXML
    void findSfqEnrollCourse() {

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
    }
    
    

    @FXML
    void checkAM() {
    	if(checkboxAM.isSelected() == true) {
    		//do sth
    		//checkboxAM.setSelected(false);
    	}
    	else {
    		//do sth
    		//checkboxAM.setSelected(true);
    	}
    }

    @FXML
    void checkCC() {
    	if(checkboxCC.isSelected()) {
    		//do sth
    		//checkboxCC.setSelected(false);
    	}
    	else {
    		//do sth
    		//checkboxCC.setSelected(true);
    	}
    }

    @FXML
    void checkFri() {
    	if(checkboxFriday.isSelected()) {
    		//do sth
    		//checkboxFriday.setSelected(false);
    	}
    	else {
    		//do sth
    		//checkboxFriday.setSelected(true);
    	}
    }

    @FXML
    void checkMon() {
    	if(checkboxMonday.isSelected()) {
    		//do sth
    		//checkboxMonday.setSelected(false);
    	}
    	else {
    		//do sth
    		//checkboxMonday.setSelected(true);
    	}
    }

    @FXML
    void checkNE() {
    	if(checkboxNoExclusion.isSelected()) {
    		//do sth
    		//checkboxNoExclusion.setSelected(false);
    	}
    	else {
    		//do sth
    		//checkboxNoExclusion.setSelected(true);
    	}
    }

    @FXML
    void checkPM() {
    	if(checkboxPM.isSelected()) {
    		//do sth
    		//checkboxPM.setSelected(false);
    	}
    	else {
    		//do sth
    		//checkboxPM.setSelected(true);
    	}
    }

    @FXML
    void checkSat() {
    	if(checkboxSaturday.isSelected()) {
    		//do sth
    		//checkboxSaturday.setSelected(false);
    	}
    	else {
    		//do sth
    		//checkboxSaturday.setSelected(true);
    	}
    }

    @FXML
    void checkThu() {
    	if(checkboxThursday.isSelected()) {
    		//do sth
    		//checkboxThursday.setSelected(false);
    	}
    	else {
    		//do sth
    		//checkboxThursday.setSelected(true);
    	}
    }

    @FXML
    void checkTue() {
    	if(checkboxTuesday.isSelected()) {
    		//do sth
    		//checkboxTuesday.setSelected(false);
    	}
    	else {
    		//do sth
    		//checkboxTuesday.setSelected(true);
    	}
    }

    @FXML
    void checkWLabTut() {
    	if(checkboxWithLabTut.isSelected()) {
    		//do sth
    		//checkboxWithLabTut.setSelected(false);
    	}
    	else {
    		//do sth
    		//checkboxWithLabTut.setSelected(true);
    	}
    	
    }

    @FXML
    void checkWed() {
    	if(checkboxWednesday.isSelected()) {
    		//do sth
    		//checkboxWednesday.setSelected(false);
    	}
    	else {
    		//do sth
    		//checkboxWednesday.setSelected(true);
    	}
    }
    
    
    @FXML
    void search() {
    	
    	textAreaConsole.setText("");// clear any console output legacies
    	Section.resetNumUnique(); // reset Section count
    	Course.resetNumValidUnique(); // reset Course count
    	Instructor.reset(); // reset instructor list
    	List<Course> v = scraper.scrape(textfieldURL.getText(), textfieldTerm.getText(),textfieldSubject.getText());
    	// check if the scraper encountered 404 error
		if (!v.isEmpty() & v.get(0).getTitle().substring(0,13).equals("404 Not Found")) {
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
    	
    	//Add a random block on Saturday
    	AnchorPane ap = (AnchorPane)tabTimetable.getContent();
    	Label randomLabel = new Label("COMP1022\nL1");
    	Random r = new Random();
    	double start = (r.nextInt(10) + 1) * 20 + 40;

    	randomLabel.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
    	randomLabel.setLayoutX(600.0);
    	randomLabel.setLayoutY(start);
    	randomLabel.setMinWidth(100.0);
    	randomLabel.setMaxWidth(100.0);
    	randomLabel.setMinHeight(60);
    	randomLabel.setMaxHeight(60);
    
    	ap.getChildren().addAll(randomLabel);
    	
    	
    	
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

