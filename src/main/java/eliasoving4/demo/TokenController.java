package eliasoving4.demo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
     * @param username the username of the person that we are creating token for
     * @param password the password hash of the person we are creating token for.
     * @return List of strings with the person that is logged in and the token that was minted.
     * @throws Exception
     */
    @PostMapping(value = "")
    @ResponseStatus(value = HttpStatus.CREATED)
    public List<Object> generateToken(@RequestParam("username") final String username,
                                      @RequestParam("password") final String password) throws Exception {
        // check personname and password are valid to access token
        Person person = logInService.findPerson(username, password);
        // note that subsequent request to the API need this token
        List<Object> strings = new ArrayList<>();
        if (person != null) {
            person.setPasswordHash("");
            strings.add(generateToken(person));
            strings.add(person);
            return strings;
        }
        System.out.println("Access denied, wrong credentials....");
        strings.add("Access denied, wrong credentials....");
        return strings;
    }

    /**
     * Creates a token given a person.
     * @param person The person we are creating token for
     * @return A string of the token.
     * @throws Exception
     */
    public String generateToken(Person person) throws Exception {
        Key key = Keys.hmacShaKeyFor(keyStr.getBytes("UTF-8"));
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ADMIN");


        Claims claims = Jwts.claims().setSubject(Integer.toString(person.getId()));
        claims.put("personId", Integer.toString(person.getId()));
        claims.put("authorities", grantedAuthorities
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(Integer.toString(person.getId()))
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000000))
                .signWith(key)
                .compact();
    }

}
