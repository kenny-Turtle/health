package com.itheima.test;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.common.Zone;
import org.junit.Test;

/**
 * @author Jay
 * @date 2022/2/19 9:35 上午
 */
public class QiNiuTest {


    //使用七牛云存储来上传本地图片
    @Test
    public void test1() {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
//...其他参数参考类注释

        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
        String accessKey = "Qxk7AtSTpzqjCAMmf_0c3nz2215JkSohV33f2hfn";
        String secretKey = "oSnG94JO5MZiyrknZY2hcRCSqt1pzf7uqH2zqp9f";
        String bucket = "itcasthealth330329";
//如果是Windows情况下，格式是 D:\\qiniu\\test.png
        // /Users/apple/ideaProjects/黑马/传智健康项目/day04/资源/图片资源
        String localFilePath = "/Users/apple/ideaProjects/黑马/传智健康项目/day04/资源/图片资源/03a36073-a140-4942-9b9b-712cecb144901.jpg";
//默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;

        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);

        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }

    }

    //使用七牛云来删除文件
    @Test
    public void test2(){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region0());
//...其他参数参考类注释
        String accessKey = "Qxk7AtSTpzqjCAMmf_0c3nz2215JkSohV33f2hfn";
        String secretKey = "oSnG94JO5MZiyrknZY2hcRCSqt1pzf7uqH2zqp9f";
        String bucket = "itcasthealth330329";
        String key = "FuM1Sa5TtL_ekLsdkYWcf5pyjKGu";
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }

}
