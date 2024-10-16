package com.kent.gmail.com.runtime.controller;

import com.kent.gmail.com.runtime.AppInit;
import com.kent.gmail.com.runtime.model.Course;
import com.kent.gmail.com.runtime.model.Student;
import com.kent.gmail.com.runtime.model.StudentToCourse;
import com.kent.gmail.com.runtime.request.LoginRequest;
import com.kent.gmail.com.runtime.request.StudentToCourseCreate;
import com.kent.gmail.com.runtime.request.StudentToCourseFilter;
import com.kent.gmail.com.runtime.request.StudentToCourseUpdate;
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
public class StudentToCourseControllerTest {

  private StudentToCourse testStudentToCourse;
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
  public void testStudentToCourseCreate() {
    StudentToCourseCreate request = new StudentToCourseCreate();

    request.setStudentId(this.student.getId());

    request.setCourseId(this.course.getId());

    ResponseEntity<StudentToCourse> response =
        this.restTemplate.postForEntity(
            "/StudentToCourse/createStudentToCourse", request, StudentToCourse.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testStudentToCourse = response.getBody();
    assertStudentToCourse(request, testStudentToCourse);
  }

  @Test
  @Order(2)
  public void testListAllStudentToCourses() {
    StudentToCourseFilter request = new StudentToCourseFilter();
    ParameterizedTypeReference<PaginationResponse<StudentToCourse>> t =
        new ParameterizedTypeReference<>() {};

    ResponseEntity<PaginationResponse<StudentToCourse>> response =
        this.restTemplate.exchange(
            "/StudentToCourse/getAllStudentToCourses",
            HttpMethod.POST,
            new HttpEntity<>(request),
            t);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    PaginationResponse<StudentToCourse> body = response.getBody();
    Assertions.assertNotNull(body);
    List<StudentToCourse> StudentToCourses = body.getList();
    Assertions.assertNotEquals(0, StudentToCourses.size());
    Assertions.assertTrue(
        StudentToCourses.stream().anyMatch(f -> f.getId().equals(testStudentToCourse.getId())));
  }

  public void assertStudentToCourse(
      StudentToCourseCreate request, StudentToCourse testStudentToCourse) {
    Assertions.assertNotNull(testStudentToCourse);

    if (request.getStudentId() != null) {

      Assertions.assertNotNull(testStudentToCourse.getStudent());
      Assertions.assertEquals(request.getStudentId(), testStudentToCourse.getStudent().getId());
    }

    if (request.getCourseId() != null) {

      Assertions.assertNotNull(testStudentToCourse.getCourse());
      Assertions.assertEquals(request.getCourseId(), testStudentToCourse.getCourse().getId());
    }
  }

  @Test
  @Order(3)
  public void testStudentToCourseUpdate() {
    StudentToCourseUpdate request = new StudentToCourseUpdate().setId(testStudentToCourse.getId());
    ResponseEntity<StudentToCourse> response =
        this.restTemplate.exchange(
            "/StudentToCourse/updateStudentToCourse",
            HttpMethod.PUT,
            new HttpEntity<>(request),
            StudentToCourse.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testStudentToCourse = response.getBody();
    assertStudentToCourse(request, testStudentToCourse);
  }
}
