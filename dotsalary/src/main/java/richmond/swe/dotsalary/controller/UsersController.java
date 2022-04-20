package richmond.swe.dotsalary.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import richmond.swe.dotsalary.controller.dto.UsersDTO;
import richmond.swe.dotsalary.service.UserService;
import richmond.swe.dotsalary.service.bean.UserBean;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Controller for users.
 * @author richmondchng
 */
@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UsersController {

    private final UserService userService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsersDTO> getUsers() {
        final Collection<UserBean> results = userService.getUsers();
        return ResponseEntity.ok(mapToBeans(results));
    }

    private UsersDTO mapToBeans(final Collection<UserBean> serviceBeans) {
        return UsersDTO.builder()
                .results(serviceBeans.stream()
                        .map(this::mapToBean).collect(Collectors.toList()))
                .build();
    }

    private UsersDTO.UserDTO mapToBean(final UserBean serviceBean) {
        return UsersDTO.UserDTO.builder()
                .name(serviceBean.getName())
                .salary(serviceBean.getSalary())
                .build();
    }
}
