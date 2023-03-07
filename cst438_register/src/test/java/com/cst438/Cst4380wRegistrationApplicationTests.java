package com.cst438;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.cst438.controller.ScheduleController;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.Student;
import com.cst438.domain.StudentDTO;
import com.cst438.domain.StudentRepository;
import com.cst438.service.GradebookService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ContextConfiguration(classes = { ScheduleController.class })
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest
class Cst4380wRegistrationApplicationTests {
	
	static final String URL = "http://localhost:8080";
	public static final int TEST_STUDENT_ID = 10;
	public static final String TEST_STUDENT_EMAIL = "james@csumb.com";
	public static final String TEST_STUDENT_NAME  = "james";
	public static final int TEST_STATUS_CODE = 0;
	public static final String TEST_STATUS = "";


	@MockBean
    CourseRepository courseRepository;
	
	@MockBean
	StudentRepository studentRepository;
	
	@MockBean
	EnrollmentRepository enrollmentRepository;

	@MockBean
	GradebookService gradebookService;
	
	@Autowired
	private MockMvc mvc;
	
	
	@Test
	void addStudent() throws Exception {
		MockHttpServletResponse response;
		
		Student testStudent = new Student();
		testStudent.setStudent_id(TEST_STUDENT_ID);
		testStudent.setEmail(TEST_STUDENT_EMAIL);
		testStudent.setName(TEST_STUDENT_NAME);
		testStudent.setStatusCode(TEST_STATUS_CODE);
		testStudent.setStatus(TEST_STATUS);
		
		given(studentRepository.findByEmail(TEST_STUDENT_EMAIL)).willReturn(testStudent);
		
		StudentDTO testDTO = ScheduleController.createStudentDTO(testStudent);
		System.out.println("DEBUG: " + asJsonString(testDTO));
		response = mvc.perform(
				MockMvcRequestBuilders
			      .get("/addStudent?email="+TEST_STUDENT_EMAIL+"&name="+TEST_STUDENT_NAME)
			      .content(asJsonString(testDTO))
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		// verify that return status = OK (value 200) 
		assertEquals(200, response.getStatus());
		System.out.println("DEBUG: Response"+ response.getContentAsString());
		Student result = fromJsonString(response.getContentAsString(), Student.class);
        assertNotEquals( 0  , result.getStudent_id());
        
        verify(studentRepository).save(any(Student.class));
	}


	private static String asJsonString(final Object obj) {
		try {

			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private static <T> T  fromJsonString(String str, Class<T> valueType ) {
		try {
			return new ObjectMapper().readValue(str, valueType);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
