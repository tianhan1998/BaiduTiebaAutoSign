package cn.th.api.impl;

import cn.th.api.Service;
import cn.th.utils.Utils;

import java.util.List;

/**
 * @author tianh
 */
public class ServiceImpl implements Service {
    @Override
    public void allSigned() {
        Utils.allSigned(true);
    }

    @Override
    public List<String> getAllTieba() {
        return Utils.getTieBaLiked();
    }

    @Override
    public List<String> getUnsignedTieba() {
        return Utils.getAllUnsignedTieBa();
    }
}
