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

import com.crio.LearningNavigator.Dto.SubjectDto;
import com.crio.LearningNavigator.Entities.Subject;
import com.crio.LearningNavigator.Service.SubjectService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/subjects")
public class SubjectController {

    @Autowired
    SubjectService subjectService;

    @GetMapping
    public ResponseEntity<Object> getAllSubject(){
        List<Subject> allSubject = subjectService.getAllSubject();
        if(allSubject.isEmpty())
        return new ResponseEntity<>("No subject found", HttpStatus.NO_CONTENT);

        return ResponseEntity.ok().body(allSubject);
    }

    @GetMapping("{id}")
    public ResponseEntity<Subject> getSubject(@PathVariable("id") long subjectid){
        Subject subject = subjectService.getSubject(subjectid);
        return new ResponseEntity<>(subject,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> createSubject(@Valid @RequestBody SubjectDto subjectDto,BindingResult result){
        if(result.hasErrors()){
            StringBuilder errors=new StringBuilder();
            result.getAllErrors().forEach(error->errors.append(error.getDefaultMessage()).append("\n"));
            return new ResponseEntity<>(errors.toString(), HttpStatus.BAD_REQUEST);
        }
        Subject subject = subjectService.createSubject(subjectDto);
        return new ResponseEntity<>(subject, HttpStatus.CREATED);
    }

    @PostMapping("/{subjectid}/enroll")
     public ResponseEntity<String> enrollStudent(@PathVariable("subjectid") long subjectId,@RequestParam("studentId") long studentId){
        subjectService.enrollStudentInSubject(studentId, subjectId);
        return ResponseEntity.ok("Student enrolled in the Subject successfully");
     }

     @DeleteMapping("/{subjectid}")
     public ResponseEntity<Object> deleteSubject(@PathVariable("subjectid") long subjectId){
        subjectService.deleteSubject(subjectId);
        return new ResponseEntity<>(HttpStatus.OK);
     }
   
}
