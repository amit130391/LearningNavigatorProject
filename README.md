# LearningNavigatorProject
A RESTful API service using Spring Boot to manage the exam enrollment process for a Learning Management System (LMS).

## Postman Collection
You can access the Postman Collection for this project using the link below:
[![Run in Postman](https://run.pstmn.io/button.svg)]((https://elements.getpostman.com/redirect?entityId=30015848-9af05fd3-bc3b-4ce6-9d87-f9008d61d678&entityType=collection))

## Table of Contents
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Installation](#installation)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)
- [Contact](#contact)

## Features
- **Student Management:**
  - Create, retrieve, update, and delete student records.

- **Subject Management:**
  - Manage subjects offered within the learning platform.
  - Enroll students in subjects

- **Exam Management:**
  - Create and manage exams associated with specific subjects.
  - Register students for exams.

## Technologies Used
- Java
- Spring Boot
- Spring Data JPA
- RESTful API
- MySQL

## Installation
### Prerequisites
- Java Development Kit (JDK) 11 or higher
- MySql
- Gradle
- Git

### Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/amit130391/LearningNavigatorProject.git
   cd LearningNavigatorProject
2. Configure MySql:
   Ensure MySql is running on your local machine or update the connection details in application.properties if using a remote MySql instance.
3. Build and run the application:
   ./gradlew clean build
   ./gradlew bootRun

## Usage
1. The application runs on http://localhost:8080.
2. Use the API endpoints to manage student,subject and exam.
3. Use Postman or any other API client to interact with the endpoints.

## API Endpoints
### Student Endpoints
1. GET /students - Retrieve a list of all student.
2. GET /students/{studentId} - Retrieve the details of a specific student.
3. POST /students - Creates a new student.
       * Request Body: { "name": "string" }
4. DELETE /students/{studentId} - Delete a student by ID.
### Subject Endpoints
1. GET /subjects - Retrieve a list of all subject.
2. GET /subjects/{studentId} - Retrieve the details of a specific subject.
3. POST /subjects - Creates a new subject.
       * Request Body: { "subjectName": "string" }
4. POST /subjects/{subjectid}/enroll?studentId={studentId} -Enrolls a student in the subject.
5. DELETE /subjects/{subjectId} - Delete a subject by ID.
### Exam Endpoints
1. GET /exams - Retrieve a list of all exam.
2. GET /exams/{examId} - Retrieve the details of a specific exam.
3. POST /exams - Creates a new exam.
       * Request Body: { "subjectId": long }
4. POST /exams/{examid}/register?studentId={studentId} -Registers a student in the exam.
5. DELETE /exams/{examid} - Delete an exam by ID.
### EasterEgg Endpoint
1. GET /hidden-feature/{number} - Generate a fact about the number which is passed as the path parameter.
   
   
## Contact
If you have any questions or suggestions, feel free to contact me:

    Name: Amit Sharma
    Email: amit130391@gmail.com
    GitHub: amit130391 



