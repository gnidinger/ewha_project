package com.ewha.back.domain.chat.entity;

import com.ewha.back.domain.user.entity.User;

import lombok.*;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "chat_room_id")
	private Long id;
	@Column
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_room_sender"))
	private User sender;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_room_receiver"))
	private User receiver;

	@CreationTimestamp
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	public static ChatRoom create(User sender, User receiver) {
		ChatRoom room = new ChatRoom();
		room.name = UUID.randomUUID().toString();
		room.sender = sender;
		room.receiver = receiver;
		return room;
	}

	public User mate(Long id) {
		if (sender.getId().equals(id))
			return receiver;
		return sender;
	}
}

