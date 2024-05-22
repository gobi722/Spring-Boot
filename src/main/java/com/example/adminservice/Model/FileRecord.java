package com.example.adminservice.Model;

import com.example.adminservice.Service.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_files")
public class FileRecord implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(name = "storage_path", nullable = false)
    private String storage_path;

//    @Column(name = "filesize", nullable = false)
    private long filesize;

//    @Column(name = "folder", nullable = false)
    private String folder;

  //  @Column(name = "file_name", nullable = false)
    private String file_name;

//    @Column(name = "storage_name", nullable = false)
   private String storage_name;

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStoragePath() {
        return storage_path;
    }

    public void setStoragePath(String storagePath) {
        this.storage_path = storage_path;
    }

    public long getFilesize() {
        return filesize;
    }

    public void setFilesize(long filesize) {
        this.filesize = filesize;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getFileName() {
        return file_name;
    }

    public void setFileName(String fileName) {
        this.file_name = file_name;
    }

    public String getStorageName() {
        return storage_name;
    }

    public void setStorageName(String storageName) {
        this.storage_name = storage_name;
    }
}
