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


/**
 * The type File service.
 */
@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    /**
     * Save file.
     *
     * @param file the file
     */
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

    /**
     * Gets file by id.
     *
     * @param fileId the file id
     * @return the file by id
     */
    public Optional<File> getFileById(Integer fileId) {
        try {
            return fileRepository.findById(fileId);
        }catch (Exception e){
            throw new RuntimeException("Could not get the file! " + e.getMessage());
        }
    }

    /**
     * Gets file by name.
     *
     * @param fileName the file name
     * @return the file by name
     */
    public Optional<File> getFileByName(String fileName) {
        try {
            return fileRepository.findByName(fileName);
        }catch (Exception e){
            throw new RuntimeException("Could not get the file! " + e.getMessage());
        }
    }


    /**
     * Get files list.
     *
     * @return the list
     */
    public List<File> getFiles(){
        try {
            return fileRepository.findAll();
        }catch (Exception e){
            throw new RuntimeException("Could not get all the files! " + e.getMessage());
        }
    }

    /**
     * Control file name boolean.
     *
     * @param fileName the file name
     * @return the boolean
     */
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

    /**
     * Delete all.
     */
    public void deleteAll() {
        try{
            fileRepository.deleteAll();
        }catch (Exception e){
            throw new RuntimeException("Could not delete the files!");
        }
    }


    /**
     * Delete.
     *
     * @param fileId the file id
     */
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