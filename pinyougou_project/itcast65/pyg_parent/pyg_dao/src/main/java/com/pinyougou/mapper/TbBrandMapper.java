package com.pinyougou.mapper;

import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbBrandExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TbBrandMapper {
    int countByExample(TbBrandExample example);  //根据条件返回条数

    int deleteByExample(TbBrandExample example); //根据example删除

    int deleteByPrimaryKey(Long id);   //根据主键删除

    int insert(TbBrand record);

    int insertSelective(TbBrand record);

    List<TbBrand> selectByExample(TbBrandExample example);   //根据条件进行查询

    TbBrand selectByPrimaryKey(Long id);     //根据key返回当前对象

    int updateByExampleSelective(@Param("record") TbBrand record, @Param("example") TbBrandExample example);

    int updateByExample(@Param("record") TbBrand record, @Param("example") TbBrandExample example);

    int updateByPrimaryKeySelective(TbBrand record);

    int updateByPrimaryKey(TbBrand record);  //根据主键更新品牌对象

    List<Map> selectOptionList();
}