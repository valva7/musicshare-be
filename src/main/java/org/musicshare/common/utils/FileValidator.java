package org.musicshare.common.utils;

import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public class FileValidator {
    private static final List<String> ALLOWED_EXTENSIONS = List.of("mp3", "wav", "flac", "aac", "ogg", "m4a");
    private static final List<String> ALLOWED_MIME_TYPES = List.of("audio/mpeg", "audio/wav", "audio/flac", "audio/aac", "audio/ogg", "audio/mp4");

    private static final Tika tika = new Tika();

    public static boolean isValidFile(MultipartFile file) throws IOException {
        String extension = getFileExtension(file.getOriginalFilename());
        String mimeType = tika.detect(file.getInputStream());

        return ALLOWED_EXTENSIONS.contains(extension.toLowerCase()) && ALLOWED_MIME_TYPES.contains(mimeType);
    }

    private static String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf(".");
        return (lastIndex == -1) ? "" : fileName.substring(lastIndex + 1);
    }
}