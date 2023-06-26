package ecommerce.user.service;

import ecommerce.user.model.User;
import ecommerce.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.jms.core.JmsTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private JmsTemplate jmsTemplate;

    public UserService(UserRepository userRepository, JmsTemplate jmsTemplate) {
        this.userRepository = userRepository;
        this.jmsTemplate = jmsTemplate;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    private void sendUserCreatedMessage(Long userId) {
        jmsTemplate.convertAndSend("userCreatedQueue", userId);
    }
    public User registerUser(User user) {

        userRepository.save(user);
        // After successfully saving the user, send a message to create a new cart for this user
        sendUserCreatedMessage(user.getId());
        return user;
    }


    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
