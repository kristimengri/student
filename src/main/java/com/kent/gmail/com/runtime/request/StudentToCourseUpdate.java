package com.kent.gmail.com.runtime.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kent.gmail.com.runtime.model.StudentToCourse;
import com.kent.gmail.com.runtime.validation.IdValid;
import com.kent.gmail.com.runtime.validation.Update;

/** Object Used to Update StudentToCourse */
@IdValid.List({
  @IdValid(
      field = "id",
      fieldType = StudentToCourse.class,
      targetField = "studentToCourse",
      groups = {Update.class})
})
public class StudentToCourseUpdate extends StudentToCourseCreate {

  private String id;

  @JsonIgnore private StudentToCourse studentToCourse;

  /**
   * @return id
   */
  public String getId() {
    return this.id;
  }

  /**
   * @param id id to set
   * @return StudentToCourseUpdate
   */
  public <T extends StudentToCourseUpdate> T setId(String id) {
    this.id = id;
    return (T) this;
  }

  /**
   * @return studentToCourse
   */
  @JsonIgnore
  public StudentToCourse getStudentToCourse() {
    return this.studentToCourse;
  }

  /**
   * @param studentToCourse studentToCourse to set
   * @return StudentToCourseUpdate
   */
  public <T extends StudentToCourseUpdate> T setStudentToCourse(StudentToCourse studentToCourse) {
    this.studentToCourse = studentToCourse;
    return (T) this;
  }
}
