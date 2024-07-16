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

import com.crio.LearningNavigator.Dto.SubjectDto;
import com.crio.LearningNavigator.Entities.Exam;
import com.crio.LearningNavigator.Entities.Student;
import com.crio.LearningNavigator.Entities.Subject;
import com.crio.LearningNavigator.Exceptions.ResourceNotFoundException;
import com.crio.LearningNavigator.Exceptions.StudentAlreadyEnrolledException;
import com.crio.LearningNavigator.Repository.ExamRepository;
import com.crio.LearningNavigator.Repository.StudentRepository;
import com.crio.LearningNavigator.Repository.SubjectRepository;
import com.crio.LearningNavigator.Service.SubjectService;

@ExtendWith(MockitoExtension.class)
public class SubjectServiceTest {

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ExamRepository examRepository;

    @InjectMocks
    private SubjectService subjectService;

    private Subject subject;
    private Student student;
    private SubjectDto subjectDto;

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

        subjectDto = new SubjectDto();
        subjectDto.setSubjectName("Mathematics");
    }

    @Test
    public void testGetAllSubject() {
        List<Subject> subjects = new ArrayList<>();
        subjects.add(subject);

        when(subjectRepository.findAll()).thenReturn(subjects);

        List<Subject> result = subjectService.getAllSubject();

        assertEquals(1, result.size());
        verify(subjectRepository, times(1)).findAll();
    }

    @Test
    public void testGetSubject() {
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));

        Subject result = subjectService.getSubject(1L);

        assertEquals("Mathematics", result.getSubjectName());
        verify(subjectRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetSubjectNotFound() {
        when(subjectRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            subjectService.getSubject(1L);
        });

        verify(subjectRepository, times(1)).findById(1L);
    }

    @Test
    public void testCreateSubject() {
        when(subjectRepository.save(any(Subject.class))).thenReturn(subject);

        Subject result = subjectService.createSubject(subjectDto);

        assertEquals("Mathematics", result.getSubjectName());
        verify(subjectRepository, times(1)).save(any(Subject.class));
    }

    @Test
    public void testEnrollStudentInSubject() {
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        subjectService.enrollStudentInSubject(1L, 1L);

        assertTrue(student.getSubjects().contains(subject));
        assertTrue(subject.getStudent().contains(student));
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    public void testEnrollStudentInSubjectAlreadyEnrolled() {
        subject.getStudent().add(student);
        student.getSubjects().add(subject);

        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        assertThrows(StudentAlreadyEnrolledException.class, () -> {
            subjectService.enrollStudentInSubject(1L, 1L);
        });

        verify(studentRepository, never()).save(student);
    }

    @Test
    public void testDeleteSubject() {
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        when(examRepository.findBySubject_SubjectId(1L)).thenReturn(null);

        subjectService.deleteSubject(1L);

        verify(subjectRepository, times(1)).findById(1L);
        verify(examRepository, times(1)).findBySubject_SubjectId(1L);
        verify(subjectRepository, times(1)).delete(subject);
    }

    @Test
    public void testDeleteSubjectWithExam() {
        Exam exam = new Exam();
        exam.setExamId(1L);
        exam.setSubject(subject);

        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        when(examRepository.findBySubject_SubjectId(1L)).thenReturn(exam);

        subjectService.deleteSubject(1L);

        verify(subjectRepository, times(1)).findById(1L);
        verify(examRepository, times(1)).findBySubject_SubjectId(1L);
        verify(examRepository, times(1)).delete(exam);
        verify(subjectRepository, times(1)).delete(subject);
    }

    @Test
    public void testDeleteSubjectNotFound() {
        when(subjectRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            subjectService.deleteSubject(1L);
        });

        verify(subjectRepository, times(1)).findById(1L);
        verify(examRepository, never()).findBySubject_SubjectId(anyLong());
        verify(subjectRepository, never()).delete(any(Subject.class));
    }
}

