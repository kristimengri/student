package com.kent.gmail.com.runtime.service;

import com.kent.gmail.com.runtime.data.InstructorRepository;
import com.kent.gmail.com.runtime.model.Instructor;
import com.kent.gmail.com.runtime.model.Instructor_;
import com.kent.gmail.com.runtime.request.InstructorCreate;
import com.kent.gmail.com.runtime.request.InstructorFilter;
import com.kent.gmail.com.runtime.request.InstructorUpdate;
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
public class InstructorService {

  @Autowired private InstructorRepository repository;

  @Autowired private BaseService baseService;

  /**
   * @param instructorCreate Object Used to Create Instructor
   * @param securityContext
   * @return created Instructor
   */
  public Instructor createInstructor(
      InstructorCreate instructorCreate, UserSecurityContext securityContext) {
    Instructor instructor = createInstructorNoMerge(instructorCreate, securityContext);
    instructor = this.repository.merge(instructor);
    return instructor;
  }

  /**
   * @param instructorCreate Object Used to Create Instructor
   * @param securityContext
   * @return created Instructor unmerged
   */
  public Instructor createInstructorNoMerge(
      InstructorCreate instructorCreate, UserSecurityContext securityContext) {
    Instructor instructor = new Instructor();
    instructor.setId(UUID.randomUUID().toString());
    updateInstructorNoMerge(instructor, instructorCreate);

    return instructor;
  }

  /**
   * @param instructorCreate Object Used to Create Instructor
   * @param instructor
   * @return if instructor was updated
   */
  public boolean updateInstructorNoMerge(Instructor instructor, InstructorCreate instructorCreate) {
    boolean update = baseService.updateBaseNoMerge(instructor, instructorCreate);

    return update;
  }

  /**
   * @param instructorUpdate
   * @param securityContext
   * @return instructor
   */
  public Instructor updateInstructor(
      InstructorUpdate instructorUpdate, UserSecurityContext securityContext) {
    Instructor instructor = instructorUpdate.getInstructor();
    if (updateInstructorNoMerge(instructor, instructorUpdate)) {
      instructor = this.repository.merge(instructor);
    }
    return instructor;
  }

  /**
   * @param instructorFilter Object Used to List Instructor
   * @param securityContext
   * @return PaginationResponse<Instructor> containing paging information for Instructor
   */
  public PaginationResponse<Instructor> getAllInstructors(
      InstructorFilter instructorFilter, UserSecurityContext securityContext) {
    List<Instructor> list = listAllInstructors(instructorFilter, securityContext);
    long count = this.repository.countAllInstructors(instructorFilter, securityContext);
    return new PaginationResponse<>(list, instructorFilter.getPageSize(), count);
  }

  /**
   * @param instructorFilter Object Used to List Instructor
   * @param securityContext
   * @return List of Instructor
   */
  public List<Instructor> listAllInstructors(
      InstructorFilter instructorFilter, UserSecurityContext securityContext) {
    return this.repository.listAllInstructors(instructorFilter, securityContext);
  }

  public Instructor deleteInstructor(String id, UserSecurityContext securityContext) {
    Instructor instructor =
        this.repository.getByIdOrNull(Instructor.class, Instructor_.id, id, securityContext);
    ;
    if (instructor == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Instructor not found");
    }

    this.repository.remove(instructor);

    return instructor;
  }

  public <T extends Instructor, I> List<T> listByIds(
      Class<T> c,
      SingularAttribute<? super T, I> idField,
      Set<I> ids,
      UserSecurityContext securityContext) {
    return repository.listByIds(c, idField, ids, securityContext);
  }

  public <T extends Instructor, I> T getByIdOrNull(
      Class<T> c,
      SingularAttribute<? super T, I> idField,
      I id,
      UserSecurityContext securityContext) {
    return repository.getByIdOrNull(c, idField, id, securityContext);
  }

  public <T extends Instructor, I> T getByIdOrNull(
      Class<T> c, SingularAttribute<? super T, I> idField, I id) {
    return repository.getByIdOrNull(c, idField, id);
  }

  public <T extends Instructor, I> List<T> listByIds(
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
