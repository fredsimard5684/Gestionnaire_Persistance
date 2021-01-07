package com.uqtr.ca.models;

import com.uqtr.ca.orm.Entity;
import com.uqtr.ca.orm.HasMany;
import com.uqtr.ca.orm.Ignore;

import java.io.Serializable;
import java.util.List;

@Entity(tablename = "students")
public class Student {
    private int id;
    private String fname;
    private String lname;
    private int age;

    @Ignore
    private int computedStuffThatIsNotInDatabase;

    // SELECT * FROM registration WHERE student_id=:id
    @HasMany(fk = "student_id", klass = Registration.class)
    private List<Registration> registrations;
}
