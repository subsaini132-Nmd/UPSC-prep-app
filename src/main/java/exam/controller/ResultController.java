package exam.controller;

import exam.model.Result;
import exam.repo.ResultRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/results")
public class ResultController {

    private final ResultRepository resultRepository;

    public ResultController(ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    @GetMapping("/{studentId}")
    public String viewResults(@PathVariable Long studentId, Model model) {
        List<Result> results = resultRepository.findByStudentId(studentId);
        System.out.println(studentId);
        System.out.println(results);
        model.addAttribute("results", results);
        return "results";  // Thymeleaf page
    }
}
