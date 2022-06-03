package com.mongodb.quickstart;

import java.util.concurrent.atomic.AtomicInteger;

public class MpFile {
    private static final AtomicInteger count = new AtomicInteger(0);
    private Integer Id;
    private String FileName;
    private Long Size;
    private Double Length;
    private Boolean IsValid = null;

    public MpFile(){

    }

    public MpFile(String fileName, Long size, Double length, Boolean isValid){
        this.FileName = fileName;
        this.Size = size;
        this.Length = length;
        this.IsValid = isValid;
        this.Id = count.incrementAndGet();
    }

    public Integer getId() {
        return Id;
    }

    public String getFileName() {
        return FileName;
    }

    public Long getSize() {
        return Size;
    }

    public Double getLength() {
        return Length;
    }

    public Boolean getValid() {
        return IsValid;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public void setSize(Long size) {
        Size = size;
    }

    public void setLength(Double length) {
        Length = length;
    }

    public void setValid(Boolean valid) {
        IsValid = valid;
    }
}
