package shop.kkeujeok.kkeujeokbackend.block.api.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.request.BlockSaveReqDto;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.request.BlockSequenceUpdateReqDto;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.request.BlockUpdateReqDto;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.response.BlockInfoResDto;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.response.BlockListResDto;
import shop.kkeujeok.kkeujeokbackend.global.annotation.CurrentUserEmail;
import shop.kkeujeok.kkeujeokbackend.global.template.RspTemplate;

@Tag(name = "BlockController", description = "블록 관련 API")
public interface BlockControllerDocs {

    @Operation(summary = "블록 생성", description = "새로운 블록을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "블록 생성 성공")
    })
    RspTemplate<BlockInfoResDto> save(@CurrentUserEmail String email,
                                      @RequestBody BlockSaveReqDto blockSaveReqDto);

    @Operation(summary = "블록 수정", description = "기존 블록을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "블록 수정 성공")
    })
    RspTemplate<BlockInfoResDto> update(@CurrentUserEmail String email,
                                        @PathVariable(name = "blockId") Long blockId,
                                        @RequestBody BlockUpdateReqDto blockUpdateReqDto);

    @Operation(summary = "블록 상태 수정", description = "블록의 진행 상태를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "블록 상태 수정 성공")
    })
    RspTemplate<BlockInfoResDto> progressUpdate(@CurrentUserEmail String email,
                                                @PathVariable(name = "blockId") Long blockId,
                                                @RequestParam(name = "progress") String progress,
                                                @RequestBody BlockSequenceUpdateReqDto blockSequenceUpdateReqDto);

    @Operation(summary = "블록 상태별 전체 조회", description = "특정 대시보드의 블록을 상태별로 페이징하여 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "블록 상태별 전체 조회 성공")
    })
    RspTemplate<BlockListResDto> findForBlockByProgress(@CurrentUserEmail String email,
                                                        @RequestParam(name = "dashboardId") Long dashboardId,
                                                        @RequestParam(name = "progress") String progress,
                                                        @RequestParam(name = "page", defaultValue = "0") int page,
                                                        @RequestParam(name = "size", defaultValue = "10") int size);

    @Operation(summary = "블록 삭제/복구", description = "특정 블록을 삭제하거나 복구합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "블록 삭제/복구 성공")
    })
    RspTemplate<Void> delete(@CurrentUserEmail String email,
                             @PathVariable(name = "blockId") Long blockId);

    @Operation(summary = "블록 상세보기", description = "특정 블록의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "블록 상세보기 성공")
    })
    RspTemplate<BlockInfoResDto> findById(@CurrentUserEmail String email,
                                          @PathVariable(name = "blockId") Long blockId);

    @Operation(summary = "블록 순서 변경", description = "블록들의 순서를 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "블록 순서 변경 성공")
    })
    RspTemplate<Void> changeBlocksSequence(@CurrentUserEmail String email,
                                           @RequestBody BlockSequenceUpdateReqDto blockSequenceUpdateReqDto);

    @Operation(summary = "삭제된 블록 조회", description = "삭제된 블록 목록을 페이징하여 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제된 블록 조회 성공")
    })
    RspTemplate<BlockListResDto> findDeletedBlocks(@CurrentUserEmail String email,
                                                   @RequestParam(name = "dashboardId") Long dashboardId,
                                                   @RequestParam(name = "page", defaultValue = "0") int page,
                                                   @RequestParam(name = "size", defaultValue = "10") int size);

    @Operation(summary = "블록 영구 삭제", description = "특정 블록을 영구적으로 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "블록 영구 삭제 성공")
    })
    RspTemplate<Void> deletePermanently(@CurrentUserEmail String email,
                                        @PathVariable(name = "blockId") Long blockId);

    @Operation(summary = "삭제된 블록 영구 삭제(휴지통 비움)", description = "삭제된 모든 블록을 영구적으로 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제된 블록 영구 삭제 성공")
    })
    RspTemplate<Void> deleteAllPermanently(@CurrentUserEmail String email,
                                           @RequestParam(name = "dashboardId") Long dashboardId);
}
