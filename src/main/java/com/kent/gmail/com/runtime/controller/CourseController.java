package com.kent.gmail.com.runtime.controller;

import com.kent.gmail.com.runtime.model.Course;
import com.kent.gmail.com.runtime.request.CourseCreate;
import com.kent.gmail.com.runtime.request.CourseFilter;
import com.kent.gmail.com.runtime.request.CourseUpdate;
import com.kent.gmail.com.runtime.response.PaginationResponse;
import com.kent.gmail.com.runtime.security.UserSecurityContext;
import com.kent.gmail.com.runtime.service.CourseService;
import com.kent.gmail.com.runtime.validation.Create;
import com.kent.gmail.com.runtime.validation.Update;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("Course")
@Tag(name = "Course")
public class CourseController {

  @Autowired private CourseService courseService;

  @PostMapping("createCourse")
  @Operation(summary = "createCourse", description = "Creates Course")
  public Course createCourse(
      @Validated(Create.class) @RequestBody CourseCreate courseCreate,
      Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return courseService.createCourse(courseCreate, securityContext);
  }

  @DeleteMapping("{id}")
  @Operation(summary = "deleteCourse", description = "Deletes Course")
  public Course deleteCourse(@PathVariable("id") String id, Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return courseService.deleteCourse(id, securityContext);
  }

  @PostMapping("getAllCourses")
  @Operation(summary = "getAllCourses", description = "lists Courses")
  public PaginationResponse<Course> getAllCourses(
      @Valid @RequestBody CourseFilter courseFilter, Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return courseService.getAllCourses(courseFilter, securityContext);
  }

  @PutMapping("updateCourse")
  @Operation(summary = "updateCourse", description = "Updates Course")
  public Course updateCourse(
      @Validated(Update.class) @RequestBody CourseUpdate courseUpdate,
      Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return courseService.updateCourse(courseUpdate, securityContext);
  }
}
