package com.springextended.core.event.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.springextended.core.event.DomainEvent;
import com.springextended.core.event.DomainEventStorage;
import com.springextended.core.event.forward.DomainEventForward;
import com.springextended.core.event.impl.mapper.DomainEventForwardMapper;
import com.springextended.core.event.impl.mapper.DomainEventMapper;
import com.springextended.core.json.JsonConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * <p>
 *
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 05 - 29 18:16
 */
@Repository
public class DomainEventStorageImpl implements DomainEventStorage {

    @Autowired
    private DomainEventMapper domainEventMapper;
    @Autowired
    private JsonConvert jsonConvert;
    @Autowired
    private DomainEventForwardMapper domainEventForwardMapper;

    @Override
    public void add(DomainEvent domainEvent) {

        domainEvent.setId(UUID.randomUUID().toString());

        DomainEventRecord eventRecord = DomainEventRecord.of(domainEvent
                , domainEvent.isEmptyArgs()?null:jsonConvert.serializeObject(domainEvent.getArgObj()));

        domainEventMapper.insert(eventRecord);

        domainEventForwardMapper.insert(DomainEventForwardRecord.of(domainEvent));
    }

    @Override
    public List<DomainEventForward> queryWaitingForwardEvents(LocalDateTime now, int topCount,int maxForwardTimes) {

        List<DomainEventForwardRecord> forwardRecords = domainEventForwardMapper
                .queryWaitingForwardEvents(now, topCount, maxForwardTimes);

        if(forwardRecords.isEmpty()){
            return new ArrayList<>();
        }

        List<String> eventIds = forwardRecords.stream().map(x -> x.getEventId()).collect(Collectors.toList());
        Map<String, List<DomainEventRecord>> domainEvents = domainEventMapper.selectList(new QueryWrapper<DomainEventRecord>().lambda()
                .in(DomainEventRecord::getId, eventIds))
                .stream().collect(Collectors.groupingBy(DomainEventRecord::getId));

        return forwardRecords.stream().map(x->x.toEntity(domainEvents)).collect(Collectors.toList());
    }

    @Override
    public void save(DomainEvent event) {
        DomainEventRecord eventRecord = DomainEventRecord.of(event
                , event.getArgs());

        domainEventMapper.updateById(eventRecord);
    }

    @Override
    public void deleteForward(DomainEventForward domainEventForward) {
        domainEventForwardMapper.deleteById(domainEventForward.getId());
    }

    @Override
    public void saveForward(DomainEventForward domainEventForward) {
        DomainEventForwardRecord forwardRecord = new DomainEventForwardRecord();
        forwardRecord.copyFrom(domainEventForward);

        domainEventForwardMapper.updateById(forwardRecord);
    }
}
