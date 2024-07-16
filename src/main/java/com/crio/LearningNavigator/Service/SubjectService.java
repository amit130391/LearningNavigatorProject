package com.crio.LearningNavigator.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crio.LearningNavigator.Dto.SubjectDto;
import com.crio.LearningNavigator.Entities.Exam;
import com.crio.LearningNavigator.Entities.Student;
import com.crio.LearningNavigator.Entities.Subject;
import com.crio.LearningNavigator.Exceptions.ResourceNotFoundException;
import com.crio.LearningNavigator.Exceptions.StudentAlreadyEnrolledException;
import com.crio.LearningNavigator.Repository.ExamRepository;
import com.crio.LearningNavigator.Repository.StudentRepository;
import com.crio.LearningNavigator.Repository.SubjectRepository;

@Service
public class SubjectService {

    @Autowired
    SubjectRepository subjectRepository;
    
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    ExamRepository examRepository;

    public List<Subject> getAllSubject(){
        List<Subject> allSubjects = subjectRepository.findAll();
        return allSubjects;
    }

    public Subject getSubject(long id){
        Subject subject = subjectRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Subject not found with the given id: "+id));
        return subject;
    }

    public Subject  createSubject(SubjectDto subjectDto){
        Subject subject=new Subject();
        subject.setSubjectName(subjectDto.getSubjectName());
        subject.setStudent(new ArrayList<>());
        return subjectRepository.save(subject);
    }

    @Transactional
    public void enrollStudentInSubject(long studentid,long subjectid){
        Subject subject = subjectRepository.findById(subjectid).orElseThrow(()->new ResourceNotFoundException("Subject not found with the given id: "+subjectid));
        Student student = studentRepository.findById(studentid).orElseThrow(()->new ResourceNotFoundException("Student not found with the given id: "+studentid));
        if(!student.getSubjects().contains(subject)){
            student.getSubjects().add(subject);
            subject.getStudent().add(student);
        }
        else{
            throw new StudentAlreadyEnrolledException("The Student is already enrolled in the subject");
        }
        studentRepository.save(student);
    }

    @Transactional
    public void deleteSubject(long subjectid){
        Subject subject = subjectRepository.findById(subjectid).orElseThrow(()->new ResourceNotFoundException("Subject not found with the given id: "+subjectid));
        Exam exam = examRepository.findBySubject_SubjectId(subjectid);
        if (exam != null) {
            examRepository.delete(exam);
        }
        for(Student student:subject.getStudent()){
            student.getSubjects().remove(subject);
        }
        subjectRepository.delete(subject);
    }   
}
