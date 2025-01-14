package md.hajji.inventoryservice.web;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/auth")
public class AuthRestController {



    @GetMapping
    public ResponseEntity<?> auth() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<String> roles = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return ResponseEntity.ok(new AuthRecord(userId, roles));
    }

}


record AuthRecord(
        String userId,
        List<String> roles
){}