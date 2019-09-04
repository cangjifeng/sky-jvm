package org.jerfan.sky.jvm.gc.annotations;

import com.github.dozermapper.core.Mapper;
import org.jerfan.sky.jvm.gc.dozer.AddressVo;
import org.jerfan.sky.jvm.gc.dozer.MemberBean;
import org.jerfan.sky.jvm.gc.dozer.MemberVo;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author jerfan.cang
 * @date 2019/9/3  9:36
 */
public class ContextLoader {

    public static void main(String[] args) {
        try{
            AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ParentConfig.class);

        /*OrderFactory orderFactory =(CommonOrderFactory)context.getBean("commonOrderFactory");
        String message = orderFactory.createOrder();
        System.out.println(message);
        orderFactory = (PromotionOrderFactory) context.getBean("promotionOrderFactory");
        message = orderFactory.createOrder();
        System.out.println(message);*/

            Mapper mapper = (Mapper)context.getBean("dozerMapper");
            AddressVo addressVo = new AddressVo();
            addressVo.setAddressCode("nj-889");
            addressVo.setAddressName("雨花台");
            MemberVo vo = new MemberVo();
            vo.setId(2L);
            vo.setMemberCode("htd100");
            vo.setMemberName("all");
            vo.setAddress(addressVo);

            List<MemberVo> memberVoList = Arrays.asList(vo,vo,vo);

            //DozerBeanMapper dozer = new DozerBeanMapper();
            MemberBean bean = mapper.map(vo,MemberBean.class);
            List<MemberBean> memberBeanList = new ArrayList<>(); // 如果memberBeanList 为null 会报 MappingException
             mapper.map(memberVoList,memberBeanList);

            System.out.println("end");
        }catch (Exception e){
            System.out.println(e);
        }

    }
}
