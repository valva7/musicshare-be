package org.musicshare.common.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
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
@Table(name = "code")
public class CodeEntity extends TimeBaseEntity {

    @EmbeddedId
    private CodeIdEntity codeId;

    @MapsId("groupCode") // 복합키의 groupCode 부분을 외래키로 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_code", referencedColumnName = "group_code", insertable = false, updatable = false)
    private CodeGroupEntity groupCode; // 외래키

    private String name;

    @Column(nullable = true, length = 255)
    private String description; // 코드 설명

}
