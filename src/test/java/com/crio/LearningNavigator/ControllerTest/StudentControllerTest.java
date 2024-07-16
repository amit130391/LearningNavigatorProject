package com.crio.LearningNavigator.ControllerTest;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.crio.LearningNavigator.Controller.StudentController;
import com.crio.LearningNavigator.Dto.StudentDto;
import com.crio.LearningNavigator.Entities.Student;
import com.crio.LearningNavigator.Service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Mock
    private BindingResult result;

    @Test
    public void testGetAllStudent() throws Exception {
        List<Student> studentList = new ArrayList<>();
        Student student = new Student();
        student.setStudentId(1L);
        student.setName("John Doe");
        studentList.add(student);

        when(studentService.getAllStudent()).thenReturn(studentList);

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("John Doe"));

        verify(studentService, times(1)).getAllStudent();
    }

    @Test
    public void testGetAllStudent_NoContent() throws Exception {
        when(studentService.getAllStudent()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/students"))
                .andExpect(status().isNoContent())
                .andExpect(content().string("No student found"));

        verify(studentService, times(1)).getAllStudent();
    }

    @Test
    public void testGetStudent() throws Exception {
        Student student = new Student();
        student.setStudentId(1L);
        student.setName("John Doe");

        when(studentService.getStudent(1L)).thenReturn(student);

        mockMvc.perform(get("/students/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("John Doe"));

        verify(studentService, times(1)).getStudent(1L);
    }

    @Test
    public void testCreateStudent() throws Exception {
        StudentDto studentDto = new StudentDto();
        studentDto.setName("John Doe");

        Student createdStudent = new Student();
        createdStudent.setStudentId(1L);
        createdStudent.setName("John Doe");

        when(studentService.createStudent(any(StudentDto.class))).thenReturn(createdStudent);

        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(studentDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("John Doe"));

        verify(studentService, times(1)).createStudent(any(StudentDto.class));
    }

    @Test
    public void testCreateStudent_BindingErrors() throws Exception {
        // Simulate binding errors
        when(result.hasErrors()).thenReturn(true);
        when(result.getAllErrors()).thenReturn(List.of(new ObjectError("name", "Name is required")));

        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new StudentDto())))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Name is required"));

        verify(studentService, never()).createStudent(any(StudentDto.class));
    }

    @Test
    public void testDeleteStudent() throws Exception {
        mockMvc.perform(delete("/students/1"))
                .andExpect(status().isOk());

        verify(studentService, times(1)).deleteStudent(1L);
    }
}

