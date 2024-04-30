package com.ssafy.piece.domain.labels.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.piece.domain.labels.dto.request.LabelsRequestDto;
import com.ssafy.piece.domain.labels.entity.Labels;
import com.ssafy.piece.domain.labels.repository.LabelsRepository;
import jakarta.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LabelsService {

    private final LabelsRepository labelsRepository;

    // 칭호 등록
    public void addLabels() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File("src/main/resources/labelsdata.json");

        LabelsRequestDto[] labelsRequestDtos = objectMapper.readValue(file,
            LabelsRequestDto[].class);
        for (LabelsRequestDto labelsRequestDto : labelsRequestDtos) {
            Labels labels = Labels.builder()
                .labelType(labelsRequestDto.getLabelType())
                .title(labelsRequestDto.getTitle())
                .description(labelsRequestDto.getDescription())
                .build();
            labelsRepository.save(labels);
        }
    }

    // 칭호 찾기
    public Labels findById(Long labelId) {
        return labelsRepository.findById(labelId)
            .orElseThrow(() -> new RuntimeException("해당 칭호가 없습니다."));
    }

    // 칭호명 찾기
    public String findLabelsTitle(Long labelId) {
        return labelsRepository.findById(labelId).get().getTitle();
    }
}
