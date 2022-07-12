package com.ruoyi.test.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * test对象 test_user
 * 
 * @author nfca
 * @date 2022-07-12
 */
public class TestUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** user_id */
    private Long userId;

    /** user_name */
    private String userName;

    /** nick_name */
    private String nickName;

    /** phonenumber */
    private Long phonenumber;

    /** Class */
    private String classname;

    /** sex */
    private String sex;

    /** status */
    private String status;

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setUserName(String userName) 
    {
        this.userName = userName;
    }

    public String getUserName() 
    {
        return userName;
    }
    public void setNickName(String nickName) 
    {
        this.nickName = nickName;
    }

    public String getNickName() 
    {
        return nickName;
    }
    public void setPhonenumber(Long phonenumber) 
    {
        this.phonenumber = phonenumber;
    }

    public Long getPhonenumber() 
    {
        return phonenumber;
    }
    public void setclassname(String classname) 
    {
        this.classname = classname;
    }

    public String getclassname() 
    {
        return classname;
    }
    public void setSex(String sex) 
    {
        this.sex = sex;
    }

    public String getSex() 
    {
        return sex;
    }
    public void setstatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userId", getUserId())
            .append("userName", getUserName())
            .append("nickName", getNickName())
            .append("phonenumber", getPhonenumber())
            .append("classname", getclassname())
            .append("sex", getSex())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
