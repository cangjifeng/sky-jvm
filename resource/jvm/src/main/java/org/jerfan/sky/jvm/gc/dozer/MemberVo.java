package org.jerfan.sky.jvm.gc.dozer;


import com.github.dozermapper.core.Mapping;

import java.io.Serializable;

/**
 * @author jerfan.cang
 * @date 2019/9/3  14:53
 */
public class MemberVo implements Serializable {


    private static final long serialVersionUID = 5366490760292815746L;
    @Mapping("pk")
    private Long id;
    private String memberCode;

    private String memberName;

    private AddressVo address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AddressVo getAddress() {
        return address;
    }

    public void setAddress(AddressVo address) {
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

    public MemberVo (){}
}
