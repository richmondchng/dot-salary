package richmond.swe.dotsalary.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;

/**
 * Service to process uploaded file.
 * @author richmondchng
 */
@Service
public class FileProcessorService {

    @Transactional
    public void process(final MultipartFile file) throws IOException {
        
    }
}
