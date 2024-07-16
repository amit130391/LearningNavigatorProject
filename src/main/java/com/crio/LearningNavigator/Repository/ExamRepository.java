package com.crio.LearningNavigator.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crio.LearningNavigator.Entities.Exam;

public interface ExamRepository extends JpaRepository<Exam,Long>{
    Exam findBySubject_SubjectId(Long subjectId);
}
