package com.phoenixhell;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.env.BasicIniEnvironment;
import org.apache.shiro.env.Environment;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

public class HelloShiro {
    public static void main(String[] args) {
        //1 初始化获取SecurityManager
        //导入权限ini文件构建权限工厂 过时方法
        //IniSecurityManagerFactory securityManagerFactory = new IniSecurityManagerFactory("classpath:shiro.ini");
        //由securityManagerFactory工厂获得安全管理器实例
        //SecurityManager securityManager = securityManagerFactory.getInstance();

        //https://shiro.apache.org/static/1.5.0/apidocs/org/apache/shiro/env/BasicIniEnvironment.html
        Environment env = new BasicIniEnvironment("classpath:shiro.ini");
        SecurityManager securityManager = env.getSecurityManager();

        //安全管理器放入SecurityUtils（shiro）中
        SecurityUtils.setSecurityManager(securityManager);

        //2 从SecurityUtils（shiro）中获取subject对象
        Subject subject = SecurityUtils.getSubject();

        //3 模拟构建账号token，实际从web页面端传递
        UsernamePasswordToken token = new UsernamePasswordToken("admin", "123456");

        //4 登录操作 AuthenticationException 为登录最大异常
        try {
            subject.login(token);
            System.out.println("是否登录成功：" + subject.isAuthenticated());
            //5 判断角色
            boolean role = subject.hasRole("admin");
            System.out.println("是否拥有此角色" + role);

            //5 判断权限
            boolean permitted = subject.isPermitted("user:add");
            System.out.println("是否拥有此权限" + permitted);

            //也可以用 checkPermission 此方法没有返回值 无权限会抛UnauthorizedException异常
            subject.checkPermission("user:add1");


        } catch (UnknownAccountException e) {
            System.out.println("用户不存在:" + e);
        } catch (IncorrectCredentialsException e) {
            System.out.println("密码错误:" + e);
        } catch (LockedAccountException e) {
            System.out.println("账号已被锁定,请联系管理员:" + e);
        } catch (UnauthorizedException e) {
            System.out.println("你无此权限:" + e);
        } catch (AuthenticationException e) {
            System.out.println("登录失败 未知错误:" + e);
        }
    }
}
