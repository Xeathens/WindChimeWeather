package com.xeathen.windchimeweather.custom;

import android.util.Log;

import com.xeathen.lib.utils.LogUtil;
import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdate.entity.CheckVersionResult;
import com.xuexiang.xupdate.entity.UpdateEntity;
import com.xuexiang.xupdate.proxy.IUpdateParser;
import com.xuexiang.xupdate.utils.UpdateUtils;

/**
 * @author: 蛋清蛋黄
 * @date: 2019/4/9
 * @description:
 */
public class CustomUpdateParser implements IUpdateParser {

    @Override
    public UpdateEntity parseJson(String json) throws Exception {
        LogUtil.i("-->onCustom", json);
        CustomCheckVersionResult result = UpdateUtils.fromJson(json, CustomCheckVersionResult.class);
        LogUtil.i("-->onCustom", result.toString());
        if (result != null && result.getCode() == 0) {
            result = doLocalCompare(result);
            UpdateEntity updateEntity = new UpdateEntity();
            if (result.getUpdateStatus() == CustomCheckVersionResult.NO_NEW_VERSION) {
                LogUtil.i("-->onCustom:", "无更新" + result.getUpdateStatus());

                updateEntity.setHasUpdate(false);
            } else {
                LogUtil.i("-->onCustom:", "有更新");
                if (result.getUpdateStatus() == CustomCheckVersionResult.HAVE_NEW_VERSION_FORCED_UPLOAD) {
                    updateEntity.setForce(true);
                }
                updateEntity.setHasUpdate(true)
                        .setUpdateContent(result.getModifyContent())
                        .setVersionCode(result.getVersionCode())
                        .setVersionName(result.getVersionName())
                        .setDownloadUrl(result.getDownloadUrl())
                        .setSize(result.getApkSize())
                        .setMd5(result.getApkMd5());
            }
            return updateEntity;
        }

        return null;
    }

    /**
     * 进行本地版本判断[防止服务端出错，本来是不需要更新，但是服务端返回是需要更新]
     *
     * @param checkResult
     * @return
     */
    private CustomCheckVersionResult doLocalCompare(CustomCheckVersionResult checkResult) {
        if (checkResult.getUpdateStatus() != CustomCheckVersionResult.NO_NEW_VERSION) { //服务端返回需要更新
            int lastVersionCode = checkResult.getVersionCode();
            if (lastVersionCode <= UpdateUtils.getVersionCode(XUpdate.getContext())) { //最新版本小于等于现在的版本，不需要更新
                checkResult.setRequireUpgrade(CustomCheckVersionResult.NO_NEW_VERSION);
            }
        }
        return checkResult;
    }
}
