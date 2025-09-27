package exam.repo;

import exam.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface StudentRepository extends JpaRepository<Student, Integer> {
        Optional<Student> findFirstByOrderByIdAsc();
    }

