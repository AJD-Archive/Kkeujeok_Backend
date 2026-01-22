package shop.kkeujeok.kkeujeokbackend.admin.api.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import shop.kkeujeok.kkeujeokbackend.admin.api.response.NoticeListResDto;
import shop.kkeujeok.kkeujeokbackend.global.template.RspTemplate;

@Tag(name = "NoticeController", description = "공지사항 관련 API")
public interface NoticeControllerDocs {

    @Operation(summary = "공지사항 전체 조회", description = "모든 공지사항을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "공지사항 전체 조회 성공")
    })
    RspTemplate<NoticeListResDto> findAll();
}
