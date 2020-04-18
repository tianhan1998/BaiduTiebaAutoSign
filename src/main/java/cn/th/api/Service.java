package cn.th.api;

import java.util.List;

/**
 * @author tianh
 */
public interface Service {
    /**
     * 一键签到接口，调用网页端一键签到之后将未签到贴吧再次轮训签到
     */
    void allSigned();

    /**
     * 取得所有关注的贴吧
     * @return 返回为List
     */
    List<String> getAllTieba();

    /**
     * 取得所有未签到的贴吧
     * @return 返回为List
     */
    List<String> getUnsignedTieba();
}
