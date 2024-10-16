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

  /**
   * @param student student to set
   * @return Enrollment
   */
  public <T extends Enrollment> T setStudent(Student student) {
    this.student = student;
    return (T) this;
  }
}
