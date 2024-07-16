package com.crio.LearningNavigator.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crio.LearningNavigator.Dto.ExamDto;
import com.crio.LearningNavigator.Entities.Exam;
import com.crio.LearningNavigator.Service.ExamService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/exams")
public class ExamController {

    @Autowired
    ExamService examService;

    @GetMapping
    public ResponseEntity<List<Exam>> getAllExams(){
        List<Exam> allExams = examService.getAllExams();
        if(allExams.isEmpty())
        return ResponseEntity.noContent().build();

        return ResponseEntity.ok().body(allExams);
    }

    @GetMapping("{id}")
    public ResponseEntity<Exam> getExam(@PathVariable("id") long examId){
       Exam exam = examService.getExam(examId);
       return new ResponseEntity<Exam>(exam, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> createExam(@Valid @RequestBody ExamDto examDto,BindingResult result){
        if(result.hasErrors()){
            StringBuilder errors=new StringBuilder();
            result.getAllErrors().forEach(error->errors.append(error.getDefaultMessage()).append("\n"));
            return new ResponseEntity<>(errors.toString(),HttpStatus.BAD_REQUEST);
        }
        Exam exam = examService.createExam(examDto);
        return new ResponseEntity<>(exam, HttpStatus.CREATED);
    }

    @PostMapping("/{examid}/register")
     public ResponseEntity<String> registerStudent(@PathVariable("examid") long examId,@RequestParam("studentId") long studentId){
        examService.registerStudentInExam(studentId, examId);
        return ResponseEntity.ok("Student registered in the Exam successfully");
     }

     @DeleteMapping("/{examid}")
     public ResponseEntity<Object> deleteExam(@PathVariable("examid") long examId){
        examService.deleteExam(examId);
        return new ResponseEntity<>(HttpStatus.OK);
     }
    
}
