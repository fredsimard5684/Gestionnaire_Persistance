package com.uqtr.ca.models;

import com.uqtr.ca.orm.Entity;
import com.uqtr.ca.orm.DbField;

@Entity(tablename = "courses")
public class Course {
    private int id;

    @DbField(name = "sigle")
    private String acronym;
    private String name;
    private String description;
}
