package com.kent.gmail.com.runtime.service;

import com.kent.gmail.com.runtime.data.EnrollmentRepository;
import com.kent.gmail.com.runtime.model.Enrollment;
import com.kent.gmail.com.runtime.model.Enrollment_;
import com.kent.gmail.com.runtime.request.EnrollmentCreate;
import com.kent.gmail.com.runtime.request.EnrollmentFilter;
import com.kent.gmail.com.runtime.request.EnrollmentUpdate;
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
public class EnrollmentService {

  @Autowired private EnrollmentRepository repository;

  @Autowired private BaseService baseService;

  /**
   * @param enrollmentCreate Object Used to Create Enrollment
   * @param securityContext
   * @return created Enrollment
   */
  public Enrollment createEnrollment(
      EnrollmentCreate enrollmentCreate, UserSecurityContext securityContext) {
    Enrollment enrollment = createEnrollmentNoMerge(enrollmentCreate, securityContext);
    enrollment = this.repository.merge(enrollment);
    return enrollment;
  }

  /**
   * @param enrollmentCreate Object Used to Create Enrollment
   * @param securityContext
   * @return created Enrollment unmerged
   */
  public Enrollment createEnrollmentNoMerge(
      EnrollmentCreate enrollmentCreate, UserSecurityContext securityContext) {
    Enrollment enrollment = new Enrollment();
    enrollment.setId(UUID.randomUUID().toString());
    updateEnrollmentNoMerge(enrollment, enrollmentCreate);

    return enrollment;
  }

  /**
   * @param enrollmentCreate Object Used to Create Enrollment
   * @param enrollment
   * @return if enrollment was updated
   */
  public boolean updateEnrollmentNoMerge(Enrollment enrollment, EnrollmentCreate enrollmentCreate) {
    boolean update = baseService.updateBaseNoMerge(enrollment, enrollmentCreate);

    if (enrollmentCreate.getStudent() != null
        && (enrollment.getStudent() == null
            || !enrollmentCreate.getStudent().getId().equals(enrollment.getStudent().getId()))) {
      enrollment.setStudent(enrollmentCreate.getStudent());
      update = true;
    }

    return update;
  }

  /**
   * @param enrollmentUpdate
   * @param securityContext
   * @return enrollment
   */
  public Enrollment updateEnrollment(
      EnrollmentUpdate enrollmentUpdate, UserSecurityContext securityContext) {
    Enrollment enrollment = enrollmentUpdate.getEnrollment();
    if (updateEnrollmentNoMerge(enrollment, enrollmentUpdate)) {
      enrollment = this.repository.merge(enrollment);
    }
    return enrollment;
  }

  /**
   * @param enrollmentFilter Object Used to List Enrollment
   * @param securityContext
   * @return PaginationResponse<Enrollment> containing paging information for Enrollment
   */
  public PaginationResponse<Enrollment> getAllEnrollments(
      EnrollmentFilter enrollmentFilter, UserSecurityContext securityContext) {
    List<Enrollment> list = listAllEnrollments(enrollmentFilter, securityContext);
    long count = this.repository.countAllEnrollments(enrollmentFilter, securityContext);
    return new PaginationResponse<>(list, enrollmentFilter.getPageSize(), count);
  }

  /**
   * @param enrollmentFilter Object Used to List Enrollment
   * @param securityContext
   * @return List of Enrollment
   */
  public List<Enrollment> listAllEnrollments(
      EnrollmentFilter enrollmentFilter, UserSecurityContext securityContext) {
    return this.repository.listAllEnrollments(enrollmentFilter, securityContext);
  }

  public Enrollment deleteEnrollment(String id, UserSecurityContext securityContext) {
    Enrollment enrollment =
        this.repository.getByIdOrNull(Enrollment.class, Enrollment_.id, id, securityContext);
    ;
    if (enrollment == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Enrollment not found");
    }

    this.repository.remove(enrollment);

    return enrollment;
  }

  public <T extends Enrollment, I> List<T> listByIds(
      Class<T> c,
      SingularAttribute<? super T, I> idField,
      Set<I> ids,
      UserSecurityContext securityContext) {
    return repository.listByIds(c, idField, ids, securityContext);
  }

  public <T extends Enrollment, I> T getByIdOrNull(
      Class<T> c,
      SingularAttribute<? super T, I> idField,
      I id,
      UserSecurityContext securityContext) {
    return repository.getByIdOrNull(c, idField, id, securityContext);
  }

  public <T extends Enrollment, I> T getByIdOrNull(
      Class<T> c, SingularAttribute<? super T, I> idField, I id) {
    return repository.getByIdOrNull(c, idField, id);
  }

  public <T extends Enrollment, I> List<T> listByIds(
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
