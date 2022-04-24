package richmond.swe.dotsalary.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import richmond.swe.dotsalary.data.entity.UserEntity;
import richmond.swe.dotsalary.data.repository.UserRepository;
import richmond.swe.dotsalary.service.bean.UserBean;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit test UserService.
 * @author richmondchng
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    // test instance
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
    }

    @AfterEach
    void tearDown() {
        userService = null;
    }

    /**
     * Test getUsers.
     *
     * Test mandatory parameter, missing min parameter.
     */
    @Test
    void givenMinParamNull_whenGetUsers_throwException() {
        try {
            userService.getUsers(null, BigDecimal.valueOf(4000), 0, null, "name");
            fail("Expect exception to be thrown");
        } catch(IllegalArgumentException e) {
            verifyNoInteractions(userRepository);
            assertEquals("Missing mandatory parameter min", e.getMessage());
        }
    }

    /**
     * Test getUsers.
     *
     * Test mandatory parameter, missing max parameter.
     */
    @Test
    void givenMaxParamNull_whenGetUsers_throwException() {
        try {
            userService.getUsers(BigDecimal.ZERO, null, 0, null, "name");
            fail("Expect exception to be thrown");
        } catch(IllegalArgumentException e) {
            verifyNoInteractions(userRepository);
            assertEquals("Missing mandatory parameter max", e.getMessage());
        }
    }

    /**
     * Test getUsers.
     *
     * Test mandatory parameter, missing offset parameter.
     */
    @Test
    void givenOffsetParamNull_whenGetUsers_throwException() {
        try {
            userService.getUsers(BigDecimal.ZERO, BigDecimal.valueOf(4000), null, null, "name");
            fail("Expect exception to be thrown");
        } catch(IllegalArgumentException e) {
            verifyNoInteractions(userRepository);
            assertEquals("Missing mandatory parameter offset", e.getMessage());
        }
    }

    /**
     * Test getUsers.
     *
     * Test invalid sort parameter value.
     */
    @Test
    void givenInvalidSortValue_whenGetUsers_throwException() {
        try {
            userService.getUsers(BigDecimal.ZERO, BigDecimal.valueOf(4000), 0, null, "gender");
            fail("Expect exception to be thrown");
        } catch(IllegalArgumentException e) {
            verifyNoInteractions(userRepository);
            assertEquals("Invalid sort parameter", e.getMessage());
        }
    }

    /**
     * Test getUsers.
     *
     * Test no records returned from repository.
     */
    @Test
    void givenNoRecordsFromRepository_whenGetUsers_returnEmptyList() {
        // return empty list
        when(userRepository.findBySalary(any(), any(), any())).thenReturn(Collections.emptyList());

        final Collection<UserBean> results = userService.getUsers(BigDecimal.valueOf(0), BigDecimal.valueOf(4000),
                0, null, "name");

        verify(userRepository, times(1)).findBySalary(eq(BigDecimal.ZERO),
                eq(BigDecimal.valueOf(4000)), any());

        assertEquals(0, results.size());
    }

    /**
     * Test getUsers.
     *
     * Test records returned.
     */
    @Test
    void givenValidParameters_whenGetUsers_returnRecords() {
        // return
        final UserEntity user1 = new UserEntity();
        user1.setId(1L);
        user1.setName("John");
        user1.setSalary(BigDecimal.valueOf(3010));

        final UserEntity user2 = new UserEntity();
        user2.setId(2L);
        user2.setName("Ryan");
        user2.setSalary(BigDecimal.valueOf(3020));

        when(userRepository.findBySalary(any(), any(), any())).thenReturn(Arrays.asList(user1, user2));

        final Collection<UserBean> results = userService.getUsers(BigDecimal.valueOf(0), BigDecimal.valueOf(4000),
                0, null, "name");

        verify(userRepository, times(1)).findBySalary(eq(BigDecimal.ZERO),
                eq(BigDecimal.valueOf(4000)), any());

        assertEquals(2, results.size());

        // get record as returned
        final Iterator<UserBean> iterator = results.iterator();

        final UserBean result1 = iterator.next();
        assertEquals(1, result1.getId());
        assertEquals("John", result1.getName());
        assertEquals(0, BigDecimal.valueOf(3010).compareTo(result1.getSalary()));

        final UserBean result2 = iterator.next();
        assertEquals(2, result2.getId());
        assertEquals("Ryan", result2.getName());
        assertEquals(0, BigDecimal.valueOf(3020).compareTo(result2.getSalary()));
    }

    @Captor
    private ArgumentCaptor<Set<String>> setNamesCaptor;

    @Captor
    private ArgumentCaptor<Collection<UserEntity>> collectionEntitiesCaptor;

    /**
     * Test bulkPersistRecords.
     *
     * New record, add into data.
     */
    @Test
    void givenNewRecords_whenBulkPersistRecords_insertNewRecord() {

        when(userRepository.findAllByNames(any())).thenReturn(Collections.emptyList());

        final Collection<UserBean> records = Collections.singletonList(
                UserBean.builder().name("Jared").salary(BigDecimal.valueOf(1000)).build()
        );

        final int result = userService.bulkPersistRecords(records);
        assertEquals(1, result);

        verify(userRepository, times(1)).findAllByNames(setNamesCaptor.capture());
        final Set<String> capturedNames = setNamesCaptor.getValue();
        assertEquals(1, capturedNames.size());
        assertTrue(capturedNames.contains("JARED"));

        verify(userRepository, times(1)).saveAllAndFlush(collectionEntitiesCaptor.capture());
        final Collection<UserEntity> capturedEntities = collectionEntitiesCaptor.getValue();
        assertEquals(1, capturedEntities.size());

        final UserEntity result1 = capturedEntities.iterator().next();
        assertEquals("Jared", result1.getName());
        assertEquals(0, BigDecimal.valueOf(1000).compareTo(result1.getSalary()));
        // new record
        assertNull(result1.getId());
    }

    /**
     * Test bulkPersistRecords.
     *
     * Duplicate new record with different salaries, add latest into data.
     */
    @Test
    void givenDuplicateNewRecords_whenBulkPersistRecords_insertNewRecord() {

        when(userRepository.findAllByNames(any())).thenReturn(Collections.emptyList());

        final Collection<UserBean> records = Arrays.asList(
                UserBean.builder().name("Jared").salary(BigDecimal.valueOf(1000)).build(),
                UserBean.builder().name("Jared").salary(BigDecimal.valueOf(1000.50)).build()
        );

        final int result = userService.bulkPersistRecords(records);
        assertEquals(1, result);

        verify(userRepository, times(1)).findAllByNames(setNamesCaptor.capture());
        final Set<String> capturedNames = setNamesCaptor.getValue();
        assertEquals(1, capturedNames.size());
        assertTrue(capturedNames.contains("JARED"));

        verify(userRepository, times(1)).saveAllAndFlush(collectionEntitiesCaptor.capture());
        final Collection<UserEntity> capturedEntities = collectionEntitiesCaptor.getValue();
        assertEquals(1, capturedEntities.size());

        final UserEntity result1 = capturedEntities.iterator().next();
        assertEquals("Jared", result1.getName());
        assertEquals(0, BigDecimal.valueOf(1000.50).compareTo(result1.getSalary()));
        // new record
        assertNull(result1.getId());
    }

    /**
     * Test bulkPersistRecords.
     *
     * Record exists, update record.
     */
    @Test
    void givenRecordExists_whenBulkPersistRecords_updateExistingRecord() {

        final UserEntity existingRecord1 = new UserEntity();
        existingRecord1.setId(100L);
        existingRecord1.setName("jared");
        existingRecord1.setSalary(BigDecimal.valueOf(1000));
        when(userRepository.findAllByNames(any())).thenReturn(Collections.singletonList(
                existingRecord1
        ));

        final Collection<UserBean> records = List.of(
                UserBean.builder().name("Jared").salary(BigDecimal.valueOf(1500)).build()
        );

        final int result = userService.bulkPersistRecords(records);
        assertEquals(1, result);

        verify(userRepository, times(1)).findAllByNames(setNamesCaptor.capture());
        final Set<String> capturedNames = setNamesCaptor.getValue();
        assertEquals(1, capturedNames.size());
        assertTrue(capturedNames.contains("JARED"));

        verify(userRepository, times(1)).saveAllAndFlush(collectionEntitiesCaptor.capture());
        final Collection<UserEntity> capturedEntities = collectionEntitiesCaptor.getValue();
        assertEquals(1, capturedEntities.size());

        final UserEntity result1 = capturedEntities.iterator().next();
        assertEquals(100L, result1.getId());
        assertEquals("jared", result1.getName());
        assertEquals(0, BigDecimal.valueOf(1500).compareTo(result1.getSalary()));
    }

    /**
     * Test bulkPersistRecords.
     *
     * Salary is zero, or less than zero, ignore record.
     */
    @Test
    void givenZeroOrNegativeSalary_whenBulkPersistRecords_ignoreRecords() {

        when(userRepository.findAllByNames(any())).thenReturn(Collections.emptyList());

        final Collection<UserBean> records = Arrays.asList(
                UserBean.builder().name("Jared").salary(BigDecimal.valueOf(0)).build(),
                UserBean.builder().name("Bobby").salary(BigDecimal.valueOf(-5)).build()
        );

        final int result = userService.bulkPersistRecords(records);
        assertEquals(1, result);

        verify(userRepository, times(1)).findAllByNames(setNamesCaptor.capture());
        final Set<String> capturedNames = setNamesCaptor.getValue();
        assertEquals(2, capturedNames.size());
        assertTrue(capturedNames.contains("JARED"));
        assertTrue(capturedNames.contains("BOBBY"));

        verify(userRepository, times(1)).saveAllAndFlush(collectionEntitiesCaptor.capture());
        final Collection<UserEntity> capturedEntities = collectionEntitiesCaptor.getValue();
        assertEquals(0, capturedEntities.size());
    }

    /**
     * Test bulkPersistRecords.
     *
     * Salary is zero, or less than zero, ignore update.
     */
    @Test
    void givenZeroOrNegativeSalaryWithExistingRecord_whenBulkPersistRecords_ignoreUpdate() {

        final UserEntity existingRecord1 = new UserEntity();
        existingRecord1.setId(100L);
        existingRecord1.setName("jared");
        existingRecord1.setSalary(BigDecimal.valueOf(1000));
        final UserEntity existingRecord2 = new UserEntity();
        existingRecord2.setId(101L);
        existingRecord2.setName("bobby");
        existingRecord2.setSalary(BigDecimal.valueOf(2000));
        when(userRepository.findAllByNames(any())).thenReturn(Arrays.asList(
                existingRecord1, existingRecord2
        ));

        final Collection<UserBean> records = Arrays.asList(
                UserBean.builder().name("Jared").salary(BigDecimal.valueOf(0)).build(),
                UserBean.builder().name("Bobby").salary(BigDecimal.valueOf(-5)).build()
        );

        final int result = userService.bulkPersistRecords(records);
        assertEquals(1, result);

        verify(userRepository, times(1)).findAllByNames(setNamesCaptor.capture());
        final Set<String> capturedNames = setNamesCaptor.getValue();
        assertEquals(2, capturedNames.size());
        assertTrue(capturedNames.contains("JARED"));
        assertTrue(capturedNames.contains("BOBBY"));

        verify(userRepository, times(1)).saveAllAndFlush(collectionEntitiesCaptor.capture());
        final Collection<UserEntity> capturedEntities = collectionEntitiesCaptor.getValue();
        assertEquals(2, capturedEntities.size());

        final Iterator<UserEntity> iterator = capturedEntities.stream()
                .sorted(Comparator.comparing(UserEntity::getName)).iterator();

        final UserEntity result1 = iterator.next();
        assertEquals(101L, result1.getId());
        assertEquals("bobby", result1.getName());
        assertEquals(0, BigDecimal.valueOf(2000).compareTo(result1.getSalary()));

        final UserEntity result2 = iterator.next();
        assertEquals(100L, result2.getId());
        assertEquals("jared", result2.getName());
        assertEquals(0, BigDecimal.valueOf(1000).compareTo(result2.getSalary()));
    }
}