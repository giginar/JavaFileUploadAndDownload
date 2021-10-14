package com.kucukcinar.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * The type File.
 */
@Entity
@Table(name="files")
@Getter
@Setter
@NoArgsConstructor
public class File {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    private String fileName;
    private String fileType;

    @Lob
    private byte[] data;

    /**
     * Instantiates a new File.
     *
     * @param fileName the file name
     * @param fileType the file type
     * @param data     the data
     */
    public File(String fileName, String fileType, byte[] data) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
    }
}