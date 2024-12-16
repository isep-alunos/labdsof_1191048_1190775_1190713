package isep.labdsof.backend.domain.models.event;

import isep.labdsof.backend.domain.exceptions.AppCustomExceptions;
import isep.labdsof.backend.domain.exceptions.LabdsofCustomException;
import isep.labdsof.backend.domain.models.ValueObject;
import isep.labdsof.backend.utils.ImageUtils;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;


@EqualsAndHashCode(callSuper = true)
@Embeddable
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class EventMap extends ValueObject {
    public final static List<String> ALLOWED_IMAGE_TYPES = List.of("image/jpeg", "image/png", "image/jpg");

    private String imageName;
    private String imageType;
    @Lob
    private byte[] imageData;

    public EventMap(final MultipartFile mapImage) throws LabdsofCustomException {

        if (!ALLOWED_IMAGE_TYPES.contains(mapImage.getContentType())) {
            throw new LabdsofCustomException(AppCustomExceptions.INVALID_IMAGE_TYPE);
        }
        this.imageName = mapImage.getOriginalFilename();
        this.imageType = mapImage.getContentType();
        try {
            this.imageData = ImageUtils.compressImage(mapImage.getBytes());
        } catch (Exception e) {
            throw new LabdsofCustomException(AppCustomExceptions.ERROR_COMPRESSING_IMAGE);
        }

    }

    public byte[] getImageBytes() throws LabdsofCustomException {
        try {
            return ImageUtils.decompressImage(imageData);
        } catch (DataFormatException | IOException e) {
            throw new LabdsofCustomException(AppCustomExceptions.ERROR_DECOMPRESSING_IMAGE);
        }
    }
}
