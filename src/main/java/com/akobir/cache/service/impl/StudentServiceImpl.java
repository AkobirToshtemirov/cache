package com.akobir.cache.service.impl;

import com.akobir.cache.dto.StudentRequestDTO;
import com.akobir.cache.entity.Student;
import com.akobir.cache.exception.NotFoundException;
import com.akobir.cache.repository.StudentRepository;
import com.akobir.cache.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final ConcurrentHashMap<Long, Student> studensCache = new ConcurrentHashMap<>();

    @Override
    public Student createStudent(StudentRequestDTO dto) {
        Student student = new Student();
        student.setName(dto.name());
        student.setAge(dto.age());

        return studentRepository.save(student);
    }

    @Override
    public Student getStudent(Long id) {
        Student cachedStudent = studensCache.get(id);
        if (cachedStudent != null)
            return cachedStudent;

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Student not found with id: " + id));

        studensCache.put(id, student);
        return student;
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student updateStudent(Long id, StudentRequestDTO dto) {
        Student student = getStudent(id);
        student.setName(dto.name());
        student.setAge(dto.age());

        Student savedStudent = studentRepository.save(student);
        studensCache.put(id, savedStudent);

        return savedStudent;
    }

    @Override
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
        studensCache.remove(id);
    }

}
