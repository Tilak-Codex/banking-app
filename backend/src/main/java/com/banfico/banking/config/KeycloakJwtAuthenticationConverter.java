/*In JWt token we saw role:ADMIN 
That means the key clock defines the authority as ADMIN
But Spring boot expects the authority to be in the format of ROLE_ADMIN (for .hasrole("") to work)

So we need to convert the authority from ADMIN to ROLE_ADMIN
*/

package com.banfico.banking.config;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class KeycloakJwtAuthenticationConverter
        implements Converter<Jwt, AbstractAuthenticationToken> {   // Jwt-> input, AbstractAuthenticationToken-> output
   
    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {


        /* Claims are piece of info stored in JWT Eg: sub,preferred_username,role_acess are claims */
        Map<String, Object> realmAccess =
                jwt.getClaim("realm_access");  // realm_access is a claim in JWT which has the role information

                // If the realmAccess is null, it means user hass no role assigned and nullPointerException occurs
        if (realmAccess == null) {
            return new JwtAuthenticationToken(
                    jwt,
                    Collections.emptyList()
            );
        }

        @SuppressWarnings("unchecked")
        Collection<String> roles =
                (Collection<String>) realmAccess.get("roles");  // retirves role key

        var authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());

        return new JwtAuthenticationToken(jwt, authorities);
    }
}
