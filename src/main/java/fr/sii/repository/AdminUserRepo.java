package fr.sii.repository;

import fr.sii.entity.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminUserRepo extends JpaRepository<AdminUser, Integer> {
    AdminUser findByEmail(String email);
}
