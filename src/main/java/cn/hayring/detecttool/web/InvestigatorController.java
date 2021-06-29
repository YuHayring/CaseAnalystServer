package cn.hayring.detecttool.web;

import cn.hayring.detecttool.domain.Investigator;
import cn.hayring.detecttool.service.InvestigatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.text.SimpleDateFormat;

/**
 * 侦查员账户相关控制器
 */
@RestController
@RequestMapping("/investigator")
public class InvestigatorController {



    public static long ONE_DAY = 86400000;

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

//    private JwtUtil jwtUtil;

    /**
     * 侦查员账户相关服务
     */
    InvestigatorService investigatorService;

//    /**
//     * 登出
//     *
//     * @param session 会话
//     * @param id
//     * @return 403 没有权限使他人登出
//     */
//    @RequestMapping(value = "/{id}/session", method = RequestMethod.DELETE)
//    public ResponseEntity logout(HttpSession session, @PathVariable String id) {
//        //获取investigatorId
//        String investigatorId = getSessionInvestigator(session).getId();
//        if (id.equals(investigatorId)) {
//            //从session中删除
//            session.removeAttribute(CommonConstant.USER_CONTEXT);
//            //从登录表中删除
//            loginInvestigators.remove(investigatorId);
//            //
//            investigatorService.logout(id);
//            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//        } else {
//            //没有权限使他人登出
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        }
//
//
//    }
//
//
//    /**
//     * 获取用户信息
//     *
//     * @param session 会话
//     * @param id
//     * @return 403 没有权限获取他人信息
//     */
//    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
//    public ResponseEntity info(HttpSession session, @PathVariable String id) {
//        //获取investigatorId
//        Investigator investigator = getSessionInvestigator(session);
//        if (id.equals(investigator.getId())) {
//            return ResponseEntity.ok(investigator);
//        } else {
//            //没有权限获取他人信息
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        }
//    }
//
//
//    /**
//     * 登录
//     *
//     * @param session  会话
//     * @param id
//     * @param password 请求参数 密码
//     * @return 401 用户名或密码错误
//     */
//    @RequestMapping(value = "/{id}/session", method = RequestMethod.POST)
//    public ResponseEntity login(HttpSession session, @PathVariable String id, @RequestParam String password) {
//        Investigator investigator;
//        if (null != (investigator = investigatorService.login(id, password))) {
//
//            if (loginInvestigators.containsKey(id)) {
//                HttpSession oldSession = loginInvestigators.get(id);
//                if (!session.equals(oldSession)) {
//                    oldSession.invalidate();
//                }
//            }
//            //登录成功
//            //设置Context
//            loginInvestigators.put(id, session);
//            session.setAttribute(CommonConstant.USER_CONTEXT, investigator);
//            String token = jwtUtil.generateToken(investigator.getId());
//            return ResponseEntity.ok(new Token(investigator.getId(), token));
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//    }
//

    @RequestMapping(value = "/username", method = RequestMethod.GET)
    @ResponseBody
    public String currentUserName(Principal principal) {
        return principal.getName();
    }

    @Autowired
    public void setInvestigatorService(InvestigatorService investigatorService) {
        this.investigatorService = investigatorService;
    }
}
