package fr.sii.repository;

import java.util.List;

import fr.sii.domain.TestClass;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TestRespository extends JpaRepository<TestClass, Long> {
	List<TestClass> findByname(String name);
}
