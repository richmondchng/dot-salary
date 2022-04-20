package richmond.swe.dotsalary.controller;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import richmond.swe.dotsalary.controller.dto.UsersDTO;
import richmond.swe.dotsalary.service.UserService;
import richmond.swe.dotsalary.service.bean.UserBean;

import java.math.BigDecimal;
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

    /**
     * Controller to get users.
     * @param min min salary
     * @param max max salary
     * @param offset offset to start, lower bound
     * @param limit number of users to get, upper bound
     * @param sort sort by field ascending
     * @return list of users
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsersDTO> getUsers(@RequestParam(name = "min", required = false, defaultValue = "0") final BigDecimal min,
                                             @RequestParam(name = "max", required = false, defaultValue = "4000") final BigDecimal max,
                                             @RequestParam(name = "offset", required = false, defaultValue = "0") final Integer offset,
                                             @RequestParam(name = "limit", required = false) final Integer limit,
                                             @RequestParam(name = "sort", required = false) final String sort) {
        final Collection<UserBean> results = userService.getUsers(min, max, offset, limit,
                StringUtils.isEmpty(sort) ? null : sort.toUpperCase());
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
