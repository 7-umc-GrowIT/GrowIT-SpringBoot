package umc.GrowIT.Server.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import umc.GrowIT.Server.service.cryptoService.CryptoService;

@Converter
public class DiaryContentConverter implements AttributeConverter<String, String> {

    private static CryptoService cryptoService;

    // 스프링 빈 주입
    @Component
    static class Injector {
        @Autowired
        public Injector(CryptoService c) {
            DiaryContentConverter.cryptoService = c;
        }
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) return null;
        return cryptoService.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return cryptoService.decrypt(dbData);
    }
}
