package com.kent.gmail.com.runtime.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kent.gmail.com.runtime.model.Student;
import com.kent.gmail.com.runtime.validation.IdValid;
import java.util.List;
import java.util.Set;

/** Object Used to List Enrollment */
@IdValid.List({@IdValid(field = "studentIds", fieldType = Student.class, targetField = "students")})
public class EnrollmentFilter extends BaseFilter {

  private Set<String> studentIds;

  @JsonIgnore private List<Student> students;

  /**
   * @return studentIds
   */
  public Set<String> getStudentIds() {
    return this.studentIds;
  }

  /**
   * @param studentIds studentIds to set
   * @return EnrollmentFilter
   */
  public <T extends EnrollmentFilter> T setStudentIds(Set<String> studentIds) {
    this.studentIds = studentIds;
    return (T) this;
  }

  /**
   * @return students
   */
  @JsonIgnore
  public List<Student> getStudents() {
    return this.students;
  }

  /**
   * @param students students to set
   * @return EnrollmentFilter
   */
  public <T extends EnrollmentFilter> T setStudents(List<Student> students) {
    this.students = students;
    return (T) this;
  }
}
