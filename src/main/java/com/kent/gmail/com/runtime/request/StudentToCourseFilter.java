package com.kent.gmail.com.runtime.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kent.gmail.com.runtime.model.Course;
import com.kent.gmail.com.runtime.model.Student;
import com.kent.gmail.com.runtime.validation.IdValid;
import java.util.List;
import java.util.Set;

/** Object Used to List StudentToCourse */
@IdValid.List({
  @IdValid(field = "studentIds", fieldType = Student.class, targetField = "students"),
  @IdValid(field = "courseIds", fieldType = Course.class, targetField = "courses")
})
public class StudentToCourseFilter extends BaseFilter {

  private Set<String> courseIds;

  @JsonIgnore private List<Course> courses;

  private Set<String> studentIds;

  @JsonIgnore private List<Student> students;

  /**
   * @return courseIds
   */
  public Set<String> getCourseIds() {
    return this.courseIds;
  }

  /**
   * @param courseIds courseIds to set
   * @return StudentToCourseFilter
   */
  public <T extends StudentToCourseFilter> T setCourseIds(Set<String> courseIds) {
    this.courseIds = courseIds;
    return (T) this;
  }

  /**
   * @return courses
   */
  @JsonIgnore
  public List<Course> getCourses() {
    return this.courses;
  }

  /**
   * @param courses courses to set
   * @return StudentToCourseFilter
   */
  public <T extends StudentToCourseFilter> T setCourses(List<Course> courses) {
    this.courses = courses;
    return (T) this;
  }

  /**
   * @return studentIds
   */
  public Set<String> getStudentIds() {
    return this.studentIds;
  }

  /**
   * @param studentIds studentIds to set
   * @return StudentToCourseFilter
   */
  public <T extends StudentToCourseFilter> T setStudentIds(Set<String> studentIds) {
    this.studentIds = studentIds;
    return (T) this;
  }

  /**
   * @return students
   */
  @JsonIgnore
  public List<Student> getStudents() {
    return this.students;
  }

  /**
   * @param students students to set
   * @return StudentToCourseFilter
   */
  public <T extends StudentToCourseFilter> T setStudents(List<Student> students) {
    this.students = students;
    return (T) this;
  }
}
