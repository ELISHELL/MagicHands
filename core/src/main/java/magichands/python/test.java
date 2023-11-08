package magichands.python;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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

public class test {


   static OrtEnvironment env = OrtEnvironment.getEnvironment();
   static OrtSession ort;
    public static OrtSession staticMethod(String path) {
        System.out.println("路径"+path);
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


    public static String convertString(String input) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == '[') {
                result.append('{');
            } else if (c == ']') {
                result.append('}');
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }


    public static float[][][][] parseStringToFloat4DArray(String jsonString) {
        saveStringToFile(jsonString,"/storage/emulated/0/1.txt");
        Gson gson = new Gson();
        return gson.fromJson(jsonString, float[][][][].class);
    }

    public static void saveStringToFile(String content, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
            // 这里可以处理文件写入失败的异常
        }
    }

    public static Map<String, OnnxTensor> processImage(float [][][][] in) {


        try {
           long[] inputShape = {10, 5};
            // 假设您已经创建了输入数据 in，将其转换为 OnnxTensor 对象
            OnnxTensor inputTensor = OnnxTensor.createTensor(env, in);

// 构建输入映射，将输入张量映射到模型的输入名称
            Map<String, OnnxTensor> inputs = new HashMap<>();
            inputs.put("input1", inputTensor);





//           long[][] tensorData = (long[][]) result.get(0).getValue();
//// 打印张量数据
//
//            for (int i = 0; i < tensorData.length; i++) {
////                System.out.print(tensorData[i].length + " ");
////                for (int j = 0; j < tensorData[i].length; j++) {
////                    System.out.print(tensorData[i][j] + " ");
////                }
//
//                System.out.println(Arrays.toString(tensorData[i]));
//            }
           System.out.println("返回值:"+t(inputs));
            return inputs;

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





