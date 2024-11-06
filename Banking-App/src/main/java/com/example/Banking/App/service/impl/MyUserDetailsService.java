package com.example.Banking.App.service.impl;

import com.example.Banking.App.entity.Account;
import com.example.Banking.App.entity.MyUserDetails;
import com.example.Banking.App.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private AccountRepository repository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = repository.findByAccountHolderName(username);
        if(account == null){
            throw new UsernameNotFoundException("User not found!");
        }
        System.out.println("Found user: " + account.getAccountHolderName());
        System.out.println("Password: " + account.getPassword());
        return new MyUserDetails(account);
    }
}
