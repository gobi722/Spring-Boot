package com.example.adminservice.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.adminservice.Model.User;
import com.example.adminservice.Service.UserService;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    @PersistenceContext
   	private EntityManager entityManager;

    public User register(User user) {
    	   user.setPassword(passwordEncoder.encode(user.getPassword()));
         entityManager.persist(user);
         entityManager.flush();
         return user;
    }
    public void changePassword(String username, String newPassword) {
    	
        User user =findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
       
        entityManager.persist(user);
    }
    public User findByUsername(String username) {
        String jpql = "SELECT u FROM User u WHERE u.username = :username";
        TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
        query.setParameter("username", username);
        return query.getSingleResult(); // or query.getResultList() if expecting multiple results
    }
}
