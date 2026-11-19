package taller.velvetnails.configuracion;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")// Permite todos los endpoints
                .allowedOrigins("https://velvet-nails-iota.vercel.app/") // Reemplaza con tu dominio real de Vercel
                .allowedMethods("GET", "POST", "PUT","PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}