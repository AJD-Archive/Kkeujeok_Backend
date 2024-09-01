package shop.kkeujeok.kkeujeokbackend.challenge.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.CycleDetail;
import shop.kkeujeok.kkeujeokbackend.challenge.exception.InvalidCycleDetailsConversionException;

class CycleDetailsConverterTest {

    private CycleDetailsConverter converter;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        converter = new CycleDetailsConverter();
        mapper = new ObjectMapper();
    }

    @Test
    @DisplayName("유효한 리스트를 JSON 문자열로 변환할 수 있다")
    void 유효한_리스트를_JSON_문자열로_변환할_수_있다() throws JsonProcessingException {
        // given
        List<CycleDetail> cycleDetails = Arrays.asList(CycleDetail.MON, CycleDetail.TUE, CycleDetail.WED);

        // when
        String json = converter.convertToDatabaseColumn(cycleDetails);

        // then
        String expectedJson = mapper.writeValueAsString(cycleDetails);
        assertThat(json).isEqualTo(expectedJson);
    }

    @Test
    @DisplayName("유효한 JSON 문자열을 리스트로 변환할 수 있다")
    void 유효한_JSON_문자열을_리스트로_변환할_수_있다() throws JsonProcessingException {
        // given
        List<CycleDetail> expectedList = Arrays.asList(CycleDetail.MON, CycleDetail.TUE, CycleDetail.WED);
        String jsonString = mapper.writeValueAsString(expectedList);

        // when
        List<CycleDetail> result = converter.convertToEntityAttribute(jsonString);

        // then
        assertThat(result).isEqualTo(expectedList);
    }

    @Test
    @DisplayName("유효하지 않은 JSON 문자열을 변환하려 할 때 예외가 발생한다")
    void 유효하지_않은_JSON_문자열을_변환하려_할_때_예외가_발생한다() {
        // given
        String invalidJsonString = "invalid json";

        // when & then
        assertThatThrownBy(() -> converter.convertToEntityAttribute(invalidJsonString))
                .isInstanceOf(InvalidCycleDetailsConversionException.class)
                .hasMessageContaining("JSON 문자열을 List<CycleDetail>로 변환하는 중 오류가 발생했습니다.");
    }
}
