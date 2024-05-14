package com.ssafy.chat.controller;

import com.ssafy.chat.dto.request.MessageRequestDto;
import com.ssafy.chat.dto.request.ChatsRequestDto;
import com.ssafy.chat.dto.response.LabelResponseDto;
import com.ssafy.chat.dto.response.MessageResponseDto;
import com.ssafy.chat.dto.response.UserFeignResponseDto;
import com.ssafy.chat.dto.response.UserResponseDto;
import com.ssafy.chat.entity.MongoDBChats;
import com.ssafy.chat.global.client.PieceClient;
import com.ssafy.chat.global.client.UserClient;
import com.ssafy.chat.global.response.code.SuccessCode;
import com.ssafy.chat.global.response.structure.SuccessResponse;
import com.ssafy.chat.service.MongoDBChatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatsController {

    private final SimpMessageSendingOperations template;
    private final MongoDBChatsService mongoDBChatsService;

    private final PieceClient pieceClient;
    private final UserClient userClient;

    @MessageMapping("/chats/{chatRoomId}") // Send Destination Queue
    public void messageSend(@RequestBody MessageRequestDto messageRequestDto
        ) {
        // senderId는 vue.js에서 처리

        UserResponseDto userInfo=userClient.getUser(messageRequestDto.getSenderId()).getData();

        String userTitle=pieceClient.getLabel(userInfo.getLabelId()).getData().getTitle();

        // mongoDB 저장용
        MongoDBChats mongoDBChat = MongoDBChats.builder()
            .chatRoomId(messageRequestDto.getChatRoomId())
            .senderId(messageRequestDto.getSenderId())
            .content(messageRequestDto.getContent())
            .createdAt(messageRequestDto.getCreatedAt())
            .profileImage(userInfo.getProfileImage())
            .nickname(userInfo.getNickname())
            .title(userTitle)
            .build();

        // response 전용
        MessageResponseDto messageResponseDto=MessageResponseDto.builder()
                .chatRoomId(messageRequestDto.getChatRoomId())
                .senderId(messageRequestDto.getSenderId())
                .content(messageRequestDto.getContent())
                .createdAt(messageRequestDto.getCreatedAt())
                .profileImage(userInfo.getProfileImage())
                .nickname(userInfo.getNickname())
                .title(userTitle)
                .build();

        template.convertAndSend("/sub/" + messageRequestDto.getChatRoomId(), messageResponseDto);

        mongoDBChatsService.saveMongoDBChat(mongoDBChat);
    }

    @GetMapping("/chats/list/{chatRoomId}")
    public ResponseEntity messageList(@PathVariable Long chatRoomId) {
        return SuccessResponse.createSuccess(SuccessCode.CHATMESSAGE_LIST_SUCCESS,
            mongoDBChatsService.listMongoDBChat(chatRoomId));
    }

    @PostMapping("/chats/list")
    public ResponseEntity messageListByCount(@RequestBody ChatsRequestDto chatsRequestDto) {
        return SuccessResponse.createSuccess(SuccessCode.CHATMESSAGE_N_LIST_SUCCESS,
            mongoDBChatsService.listMessageByCount(chatsRequestDto));
    }
}
