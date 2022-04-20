package richmond.swe.dotsalary.service;

import org.apache.commons.lang3.StringUtils;
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

    /**
     * Get users.
     * @param min min salary
     * @param max max salary
     * @param offset offset to start, lower bound
     * @param limit number of users to get, upper bound
     * @param sort sort by field ascending
     * @return collection of user beans
     */
    public Collection<UserBean> getUsers(final BigDecimal min, final BigDecimal max, final Integer offset,
                                         final Integer limit, final String sort) {

        if(min == null) {
            throw new IllegalArgumentException("Missing mandatory parameter min");
        }
        if(max == null) {
            throw new IllegalArgumentException("Missing mandatory parameter max");
        }
        if(offset == null) {
            throw new IllegalArgumentException("Missing mandatory parameter offset");
        }
        final SortField sortField = getSortField(sort);
        return null;
    }

    private SortField getSortField(final String sort) {
        SortField sortField = SortField.NONE;
        if(StringUtils.isNotEmpty((sort))) {
            try {
                sortField = SortField.valueOf(sort.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid sort parameter");
            }
        }
        return sortField;
    }

    private enum SortField {
        NAME, SALARY, NONE;
    }
}
