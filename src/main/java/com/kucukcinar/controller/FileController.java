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


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

//    @GetMapping("/")
//    public String get(Model model) {
//        List<File> docs = fileService.getFiles();
//        model.addAttribute("docs", docs);
//        return "doc";
//    }

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

    @GetMapping("/downloadFile/{fileId}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Integer fileId){
        File file = fileService.getFile(fileId).get();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment:filename=\""+file.getFileType()+"\"")
                .body(new ByteArrayResource(file.getData()));
    }


    @GetMapping("/downloadFiles")
    public ResponseEntity<List<File>> downloadFiles(){
        List<File> files = fileService.getFiles();
        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

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

    @DeleteMapping("/delete/{fileId}")
    public ResponseEntity<ResponseMessage> deleteFile(@PathVariable Integer fileId){
        String message = "";
        Optional<File> file = fileService.getFile(fileId);
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