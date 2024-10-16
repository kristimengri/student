package com.kent.gmail.com.runtime.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class Enrollment extends Base {

  @ManyToOne(targetEntity = Student.class)
  private Student student;

  /**
   * @return student
   */
  @ManyToOne(targetEntity = Student.class)
  public Student getStudent() {
    return this.student;
  }

  @ManyToOne(targetEntity = Course.class)
  public Course course;

  @ManyToOne(targetEntity = Course.class)
  public Course getCourse() {
    return this.course;
  }

  public <T extends Enrollment> T setCourse(Course course) {
    this.course = course;
    return (T) this;
  }

  /**
   * @param student student to set
   * @return Enrollment
   */
  public <T extends Enrollment> T setStudent(Student student) {
    this.student = student;
    return (T) this;
  }
}
