package magichands.core.tool;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpTool {
    private static final OkHttpClient client = new OkHttpClient();

    /**
     * 发送不带文件的GET请求，并返回请求结果
     *
     * @param url 请求的URL
     * @return 请求结果
     * @throws IOException 网络请求异常
     */
    public static String sendGetRequest(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public static String sendGetRequests(String url,String jwtToken) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + jwtToken)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    /**
     * 发送带文件的GET请求，并返回请求结果
     *
     * @param url  请求的URL
     * @param file 文件
     * @return 请求结果
     * @throws IOException 网络请求异常
     */
    public static String sendGetRequestWithFile(String url,String fileKey, File file) throws IOException {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(fileKey, file.getName(),
                        RequestBody.create(MediaType.parse("application/octet-stream"), file))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .method("GET", requestBody)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    /**
     * 发送不带文件的POST请求，并返回请求结果
     *
     * @param url      请求的URL
     * @param formData 请求的表单数据
     * @return 请求结果
     * @throws IOException 网络请求异常
     */
    public static String sendPostRequest(String url, FormBody formData) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .post(formData)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    /**
     * 发送带文件的POST请求，并返回请求结果
     *
     * @param url      请求的URL
     * @param formData 请求的表单数据
     * @param file     文件
     * @param fileKey  文件参数名
     * @return 请求结果
     * @throws IOException 网络请求异常
     */
    public static String sendPostRequestWithFile(String url, FormBody formData, String fileKey, File file) throws IOException {
        RequestBody fileRequestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        RequestBody requestBody=null;
        if (formData!=null) {
            requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addPart(formData)
                    .addFormDataPart(fileKey, file.getName(), fileRequestBody)
                    .build();
        }else{
            requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(fileKey, file.getName(), fileRequestBody)
                    .build();
        }
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
    public static String sendPostRequestWithFile(String url,String jn, String fileKey,String f) throws IOException {
        File file = new File(f);
        if (jn != null && !jn.equals("")) {
            JsonParser jsonParser = new JsonParser();
            // 将字符串解析为 JsonElement
            JsonElement jsonElement = jsonParser.parse(jn);
            // 将 JsonElement 转换为 JsonObject
            JsonObject json = jsonElement.getAsJsonObject();
            FormBody.Builder builder = new FormBody.Builder();
            for (String key : json.keySet()) {
                String value = json.get(key).getAsString();
                builder.add(key, value);
            }
            FormBody formData = builder.build();
            return sendPostRequestWithFile(url, formData, fileKey,file);
            // 条件为真时的逻辑处理
        }

        return sendPostRequestWithFile(url, null, fileKey,file);


    }

    public static String generateJwtToken(String userId, String username, String userRole) {
        String SECRET_KEY = "ThisIsASecretKey"; // 密钥，与Python端使用相同密钥
        long expirationTime = System.currentTimeMillis() + 60000; // 设置过期时间为1分钟后


        JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject("JavaRequest") // 标识请求来源
                .claim("user_id", userId)   // 添加用户ID到有效负载
                .claim("username", username)   // 添加用户名到有效负载
                .claim("user_role", userRole)   // 添加用户角色到有效负载
                .setExpiration(new Date(expirationTime))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY);

        return jwtBuilder.compact();
    }


}
