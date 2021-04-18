package com.finals.kinoarena.service;

import com.finals.kinoarena.model.entity.UserStatus;
import com.finals.kinoarena.util.Constants;
import com.finals.kinoarena.util.exceptions.BadRequestException;
import com.finals.kinoarena.model.DTO.RegisterDTO;
import com.finals.kinoarena.model.DTO.EditUserPasswordDTO;
import com.finals.kinoarena.model.DTO.UserWithoutPassDTO;
import com.finals.kinoarena.model.DTO.UserWithoutTicketAndPassDTO;
import com.finals.kinoarena.model.entity.ConfirmationToken;
import com.finals.kinoarena.model.entity.User;
import com.finals.kinoarena.model.repository.ConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService extends AbstractService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Transactional
    public UserWithoutPassDTO registerUser(RegisterDTO registerDTO) throws BadRequestException {
        if (userRepository.findByEmail(registerDTO.getEmail()) != null) {
            throw new BadRequestException("There is already a user with that email address: " + registerDTO.getEmail());
        }
        if (userRepository.findByUsername(registerDTO.getUsername())!= null) {
            throw new BadRequestException("There is already a user with that username: " + registerDTO.getUsername());
        }
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            throw new BadRequestException("Passwords must match");
        }
        User user = User.builder()
                .username(registerDTO.getUsername())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .email(registerDTO.getEmail())
                .firstName(registerDTO.getFirstName())
                .lastName(registerDTO.getLastName())
                .age(registerDTO.getAge())
                .roleId(Constants.ROLE_USER)
                .statusId(UserStatus.valueOf(registerDTO.getStatus().toUpperCase()).ordinal() + 1)
                .createdAt(LocalDateTime.now())
                .tickets(new ArrayList<>())
                .isEnabled(false)
                .build();
        user = userRepository.save(user);
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationTokenRepository.save(confirmationToken);
        return new UserWithoutPassDTO(user);
    }

    public UserWithoutTicketAndPassDTO logInUser(String username, String password) throws BadRequestException {
        User user = userRepository.findByUsername(username);
        if (passwordEncoder.matches(password, user.getPassword())) {
            return new UserWithoutTicketAndPassDTO(user);
        }
        throw new BadRequestException("Username or Password incorrect");
    }

    public UserWithoutTicketAndPassDTO changePassword(EditUserPasswordDTO passwordDTO, int userId) throws BadRequestException {
        if (!passwordDTO.getNewPassword().equals(passwordDTO.getConfirmPassword())) {
            throw new BadRequestException("Passwords must match");
        }
        User user = userRepository.findById(userId).get();
        if (passwordEncoder.matches(passwordDTO.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
            return new UserWithoutTicketAndPassDTO(userRepository.save(user));
        }
        throw new BadRequestException("Username or Password incorrect");
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
