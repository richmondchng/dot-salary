package richmond.swe.dotsalary.controller;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import richmond.swe.dotsalary.service.FileProcessorService;
import richmond.swe.dotsalary.service.UserService;
import richmond.swe.dotsalary.service.bean.UserBean;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
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
    @MockBean
    private UserService userService;

    @Captor
    private ArgumentCaptor<Collection<UserBean>> captor;

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

        when(fileProcessorService.process(any())).thenReturn(Collections.singletonList(
                UserBean.builder().name("JOHN").salary(BigDecimal.valueOf(1000)).build()
        ));
        when(userService.bulkPersistRecords(any())).thenReturn(1);

        mockMvc.perform(multipart("/upload")
                        .file(file).contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(1)));

        verify(fileProcessorService, times(1)).process(file);
        verify(userService, times(1)).bulkPersistRecords(captor.capture());

        final Collection<UserBean> captured = captor.getValue();
        assertEquals(1, captured.size());
        final UserBean userBean = captured.iterator().next();
        assertEquals("JOHN", userBean.getName());
        assertTrue(BigDecimal.valueOf(1000).compareTo(userBean.getSalary()) == 0);
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
        when(fileProcessorService.process(any())).thenThrow(new RuntimeException("error"));

        mockMvc.perform(multipart("/upload")
                        .file(file).contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.success", is(0)));

        verify(fileProcessorService, times(1)).process(file);
        verifyNoInteractions(userService);
    }

    /**
     * Test /upload.
     *
     * File uploaded but failed processing beans.
     *
     * @throws Exception
     */
    @Test
    void giveProcessingFailed_whenUploadFile_returnFailed() throws Exception {
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.csv",
                MediaType.TEXT_PLAIN_VALUE,
                "NAME, SALARY\nJohn,3000.0\nJohn 2,3500.0".getBytes()
        );

        // throw exception
        when(fileProcessorService.process(any())).thenReturn(Collections.singletonList(
                UserBean.builder().name("JOHN").salary(BigDecimal.valueOf(1000)).build()
        ));
        when(userService.bulkPersistRecords(any())).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(multipart("/upload")
                        .file(file).contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.success", is(0)));

        verify(fileProcessorService, times(1)).process(file);
        verify(userService, times(1)).bulkPersistRecords(any());
    }
}