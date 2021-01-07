package com.uqtr.ca;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.uqtr.ca.logging.InterceptorModule;
import com.uqtr.ca.logging.Log;
import com.uqtr.ca.models.Course;
import com.uqtr.ca.models.Registration;
import com.uqtr.ca.models.Student;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


public class Main {
    static PersistentManager persistentManager;

    static {
        Injector injector = Guice.createInjector(new InterceptorModule());
        persistentManager = injector.getInstance(PersistentManager.class);
    }

    public static void main(String[] args) {
        try {
            display("STUDENTS", 1);
            List<Student> studentList = persistentManager.fetchAll(Student.class, "SELECT * FROM public.students LIMIT 15");
            display("STUDENTS", 0);

            display("REGISTRATIONS", 1);
            List<Registration> registrationList = persistentManager.fetchAll(Registration.class, "SELECT * FROM public.registrations");
            display("REGISTRATIONS", 0);

            display("COURSES", 1);
            List<Course> courseList = persistentManager.fetchAll(Course.class, "SELECT * FROM public.courses");
            display("COURSES", 0);
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        PersistentManager.getInstance().close();
    }

    /**
     * Display the info to the console
     *
     * @param bean   the name of the bean
     * @param status the status: 0:FINISH and 1:BEGIN
     */
    private static void display(String bean, int status) {
        if (status == 1)
            Log.logInfo("-----------------------------------------------RETRIEVING ALL" + bean +
                    "---------------------------------------------", ConsoleColors.BLACK_BOLD);
        else
            Log.logInfo("-----------------------------------------------END RETRIEVING ALL" + bean +
                    "---------------------------------------------", ConsoleColors.BLACK_BOLD);
    }
}
