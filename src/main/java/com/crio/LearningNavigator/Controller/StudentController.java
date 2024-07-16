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
import org.springframework.web.bind.annotation.RestController;

import com.crio.LearningNavigator.Dto.StudentDto;
import com.crio.LearningNavigator.Entities.Student;
import com.crio.LearningNavigator.Service.StudentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    StudentService studentService;

    @GetMapping(value = {"","/"})
    public ResponseEntity<Object> getAllStudent(){
        List<Student> allStudent = studentService.getAllStudent();
        if(allStudent.isEmpty())
        return new ResponseEntity<>("No student found", HttpStatus.NO_CONTENT);

        return ResponseEntity.ok().body(allStudent);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudent(@PathVariable("id") Long id){
        if(id==null){
            List<Student> allStudent = studentService.getAllStudent();
            if(allStudent.isEmpty())
            return new ResponseEntity<>("No student found", HttpStatus.NO_CONTENT);
            return ResponseEntity.ok(allStudent);
        }
        Student student = studentService.getStudent(id);
        return new ResponseEntity<Student>(student, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> createStudent(@Valid @RequestBody StudentDto studentDto,BindingResult result){
        // if(result.hasErrors()){
        //     StringBuilder errors = new StringBuilder();
        //     result.getAllErrors().forEach(error->errors.append(error.getDefaultMessage()).append("\n"));
        //     return new ResponseEntity<>(errors.toString(),HttpStatus.BAD_REQUEST);
        // }
        if(result.hasErrors()){
            // Get the first field error message for 'name' field
            String errorMessage = result.getFieldError("name").getDefaultMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
        Student student = studentService.createStudent(studentDto);
        return new ResponseEntity<>(student,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteStudent(@PathVariable("id") long id){
        studentService.deleteStudent(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    
}
