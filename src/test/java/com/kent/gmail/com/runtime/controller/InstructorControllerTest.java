package com.kent.gmail.com.runtime.controller;

import com.kent.gmail.com.runtime.AppInit;
import com.kent.gmail.com.runtime.model.Instructor;
import com.kent.gmail.com.runtime.request.InstructorCreate;
import com.kent.gmail.com.runtime.request.InstructorFilter;
import com.kent.gmail.com.runtime.request.InstructorUpdate;
import com.kent.gmail.com.runtime.request.LoginRequest;
import com.kent.gmail.com.runtime.response.PaginationResponse;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.bind.annotation.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = AppInit.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class InstructorControllerTest {

  private Instructor testInstructor;
  @Autowired private TestRestTemplate restTemplate;

  @BeforeAll
  private void init() {
    ResponseEntity<Object> authenticationResponse =
        this.restTemplate.postForEntity(
            "/login",
            new LoginRequest().setUsername("admin@flexicore.com").setPassword("admin"),
            Object.class);
    String authenticationKey =
        authenticationResponse.getHeaders().get(HttpHeaders.AUTHORIZATION).stream()
            .findFirst()
            .orElse(null);
    restTemplate
        .getRestTemplate()
        .setInterceptors(
            Collections.singletonList(
                (request, body, execution) -> {
                  request.getHeaders().add("Authorization", "Bearer " + authenticationKey);
                  return execution.execute(request, body);
                }));
  }

  @Test
  @Order(1)
  public void testInstructorCreate() {
    InstructorCreate request = new InstructorCreate();

    ResponseEntity<Instructor> response =
        this.restTemplate.postForEntity("/Instructor/createInstructor", request, Instructor.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testInstructor = response.getBody();
    assertInstructor(request, testInstructor);
  }

  @Test
  @Order(2)
  public void testListAllInstructors() {
    InstructorFilter request = new InstructorFilter();
    ParameterizedTypeReference<PaginationResponse<Instructor>> t =
        new ParameterizedTypeReference<>() {};

    ResponseEntity<PaginationResponse<Instructor>> response =
        this.restTemplate.exchange(
            "/Instructor/getAllInstructors", HttpMethod.POST, new HttpEntity<>(request), t);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    PaginationResponse<Instructor> body = response.getBody();
    Assertions.assertNotNull(body);
    List<Instructor> Instructors = body.getList();
    Assertions.assertNotEquals(0, Instructors.size());
    Assertions.assertTrue(
        Instructors.stream().anyMatch(f -> f.getId().equals(testInstructor.getId())));
  }

  public void assertInstructor(InstructorCreate request, Instructor testInstructor) {
    Assertions.assertNotNull(testInstructor);
  }

  @Test
  @Order(3)
  public void testInstructorUpdate() {
    InstructorUpdate request = new InstructorUpdate().setId(testInstructor.getId());
    ResponseEntity<Instructor> response =
        this.restTemplate.exchange(
            "/Instructor/updateInstructor",
            HttpMethod.PUT,
            new HttpEntity<>(request),
            Instructor.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testInstructor = response.getBody();
    assertInstructor(request, testInstructor);
  }
}
