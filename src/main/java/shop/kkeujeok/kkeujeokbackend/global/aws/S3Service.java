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
    private static final String bucketName = "kkeujeok-image-bucket";
    private static final String urlImage = "https://" + bucketName + ".s3.amazonaws.com/challenge-images/key.jpg";

    // 파일 업로드 메서드
    public String uploadChallengeImage(MultipartFile file) {
        String key = UUID.randomUUID().toString();
        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .build(),
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );
            return urlImage;
        } catch (IOException e) {
            throw new InvalidImageUploadException();
        }
    }
}

