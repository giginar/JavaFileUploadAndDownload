package com.kucukcinar.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.kucukcinar.entities.File;
import com.kucukcinar.repositories.FileRepository;
import com.kucukcinar.responses.FileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    public void saveFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        try {
            File file1 = new File(fileName,file.getContentType(),file.getBytes());
            fileRepository.save(file1);
        }
        catch(Exception e) {
            throw new RuntimeException("Could not save the file! " + e.getMessage());
        }
    }

    public Optional<File> getFile(Integer fileId) {
        try {
            return fileRepository.findById(fileId);
        }catch (Exception e){
            throw new RuntimeException("Could not get the file! " + e.getMessage());
        }
    }


    public List<File> getFiles(){
        try {
            return fileRepository.findAll();
        }catch (Exception e){
            throw new RuntimeException("Could not get all the files! " + e.getMessage());
        }
    }


    public List<FileResponse> getResponseFiles(){
        List<FileResponse> fileResponses = new ArrayList<>();
        List<File> files =  fileRepository.findAll();
        for (File file : files) {
            fileResponses.add(new FileResponse(file.getId(),file.getFileName()));
        }
        return fileResponses;
    }

    public boolean controlFileName(String fileName){
        boolean canSave = false;
        String[] suffixOfFile = fileName.split("\\.");
        String[] suffixes = new String[]{"png", "jpeg", "jpg", "docx", "pdf", "xlsx"};
        try{
            for(String s : suffixes){
                if(suffixOfFile[suffixOfFile.length-1].equals(s)){
                    System.out.println(s);
                    canSave = true;
                }
            }
            return canSave;
        }catch (Exception e){
            throw new RuntimeException("Could not save the file! The file type must be \"png\", \"jpeg\", \"jpg\", \"docx\", \"pdf\", \"xlsx\"");
        }
    }

    public void deleteAll() {
        try{
            fileRepository.deleteAll();
        }catch (Exception e){
            throw new RuntimeException("Could not delete the files!");
        }
    }


    public void delete(Integer fileId) {
        try{
            File file = fileRepository.getById(fileId);
            //Resource resource = new UrlResource(file.toUri());
            fileRepository.delete(file);
        }catch (Exception e){
            throw new RuntimeException("Could not delete the file!");
        }
    }
}