package magichands.core.model.adb;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class run {

    public interface ShellResultCallback {
        void onResultReceived(String result);
    }

    public static void runShell(final String cmd ,final ShellResultCallback callback) {
        if (TextUtils.isEmpty(cmd)) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                new SocketClient(cmd, new SocketClient.onServiceSend() {
                    @Override
                    public void getSend(String result) {
                        if (callback != null) {
                            callback.onResultReceived(result);
                        }

                    }
                });
            }
        }).start();
    }

    public static boolean hasRootAccess() {
        try {
            Process process = Runtime.getRuntime().exec("su");
            OutputStream outputStream = process.getOutputStream();
            outputStream.write("exit\n".getBytes());
            outputStream.flush();

            int exitValue = process.waitFor();
            return (exitValue == 0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean hasAdbAccess() {
        try {
            Process process = Runtime.getRuntime().exec("adb shell");
            OutputStream outputStream = process.getOutputStream();
            outputStream.write("exit\n".getBytes());
            outputStream.flush();

            int exitValue = process.waitFor();
            return (exitValue == 0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String executeCommand(String command) {
        Process process = null;
        BufferedReader reader = null;
        BufferedReader errorReader = null;
        StringBuilder output = new StringBuilder();

        try {
            if (hasRootAccess()) {
                System.out.println("root环境正常");
                process = Runtime.getRuntime().exec("su");
                OutputStream outputStream = process.getOutputStream();
                outputStream.write((command + "\n").getBytes());
                outputStream.flush();
                outputStream.write("exit\n".getBytes());
                outputStream.flush();

                process.waitFor();

                // 读取命令执行结果并保存
                reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }

                // 读取命令执行错误输出并保存
                errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String errorLine;
                while ((errorLine = errorReader.readLine()) != null) {
                    output.append(errorLine).append("\n");
                }
            } else if (hasAdbAccess()) {
                System.out.println("adb环境正常");
                process = Runtime.getRuntime().exec("adb shell " + command);
                process.waitFor();

                // 读取命令执行结果并保存
                reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }

                // 读取命令执行错误输出并保存
                errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String errorLine;
                while ((errorLine = errorReader.readLine()) != null) {
                    output.append(errorLine).append("\n");
                }
            } else {
                System.out.println("普通环境正常");
                process = Runtime.getRuntime().exec(command);
                process.waitFor();

                // 读取命令执行结果并保存
                reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }

                // 读取命令执行错误输出并保存
                errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String errorLine;
                while ((errorLine = errorReader.readLine()) != null) {
                    output.append(errorLine).append("\n");
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (errorReader != null) {
                try {
                    errorReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return output.toString();
    }

}
