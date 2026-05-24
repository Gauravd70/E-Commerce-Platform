package com.gauravd70.ecommerce.dtos;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_role_mappings")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleMappingEntity {
    @EmbeddedId
    private UserRoleMappingId id;
}
