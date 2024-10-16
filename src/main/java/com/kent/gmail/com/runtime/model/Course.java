package com.kent.gmail.com.runtime.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity
public class Course extends Base {

  @OneToMany(targetEntity = StudentToCourse.class, mappedBy = "course")
  @JsonIgnore
  private List<StudentToCourse> courseStudentToCourses;

  @ManyToOne(targetEntity = Instructor.class)
  private Instructor instructor;

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
