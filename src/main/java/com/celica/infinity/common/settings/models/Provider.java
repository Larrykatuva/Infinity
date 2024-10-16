package com.celica.infinity.common.settings.models;

import com.celica.infinity.common.settings.enums.ProviderType;
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
@Table(
        name = "providers",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"name", "country", "type"}
        )
)
public class Provider {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String code;

    @Column()
    private String Logo;

    @Column(nullable = false)
    private boolean isActive = true;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProviderType type;

    @ManyToOne
    @JoinColumn(name = "country", nullable = false)
    private Country country;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
