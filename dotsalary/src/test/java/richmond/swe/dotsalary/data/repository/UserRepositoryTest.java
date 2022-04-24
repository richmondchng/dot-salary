package richmond.swe.dotsalary.data.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import richmond.swe.dotsalary.data.SortField;
import richmond.swe.dotsalary.data.entity.UserEntity;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test UserRepository.
 * @author richmondchng
 */
@ActiveProfiles("test")
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    /**
     * Test findBySalary.
     *
     * Test find by salary between min and max.
     */
    @Test
    void givenMinMax_whenFindBySalary_returnRecordsBetweenSalaryMinMax() {
        final Pageable pageable = OffsetPageRequest.builder().build();

        final List<UserEntity> results = userRepository.findBySalary(BigDecimal.valueOf(3070), BigDecimal.valueOf(3075),
                pageable);
        assertEquals(3, results.size());

        // sort by name
        final Iterator<UserEntity> iterator = results.stream().sorted(Comparator.comparing(UserEntity::getName)).iterator();

        final UserEntity result1 = iterator.next();
        assertEquals(8, result1.getId());
        assertEquals("Donna", result1.getName());
        assertTrue(BigDecimal.valueOf(3075).compareTo(result1.getSalary()) == 0);

        final UserEntity result2 = iterator.next();
        assertEquals(9, result2.getId());
        assertEquals("Kitty", result2.getName());
        assertTrue(BigDecimal.valueOf(3072).compareTo(result2.getSalary()) == 0);

        final UserEntity result3 = iterator.next();
        assertEquals(6, result3.getId());
        assertEquals("Michael", result3.getName());
        assertTrue(BigDecimal.valueOf(3070).compareTo(result3.getSalary()) == 0);
    }

    /**
     * Test findBySalary.
     *
     * Test records sorted by name ascending.
     */
    @Test
    void givenSortByName_whenFindBySalary_returnRecordsSortedByNameAscending() {
        final Pageable pageable = OffsetPageRequest.builder()
                .sort(SortField.NAME).build();

        final List<UserEntity> results = userRepository.findBySalary(BigDecimal.valueOf(3070), BigDecimal.valueOf(3075),
                pageable);
        assertEquals(3, results.size());

        // get record as returned
        final Iterator<UserEntity> iterator = results.iterator();

        final UserEntity result1 = iterator.next();
        assertEquals(8, result1.getId());
        assertEquals("Donna", result1.getName());
        assertTrue(BigDecimal.valueOf(3075).compareTo(result1.getSalary()) == 0);

        final UserEntity result2 = iterator.next();
        assertEquals(9, result2.getId());
        assertEquals("Kitty", result2.getName());
        assertTrue(BigDecimal.valueOf(3072).compareTo(result2.getSalary()) == 0);

        final UserEntity result3 = iterator.next();
        assertEquals(6, result3.getId());
        assertEquals("Michael", result3.getName());
        assertTrue(BigDecimal.valueOf(3070).compareTo(result3.getSalary()) == 0);
    }

    /**
     * Test findBySalary.
     *
     * Test records sorted by salary ascending.
     */
    @Test
    void givenSortBySalary_whenFindBySalary_returnRecordsSortedBySalaryAscending() {
        final Pageable pageable = OffsetPageRequest.builder()
                .sort(SortField.SALARY).build();

        final List<UserEntity> results = userRepository.findBySalary(BigDecimal.valueOf(3070), BigDecimal.valueOf(3075),
                pageable);
        assertEquals(3, results.size());

        // get record as returned
        final Iterator<UserEntity> iterator = results.iterator();

        final UserEntity result1 = iterator.next();
        assertEquals(6, result1.getId());
        assertEquals("Michael", result1.getName());
        assertTrue(BigDecimal.valueOf(3070).compareTo(result1.getSalary()) == 0);

        final UserEntity result2 = iterator.next();
        assertEquals(9, result2.getId());
        assertEquals("Kitty", result2.getName());
        assertTrue(BigDecimal.valueOf(3072).compareTo(result2.getSalary()) == 0);

        final UserEntity result3 = iterator.next();
        assertEquals(8, result3.getId());
        assertEquals("Donna", result3.getName());
        assertTrue(BigDecimal.valueOf(3075).compareTo(result3.getSalary()) == 0);
    }

    /**
     * Test findBySalary.
     *
     * Test no matching salary.
     */
    @Test
    void givenNoMatchingSalary_whenFindBySalary_returnEmptyList() {
        final Pageable pageable = OffsetPageRequest.builder().build();

        final List<UserEntity> results = userRepository.findBySalary(BigDecimal.valueOf(3081), BigDecimal.valueOf(4000),
                pageable);
        assertEquals(0, results.size());
    }

    /**
     * Test findBySalary.
     *
     * Test get all records.
     */
    @Test
    void givenNoLimit_whenFindBySalary_returnAllRecords() {
        final Pageable pageable = OffsetPageRequest.builder().build();

        final List<UserEntity> results = userRepository.findBySalary(BigDecimal.valueOf(0), BigDecimal.valueOf(9000),
                pageable);
        assertEquals(10, results.size());

        // get record as returned
        final Iterator<UserEntity> iterator = results.iterator();

        final UserEntity result1 = iterator.next();
        assertEquals(1, result1.getId());
        assertEquals("John", result1.getName());
        assertTrue(BigDecimal.valueOf(3010).compareTo(result1.getSalary()) == 0);

        final UserEntity result2 = iterator.next();
        assertEquals(2, result2.getId());
        assertEquals("Ryan", result2.getName());
        assertTrue(BigDecimal.valueOf(3020).compareTo(result2.getSalary()) == 0);

        final UserEntity result3 = iterator.next();
        assertEquals(3, result3.getId());
        assertEquals("Betty", result3.getName());
        assertTrue(BigDecimal.valueOf(3050).compareTo(result3.getSalary()) == 0);

        final UserEntity result4 = iterator.next();
        assertEquals(4, result4.getId());
        assertEquals("Eric", result4.getName());
        assertTrue(BigDecimal.valueOf(3060).compareTo(result4.getSalary()) == 0);

        final UserEntity result5 = iterator.next();
        assertEquals(5, result5.getId());
        assertEquals("Steven", result5.getName());
        assertTrue(BigDecimal.valueOf(3030).compareTo(result5.getSalary()) == 0);

        final UserEntity result6 = iterator.next();
        assertEquals(6, result6.getId());
        assertEquals("Michael", result6.getName());
        assertTrue(BigDecimal.valueOf(3070).compareTo(result6.getSalary()) == 0);

        final UserEntity result7 = iterator.next();
        assertEquals(7, result7.getId());
        assertEquals("Jackie", result7.getName());
        assertTrue(BigDecimal.valueOf(3025).compareTo(result7.getSalary()) == 0);

        final UserEntity result8 = iterator.next();
        assertEquals(8, result8.getId());
        assertEquals("Donna", result8.getName());
        assertTrue(BigDecimal.valueOf(3075).compareTo(result8.getSalary()) == 0);

        final UserEntity result9 = iterator.next();
        assertEquals(9, result9.getId());
        assertEquals("Kitty", result9.getName());
        assertTrue(BigDecimal.valueOf(3072).compareTo(result9.getSalary()) == 0);

        final UserEntity result10 = iterator.next();
        assertEquals(10, result10.getId());
        assertEquals("Luke", result10.getName());
        assertTrue(BigDecimal.valueOf(3080).compareTo(result10.getSalary()) == 0);
    }

    /**
     * Test findBySalary.
     *
     * Test get records up to limit.
     */
    @Test
    void givenLimit_whenFindBySalary_returnAllRecordsUpToLimit() {
        final Pageable pageable = OffsetPageRequest.builder()
                .limit(3)
                .build();

        final List<UserEntity> results = userRepository.findBySalary(BigDecimal.valueOf(0), BigDecimal.valueOf(9000),
                pageable);
        assertEquals(3, results.size());

        // get record as returned
        final Iterator<UserEntity> iterator = results.iterator();

        final UserEntity result1 = iterator.next();
        assertEquals(1, result1.getId());
        assertEquals("John", result1.getName());
        assertTrue(BigDecimal.valueOf(3010).compareTo(result1.getSalary()) == 0);

        final UserEntity result2 = iterator.next();
        assertEquals(2, result2.getId());
        assertEquals("Ryan", result2.getName());
        assertTrue(BigDecimal.valueOf(3020).compareTo(result2.getSalary()) == 0);

        final UserEntity result3 = iterator.next();
        assertEquals(3, result3.getId());
        assertEquals("Betty", result3.getName());
        assertTrue(BigDecimal.valueOf(3050).compareTo(result3.getSalary()) == 0);
    }

    /**
     * Test findBySalary.
     *
     * Test get offset records.
     */
    @Test
    void givenOffset_whenFindBySalary_returnAllRecordsFromOffset() {
        final Pageable pageable = OffsetPageRequest.builder()
                .offset(6)
                .build();

        final List<UserEntity> results = userRepository.findBySalary(BigDecimal.valueOf(0), BigDecimal.valueOf(9000),
                pageable);
        assertEquals(4, results.size());

        // get record as returned
        final Iterator<UserEntity> iterator = results.iterator();

        final UserEntity result1 = iterator.next();
        assertEquals(7, result1.getId());
        assertEquals("Jackie", result1.getName());
        assertTrue(BigDecimal.valueOf(3025).compareTo(result1.getSalary()) == 0);

        final UserEntity result2 = iterator.next();
        assertEquals(8, result2.getId());
        assertEquals("Donna", result2.getName());
        assertTrue(BigDecimal.valueOf(3075).compareTo(result2.getSalary()) == 0);

        final UserEntity result3 = iterator.next();
        assertEquals(9, result3.getId());
        assertEquals("Kitty", result3.getName());
        assertTrue(BigDecimal.valueOf(3072).compareTo(result3.getSalary()) == 0);

        final UserEntity result4 = iterator.next();
        assertEquals(10, result4.getId());
        assertEquals("Luke", result4.getName());
        assertTrue(BigDecimal.valueOf(3080).compareTo(result4.getSalary()) == 0);
    }

    /**
     * Test findBySalary.
     *
     * Test number of sorted records from offset.
     */
    @Test
    void givenOffsetLimitSort_whenFindBySalary_returnFilteredByPageable() {
        final Pageable pageable = OffsetPageRequest.builder()
                .sort(SortField.NAME)
                .offset(2)
                .limit(4)
                .build();

        final List<UserEntity> results = userRepository.findBySalary(BigDecimal.valueOf(0), BigDecimal.valueOf(9000),
                pageable);
        assertEquals(4, results.size());

        // get record as returned
        final Iterator<UserEntity> iterator = results.iterator();

        final UserEntity result1 = iterator.next();
        assertEquals(4, result1.getId());
        assertEquals("Eric", result1.getName());
        assertTrue(BigDecimal.valueOf(3060).compareTo(result1.getSalary()) == 0);

        final UserEntity result2 = iterator.next();
        assertEquals(7, result2.getId());
        assertEquals("Jackie", result2.getName());
        assertTrue(BigDecimal.valueOf(3025).compareTo(result2.getSalary()) == 0);

        final UserEntity result3 = iterator.next();
        assertEquals(1, result3.getId());
        assertEquals("John", result3.getName());
        assertTrue(BigDecimal.valueOf(3010).compareTo(result3.getSalary()) == 0);

        final UserEntity result4 = iterator.next();
        assertEquals(9, result4.getId());
        assertEquals("Kitty", result4.getName());
        assertTrue(BigDecimal.valueOf(3072).compareTo(result4.getSalary()) == 0);
    }

    /**
     * Test findAllByNames.
     *
     * Supply set of names, returned matched records.
     */
    @Test
    void givenListOfNames_whenFindAllByName_returnRecords() {
        final Set<String> names = Set.of("BETTY", "KITTY", "DONNA", "BART", "HOMER");

        final List<UserEntity> results = userRepository.findAllByNames(names);
        assertEquals(3, results.size());

        // sort by name
        final Iterator<UserEntity> iterator = results.stream().sorted(Comparator.comparing(UserEntity::getName)).iterator();

        final UserEntity result1 = iterator.next();
        assertEquals(3, result1.getId());
        assertEquals("Betty", result1.getName());
        assertTrue(BigDecimal.valueOf(3050).compareTo(result1.getSalary()) == 0);

        final UserEntity result2 = iterator.next();
        assertEquals(8, result2.getId());
        assertEquals("Donna", result2.getName());
        assertTrue(BigDecimal.valueOf(3075).compareTo(result2.getSalary()) == 0);

        final UserEntity result3 = iterator.next();
        assertEquals(9, result3.getId());
        assertEquals("Kitty", result3.getName());
        assertTrue(BigDecimal.valueOf(3072).compareTo(result3.getSalary()) == 0);
    }

    /**
     * Test findAllByNames.
     *
     * Supply set of names that are not in data, returned empty list.
     */
    @Test
    void givenListOfNamesNotInData_whenFindAllByName_returnEmptyList() {
        final Set<String> names = Set.of("BART", "HOMER");

        final List<UserEntity> results = userRepository.findAllByNames(names);
        assertEquals(0, results.size());
    }
}