package comp3111.coursescraper;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.DomText;
import java.util.Vector;

/**
 * WebScraper provide a sample code that scrape web content. After it is constructed, you can call the method scrape with a keyword, 
 * the client will go to the default url and parse the page by looking at the HTML DOM.  
 * <br>
 * In this particular sample code, it access to HKUST class schedule and quota page (COMP). 
 * <br>
 * https://w5.ab.ust.hk/wcq/cgi-bin/1830/subject/COMP
 *  <br>
 * where 1830 means the third spring term of the academic year 2018-19 and COMP is the course code begins with COMP.
 * <br>
 * Assume you are working on Chrome, paste the url into your browser and press F12 to load the source code of the HTML. You might be freak
 * out if you have never seen a HTML source code before. Keep calm and move on. Press Ctrl-Shift-C (or CMD-Shift-C if you got a mac) and move your
 * mouse cursor around, different part of the HTML code and the corresponding the HTML objects will be highlighted. Explore your HTML page from
 * body &rarr; div id="classes" &rarr; div class="course" &rarr;. You might see something like this:
 * <br>
 * <pre>
 * {@code
 * <div class="course">
 * <div class="courseanchor" style="position: relative; float: left; visibility: hidden; top: -164px;"><a name="COMP1001">&nbsp;</a></div>
 * <div class="courseinfo">
 * <div class="popup attrword"><span class="crseattrword">[3Y10]</span><div class="popupdetail">CC for 3Y 2010 &amp; 2011 cohorts</div></div><div class="popup attrword"><span class="crseattrword">[3Y12]</span><div class="popupdetail">CC for 3Y 2012 cohort</div></div><div class="popup attrword"><span class="crseattrword">[4Y]</span><div class="popupdetail">CC for 4Y 2012 and after</div></div><div class="popup attrword"><span class="crseattrword">[DELI]</span><div class="popupdetail">Mode of Delivery</div></div>	
 *    <div class="courseattr popup">
 * 	    <span style="font-size: 12px; color: #688; font-weight: bold;">COURSE INFO</span>
 * 	    <div class="popupdetail">
 * 	    <table width="400">
 *         <tbody>
 *             <tr><th>ATTRIBUTES</th><td>Common Core (S&amp;T) for 2010 &amp; 2011 3Y programs<br>Common Core (S&amp;T) for 2012 3Y programs<br>Common Core (S&amp;T) for 4Y programs<br>[BLD] Blended learning</td></tr><tr><th>EXCLUSION</th><td>ISOM 2010, any COMP courses of 2000-level or above</td></tr><tr><th>DESCRIPTION</th><td>This course is an introduction to computers and computing tools. It introduces the organization and basic working mechanism of a computer system, including the development of the trend of modern computer system. It covers the fundamentals of computer hardware design and software application development. The course emphasizes the application of the state-of-the-art software tools to solve problems and present solutions via a range of skills related to multimedia and internet computing tools such as internet, e-mail, WWW, webpage design, computer animation, spread sheet charts/figures, presentations with graphics and animations, etc. The course also covers business, accessibility, and relevant security issues in the use of computers and Internet.</td>
 *             </tr>	
 *          </tbody>
 *      </table>
 * 	    </div>
 *    </div>
 * </div>
 *  <h2>COMP 1001 - Exploring Multimedia and Internet Computing (3 units)</h2>
 *  <table class="sections" width="1012">
 *   <tbody>
 *    <tr>
 *        <th width="85">Section</th><th width="190" style="text-align: left">Date &amp; Time</th><th width="160" style="text-align: left">Room</th><th width="190" style="text-align: left">Instructor</th><th width="45">Quota</th><th width="45">Enrol</th><th width="45">Avail</th><th width="45">Wait</th><th width="81">Remarks</th>
 *    </tr>
 *    <tr class="newsect secteven">
 *        <td align="center">L1 (1765)</td>
 *        <td>We 02:00PM - 03:50PM</td><td>Rm 5620, Lift 31-32 (70)</td><td><a href="/wcq/cgi-bin/1830/instructor/LEUNG, Wai Ting">LEUNG, Wai Ting</a></td><td align="center">67</td><td align="center">0</td><td align="center">67</td><td align="center">0</td><td align="center">&nbsp;</td></tr><tr class="newsect sectodd">
 *        <td align="center">LA1 (1766)</td>
 *        <td>Tu 09:00AM - 10:50AM</td><td>Rm 4210, Lift 19 (67)</td><td><a href="/wcq/cgi-bin/1830/instructor/LEUNG, Wai Ting">LEUNG, Wai Ting</a></td><td align="center">67</td><td align="center">0</td><td align="center">67</td><td align="center">0</td><td align="center">&nbsp;</td>
 *    </tr>
 *   </tbody>
 *  </table>
 * </div>
 *}
 *</pre>
 * <br>
 * The code 
 * <pre>
 * {@code
 * List<?> items = (List<?>) page.getByXPath("//div[@class='course']");
 * }
 * </pre>
 * extracts all result-row and stores the corresponding HTML elements to a list called items. Later in the loop it extracts the anchor tag 
 * &lsaquo; a &rsaquo; to retrieve the display text (by .asText()) and the link (by .getHrefAttribute()).   
 * 
 *
 */
