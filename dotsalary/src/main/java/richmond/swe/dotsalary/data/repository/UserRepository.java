package richmond.swe.dotsalary.data.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import richmond.swe.dotsalary.data.entity.UserEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * User repository.
 * @author richmondchng
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * Find records between minimum and maximum salary.
     * @param min minimum salary (inclusive)
     * @param max maximum salary (inclusive)
     * @param pageable pagination configuration
     * @return list of entity beans
     */
    @Query(value = "from UserEntity t where salary BETWEEN :min AND :max")
    List<UserEntity> findBySalary(@Param("min") final BigDecimal min, @Param("max") final BigDecimal max, final Pageable pageable);

    @Query(value = "from UserEntity t where name IN :names")
    List<UserEntity> findAllByNames(@Param("names") final Set<String> names);
}
