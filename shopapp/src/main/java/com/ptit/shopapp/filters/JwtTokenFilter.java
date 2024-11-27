package com.ptit.shopapp.filters;

import com.ptit.shopapp.components.JwtTokenUtil;
import com.ptit.shopapp.models.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import lombok.NonNull;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
  @Value("${api.prefix}")
  private String apiPrefix;
  private final UserDetailsService userDetailsService;
  private final JwtTokenUtil jwtTokenUtil;
  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain) throws ServletException, IOException {
    try {
      // request ko yeu cau token
      if(isBypassToken(request)) {
        filterChain.doFilter(request, response);
        return;// enable by pass
      }
      final String authHeader = request.getHeader("Authorization");
      if(authHeader != null && authHeader.startsWith("Bearer ")) {
        final String token = authHeader.substring(7);
        final String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
          response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
          return;
        }
        // kiem tra neu sdt trong token nay da co va phan authenticate
        // chua duoc authenticate thi load user tu sdt do ra
        if(phoneNumber != null && SecurityContextHolder.getContext().getAuthentication() == null){
          User userDetails = (User) userDetailsService.loadUserByUsername(phoneNumber);
          if(jwtTokenUtil.validateToken(token, userDetails)) {
            UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails,
                    null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
          }
        }
      }
      // request yeu cau token
      filterChain.doFilter(request, response); //enable by pass
    } catch (Exception e){
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
  }

  private boolean isBypassToken(@NonNull HttpServletRequest request){
    final List<Pair<String, String>> bypassTokens = Arrays.asList(
        Pair.of(String.format("%s/products", apiPrefix), "GET"),
        Pair.of(String.format("%s/categories", apiPrefix), "GET"),
        Pair.of(String.format("%s/users/register", apiPrefix), "POST"),
        Pair.of(String.format("%s/users/login", apiPrefix), "POST"),
        Pair.of(String.format("%s/roles", apiPrefix), "GET")
    );

    String requestPath = request.getServletPath();
    String requestMethod = request.getMethod();

    if(requestPath.equals(String.format("%s/orders", apiPrefix)) && requestMethod.equals("GET")) {
      return true;
    }
    for (Pair<String, String> bypassToken : bypassTokens){
      if(request.getServletPath().contains(bypassToken.getFirst()) &&
          request.getMethod().equals(bypassToken.getSecond())) {
        return true;
      }
    }
    return false;
  }
}
