package com.horkr.spring.learn.sourceCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 卢亮宏
 */
@Component
public class TestBean1 {

    private TestBean testBean;

    @Autowired
    public TestBean1(TestBean testBean) {
        this.testBean = testBean;
    }
}
