package be.trakk.jwtdemo.service.impl;

import be.trakk.jwtdemo.config.PasswordEncoderConfig;
import be.trakk.jwtdemo.dto.UserDTO;
import be.trakk.jwtdemo.entity.User;
import be.trakk.jwtdemo.form.UserForm;
import be.trakk.jwtdemo.mapper.WebApiMapper;
import be.trakk.jwtdemo.repository.UserRepository;
import be.trakk.jwtdemo.service.BaseService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService, BaseService<UserDTO, UserForm, Long> {

    private final UserRepository userRepository;

    private final WebApiMapper webApiMapper;

    private final PasswordEncoderConfig passwordEncoder;

    public UserDetailsServiceImpl(UserRepository userRepository, WebApiMapper webApiMapper, PasswordEncoderConfig passwordEncoder) {
        this.userRepository = userRepository;
        this.webApiMapper = webApiMapper;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return this.userRepository
                .findByUsername(s)
                .orElseThrow(() -> new UsernameNotFoundException("L'utilisateur " + s + " n'existe pas !"));
    }

    @Override
    public List<UserDTO> getAll() {
        return this.userRepository.findAll()
                .stream()
                .map(webApiMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getOne(Long id) {
        return this.webApiMapper.toDto(this.userRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur avec l'id " + id + " n'a pas été trouvé")));
    }

    @Override
    public boolean insert(UserForm form) {

        User userToInsert = this.webApiMapper.fromFormToEntity(form);

        userToInsert.setAccountNonExpired(true);
        userToInsert.setAccountNonLocked(true);
        userToInsert.setCredentialsNonExpired(true);
        userToInsert.setEnabled(true);

        userToInsert.setPassword(this.passwordEncoder.getPasswordEncoder().encode(userToInsert.getPassword()));

        userToInsert.setRoles(form.getRoles());

        User uInserted = this.userRepository.save(userToInsert);

        return uInserted.getId() > 0;
    }

    @Override
    public boolean delete(Long id) {

        User userToDelete = this.webApiMapper.toEntity(getOne(id));

        this.userRepository.delete(userToDelete);

        return !this.userRepository.existsById(userToDelete.getId());
    }

    @Override
    public UserDTO update(UserForm form, Long id) {

        User userToUpdate = this.webApiMapper.toEntity(getOne(id));

        userToUpdate.setUsername(form.getUsername());
        userToUpdate.setPassword(this.passwordEncoder.getPasswordEncoder().encode(form.getPassword()));
        userToUpdate.setRoles(form.getRoles());

        return this.webApiMapper.toDto(this.userRepository.save(userToUpdate));
    }

    public UserDTO updatePatch(Map<String, Object> updates, Long id) throws IllegalAccessException {

        User userToUpdate = this.webApiMapper.toEntity(getOne(id));

        Class<?> clazz = userToUpdate.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for(Map.Entry<String, Object> entry : updates.entrySet()) {
            Field field = Arrays.stream(fields)
                    .filter(f -> f.getName().equals(entry.getKey()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Champ de la classe non trouvé"));
            field.setAccessible(true);
            field.set(userToUpdate, entry.getValue());
        }

        User userUpdated = this.userRepository.save(userToUpdate);

        return this.webApiMapper.toDto(userUpdated);
    }
}
