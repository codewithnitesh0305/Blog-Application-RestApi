package com.springboot.Controller;

import com.springboot.DTO.PostDto;
import com.springboot.File.FileUpload;
import com.springboot.Model.Post;
import com.springboot.Payload.SuccessResponse;
import com.springboot.Service.BlogDashboardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.hibernate.event.spi.PostDeleteEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/blogDashboard")
@Tag(name = "Blog Dashboard Api",description = "In this endpoint all user (authenticated or un-authenticate) can access")
public class BlogDashboardController {

    @Autowired
    private BlogDashboardService blogDashboardService;

    @Autowired
    private FileUpload fileUpload;

    @GetMapping("/AllPost")
    public ResponseEntity<?> allPost() throws Exception {
        List<PostDto> dto = blogDashboardService.allPost();
        if(dto.isEmpty()){
            SuccessResponse<String> response = new SuccessResponse<>("Failure", "No Post Available", HttpStatus.NOT_FOUND);
            return new ResponseEntity<SuccessResponse<String>>(response, HttpStatus.NOT_FOUND);
        }
        SuccessResponse<List<PostDto>> response = new SuccessResponse<>("Success", "All Post Retrieve Successfully", dto, HttpStatus.OK);
        return new ResponseEntity<SuccessResponse<List<PostDto>>>(response, HttpStatus.OK);
    }

    @GetMapping("/PostById/{id}")
    public ResponseEntity<?> postById(@PathVariable("id") int id) throws Exception {
        PostDto postDto = blogDashboardService.postById(id);
        SuccessResponse<PostDto> response = new SuccessResponse<>("Success", "All Post Retrieve Successfully", postDto, HttpStatus.OK);
        return new ResponseEntity<SuccessResponse<PostDto>>(response, HttpStatus.OK);
    }

    @GetMapping("/SearchPost")
    public ResponseEntity<?> searchPost(@RequestParam("postName") String postName){
        List<PostDto> postDto = blogDashboardService.searchPost(postName);
        SuccessResponse<List<PostDto>> response = new SuccessResponse<>("Success", "All Post Retrieve Successfully", postDto, HttpStatus.OK);
        return new ResponseEntity<SuccessResponse<List<PostDto>>>(response, HttpStatus.OK);
    }

    @GetMapping("/PostByCategory")
    public ResponseEntity<?> postByCategory(@RequestParam("categoryName") String categoryName){
        List<PostDto> postDto = blogDashboardService.postByCategory(categoryName);
        SuccessResponse<List<PostDto>> response = new SuccessResponse<>("Success", "All Post Retrieve Successfully", postDto, HttpStatus.OK);
        return new ResponseEntity<SuccessResponse<List<PostDto>>>(response, HttpStatus.OK);
    }

    @GetMapping("/GetImages")
    public ResponseEntity<?> downloadFile(@RequestParam String file){
        try {
            byte[] downloadFile = fileUpload.downloadFile(file);
            String contentType = fileUpload.getContentType(file);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            //headers.setContentLength(file.length());
            headers.setContentDispositionFormData("attachment", file);
            return ResponseEntity.ok().headers(headers).body(downloadFile);
        }catch (FileNotFoundException e) {
            return new ResponseEntity<>("Field not found...",HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
