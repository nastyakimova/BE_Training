package com.github.training.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/applicationСontext.xml");
        User firstUser = (User) applicationContext.getBean("user1");
        System.out.println(firstUser.toString());

        User secondUser = (User) applicationContext.getBean("user2");
        System.out.println(secondUser.toString());

        ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        User user = (User) ctx.getBean("user");
        user.setFirstName("Aleksey");
        System.out.println(user.getFirstName());

    }
}
