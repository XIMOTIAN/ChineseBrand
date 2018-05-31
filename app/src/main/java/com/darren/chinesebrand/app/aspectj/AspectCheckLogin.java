package com.darren.chinesebrand.app.aspectj;

import android.content.Context;
import android.content.Intent;

import com.darren.chinesebrand.app.activity.user.UserLoginActivity;
import com.darren.chinesebrand.app.user.LoginSession;
import com.darren.chinesebrand.base.AppManagerUtil;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * description:
 * created by darren on 2017/12/4 21:16
 * email: 240336124@qq.com
 * version: 1.0
 */

/**
 * Created by hcDarren on 2017/8/27.
 * 处理网络检测切面
 */
@Aspect
public class AspectCheckLogin {
    /**
     * 找到处理的切点
     * * *(..)  可以处理所有的方法
     */
    @Pointcut("execution(@com.darren.chinesebrand.app.aspectj.CheckLogin * *(..))")
    public void checkNetBehavior() {

    }

    /**
     * 处理切面
     */
    @Around("checkNetBehavior()")
    public Object checkNet(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        CheckLogin checkLogin = signature.getMethod().getAnnotation(CheckLogin.class);
        if (checkLogin != null) {
            if (!LoginSession.getLoginSession().isLogin()) {
                Context context = AppManagerUtil.instance().currentActivity();
                Intent intent = new Intent(context, UserLoginActivity.class);
                context.startActivity(intent);
                return null;
            }
        }
        return joinPoint.proceed();
    }
}
