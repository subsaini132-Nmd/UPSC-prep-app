package exam.controller;

import exam.model.Question;
import exam.model.Subject;
import exam.repo.QuestionRepository;
import exam.repo.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    // Show form to add new question
    @GetMapping("/add")
    public String showAddQuestionForm(Model model) {
        model.addAttribute("question", new Question());
        model.addAttribute("subjects", subjectRepository.findAll()); // dropdown
        return "add_question";  // maps to add_question.html
    }

    // Save question
    @PostMapping("/add")
    public String saveQuestion(@ModelAttribute Question question,
                               @RequestParam int subjectId) {
        Subject subject = subjectRepository.findById(subjectId).orElseThrow();
        question.setSubject(subject);
        questionRepository.save(question);

        return "redirect:/questions/list";
    }

    // List all questions
    @GetMapping("/list")
    public String listQuestions(Model model) {
        model.addAttribute("questions", questionRepository.findAll());
        return "list_questions"; // maps to list_questions.html
    }
}
