package com.celica.infinity.common.authorization.models;

import com.celica.infinity.common.auth.models.User;
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
        name = "role_permissions",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"role", "permission"}
        )
)
public class RolePermission {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "role", nullable = false)
    private Role role;

    @OneToOne
    @JoinColumn(name = "permission")
    private Permission permission;

    @Column(nullable = false)
    private Boolean active = true;

    @ManyToOne
    @JoinColumn(name = "addedBy")
    private User addedBy;

    @ManyToOne
    @JoinColumn(name = "revokedBy")
    private User revokedBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
