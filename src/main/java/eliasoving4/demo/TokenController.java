package eliasoving4.demo;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * Responsible for validating and serving requests to create tokens.
 */
@RestController
@RequestMapping(value = "token")
@EnableAutoConfiguration
@CrossOrigin
public class TokenController {

    public static String keyStr = "testsecrettestsecrettestsecrettestsecrettestsecret";
    private LogInService logInService = new LogInService();

    /**
     * Validates and fullfills token creation request.
     * @param email the email of the user that we are creating token for
     * @param password the password hash of the user we are creating token for.
     * @return List of strings with the user that is logged in and the token that was minted.
     * @throws Exception
     */
    @PostMapping(value = "")
    @ResponseStatus(value = HttpStatus.CREATED)
    public List<Object> generateToken(@RequestParam("email") final String email,
                                      @RequestParam("password") final String password) throws Exception {
        // check username and password are valid to access token
        User user = logInService.findUser(email, password);
        // note that subsequent request to the API need this token
        List<Object> strings = new ArrayList<>();
        if (user != null) {
            user.setPasswordHash("");
            strings.add(generateToken(user));
            strings.add(user);
            return strings;
        }
        System.out.println("Access denied, wrong credentials....");
        strings.add("Access denied, wrong credentials....");
        return strings;
    }

    /**
     * Creates a token given a user.
     * @param user The user we are creating token for
     * @return A string of the token.
     * @throws Exception
     */
    public String generateToken(User user) throws Exception {
        Key key = Keys.hmacShaKeyFor(keyStr.getBytes("UTF-8"));
        List<GrantedAuthority> grantedAuthorities;
        if (user.isAdmin()) {
            grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ADMIN");
        } else {
            grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("USER");
        }

        Claims claims = Jwts.claims().setSubject(Integer.toString(user.getID()));
        claims.put("userId", Integer.toString(user.getID()));
        claims.put("authorities", grantedAuthorities
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(Integer.toString(user.getID()))
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000000))
                .signWith(key)
                .compact();
    }

}
