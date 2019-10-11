package com;

import java.util.List;

public class MR<T,R> implements BaseMR<T,R> {

    private Class<? extends Map> Map;

    @Override
    public List<R> start(List<T> data) {
        return null;
    }
}
