package richmond.swe.dotsalary.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import richmond.swe.dotsalary.service.bean.UserBean;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

/**
 * Service to process uploaded file.
 * @author richmondchng
 */
@Service
@Slf4j
public class FileProcessorService {

    private static final String ERROR_NOT_TWO_HEADERS = "Expect 2 columns, but found {0} column(s)";
    private static final String ERROR_FIRST_HEADER_NOT_NAME = "First column should be NAME";
    private static final String ERROR_SECOND_HEADER_NOT_NAME = "Second column should be SALARY";

    private static final String ERROR_NOT_TWO_FIELD = "{0}. Expect line to contain 2 fields, but found {1} field(s)";
    private static final String ERROR_FIRST_FIELD_INVALID = "{0}. NAME field is blank";
    private static final String ERROR_SECOND_FIELD_INVALID = "{0}. SALARY field is not numeric";

    public Collection<UserBean> process(final MultipartFile file) throws IOException {
        if(!"text/csv".equalsIgnoreCase(file.getContentType())) {
            throw new IllegalArgumentException("Not a CSV file");
        }

        // read file
        final Scanner scanner = new Scanner(file.getInputStream());

        final String[] headers = convertToArray(scanner.nextLine());
        if(headers == null) {
            throw new IllegalArgumentException(MessageFormat.format(ERROR_NOT_TWO_HEADERS, 0));
        }
        if(headers.length != 2) {
            throw new IllegalArgumentException(MessageFormat.format(ERROR_NOT_TWO_HEADERS, headers.length));
        }
        if(!"NAME".equalsIgnoreCase(headers[0])) {
            throw new IllegalArgumentException(ERROR_FIRST_HEADER_NOT_NAME);
        }
        if(!"SALARY".equalsIgnoreCase(headers[1])) {
            throw new IllegalArgumentException(ERROR_SECOND_HEADER_NOT_NAME);
        }
        final Collection<UserBean> results = new ArrayList<>();
        int line = 0;
        while(scanner.hasNext()) {
            final String[] record = convertToArray(scanner.nextLine());
            if(record == null) {
                // has no fields, this should be the end of file
                break;
            }
            line = line + 1;
            results.add(mapToUserBean(line, record));
        }
        return results;
    }

    private String[] convertToArray(final String line) {
        if(!StringUtils.isBlank(line)) {
            return line.split(",");
        }
        return null;
    }

    private UserBean mapToUserBean(final int lineNumber, final String[] record) {
        if(record.length != 2) {
            throw new IllegalArgumentException(MessageFormat.format(ERROR_NOT_TWO_FIELD, lineNumber, record.length));
        }
        final String name = record[0];
        if(StringUtils.isBlank(name)) {
            throw new IllegalArgumentException(MessageFormat.format(ERROR_FIRST_FIELD_INVALID, lineNumber));
        }
        final BigDecimal salary;
        try {
            salary = new BigDecimal((record[1]));
        } catch(NumberFormatException nfe) {
            throw new IllegalArgumentException(MessageFormat.format(ERROR_SECOND_FIELD_INVALID, lineNumber));
        }
        return UserBean.builder()
                .name(name)
                .salary(salary)
                .build();
    }
}
