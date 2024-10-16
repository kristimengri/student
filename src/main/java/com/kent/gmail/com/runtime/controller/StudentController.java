package com.kent.gmail.com.runtime.controller;

import com.kent.gmail.com.runtime.model.Student;
import com.kent.gmail.com.runtime.request.StudentCreate;
import com.kent.gmail.com.runtime.request.StudentFilter;
import com.kent.gmail.com.runtime.request.StudentUpdate;
import com.kent.gmail.com.runtime.response.PaginationResponse;
import com.kent.gmail.com.runtime.security.UserSecurityContext;
import com.kent.gmail.com.runtime.service.StudentService;
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
@RequestMapping("Student")
@Tag(name = "Student")
public class StudentController {

  @Autowired private StudentService studentService;

  @PostMapping("createStudent")
  @Operation(summary = "createStudent", description = "Creates Student")
  public Student createStudent(
      @Validated(Create.class) @RequestBody StudentCreate studentCreate,
      Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return studentService.createStudent(studentCreate, securityContext);
  }

  @DeleteMapping("{id}")
  @Operation(summary = "deleteStudent", description = "Deletes Student")
  public Student deleteStudent(@PathVariable("id") String id, Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return studentService.deleteStudent(id, securityContext);
  }

  @PostMapping("getAllStudents")
  @Operation(summary = "getAllStudents", description = "lists Students")
  public PaginationResponse<Student> getAllStudents(
      @Valid @RequestBody StudentFilter studentFilter, Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return studentService.getAllStudents(studentFilter, securityContext);
  }

  @PutMapping("updateStudent")
  @Operation(summary = "updateStudent", description = "Updates Student")
  public Student updateStudent(
      @Validated(Update.class) @RequestBody StudentUpdate studentUpdate,
      Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return studentService.updateStudent(studentUpdate, securityContext);
  }
}
