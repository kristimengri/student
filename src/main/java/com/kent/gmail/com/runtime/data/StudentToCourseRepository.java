package com.kent.gmail.com.runtime.data;

import com.kent.gmail.com.runtime.model.Course;
import com.kent.gmail.com.runtime.model.Course_;
import com.kent.gmail.com.runtime.model.Student;
import com.kent.gmail.com.runtime.model.StudentToCourse;
import com.kent.gmail.com.runtime.model.StudentToCourse_;
import com.kent.gmail.com.runtime.model.Student_;
import com.kent.gmail.com.runtime.request.StudentToCourseFilter;
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
public class StudentToCourseRepository {
  @PersistenceContext private EntityManager em;

  @Autowired private ApplicationEventPublisher applicationEventPublisher;

  @Autowired private BaseRepository baseRepository;

  /**
   * @param studentToCourseFilter Object Used to List StudentToCourse
   * @param securityContext
   * @return List of StudentToCourse
   */
  public List<StudentToCourse> listAllStudentToCourses(
      StudentToCourseFilter studentToCourseFilter, UserSecurityContext securityContext) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<StudentToCourse> q = cb.createQuery(StudentToCourse.class);
    Root<StudentToCourse> r = q.from(StudentToCourse.class);
    List<Predicate> preds = new ArrayList<>();
    addStudentToCoursePredicate(studentToCourseFilter, cb, q, r, preds, securityContext);
    q.select(r).where(preds.toArray(new Predicate[0]));
    TypedQuery<StudentToCourse> query = em.createQuery(q);

    if (studentToCourseFilter.getPageSize() != null
        && studentToCourseFilter.getCurrentPage() != null
        && studentToCourseFilter.getPageSize() > 0
        && studentToCourseFilter.getCurrentPage() > -1) {
      query
          .setFirstResult(
              studentToCourseFilter.getPageSize() * studentToCourseFilter.getCurrentPage())
          .setMaxResults(studentToCourseFilter.getPageSize());
    }

    return query.getResultList();
  }

  public <T extends StudentToCourse> void addStudentToCoursePredicate(
      StudentToCourseFilter studentToCourseFilter,
      CriteriaBuilder cb,
      CommonAbstractCriteria q,
      From<?, T> r,
      List<Predicate> preds,
      UserSecurityContext securityContext) {

    baseRepository.addBasePredicate(studentToCourseFilter, cb, q, r, preds, securityContext);

    if (studentToCourseFilter.getStudents() != null
        && !studentToCourseFilter.getStudents().isEmpty()) {
      Set<String> ids =
          studentToCourseFilter.getStudents().parallelStream()
              .map(f -> f.getId())
              .collect(Collectors.toSet());
      Join<T, Student> join = r.join(StudentToCourse_.student);
      preds.add(join.get(Student_.id).in(ids));
    }

    if (studentToCourseFilter.getCourses() != null
        && !studentToCourseFilter.getCourses().isEmpty()) {
      Set<String> ids =
          studentToCourseFilter.getCourses().parallelStream()
              .map(f -> f.getId())
              .collect(Collectors.toSet());
      Join<T, Course> join = r.join(StudentToCourse_.course);
      preds.add(join.get(Course_.id).in(ids));
    }
  }

  /**
   * @param studentToCourseFilter Object Used to List StudentToCourse
   * @param securityContext
   * @return count of StudentToCourse
   */
  public Long countAllStudentToCourses(
      StudentToCourseFilter studentToCourseFilter, UserSecurityContext securityContext) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Long> q = cb.createQuery(Long.class);
    Root<StudentToCourse> r = q.from(StudentToCourse.class);
    List<Predicate> preds = new ArrayList<>();
    addStudentToCoursePredicate(studentToCourseFilter, cb, q, r, preds, securityContext);
    q.select(cb.count(r)).where(preds.toArray(new Predicate[0]));
    TypedQuery<Long> query = em.createQuery(q);
    return query.getSingleResult();
  }

  public void remove(Object o) {
    em.remove(o);
  }

  public <T extends StudentToCourse, I> List<T> listByIds(
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

  public <T extends StudentToCourse, I> T getByIdOrNull(
      Class<T> c, SingularAttribute<? super T, I> idField, I id) {
    return getByIdOrNull(c, idField, id, null);
  }

  public <T extends StudentToCourse, I> List<T> listByIds(
      Class<T> c, SingularAttribute<? super T, I> idField, Set<I> ids) {
    return listByIds(c, idField, ids, null);
  }

  public <T extends StudentToCourse, I> T getByIdOrNull(
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
