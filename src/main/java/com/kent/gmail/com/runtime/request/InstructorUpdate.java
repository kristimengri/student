package com.kent.gmail.com.runtime.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kent.gmail.com.runtime.model.Instructor;
import com.kent.gmail.com.runtime.validation.IdValid;
import com.kent.gmail.com.runtime.validation.Update;

/** Object Used to Update Instructor */
@IdValid.List({
  @IdValid(
      field = "id",
      fieldType = Instructor.class,
      targetField = "instructor",
      groups = {Update.class})
})
public class InstructorUpdate extends InstructorCreate {

  private String id;

  @JsonIgnore private Instructor instructor;

  /**
   * @return id
   */
  public String getId() {
    return this.id;
  }

  /**
   * @param id id to set
   * @return InstructorUpdate
   */
  public <T extends InstructorUpdate> T setId(String id) {
    this.id = id;
    return (T) this;
  }

  /**
   * @return instructor
   */
  @JsonIgnore
  public Instructor getInstructor() {
    return this.instructor;
  }

  /**
   * @param instructor instructor to set
   * @return InstructorUpdate
   */
  public <T extends InstructorUpdate> T setInstructor(Instructor instructor) {
    this.instructor = instructor;
    return (T) this;
  }
}
