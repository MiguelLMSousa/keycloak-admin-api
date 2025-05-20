package com.example.keycloak_admin_api.authflow;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;

@RequiredArgsConstructor
@RestController
@RequestMapping("/authflows")
public class AuthFlowController {

    private final AuthFlowService authFlowService;

    @GetMapping
    public ResponseEntity<?> listAuthFlows() {
        return ok(authFlowService.listAuthFlows());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> cloneAuthFlow(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.CREATED).body(Optional.ofNullable(authFlowService.cloneAuthFlow(id)).orElseThrow(BadRequestException::new));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteOldAuthFlows() {
        return authFlowService.deleteOldAuthFlows().isEmpty() ? ResponseEntity.status(HttpStatus.GONE).build(): ResponseEntity.noContent().build();
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> notFound(){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> badRequest(){
        return ResponseEntity.badRequest().build();
    }

}
