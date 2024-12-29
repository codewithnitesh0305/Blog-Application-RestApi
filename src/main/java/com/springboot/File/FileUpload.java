package com.springboot.File;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileUpload {

	@Value("${file.upload.path}")
	private String uploadProfilePath;
	

	
	public  String uploadFile(MultipartFile file) throws IOException {
		System.out.println("File name : "+ file.getOriginalFilename());
		// TODO Auto-generated method stub
		String fileName = file.getOriginalFilename();
		File saveFile = new File(uploadProfilePath);
		
		//Create a unique name of file name
		String randomString = UUID.randomUUID().toString();
		String removeExtension = FilenameUtils.removeExtension(fileName);
		fileName = removeExtension+"_"+randomString+"."+FilenameUtils.getExtension(fileName);
		
		if(!saveFile.exists()) {
			saveFile.mkdir();
		}
		String storedPath = uploadProfilePath.concat(fileName);
		long upload = Files.copy(file.getInputStream(), Paths.get(storedPath));
		if(upload != 0) {
			return fileName;
		}
		return null;
	}
	
	
	public  String getImageUrlByName(String imageName, String endUrl) throws Exception {
		String baseUrl = "http://localhost:8080/"+endUrl+"/GetImages?file=";
	 	String encodedName = URLEncoder.encode(imageName, StandardCharsets.UTF_8);
		return baseUrl + encodedName;
	}

	public byte[] downloadFile(String file) throws Exception {
		// TODO Auto-generated method stub
		String fullPath = uploadProfilePath.concat(file);
		try {
			InputStream stream = new FileInputStream(fullPath);
			return StreamUtils.copyToByteArray(stream);
			
		}catch(FileNotFoundException e) {
			e.printStackTrace();
			throw e;
		}catch(IOException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public String getContentType(String filename) {
		String extension = FilenameUtils.getExtension(filename);
		switch(extension) {
		case "pdf":
			return "application/pdf";
		case "xlsx":
			return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
		case "txt":
			return "text/plan";
		case "png":
			return "image/png";
		case "jpeg":
			return "image/jpeg";
		case "jpg":
			return "image/jpg";
		default:
			return "application/octet-stream";
				
		}
		
	}
	
}
