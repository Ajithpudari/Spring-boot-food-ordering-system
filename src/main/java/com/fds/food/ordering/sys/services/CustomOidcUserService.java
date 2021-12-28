package com.fds.food.ordering.sys.services;

import com.fds.food.ordering.sys.models.GoogleUserInfo;
import com.fds.food.ordering.sys.models.Role;
import com.fds.food.ordering.sys.models.User;
import com.fds.food.ordering.sys.repositories.RoleRepository;
import com.fds.food.ordering.sys.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.*;

@Service

public class CustomOidcUserService extends OidcUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
//        System.out.println(oidcUser.getAuthorities());
        try {
            return processOidcUser(userRequest, oidcUser);
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OidcUser processOidcUser(OidcUserRequest userRequest, OidcUser oidcUser) {
        GoogleUserInfo googleUserInfo = new GoogleUserInfo(oidcUser.getAttributes());
        // see what other data from userRequest or oidcUser you need
        Optional<User> userOptional = Optional.ofNullable(userRepository.getUserByUsername(googleUserInfo.getEmail()));
        if (!userOptional.isPresent()) {
            User user = new User();
            user.setUsername(googleUserInfo.getEmail());
            user.setEnabled(true);
            user.setPassword(new BCryptPasswordEncoder().encode("i_know_this_is_not_CORRECT_WAY_MAYBE"));
            Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.getRole("ADMIN"));
            user.setRoles(roles);
            userRepository.save(user);
        }
        OidcUser oidcUser1 = new OidcUser() {
            @Override
            public Map<String, Object> getClaims() {
                return oidcUser.getClaims();
            }

            @Override
            public OidcUserInfo getUserInfo() {
                return oidcUser.getUserInfo();
            }

            @Override
            public OidcIdToken getIdToken() {
                return oidcUser.getIdToken();
            }

            @Override
            public Map<String, Object> getAttributes() {
                return oidcUser.getAttributes();
            }

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();

                if(!userOptional.isPresent()){
                    Role adminRole = new Role();
                    adminRole.setName("ADMIN");
                    authorities.add(new SimpleGrantedAuthority(adminRole.getName()));
                }else {
                    Collection<Role> roles = userOptional.get().getRoles();
                    for (Role role : roles) {
                        authorities.add(new SimpleGrantedAuthority(role.getName()));
                    }
                }
                return authorities;

            }

            @Override
            public String getName() {
                return oidcUser.getName();
            }
        };
        return oidcUser1;
    }
}