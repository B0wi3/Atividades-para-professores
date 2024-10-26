package com.atividades.bowie.service;

import com.atividades.bowie.api.model.LoginBody;
import com.atividades.bowie.api.model.RegistrationBody;
import com.atividades.bowie.exception.IncorrectPasswordException;
import com.atividades.bowie.exception.UserAlreadyExistsException;
import com.atividades.bowie.exception.UsernameNotFoundException;
import com.atividades.bowie.model.LocalUser;
import com.atividades.bowie.model.dao.LocalUserDAO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private LocalUserDAO localUserDAO;
    private EncryptionService encryptionService;

    public UserService(LocalUserDAO localUserDAO, EncryptionService encryptionService) {
        this.localUserDAO = localUserDAO;
        this.encryptionService = encryptionService;
    }

    public LocalUser registerUser(RegistrationBody registrationBody) throws UserAlreadyExistsException {
        System.out.println("Tentando registrar usuário: " + registrationBody.getUsername());
        if (localUserDAO.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent()
        || localUserDAO.findByUsernameIgnoreCase(registrationBody.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        LocalUser user = new LocalUser();
        user.setUsername(registrationBody.getUsername());
        user.setEmail(registrationBody.getEmail());
        user.setPassword(encryptionService.encryptPassword(registrationBody.getPassword()));
        return localUserDAO.save(user);
    }

    public LocalUser loginUser(LoginBody loginBody) throws UsernameNotFoundException, IncorrectPasswordException {
        LocalUser user = localUserDAO.findByUsernameIgnoreCase(loginBody.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + loginBody.getUsername()));
        if (!encryptionService.verifyPassword(loginBody.getPassword(), user.getPassword())) {
            throw new IncorrectPasswordException("Incorrect password for user: " + user.getUsername());
        }
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws org.springframework.security.core.userdetails.UsernameNotFoundException {
        return null;
    }
}
