package com.kent.gmail.com.runtime.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kent.gmail.com.runtime.model.Course;
import com.kent.gmail.com.runtime.validation.IdValid;
import java.util.List;
import java.util.Set;

/** Object Used to List Instructor */
@IdValid.List({
  @IdValid(
      field = "instructorCoursesIds",
      fieldType = Course.class,
      targetField = "instructorCourseses")
})
public class InstructorFilter extends BaseFilter {

  private Set<String> instructorCoursesIds;

  @JsonIgnore private List<Course> instructorCourseses;

  /**
   * @return instructorCoursesIds
   */
  public Set<String> getInstructorCoursesIds() {
    return this.instructorCoursesIds;
  }

  /**
   * @param instructorCoursesIds instructorCoursesIds to set
   * @return InstructorFilter
   */
  public <T extends InstructorFilter> T setInstructorCoursesIds(Set<String> instructorCoursesIds) {
    this.instructorCoursesIds = instructorCoursesIds;
    return (T) this;
  }

  /**
   * @return instructorCourseses
   */
  @JsonIgnore
  public List<Course> getInstructorCourseses() {
    return this.instructorCourseses;
  }

  /**
   * @param instructorCourseses instructorCourseses to set
   * @return InstructorFilter
   */
  public <T extends InstructorFilter> T setInstructorCourseses(List<Course> instructorCourseses) {
    this.instructorCourseses = instructorCourseses;
    return (T) this;
  }
}
