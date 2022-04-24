package richmond.swe.dotsalary.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Collection;

/**
 * Response for /users
 * @author richmondchng
 */
@Builder
@Getter
public class UsersDTO {

    @JsonProperty("results")
    private Collection<UserDTO> results;

    @Builder
    @Getter
    public static class UserDTO {
        @JsonProperty("name")
        private String name;
        @JsonProperty("salary")
        private BigDecimal salary;
    }
}
