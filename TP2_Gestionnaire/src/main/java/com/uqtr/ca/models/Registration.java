package com.uqtr.ca.models;

import com.uqtr.ca.orm.Entity;
import com.uqtr.ca.orm.DbField;
import com.uqtr.ca.orm.HasOne;

@Entity(tablename = "registrations")
public class Registration {
    private int id;

    @DbField(name = "student_id")
    private int studentId;

    @DbField(name = "course_id")
    private int courseId;

    @HasOne(fk = "course_id", klass = Course.class)
    private Course course;
}