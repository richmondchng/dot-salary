package richmond.swe.dotsalary.service;

import org.springframework.stereotype.Service;
import richmond.swe.dotsalary.service.bean.UserBean;

import java.math.BigDecimal;
import java.util.Collection;

/**
 * Service to manage users.
 * @author richmondchng
 */
@Service
public class UserService {

    public Collection<UserBean> getUsers(final BigDecimal min, final BigDecimal max, final Integer offset, final Integer limit, final String sort) {
        return null;
    }

}
