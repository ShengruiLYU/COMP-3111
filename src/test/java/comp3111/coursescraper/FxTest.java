/**
 * 
 * You might want to uncomment the following code to learn testFX. Sorry, no tutorial session on this.
 * 
 */
package comp3111.coursescraper;

import static org.junit.Assert.*;

import org.junit.Test;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit.ApplicationTest;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;


public class FxTest extends ApplicationTest {

	private Scene s;
	
	@Override
	public void start(Stage stage) throws Exception {
    	FXMLLoader loader = new FXMLLoader();
    	loader.setLocation(getClass().getResource("/ui.fxml"));
   		VBox root = (VBox) loader.load();
   		Scene scene =  new Scene(root);
   		stage.setScene(scene);
   		stage.setTitle("Course Scraper");
   		stage.show();
   		s = scene;
	}
	
	@Test
	public void testSearchAndSearchSFQ() {
		Button b = (Button)s.lookup("#buttonSfqEnrollCourse");
		assertTrue(b.isDisabled());
		clickOn("#tabMain");
		clickOn("#buttonSearch");
		sleep(4000);
		assertTrue(!b.isDisabled());
	
		// searchSFQ
		clickOn("#tabSfq");
		clickOn("#textfieldSfqUrl");
		eraseText(30);
		type(KeyCode.DELETE, 30);
		write("http://ywangdr.student.ust.hk/wp-content/uploads/2019/04/School_Summary_Report.html");
		clickOn("#buttonSfqEnrollCourse");
		sleep(3000);
		clickOn("#buttonInstructorSfq");
		sleep(2000);
	}
	
	@Test
	public void test404() {
		//test 404
		clickOn("#tabMain");
		clickOn("#textfieldURL");
		type(KeyCode.DELETE, 3);
		clickOn("#buttonSearch");
		sleep(2000);
	}
//	
	@Test
	public void testSearchAll() {
		clickOn("#tabAllSubject");
		clickOn("#buttonAllSubjectSearch");
		sleep(2000);
	}
	
	@Test
	public void testFilter() {
		clickOn("#tabMain");
		clickOn("#buttonSearch");
		sleep(4000);
		clickOn("#tabFilter");
		clickOn("#buttonSelectAll");
		clickOn("#buttonSelectAll");
		clickOn("#checkboxMonday");
	}
}
