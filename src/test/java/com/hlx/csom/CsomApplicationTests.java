package com.hlx.csom;

import com.hlx.csom.service.impl.ScheduleTask;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class CsomApplicationTests {


    @Resource
    private ScheduleTask scheduleTask;
    @Test
    void contextLoads() {


        scheduleTask.redisDataToMysql();
    }

}
