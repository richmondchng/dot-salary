package richmond.swe.dotsalary.data.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import richmond.swe.dotsalary.data.entity.UserEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test UserRepository.
 * @author richmondchng
 */
@ActiveProfiles("test")
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindAll() {
        final List<UserEntity> results = userRepository.findAll();
        assertEquals(2, results.size());
    }
}