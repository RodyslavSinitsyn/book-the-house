package bth.ui.utils;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AuthenticationAspect {

    @Pointcut("execution(public * bth.ui.service.RedisWrapper.set*(..))")
    public void publicSetMethodsInRedisWrapper() {
    }

    @Around("publicSetMethodsInRedisWrapper()")
    public Object executeIfAuthenticated(ProceedingJoinPoint joinPoint) throws Throwable {
        if (SessionUtils.isAuthenticated()) {
            return joinPoint.proceed();
        } else {
            log.trace("Skipped method execution due to unauthenticated user: {}", joinPoint.getSignature());
            return null;
        }
    }
}
