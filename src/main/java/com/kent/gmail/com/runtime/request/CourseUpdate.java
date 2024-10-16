package com.kent.gmail.com.runtime.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kent.gmail.com.runtime.model.Course;
import com.kent.gmail.com.runtime.validation.IdValid;
import com.kent.gmail.com.runtime.validation.Update;

/** Object Used to Update Course */
@IdValid.List({
  @IdValid(
      field = "id",
      fieldType = Course.class,
      targetField = "course",
      groups = {Update.class})
})
public class CourseUpdate extends CourseCreate {

  @JsonIgnore private Course course;

  private String id;

  /**
   * @return course
   */
  @JsonIgnore
  public Course getCourse() {
    return this.course;
  }

  /**
   * @param course course to set
   * @return CourseUpdate
   */
  public <T extends CourseUpdate> T setCourse(Course course) {
    this.course = course;
    return (T) this;
  }

  /**
   * @return id
   */
  public String getId() {
    return this.id;
  }

  /**
   * @param id id to set
   * @return CourseUpdate
   */
  public <T extends CourseUpdate> T setId(String id) {
    this.id = id;
    return (T) this;
  }
}
