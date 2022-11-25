package com.horkr.spring.learn.transaction;

import com.horkr.spring.learn.dao.PeopleMapper;
import org.apache.commons.lang3.RandomUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 卢亮宏
 */
@RestController
public class Transaction {
    private static final Logger log = LogManager.getLogger(Transaction.class);
    @Resource
    private PeopleMapper mapper;

    @Resource
    PeopleService peopleService;


    /**
     * 测试事务内部捕捉异常是否会回滚 --->不会
     *
     * @return Object
     */
    @GetMapping("/catchErrorInner")
    public Object catchErrorInner() {
        peopleService.updateErrorWithCatch();
        return mapper.selectPeople();
    }


    /**
     * 测试事务外部捕捉异常是否会回滚 ---> 会，但是事务代码不能在当前类中
     *
     * @return Object
     */
    @GetMapping("/catchErrorOut")
    public Object catchErrorOut() {
        try {
            peopleService.updateError();
        } catch (Exception e) {
            log.error("出现错误", e);
        }
        return mapper.selectPeople();
    }

    @Transactional
    public void updateErrorWithCatch() {
        try {
            peopleService.insert(RandomUtils.nextInt());
            int a = 1 / 0;
        } catch (Exception e) {
            log.error("出现错误", e);
        }
    }

    @Transactional
    public void updateError() {
        peopleService.insert(RandomUtils.nextInt());
        int a = 1 / 0;
    }

}
