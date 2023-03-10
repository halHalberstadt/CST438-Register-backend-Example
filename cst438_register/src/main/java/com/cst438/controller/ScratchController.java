package com.cst438.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.ScheduleDTO;
import com.cst438.domain.ScheduleDTO.CourseDTO;
import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;
import com.cst438.service.GradebookService;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://registerf-cst438.herokuapp.com/"})
public class ScratchController {
	
	// Autowire all necessary components
	@Autowired
	CourseRepository courseRepository;
	
	@Autowired
	StudentRepository studentRepository;
	
	@Autowired
	EnrollmentRepository enrollmentRepository;
	
	@Autowired
	GradebookService gradebookService;
	
	/*
	 * TODO URI's For example(change course)
	 *  
	 */
	
	/*
	 * TODO Create Course
	 * POST mapping - add a entirely new obj/entry
	 * /createCourse?param=val&param2=val2
	 */
	
	@PostMapping("/createCourse")
	public Course createCourse( @RequestParam("id") int id, @RequestParam("year") int year, @RequestParam("semester") String semester, @RequestParam("title") String title ) {
		// Need to get data from URI ^
		// Format the data from a DTO to a regular object
		Course course = new Course();
		course.setCourse_id(id);
		course.setYear(year);
		course.setSemester(semester);
		course.setTitle(title);
		// ...
		
		// check to make sure not duplicating data/ check to make sure there and exact copy
		Course existingCourse = courseRepository.findById(id).orElse(null);
		
		if(existingCourse != null) {
			throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "Course_id already taken");
		}
		// save the new data into the database, save also updates
		courseRepository.save(course);
		// give a success (runs without issues, ideal senario)/failure response
		
		return course;
	}
	
	/*
	 * TODO Retrieve Course
	 * any type list (including iterable) can be empty (effectively null) but single obj cannot
	 */

	@GetMapping("/getCourse")
	public CourseDTO getCourse( @RequestParam("id") int id ) {
		// get id of course I want to find ^
		// check if there course there
		Course course = courseRepository.findById(id).orElse(null);
		// if course is there, display it
		CourseDTO courseDTO = new CourseDTO();
		
		// replace items before displaying
		// I needed to do this since there was no method
		courseDTO.course_id = course.getCourse_id();
		courseDTO.building = course.getBuilding();
		courseDTO.startDate = course.getStart().toString();
		courseDTO.endDate = course.getEnd().toString();
		courseDTO.room = course.getRoom();
		courseDTO.instructor = course.getInstructor();
		courseDTO.title = course.getTitle();
		courseDTO.section = course.getSection();
		courseDTO.times = course.getTimes();
		
		return courseDTO;
	}
	
	/*
	 * TODO Update Course
	 * @RequestBody ScheduleDTO.CourseDTO courseDTO
	 */
	@PutMapping("/updateCourse")
	public CourseDTO updateCourse( @RequestBody ScheduleDTO.CourseDTO courseDTO ) {
		// get updated values of course I want to find ^
		// check if there course there
		Course course = courseRepository.findById(courseDTO.course_id).orElse(null);
		
		if(course == null) {
			throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "Course not found with id" + courseDTO.id);
		}
		course.setCourse_id(courseDTO.course_id);
		course.setTitle(courseDTO.title);
		course.setTimes(courseDTO.times);
		course.setRoom(courseDTO.room);
		course.setBuilding(courseDTO.building);
		course.setInstructor(courseDTO.instructor);
		course.setSection(courseDTO.section);
		
		Course changedCourse = courseRepository.save(course);
		
		// if course is there, display it
		return courseDTO;
	}
	
	/*
	 * TODO Delete Course
	 */
	@DeleteMapping("/deleteCourse")
	public boolean deleteCourse( @RequestParam("id") int id ) {
		Course course = courseRepository.findById(id).orElse(null);
		if(course.equals(null)) {
			return false;
		}
		courseRepository.delete(course);
		return true;
	}
	
//	@GetMapping("/schedule")
//	public ScheduleDTO getSchedule( @RequestParam("year") int year, @RequestParam("semester") String semester ) {
//		System.out.println("/schedule called.");
//		String student_email = "test@csumb.edu";   // student's email 
//		
//		Student student = studentRepository.findByEmail(student_email);
//		if (student != null) {
//			System.out.println("/schedule student "+student.getName()+" "+student.getStudent_id());
//			List<Enrollment> enrollments = enrollmentRepository.findStudentSchedule(student_email, year, semester);
//			ScheduleDTO sched = createSchedule(year, semester, student, enrollments);
//			return sched;
//		} else {
//			System.out.println("/schedule student not found. "+student_email);
//			throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "Student not found. " );
//		}
//	}
}
