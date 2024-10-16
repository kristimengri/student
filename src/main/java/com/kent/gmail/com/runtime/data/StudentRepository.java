package com.kent.gmail.com.runtime.data;

import com.kent.gmail.com.runtime.model.Enrollment;
import com.kent.gmail.com.runtime.model.Enrollment_;
import com.kent.gmail.com.runtime.model.Student;
import com.kent.gmail.com.runtime.model.StudentToCourse;
import com.kent.gmail.com.runtime.model.StudentToCourse_;
import com.kent.gmail.com.runtime.model.Student_;
import com.kent.gmail.com.runtime.request.StudentFilter;
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
public class StudentRepository {
  @PersistenceContext private EntityManager em;

  @Autowired private ApplicationEventPublisher applicationEventPublisher;

  @Autowired private BaseRepository baseRepository;

  /**
   * @param studentFilter Object Used to List Student
   * @param securityContext
   * @return List of Student
   */
  public List<Student> listAllStudents(
      StudentFilter studentFilter, UserSecurityContext securityContext) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Student> q = cb.createQuery(Student.class);
    Root<Student> r = q.from(Student.class);
    List<Predicate> preds = new ArrayList<>();
    addStudentPredicate(studentFilter, cb, q, r, preds, securityContext);
    q.select(r).where(preds.toArray(new Predicate[0]));
    TypedQuery<Student> query = em.createQuery(q);

    if (studentFilter.getPageSize() != null
        && studentFilter.getCurrentPage() != null
        && studentFilter.getPageSize() > 0
        && studentFilter.getCurrentPage() > -1) {
      query
          .setFirstResult(studentFilter.getPageSize() * studentFilter.getCurrentPage())
          .setMaxResults(studentFilter.getPageSize());
    }

    return query.getResultList();
  }

  public <T extends Student> void addStudentPredicate(
      StudentFilter studentFilter,
      CriteriaBuilder cb,
      CommonAbstractCriteria q,
      From<?, T> r,
      List<Predicate> preds,
      UserSecurityContext securityContext) {

    baseRepository.addBasePredicate(studentFilter, cb, q, r, preds, securityContext);

    if (studentFilter.getStudentStudentToCourseses() != null
        && !studentFilter.getStudentStudentToCourseses().isEmpty()) {
      Set<String> ids =
          studentFilter.getStudentStudentToCourseses().parallelStream()
              .map(f -> f.getId())
              .collect(Collectors.toSet());
      Join<T, StudentToCourse> join = r.join(Student_.studentStudentToCourses);
      preds.add(join.get(StudentToCourse_.id).in(ids));
    }

    if (studentFilter.getStudentEnrollmentses() != null
        && !studentFilter.getStudentEnrollmentses().isEmpty()) {
      Set<String> ids =
          studentFilter.getStudentEnrollmentses().parallelStream()
              .map(f -> f.getId())
              .collect(Collectors.toSet());
      Join<T, Enrollment> join = r.join(Student_.studentEnrollments);
      preds.add(join.get(Enrollment_.id).in(ids));
    }
  }

  /**
   * @param studentFilter Object Used to List Student
   * @param securityContext
   * @return count of Student
   */
  public Long countAllStudents(StudentFilter studentFilter, UserSecurityContext securityContext) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Long> q = cb.createQuery(Long.class);
    Root<Student> r = q.from(Student.class);
    List<Predicate> preds = new ArrayList<>();
    addStudentPredicate(studentFilter, cb, q, r, preds, securityContext);
    q.select(cb.count(r)).where(preds.toArray(new Predicate[0]));
    TypedQuery<Long> query = em.createQuery(q);
    return query.getSingleResult();
  }

  public void remove(Object o) {
    em.remove(o);
  }

  public <T extends Student, I> List<T> listByIds(
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

  public <T extends Student, I> T getByIdOrNull(
      Class<T> c, SingularAttribute<? super T, I> idField, I id) {
    return getByIdOrNull(c, idField, id, null);
  }

  public <T extends Student, I> List<T> listByIds(
      Class<T> c, SingularAttribute<? super T, I> idField, Set<I> ids) {
    return listByIds(c, idField, ids, null);
  }

  public <T extends Student, I> T getByIdOrNull(
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
