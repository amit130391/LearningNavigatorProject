package com.crio.LearningNavigator.Exceptions;

public class StudentNotEnrolledInSubjectException extends RuntimeException{
    public StudentNotEnrolledInSubjectException(String message){
        super(message);
    }
}
