package com.crio.LearningNavigator.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crio.LearningNavigator.Dto.StudentDto;
import com.crio.LearningNavigator.Entities.Student;
import com.crio.LearningNavigator.Exceptions.ResourceNotFoundException;
import com.crio.LearningNavigator.Repository.ExamRepository;
import com.crio.LearningNavigator.Repository.StudentRepository;
import com.crio.LearningNavigator.Repository.SubjectRepository;



@Service
public class StudentService {
    
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    ExamRepository examRepository;

    public List<Student> getAllStudent(){
        List<Student> students = studentRepository.findAll();
        return students;
    }

    public Student getStudent(long id){
        return  studentRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Student not found with the given id: "+id));
    }

    public Student createStudent(StudentDto studentDto){
        Student student=new Student();
        student.setName(studentDto.getName());
        student.setSubjects(new ArrayList<>());
        student.setExams(new ArrayList<>());
        return studentRepository.save(student);
    }

    @Transactional
    public void deleteStudent(long id){
        Student student = studentRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Student not found with the given id: "+id));
        studentRepository.delete(student);
    }
}
