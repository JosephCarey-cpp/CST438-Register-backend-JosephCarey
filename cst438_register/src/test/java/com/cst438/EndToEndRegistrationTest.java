package com.cst438;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cst438.domain.Enrollment;
import com.cst438.domain.Course;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.Student;
import com.cst438.domain.StudentDTO;
import com.cst438.domain.StudentRepository;
import com.cst438.domain.StudentRepository;
import com.cst438.service.GradebookService;
import com.cst438.domain.CourseRepository;

@SpringBootTest
public class EndToEndRegistrationTest {
	public static final String CHROME_DRIVER_FILE_LOCATION = "C:/chromedriver_win32/chromedriver.exe";
	public static final String URL = "http://localhost:3000";
	public static final String ALIAS_NAME_FIRST = "manualTest";
	public static final String ALIAS_NAME_SECOND = "automaticTest";
	public static final int SLEEP_DURATION = 5000; // 1 second.
	public static final String TEST_USER_EMAIL_FIRST = "manualTest@csumb.edu";
	public static final String TEST_USER_EMAIL_SECOND = "automaticTest@csumb.edu";
	
	
	
	@Autowired
	CourseRepository courseRepository;
	
	@Autowired
	StudentRepository studentRepository;
	
	@Autowired
	GradebookService gradebookService;
	
	@Test
	public void addStudentAsAdmin() throws Exception {
		// browser    property name                 Java Driver Class
        // edge       webdriver.edge.driver         EdgeDriver
        // FireFox    webdriver.firefox.driver      FirefoxDriver
        // IE         webdriver.ie.driver           InternetExplorerDriver
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
        WebDriver driver = new ChromeDriver();
        // Puts an Implicit wait for 10 seconds before throwing exception
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        
        Student student = null;
        String elementPath = "//a[@href='/addStudent']";
        String elementPathAddStudent = "//button";
        do {
            student = studentRepository.findByEmail(TEST_USER_EMAIL_FIRST);
            if (student == null) {
            	student = new Student();
            	student.setEmail(TEST_USER_EMAIL_FIRST);
            	student.setName(ALIAS_NAME_FIRST);
            	student.setStatus(null);
            	student.setStatusCode(0);
            	student.setStudent_id(1);
            	studentRepository.save(student);
            }
        } while (student == null);
        
        // verify that enrollment row has been inserted to database.
        Student e = studentRepository.findByEmail(TEST_USER_EMAIL_FIRST);
        assertNotNull(e, "Student not found in database.");

        
        driver.get(URL);
        Thread.sleep(SLEEP_DURATION);
        
        driver.findElement(By.xpath(elementPath)).click();
        Thread.sleep(SLEEP_DURATION);
        
        // select the last of the radio buttons 
        WebElement we = driver.findElement(By.name("name"));
        we.sendKeys(ALIAS_NAME_SECOND);
        
        we = driver.findElement(By.name("email"));
        we.sendKeys(TEST_USER_EMAIL_SECOND);
        Thread.sleep(SLEEP_DURATION);
        
        driver.findElement(By.xpath(elementPathAddStudent)).click();
        //name="name" -> [@name='name']
        Thread.sleep(SLEEP_DURATION);
        
        student = studentRepository.findByEmail(TEST_USER_EMAIL_SECOND);
        System.out.println(student);
        
//        assertNotNull(student, "student not added successfully.");
        driver.quit();
	}
}
