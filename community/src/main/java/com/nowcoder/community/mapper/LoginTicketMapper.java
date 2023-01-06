package com.nowcoder.community.mapper;

import com.nowcoder.community.entity.LoginTicket;
import org.apache.ibatis.annotations.*;

@Mapper
@Deprecated//标记过时  因为登录凭证存到redis中了不需要放到,Mysql数据库了
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
    int updateStatus(String ticket, int status);//根据凭证修改状态
}
