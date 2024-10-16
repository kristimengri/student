package com.kent.gmail.com.runtime.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kent.gmail.com.runtime.model.Enrollment;
import com.kent.gmail.com.runtime.validation.IdValid;
import com.kent.gmail.com.runtime.validation.Update;

/** Object Used to Update Enrollment */
@IdValid.List({
  @IdValid(
      field = "id",
      fieldType = Enrollment.class,
      targetField = "enrollment",
      groups = {Update.class})
})
public class EnrollmentUpdate extends EnrollmentCreate {

  @JsonIgnore private Enrollment enrollment;

  private String id;

  /**
   * @return enrollment
   */
  @JsonIgnore
  public Enrollment getEnrollment() {
    return this.enrollment;
  }

  /**
   * @param enrollment enrollment to set
   * @return EnrollmentUpdate
   */
  public <T extends EnrollmentUpdate> T setEnrollment(Enrollment enrollment) {
    this.enrollment = enrollment;
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
   * @return EnrollmentUpdate
   */
  public <T extends EnrollmentUpdate> T setId(String id) {
    this.id = id;
    return (T) this;
  }
}
