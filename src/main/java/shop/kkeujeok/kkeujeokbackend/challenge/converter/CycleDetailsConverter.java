package shop.kkeujeok.kkeujeokbackend.challenge.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.List;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.CycleDetail;
import shop.kkeujeok.kkeujeokbackend.challenge.exception.InvalidCycleDetailsConversionException;

@Converter(autoApply = true)
public class CycleDetailsConverter implements AttributeConverter<List<CycleDetail>, String> {

    private final ObjectMapper mapper = new ObjectMapper();
    private final CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class, CycleDetail.class);

    @Override
    public String convertToDatabaseColumn(List<CycleDetail> cycleDetails) {
        try {
            return mapper.writeValueAsString(cycleDetails);
        } catch (JsonProcessingException e) {
            throw new InvalidCycleDetailsConversionException("List<CycleDetail>를 JSON 문자열로 변환하는 중 오류가 발생했습니다.");
        }
    }

    @Override
    public List<CycleDetail> convertToEntityAttribute(String data) {
        try {
            return mapper.readValue(data, listType);
        } catch (JsonProcessingException e) {
            throw new InvalidCycleDetailsConversionException("JSON 문자열을 List<CycleDetail>로 변환하는 중 오류가 발생했습니다.");
        }
    }
}
