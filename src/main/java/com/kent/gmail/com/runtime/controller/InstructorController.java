package com.kent.gmail.com.runtime.controller;

import com.kent.gmail.com.runtime.model.Instructor;
import com.kent.gmail.com.runtime.request.InstructorCreate;
import com.kent.gmail.com.runtime.request.InstructorFilter;
import com.kent.gmail.com.runtime.request.InstructorUpdate;
import com.kent.gmail.com.runtime.response.PaginationResponse;
import com.kent.gmail.com.runtime.security.UserSecurityContext;
import com.kent.gmail.com.runtime.service.InstructorService;
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
@RequestMapping("Instructor")
@Tag(name = "Instructor")
public class InstructorController {

  @Autowired private InstructorService instructorService;

  @PostMapping("createInstructor")
  @Operation(summary = "createInstructor", description = "Creates Instructor")
  public Instructor createInstructor(
      @Validated(Create.class) @RequestBody InstructorCreate instructorCreate,
      Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return instructorService.createInstructor(instructorCreate, securityContext);
  }

  @DeleteMapping("{id}")
  @Operation(summary = "deleteInstructor", description = "Deletes Instructor")
  public Instructor deleteInstructor(@PathVariable("id") String id, Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return instructorService.deleteInstructor(id, securityContext);
  }

  @PostMapping("getAllInstructors")
  @Operation(summary = "getAllInstructors", description = "lists Instructors")
  public PaginationResponse<Instructor> getAllInstructors(
      @Valid @RequestBody InstructorFilter instructorFilter, Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return instructorService.getAllInstructors(instructorFilter, securityContext);
  }

  @PutMapping("updateInstructor")
  @Operation(summary = "updateInstructor", description = "Updates Instructor")
  public Instructor updateInstructor(
      @Validated(Update.class) @RequestBody InstructorUpdate instructorUpdate,
      Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return instructorService.updateInstructor(instructorUpdate, securityContext);
  }
}
