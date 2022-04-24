package richmond.swe.dotsalary.service;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import richmond.swe.dotsalary.data.SortField;
import richmond.swe.dotsalary.data.entity.UserEntity;
import richmond.swe.dotsalary.data.repository.OffsetPageRequest;
import richmond.swe.dotsalary.data.repository.UserRepository;
import richmond.swe.dotsalary.service.bean.UserBean;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service to manage users.
 * @author richmondchng
 */
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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

        // build pagination
        final Pageable pageable = OffsetPageRequest.builder()
                .offset(offset)
                .limit(limit)
                .sort(sortField)
                .build();

        final List<UserEntity> results = userRepository.findBySalary(min, max, pageable);
        return results.stream().map(this::mapToBean).collect(Collectors.toList());
    }

    /**
     * Bulk persist records.
     * @param records collection of UserBeans
     * @return 1 if successful, 0 if failure
     */
    public int bulkPersistRecords(final Collection<UserBean> records) {
        final List<String> names = records.stream().map(b -> b.getName().toUpperCase()).collect(Collectors.toList());

        for(UserBean record : records) {

        }
        return 1;
    }

    /**
     * Map to service bean.
     * @param bean entity bean
     * @return service bean
     */
    private UserBean mapToBean(final UserEntity bean) {
        return UserBean.builder()
                .id(bean.getId())
                .name(bean.getName())
                .salary(bean.getSalary())
                .build();
    }

    /**
     * Get sort field enum value.
     * @param sort sort field name
     * @return SortField
     */
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
}
