package com.sstree.streaming.streamingserver.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sstree.streaming.streamingserver.service.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ObjectMapperTest {

    ObjectMapper objectMapper = new ObjectMapper();


//    @Test
//    private void aaa() throws JsonProcessingException {
////        UserDto user1 = new UserDto("aaa","a@a","1111");
//        String json = objectMapper.writeValueAsString(user1);
//        System.out.println(user1);
//        System.out.println("json = " + json);
//    }



}
