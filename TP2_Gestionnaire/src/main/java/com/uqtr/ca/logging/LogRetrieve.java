package com.uqtr.ca.logging;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uqtr.ca.ConsoleColors;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Intercept the retrieves method to log the sql info before and after retrieving
 */
public class LogRetrieve implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {

        Object s = methodInvocation.getArguments()[1];

        Log.logInfo("BEGIN:: EXECUTING THE FOLLOWING METHOD ---> " + methodInvocation.getMethod().getName() +
                        "; WITH THE SQL QUERY ---> " + s,
                ConsoleColors.CYAN_BOLD_BRIGHT);

        Object result = methodInvocation.proceed();


        Gson jsonOneLine = new GsonBuilder().setPrettyPrinting().create();

        String remove = "com.uqtr.ca.models.";
        String beanName = methodInvocation.getArguments()[0].toString().replace(remove, "");

        Log.logInfo("The following information have been inserted into the bean " +
                        beanName + " by the request --> " + s,
                ConsoleColors.PURPLE);

        Log.logInfo(jsonOneLine.toJson(result), ConsoleColors.BLUE);
        Log.logInfo("FINISH:: THE EXECUTION FOR THE FOLLOWING METHOD ---> " + methodInvocation.getMethod().getName() +
                        "; WITH THE ASSOCIATED SQL RESQUEST: " + s + "\n",
                ConsoleColors.CYAN_BOLD_BRIGHT);
        return result;

    }
}
