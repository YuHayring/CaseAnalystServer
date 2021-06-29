package cn.hayring.detecttool;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Scanner;

public class BcryptEncodedPasswordGetter {



    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    public void get() {
        String result = passwordEncoder.encode("123456");
        System.out.println(result);
    }


    @Test
    public void match() {
        String encodedPassword = "$2a$10$lNbbUMohas8DTGD6bvQxi.n7c2RS78FGNy2QNGqsBTsgM9B93gREO";
        assert passwordEncoder.matches("123456", encodedPassword);
    }
}
