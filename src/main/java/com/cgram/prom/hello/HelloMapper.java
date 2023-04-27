package com.cgram.prom.hello;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HelloMapper {

    String sayHello();

    @Insert("insert into hello(name) values('halo')")
    void addHello();
}
