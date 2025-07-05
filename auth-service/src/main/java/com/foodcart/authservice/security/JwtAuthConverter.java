//JwtAuthConverter: Extracts roles from Keycloak JWT into Spring Security authorities

package com.foodcart.authservice.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* This class maps roles embedded in the Keycloak JWT into Spring Security authorities.
* Required to make @PreAuthorize and role-based access control work.
*/
@Component
public class JwtAuthConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

 /**
  * Converts the "realm_access.roles" claim into ROLE_ authorities
  */
 @Override
 public Collection<GrantedAuthority> convert(Jwt jwt) {
     Map<String, Object> realmAccess = jwt.getClaim("realm_access");

     if (realmAccess == null || !realmAccess.containsKey("roles")) {
         return List.of();
     }

     @SuppressWarnings("unchecked")
     List<String> roles = (List<String>) realmAccess.get("roles");

     // Prefix roles with "ROLE_" to match Spring Security conventions
     return roles.stream()
             .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
             .collect(Collectors.toList());
 }
}
