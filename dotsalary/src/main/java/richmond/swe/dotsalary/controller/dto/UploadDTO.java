package richmond.swe.dotsalary.controller.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * Response for /upload
 * @author richmondchng
 */
@Builder
@Getter
public class UploadDTO {
    private int success = 1;
}
