package richmond.swe.dotsalary.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import richmond.swe.dotsalary.controller.dto.UploadDTO;
import richmond.swe.dotsalary.exception.BulkRecordProcessException;
import richmond.swe.dotsalary.exception.FileProcessException;
import richmond.swe.dotsalary.service.FileProcessorService;
import richmond.swe.dotsalary.service.UserService;
import richmond.swe.dotsalary.service.bean.UserBean;

import java.util.Collection;

/**
 * Controller for upload.
 * @author richmondchng
 */
@AllArgsConstructor
@RestController
@RequestMapping("/upload")
@Slf4j
public class UploadController {

    private final FileProcessorService fileProcessorService;
    private final UserService userService;

    /**
     * Upload and process CSV file.
     * @param file csv text file
     * @return success = 1 if success, 0 if failed
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UploadDTO> uploadData(@RequestParam(name = "file") final MultipartFile file)  {
        final Collection<UserBean> records;
        try {
            records = fileProcessorService.process(file);
        } catch (Exception e) {
            log.error("Exception caught {}", e.getMessage(), e);
            throw new FileProcessException(e.getMessage(), e);
        }

        int result = 0;
        if(records != null && records.size() > 0) {
            try {
                result = userService.bulkPersistRecords(records);
            } catch(Exception e) {
                log.error("Exception caught {}", e.getMessage(), e);
                throw new BulkRecordProcessException(e.getMessage(), e);
            }
        }

        return ResponseEntity.ok(UploadDTO.builder().success(result).build());
    }
}
