package com.kent.gmail.com.runtime.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kent.gmail.com.runtime.model.Course;
import com.kent.gmail.com.runtime.model.Student;
import com.kent.gmail.com.runtime.validation.Create;
import com.kent.gmail.com.runtime.validation.IdValid;
import com.kent.gmail.com.runtime.validation.Update;

/** Object Used to Create StudentToCourse */
@IdValid.List({
  @IdValid(
      field = "courseId",
      fieldType = Course.class,
      targetField = "course",
      groups = {Update.class, Create.class}),
  @IdValid(
      field = "studentId",
      fieldType = Student.class,
      targetField = "student",
      groups = {Create.class, Update.class})
})
public class StudentToCourseCreate extends BaseCreate {

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
   * @return StudentToCourseCreate
   */
  public <T extends StudentToCourseCreate> T setCourse(Course course) {
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
   * @return StudentToCourseCreate
   */
  public <T extends StudentToCourseCreate> T setCourseId(String courseId) {
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
   * @return StudentToCourseCreate
   */
  public <T extends StudentToCourseCreate> T setStudent(Student student) {
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
   * @return StudentToCourseCreate
   */
  public <T extends StudentToCourseCreate> T setStudentId(String studentId) {
    this.studentId = studentId;
    return (T) this;
  }
}
