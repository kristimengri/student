package com.kent.gmail.com.runtime.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity
public class Instructor extends Base {

  @OneToMany(targetEntity = Course.class, mappedBy = "instructor")
  @JsonIgnore
  private List<Course> instructorCourses;

  /**
   * @return instructorCourses
   */
  @OneToMany(targetEntity = Course.class, mappedBy = "instructor")
  @JsonIgnore
  public List<Course> getInstructorCourses() {
    return this.instructorCourses;
  }

  /**
   * @param instructorCourses instructorCourses to set
   * @return Instructor
   */
  public <T extends Instructor> T setInstructorCourses(List<Course> instructorCourses) {
    this.instructorCourses = instructorCourses;
    return (T) this;
  }
}
