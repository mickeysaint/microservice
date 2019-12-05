package com.xyz.base.tx;

import com.xyz.base.common.ResultBean;

public interface Tcc {

    public ResultBean<Void> doTry();

    public ResultBean<Void> doConfirm();

    public ResultBean<Void> doCancel();

}
