package com.chenhao.springmall.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    // 定義切入點：所有 controller 包下的方法
    @Pointcut("execution(* com.chenhao.springmall.controller.*.*(..))")
    public void controllerPointcut() {}

    // 定義切入點：所有 service 包下的方法
    @Pointcut("execution(* com.chenhao.springmall.service.*.*(..))")
    public void servicePointcut() {}

    // 定義切入點：所有 repository 包下的方法
    @Pointcut("execution(* com.chenhao.springmall.repository.*.*(..))")
    public void repositoryPointcut() {

    }

    // 組合所有切入點
    @Pointcut("controllerPointcut() || servicePointcut() || repositoryPointcut()")
    public void allPointcuts() {}

    @Around("allPointcuts()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        // 記錄進入點日誌
        logger.info("進入方法: {}.{}() 參數: {}", className, methodName, args);

        long startTime = System.currentTimeMillis();
        Object result = null;
        try {
            // 執行目標方法
            result = joinPoint.proceed();
            return result;
        } catch (Throwable e) {
            // 記錄異常日誌
            logger.error("方法 {}.{}() 發生異常: {}", className, methodName, e.getMessage());
            throw e;
        } finally {
            // 記錄離開點日誌
            long endTime = System.currentTimeMillis();
            logger.info("離開方法: {}.{}() 返回值: {} 執行時間: {}ms", 
                       className, methodName, result, (endTime - startTime));
        }
    }
} 