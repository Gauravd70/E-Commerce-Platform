package com.gauravd70.ecommerce.dtos;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserRoleMappingId implements Serializable {
    @Column(name = "user_id")
    private long userId;
    @Column(name = "role_id")
    private long roleId;
}
