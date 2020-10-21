package be.trakk.jwtdemo.form;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserForm {

    String username;

    String password;

    List<String> roles = new ArrayList<>();

}
