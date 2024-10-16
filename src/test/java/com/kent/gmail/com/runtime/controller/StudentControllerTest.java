package com.kent.gmail.com.runtime.controller;

import com.kent.gmail.com.runtime.AppInit;
import com.kent.gmail.com.runtime.model.Student;
import com.kent.gmail.com.runtime.request.LoginRequest;
import com.kent.gmail.com.runtime.request.StudentCreate;
import com.kent.gmail.com.runtime.request.StudentFilter;
import com.kent.gmail.com.runtime.request.StudentUpdate;
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
public class StudentControllerTest {

  private Student testStudent;
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
  public void testStudentCreate() {
    StudentCreate request = new StudentCreate();

    ResponseEntity<Student> response =
        this.restTemplate.postForEntity("/Student/createStudent", request, Student.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testStudent = response.getBody();
    assertStudent(request, testStudent);
  }

  @Test
  @Order(2)
  public void testListAllStudents() {
    StudentFilter request = new StudentFilter();
    ParameterizedTypeReference<PaginationResponse<Student>> t =
        new ParameterizedTypeReference<>() {};

    ResponseEntity<PaginationResponse<Student>> response =
        this.restTemplate.exchange(
            "/Student/getAllStudents", HttpMethod.POST, new HttpEntity<>(request), t);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    PaginationResponse<Student> body = response.getBody();
    Assertions.assertNotNull(body);
    List<Student> Students = body.getList();
    Assertions.assertNotEquals(0, Students.size());
    Assertions.assertTrue(Students.stream().anyMatch(f -> f.getId().equals(testStudent.getId())));
  }

  public void assertStudent(StudentCreate request, Student testStudent) {
    Assertions.assertNotNull(testStudent);
  }

  @Test
  @Order(3)
  public void testStudentUpdate() {
    StudentUpdate request = new StudentUpdate().setId(testStudent.getId());
    ResponseEntity<Student> response =
        this.restTemplate.exchange(
            "/Student/updateStudent", HttpMethod.PUT, new HttpEntity<>(request), Student.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testStudent = response.getBody();
    assertStudent(request, testStudent);
  }
}
