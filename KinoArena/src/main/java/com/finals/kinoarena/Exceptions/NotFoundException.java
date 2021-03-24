package com.finals.kinoarena.Exceptions;

import org.aspectj.weaver.ast.Not;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String msg){
        super(msg);
    }
}
