package com.phoenixhell;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;

import java.util.UUID;

public class ShiroMD5 {
    public static void main(String[] args) {
        /**
         * md5 特点
         * 无论多长多随意的信息，最后都转换成一个固定长度的散列值；
         * 对于大量不同的信息，最后出来的散列值呈平均分布；
         * 对于特定的一个信息，最后出来的散列值都是相同的。
         */

        //建议 选择SimpleHash 使用SHA2 加密 MD5 与sha1 已经不安全了 如果要使用请一定要加盐


        //密码明文
        String password = "123456";
        Md5Hash md5Hash = new Md5Hash(password);
        //e10adc3949ba59abbe56e057f20f883e  加密后的值为固定的 默认为toString toString会调用toHex()
        System.out.println("md5加密后="+md5Hash);
        //解密

        //第二个参数  加固定盐，盐就是在明文后面拼接新字符串，让后一起进行加密
        Md5Hash md5HashSalt = new Md5Hash(password,"IamASalt");
        System.out.println("md5加盐加密后="+md5HashSalt.toString());


        //第三个参数 迭代加密 就是把生成的md5 字符串再次md5 加密
        Md5Hash md5HashIterations = new Md5Hash(password,"IamASalt",3);
        System.out.println("md5加盐3次迭代加密后="+md5HashIterations.toHex());


        //使用父类进行加密，这个父类不只是可以选择MD5加密
        SimpleHash simpleHash = new SimpleHash("MD5", password, "IamASalt", 3);
        System.out.println("使用父类SimpleHash MD5 加盐IamASalt="+simpleHash.toHex());

        //使用父类进行加密，选择SHA加密  可以选择的algorithmName 见文档
        SimpleHash hash = new SimpleHash("SHA-256", password, "IamASalt", 3);
        System.out.println("使用父类SimpleHash SHA-256 加盐IamASalt="+hash.toHex());

        //=====================盐的选择规则=========================

        /**
         * 不能在代码中写死盐，且盐需要有一定的长度（盐写死太简单的话，黑客可能注册几个账号反推出来）；
         * 每一个密码都有独立的盐，并且盐要长一点，比如超过 20 位（盐太短，加上原始密码太短，容易破解）；
         * 最好是随机的值，并且是全球唯一的，意味着全球不可能有现成的彩虹表给你用；
         */

        //随机盐 UUID为每个用户生成盐，并将生成的加密文和盐一起存入数据库
        String salt = UUID.randomUUID().toString();
        Md5Hash randomSalt = new Md5Hash(password,salt);
        System.out.println("随机盐="+hash.toHex());

        //存入数据库  ...  比对密码的时候取出数据库中的盐 来比对md5加密后的值是否相同
    }
}
