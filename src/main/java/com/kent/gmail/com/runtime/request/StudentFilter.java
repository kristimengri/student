package com.kent.gmail.com.runtime.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kent.gmail.com.runtime.model.Enrollment;
import com.kent.gmail.com.runtime.model.StudentToCourse;
import com.kent.gmail.com.runtime.validation.IdValid;
import java.util.List;
import java.util.Set;

/** Object Used to List Student */
@IdValid.List({
  @IdValid(
      field = "studentEnrollmentsIds",
      fieldType = Enrollment.class,
      targetField = "studentEnrollmentses"),
  @IdValid(
      field = "studentStudentToCoursesIds",
      fieldType = StudentToCourse.class,
      targetField = "studentStudentToCourseses")
})
public class StudentFilter extends BaseFilter {

  private Set<String> studentEnrollmentsIds;

  @JsonIgnore private List<Enrollment> studentEnrollmentses;

  private Set<String> studentStudentToCoursesIds;

  @JsonIgnore private List<StudentToCourse> studentStudentToCourseses;

  /**
   * @return studentEnrollmentsIds
   */
  public Set<String> getStudentEnrollmentsIds() {
    return this.studentEnrollmentsIds;
  }

  /**
   * @param studentEnrollmentsIds studentEnrollmentsIds to set
   * @return StudentFilter
   */
  public <T extends StudentFilter> T setStudentEnrollmentsIds(Set<String> studentEnrollmentsIds) {
    this.studentEnrollmentsIds = studentEnrollmentsIds;
    return (T) this;
  }

  /**
   * @return studentEnrollmentses
   */
  @JsonIgnore
  public List<Enrollment> getStudentEnrollmentses() {
    return this.studentEnrollmentses;
  }

  /**
   * @param studentEnrollmentses studentEnrollmentses to set
   * @return StudentFilter
   */
  public <T extends StudentFilter> T setStudentEnrollmentses(
      List<Enrollment> studentEnrollmentses) {
    this.studentEnrollmentses = studentEnrollmentses;
    return (T) this;
  }

  /**
   * @return studentStudentToCoursesIds
   */
  public Set<String> getStudentStudentToCoursesIds() {
    return this.studentStudentToCoursesIds;
  }

  /**
   * @param studentStudentToCoursesIds studentStudentToCoursesIds to set
   * @return StudentFilter
   */
  public <T extends StudentFilter> T setStudentStudentToCoursesIds(
      Set<String> studentStudentToCoursesIds) {
    this.studentStudentToCoursesIds = studentStudentToCoursesIds;
    return (T) this;
  }

  /**
   * @return studentStudentToCourseses
   */
  @JsonIgnore
  public List<StudentToCourse> getStudentStudentToCourseses() {
    return this.studentStudentToCourseses;
  }

  /**
   * @param studentStudentToCourseses studentStudentToCourseses to set
   * @return StudentFilter
   */
  public <T extends StudentFilter> T setStudentStudentToCourseses(
      List<StudentToCourse> studentStudentToCourseses) {
    this.studentStudentToCourseses = studentStudentToCourseses;
    return (T) this;
  }
}
