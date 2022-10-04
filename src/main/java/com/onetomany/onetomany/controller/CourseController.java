package com.onetomany.onetomany.controller;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onetomany.onetomany.model.Course;
import com.onetomany.onetomany.repository.CourseRepository;
import com.onetomany.onetomany.repository.InstructorRepository;

@RestController

public class CourseController {

@Autowired
private CourseRepository courseRepository;

@Autowired
private InstructorRepository instructorRepository;

@GetMapping("/instructors/{instructorId}/courses")
public List < Course > getCoursesByInstructor(@PathVariable(value = "postId") Long instructorId) {
    return courseRepository.findByInstructorId(instructorId);
}

@PostMapping("/instructors/{instructorId}/courses")
public Course createCourse(@PathVariable(value = "instructorId") Integer instructorId,
    @Valid @RequestBody Course course) throws ResourceNotFoundException {
    return instructorRepository.findById(instructorId).map(instructor -> {
        course.setInstructor(instructor);
        return courseRepository.save(course);
    }).orElseThrow(() -> new ResourceNotFoundException("instructor not found"));
}

@PutMapping("/instructors/{instructorId}/courses/{courseId}")
public Course updateCourse(@PathVariable(value = "instructorId") Integer instructorId,
    @PathVariable(value = "courseId") Long courseId, @Valid @RequestBody Course courseRequest)
throws ResourceNotFoundException {
    if (!instructorRepository.existsById(instructorId)) {
        throw new ResourceNotFoundException("instructorId not found");
    }

    return courseRepository.findById(courseId).map(course -> {
        course.setTitle(courseRequest.getTitle());
        return courseRepository.save(course);
    }).orElseThrow(() -> new ResourceNotFoundException("course id not found"));
}

@DeleteMapping("/instructors/{instructorId}/courses/{courseId}")
public ResponseEntity < ? > deleteCourse(@PathVariable(value = "instructorId") Long instructorId,
    @PathVariable(value = "courseId") Long courseId) throws ResourceNotFoundException {
    return courseRepository.findByIdAndInstructorId(courseId, instructorId).map(course -> {
        courseRepository.delete(course);
        return ResponseEntity.ok().build();
    }).orElseThrow(() -> new ResourceNotFoundException(
        "Course not found with id " + courseId + " and instructorId " + instructorId));
}
}