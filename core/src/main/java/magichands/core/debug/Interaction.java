package magichands.core.debug;

import static magichands.core.tool.HttpTool.sendPostRequestWithFile;

import android.annotation.SuppressLint;
import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import magichands.core.Env;

public class Interaction {
    @SuppressLint("StaticFieldLeak")
    static Context context = Env.Companion.getContext();
    public static  String LOG_SERVER_URL;
    // 发送日志到服务器的方法
    public static void sendLogToServer(String log) {
        try {
            System.out.println("日志：" + log);
            URL url = new URL(LOG_SERVER_URL + "log_receiver");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setChunkedStreamingMode(10 * 1024 * 1024);
            connection.setReadTimeout(5000); // 设置读取超时时间为10秒
            String encodedLog = URLEncoder.encode(log, StandardCharsets.UTF_8.toString())
                    .replace("+", "%20"); // 将加号替换为%20
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(encodedLog.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            outputStream.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String response = reader.readLine();
                reader.close();
                // 在这里可以处理服务器返回的响应数据
                System.out.println("Response: " + response);

            } else {
                System.out.println("Response: " + "no OK");
                // 请求失败，可以处理错误情况
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void downloadFile(String savePath) {
        try {
            URL url = new URL(LOG_SERVER_URL+"download");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                FileOutputStream fileOutputStream = new FileOutputStream(savePath);

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }

                fileOutputStream.close();
                inputStream.close();
                System.out.println("File downloaded successfully.");
            } else {
                // 请求失败，可以处理错误情况
                System.out.println("Failed to download file. Response Code: " + responseCode);
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void downloadFilejava(String savePath) {
        try {
            URL url = new URL(LOG_SERVER_URL+"download_java");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                FileOutputStream fileOutputStream = new FileOutputStream(savePath);

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }

                fileOutputStream.close();
                inputStream.close();
                System.out.println("File downloaded successfully.");
            } else {
                // 请求失败，可以处理错误情况
                System.out.println("Failed to download file. Response Code: " + responseCode);
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void downloadFilepy(String savePath) {
        try {
            URL url = new URL(LOG_SERVER_URL+"download_py");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                FileOutputStream fileOutputStream = new FileOutputStream(savePath);

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }

                fileOutputStream.close();
                inputStream.close();
                System.out.println("File downloaded successfully.");
            } else {
                // 请求失败，可以处理错误情况
                System.out.println("Failed to download file. Response Code: " + responseCode);
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void uploadFile()  {
        try {
            System.out.println(sendPostRequestWithFile(LOG_SERVER_URL+"upload",null,"file",context.getExternalFilesDir("debug")+"/uix/1.png"));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("================================");
            System.out.println(sendPostRequestWithFile(LOG_SERVER_URL + "upload1", null, "file", context.getExternalFilesDir("debug") + "/uix/1.xml"));

        } catch (IOException e) {
            System.out.println("================================");
            System.out.println(e);
        }

    }
    public static int checkResponse() {
        try {
            URL requestUrl = new URL(LOG_SERVER_URL+"cmd");
            HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String response = reader.readLine();
                reader.close();
                switch (response){
                    case "3":
                        System.out.println("1");
                        return 3;
                    case  "4":
                        System.out.println("4");
                        return 4;
                    case  "5":
                        System.out.println("5");
                        return 5;
                    case  "6":
                        System.out.println("6");
                        return 6;
                    case  "7":
                        System.out.println("7");
                        return 7;
                    case  "8":
                        System.out.println("8");
                        return 8;
                    case  "9":
                        System.out.println("9");
                        return 9;

                    case  "30":
                        System.out.println("31");
                        return 30;

                    case  "31":
                        System.out.println("31");
                        return 31;



                    case "0":
                        System.out.println("0");
                        return 0;

                }
            } else {
                // 请求失败，可以处理错误情况
                System.out.println("Failed to download file. Response Code: " + responseCode);
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int in() {
        try {

            URL requestUrl = new URL(LOG_SERVER_URL+"set");
            HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String response = reader.readLine();
                reader.close();
            } else {
                // 请求失败，可以处理错误情况
                System.out.println("Failed to download file. Response Code: " + responseCode);
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int inn() {
        try {

            URL requestUrl = new URL(LOG_SERVER_URL+"heartbeat");
            HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            System.out.println("请求结果:"+responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String response = reader.readLine();
                reader.close();
                System.out.println("请求正常");
            } else {
                // 请求失败，可以处理错误情况
                System.out.println("Failed to download file. Response Code: " + responseCode);
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    public static int png() {
        try {

            URL requestUrl = new URL(LOG_SERVER_URL+"pngset");
            HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String response = reader.readLine();
                reader.close();
            } else {
                // 请求失败，可以处理错误情况
                System.out.println("Failed to download file. Response Code: " + responseCode);
            }
//            switch (responseCode) {
//                case HttpURLConnection.HTTP_OK:
//                    System.out.println("Response: HTTP_OK");
//                    // 在这里可以处理服务器返回的响应数据
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                    String response = reader.readLine();
//                    reader.close();
//                    System.out.println("Response: " + response);
//                    break;
//                case HttpURLConnection.HTTP_NOT_FOUND:
//                    System.out.println("Response: HTTP_NOT_FOUND");
//                    // 处理 HTTP_NOT_FOUND 返回情况
//                    break;
//                case HttpURLConnection.HTTP_UNAUTHORIZED:
//                    System.out.println("Response: HTTP_UNAUTHORIZED");
//                    // 处理 HTTP_UNAUTHORIZED 返回情况
//                    break;
//                // 可以根据需要添加其他的 HTTP 返回码判断
//                default:
//                    System.out.println("Response: " + responseCode);
//                    // 处理其他返回情况
//                    break;
//            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
