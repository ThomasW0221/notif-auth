package io.twinterf.notifauth.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.twinterf.notifauth.entities.User;
import io.twinterf.notifauth.repositories.UserRepository;
import io.twinterf.notifauth.token.TokenConstantContainer;
import io.twinterf.notifauth.token.TokenResponse;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.support.CustomSQLErrorCodesTranslation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Date;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private Logger logger = LoggerFactory.getLogger(AuthController.class);
    private TokenConstantContainer tokenConstants;

    private UserRepository userRepository;

    @Autowired
    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
        tokenConstants = new TokenConstantContainer();
    }

    @PostMapping("/createUser")
    public ResponseEntity<User> createUser(@RequestBody User newUser) throws URISyntaxException {
        logger.info("User creation requested: " + newUser.getUsername());
        String hashedPassword = BCrypt.hashpw(newUser.getPassword(), BCrypt.gensalt());
        newUser.setPassword(hashedPassword);
        userRepository.save(newUser);
        logger.info("User creation successful: " + newUser.getUsername());
        return ResponseEntity.created(new URI("/users/" + newUser.getUsername())).build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody User user) {
        logger.info("Login requested for: " + user.getUsername());
        var requestedUser = userRepository.findById(user.getUsername()).orElse(null);
        if (requestedUser == null) {
            logger.error("could not find username: " + user.getUsername());
            return ResponseEntity.notFound().build();
        }
        if(!BCrypt.checkpw(user.getPassword(), requestedUser.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var tokenString = generateTokenForUser(requestedUser);

        logger.info("Login successful for: " + requestedUser.getUsername());
        return ResponseEntity.ok(new TokenResponse(tokenString));

    }

    private String generateTokenForUser(User u) {
        return JWT.create()
                .withIssuer(tokenConstants.getJwtIssuer())
                .withClaim("user", u.getUsername())
                .withExpiresAt(Date.from(Instant.now().plusMillis(300000)))
                .sign(Algorithm.HMAC256(tokenConstants.getJwtSignature()));
    }
}
