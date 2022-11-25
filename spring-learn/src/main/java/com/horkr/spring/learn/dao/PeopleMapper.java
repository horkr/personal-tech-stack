package com.horkr.spring.learn.dao;

import com.horkr.spring.learn.dto.People;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface PeopleMapper {
    int insert(@Param("people") People people);


    @Select("select * from people")
    List<Map> selectPeople();

    @Select("{call CUSTOM_FUNC(#{id})}")
    List<Map> customFunc(@Param("id") int id);


    @Select("SELECT TRX_ID FROM INFORMATION_SCHEMA.INNODB_TRX WHERE TRX_MYSQL_THREAD_ID = CONNECTION_ID()")
    String queryCurrentTransactionId();

    @Select("select DISTINCT(tenant_id) from t_support_code_pattern")
    List<String> dictinctTenaentId();

    @Select("select * from t_support_code_pattern where tenant_id=#{tenantId} ")
    List<Map<String,Object>> selectPartTypePattern(@Param("tenantId") String tenantId);

    @Select("select * from t_code_sequence where tenant_id=#{tenantId}")
    List<Map<String,Object>> selectCodeSequence(@Param("tenantId") String tenantId);

    @Select("select * from t_support_code_pattern where tenant_id=#{tenantId} and ext_kv=#{extKv}")
    List<Map<String,Object>> selectPartternByExtKv(@Param("tenantId") String tenantId, @Param("extKv") String extKv);
}
