package com.crio.LearningNavigator.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crio.LearningNavigator.Dto.ExamDto;
import com.crio.LearningNavigator.Entities.Exam;
import com.crio.LearningNavigator.Entities.Student;
import com.crio.LearningNavigator.Entities.Subject;
import com.crio.LearningNavigator.Exceptions.ResourceNotFoundException;
import com.crio.LearningNavigator.Exceptions.StudentAlreadyEnrolledException;
import com.crio.LearningNavigator.Exceptions.StudentNotEnrolledInSubjectException;
import com.crio.LearningNavigator.Repository.ExamRepository;
import com.crio.LearningNavigator.Repository.StudentRepository;
import com.crio.LearningNavigator.Repository.SubjectRepository;

@Service
public class ExamService {

    @Autowired
    ExamRepository examRepository;

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    StudentRepository studentRepository;

    public List<Exam> getAllExams(){
        List<Exam> allexams = examRepository.findAll();
        return allexams;
    }

    public Exam getExam(long examid){
        Exam exam = examRepository.findById(examid).orElseThrow(()->new ResourceNotFoundException("Exam not found with the given id: "+examid));
        return exam;
    }


    public Exam createExam(ExamDto examDto){
        Exam exam=new Exam();
        Subject subject=subjectRepository.findById(examDto.getSubjectId()).orElseThrow(()->new ResourceNotFoundException("Subject not found with the given id: "+examDto.getSubjectId()));
        exam.setSubject(subject);
        exam.setStudent(new ArrayList<>());
        return examRepository.save(exam);
    }

    @Transactional
    public void registerStudentInExam(long studentid,long examid){
        Student student = studentRepository.findById(studentid).orElseThrow(()->new ResourceNotFoundException("Student not found with the given id: "+studentid));
        Exam exam = examRepository.findById(examid).orElseThrow(()->new ResourceNotFoundException("Exam not found with the given id: "+examid));
        Subject subject = exam.getSubject();
        if(subject.getStudent().contains(student)){
            if(!student.getExams().contains(exam)){
                student.getExams().add(exam);
                exam.getStudent().add(student);
                studentRepository.save(student);
            }
            else{
                throw new StudentAlreadyEnrolledException("The Student is already enrolled in the exam");
            }
        }
        else{
            throw new StudentNotEnrolledInSubjectException("The Student is not enrolled in the exam subject, please enroll the Student in the Subject first");
        }
    }

    @Transactional
    public void deleteExam(long examid){
        Exam exam = examRepository.findById(examid).orElseThrow(()->new ResourceNotFoundException("Exam not found with the given id: "+examid));
        for(Student student:exam.getStudent()){
            student.getExams().remove(exam);
        }
        examRepository.delete(exam);
    }
    
}
