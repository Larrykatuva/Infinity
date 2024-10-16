package com.celica.infinity.common.auth.models;

import com.celica.infinity.common.auth.enums.Context;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "codes")
public class Code {
    @Id
    @GeneratedValue
    private Long id;

    private String code;

   @ManyToOne
   @JoinColumn(name="_user", nullable = false)
   private User user;

   private LocalDateTime expiry;

   private boolean used = false;

   @Column(nullable = false)
   @Enumerated(EnumType.STRING)
   private Context context;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
