package com.crio.LearningNavigator.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crio.LearningNavigator.Entities.Student;

public interface StudentRepository extends JpaRepository<Student,Long>{
}
