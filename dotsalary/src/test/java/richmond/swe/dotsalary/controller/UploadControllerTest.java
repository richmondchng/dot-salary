package richmond.swe.dotsalary.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import richmond.swe.dotsalary.service.FileProcessorService;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test "/users".
 * @author richmondchng
 */
@WebMvcTest(controllers = { UploadController.class })
class UploadControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FileProcessorService fileProcessorService;

    /**
     * Test /upload.
     *
     * File uploaded and processed without error.
     *
     * @throws Exception
     */
    @Test
    void givenFile_whenUploadFile_returnSuccess() throws Exception {
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.csv",
                MediaType.TEXT_PLAIN_VALUE,
                "NAME, SALARY\nJohn,3000.0\nJohn 2,3500.0".getBytes()
        );

        mockMvc.perform(multipart("/upload")
                        .file(file).contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(1)));

        verify(fileProcessorService, times(1)).process(file);
    }

    /**
     * Test /upload.
     *
     * File uploaded but failed processing.
     *
     * @throws Exception
     */
    @Test
    void givenFileFailed_whenUploadFile_returnFailed() throws Exception {
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.csv",
                MediaType.TEXT_PLAIN_VALUE,
                "NAME, SALARY\nJohn,3000.0\nJohn 2,3500.0".getBytes()
        );

        // throw exception
        doThrow(new RuntimeException("error")).when(fileProcessorService).process(any());

        mockMvc.perform(multipart("/upload")
                        .file(file).contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.success", is(0)));

        verify(fileProcessorService, times(1)).process(file);
    }
}