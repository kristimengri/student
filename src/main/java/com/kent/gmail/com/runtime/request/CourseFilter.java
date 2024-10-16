package com.kent.gmail.com.runtime.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kent.gmail.com.runtime.model.Enrollment;
import com.kent.gmail.com.runtime.model.Instructor;
import com.kent.gmail.com.runtime.model.StudentToCourse;
import com.kent.gmail.com.runtime.validation.IdValid;
import java.util.List;
import java.util.Set;

/** Object Used to List Course */
@IdValid.List({
  @IdValid(
      field = "courseEnrollmentsIds",
      fieldType = Enrollment.class,
      targetField = "courseEnrollmentses"),
  @IdValid(field = "instructorIds", fieldType = Instructor.class, targetField = "instructors"),
  @IdValid(
      field = "courseStudentToCoursesIds",
      fieldType = StudentToCourse.class,
      targetField = "courseStudentToCourseses")
})
public class CourseFilter extends BaseFilter {

  private Set<String> courseEnrollmentsIds;

  @JsonIgnore private List<Enrollment> courseEnrollmentses;

  private Set<String> courseStudentToCoursesIds;

  @JsonIgnore private List<StudentToCourse> courseStudentToCourseses;

  private Set<String> instructorIds;

  @JsonIgnore private List<Instructor> instructors;

  /**
   * @return courseEnrollmentsIds
   */
  public Set<String> getCourseEnrollmentsIds() {
    return this.courseEnrollmentsIds;
  }

  /**
   * @param courseEnrollmentsIds courseEnrollmentsIds to set
   * @return CourseFilter
   */
  public <T extends CourseFilter> T setCourseEnrollmentsIds(Set<String> courseEnrollmentsIds) {
    this.courseEnrollmentsIds = courseEnrollmentsIds;
    return (T) this;
  }

  /**
   * @return courseEnrollmentses
   */
  @JsonIgnore
  public List<Enrollment> getCourseEnrollmentses() {
    return this.courseEnrollmentses;
  }

  /**
   * @param courseEnrollmentses courseEnrollmentses to set
   * @return CourseFilter
   */
  public <T extends CourseFilter> T setCourseEnrollmentses(List<Enrollment> courseEnrollmentses) {
    this.courseEnrollmentses = courseEnrollmentses;
    return (T) this;
  }

  /**
   * @return courseStudentToCoursesIds
   */
  public Set<String> getCourseStudentToCoursesIds() {
    return this.courseStudentToCoursesIds;
  }

  /**
   * @param courseStudentToCoursesIds courseStudentToCoursesIds to set
   * @return CourseFilter
   */
  public <T extends CourseFilter> T setCourseStudentToCoursesIds(
      Set<String> courseStudentToCoursesIds) {
    this.courseStudentToCoursesIds = courseStudentToCoursesIds;
    return (T) this;
  }

  /**
   * @return courseStudentToCourseses
   */
  @JsonIgnore
  public List<StudentToCourse> getCourseStudentToCourseses() {
    return this.courseStudentToCourseses;
  }

  /**
   * @param courseStudentToCourseses courseStudentToCourseses to set
   * @return CourseFilter
   */
  public <T extends CourseFilter> T setCourseStudentToCourseses(
      List<StudentToCourse> courseStudentToCourseses) {
    this.courseStudentToCourseses = courseStudentToCourseses;
    return (T) this;
  }

  /**
   * @return instructorIds
   */
  public Set<String> getInstructorIds() {
    return this.instructorIds;
  }

  /**
   * @param instructorIds instructorIds to set
   * @return CourseFilter
   */
  public <T extends CourseFilter> T setInstructorIds(Set<String> instructorIds) {
    this.instructorIds = instructorIds;
    return (T) this;
  }

  /**
   * @return instructors
   */
  @JsonIgnore
  public List<Instructor> getInstructors() {
    return this.instructors;
  }

  /**
   * @param instructors instructors to set
   * @return CourseFilter
   */
  public <T extends CourseFilter> T setInstructors(List<Instructor> instructors) {
    this.instructors = instructors;
    return (T) this;
  }
}
