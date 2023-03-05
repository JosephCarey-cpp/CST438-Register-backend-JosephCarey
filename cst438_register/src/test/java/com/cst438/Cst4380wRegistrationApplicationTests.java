package com.cst438;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

@SpringBootTest
class Cst4380wRegistrationApplicationTests {
	
	static final String URL = "http://localhost:8080";
	public static final int TEST_STUDENT_ID = 10;
	public static final String TEST_STUDENT_EMAIL = "test@csumb.edu";
	public static final String TEST_STUDENT_NAME  = "test";
	public static final int TEST_STATUS_CODE = 1;
	public static final String TEST_STATUS = "HOLD";


	@MockBean
	StudentRepository studentRepository;

	@Autowired
	private MockMvc mvc;
	
	
	@Test
	void contextLoads() throws Exception {
		MockHttpServletResponse response;
		
		Student testStudent = new Student();
		testStudent.setStudent_id(TEST_STUDENT_ID);
		testStudent.setEmail(TEST_STUDENT_EMAIL);
		testStudent.setName(TEST_STUDENT_NAME);
		testStudent.setStatusCode(TEST_STATUS_CODE);
		testStudent.setStatus(TEST_STATUS);
		
		given(studentRepository.findByEmail(TEST_STUDENT_EMAIL)).willReturn(testStudent);
		
		StudentDTO testDTO = ScheduleController.createStudentDTO(testStudent);
		
		response = mvc.perform(
				MockMvcRequestBuilders
			      .post("/addStudent")
			      .content(asJsonString(testDTO))
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		// verify that return status = OK (value 200) 
		assertEquals(200, response.getStatus());
	}


	private static String asJsonString(final Object obj) {
		try {

			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
