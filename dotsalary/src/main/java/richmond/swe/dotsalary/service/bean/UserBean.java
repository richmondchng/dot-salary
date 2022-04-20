package richmond.swe.dotsalary.service.bean;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * User service bean.
 * @author richmondchng
 */
@Getter
@Builder
public class UserBean {
    private String name;
    private BigDecimal salary;
}
