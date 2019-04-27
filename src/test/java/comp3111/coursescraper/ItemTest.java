package comp3111.coursescraper;


import org.junit.Test;

import comp3111.coursescraper.Course;

import static org.junit.Assert.*;


public class ItemTest {

	@Test
	public void testSetTitle() {
		Course i = new Course();
		i.setTitle("ABCDE");
		assertEquals(i.getTitle(), "ABCDE");
	}
	
	@Test
	public void testSetDescription() {
		Course i = new Course();
		i.setDescription("ABCDEFG");
		assertEquals(i.getDescription(), "ABCDEFG");	
	}
	
	@Test
	public void testAddSlot() {
		Course c =  new Course();
		for (int i=0; i < 22; i+=1) {
			Slot s = new Slot();
			c.addSlot(s);
		}
		assertEquals(c.getNumSlots(), 20);
	}
}
