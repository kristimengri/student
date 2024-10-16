package com.kent.gmail.com.runtime.service;

import com.kent.gmail.com.runtime.data.StudentRepository;
import com.kent.gmail.com.runtime.model.Student;
import com.kent.gmail.com.runtime.model.Student_;
import com.kent.gmail.com.runtime.request.StudentCreate;
import com.kent.gmail.com.runtime.request.StudentFilter;
import com.kent.gmail.com.runtime.request.StudentUpdate;
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
public class StudentService {

  @Autowired private StudentRepository repository;

  @Autowired private BaseService baseService;

  /**
   * @param studentCreate Object Used to Create Student
   * @param securityContext
   * @return created Student
   */
  public Student createStudent(StudentCreate studentCreate, UserSecurityContext securityContext) {
    Student student = createStudentNoMerge(studentCreate, securityContext);
    student = this.repository.merge(student);
    return student;
  }

  /**
   * @param studentCreate Object Used to Create Student
   * @param securityContext
   * @return created Student unmerged
   */
  public Student createStudentNoMerge(
      StudentCreate studentCreate, UserSecurityContext securityContext) {
    Student student = new Student();
    student.setId(UUID.randomUUID().toString());
    updateStudentNoMerge(student, studentCreate);

    return student;
  }

  /**
   * @param studentCreate Object Used to Create Student
   * @param student
   * @return if student was updated
   */
  public boolean updateStudentNoMerge(Student student, StudentCreate studentCreate) {
    boolean update = baseService.updateBaseNoMerge(student, studentCreate);

    return update;
  }

  /**
   * @param studentUpdate
   * @param securityContext
   * @return student
   */
  public Student updateStudent(StudentUpdate studentUpdate, UserSecurityContext securityContext) {
    Student student = studentUpdate.getStudent();
    if (updateStudentNoMerge(student, studentUpdate)) {
      student = this.repository.merge(student);
    }
    return student;
  }

  /**
   * @param studentFilter Object Used to List Student
   * @param securityContext
   * @return PaginationResponse<Student> containing paging information for Student
   */
  public PaginationResponse<Student> getAllStudents(
      StudentFilter studentFilter, UserSecurityContext securityContext) {
    List<Student> list = listAllStudents(studentFilter, securityContext);
    long count = this.repository.countAllStudents(studentFilter, securityContext);
    return new PaginationResponse<>(list, studentFilter.getPageSize(), count);
  }

  /**
   * @param studentFilter Object Used to List Student
   * @param securityContext
   * @return List of Student
   */
  public List<Student> listAllStudents(
      StudentFilter studentFilter, UserSecurityContext securityContext) {
    return this.repository.listAllStudents(studentFilter, securityContext);
  }

  public Student deleteStudent(String id, UserSecurityContext securityContext) {
    Student student =
        this.repository.getByIdOrNull(Student.class, Student_.id, id, securityContext);
    ;
    if (student == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student not found");
    }

    this.repository.remove(student);

    return student;
  }

  public <T extends Student, I> List<T> listByIds(
      Class<T> c,
      SingularAttribute<? super T, I> idField,
      Set<I> ids,
      UserSecurityContext securityContext) {
    return repository.listByIds(c, idField, ids, securityContext);
  }

  public <T extends Student, I> T getByIdOrNull(
      Class<T> c,
      SingularAttribute<? super T, I> idField,
      I id,
      UserSecurityContext securityContext) {
    return repository.getByIdOrNull(c, idField, id, securityContext);
  }

  public <T extends Student, I> T getByIdOrNull(
      Class<T> c, SingularAttribute<? super T, I> idField, I id) {
    return repository.getByIdOrNull(c, idField, id);
  }

  public <T extends Student, I> List<T> listByIds(
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
