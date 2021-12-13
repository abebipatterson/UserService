package com.co.ke.moneypal.moneypal.service;

import com.co.ke.moneypal.moneypal.model.User;
import com.co.ke.moneypal.moneypal.repository.UserRepository;
import com.co.ke.moneypal.moneypal.wrapper.GeneralResponseWrapper;
import com.co.ke.moneypal.moneypal.wrapper.LoginWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AmqpTemplate rabbitTemplate1;

    @Value("${exchange}")
    String exchange;

    @Value("${bindingKey}")
    private String routingkey;


    //create a user
    public GeneralResponseWrapper createUser(User user){
        GeneralResponseWrapper generalResponseWrapper=new GeneralResponseWrapper();
        try{
            log.info("Saving the User");
            String encryptedPass=encryptPassword(user.getPassword());
            user.setPassword(encryptedPass);
            boolean cc=checkUserEmailAddress(user.getEmail().trim().toUpperCase());
            if (!cc){
                user.setEmail(user.getEmail().trim().toUpperCase());

                // PLACE TO PUT THE USER CREATED DETAILS ON RABBIT MQ  for wallet service to a
                //access then automatically creates wallet for the user
                //===========================new implementation
                rabbitTemplate1.convertAndSend(exchange, routingkey, user);
                System.out.println("++++++++++ RMQ section");
                System.out.println("User Details sent to RMQ: "+user);
                log.info("User Details sent to RMQ : {}",user);
                //====================================
                userRepository.save(user);
                generalResponseWrapper.setResponseCode(201);
                generalResponseWrapper.setResponseMessage("User Created Successfully");
                generalResponseWrapper.setResponseBody(user);
            }
            else{
                generalResponseWrapper.setResponseCode(400);
                generalResponseWrapper.setResponseMessage("User with Email Addresss Already Exist");
                generalResponseWrapper.setResponseBody(user);
            }

        }
        catch(Exception ex){
            log.error(ex.getMessage());
            ex.printStackTrace();
            generalResponseWrapper.setResponseCode(500);
            generalResponseWrapper.setResponseMessage("Internal Server Error");

        }
        return generalResponseWrapper;
    }

    public String encryptPassword(String plainTextPassword) {
        String encryptedPassword = null;
        encryptedPassword = passwordEncoder.encode(plainTextPassword);
        return encryptedPassword;
    }

    //delete the user

    //inactivate user

    //approve users

    // login of user
    public GeneralResponseWrapper userLogin(LoginWrapper loginWrapper){
        GeneralResponseWrapper generalResponseWrapper=new GeneralResponseWrapper();
        try{
            log.info("Login in user");
            String encryptedPass=encryptPassword(loginWrapper.getPassword());
            Optional<User> user=userRepository.findByUserName(loginWrapper.getUserName().trim());
            if(user.isPresent()){
                User user1=user.get();
                String pass=user1.getPassword().trim();
                if (passwordEncoder.matches(loginWrapper.getPassword().trim(),pass)) {
                    generalResponseWrapper.setResponseCode(200);
                    generalResponseWrapper.setResponseMessage("Login Successful");
                    generalResponseWrapper.setResponseBody(user1);
                }
                else{
                    generalResponseWrapper.setResponseCode(400);
                    generalResponseWrapper.setResponseMessage("Invalid Password");
                    generalResponseWrapper.setResponseBody(loginWrapper);
                }

            }
            else{
                generalResponseWrapper.setResponseCode(404);
                generalResponseWrapper.setResponseMessage("User with UserName : " +loginWrapper.getUserName() +" NOT Found");
                generalResponseWrapper.setResponseBody(loginWrapper);
            }

        }
        catch(Exception ex){
            log.error(ex.getMessage());
            ex.printStackTrace();
            generalResponseWrapper.setResponseCode(500);
            generalResponseWrapper.setResponseMessage("Internal Server Error");
            generalResponseWrapper.setResponseBody(loginWrapper);

        }
        return generalResponseWrapper;
    }


    //check if that userName exist
    public GeneralResponseWrapper checkUserUserName(String userName){
        GeneralResponseWrapper generalResponseWrapper=new GeneralResponseWrapper();
        try{
            log.info("checkUserName");
            Optional<User> user=userRepository.findByUserName(userName.trim());
            if(user.isPresent()){
                User user1=user.get();
                User u=new User();
                u.setFirstName(user1.getFirstName());
                u.setId(user1.getId());
                u.setLastName(user1.getLastName());
                u.setEmail(user1.getEmail());
                u.setUserName(user1.getUserName());
                generalResponseWrapper.setResponseCode(200);
                generalResponseWrapper.setResponseMessage("UserName Exist");
                generalResponseWrapper.setResponseBody(u);

            }
            else{
                generalResponseWrapper.setResponseCode(404);
                generalResponseWrapper.setResponseMessage("User with UserName : " +userName +" NOT Found");
                generalResponseWrapper.setResponseBody(userName);
            }

        }
        catch(Exception ex){
            log.error(ex.getMessage());
            ex.printStackTrace();
            generalResponseWrapper.setResponseCode(500);
            generalResponseWrapper.setResponseMessage("Internal Server Error");
            generalResponseWrapper.setResponseBody(null);

        }
        return generalResponseWrapper;
    }


    public Boolean checkUserEmailAddress(String emailAddress){
        boolean res=false;
        try{
            log.info("checkUserName");
            Optional<User> user=userRepository.findByEmail(emailAddress.trim().toUpperCase());
            if(user.isPresent()){
               res=true;

            }
            else{
                res=false;
            }

        }
        catch(Exception ex){
            log.error(ex.getMessage());
            ex.printStackTrace();
            res=true;
        }
        return res;
    }

    public GeneralResponseWrapper userByEmail(String email){
        GeneralResponseWrapper generalResponseWrapper=new GeneralResponseWrapper();
        try{
            log.info("Get User By Email");
            Optional<User> user=userRepository.findByEmail(email.trim().toUpperCase());
            if(user.isPresent()){
                User user1=user.get();
                User u=new User();
                u.setFirstName(user1.getFirstName());
                u.setId(user1.getId());
                u.setLastName(user1.getLastName());
                u.setEmail(user1.getEmail());
                u.setUserName(user1.getUserName());
                generalResponseWrapper.setResponseCode(200);
                generalResponseWrapper.setResponseMessage("Email Address Exist");
                generalResponseWrapper.setResponseBody(u);

            }
            else{
                generalResponseWrapper.setResponseCode(404);
                generalResponseWrapper.setResponseMessage("User with Email Address : " +email +" NOT Found");
            }

        }
        catch(Exception ex){
            log.error(ex.getMessage());
            ex.printStackTrace();
            generalResponseWrapper.setResponseCode(500);
            generalResponseWrapper.setResponseMessage("Internal Server Error");

        }
        return generalResponseWrapper;
    }



}
