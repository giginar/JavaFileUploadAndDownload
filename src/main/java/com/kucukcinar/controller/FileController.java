package com.kucukcinar.controller;

import java.util.List;
import java.util.Optional;

import com.kucukcinar.entities.File;
import com.kucukcinar.responses.ResponseMessage;
import com.kucukcinar.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * The type File controller.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * Upload file response entity.
     *
     * @param file the file
     * @return the response entity
     */
    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        String fileName = file.getOriginalFilename();
        if(fileService.controlFileName(fileName) == true){
            try {
                fileService.saveFile(file);
                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
            }
        }else{
            message = "Could not save the file! The file type must be png, jpeg, jpg, docx, pdf or xlsx.";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    /**
     * Download file by id response entity.
     *
     * @param fileId the file id
     * @return the response entity
     */
    @GetMapping("/downloadFileById/{fileId}")
    public ResponseEntity<ByteArrayResource> downloadFileById(@PathVariable Integer fileId){
        try {
            File file = fileService.getFileById(fileId).get();
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(file.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,"attachment:filename=\""+file.getFileType()+"\"")
                    .body(new ByteArrayResource(file.getData()));
        } catch (Exception e){
            throw new RuntimeException("Could not get the file! Please control the file ID");
        }

    }

    /**
     * Download file by name response entity.
     *
     * @param fileName the file name
     * @return the response entity
     */
    @GetMapping("/downloadFileByName/{fileName}")
    public ResponseEntity<ByteArrayResource> downloadFileByName(@PathVariable String fileName){
        try {
            File file = fileService.getFileByName(fileName).get();
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(file.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,"attachment:filename=\""+file.getFileType()+"\"")
                    .body(new ByteArrayResource(file.getData()));
        } catch (Exception e){
            throw new RuntimeException("Could not get the file! Please control the file name!");
        }

    }


    /**
     * Download files response entity.
     *
     * @return the response entity
     */
    @GetMapping("/downloadFiles")
    public ResponseEntity<List<File>> downloadFiles(){
        List<File> files = fileService.getFiles();
        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

    /**
     * Delete all files response entity.
     *
     * @return the response entity
     */
    @DeleteMapping("/deleteAll")
    public ResponseEntity<ResponseMessage> deleteAllFiles(){
        String message = "";
        try {
            fileService.deleteAll();
            message = "All files are deleted!";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not delete files!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    /**
     * Delete file response entity.
     *
     * @param fileId the file id
     * @return the response entity
     */
    @DeleteMapping("/delete/{fileId}")
    public ResponseEntity<ResponseMessage> deleteFile(@PathVariable Integer fileId){
        String message = "";
        Optional<File> file = fileService.getFileById(fileId);
        try {
            fileService.delete(fileId);
            message = "File deleted successfully!";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not delete the file!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

}