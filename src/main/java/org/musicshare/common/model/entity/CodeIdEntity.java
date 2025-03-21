package org.musicshare.common.model.entity;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CodeIdEntity implements Serializable {
    private String code;
    @ManyToOne
    @JoinColumn(name = "group_code", referencedColumnName = "group_code") // 외래키 설정
    private CodeGroupEntity groupCode;
}