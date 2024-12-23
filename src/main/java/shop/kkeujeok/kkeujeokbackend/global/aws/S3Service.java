package shop.kkeujeok.kkeujeokbackend.global.aws;

import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import shop.kkeujeok.kkeujeokbackend.global.aws.exception.InvalidImageUploadException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    private static final String BUCKET_NAME = "kkeujeok-s3-images-bucket";
    private static final String IMAGE_PATH = "challenge-images/";
    private static final String S3_URL_FORMAT = "https://%s.s3.amazonaws.com/%s";
    private static final String IMAGE_EXTENSION = ".jpg";


    public String uploadChallengeImage(MultipartFile file) {
        String key = IMAGE_PATH + UUID.randomUUID() + IMAGE_EXTENSION;
        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(BUCKET_NAME)
                            .key(key)
                            .build(),
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );
            return String.format(S3_URL_FORMAT, BUCKET_NAME, key);
        } catch (IOException e) {
            throw new InvalidImageUploadException();
        }
    }
}
