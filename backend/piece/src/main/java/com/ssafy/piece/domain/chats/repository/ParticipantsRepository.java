package com.ssafy.piece.domain.chats.repository;

import com.ssafy.piece.domain.chats.entity.ChatRooms;
import com.ssafy.piece.domain.chats.entity.Participants;
import com.ssafy.piece.domain.chats.entity.ParticipantsId;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ParticipantsRepository extends JpaRepository<Participants, ParticipantsId> {

    Participants findByParticipantsId(ParticipantsId participantsId);

    @Query("SELECT c FROM Participants p " +
        "INNER JOIN ChatRooms c ON p.participantsId.chatroomId = c.chatRoomId " +
        "WHERE p.participantsId.userId = :userId AND c.isPersonal = :isPersonal")
    List<ChatRooms> findIsPersonalChatRoomsByUserId(Long userId, Boolean isPersonal);

    Long countByParticipantsId_ChatroomId(Long chatroomId);

    @Transactional
    void deleteByParticipantsId_ChatroomId(Long chatroomId);
}
