// 
// Decompiled by Procyon v0.5.36
// 

package com.oasys.helpdesk.conf.filter;

import org.slf4j.LoggerFactory;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Around;
import java.util.Arrays;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Aspect;

@Aspect
@Component
public class LoggingAspect
{
    private static final Logger log;
    
    @Around("execution(public * com.oasys..*.*(..))")
    public Object logAround(final ProceedingJoinPoint joinPoint) throws Throwable {
        final Long startTime = System.currentTimeMillis();
        final Object className = joinPoint.getTarget().getClass().getName();
        final String methodName = joinPoint.getSignature().getName();
        log.info("Request," + className + "," + methodName + ",(" + Arrays.asList(joinPoint.getArgs()).toString() + ")");
        final Object response = joinPoint.proceed();
        final Long endTime = System.currentTimeMillis() - startTime;
		log.info("Response" +","+ className +","+ methodName +","+"("+ Arrays.asList(joinPoint.getArgs()).toString()  +")"+","+ response +","+ endTime);
        return response;
    }
    
    @AfterThrowing(pointcut = "execution(public * com.oasys..*.*(..))", throwing = "ex")
    public void logAfterThrowing(final JoinPoint joinpoint, final Exception ex) {
        final Object className = joinpoint.getTarget().getClass().getName();
        final String methodName = joinpoint.getSignature().getName();
        LoggingAspect.log.error("Error"+"," + className + "," + methodName + ",(" + Arrays.asList(joinpoint.getArgs()).toString() + ")," + ex.getMessage());
    }
    
    static {
        log = LoggerFactory.getLogger((Class)LoggingAspect.class);
    }
}
