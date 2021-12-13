package com.co.ke.moneypal.moneypal;

import com.co.ke.moneypal.moneypal.model.User;
import com.co.ke.moneypal.moneypal.repository.UserRepository;
import com.co.ke.moneypal.moneypal.service.UserService;
import com.co.ke.moneypal.moneypal.wrapper.GeneralResponseWrapper;
import com.co.ke.moneypal.moneypal.wrapper.LoginWrapper;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


@SpringBootTest
//@RunWith(Suite.class)
//@Suite.SuiteClasses({
//        UserServiceTest.class
//})
class MoneypalApplicationTests {

    @Test
    public void contextLoads() {
    }

    // initialize all objects with @Mock and @InjectMocks Annotations
    @Mock
    private UserRepository userRepository;

    @Autowired
    private UserService userService;
    // test create user
    @Test
    public void createUserTest(){
        User user=new User();
        user.setEmail("nelson678967899@gmail.com");
        user.setUserName("Nel0909888op");
        user.setFirstName("Nelson");
        user.setLastName("Moses");
        user.setPassword("Nel12345678999");

        GeneralResponseWrapper g=userService.createUser(user);
        User u= (User) g.getResponseBody();
        assertNotEquals(u, null);
        //assertNotEquals(u.getId(),null);
        assertEquals(u.getEmail(),user.getEmail());
        assertEquals(u.getFirstName(),user.getFirstName());

    }

    //test user login
    @Test
    public void loginUserTest(){
        LoginWrapper loginWrapper=new LoginWrapper();
        loginWrapper.setUserName("Nel0909888op");
        loginWrapper.setPassword("Nel12345678999");
        GeneralResponseWrapper g=userService.userLogin(loginWrapper);
        assertNotEquals(g.getResponseBody(), null);
    }

    //test on getting user by email
    @Test
    public void getUserByEmailTest(){
        Boolean check=userService.checkUserEmailAddress("nelson678967899@gmail.com");//
        assertEquals(check,true);
    }


    @Test
    public void  userByEmailTest(){
        String email="nelson678967899@gmail.com";
        GeneralResponseWrapper generalResponseWrapper=userService.userByEmail(email);//
        User u= (User) generalResponseWrapper.getResponseBody();
        assertNotEquals(generalResponseWrapper.getResponseBody(),null);
        assertEquals(u.getEmail(),email.toUpperCase());
        assertEquals(generalResponseWrapper.getResponseCode(),200);

    }
}
