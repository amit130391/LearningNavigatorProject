package com.crio.LearningNavigator.Exceptions;

public class StudentAlreadyEnrolledException extends RuntimeException{
    public StudentAlreadyEnrolledException(String message){
        super(message);
    }
}
