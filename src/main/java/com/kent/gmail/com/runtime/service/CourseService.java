package com.kent.gmail.com.runtime.service;

import com.kent.gmail.com.runtime.data.CourseRepository;
import com.kent.gmail.com.runtime.model.Course;
import com.kent.gmail.com.runtime.model.Course_;
import com.kent.gmail.com.runtime.request.CourseCreate;
import com.kent.gmail.com.runtime.request.CourseFilter;
import com.kent.gmail.com.runtime.request.CourseUpdate;
import com.kent.gmail.com.runtime.response.PaginationResponse;
import com.kent.gmail.com.runtime.security.UserSecurityContext;
import jakarta.persistence.metamodel.SingularAttribute;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class CourseService {

  @Autowired private CourseRepository repository;

  @Autowired private BaseService baseService;

  /**
   * @param courseCreate Object Used to Create Course
   * @param securityContext
   * @return created Course
   */
  public Course createCourse(CourseCreate courseCreate, UserSecurityContext securityContext) {
    Course course = createCourseNoMerge(courseCreate, securityContext);
    course = this.repository.merge(course);
    return course;
  }

  /**
   * @param courseCreate Object Used to Create Course
   * @param securityContext
   * @return created Course unmerged
   */
  public Course createCourseNoMerge(
      CourseCreate courseCreate, UserSecurityContext securityContext) {
    Course course = new Course();
    course.setId(UUID.randomUUID().toString());
    updateCourseNoMerge(course, courseCreate);

    return course;
  }

  /**
   * @param courseCreate Object Used to Create Course
   * @param course
   * @return if course was updated
   */
  public boolean updateCourseNoMerge(Course course, CourseCreate courseCreate) {
    boolean update = baseService.updateBaseNoMerge(course, courseCreate);

    if (courseCreate.getInstructor() != null
        && (course.getInstructor() == null
            || !courseCreate.getInstructor().getId().equals(course.getInstructor().getId()))) {
      course.setInstructor(courseCreate.getInstructor());
      update = true;
    }

    return update;
  }

  /**
   * @param courseUpdate
   * @param securityContext
   * @return course
   */
  public Course updateCourse(CourseUpdate courseUpdate, UserSecurityContext securityContext) {
    Course course = courseUpdate.getCourse();
    if (updateCourseNoMerge(course, courseUpdate)) {
      course = this.repository.merge(course);
    }
    return course;
  }

  /**
   * @param courseFilter Object Used to List Course
   * @param securityContext
   * @return PaginationResponse<Course> containing paging information for Course
   */
  public PaginationResponse<Course> getAllCourses(
      CourseFilter courseFilter, UserSecurityContext securityContext) {
    List<Course> list = listAllCourses(courseFilter, securityContext);
    long count = this.repository.countAllCourses(courseFilter, securityContext);
    return new PaginationResponse<>(list, courseFilter.getPageSize(), count);
  }

  /**
   * @param courseFilter Object Used to List Course
   * @param securityContext
   * @return List of Course
   */
  public List<Course> listAllCourses(
      CourseFilter courseFilter, UserSecurityContext securityContext) {
    return this.repository.listAllCourses(courseFilter, securityContext);
  }

  public Course deleteCourse(String id, UserSecurityContext securityContext) {
    Course course = this.repository.getByIdOrNull(Course.class, Course_.id, id, securityContext);
    ;
    if (course == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course not found");
    }

    this.repository.remove(course);

    return course;
  }

  public <T extends Course, I> List<T> listByIds(
      Class<T> c,
      SingularAttribute<? super T, I> idField,
      Set<I> ids,
      UserSecurityContext securityContext) {
    return repository.listByIds(c, idField, ids, securityContext);
  }

  public <T extends Course, I> T getByIdOrNull(
      Class<T> c,
      SingularAttribute<? super T, I> idField,
      I id,
      UserSecurityContext securityContext) {
    return repository.getByIdOrNull(c, idField, id, securityContext);
  }

  public <T extends Course, I> T getByIdOrNull(
      Class<T> c, SingularAttribute<? super T, I> idField, I id) {
    return repository.getByIdOrNull(c, idField, id);
  }

  public <T extends Course, I> List<T> listByIds(
      Class<T> c, SingularAttribute<? super T, I> idField, Set<I> ids) {
    return repository.listByIds(c, idField, ids);
  }

  public <T> T merge(T base) {
    return this.repository.merge(base);
  }

  public void massMerge(List<?> toMerge) {
    this.repository.massMerge(toMerge);
  }
}
