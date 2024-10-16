package com.kent.gmail.com.runtime.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity
public class Student extends Base {

  @OneToMany(targetEntity = Enrollment.class, mappedBy = "student")
  @JsonIgnore
  private List<Enrollment> studentEnrollments;

  @OneToMany(targetEntity = StudentToCourse.class, mappedBy = "student")
  @JsonIgnore
  private List<StudentToCourse> studentStudentToCourses;

  /**
   * @return studentEnrollments
   */
  @OneToMany(targetEntity = Enrollment.class, mappedBy = "student")
  @JsonIgnore
  public List<Enrollment> getStudentEnrollments() {
    return this.studentEnrollments;
  }

  /**
   * @param studentEnrollments studentEnrollments to set
   * @return Student
   */
  public <T extends Student> T setStudentEnrollments(List<Enrollment> studentEnrollments) {
    this.studentEnrollments = studentEnrollments;
    return (T) this;
  }

  /**
   * @return studentStudentToCourses
   */
  @OneToMany(targetEntity = StudentToCourse.class, mappedBy = "student")
  @JsonIgnore
  public List<StudentToCourse> getStudentStudentToCourses() {
    return this.studentStudentToCourses;
  }

  /**
   * @param studentStudentToCourses studentStudentToCourses to set
   * @return Student
   */
  public <T extends Student> T setStudentStudentToCourses(
      List<StudentToCourse> studentStudentToCourses) {
    this.studentStudentToCourses = studentStudentToCourses;
    return (T) this;
  }
}
