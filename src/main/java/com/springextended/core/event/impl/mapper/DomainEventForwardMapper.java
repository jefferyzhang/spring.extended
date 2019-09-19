package com.springextended.core.event.impl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springextended.core.event.impl.DomainEventForwardRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 07 - 16 14:10
 */
@Component
public interface DomainEventForwardMapper extends BaseMapper<DomainEventForwardRecord> {

    List<DomainEventForwardRecord> queryWaitingForwardEvents(@Param("now") LocalDateTime now,@Param("topCount") int topCount,@Param("maxForwardTimes") int maxForwardTimes);
}
