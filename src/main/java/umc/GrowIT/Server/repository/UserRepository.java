package umc.GrowIT.Server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.GrowIT.Server.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>  {

    Optional<User> findByPrimaryEmail(String primaryEmail);

    boolean existsByPrimaryEmail(String primaryEmail);

    @Query("SELECT user.password FROM User user WHERE user.primaryEmail = :primaryEmail")
    String findPasswordByPrimaryEmail(@Param("primaryEmail") String primaryEmail);
}