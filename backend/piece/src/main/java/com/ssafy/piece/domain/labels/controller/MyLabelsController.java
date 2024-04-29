package com.ssafy.piece.domain.labels.controller;

import com.ssafy.piece.domain.labels.service.MyLabelsService;
import com.ssafy.piece.global.annotation.AuthenticatedUser;
import com.ssafy.piece.global.response.code.SuccessCode;
import com.ssafy.piece.global.response.structure.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mylabels")
public class MyLabelsController {

    private final MyLabelsService myLabelsService;

    // 칭호 착용
    @PutMapping("/{labelId}")
    public ResponseEntity<Object> myLabelsWear(@AuthenticatedUser Long userId,
        @PathVariable Long labelId) {
        myLabelsService.wearMyLabels(userId, labelId);

        return SuccessResponse.createSuccess(SuccessCode.ADD_MY_LABELS_SUCCESS);
    }

    // 칭호 착용 해제
    @PutMapping("/wearoff")
    public ResponseEntity<Object> myLabelsWearoff(@AuthenticatedUser Long userId) {
        myLabelsService.wearoffMyLabels(userId);

        return SuccessResponse.createSuccess(SuccessCode.DELETE_MY_LABELS_SUCCESS);
    }

    // 칭호 목록
    @GetMapping()
    public ResponseEntity<Object> myLabelsList(@AuthenticatedUser Long userId) {
        return SuccessResponse.createSuccess(SuccessCode.FIND_MY_LABELS_SUCCESS,
            myLabelsService.listMyLabels(userId));
    }

    // 칭호 획득 검사
    @GetMapping("/check")
    public ResponseEntity<Object> myLabelsCheck(@AuthenticatedUser Long userId) {
        myLabelsService.checkMyLabels(userId);

        return SuccessResponse.createSuccess(SuccessCode.CHECK_MY_LABELS_SUCCESS);
    }
}