public class Scraper {
	private WebClient client;

	/**
	 * Default Constructor 
	 */
	public Scraper() {
		client = new WebClient();
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);
	}

	private void addSlot(HtmlElement e, Course c, boolean secondRow, String sectionCode) {
		String times[] =  e.getChildNodes().get(secondRow ? 0 : 3).asText().split(" ");
		String venue = e.getChildNodes().get(secondRow ? 1 : 4).asText();
		String instructorForThisSection = e.getChildNodes().get(secondRow ? 2 : 5).getChildNodes().get(0).asText();
		
		if (times[0].equals("TBA"))
			return;
		boolean tu310pmFlag = false; // for checking if the slot covers Tuesday 3:10pm
		for (int j = 0; j < times[0].length(); j+=2) {
			String code = times[0].substring(j , j + 2);
			if (Slot.DAYS_MAP.get(code) == null)
				break;
			Slot s = new Slot();
			s.setDay(Slot.DAYS_MAP.get(code));
			s.setStart(times[1]);
			s.setEnd(times[3]);
			Time time1 = new Time(times[1]);
			Time time2 = new Time(times[3]);
			if (time1.compares(new Time("03:10pm")) == -1 & time2.compares(new Time("03:10pm")) == 1 & 
					code.equals("Tu")) {
				tu310pmFlag = true;
			}
			s.setVenue(venue);
			s.setSectionCode(sectionCode);
			s.setInstructor(instructorForThisSection);
			c.addSlot(s);	
		}
		
		for (DomNode instructor_line: e.getChildNodes().get(secondRow ? 2 : 5).getChildNodes()) {
			if (!instructor_line.asText().isEmpty() & !instructor_line.asText().equals(" ")) {
				String instructor = instructor_line.asText();
				Instructor.addAll(instructor);
				if (tu310pmFlag)
					Instructor.addToRemove(instructor);
			}
		}

	}
	
	/*
	 * scrape the list of courses
	 * @param a string of base url
	 * @return the list of courses
	 */
	public List<Course> scrape(String baseurl, String term, String sub) {

		try {
			
			HtmlPage page = client.getPage(baseurl + "/" + term + "/subject/" + sub);

			
			List<?> items = (List<?>) page.getByXPath("//div[@class='course']");
			
			Vector<Course> result = new Vector<Course>();

			for (int i = 0; i < items.size(); i++) {
				Course c = new Course();
				HtmlElement htmlItem = (HtmlElement) items.get(i);
				
				HtmlElement title = (HtmlElement) htmlItem.getFirstByXPath(".//h2");
				c.setTitle(title.asText());
				
				List<?> popupdetailslist = (List<?>) htmlItem.getByXPath(".//div[@class='popupdetail']/table/tbody/tr");
				HtmlElement exclusion = null;
				for ( HtmlElement e : (List<HtmlElement>)popupdetailslist) {
					HtmlElement t = (HtmlElement) e.getFirstByXPath(".//th");
					HtmlElement d = (HtmlElement) e.getFirstByXPath(".//td");
					if (t.asText().equals("EXCLUSION")) {
						exclusion = d;
					}
				}
				c.setExclusion((exclusion == null ? "null" : exclusion.asText()));
				
				List<?> popupdetailslist2 = (List<?>) htmlItem.getByXPath(".//div[@class='popupdetail']/table/tbody/tr");
				HtmlElement commonCore = null;
				for ( HtmlElement e : (List<HtmlElement>)popupdetailslist2) {
					HtmlElement t = (HtmlElement) e.getFirstByXPath(".//th");
					HtmlElement d = (HtmlElement) e.getFirstByXPath(".//td");
					if (t.asText().equals("ATTRIBUTES")) {
						commonCore = d;
					}
				}
				c.setCommonCore((commonCore == null ? "null" : commonCore.asText()));
				
				List<?> sections = (List<?>) htmlItem.getByXPath(".//tr[contains(@class,'newsect')]");
				boolean validCourseFlag = false; // indicate whether this course, c, has at least one valid section
				for ( HtmlElement e: (List<HtmlElement>)sections) {
					Section s = new Section(e.getChildNodes().get(1).asText());
					Section.increaseNumUnique();
					if (!s.isValid())
						continue;
					validCourseFlag = true;
					addSlot(e, c, false, s.getSectionCode());
					e = (HtmlElement)e.getNextSibling();
					if (e != null && !e.getAttribute("class").contains("newsect"))
						addSlot(e, c, true, s.getSectionCode());

				}
				
				//if this course contains at least a valid section, then increase the count of total num of courses
				if (validCourseFlag)
					Course.increaseNumValidUnique(); 
				
				result.add(c);
			}
			client.close();
			return result;
		} catch (Exception e) {
			if (e instanceof com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException & 
					e.getMessage().substring(0,3).equals("404")) {
				Vector<Course> notfound = new Vector<Course>(); // a "notfound" list of course
				Course c= new Course();
				c.setTitle(e.getMessage());
				notfound.add(c);
				return notfound;
			}
			else {
				System.out.println(e);
			}
		}
		return null;
	}
	
	public List<String> getAllSubjectName (String baseurl, String term){
		try {
			HtmlPage page = client.getPage(baseurl +'/'+ term +'/');
			//sample Xpath : //*[@id="navigator"]/div[2]/a[21] //*[@id="navigator"]/div[2]/a[15] //*[@id="navigator"]/div[2]/a[40]
			List<?> courseNameItems = (List<?>) page.getByXPath("//div[@id='navigator']/div[2]/a");
			List<String> result = new Vector<String>();
			
			for (int i = 0; i < courseNameItems.size(); i++) {
				HtmlElement htmlItem = (HtmlElement) courseNameItems.get(i);
				result.add(htmlItem.asText());
				
			}
			client.close();
			return result;
			
		} catch (Exception e) {
			System.out.println("Error when getAllSubjectSearch : ");
			System.out.println(e);
		}
		
		return null;
	}
	

	public List<String> scrapeSFQEnrolledCourses(String baseurl, List<String> enrolledCourses){
		try {
			//dummy url for testing
			List<String> results = new ArrayList<String>();
			HtmlPage page = client.getPage(baseurl); 
			List<?> items = (List<?>) page.getByXPath(".//td[@colspan='3']");
			
			//parse the html for target courses
			List<HtmlElement> enrolledNodes = new ArrayList<HtmlElement>();
			for (HtmlElement item: (List<HtmlElement>)items) {
				String[] name_code = item.asText().split(" ");
				if (name_code[1].length()!=4 | name_code[2].length()<4 | name_code[2].length()>5) {
					continue;
				}
				if (enrolledCourses.contains(name_code[1] + ' ' + name_code[2])) {
					enrolledNodes.add(item);
					enrolledCourses.remove(name_code[1] + ' ' + name_code[2]);
				}
				else 
					continue;
				
				//retrieve corresponding section(s)'s scores
				HtmlElement parent = (HtmlElement)item.getParentNode();
				float totalScore=0;
				int count=0;
				while (true) {
					HtmlElement next = (HtmlElement)parent.getNextElementSibling();
					parent = next;
					List<HtmlElement> columnNodes = new ArrayList<HtmlElement>();
					for (DomElement child: next.getChildElements()) {
						columnNodes.add((HtmlElement) child);
					}
					//end at "department overall line" or next course's line
					if (columnNodes.size()< 8 | ((HtmlElement)columnNodes.get(0)).asText().trim().length()>3) {
						break;
					}
					//skip instructor's line
					if (((HtmlElement) columnNodes.get(1)).asText().trim().length() <= 1){
						continue;
					}
					//finally pinpoint the section's line
					String scoreString = ((HtmlElement) columnNodes.get(3)).asText().substring(0, 4);
					try {
						totalScore +=  Float.parseFloat(scoreString);
						count += 1;
					}catch (Exception e) {
						System.out.println(e);
					}
				}
				if (count > 0){
					float aveScore = totalScore/count;
					results.add(name_code[1] + ' ' + name_code[2] + " unadjusted average score: " + Float.toString(aveScore) + ".");
				}
				else {
					results.add("Cannot identify the score of " + name_code[1] + ' ' + name_code[2] + ".");
				}
			}
			for (String notFoundCourse: enrolledCourses) {
				results.add(notFoundCourse + " not found in " + baseurl);
			}
			client.close();
			return results;
		} catch (Exception e) {
			// still only look for 404 error
			if (e instanceof com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException & 
					e.getMessage().substring(0,3).equals("404")) {
				List<String> notfound = new ArrayList<String>(); // a "notfound" list of course
				notfound.add(e.getMessage());
				return notfound;
			}
			else {
				System.out.println(e);
			}
		}
		return null;
	}
	
	/*
	 * scrape the list of SFQInstructors
	 * @param a string of base url
	 * @return the list of SFQInstructors
	 */
	public List<String> scrapeSFQInstructors(String baseurl){
		Map<String, Instructor> name_instructor_map = new HashMap<String, Instructor>();
		try {
			HtmlPage page = client.getPage(baseurl); 
			List<?> rows = (List<?>) page.getElementsByTagName("tr");
			for (HtmlElement row: (List<HtmlElement>)rows) {
				// collect child elements
				List<HtmlElement> columnNodes = new ArrayList<HtmlElement>();
				for (DomElement child: row.getChildElements()) {
					columnNodes.add((HtmlElement) child);
				}
				
				// if not a instructor's row, ignored
				if (columnNodes.size()!=8) {
					continue;
				}
				if (columnNodes.get(0).asText().trim().length() != 0 |
						columnNodes.get(1).asText().trim().length() != 0 | columnNodes.get(2).asText().trim().length() <= 2) {
					continue;
				}
				
				// check if the score is convertable to float
				float score;
				try {
					String scoreString = ((HtmlElement) columnNodes.get(4)).asText().substring(0, 4);
					score = Float.parseFloat(scoreString);
				}
				catch(Exception e) {
					System.out.println(e);
					System.out.println("Cannot idenfify the score of " + columnNodes.get(2).asText().trim() 
							+ " in scrapeSFQInstructors(): ");
					continue;
				}
				
				//update map
				if (name_instructor_map.containsKey(columnNodes.get(2).asText().trim())) {
					// map already has the instructor's record
					name_instructor_map.get(columnNodes.get(2).asText().trim()).addSFQScore(score);
				}
				else {
					name_instructor_map.put(columnNodes.get(2).asText().trim(), new Instructor(score));
				}
			}
			client.close();
		} catch (Exception e) {
			// still only looking for 404 error
			if (e instanceof com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException & 
					e.getMessage().substring(0,3).equals("404")) {
				List<String> notfound = new ArrayList<String>(); // a "notfound" list of course
				notfound.add(e.getMessage());
				return notfound;
			}
			else {
				System.out.println(e);
			}
		}
		// convert the map to strings for display
		List<String> results = new ArrayList<String>();
		for (Map.Entry e: name_instructor_map.entrySet()) {
			results.add("Instructor: " + e.getKey() + "; average SFQ score: " + 
							Float.toString(((Instructor)e.getValue()).getAverageSFQScore()) + "; #Sections: " + 
							Integer.toString(((Instructor)e.getValue()).getNumSections()));
		}
		return results;
	}
	
	
	
}
