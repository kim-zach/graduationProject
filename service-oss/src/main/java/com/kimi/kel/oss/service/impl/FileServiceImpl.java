package com.kimi.kel.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.kimi.kel.oss.service.FileService;
import com.kimi.kel.oss.util.OssProperties;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Service
public class FileServiceImpl implements FileService {
    @Override
    public String upload(InputStream inputStream, String module, String fileName) {

        // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
//        String endpoint = "yourEndpoint"; //定义在常量OssProperties中，不再这里声明
// 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
//        String accessKeyId = "yourAccessKeyId";
//        String accessKeySecret = "yourAccessKeySecret";

// 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(
                OssProperties.ENDPOINT,
                OssProperties.KEY_ID,
                OssProperties.KEY_SECRET);

// 填写本地文件的完整路径。如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
//        InputStream inputStream = new FileInputStream("D:\\localpath\\examplefile.txt");//参数已从方法传进
// 依次填写Bucket名称（例如examplebucket）和Object完整路径（例如exampledir/exampleobject.txt）。Object完整路径中不能包含Bucket名称。
        //判断BUCKET_NAME是否存在
        if(!ossClient.doesBucketExist(OssProperties.BUCKET_NAME)){
            ossClient.createBucket(OssProperties.BUCKET_NAME);
            ossClient.setBucketAcl(OssProperties.BUCKET_NAME, CannedAccessControlList.PublicRead);
        }

        //上传文件流
        //文件目录结构  "avatar/2022/01/28/uuid.jpg"
        //构建日期路径
        String timeFolder = new DateTime().toString("/yyyy/MM/dd/");
        //文件名生成
        fileName = UUID.randomUUID().toString() + fileName.substring(fileName.lastIndexOf("."));

        String key = module  + timeFolder  + fileName;
        ossClient.putObject(OssProperties.BUCKET_NAME, key, inputStream);

// 关闭OSSClient。
        ossClient.shutdown();

        //文件的url地址
        //https://kel-file-kimi.oss-cn-shenzhen.aliyuncs.com/avatar/-2505434.gif
        //https://bucketName.endpoint / + key
        return "https://" +OssProperties.BUCKET_NAME + "." + OssProperties.ENDPOINT + "/" + key;
    }

    @Override
    public boolean removeFile(String url) {


//// 填写Bucket名称。
//        String bucketName = "examplebucket";
// 填写文件完整路径。文件完整路径中不能包含Bucket名称。
        // https://kel-file-kimi.oss-cn-shenzhen.aliyuncs.com
        // avatar/-2505434.gif
        String host = "https://" + OssProperties.BUCKET_NAME  + "." + OssProperties.ENDPOINT + "/";
        String objectName = url.substring(host.length()) ;

// 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(
                OssProperties.ENDPOINT,
                OssProperties.KEY_ID,
                OssProperties.KEY_SECRET);

// 删除文件或目录。如果要删除目录，目录必须为空。
        ossClient.deleteObject(OssProperties.BUCKET_NAME, objectName);

        //判断删除成功与否
        boolean result = ossClient.doesObjectExist(OssProperties.BUCKET_NAME, objectName);
        log.info("objectName:{}",objectName);

// 关闭OSSClient。
        ossClient.shutdown();


        return result;
    }
}
