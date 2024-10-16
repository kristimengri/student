package com.kent.gmail.com.runtime.data;

import com.kent.gmail.com.runtime.model.Course;
import com.kent.gmail.com.runtime.model.Course_;
import com.kent.gmail.com.runtime.model.Instructor;
import com.kent.gmail.com.runtime.model.Instructor_;
import com.kent.gmail.com.runtime.request.InstructorFilter;
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
public class InstructorRepository {
  @PersistenceContext private EntityManager em;

  @Autowired private ApplicationEventPublisher applicationEventPublisher;

  @Autowired private BaseRepository baseRepository;

  /**
   * @param instructorFilter Object Used to List Instructor
   * @param securityContext
   * @return List of Instructor
   */
  public List<Instructor> listAllInstructors(
      InstructorFilter instructorFilter, UserSecurityContext securityContext) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Instructor> q = cb.createQuery(Instructor.class);
    Root<Instructor> r = q.from(Instructor.class);
    List<Predicate> preds = new ArrayList<>();
    addInstructorPredicate(instructorFilter, cb, q, r, preds, securityContext);
    q.select(r).where(preds.toArray(new Predicate[0]));
    TypedQuery<Instructor> query = em.createQuery(q);

    if (instructorFilter.getPageSize() != null
        && instructorFilter.getCurrentPage() != null
        && instructorFilter.getPageSize() > 0
        && instructorFilter.getCurrentPage() > -1) {
      query
          .setFirstResult(instructorFilter.getPageSize() * instructorFilter.getCurrentPage())
          .setMaxResults(instructorFilter.getPageSize());
    }

    return query.getResultList();
  }

  public <T extends Instructor> void addInstructorPredicate(
      InstructorFilter instructorFilter,
      CriteriaBuilder cb,
      CommonAbstractCriteria q,
      From<?, T> r,
      List<Predicate> preds,
      UserSecurityContext securityContext) {

    baseRepository.addBasePredicate(instructorFilter, cb, q, r, preds, securityContext);

    if (instructorFilter.getInstructorCourseses() != null
        && !instructorFilter.getInstructorCourseses().isEmpty()) {
      Set<String> ids =
          instructorFilter.getInstructorCourseses().parallelStream()
              .map(f -> f.getId())
              .collect(Collectors.toSet());
      Join<T, Course> join = r.join(Instructor_.instructorCourses);
      preds.add(join.get(Course_.id).in(ids));
    }
  }

  /**
   * @param instructorFilter Object Used to List Instructor
   * @param securityContext
   * @return count of Instructor
   */
  public Long countAllInstructors(
      InstructorFilter instructorFilter, UserSecurityContext securityContext) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Long> q = cb.createQuery(Long.class);
    Root<Instructor> r = q.from(Instructor.class);
    List<Predicate> preds = new ArrayList<>();
    addInstructorPredicate(instructorFilter, cb, q, r, preds, securityContext);
    q.select(cb.count(r)).where(preds.toArray(new Predicate[0]));
    TypedQuery<Long> query = em.createQuery(q);
    return query.getSingleResult();
  }

  public void remove(Object o) {
    em.remove(o);
  }

  public <T extends Instructor, I> List<T> listByIds(
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

  public <T extends Instructor, I> T getByIdOrNull(
      Class<T> c, SingularAttribute<? super T, I> idField, I id) {
    return getByIdOrNull(c, idField, id, null);
  }

  public <T extends Instructor, I> List<T> listByIds(
      Class<T> c, SingularAttribute<? super T, I> idField, Set<I> ids) {
    return listByIds(c, idField, ids, null);
  }

  public <T extends Instructor, I> T getByIdOrNull(
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
