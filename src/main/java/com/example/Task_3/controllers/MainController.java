package com.example.Task_3.controllers;

import com.example.Task_3.services.DatabaseSeeder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//Replace it with your name_ID (e.g random_52_0000)
@RequestMapping("/ahmed_52_2383")
public class MainController {
   @Value("FirstName")
   String firstName;
   @Value("LastName")
   String lastName;
   @Value("ID")
   String ID;
   @Value("Instance")
   String Instance;

   DatabaseSeeder databaseSeeder;

   public MainController(DatabaseSeeder databaseSeeder) {
       this.databaseSeeder = databaseSeeder;
   }

   @GetMapping
   public String Hello() {
       return "Hello From Instance "+Instance+" "+firstName+" "+lastName+" "+ID;
   }

   @PostMapping("/seed")
   public String seed() {
       databaseSeeder.seed();
       return "The database has been seeded successfully!!";
   }
}
