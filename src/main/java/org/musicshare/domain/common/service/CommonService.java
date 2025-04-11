package org.musicshare.domain.common.service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.musicshare.domain.common.dto.res.CommonCodeRes;
import org.musicshare.domain.common.repository.CodeRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CommonService {

    private final CodeRepository codeRepository;

    public CommonService(CodeRepository codeRepository) {
        this.codeRepository = codeRepository;
    }

    /**
     * 공통코드 조회
     *
     * @param codeGroup 공통코드그룹
     * @return 공통코드 리스트
     */
    public List<CommonCodeRes> getCommonCodeList(String codeGroup) {
        return codeRepository.findCommonCodeList(codeGroup.toUpperCase());
    }


}
