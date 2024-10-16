package com.kent.gmail.com.runtime.controller;

import com.kent.gmail.com.runtime.AppInit;
import com.kent.gmail.com.runtime.model.Course;
import com.kent.gmail.com.runtime.model.Instructor;
import com.kent.gmail.com.runtime.request.CourseCreate;
import com.kent.gmail.com.runtime.request.CourseFilter;
import com.kent.gmail.com.runtime.request.CourseUpdate;
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
public class CourseControllerTest {

  private Course testCourse;
  @Autowired private TestRestTemplate restTemplate;

  @Autowired private Instructor instructor;

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
  public void testCourseCreate() {
    CourseCreate request = new CourseCreate();

    request.setInstructorId(this.instructor.getId());

    ResponseEntity<Course> response =
        this.restTemplate.postForEntity("/Course/createCourse", request, Course.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testCourse = response.getBody();
    assertCourse(request, testCourse);
  }

  @Test
  @Order(2)
  public void testListAllCourses() {
    CourseFilter request = new CourseFilter();
    ParameterizedTypeReference<PaginationResponse<Course>> t =
        new ParameterizedTypeReference<>() {};

    ResponseEntity<PaginationResponse<Course>> response =
        this.restTemplate.exchange(
            "/Course/getAllCourses", HttpMethod.POST, new HttpEntity<>(request), t);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    PaginationResponse<Course> body = response.getBody();
    Assertions.assertNotNull(body);
    List<Course> Courses = body.getList();
    Assertions.assertNotEquals(0, Courses.size());
    Assertions.assertTrue(Courses.stream().anyMatch(f -> f.getId().equals(testCourse.getId())));
  }

  public void assertCourse(CourseCreate request, Course testCourse) {
    Assertions.assertNotNull(testCourse);

    if (request.getInstructorId() != null) {

      Assertions.assertNotNull(testCourse.getInstructor());
      Assertions.assertEquals(request.getInstructorId(), testCourse.getInstructor().getId());
    }
  }

  @Test
  @Order(3)
  public void testCourseUpdate() {
    CourseUpdate request = new CourseUpdate().setId(testCourse.getId());
    ResponseEntity<Course> response =
        this.restTemplate.exchange(
            "/Course/updateCourse", HttpMethod.PUT, new HttpEntity<>(request), Course.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testCourse = response.getBody();
    assertCourse(request, testCourse);
  }
}
