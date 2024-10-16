package com.kent.gmail.com.runtime.controller;

import com.kent.gmail.com.runtime.AppInit;
import com.kent.gmail.com.runtime.model.Course;
import com.kent.gmail.com.runtime.model.Enrollment;
import com.kent.gmail.com.runtime.model.Student;
import com.kent.gmail.com.runtime.request.EnrollmentCreate;
import com.kent.gmail.com.runtime.request.EnrollmentFilter;
import com.kent.gmail.com.runtime.request.EnrollmentUpdate;
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
public class EnrollmentControllerTest {

  private Enrollment testEnrollment;
  @Autowired private TestRestTemplate restTemplate;

  @Autowired private Student student;

  @Autowired private Course course;

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
  public void testEnrollmentCreate() {
    EnrollmentCreate request = new EnrollmentCreate();

    request.setStudentId(this.student.getId());

    request.setCourseId(this.course.getId());

    ResponseEntity<Enrollment> response =
        this.restTemplate.postForEntity("/Enrollment/createEnrollment", request, Enrollment.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testEnrollment = response.getBody();
    assertEnrollment(request, testEnrollment);
  }

  @Test
  @Order(2)
  public void testListAllEnrollments() {
    EnrollmentFilter request = new EnrollmentFilter();
    ParameterizedTypeReference<PaginationResponse<Enrollment>> t =
        new ParameterizedTypeReference<>() {};

    ResponseEntity<PaginationResponse<Enrollment>> response =
        this.restTemplate.exchange(
            "/Enrollment/getAllEnrollments", HttpMethod.POST, new HttpEntity<>(request), t);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    PaginationResponse<Enrollment> body = response.getBody();
    Assertions.assertNotNull(body);
    List<Enrollment> Enrollments = body.getList();
    Assertions.assertNotEquals(0, Enrollments.size());
    Assertions.assertTrue(
        Enrollments.stream().anyMatch(f -> f.getId().equals(testEnrollment.getId())));
  }

  public void assertEnrollment(EnrollmentCreate request, Enrollment testEnrollment) {
    Assertions.assertNotNull(testEnrollment);

    if (request.getStudentId() != null) {

      Assertions.assertNotNull(testEnrollment.getStudent());
      Assertions.assertEquals(request.getStudentId(), testEnrollment.getStudent().getId());
    }

    if (request.getCourseId() != null) {

      Assertions.assertNotNull(testEnrollment.getCourse());
      Assertions.assertEquals(request.getCourseId(), testEnrollment.getCourse().getId());
    }
  }

  @Test
  @Order(3)
  public void testEnrollmentUpdate() {
    EnrollmentUpdate request = new EnrollmentUpdate().setId(testEnrollment.getId());
    ResponseEntity<Enrollment> response =
        this.restTemplate.exchange(
            "/Enrollment/updateEnrollment",
            HttpMethod.PUT,
            new HttpEntity<>(request),
            Enrollment.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testEnrollment = response.getBody();
    assertEnrollment(request, testEnrollment);
  }
}
