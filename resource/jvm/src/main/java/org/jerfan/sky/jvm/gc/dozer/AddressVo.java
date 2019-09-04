package org.jerfan.sky.jvm.gc.dozer;

import java.io.Serializable;

/**
 * @author jerfan.cang
 * @date 2019/9/3  14:59
 */
public class AddressVo  implements Serializable {


    private static final long serialVersionUID = 224375472717442639L;

    private String addressCode;

    private String addressName;


    public String getAddressCode() {
        return addressCode;
    }

    public void setAddressCode(String addressCode) {
        this.addressCode = addressCode;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public AddressVo(){}
}
