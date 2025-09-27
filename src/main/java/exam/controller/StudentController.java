package exam.controller;


import exam.model.Student;
import exam.repo.StudentRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/students")
public class StudentController {
    private final StudentRepository studentRepo;

    public StudentController(StudentRepository studentRepo) {
        this.studentRepo = studentRepo;
    }

    @PostMapping("/register")
    public Student register(@RequestBody Student student) {
        System.out.println("Request for register hit");
        return studentRepo.save(student);
    }

    @GetMapping
    public List<Student> getAll() {
        return studentRepo.findAll();
    }

    @Autowired
    private StudentRepository studentRepository;

    // Show form
    @GetMapping("/new")
    public String showForm(Model model) {
        Student student = studentRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new RuntimeException("No students found in the database"));
        model.addAttribute("student", student);
        return "student_form";   // maps to student_form.html
    }

    // Handle form submit
    @PostMapping("/save")
    public String saveStudent(@ModelAttribute Student student) {
        Random rand = new Random();

        student.setId(rand.nextInt(100));
        studentRepository.save(student);
        return "redirect:/students/list";
    }

    // List all students
    @GetMapping("/list")
    public String listStudents(Model model) {
        model.addAttribute("students", studentRepository.findAll());
        return "student_list";  // maps to student_list.html
    }
}



