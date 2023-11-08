package magichands.core.tool;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OnnxValue;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;

public class OnnxTool {
    static OrtEnvironment env = OrtEnvironment.getEnvironment();
    static OrtSession ort;
    public static void init(){

    }
    public static OrtSession onnxcreate(String path) {
        String modelPath = path; // 替换成你的ONNX模型文件路径
        OrtSession.SessionOptions options = new OrtSession.SessionOptions();
        try {
            options.setOptimizationLevel(OrtSession.SessionOptions.OptLevel.ALL_OPT );
        } catch (OrtException e) {
            throw new RuntimeException(e);
        }
// 或者
        try {
            ort= env.createSession(modelPath, options);
            return ort;
        } catch (OrtException e) {
            throw new RuntimeException(e);
        }

    }
    public static float[][][][] Float4DArray(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, float[][][][].class);
    }
    public static List<Long> illation(float [][][][] in, String type) {
        try {
            // 假设您已经创建了输入数据 in，将其转换为 OnnxTensor 对象
            OnnxTensor inputTensor = OnnxTensor.createTensor(env, in);
            // 构建输入映射，将输入张量映射到模型的输入名称
            Map<String, OnnxTensor> inputs = new HashMap<>();
            inputs.put(type, inputTensor);
            return t(inputs);
        } catch (OrtException e) {
            throw new RuntimeException(e);
        }
    }
    public static List<Long> t(Map<String, OnnxTensor> inputs) {
        try {
            Set<String> OPT = ort.getOutputNames();
            OrtSession.Result result = ort.run(inputs);
            Optional<OnnxValue> outputTensor = result.get(OPT.iterator().next());
            System.out.println(outputTensor);
            return outputTensor.map(value -> {
                try {
                    long[][] tensorData = (long[][]) value.getValue();
                    List<Long> resultValues = new ArrayList<>();
                    for (int i = 0; i < tensorData.length; i++) {
                        for (int j = 0; j < tensorData[i].length; j++) {
                            resultValues.add(tensorData[i][j]);
                        }
                    }
                    return resultValues;
                } catch (OrtException e) {
                    throw new RuntimeException(e);
                }
            }).orElse(new ArrayList<>());
        } catch (OrtException e) {
            throw new RuntimeException(e);
        }
    }

}
