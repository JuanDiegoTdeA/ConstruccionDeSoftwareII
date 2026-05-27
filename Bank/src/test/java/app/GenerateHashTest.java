package app;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerateHashTest {

    @Test
    void generateHash() {
        String hash = new BCryptPasswordEncoder().encode("123456");
        System.out.println("HASH: " + hash);
    }
}