package org.jerfan.sky.jvm.gc.dozer;


import com.github.dozermapper.core.Mapping;

import java.io.Serializable;

/**
 * @author jerfan.cang
 * @date 2019/9/3  14:51
 */
public class MemberBean implements Serializable {

    private static final long serialVersionUID = -2716161572737358513L;

    @Mapping("id")
    private Long pk;

    private String memberCode = "cjf100";
    private String memberName;

    private AddressBean address;





    public MemberBean(){}

    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    public AddressBean getAddress() {
        return address;
    }

    public void setAddress(AddressBean address) {
        this.address = address;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }




}
