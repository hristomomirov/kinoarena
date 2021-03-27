package com.finals.kinoarena.Model.Repository;

import com.finals.kinoarena.Model.Entity.Cinema;
import com.finals.kinoarena.Model.Entity.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Integer> {
    ConfirmationToken findByConfirmationToken(String confirmationToken);
    ConfirmationToken findByUser_Id(int id);

}
