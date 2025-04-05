package org.musicshare.domain.common.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.musicshare.domain.common.dto.res.CommonCodeRes;
import org.musicshare.domain.common.model.entity.QCodeEntity;
import org.musicshare.domain.common.model.entity.QCodeGroupEntity;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CodeRepositoryImpl implements CodeRepository {

    private final JPAQueryFactory queryFactory;

    private static final QCodeEntity code = QCodeEntity.codeEntity;
    private static final QCodeGroupEntity codeGroup = QCodeGroupEntity.codeGroupEntity;

    public List<CommonCodeRes> findCommonCodeList(String groupCode) {
        return queryFactory
            .select(
                Projections.constructor(
                    CommonCodeRes.class,
                    code.id.code,
                    code.name
                )
            )
            .from(code)
            .where(code.id.groupCode.eq(groupCode))
            .orderBy(code.id.code.asc())
            .fetch();
    }

}
