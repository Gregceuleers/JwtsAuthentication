package be.trakk.jwtdemo.mapper;

import be.trakk.jwtdemo.dto.UserDTO;
import be.trakk.jwtdemo.entity.User;
import be.trakk.jwtdemo.form.UserForm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface WebApiMapper {

    @Mappings({})
    UserDTO toDto(User user);

    @Mappings({
            @Mapping(target = "authorities", ignore = true)
    })
    User toEntity(UserDTO dto);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "accountNonExpired", ignore = true),
            @Mapping(target = "accountNonLocked", ignore = true),
            @Mapping(target = "credentialsNonExpired", ignore = true),
            @Mapping(target = "enabled", ignore = true),
            @Mapping(target = "authorities", ignore = true)
    })
    User fromFormToEntity(UserForm form);

}
