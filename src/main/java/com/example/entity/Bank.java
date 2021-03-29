package com.example.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @ClassName:Bank
 * @Author:sq
 * @Description:
 * @Date:2021/3/29 14:13
 */

public class Bank {
    /**
     * id
     */
    private Integer id;
    /**
     * 银行号
     */
    private String  bank_no;
    /**
     * 银行中文
     */
    private  String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBank_no() {
        return bank_no;
    }

    public void setBank_no(String bank_no) {
        this.bank_no = bank_no;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
