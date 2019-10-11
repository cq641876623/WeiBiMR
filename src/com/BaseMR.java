package com;

import java.util.List;

public interface BaseMR<T,R> {
    List<R> start(List<T> data);

}
