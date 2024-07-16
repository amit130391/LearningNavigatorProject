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
import com.crio.LearningNavigator.Service.ExamService;

@ExtendWith(MockitoExtension.class)
public class ExamServiceTest {

    @Mock
    private ExamRepository examRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private ExamService examService;

    private Exam exam;
    private Student student;
    private Subject subject;
    private ExamDto examDto;

    @BeforeEach
    public void setUp() {
        subject = new Subject();
        subject.setSubjectId(1L);
        subject.setSubjectName("Mathematics");
        subject.setStudent(new ArrayList<>());

        student = new Student();
        student.setStudentId(1L);
        student.setName("John Doe");
        student.setSubjects(new ArrayList<>());
        student.setExams(new ArrayList<>());

        exam = new Exam();
        exam.setExamId(1L);
        exam.setSubject(subject);
        exam.setStudent(new ArrayList<>());

        examDto = new ExamDto();
        examDto.setSubjectId(1L);
    }

    @Test
    public void testGetAllExams() {
        List<Exam> exams = new ArrayList<>();
        exams.add(exam);

        when(examRepository.findAll()).thenReturn(exams);

        List<Exam> result = examService.getAllExams();

        assertEquals(1, result.size());
        verify(examRepository, times(1)).findAll();
    }

    @Test
    public void testGetExam() {
        when(examRepository.findById(1L)).thenReturn(Optional.of(exam));

        Exam result = examService.getExam(1L);

        assertEquals(1L, result.getExamId());
        verify(examRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetExamNotFound() {
        when(examRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            examService.getExam(1L);
        });

        verify(examRepository, times(1)).findById(1L);
    }

    @Test
    public void testCreateExam() {
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        when(examRepository.save(any(Exam.class))).thenAnswer(invocation -> {
            Exam savedExam = invocation.getArgument(0);
            savedExam.setExamId(1L); // Simulate the repository setting the ID
            return savedExam;
        });

        Exam result = examService.createExam(examDto);

        assertEquals(1L, result.getSubject().getSubjectId());
        assertNotNull(result.getExamId());
        verify(subjectRepository, times(1)).findById(1L);
        verify(examRepository, times(1)).save(any(Exam.class));
    }

    @Test
    public void testCreateExamSubjectNotFound() {
        when(subjectRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            examService.createExam(examDto);
        });

        verify(subjectRepository, times(1)).findById(1L);
        verify(examRepository, never()).save(any(Exam.class));
    }

    @Test
    public void testRegisterStudentInExam() {
        subject.getStudent().add(student);
        student.getSubjects().add(subject);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(examRepository.findById(1L)).thenReturn(Optional.of(exam));

        examService.registerStudentInExam(1L, 1L);

        assertTrue(student.getExams().contains(exam));
        assertTrue(exam.getStudent().contains(student));
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    public void testRegisterStudentInExamAlreadyEnrolled() {
        subject.getStudent().add(student);
        student.getSubjects().add(subject);
        student.getExams().add(exam);
        exam.getStudent().add(student);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(examRepository.findById(1L)).thenReturn(Optional.of(exam));

        assertThrows(StudentAlreadyEnrolledException.class, () -> {
            examService.registerStudentInExam(1L, 1L);
        });

        verify(studentRepository, never()).save(student);
    }

    @Test
    public void testRegisterStudentInExamNotEnrolledInSubject() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(examRepository.findById(1L)).thenReturn(Optional.of(exam));

        assertThrows(StudentNotEnrolledInSubjectException.class, () -> {
            examService.registerStudentInExam(1L, 1L);
        });

        verify(studentRepository, never()).save(student);
    }

    @Test
    public void testRegisterStudentInExamStudentNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            examService.registerStudentInExam(1L, 1L);
        });

        verify(studentRepository, times(1)).findById(1L);
        verify(examRepository, never()).findById(anyLong());
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    public void testRegisterStudentInExamExamNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(examRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            examService.registerStudentInExam(1L, 1L);
        });

        verify(studentRepository, times(1)).findById(1L);
        verify(examRepository, times(1)).findById(1L);
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    public void testDeleteExam() {
        when(examRepository.findById(1L)).thenReturn(Optional.of(exam));

        examService.deleteExam(1L);

        verify(examRepository, times(1)).findById(1L);
        verify(examRepository, times(1)).delete(exam);
    }

    @Test
    public void testDeleteExamNotFound() {
        when(examRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            examService.deleteExam(1L);
        });

        verify(examRepository, times(1)).findById(1L);
        verify(examRepository, never()).delete(any(Exam.class));
    }
}

