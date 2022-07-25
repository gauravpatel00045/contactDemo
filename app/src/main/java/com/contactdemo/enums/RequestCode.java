package com.contactdemo.enums;


import com.contactdemo.model.ContactModel;

import java.lang.reflect.Type;



public enum RequestCode {

    UserId(ContactModel[].class);



    private Class<?> localClass = null;
    private Type localType = null;

    RequestCode(Class<?> localClass) {
        this.localClass = localClass;
    }

    RequestCode(Type localType) {
        this.localType = localType;
        this.localClass = localType.getClass();
    }

    protected Class<?> getLocalClass() {
        return localClass;
    }

    public Type getLocalType() {
        return localType;
    }
}
