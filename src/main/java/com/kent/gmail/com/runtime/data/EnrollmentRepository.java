package com.kent.gmail.com.runtime.data;

import com.kent.gmail.com.runtime.model.Course;
import com.kent.gmail.com.runtime.model.Course_;
import com.kent.gmail.com.runtime.model.Enrollment;
import com.kent.gmail.com.runtime.model.Enrollment_;
import com.kent.gmail.com.runtime.model.Student;
import com.kent.gmail.com.runtime.model.Student_;
import com.kent.gmail.com.runtime.request.EnrollmentFilter;
import com.kent.gmail.com.runtime.security.UserSecurityContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class EnrollmentRepository {
  @PersistenceContext private EntityManager em;

  @Autowired private ApplicationEventPublisher applicationEventPublisher;

  @Autowired private BaseRepository baseRepository;

  /**
   * @param enrollmentFilter Object Used to List Enrollment
   * @param securityContext
   * @return List of Enrollment
   */
  public List<Enrollment> listAllEnrollments(
      EnrollmentFilter enrollmentFilter, UserSecurityContext securityContext) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Enrollment> q = cb.createQuery(Enrollment.class);
    Root<Enrollment> r = q.from(Enrollment.class);
    List<Predicate> preds = new ArrayList<>();
    addEnrollmentPredicate(enrollmentFilter, cb, q, r, preds, securityContext);
    q.select(r).where(preds.toArray(new Predicate[0]));
    TypedQuery<Enrollment> query = em.createQuery(q);

    if (enrollmentFilter.getPageSize() != null
        && enrollmentFilter.getCurrentPage() != null
        && enrollmentFilter.getPageSize() > 0
        && enrollmentFilter.getCurrentPage() > -1) {
      query
          .setFirstResult(enrollmentFilter.getPageSize() * enrollmentFilter.getCurrentPage())
          .setMaxResults(enrollmentFilter.getPageSize());
    }

    return query.getResultList();
  }

  public <T extends Enrollment> void addEnrollmentPredicate(
      EnrollmentFilter enrollmentFilter,
      CriteriaBuilder cb,
      CommonAbstractCriteria q,
      From<?, T> r,
      List<Predicate> preds,
      UserSecurityContext securityContext) {

    baseRepository.addBasePredicate(enrollmentFilter, cb, q, r, preds, securityContext);

    if (enrollmentFilter.getStudents() != null && !enrollmentFilter.getStudents().isEmpty()) {
      Set<String> ids =
          enrollmentFilter.getStudents().parallelStream()
              .map(f -> f.getId())
              .collect(Collectors.toSet());
      Join<T, Student> join = r.join(Enrollment_.student);
      preds.add(join.get(Student_.id).in(ids));
    }

    if (enrollmentFilter.getCourses() != null && !enrollmentFilter.getCourses().isEmpty()) {
      Set<String> ids =
          enrollmentFilter.getCourses().parallelStream()
              .map(f -> f.getId())
              .collect(Collectors.toSet());
      Join<T, Course> join = r.join(Enrollment_.course);
      preds.add(join.get(Course_.id).in(ids));
    }
  }

  /**
   * @param enrollmentFilter Object Used to List Enrollment
   * @param securityContext
   * @return count of Enrollment
   */
  public Long countAllEnrollments(
      EnrollmentFilter enrollmentFilter, UserSecurityContext securityContext) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Long> q = cb.createQuery(Long.class);
    Root<Enrollment> r = q.from(Enrollment.class);
    List<Predicate> preds = new ArrayList<>();
    addEnrollmentPredicate(enrollmentFilter, cb, q, r, preds, securityContext);
    q.select(cb.count(r)).where(preds.toArray(new Predicate[0]));
    TypedQuery<Long> query = em.createQuery(q);
    return query.getSingleResult();
  }

  public void remove(Object o) {
    em.remove(o);
  }

  public <T extends Enrollment, I> List<T> listByIds(
      Class<T> c,
      SingularAttribute<? super T, I> idField,
      Set<I> ids,
      UserSecurityContext securityContext) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<T> q = cb.createQuery(c);
    Root<T> r = q.from(c);
    List<Predicate> preds = new ArrayList<>();
    preds.add(r.get(idField).in(ids));

    q.select(r).where(preds.toArray(new Predicate[0]));
    return em.createQuery(q).getResultList();
  }

  public <T extends Enrollment, I> T getByIdOrNull(
      Class<T> c, SingularAttribute<? super T, I> idField, I id) {
    return getByIdOrNull(c, idField, id, null);
  }

  public <T extends Enrollment, I> List<T> listByIds(
      Class<T> c, SingularAttribute<? super T, I> idField, Set<I> ids) {
    return listByIds(c, idField, ids, null);
  }

  public <T extends Enrollment, I> T getByIdOrNull(
      Class<T> c,
      SingularAttribute<? super T, I> idField,
      I id,
      UserSecurityContext securityContext) {
    return listByIds(c, idField, Collections.singleton(id), securityContext).stream()
        .findFirst()
        .orElse(null);
  }

  @Transactional
  public <T> T merge(T base) {
    T updated = em.merge(base);
    applicationEventPublisher.publishEvent(updated);
    return updated;
  }

  @Transactional
  public void massMerge(List<?> toMerge) {
    for (Object o : toMerge) {
      java.lang.Object updated = em.merge(o);
      applicationEventPublisher.publishEvent(updated);
    }
  }
}
