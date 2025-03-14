package org.ukstagram.domain.music.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.ukstagram.global.exception.ErrorCode;
import org.ukstagram.global.response.Response;

@RestController
@RequestMapping("/music")
public class MusicController {

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Response<String> uploadMusicFile(@RequestParam("file") MultipartFile file,
                                            @RequestParam(name = "artist", required = true) String artist,
                                            @RequestParam(name = "description", required = true) String description,
                                            @RequestParam(name = "genre", required = true) String genre,
                                            @RequestParam(name = "tags", required = true) String tags,
                                            @RequestParam(name = "title", required = true) String title
    ){
        if (file.isEmpty()) {
            return Response.error(ErrorCode.INVALID_INPUT_VALUE);
        }

        return Response.ok("업로드 성공했습니다.");
    }

    @GetMapping
    public String getMusicList(){
        return "main/main";
    }

}
