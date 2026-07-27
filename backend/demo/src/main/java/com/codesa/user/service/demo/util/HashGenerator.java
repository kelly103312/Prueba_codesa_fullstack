package com.codesa.user.service.demo.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashGenerator {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        String plainPassword = args.length > 0 ? args[0] : "Admin123!";

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(plainPassword.getBytes());
        String sha256hex = HexFormat.of().formatHex(hash);

        System.out.println("Password: " + plainPassword);
        System.out.println("SHA-256:  " + sha256hex);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
        String bcryptHash = encoder.encode(sha256hex);

        System.out.println("BCrypt(SHA-256): " + bcryptHash);
    }
}
