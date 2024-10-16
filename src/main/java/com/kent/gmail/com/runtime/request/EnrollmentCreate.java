package com.kent.gmail.com.runtime.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kent.gmail.com.runtime.model.Student;
import com.kent.gmail.com.runtime.validation.Create;
import com.kent.gmail.com.runtime.validation.IdValid;
import com.kent.gmail.com.runtime.validation.Update;

/** Object Used to Create Enrollment */
@IdValid.List({
  @IdValid(
      field = "studentId",
      fieldType = Student.class,
      targetField = "student",
      groups = {Create.class, Update.class})
})
public class EnrollmentCreate extends BaseCreate {

  @JsonIgnore private Student student;

  private String studentId;

  /**
   * @return student
   */
  @JsonIgnore
  public Student getStudent() {
    return this.student;
  }

  /**
   * @param student student to set
   * @return EnrollmentCreate
   */
  public <T extends EnrollmentCreate> T setStudent(Student student) {
    this.student = student;
    return (T) this;
  }

  /**
   * @return studentId
   */
  public String getStudentId() {
    return this.studentId;
  }

  /**
   * @param studentId studentId to set
   * @return EnrollmentCreate
   */
  public <T extends EnrollmentCreate> T setStudentId(String studentId) {
    this.studentId = studentId;
    return (T) this;
  }
}
