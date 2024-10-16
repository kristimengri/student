package com.kent.gmail.com.runtime.controller;

import com.kent.gmail.com.runtime.model.StudentToCourse;
import com.kent.gmail.com.runtime.request.StudentToCourseCreate;
import com.kent.gmail.com.runtime.request.StudentToCourseFilter;
import com.kent.gmail.com.runtime.request.StudentToCourseUpdate;
import com.kent.gmail.com.runtime.response.PaginationResponse;
import com.kent.gmail.com.runtime.security.UserSecurityContext;
import com.kent.gmail.com.runtime.service.StudentToCourseService;
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
@RequestMapping("StudentToCourse")
@Tag(name = "StudentToCourse")
public class StudentToCourseController {

  @Autowired private StudentToCourseService studentToCourseService;

  @PostMapping("createStudentToCourse")
  @Operation(summary = "createStudentToCourse", description = "Creates StudentToCourse")
  public StudentToCourse createStudentToCourse(
      @Validated(Create.class) @RequestBody StudentToCourseCreate studentToCourseCreate,
      Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return studentToCourseService.createStudentToCourse(studentToCourseCreate, securityContext);
  }

  @DeleteMapping("{id}")
  @Operation(summary = "deleteStudentToCourse", description = "Deletes StudentToCourse")
  public StudentToCourse deleteStudentToCourse(
      @PathVariable("id") String id, Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return studentToCourseService.deleteStudentToCourse(id, securityContext);
  }

  @PostMapping("getAllStudentToCourses")
  @Operation(summary = "getAllStudentToCourses", description = "lists StudentToCourses")
  public PaginationResponse<StudentToCourse> getAllStudentToCourses(
      @Valid @RequestBody StudentToCourseFilter studentToCourseFilter,
      Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return studentToCourseService.getAllStudentToCourses(studentToCourseFilter, securityContext);
  }

  @PutMapping("updateStudentToCourse")
  @Operation(summary = "updateStudentToCourse", description = "Updates StudentToCourse")
  public StudentToCourse updateStudentToCourse(
      @Validated(Update.class) @RequestBody StudentToCourseUpdate studentToCourseUpdate,
      Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return studentToCourseService.updateStudentToCourse(studentToCourseUpdate, securityContext);
  }
}
