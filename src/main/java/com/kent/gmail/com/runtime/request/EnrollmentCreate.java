package com.kent.gmail.com.runtime.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kent.gmail.com.runtime.model.Course;
import com.kent.gmail.com.runtime.model.Student;
import com.kent.gmail.com.runtime.validation.Create;
import com.kent.gmail.com.runtime.validation.IdValid;
import com.kent.gmail.com.runtime.validation.Update;

/** Object Used to Create Enrollment */
@IdValid.List({
  @IdValid(
      field = "studentId",
      fieldType = Student.class,
      targetField = "student",
      groups = {Create.class, Update.class}),
  @IdValid(
      field = "courseId",
      fieldType = Course.class,
      targetField = "course",
      groups = {Create.class, Update.class})
})
public class EnrollmentCreate extends BaseCreate {

  @JsonIgnore private Course course;

  private String courseId;

  @JsonIgnore private Student student;

  private String studentId;

  /**
   * @return course
   */
  @JsonIgnore
  public Course getCourse() {
    return this.course;
  }

  /**
   * @param course course to set
   * @return EnrollmentCreate
   */
  public <T extends EnrollmentCreate> T setCourse(Course course) {
    this.course = course;
    return (T) this;
  }

  /**
   * @return courseId
   */
  public String getCourseId() {
    return this.courseId;
  }

  /**
   * @param courseId courseId to set
   * @return EnrollmentCreate
   */
  public <T extends EnrollmentCreate> T setCourseId(String courseId) {
    this.courseId = courseId;
    return (T) this;
  }

  /**
   * @return student
   */
  @JsonIgnore
  public Student getStudent() {
    return this.student;
  }

  /**
   * @param student student to set
   * @return EnrollmentCreate
   */
  public <T extends EnrollmentCreate> T setStudent(Student student) {
    this.student = student;
    return (T) this;
  }

  /**
   * @return studentId
   */
  public String getStudentId() {
    return this.studentId;
  }

  /**
   * @param studentId studentId to set
   * @return EnrollmentCreate
   */
  public <T extends EnrollmentCreate> T setStudentId(String studentId) {
    this.studentId = studentId;
    return (T) this;
  }
}
