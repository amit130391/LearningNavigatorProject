package com.crio.LearningNavigator.ServiceTest;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.crio.LearningNavigator.Dto.StudentDto;
import com.crio.LearningNavigator.Entities.Student;
import com.crio.LearningNavigator.Exceptions.ResourceNotFoundException;
import com.crio.LearningNavigator.Repository.ExamRepository;
import com.crio.LearningNavigator.Repository.StudentRepository;
import com.crio.LearningNavigator.Repository.SubjectRepository;
import com.crio.LearningNavigator.Service.StudentService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private ExamRepository examRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student;
    private StudentDto studentDto;

    @BeforeEach
    public void setUp() {
        student = new Student();
        student.setName("John Doe");

        studentDto = new StudentDto();
        studentDto.setName("John Doe");
    }

    @Test
    public void testGetAllStudent() {
        List<Student> students = new ArrayList<>();
        students.add(student);

        when(studentRepository.findAll()).thenReturn(students);

        List<Student> result = studentService.getAllStudent();

        assertEquals(1, result.size());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    public void testGetStudent() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Student result = studentService.getStudent(1L);

        assertEquals("John Doe", result.getName());
        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetStudentNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            studentService.getStudent(1L);
        });

        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    public void testCreateStudent() {
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        Student result = studentService.createStudent(studentDto);

        assertEquals("John Doe", result.getName());
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    public void testDeleteStudent() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        studentService.deleteStudent(1L);

        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).delete(student);
    }

    @Test
    public void testDeleteStudentNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            studentService.deleteStudent(1L);
        });

        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, never()).delete(any(Student.class));
    }
}

