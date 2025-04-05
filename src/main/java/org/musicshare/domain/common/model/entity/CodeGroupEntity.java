package org.musicshare.domain.common.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.musicshare.global.entity.TimeBaseEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "code_group")
public class CodeGroupEntity extends TimeBaseEntity {

    @Id
    @Column(name = "group_code", nullable = false, length = 10)
    private String groupCode;

    @OneToMany(mappedBy = "groupCode", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CodeEntity> codes = new ArrayList<>();

    @Column(name = "group_name", nullable = false, length = 20)
    private String groupName;

    @Column(nullable = true, length = 255)
    private String description;

}
