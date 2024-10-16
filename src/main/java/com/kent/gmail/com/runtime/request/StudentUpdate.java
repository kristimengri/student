package com.kent.gmail.com.runtime.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kent.gmail.com.runtime.model.Student;
import com.kent.gmail.com.runtime.validation.IdValid;
import com.kent.gmail.com.runtime.validation.Update;

/** Object Used to Update Student */
@IdValid.List({
  @IdValid(
      field = "id",
      fieldType = Student.class,
      targetField = "student",
      groups = {Update.class})
})
public class StudentUpdate extends StudentCreate {

  private String id;

  @JsonIgnore private Student student;

  /**
   * @return id
   */
  public String getId() {
    return this.id;
  }

  /**
   * @param id id to set
   * @return StudentUpdate
   */
  public <T extends StudentUpdate> T setId(String id) {
    this.id = id;
    return (T) this;
  }

  /**
   * @return student
   */
  @JsonIgnore
  public Student getStudent() {
    return this.student;
  }

  /**
   * @param student student to set
   * @return StudentUpdate
   */
  public <T extends StudentUpdate> T setStudent(Student student) {
    this.student = student;
    return (T) this;
  }
}
