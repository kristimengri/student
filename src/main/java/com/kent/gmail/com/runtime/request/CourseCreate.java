package com.kent.gmail.com.runtime.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kent.gmail.com.runtime.model.Instructor;
import com.kent.gmail.com.runtime.validation.Create;
import com.kent.gmail.com.runtime.validation.IdValid;
import com.kent.gmail.com.runtime.validation.Update;

/** Object Used to Create Course */
@IdValid.List({
  @IdValid(
      field = "instructorId",
      fieldType = Instructor.class,
      targetField = "instructor",
      groups = {Create.class, Update.class})
})
public class CourseCreate extends BaseCreate {

  @JsonIgnore private Instructor instructor;

  private String instructorId;

  /**
   * @return instructor
   */
  @JsonIgnore
  public Instructor getInstructor() {
    return this.instructor;
  }

  /**
   * @param instructor instructor to set
   * @return CourseCreate
   */
  public <T extends CourseCreate> T setInstructor(Instructor instructor) {
    this.instructor = instructor;
    return (T) this;
  }

  /**
   * @return instructorId
   */
  public String getInstructorId() {
    return this.instructorId;
  }

  /**
   * @param instructorId instructorId to set
   * @return CourseCreate
   */
  public <T extends CourseCreate> T setInstructorId(String instructorId) {
    this.instructorId = instructorId;
    return (T) this;
  }
}
