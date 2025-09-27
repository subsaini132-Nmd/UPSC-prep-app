package exam;

import exam.model.Student;
import exam.model.Subject;
import exam.repo.StudentRepository;
import exam.repo.SubjectRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
public class UpscPrepApplication implements CommandLineRunner {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    public static void main(String[] args) {
        SpringApplication.run(UpscPrepApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // --- Test the Application ---

        // Add a subject
        Subject polity = new Subject();
        polity.setName("Polity");
        subjectRepository.save(polity);

        // Add a student
        Student s1 = new Student();
        s1.setName("Subhash");
        studentRepository.save(s1);

        // Print all students
        System.out.println("✅ Students in DB:");
        studentRepository.findAll().forEach(st ->
                System.out.println(st.getId() + " -> " + st.getName())
        );

        // Print all subjects
        System.out.println("✅ Subjects in DB:");
        subjectRepository.findAll().forEach(sub ->
                System.out.println(sub.getId() + " -> " + sub.getName())
        );
    }
}
