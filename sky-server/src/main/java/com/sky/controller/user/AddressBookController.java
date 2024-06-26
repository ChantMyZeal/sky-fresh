package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/addressBook")
@Tag(name = "C端-地址簿接口")
@Slf4j
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 查询当前登录用户的所有地址信息
     *
     * @return 返回统一响应结果
     */
    @GetMapping("/list")
    @Operation(summary = "查询当前登录用户的所有地址信息")
    public Result<List<AddressBook>> list() {
        log.info("查询当前登录用户的所有地址信息");
        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(BaseContext.getCurrentId());
        List<AddressBook> list = addressBookService.list(addressBook);
        return Result.success(list);
    }

    /**
     * 新增地址
     *
     * @param addressBook 地址信息实体对象
     * @return 返回统一响应结果
     */
    @PostMapping
    @Operation(summary = "新增地址")
    public Result<Long> save(@RequestBody AddressBook addressBook) {
        log.info("新增地址：{}", addressBook);
        return Result.success(addressBookService.save(addressBook));
    }

    /**
     * 根据ID查询地址
     *
     * @param id 地址信息ID
     * @return 返回统一响应结果
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询地址")
    public Result<AddressBook> getById(@PathVariable Long id) {
        log.info("根据ID查询地址，ID：{}", id);
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }

    /**
     * 根据ID修改地址
     *
     * @param addressBook 地址信息实体对象
     * @return 返回统一响应结果
     */
    @PutMapping
    @Operation(summary = "根据ID修改地址")
    public Result<String> update(@RequestBody AddressBook addressBook) {
        log.info("根据ID修改地址：{}", addressBook);
        addressBookService.update(addressBook);
        return Result.success();
    }

    /**
     * 设置默认地址
     *
     * @param addressBook 地址信息实体对象
     * @return 返回统一响应结果
     */
    @PutMapping("/default")
    @Operation(summary = "设置默认地址")
    public Result<String> setDefault(@RequestBody AddressBook addressBook) {
        log.info("设置默认地址，{}", addressBook);
        addressBookService.setDefault(addressBook);
        return Result.success();
    }

    /**
     * 根据ID删除地址
     *
     * @param id 地址信息ID
     * @return 返回统一响应结果
     */
    @DeleteMapping("/")
    @Operation(summary = "根据ID删除地址")
    public Result<String> deleteById(Long id) {
        log.info("根据ID删除地址，ID：{}", id);
        addressBookService.deleteById(id);
        return Result.success();
    }

    /**
     * 查询默认地址
     */
    @GetMapping("/default")
    @Operation(summary = "查询默认地址")
    public Result<AddressBook> getDefault() {
        log.info("查询默认地址");
        AddressBook addressBook = new AddressBook();
        addressBook.setIsDefault(1);
        addressBook.setUserId(BaseContext.getCurrentId());
        List<AddressBook> list = addressBookService.list(addressBook);

        if (list != null && list.size() == 1) {
            return Result.success(list.get(0));
        }

        return Result.error("没有查询到默认地址");
    }

}
