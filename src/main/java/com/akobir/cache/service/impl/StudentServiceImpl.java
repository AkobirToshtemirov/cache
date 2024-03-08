package com.akobir.cache.service.impl;

import com.akobir.cache.dto.StudentRequestDTO;
import com.akobir.cache.entity.Student;
import com.akobir.cache.exception.NotFoundException;
import com.akobir.cache.repository.StudentRepository;
import com.akobir.cache.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Override
    public Student createStudent(StudentRequestDTO dto) {
        Student student = new Student();
        student.setName(dto.name());
        student.setAge(dto.age());

        return studentRepository.save(student);
    }

    @Override
    @Cacheable(key = "#id", value = "students")
    public Student getStudent(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Student not found with id: " + id));
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    @CachePut(key = "#id", value = "students")
    public Student updateStudent(Long id, StudentRequestDTO dto) {
        Student student = getStudent(id);
        student.setName(dto.name());
        student.setAge(dto.age());

        return studentRepository.save(student);
    }

    @Override
    @CacheEvict(key = "#id", value = "students")
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

}
