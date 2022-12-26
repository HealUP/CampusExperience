package com.nowcoder.community.mapper;

import com.nowcoder.community.entity.LoginTicket;
import org.apache.ibatis.annotations.*;

@Mapper

public interface LoginTicketMapper {

//    @Insert({
//            "insert into login_ticket(user_id,ticket,status,expired) ",
//            "values(#{userId},#{ticket},#{status},#{expired})"
//    })
//    @Options(userGeneratedKeys = true, keyProperty = "id")
    int insertLoginTicket(LoginTicket loginTicket);//插入凭证

//    @Select({
//          "select id,user_id,ticket,status,expired ",
//          "from login_ticket where ticket=#{ticket}"
//    })
    LoginTicket selectByTicket(String ticket);//根据ticket查找

//    @Update({
//            "<script>",
//            "update login_ticket set status={status} where ticket =#{ticket} ",
//            "<if test=\"ticket!=null\"> ",
//            "and 1=1 ",
//            "</if>",
//            "<script>"
//    })
    int updateStatus(String ticket, int status);//根据状态修改凭证
}
