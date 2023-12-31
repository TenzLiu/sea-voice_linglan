package com.tiantian.yy.utils.spripaar;

import com.mobsandgeeks.saripaar.annotation.ValidateUsing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 创建日期：2018/5/14 1:42
 *
 * @author howchange
 */
    @ValidateUsing(IsPhoneNumberRule.class)
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)

    public @interface IsPhoneNumber{
    int sequence()      default -1;
    int messageResId()  default -1;
    String message()    default "手机号格式错误";
    }


