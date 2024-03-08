package com.akobir.cache.service;

import com.akobir.cache.dto.StudentRequestDTO;
import com.akobir.cache.entity.Student;

import java.util.List;

public interface StudentService {
    Student createStudent(StudentRequestDTO dto);

    Student getStudent(Long id);

    List<Student> getAllStudents();

    Student updateStudent(Long id, StudentRequestDTO dto);

    void deleteStudent(Long id);
}
