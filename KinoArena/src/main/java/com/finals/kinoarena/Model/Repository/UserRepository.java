package com.finals.kinoarena.Model.Repository;

import com.finals.kinoarena.Model.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    public User findByEmail(String email);
    public User findByUsername(String username);

}
