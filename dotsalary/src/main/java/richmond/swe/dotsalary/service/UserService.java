package richmond.swe.dotsalary.service;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import richmond.swe.dotsalary.data.SortField;
import richmond.swe.dotsalary.data.entity.UserEntity;
import richmond.swe.dotsalary.data.repository.OffsetPageRequest;
import richmond.swe.dotsalary.data.repository.UserRepository;
import richmond.swe.dotsalary.service.bean.UserBean;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
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
    @Transactional
    public int bulkPersistRecords(final Collection<UserBean> records) {
        // get all names
        final Set<String> names = records.stream().map(b -> b.getName().toUpperCase()).collect(Collectors.toSet());
        // find existing records, put into map where key = name
        final Map<String, UserEntity> existingUserMap = userRepository.findAllByNames(names)
                .stream().collect(Collectors.toMap(k -> k.getName().toUpperCase(), Function.identity()));

        for(UserBean record : records) {
            if(BigDecimal.ZERO.compareTo(record.getSalary()) >= 0) {
                // if less than or equal 0
                continue;
            }

            final String key = record.getName().toUpperCase();
            final UserEntity existingRecord = existingUserMap.get(key);
            if(existingRecord == null) {
                // new record
                final UserEntity newRecord = new UserEntity();
                newRecord.setName(record.getName());
                newRecord.setSalary(record.getSalary());
                // put back into map
                existingUserMap.put(key, newRecord);
                continue;
            }
            // update salary for existing record
            existingRecord.setSalary(record.getSalary());
        }

        userRepository.saveAllAndFlush(existingUserMap.values());
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
