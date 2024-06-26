package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

public interface AddressBookService {

    /**
     * 条件查询
     *
     * @param addressBook 地址信息实体对象
     * @return 返回地址信息实体对象集合
     */
    List<AddressBook> list(AddressBook addressBook);

    /**
     * 新增地址
     *
     * @param addressBook 地址信息实体对象
     * @return 返回插入后回显的地址ID
     */
    Long save(AddressBook addressBook);

    /**
     * 根据id查询
     *
     * @param id 地址信息ID
     * @return 返回地址信息实体对象
     */
    AddressBook getById(Long id);

    /**
     * 根据id修改地址
     *
     * @param addressBook 地址信息实体对象
     */
    void update(AddressBook addressBook);

    /**
     * 设置默认地址
     *
     * @param addressBook 地址信息实体对象
     */
    void setDefault(AddressBook addressBook);

    /**
     * 根据id删除地址
     *
     * @param id 地址信息ID
     */
    void deleteById(Long id);

}
