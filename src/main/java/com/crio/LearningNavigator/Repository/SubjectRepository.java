package com.crio.LearningNavigator.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crio.LearningNavigator.Entities.Subject;

public interface SubjectRepository extends JpaRepository<Subject,Long>{
    
}
