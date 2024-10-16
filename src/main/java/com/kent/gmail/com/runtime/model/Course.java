package com.kent.gmail.com.runtime.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity
public class Course extends Base {

  @OneToMany(targetEntity = Enrollment.class, mappedBy = "course")
  @JsonIgnore
  private List<Enrollment> courseEnrollments;

  @OneToMany(targetEntity = StudentToCourse.class, mappedBy = "course")
  @JsonIgnore
  private List<StudentToCourse> courseStudentToCourses;

  @ManyToOne(targetEntity = Instructor.class)
  private Instructor instructor;

  /**
   * @return courseEnrollments
   */
  @OneToMany(targetEntity = Enrollment.class, mappedBy = "course")
  @JsonIgnore
  public List<Enrollment> getCourseEnrollments() {
    return this.courseEnrollments;
  }

  /**
   * @param courseEnrollments courseEnrollments to set
   * @return Course
   */
  public <T extends Course> T setCourseEnrollments(List<Enrollment> courseEnrollments) {
    this.courseEnrollments = courseEnrollments;
    return (T) this;
  }

  /**
   * @return courseStudentToCourses
   */
  @OneToMany(targetEntity = StudentToCourse.class, mappedBy = "course")
  @JsonIgnore
  public List<StudentToCourse> getCourseStudentToCourses() {
    return this.courseStudentToCourses;
  }

  /**
   * @param courseStudentToCourses courseStudentToCourses to set
   * @return Course
   */
  public <T extends Course> T setCourseStudentToCourses(
      List<StudentToCourse> courseStudentToCourses) {
    this.courseStudentToCourses = courseStudentToCourses;
    return (T) this;
  }

  /**
   * @return instructor
   */
  @ManyToOne(targetEntity = Instructor.class)
  public Instructor getInstructor() {
    return this.instructor;
  }

  /**
   * @param instructor instructor to set
   * @return Course
   */
  public <T extends Course> T setInstructor(Instructor instructor) {
    this.instructor = instructor;
    return (T) this;
  }
}
