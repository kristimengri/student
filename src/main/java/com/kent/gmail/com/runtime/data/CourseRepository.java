package com.kent.gmail.com.runtime.data;

import com.kent.gmail.com.runtime.model.Course;
import com.kent.gmail.com.runtime.model.Course_;
import com.kent.gmail.com.runtime.model.Enrollment;
import com.kent.gmail.com.runtime.model.Enrollment_;
import com.kent.gmail.com.runtime.model.Instructor;
import com.kent.gmail.com.runtime.model.Instructor_;
import com.kent.gmail.com.runtime.model.StudentToCourse;
import com.kent.gmail.com.runtime.model.StudentToCourse_;
import com.kent.gmail.com.runtime.request.CourseFilter;
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
public class CourseRepository {
  @PersistenceContext private EntityManager em;

  @Autowired private ApplicationEventPublisher applicationEventPublisher;

  @Autowired private BaseRepository baseRepository;

  /**
   * @param courseFilter Object Used to List Course
   * @param securityContext
   * @return List of Course
   */
  public List<Course> listAllCourses(
      CourseFilter courseFilter, UserSecurityContext securityContext) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Course> q = cb.createQuery(Course.class);
    Root<Course> r = q.from(Course.class);
    List<Predicate> preds = new ArrayList<>();
    addCoursePredicate(courseFilter, cb, q, r, preds, securityContext);
    q.select(r).where(preds.toArray(new Predicate[0]));
    TypedQuery<Course> query = em.createQuery(q);

    if (courseFilter.getPageSize() != null
        && courseFilter.getCurrentPage() != null
        && courseFilter.getPageSize() > 0
        && courseFilter.getCurrentPage() > -1) {
      query
          .setFirstResult(courseFilter.getPageSize() * courseFilter.getCurrentPage())
          .setMaxResults(courseFilter.getPageSize());
    }

    return query.getResultList();
  }

  public <T extends Course> void addCoursePredicate(
      CourseFilter courseFilter,
      CriteriaBuilder cb,
      CommonAbstractCriteria q,
      From<?, T> r,
      List<Predicate> preds,
      UserSecurityContext securityContext) {

    baseRepository.addBasePredicate(courseFilter, cb, q, r, preds, securityContext);

    if (courseFilter.getInstructors() != null && !courseFilter.getInstructors().isEmpty()) {
      Set<String> ids =
          courseFilter.getInstructors().parallelStream()
              .map(f -> f.getId())
              .collect(Collectors.toSet());
      Join<T, Instructor> join = r.join(Course_.instructor);
      preds.add(join.get(Instructor_.id).in(ids));
    }

    if (courseFilter.getCourseStudentToCourseses() != null
        && !courseFilter.getCourseStudentToCourseses().isEmpty()) {
      Set<String> ids =
          courseFilter.getCourseStudentToCourseses().parallelStream()
              .map(f -> f.getId())
              .collect(Collectors.toSet());
      Join<T, StudentToCourse> join = r.join(Course_.courseStudentToCourses);
      preds.add(join.get(StudentToCourse_.id).in(ids));
    }

    if (courseFilter.getCourseEnrollmentses() != null
        && !courseFilter.getCourseEnrollmentses().isEmpty()) {
      Set<String> ids =
          courseFilter.getCourseEnrollmentses().parallelStream()
              .map(f -> f.getId())
              .collect(Collectors.toSet());
      Join<T, Enrollment> join = r.join(Course_.courseEnrollments);
      preds.add(join.get(Enrollment_.id).in(ids));
    }
  }

  /**
   * @param courseFilter Object Used to List Course
   * @param securityContext
   * @return count of Course
   */
  public Long countAllCourses(CourseFilter courseFilter, UserSecurityContext securityContext) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Long> q = cb.createQuery(Long.class);
    Root<Course> r = q.from(Course.class);
    List<Predicate> preds = new ArrayList<>();
    addCoursePredicate(courseFilter, cb, q, r, preds, securityContext);
    q.select(cb.count(r)).where(preds.toArray(new Predicate[0]));
    TypedQuery<Long> query = em.createQuery(q);
    return query.getSingleResult();
  }

  public void remove(Object o) {
    em.remove(o);
  }

  public <T extends Course, I> List<T> listByIds(
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

  public <T extends Course, I> T getByIdOrNull(
      Class<T> c, SingularAttribute<? super T, I> idField, I id) {
    return getByIdOrNull(c, idField, id, null);
  }

  public <T extends Course, I> List<T> listByIds(
      Class<T> c, SingularAttribute<? super T, I> idField, Set<I> ids) {
    return listByIds(c, idField, ids, null);
  }

  public <T extends Course, I> T getByIdOrNull(
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
