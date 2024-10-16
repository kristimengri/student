package com.kent.gmail.com.runtime;

import com.kent.gmail.com.runtime.model.Course;
import com.kent.gmail.com.runtime.model.Instructor;
import com.kent.gmail.com.runtime.model.Student;
import com.kent.gmail.com.runtime.request.AppUserCreate;
import com.kent.gmail.com.runtime.request.CourseCreate;
import com.kent.gmail.com.runtime.request.InstructorCreate;
import com.kent.gmail.com.runtime.request.StudentCreate;
import com.kent.gmail.com.runtime.security.UserSecurityContext;
import com.kent.gmail.com.runtime.service.AppUserService;
import com.kent.gmail.com.runtime.service.CourseService;
import com.kent.gmail.com.runtime.service.InstructorService;
import com.kent.gmail.com.runtime.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppInitConfig {

  @Autowired private CourseService courseService;

  @Autowired private StudentService studentService;

  @Autowired private InstructorService instructorService;

  @Autowired
  @Qualifier("adminSecurityContext")
  private UserSecurityContext securityContext;

  @Bean
  public Course course() {
    CourseCreate courseCreate = new CourseCreate();
    return courseService.createCourse(courseCreate, securityContext);
  }

  @Bean
  public Student student() {
    StudentCreate studentCreate = new StudentCreate();
    return studentService.createStudent(studentCreate, securityContext);
  }

  @Bean
  public Instructor instructor() {
    InstructorCreate instructorCreate = new InstructorCreate();
    return instructorService.createInstructor(instructorCreate, securityContext);
  }

  @Configuration
  public static class UserConfig {
    @Bean
    @Qualifier("adminSecurityContext")
    public UserSecurityContext adminSecurityContext(AppUserService appUserService) {
      com.kent.gmail.com.runtime.model.AppUser admin =
          appUserService.createAppUser(
              new AppUserCreate().setUsername("admin@flexicore.com").setPassword("admin"), null);
      return new UserSecurityContext(admin);
    }
  }
}
