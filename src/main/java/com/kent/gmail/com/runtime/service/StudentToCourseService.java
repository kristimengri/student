package com.kent.gmail.com.runtime.service;

import com.kent.gmail.com.runtime.data.StudentToCourseRepository;
import com.kent.gmail.com.runtime.model.StudentToCourse;
import com.kent.gmail.com.runtime.model.StudentToCourse_;
import com.kent.gmail.com.runtime.request.StudentToCourseCreate;
import com.kent.gmail.com.runtime.request.StudentToCourseFilter;
import com.kent.gmail.com.runtime.request.StudentToCourseUpdate;
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
public class StudentToCourseService {

  @Autowired private StudentToCourseRepository repository;

  @Autowired private BaseService baseService;

  /**
   * @param studentToCourseCreate Object Used to Create StudentToCourse
   * @param securityContext
   * @return created StudentToCourse
   */
  public StudentToCourse createStudentToCourse(
      StudentToCourseCreate studentToCourseCreate, UserSecurityContext securityContext) {
    StudentToCourse studentToCourse =
        createStudentToCourseNoMerge(studentToCourseCreate, securityContext);
    studentToCourse = this.repository.merge(studentToCourse);
    return studentToCourse;
  }

  /**
   * @param studentToCourseCreate Object Used to Create StudentToCourse
   * @param securityContext
   * @return created StudentToCourse unmerged
   */
  public StudentToCourse createStudentToCourseNoMerge(
      StudentToCourseCreate studentToCourseCreate, UserSecurityContext securityContext) {
    StudentToCourse studentToCourse = new StudentToCourse();
    studentToCourse.setId(UUID.randomUUID().toString());
    updateStudentToCourseNoMerge(studentToCourse, studentToCourseCreate);

    return studentToCourse;
  }

  /**
   * @param studentToCourseCreate Object Used to Create StudentToCourse
   * @param studentToCourse
   * @return if studentToCourse was updated
   */
  public boolean updateStudentToCourseNoMerge(
      StudentToCourse studentToCourse, StudentToCourseCreate studentToCourseCreate) {
    boolean update = baseService.updateBaseNoMerge(studentToCourse, studentToCourseCreate);

    if (studentToCourseCreate.getStudent() != null
        && (studentToCourse.getStudent() == null
            || !studentToCourseCreate
                .getStudent()
                .getId()
                .equals(studentToCourse.getStudent().getId()))) {
      studentToCourse.setStudent(studentToCourseCreate.getStudent());
      update = true;
    }

    if (studentToCourseCreate.getCourse() != null
        && (studentToCourse.getCourse() == null
            || !studentToCourseCreate
                .getCourse()
                .getId()
                .equals(studentToCourse.getCourse().getId()))) {
      studentToCourse.setCourse(studentToCourseCreate.getCourse());
      update = true;
    }

    return update;
  }

  /**
   * @param studentToCourseUpdate
   * @param securityContext
   * @return studentToCourse
   */
  public StudentToCourse updateStudentToCourse(
      StudentToCourseUpdate studentToCourseUpdate, UserSecurityContext securityContext) {
    StudentToCourse studentToCourse = studentToCourseUpdate.getStudentToCourse();
    if (updateStudentToCourseNoMerge(studentToCourse, studentToCourseUpdate)) {
      studentToCourse = this.repository.merge(studentToCourse);
    }
    return studentToCourse;
  }

  /**
   * @param studentToCourseFilter Object Used to List StudentToCourse
   * @param securityContext
   * @return PaginationResponse<StudentToCourse> containing paging information for StudentToCourse
   */
  public PaginationResponse<StudentToCourse> getAllStudentToCourses(
      StudentToCourseFilter studentToCourseFilter, UserSecurityContext securityContext) {
    List<StudentToCourse> list = listAllStudentToCourses(studentToCourseFilter, securityContext);
    long count = this.repository.countAllStudentToCourses(studentToCourseFilter, securityContext);
    return new PaginationResponse<>(list, studentToCourseFilter.getPageSize(), count);
  }

  /**
   * @param studentToCourseFilter Object Used to List StudentToCourse
   * @param securityContext
   * @return List of StudentToCourse
   */
  public List<StudentToCourse> listAllStudentToCourses(
      StudentToCourseFilter studentToCourseFilter, UserSecurityContext securityContext) {
    return this.repository.listAllStudentToCourses(studentToCourseFilter, securityContext);
  }

  public StudentToCourse deleteStudentToCourse(String id, UserSecurityContext securityContext) {
    StudentToCourse studentToCourse =
        this.repository.getByIdOrNull(
            StudentToCourse.class, StudentToCourse_.id, id, securityContext);
    ;
    if (studentToCourse == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "StudentToCourse not found");
    }

    this.repository.remove(studentToCourse);

    return studentToCourse;
  }

  public <T extends StudentToCourse, I> List<T> listByIds(
      Class<T> c,
      SingularAttribute<? super T, I> idField,
      Set<I> ids,
      UserSecurityContext securityContext) {
    return repository.listByIds(c, idField, ids, securityContext);
  }

  public <T extends StudentToCourse, I> T getByIdOrNull(
      Class<T> c,
      SingularAttribute<? super T, I> idField,
      I id,
      UserSecurityContext securityContext) {
    return repository.getByIdOrNull(c, idField, id, securityContext);
  }

  public <T extends StudentToCourse, I> T getByIdOrNull(
      Class<T> c, SingularAttribute<? super T, I> idField, I id) {
    return repository.getByIdOrNull(c, idField, id);
  }

  public <T extends StudentToCourse, I> List<T> listByIds(
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
