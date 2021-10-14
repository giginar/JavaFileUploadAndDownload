package com.kucukcinar.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kucukcinar.entities.File;
import com.kucukcinar.services.FileService;

import java.util.ArrayList;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

@ContextConfiguration(classes = {FileController.class})
@ExtendWith(SpringExtension.class)
class FileControllerTest {
    @Autowired
    private FileController fileController;

    @MockBean
    private FileService fileService;

    @Test
    void testUploadFile() {
        FileController fileController = new FileController();
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getOriginalFilename()).thenThrow(new RuntimeException("foo"));
        assertThrows(RuntimeException.class, () -> fileController.uploadFile(multipartFile));
        verify(multipartFile).getOriginalFilename();
    }

    @Test
    void testDeleteAllFiles() throws Exception {
        doNothing().when(this.fileService).deleteAll();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/file/deleteAll");
        MockMvcBuilders.standaloneSetup(this.fileController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"message\":\"All files are deleted!\"}"));
    }

    @Test
    void testDownloadFileById() {
        assertThrows(RuntimeException.class, () -> (new FileController()).downloadFileById(123));
    }

    @Test
    void testDownloadFileByName() {
        assertThrows(RuntimeException.class, () -> (new FileController()).downloadFileByName("foo.txt"));
    }

    @Test
    void testDeleteAllFiles2() throws Exception {
        doThrow(new RuntimeException("foo")).when(this.fileService).deleteAll();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/file/deleteAll");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.fileController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(417))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"message\":\"Could not delete files!\"}"));
    }

    @Test
    void testDeleteFile() throws Exception {
        File file = new File();
        file.setFileName("foo.txt");
        file.setId(1);
        file.setData("AAAAAAAA".getBytes("UTF-8"));
        file.setFileType("File Type");
        Optional<File> ofResult = Optional.<File>of(file);
        doNothing().when(this.fileService).delete((Integer) any());
        when(this.fileService.getFileById((Integer) any())).thenReturn(ofResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/file/delete/{fileId}", 123);
        MockMvcBuilders.standaloneSetup(this.fileController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"message\":\"File deleted successfully!\"}"));
    }

    @Test
    void testDeleteFile2() throws Exception {
        File file = new File();
        file.setFileName("foo.txt");
        file.setId(1);
        file.setData("AAAAAAAA".getBytes("UTF-8"));
        file.setFileType("File Type");
        Optional<File> ofResult = Optional.<File>of(file);
        doThrow(new RuntimeException("foo")).when(this.fileService).delete((Integer) any());
        when(this.fileService.getFileById((Integer) any())).thenReturn(ofResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/file/delete/{fileId}", 123);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.fileController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(417))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"message\":\"Could not delete the file!\"}"));
    }

    @Test
    void testDownloadFiles() throws Exception {
        when(this.fileService.getFiles()).thenReturn(new ArrayList<File>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/file/downloadFiles");
        MockMvcBuilders.standaloneSetup(this.fileController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void testDownloadFiles2() throws Exception {
        when(this.fileService.getFiles()).thenReturn(new ArrayList<File>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/file/downloadFiles");
        getResult.contentType("Not all who wander are lost");
        MockMvcBuilders.standaloneSetup(this.fileController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }
}

