package com.example.karyaflow.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.Map;

@Service
public class FileUploadService {

    private final Cloudinary cloudinary;

    public FileUploadService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadFile(byte[] file)throws IOException{
        Map cloudResult=cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
        return cloudResult.get("secure_url").toString();
    }

}
