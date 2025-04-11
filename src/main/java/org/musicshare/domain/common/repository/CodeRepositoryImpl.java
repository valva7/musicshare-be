package org.musicshare.domain.common.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.musicshare.domain.common.dto.res.CommonCodeRes;
import org.musicshare.domain.common.model.entity.QCodeEntity;
import org.musicshare.domain.common.model.entity.QCodeGroupEntity;
import org.springframework.stereotype.Repository;

@Repository
public class CodeRepositoryImpl implements CodeRepository {

    private static final QCodeEntity code = QCodeEntity.codeEntity;
    private static final QCodeGroupEntity codeGroup = QCodeGroupEntity.codeGroupEntity;

    private final JPAQueryFactory queryFactory;

    public CodeRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * 공통코드 조회
     *
     * @param groupCode 공통코드그룹
     * @return 공통코드 리스트
     */
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
