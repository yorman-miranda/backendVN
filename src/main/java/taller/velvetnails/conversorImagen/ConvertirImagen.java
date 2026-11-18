package taller.velvetnails.conversorImagen;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Converter(autoApply = true)
@Component
public class ConvertirImagen implements AttributeConverter<BufferedImage, byte[]> {

    @Override
    public byte[] convertToDatabaseColumn(BufferedImage image){
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()){
            ImageIO.write(image, "png",baos);
            return baos.toByteArray();
        }catch (IOException e){
            throw new RuntimeException("Error al convertir imagen",e);
        }
    }

    @Override
    public BufferedImage convertToEntityAttribute(byte[] bytes){
        if(bytes == null) return null;
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes)){
            return ImageIO.read(bais);
        }catch (IOException e){
            throw new RuntimeException("Error al leer Imagen", e);
        }
    }
}
