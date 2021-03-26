package com.example.demo;

import com.example.controller.ConcatController;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {
    @Autowired
    private ConcatController concatController;
    @Test
    public void contextLoads() {
        try {
            concatController.inputRun();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
