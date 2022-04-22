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
import richmond.swe.dotsalary.exception.FileProcessException;
import richmond.swe.dotsalary.service.FileProcessorService;

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

    /**
     * Upload and process CSV file.
     * @param file csv text file
     * @return success = 1 if success, 0 if failed
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UploadDTO> uploadData(@RequestParam(name = "file") final MultipartFile file)  {
        try {
            fileProcessorService.process(file);
        } catch (Exception e) {
            log.error("Exception caught {}", e.getMessage(), e);
            throw new FileProcessException(e.getMessage(), e);
        }
        return ResponseEntity.ok(UploadDTO.builder().success(1).build());
    }
}
