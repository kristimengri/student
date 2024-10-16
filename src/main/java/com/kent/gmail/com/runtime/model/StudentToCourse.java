package com.kent.gmail.com.runtime.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class StudentToCourse extends Base {

  @ManyToOne(targetEntity = Student.class)
  private Student student;

  @ManyToOne(targetEntity = Course.class)
  private Course course;

  /**
   * @return student
   */
  @ManyToOne(targetEntity = Student.class)
  public Student getStudent() {
    return this.student;
  }

  /**
   * @param student student to set
   * @return StudentToCourse
   */
  public <T extends StudentToCourse> T setStudent(Student student) {
    this.student = student;
    return (T) this;
  }

  /**
   * @return course
   */
  @ManyToOne(targetEntity = Course.class)
  public Course getCourse() {
    return this.course;
  }

  /**
   * @param course course to set
   * @return StudentToCourse
   */
  public <T extends StudentToCourse> T setCourse(Course course) {
    this.course = course;
    return (T) this;
  }
}
