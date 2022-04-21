package richmond.swe.dotsalary.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import richmond.swe.dotsalary.data.entity.UserEntity;

/**
 * User repository.
 * @author richmondchng
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {

}
