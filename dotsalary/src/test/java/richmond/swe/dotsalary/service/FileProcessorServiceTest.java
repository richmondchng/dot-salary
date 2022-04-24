package richmond.swe.dotsalary.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import richmond.swe.dotsalary.service.bean.UserBean;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Unit test FileProcessorService.
 * @author richmondchng
 */
@ExtendWith({MockitoExtension.class})
class FileProcessorServiceTest {

    // test instance
    private FileProcessorService fileProcessorService;

    @BeforeEach
    void setUp() {
        fileProcessorService = new FileProcessorService();
    }

    @AfterEach
    void tearDown() {
        fileProcessorService = null;
    }

    private MultipartFile toMultipartFile(final File file, final String contentType) throws IOException {
        final Path path = Path.of(file.toURI());
        return new MockMultipartFile(
                "file",
                file.getName(),
                contentType,
                Files.readAllBytes(path));
    }

    /**
     * Test process.
     *
     * Non text file provided, throw exception
     *
     * @throws IOException exception
     */
    @Test
    void givenNonTextFile_whenProcess_throwsException() throws IOException {
        final MultipartFile file = toMultipartFile(ResourceUtils.getFile("classpath:data/file-image.png"), "image/png");

        try {
            fileProcessorService.process(file);
            fail("Expect IllegalArgumentException to be thrown");
        } catch(IllegalArgumentException e) {
            assertEquals("Not a CSV file", e.getMessage());
        }
    }

    /**
     * Test process.
     *
     * More than 2 headers, throw exception
     *
     * @throws IOException exception
     */
    @Test
    void givenMoreThan2Headers_whenProcess_throwsException() throws IOException {
        final MultipartFile file = toMultipartFile(ResourceUtils.getFile("classpath:data/data-file-three-headers.csv"), "text/csv");

        try {
            fileProcessorService.process(file);
            fail("Expect IllegalArgumentException to be thrown");
        } catch(IllegalArgumentException e) {
            assertEquals("Expect 2 columns, but found 3 column(s)", e.getMessage());
        }
    }

    /**
     * Test process.
     *
     * Less than 2 headers, throw exception
     *
     * @throws IOException exception
     */
    @Test
    void givenLessThan2Headers_whenProcess_throwsException() throws IOException {
        final MultipartFile file = toMultipartFile(ResourceUtils.getFile("classpath:data/data-file-one-header.csv"), "text/csv");

        try {
            fileProcessorService.process(file);
            fail("Expect IllegalArgumentException to be thrown");
        } catch(IllegalArgumentException e) {
            assertEquals("Expect 2 columns, but found 1 column(s)", e.getMessage());
        }
    }

    /**
     * Test process.
     *
     * First column header is incorrect, throw exception
     *
     * @throws IOException exception
     */
    @Test
    void givenWrongFirstColumn_whenProcess_throwsException() throws IOException {
        final MultipartFile file = toMultipartFile(ResourceUtils.getFile("classpath:data/data-file-invalid-column-1.csv"), "text/csv");

        try {
            fileProcessorService.process(file);
            fail("Expect IllegalArgumentException to be thrown");
        } catch(IllegalArgumentException e) {
            assertEquals("First column should be NAME", e.getMessage());
        }
    }

    /**
     * Test process.
     *
     * Second column header is incorrect, throw exception
     *
     * @throws IOException exception
     */
    @Test
    void givenWrongSecondColumn_whenProcess_throwsException() throws IOException {
        final MultipartFile file = toMultipartFile(ResourceUtils.getFile("classpath:data/data-file-invalid-column-2.csv"), "text/csv");

        try {
            fileProcessorService.process(file);
            fail("Expect IllegalArgumentException to be thrown");
        } catch(IllegalArgumentException e) {
            assertEquals("Second column should be SALARY", e.getMessage());
        }
    }

    /**
     * Test process.
     *
     * Line with one field, throw exception
     *
     * @throws IOException exception
     */
    @Test
    void giveLineWithOneField_whenProcess_throwsException() throws IOException {
        final MultipartFile file = toMultipartFile(ResourceUtils.getFile("classpath:data/data-file-line-one-field.csv"), "text/csv");

        try {
            fileProcessorService.process(file);
            fail("Expect IllegalArgumentException to be thrown");
        } catch(IllegalArgumentException e) {
            assertEquals("1. Expect line to contain 2 fields, but found 1 field(s)", e.getMessage());
        }
    }

    /**
     * Test process.
     *
     * Line with three field, throw exception
     *
     * @throws IOException exception
     */
    @Test
    void giveLineWithThreeField_whenProcess_throwsException() throws IOException {
        final MultipartFile file = toMultipartFile(ResourceUtils.getFile("classpath:data/data-file-line-three-field.csv"), "text/csv");

        try {
            fileProcessorService.process(file);
            fail("Expect IllegalArgumentException to be thrown");
        } catch(IllegalArgumentException e) {
            assertEquals("1. Expect line to contain 2 fields, but found 3 field(s)", e.getMessage());
        }
    }

    /**
     * Test process.
     *
     * Line with empty field 2, throw exception
     *
     * @throws IOException exception
     */
    @Test
    void giveLineNameEmpty_whenProcess_throwsException() throws IOException {
        final MultipartFile file = toMultipartFile(ResourceUtils.getFile("classpath:data/data-file-line-empty-name.csv"), "text/csv");

        try {
            fileProcessorService.process(file);
            fail("Expect IllegalArgumentException to be thrown");
        } catch(IllegalArgumentException e) {
            assertEquals("1. NAME field is blank", e.getMessage());
        }
    }

    /**
     * Test process.
     *
     * Line with non-numeric field 2, throw exception
     *
     * @throws IOException exception
     */
    @Test
    void giveLineNotNumericFieldValue_whenProcess_throwsException() throws IOException {
        final MultipartFile file = toMultipartFile(ResourceUtils.getFile("classpath:data/data-file-not-numeric-salary.csv"), "text/csv");

        try {
            fileProcessorService.process(file);
            fail("Expect IllegalArgumentException to be thrown");
        } catch(IllegalArgumentException e) {
            assertEquals("1. SALARY field is not numeric", e.getMessage());
        }
    }

    /**
     * Test process.
     *
     * CSV file in correct format, return list of beans.
     *
     * @throws IOException exception
     */
    @Test
    void givenCsvFileCorrectFormat_whenProcess_returnRecords() throws IOException {
        final MultipartFile file = toMultipartFile(ResourceUtils.getFile("classpath:data/data-file-success.csv"), "text/csv");

        final Collection<UserBean> results = fileProcessorService.process(file);

        assertEquals(2, results.size());

        final Iterator<UserBean> iterator = results.iterator();

        final UserBean result1 = iterator.next();
        assertEquals("JOHN", result1.getName());
        assertEquals(0, BigDecimal.valueOf(3000.80).compareTo(result1.getSalary()));

        final UserBean result2 = iterator.next();
        assertEquals("BRUCE", result2.getName());
        assertEquals(0, BigDecimal.valueOf(5000).compareTo(result2.getSalary()));
    }
}