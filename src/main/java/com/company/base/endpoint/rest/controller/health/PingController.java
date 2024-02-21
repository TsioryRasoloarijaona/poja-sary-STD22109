package com.company.base.endpoint.rest.controller.health;

import com.company.base.PojaGenerated;
import com.company.base.repository.DummyRepository;
import com.company.base.repository.DummyUuidRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@PojaGenerated
@RestController
@AllArgsConstructor
public class PingController {

  DummyRepository dummyRepository;
  DummyUuidRepository dummyUuidRepository;

  public static final ResponseEntity<String> OK = new ResponseEntity<>("OK", HttpStatus.OK);
  public static final ResponseEntity<String> KO =
      new ResponseEntity<>("KO", HttpStatus.INTERNAL_SERVER_ERROR);

  @GetMapping("/ping")
  public String ping() {
    return "pong";
  }

  @PostMapping(value = "/convertToBlackAndWhite", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.IMAGE_JPEG_VALUE)
  public ResponseEntity<byte[]> toBlackAndWhite(@RequestBody MultipartFile img) {
    try {
      BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(img.getBytes()));
      int width = bufferedImage.getWidth();
      int height = bufferedImage.getHeight();

      BufferedImage blackAndWhiteImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);


      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          int rgb = bufferedImage.getRGB(x, y);
          int r = (rgb >> 16) & 0xFF;
          int g = (rgb >> 8) & 0xFF;
          int b = rgb & 0xFF;
          int gray = (r + g + b) / 3;
          int newPixel = (gray << 16) + (gray << 8) + gray;
          blackAndWhiteImage.setRGB(x, y, newPixel);
        }
      }

      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      ImageIO.write(blackAndWhiteImage, "jpg", byteArrayOutputStream);

      byte[] imageData = byteArrayOutputStream.toByteArray();

      return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageData);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
