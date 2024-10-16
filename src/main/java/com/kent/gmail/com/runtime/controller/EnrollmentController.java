package com.kent.gmail.com.runtime.controller;

import com.kent.gmail.com.runtime.model.Enrollment;
import com.kent.gmail.com.runtime.request.EnrollmentCreate;
import com.kent.gmail.com.runtime.request.EnrollmentFilter;
import com.kent.gmail.com.runtime.request.EnrollmentUpdate;
import com.kent.gmail.com.runtime.response.PaginationResponse;
import com.kent.gmail.com.runtime.security.UserSecurityContext;
import com.kent.gmail.com.runtime.service.EnrollmentService;
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
@RequestMapping("Enrollment")
@Tag(name = "Enrollment")
public class EnrollmentController {

  @Autowired private EnrollmentService enrollmentService;

  @PostMapping("createEnrollment")
  @Operation(summary = "createEnrollment", description = "Creates Enrollment")
  public Enrollment createEnrollment(
      @Validated(Create.class) @RequestBody EnrollmentCreate enrollmentCreate,
      Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return enrollmentService.createEnrollment(enrollmentCreate, securityContext);
  }

  @DeleteMapping("{id}")
  @Operation(summary = "deleteEnrollment", description = "Deletes Enrollment")
  public Enrollment deleteEnrollment(@PathVariable("id") String id, Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return enrollmentService.deleteEnrollment(id, securityContext);
  }

  @PostMapping("getAllEnrollments")
  @Operation(summary = "getAllEnrollments", description = "lists Enrollments")
  public PaginationResponse<Enrollment> getAllEnrollments(
      @Valid @RequestBody EnrollmentFilter enrollmentFilter, Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return enrollmentService.getAllEnrollments(enrollmentFilter, securityContext);
  }

  @PutMapping("updateEnrollment")
  @Operation(summary = "updateEnrollment", description = "Updates Enrollment")
  public Enrollment updateEnrollment(
      @Validated(Update.class) @RequestBody EnrollmentUpdate enrollmentUpdate,
      Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return enrollmentService.updateEnrollment(enrollmentUpdate, securityContext);
  }
}
