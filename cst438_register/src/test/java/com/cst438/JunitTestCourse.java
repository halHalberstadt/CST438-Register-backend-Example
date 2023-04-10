package com.cst438;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cst438.controller.ScheduleController;
import com.cst438.controller.ScratchController;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.ScheduleDTO;
import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;
import com.cst438.service.GradebookService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ContextConfiguration(classes = { ScratchController.class })
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest
public class JunitTestCourse {
	
	static final String URL = "http://localhost:8080";
	public static final int TEST_COURSE_ID = 54116;
	public static final int TEST_YEAR = 2024;
	public static final int TEST_SECTION = 9;
	public static final String TEST_SEMESTER = "Spring";
	public static final String TEST_TITLE = "CST 000 - Example Course";
	public static final String TEST_TIMES = "Su 15:00PM - 1:50AM";
	public static final String TEST_INSTRUCTOR = "Isee@but.why";
	public static final String TEST_ROOM = "outside";
	public static final String TEST_BUILDING = "TBA";
	// comment added

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
	
	@Test
	public void updateCourse() throws Exception {
		
		MockHttpServletResponse response;
		
		// setup test course
		Course course = new Course();
		course.setCourse_id(TEST_COURSE_ID);
		course.setSemester(TEST_SEMESTER);
		course.setYear(TEST_YEAR);
		course.setTitle(TEST_TITLE);
		course.setTimes(TEST_TIMES);
		course.setRoom(TEST_ROOM);
		course.setBuilding(TEST_BUILDING);
		course.setInstructor(TEST_INSTRUCTOR);
		course.setSection(TEST_SECTION);
		
		
		// sets the start and end date of the course
		Calendar c = Calendar.getInstance();
		c.set(2012,  8,  16);
		course.setStart(new java.sql.Date( c.getTimeInMillis() ));
		c.set(2021,  6, 5);
		course.setEnd(new java.sql.Date( c.getTimeInMillis() ));

	    given(courseRepository.findById(TEST_COURSE_ID)).willReturn(Optional.of(course));
	    given(courseRepository.findById(100).orElse(null)).willReturn(null);
//	    given(courseRepository.save(course)).willReturn(course);
	    
//		succeeds
	    response = mvc.perform(
				MockMvcRequestBuilders
			      .put("/updateCourse")
			      .content(asJsonString(course))
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
	    
	    // test succeed
	    assertEquals(200, response.getStatus());
	    
	    // verify save occurred
		verify(courseRepository).save(any(Course.class));
	    
	    // test content
//	    ScheduleDTO.CourseDTO result = fromJsonString(response.getContentAsString(), ScheduleDTO.CourseDTO.class);
//	    
//	    ScheduleDTO.CourseDTO expectedResult = new ScheduleDTO.CourseDTO();
//	    expectedResult.course_id = course.getCourse_id();
//	    expectedResult.building = course.getBuilding();
//	    expectedResult.startDate = course.getStart().toString();
//		expectedResult.endDate = course.getEnd().toString();
//		expectedResult.room = course.getRoom();
//		expectedResult.instructor = course.getInstructor();
//		expectedResult.title = course.getTitle();
//		expectedResult.section = course.getSection();
//		expectedResult.times = course.getTimes();
//	    
//	    assertEquals(asJsonString(expectedResult), asJsonString(result));
	    
//		fails
	    response = mvc.perform(
				MockMvcRequestBuilders
			      .put("/updateCourse")
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
	    
	    assertEquals(400, response.getStatus());
	    
	    response = mvc.perform(
				MockMvcRequestBuilders
			      .post("/updateCour")
			      .content(asJsonString(course))
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
	    
	    assertEquals(404, response.getStatus());
	    
	}
}
