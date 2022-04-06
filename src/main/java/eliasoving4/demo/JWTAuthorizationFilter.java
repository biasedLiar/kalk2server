package eliasoving4.demo;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.apache.el.parser.Token;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.security.Key;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Checks if request has proper token that matches userID and sets authorization of user.
 */
public class JWTAuthorizationFilter extends OncePerRequestFilter {
    private final String HEADER = "Authorization";

    /**
     * Checks if request has proper token that matches userID and sets authorization of user.
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            Key key = Keys.hmacShaKeyFor(TokenController.keyStr.getBytes("UTF-8"));

            // expects JWT in the header
            String authenticationHeader = request.getHeader(HEADER);
            final String PREFIX = "Bearer ";

            // check Authorization header exists
            if (authenticationHeader == null || !authenticationHeader.startsWith(PREFIX)){
                SecurityContextHolder.clearContext();
            } else {
                // get token and claims
                String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
                Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwtToken);

                // perform necessary checks
                if (claims.getBody().get("authorities") != null) {
                    // setup Spring authentication
                    List<String> authorities = (List) claims.getBody().get("authorities");
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getBody().getSubject(), null,
                            authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    //Checks that the user id param is the same as the userid in the token.
                    String userId = "";
                    String[] params = request.getQueryString().split("&");
                    for (int i = 0; i < params.length; i++) {
                        if (params[i].split("=")[0].equals("userId")){
                            userId = params[i].split("=")[1];
                        }
                    }
                    if (Integer.parseInt(claims.getBody().get("userId", String.class)) == Integer.parseInt(userId)){
                        //System.out.println("User ids match");
                    } else {
                        System.out.println("User id sent does not match user id in token.");
                        SecurityContextHolder.clearContext();
                    }
                } else {
                    System.out.println("no auth.");
                    SecurityContextHolder.clearContext();
                }
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
            return;
        }
    }
}
