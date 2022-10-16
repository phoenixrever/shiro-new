package com.phoenixhell;

import org.apache.shiro.authc.*;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.SimpleByteSource;
import org.apache.shiro.util.StringUtils;

/**
 * Subject.login 方法 把 userPasswordToken 交给SecurityManager.login()方法
 * SecurityManager 把userPasswordToken委托给认证器Authenticator进行身份验证。
 * Authenticator认证器的作用一般是用来指定如何验证，它规定本次认证用到哪些Realm
 *认证器Authenticator将传入的标识userPasswordToken，与数据源Realm获取的数据对比，验证token是否合法、
 *
 * 创建好realm后需要告诉shiro 使其生效
 *      1 在ini 文件中可以配置
 *      2 在springboot中进行配置（整合后）
 */
public class CustomRealm extends AuthenticatingRealm {
    /**
     * @param token  AuthenticationToken web端form传来的用户名和密码等信息
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //获取身份信息 这边这是打印出来看看
        String username = token.getPrincipal().toString();
        System.out.println(username);

        //获取凭证信息 这边用不到 这边只是根据用户名到数据中查找到这个用户名的真实存在数据库中的密码
        //如果加了盐 这边还要从数据库中获取盐
        Object credentials = token.getCredentials();
        //要转成字符串 不然输出[C@34ce8af7
        String password = new String((char[]) credentials);
        System.out.println((password));

        //模拟从数据库中获取用户信息 交给Authenticator来比对
        if(!StringUtils.hasText(username)){
            throw new UnknownAccountException("用户不存在");
        }
        //
        String passwordFromDatabase="899984dcb72dcb1266fa7192e35f22f4caee29808a7b25b999a6701a9ab2ef35";
        String salt="IamASalt";


        //构造返回数据AuthenticationInfo
        //salt是bySource类型
        //SimpleByteSource byteSourceSalt = new SimpleByteSource(salt);
        ByteSource byteSourceSalt = ByteSource.Util.bytes(salt);

        //realName理解成用户名就好
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(token.getPrincipal(),passwordFromDatabase,byteSourceSalt,token.getPrincipal().toString());
        return authenticationInfo;
    }
}
