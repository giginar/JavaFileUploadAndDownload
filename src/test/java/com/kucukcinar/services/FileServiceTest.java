package com.kucukcinar.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kucukcinar.entities.File;
import com.kucukcinar.repositories.FileRepository;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

@ContextConfiguration(classes = {FileService.class})
@ExtendWith(SpringExtension.class)
class FileServiceTest {
    @MockBean
    private FileRepository fileRepository;

    @Autowired
    private FileService fileService;

    @Test
    void testSaveFile() throws IOException {
        File file = new File();
        file.setFileName("foo.txt");
        file.setId(1);
        file.setData("AAAAAAAA".getBytes("UTF-8"));
        file.setFileType("File Type");
        when(this.fileRepository.save((File) any())).thenReturn(file);
        this.fileService.saveFile(
                new MockMultipartFile("Name", new ByteArrayInputStream("AAAAAAAAAAAAAAAAAAAAAAAA".getBytes("UTF-8"))));
        verify(this.fileRepository).save((File) any());
        assertTrue(this.fileService.getFiles().isEmpty());
    }

    @Test
    void testSaveFile2() throws IOException {
        when(this.fileRepository.save((File) any())).thenThrow(new RuntimeException("foo"));
        assertThrows(RuntimeException.class, () -> this.fileService.saveFile(
                new MockMultipartFile("Name", new ByteArrayInputStream("AAAAAAAAAAAAAAAAAAAAAAAA".getBytes("UTF-8")))));
        verify(this.fileRepository).save((File) any());
    }

    @Test
    void testSaveFile3() throws IOException {
        File file = new File();
        file.setFileName("foo.txt");
        file.setId(1);
        file.setData("AAAAAAAA".getBytes("UTF-8"));
        file.setFileType("File Type");
        when(this.fileRepository.save((File) any())).thenReturn(file);
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getBytes()).thenReturn("AAAAAAAA".getBytes("UTF-8"));
        when(multipartFile.getContentType()).thenReturn("text/plain");
        when(multipartFile.getOriginalFilename()).thenReturn("foo.txt");
        this.fileService.saveFile(multipartFile);
        verify(this.fileRepository).save((File) any());
        verify(multipartFile).getBytes();
        verify(multipartFile).getContentType();
        verify(multipartFile).getOriginalFilename();
        assertTrue(this.fileService.getFiles().isEmpty());
    }

    @Test
    void testSaveFile4() throws IOException {
        File file = new File();
        file.setFileName("foo.txt");
        file.setId(1);
        file.setData("AAAAAAAA".getBytes("UTF-8"));
        file.setFileType("File Type");
        when(this.fileRepository.save((File) any())).thenReturn(file);
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getBytes()).thenThrow(new RuntimeException("foo"));
        when(multipartFile.getContentType()).thenReturn("text/plain");
        when(multipartFile.getOriginalFilename()).thenReturn("foo.txt");
        assertThrows(RuntimeException.class, () -> this.fileService.saveFile(multipartFile));
        verify(multipartFile).getBytes();
        verify(multipartFile).getContentType();
        verify(multipartFile).getOriginalFilename();
    }

    @Test
    void testGetFileById() throws UnsupportedEncodingException {
        File file = new File();
        file.setFileName("foo.txt");
        file.setId(1);
        file.setData("AAAAAAAA".getBytes("UTF-8"));
        file.setFileType("File Type");
        Optional<File> ofResult = Optional.<File>of(file);
        when(this.fileRepository.findById((Integer) any())).thenReturn(ofResult);
        Optional<File> actualFileById = this.fileService.getFileById(123);
        assertSame(ofResult, actualFileById);
        assertTrue(actualFileById.isPresent());
        verify(this.fileRepository).findById((Integer) any());
        assertTrue(this.fileService.getFiles().isEmpty());
    }

    @Test
    void testGetFileById2() {
        when(this.fileRepository.findById((Integer) any())).thenThrow(new RuntimeException("foo"));
        assertThrows(RuntimeException.class, () -> this.fileService.getFileById(123));
        verify(this.fileRepository).findById((Integer) any());
    }

    @Test
    void testGetFileByName() throws UnsupportedEncodingException {
        File file = new File();
        file.setFileName("foo.txt");
        file.setId(1);
        file.setData("AAAAAAAA".getBytes("UTF-8"));
        file.setFileType("File Type");
        Optional<File> ofResult = Optional.<File>of(file);
        when(this.fileRepository.findByName((String) any())).thenReturn(ofResult);
        Optional<File> actualFileByName = this.fileService.getFileByName("foo.txt");
        assertSame(ofResult, actualFileByName);
        assertTrue(actualFileByName.isPresent());
        verify(this.fileRepository).findByName((String) any());
        assertTrue(this.fileService.getFiles().isEmpty());
    }

    @Test
    void testGetFileByName2() {
        when(this.fileRepository.findByName((String) any())).thenThrow(new RuntimeException("foo"));
        assertThrows(RuntimeException.class, () -> this.fileService.getFileByName("foo.txt"));
        verify(this.fileRepository).findByName((String) any());
    }

    @Test
    void testGetFiles() {
        ArrayList<File> fileList = new ArrayList<File>();
        when(this.fileRepository.findAll()).thenReturn(fileList);
        List<File> actualFiles = this.fileService.getFiles();
        assertSame(fileList, actualFiles);
        assertTrue(actualFiles.isEmpty());
        verify(this.fileRepository).findAll();
    }

    @Test
    void testGetFiles2() {
        when(this.fileRepository.findAll()).thenThrow(new RuntimeException("foo"));
        assertThrows(RuntimeException.class, () -> this.fileService.getFiles());
        verify(this.fileRepository).findAll();
    }

    @Test
    void testControlFileName() {
        assertFalse(this.fileService.controlFileName("foo.txt"));
        assertTrue(this.fileService.controlFileName("png"));
    }

    @Test
    void testDeleteAll() {
        doNothing().when(this.fileRepository).deleteAll();
        this.fileService.deleteAll();
        verify(this.fileRepository).deleteAll();
        assertTrue(this.fileService.getFiles().isEmpty());
    }

    @Test
    void testDeleteAll2() {
        doThrow(new RuntimeException("foo")).when(this.fileRepository).deleteAll();
        assertThrows(RuntimeException.class, () -> this.fileService.deleteAll());
        verify(this.fileRepository).deleteAll();
    }

    @Test
    void testDelete() throws UnsupportedEncodingException {
        File file = new File();
        file.setFileName("foo.txt");
        file.setId(1);
        file.setData("AAAAAAAA".getBytes("UTF-8"));
        file.setFileType("File Type");
        doNothing().when(this.fileRepository).delete((File) any());
        when(this.fileRepository.getById((Integer) any())).thenReturn(file);
        this.fileService.delete(123);
        verify(this.fileRepository).delete((File) any());
        verify(this.fileRepository).getById((Integer) any());
        assertTrue(this.fileService.getFiles().isEmpty());
    }

    @Test
    void testDelete2() throws UnsupportedEncodingException {
        File file = new File();
        file.setFileName("foo.txt");
        file.setId(1);
        file.setData("AAAAAAAA".getBytes("UTF-8"));
        file.setFileType("File Type");
        doThrow(new RuntimeException("foo")).when(this.fileRepository).delete((File) any());
        when(this.fileRepository.getById((Integer) any())).thenReturn(file);
        assertThrows(RuntimeException.class, () -> this.fileService.delete(123));
        verify(this.fileRepository).delete((File) any());
        verify(this.fileRepository).getById((Integer) any());
    }
}

