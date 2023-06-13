package com.horkr.spring.learn.mysql.transaction;

import com.horkr.spring.learn.mysql.dao.PeopleMapper;
import com.horkr.spring.learn.dto.People;
import com.horkr.util.thread.ThreadUtil;
import com.horkr.util.trace.StackTraceUtil;
import org.apache.commons.lang3.RandomUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Component
public class PeopleService {

    private static final Logger log = LogManager.getLogger(PeopleService.class);
    @Resource
    private PeopleMapper mapper;

    public void insertAsync(int id) {
        Thread thread = ThreadUtil.startByNewThread(() -> {
            mapper.insert(new People(id, "21df", 1));
        });
        ThreadUtil.join(thread);
    }

    public void insert(int id) {
        mapper.insert(new People(id, "21df", 1));
    }


    @Transactional
    public void updateErrorWithCatch() {
        try {
            insert(RandomUtils.nextInt());
            int a = 1 / 0;
        } catch (Exception e) {
            log.error("出现错误",e);
        }
    }

    @Transactional
    public void updateError() {
        insert(RandomUtils.nextInt());
        int a = 1 / 0;
    }


    public List<Map> queryAll(){
        List<String> callChain = StackTraceUtil.getCallChain();
        callChain.forEach(log::info);
        return mapper.selectPeople();
    }


}
