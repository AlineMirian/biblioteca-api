package com.biblioteca.biblioteca_api.api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biblioteca.biblioteca_api.api.domain.model.User;
import com.biblioteca.biblioteca_api.api.domain.repository.UserRepository;
import com.biblioteca.biblioteca_api.api.exception.BusinessException;
import com.biblioteca.biblioteca_api.api.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }
    
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }
    
    @Transactional
    public User create(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BusinessException("User with email " + user.getEmail() + " already exists");
        }
        
        if (userRepository.existsByCpf(user.getCpf())) {
            throw new BusinessException("User with CPF " + user.getCpf() + " already exists");
        }
        
        return userRepository.save(user);
    }
    
    @Transactional
    public User update(Long id, User userDetails) {
        User user = findById(id);
        
        user.setName(userDetails.getName());
        user.setPhone(userDetails.getPhone());
        user.setActive(userDetails.getActive());
        
        return userRepository.save(user);
    }
    
    @Transactional
    public void delete(Long id) {
        User user = findById(id);
        userRepository.delete(user);
    }
}
