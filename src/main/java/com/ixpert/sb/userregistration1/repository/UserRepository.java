package com.ixpert.sb.userregistration1.repository;


import com.ixpert.sb.userregistration1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
    User findByConfirmationToken(String confirmationToken);

}
