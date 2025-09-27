package exam.controller;

import exam.model.*;
import exam.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/tests")
public class TestController {

    private final TestRepository testRepo;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private ResultRepository resultRepository;

    // Show subjects for student to choose
    @GetMapping("/start/{studentId}")
    public String selectSubject(Model model, @PathVariable int studentId) {
        System.out.println("Into start function");

        Student student = studentRepository.findById(studentId).orElseThrow();
        List<Subject> subjects = subjectRepository.findAll();
        model.addAttribute("student", student);
        model.addAttribute("subjects", subjects);
        return "select_subject"; // maps to select_subject.html
    }

    // Generate test for chosen subject
    @GetMapping("/take/{studentId}/{subjectId}")
    public String takeTest(@PathVariable int studentId,
                           @PathVariable int subjectId,
                           Model model) {

        Student student = studentRepository.findById(studentId).orElseThrow();
        Subject subject = subjectRepository.findById(subjectId).orElseThrow();

        List<Question> questions = questionRepository.findBySubjectId(subjectId);

        model.addAttribute("student", student);
        model.addAttribute("subject", subject);
        model.addAttribute("questions", questions);
        return "test_form"; // maps to test_form.html
    }

    // Submit test and evaluate
    @PostMapping("/submit/{studentId}/{subjectId}")
    public String submitTest(@PathVariable int studentId,
                             @PathVariable int subjectId,
                             @RequestParam List<Integer> questionIds,
                             @RequestParam Map<String, String> answers,
                             Model model) {

        Student student = studentRepository.findById(studentId).orElseThrow();
        Subject subject = subjectRepository.findById(subjectId).orElseThrow();

        List<String> answerList = new ArrayList<>();
        for (int i = 0; i < questionIds.size(); i++) {
            answerList.add(answers.get("answers[" + i + "]"));
        }
        int score = 0;
        for (int i = 0; i < questionIds.size(); i++) {
            Question q = questionRepository.findById(questionIds.get(i)).orElseThrow();
            if (q.getCorrectOption().equalsIgnoreCase(answerList.get(i))) {
                score += q.getMarks();
            }
        }

        // Save test result
        Test test = new Test();
        test.setStudent(student);
        test.setSubject(subject);
        test.setScore(score);
        test.setTestDate(LocalDateTime.now());
        testRepository.save(test);

        model.addAttribute("student", student);
        model.addAttribute("subject", subject);
        model.addAttribute("score", score);

// 1️⃣ Fetch all attempted questions
        List<Question> attemptedQuestions = questionRepository.findAllById(questionIds);

        // 2️⃣ Calculate score
        int totalScore = 0;
        for (int i = 0; i < attemptedQuestions.size(); i++) {
            Question q = attemptedQuestions.get(i);
            String givenAnswer = answerList.get(i);

            if (q.getCorrectOption().equalsIgnoreCase(givenAnswer)) {
                totalScore++;
            }
        }

        // 3️⃣ Build Result entity
        Result result = new Result();
        result.setStudent(studentRepository.findById(studentId).orElseThrow());
        result.setSubject(subjectRepository.findById(subjectId).orElseThrow());
        result.setScore(totalScore);
        result.setTotalQuestions(attemptedQuestions.size());
        result.setTestDate(LocalDateTime.now());

        // 4️⃣ Save in DB
        resultRepository.save(result);

        // 5️⃣ Send data to UI
        model.addAttribute("totalScore", totalScore);
        model.addAttribute("total", attemptedQuestions.size());
        model.addAttribute("subject", result.getSubject());
        model.addAttribute("student", result.getStudent());


        return "test_result";
    }// maps to test_result.html

    public TestController(TestRepository testRepo) {
        this.testRepo = testRepo;
    }

    @PostMapping("/submit")
    public Test submitTest(@RequestBody Test test) {
        return testRepo.save(test);
    }

    @GetMapping("/{studentId}")
    public List<Test> getTestsByStudent(@PathVariable int studentId) {
        return testRepo.findAll()
                .stream()
                .filter(t -> t.getStudent().getId() == studentId)
                .toList();
    }
}
