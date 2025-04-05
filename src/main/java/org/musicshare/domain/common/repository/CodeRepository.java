package org.musicshare.domain.common.repository;

import java.util.List;
import org.musicshare.domain.common.dto.res.CommonCodeRes;

public interface CodeRepository {

    List<CommonCodeRes> findCommonCodeList(String groupCode);

}
