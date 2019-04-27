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
    private TextArea textAreaConsole;
    
    private Scraper scraper = new Scraper();
    
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
    void search() {
    	buttonSfqEnrollCourse.setDisable(false);
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

