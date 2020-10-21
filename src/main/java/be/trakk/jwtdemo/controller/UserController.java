package be.trakk.jwtdemo.controller;

import be.trakk.jwtdemo.dto.UserDTO;
import be.trakk.jwtdemo.form.UserForm;
import be.trakk.jwtdemo.service.impl.UserDetailsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserDetailsServiceImpl userDetailsService;

    public UserController(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll() {
        return ResponseEntity.ok(this.userDetailsService.getAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<UserDTO> getOne(@PathVariable("id") Long id) {
        return ResponseEntity.ok(this.userDetailsService.getOne(id));
    }

    @PostMapping
    public ResponseEntity<Boolean> insert(@RequestBody UserForm form) {

        boolean inserted = this.userDetailsService.insert(form);

        return inserted ? ResponseEntity.ok(true) : ResponseEntity.badRequest().body(false);

    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") Long id) {

        boolean deleted = this.userDetailsService.delete(id);

        return deleted ? ResponseEntity.ok(true) : ResponseEntity.badRequest().body(false);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<UserDTO> update(@RequestBody UserForm form, @PathVariable("id") Long id) {
        return ResponseEntity.ok(this.userDetailsService.update(form, id));
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<UserDTO> patch(@RequestBody Map<String, Object> updates, @PathVariable("id") Long id) throws IllegalAccessException {
        return ResponseEntity.ok(this.userDetailsService.updatePatch(updates, id));
    }
}
