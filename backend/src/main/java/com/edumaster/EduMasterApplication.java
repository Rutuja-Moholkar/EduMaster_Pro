package com.edumaster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Main Application Class for EduMaster Pro
 * Enterprise E-Learning Platform
 * 
 * @author EduMaster Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableTransactionManagement
public class EduMasterApplication {

    public static void main(String[] args) {
        SpringApplication.run(EduMasterApplication.class, args);
    }
}