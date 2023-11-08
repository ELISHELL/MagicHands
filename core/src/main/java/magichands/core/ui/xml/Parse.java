package magichands.core.ui.xml;



import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Parse {

    public static View parseLayout(Context context, String layoutName) {
// 初始化 AssetManager
        AssetManager assetManager = context.getAssets();

// 读取 XML 文件
        InputStream inputStream;
        try {
            inputStream = assetManager.open(layoutName);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

// 解析 XML 布局
        XmlPullParserFactory factory = null;
        XmlPullParser parser = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            parser = factory.newPullParser();
            parser.setInput(inputStream, null);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return null;
        }

// 动态加载布局
        LinearLayout layout = new LinearLayout(context);
        RadioGroup radioGroup = new RadioGroup(context);
        try {
            int eventType = parser.getEventType();
            LinearLayout currentLayout = null; // 记录当前 LinearLayout
            Map<Integer,LinearLayout> ma = new HashMap<>();
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String name = parser.getName();
                    System.out.println(name+":nameS");
                    if (name.equals("LinearLayout")) {
                        LinearLayout linearLayout = new LinearLayout(context);
                        linearLayout.setOrientation(LinearLayout.VERTICAL);
                        String bg = parser.getAttributeValue(null, "background");
                        String tag = parser.getAttributeValue(null, "tag");
                        String layout_height = parser.getAttributeValue(null, "layout_height");
                        String layout_width = parser.getAttributeValue(null, "layout_width");
                        String orientation = parser.getAttributeValue(null, "orientation");
                        String padding = parser.getAttributeValue(null, "padding");

// 解析 layout_height 属性并设置值
                        if (layout_width != null) {
                            if (layout_width.equals("match_parent")) {
                                linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT));
                            } else if (layout_width.equals("wrap_content")) {
                                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            } else {
                                // 解析数字类型的值
                                String value = layout_width.replaceAll("[^\\d]", "");
                                int width = Integer.parseInt(value);
                                if (layout_width.contains("dp")) {
                                    linearLayout.setLayoutParams(new LinearLayout.LayoutParams(dpToPx(context,width), ViewGroup.LayoutParams.MATCH_PARENT));
                                } else if (layout_width.contains("px")) {
                                    linearLayout.setLayoutParams(new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT));
                                }
                            }
                        }
                        if (layout_height != null) {
                            if (layout_height.equals("match_parent")) {
                                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(linearLayout.getLayoutParams().width,
                                        ViewGroup.LayoutParams.MATCH_PARENT));
                            } else if (layout_height.equals("wrap_content")) {
                                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(linearLayout.getLayoutParams().width, ViewGroup.LayoutParams.WRAP_CONTENT));
                            } else {
                                // 解析数字类型的值
                                String value = layout_height.replaceAll("[^\\d]", "");
                                int height = Integer.parseInt(value);
                                if (layout_height.contains("dp")) {
                                    linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(context,height)));
                                } else if (layout_height.contains("px")) {
                                    linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
                                }
                            }
                        }


// 解析 orientation 属性并设置值
                        if (orientation != null) {
                            if (orientation.equals("horizontal")) {
                                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                            } else if (orientation.equals("vertical")) {
                                linearLayout.setOrientation(LinearLayout.VERTICAL);
                            }
                        }
                        if (padding != null) {
                            int paddingVal = dpToPx(context,Integer.parseInt(padding.replaceAll("[^\\d]", "")));
                            linearLayout.setPadding(paddingVal, paddingVal, paddingVal, paddingVal);
                        }
                        if (bg != null && !bg.isEmpty()) {
                            int color = Color.parseColor("#"+bg.substring(1));
                            linearLayout.setBackgroundColor(color);

//                            int colorResId = context.getResources().getIdentifier("color/" + bg.substring(1), null, context.getPackageName());
//                            linearLayout.setBackgroundResource(colorResId);
                        }
                        if (currentLayout == null) {
                            // 如果当前没有 LinearLayout，说明这是第一个，将其设置为根布局
                            layout.addView(linearLayout);
                            ma.put(Integer.valueOf(tag), layout);
                        } else {
                            LinearLayout parentLayout = ma.get(Integer.valueOf(tag));
                            if (parentLayout != null) {
                                // 如果 tag 对应的父布局已经存在，将子视图添加到该布局中
                                parentLayout.addView(linearLayout);
                                currentLayout = parentLayout;
                            } else {
                                // 如果 tag 对应的父布局不存在，将子视图添加到当前布局中
                                currentLayout.addView(linearLayout);
                            }
                            ma.put(Integer.valueOf(tag), currentLayout);
                        }
                        currentLayout = linearLayout;
                    }else if (name.equals("TextView")) {
                        TextView textView = new TextView(context);
                        String text = parser.getAttributeValue(null, "text");
                        String layout_height = parser.getAttributeValue(null, "layout_height");
                        String layout_width = parser.getAttributeValue(null, "layout_width");
                        textView.setText(text);
                        if (layout_width != null) {
                            if (layout_width.equals("match_parent")) {
                                textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                            } else if (layout_width.equals("wrap_content")) {
                                textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            } else {
                                // 解析数字类型的值
                                String value = layout_width.replaceAll("[^\\d]", "");
                                int width = Integer.parseInt(value);
                                if (layout_width.contains("dp")) {
                                    textView.setLayoutParams(new LinearLayout.LayoutParams(dpToPx(context,width), ViewGroup.LayoutParams.MATCH_PARENT));
                                } else if (layout_width.contains("px")) {
                                    textView.setLayoutParams(new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT));
                                }
                            }
                        }
// 解析 layout_height 属性并设置值
                        if (layout_height != null) {
                            if (layout_height.equals("match_parent")) {
                                textView.setLayoutParams(new LinearLayout.LayoutParams(textView.getLayoutParams().width, ViewGroup.LayoutParams.MATCH_PARENT));
                            } else if (layout_height.equals("wrap_content")) {
                                textView.setLayoutParams(new LinearLayout.LayoutParams(textView.getLayoutParams().width, ViewGroup.LayoutParams.WRAP_CONTENT));
                            } else {
                                // 解析数字类型的值
                                String value = layout_height.replaceAll("[^\\d]", "");
                                int height = Integer.parseInt(value);
                                if (layout_height.contains("dp")) {
                                    textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(context,height)));
                                } else if (layout_height.contains("px")) {
                                    textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
                                }
                            }
                        }
// 解析 layout_width 属性并设置值
                        if (currentLayout == null) {
                            // 如果当前没有 LinearLayout，说明这是根布局的子 View，将其直接添加到根布局中
                            layout.addView(textView);
                        } else {
                            currentLayout.addView(textView);
                        }
                    }else if (name.equals("Spinner")) {
                        Spinner spinner = new Spinner(context);
                        String entries = parser.getAttributeValue(null, "entries");
                        String layout_height = parser.getAttributeValue(null, "layout_height");
                        String layout_width = parser.getAttributeValue(null, "layout_width");
                        String idName = parser.getAttributeValue(null, "id");
                        int id = context.getResources().getIdentifier(idName, "id", context.getPackageName());
                        spinner.setId(id);
                        Env.getSpinner_m().put(idName,spinner);
                        if (entries != null) {
//                            String[] options = {entries};
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item);
                            String [] es = entries.split(",");
                            for (int i = 0; i < es.length; i++) {
                                adapter.add(es[i]);
                            }
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(adapter);
                        }
                        if (layout_width != null) {
                            if (layout_width.equals("match_parent")) {
                                spinner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                            } else if (layout_width.equals("wrap_content")) {
                                spinner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            } else {
                                // 解析数字类型的值
                                String value = layout_width.replaceAll("[^\\d]", "");
                                int width = Integer.parseInt(value);
                                if (layout_width.contains("dp")) {
                                    spinner.setLayoutParams(new LinearLayout.LayoutParams(dpToPx(context,width), ViewGroup.LayoutParams.MATCH_PARENT));
                                } else if (layout_width.contains("px")) {
                                    spinner.setLayoutParams(new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT));
                                }
                            }
                        }
// 解析 layout_height 属性并设置值
                        if (layout_height != null) {
                            if (layout_height.equals("match_parent")) {
                                spinner.setLayoutParams(new LinearLayout.LayoutParams(spinner.getLayoutParams().width, ViewGroup.LayoutParams.MATCH_PARENT));
                            } else if (layout_height.equals("wrap_content")) {
                                spinner.setLayoutParams(new LinearLayout.LayoutParams(spinner.getLayoutParams().width, ViewGroup.LayoutParams.WRAP_CONTENT));
                            } else {
                                // 解析数字类型的值
                                String value = layout_height.replaceAll("[^\\d]", "");
                                int height = Integer.parseInt(value);
                                if (layout_height.contains("dp")) {
                                    spinner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(context,height)));
                                } else if (layout_height.contains("px")) {
                                    spinner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
                                }
                            }
                        }
// 解析 layout_width 属性并设置值
                        if (currentLayout == null) {
                            // 如果当前没有 LinearLayout，说明这是根布局的子 View，将其直接添加到根布局中
                            layout.addView(spinner);
                        } else {
                            currentLayout.addView(spinner);
                        }
                    }else if (name.equals("Button")) {
                        Button button = new Button(context);
                        String layout_height = parser.getAttributeValue(null, "layout_height");
                        String layout_width = parser.getAttributeValue(null, "layout_width");
                        String text = parser.getAttributeValue(null, "text");
                        String idName = parser.getAttributeValue(null, "id");
                        int id = context.getResources().getIdentifier(idName, "id", context.getPackageName());
                        button.setId(id);
                        button.setText(text);
                        Env.getButton_m().put(idName,button);
                        if (layout_width != null) {
                            if (layout_width.equals("match_parent")) {
                                button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                            } else if (layout_width.equals("wrap_content")) {
                                button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            } else {
                                // 解析数字类型的值
                                String value = layout_width.replaceAll("[^\\d]", "");
                                int width = Integer.parseInt(value);
                                if (layout_width.contains("dp")) {
                                    button.setLayoutParams(new LinearLayout.LayoutParams(dpToPx(context,width), ViewGroup.LayoutParams.MATCH_PARENT));
                                } else if (layout_width.contains("px")) {
                                    button.setLayoutParams(new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT));
                                }
                            }
                        }
// 解析 layout_height 属性并设置值
                        if (layout_height != null) {
                            if (layout_height.equals("match_parent")) {
                                button.setLayoutParams(new LinearLayout.LayoutParams(button.getLayoutParams().width, ViewGroup.LayoutParams.MATCH_PARENT));
                            } else if (layout_height.equals("wrap_content")) {
                                button.setLayoutParams(new LinearLayout.LayoutParams(button.getLayoutParams().width, ViewGroup.LayoutParams.WRAP_CONTENT));
                            } else {
                                // 解析数字类型的值
                                String value = layout_height.replaceAll("[^\\d]", "");
                                int height = Integer.parseInt(value);
                                if (layout_height.contains("dp")) {
                                    button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(context,height)));
                                } else if (layout_height.contains("px")) {
                                    button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
                                }
                            }
                        }
                        if (currentLayout == null) {
                            // 如果当前没有 LinearLayout，说明这是根布局的子 View，将其直接添加到根布局中
                            layout.addView(button);
                        } else {
                            currentLayout.addView(button);
                        }
                    }else if (name.equals("EditText")) {
                        EditText edittext = new EditText(context);
                        String layout_height = parser.getAttributeValue(null, "layout_height");
                        String layout_width = parser.getAttributeValue(null, "layout_width");
                        String text = parser.getAttributeValue(null, "text");
                        String idName = parser.getAttributeValue(null, "id");
                        String hint=parser.getAttributeValue(null,"hint");
                        int id = context.getResources().getIdentifier(idName, "id", context.getPackageName());
                        edittext.setId(id);
                        edittext.setText(text);
                        Env.getEdittext_m().put(idName,edittext);
                        if (hint != null) {
                            edittext.setHint(hint);
                        }

                        if (layout_width != null) {
                            if (layout_width.equals("match_parent")) {
                                edittext.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                            } else if (layout_width.equals("wrap_content")) {
                                edittext.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            } else {
                                // 解析数字类型的值
                                String value = layout_width.replaceAll("[^\\d]", "");
                                int width = Integer.parseInt(value);
                                if (layout_width.contains("dp")) {
                                    edittext.setLayoutParams(new LinearLayout.LayoutParams(dpToPx(context,width), ViewGroup.LayoutParams.MATCH_PARENT));
                                } else if (layout_width.contains("px")) {
                                    edittext.setLayoutParams(new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT));
                                }
                            }
                        }
// 解析 layout_height 属性并设置值
                        if (layout_height != null) {
                            if (layout_height.equals("match_parent")) {
                                edittext.setLayoutParams(new LinearLayout.LayoutParams(edittext.getLayoutParams().width, ViewGroup.LayoutParams.MATCH_PARENT));
                            } else if (layout_height.equals("wrap_content")) {
                                edittext.setLayoutParams(new LinearLayout.LayoutParams(edittext.getLayoutParams().width, ViewGroup.LayoutParams.WRAP_CONTENT));
                            } else {
                                // 解析数字类型的值
                                String value = layout_height.replaceAll("[^\\d]", "");
                                int height = Integer.parseInt(value);
                                if (layout_height.contains("dp")) {
                                    edittext.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(context,height)));
                                } else if (layout_height.contains("px")) {
                                    edittext.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
                                }
                            }
                        }

                        if (currentLayout == null) {
                            // 如果当前没有 LinearLayout，说明这是根布局的子 View，将其直接添加到根布局中
                            layout.addView(edittext);
                        } else {
                            currentLayout.addView(edittext);
                        }
                    }
                }
                eventType = parser.next();
            }


        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

// 返回解析后的 View 对象
        return layout;
    }

    public static View parseLayout1(Context context, String layoutName) {
        // 读取 XML 文件
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(layoutName);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        // 解析 XML 布局
        XmlPullParserFactory factory = null;
        XmlPullParser parser = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            parser = factory.newPullParser();
            parser.setInput(fileInputStream, null);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return null;
        }

// 动态加载布局
        LinearLayout layout = new LinearLayout(context);
        RadioGroup radioGroup = new RadioGroup(context);
        try {
            int eventType = parser.getEventType();
            LinearLayout currentLayout = null; // 记录当前 LinearLayout
            Map<Integer,LinearLayout> ma = new HashMap<>();
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String name = parser.getName();
                    System.out.println(name+":nameS");
                    if (name.equals("LinearLayout")) {
                        LinearLayout linearLayout = new LinearLayout(context);
                        linearLayout.setOrientation(LinearLayout.VERTICAL);
                        String bg = parser.getAttributeValue(null, "background");
                        String tag = parser.getAttributeValue(null, "tag");
                        String layout_height = parser.getAttributeValue(null, "layout_height");
                        String layout_width = parser.getAttributeValue(null, "layout_width");
                        String orientation = parser.getAttributeValue(null, "orientation");
                        String padding = parser.getAttributeValue(null, "padding");

// 解析 layout_height 属性并设置值
                        if (layout_width != null) {
                            if (layout_width.equals("match_parent")) {
                                linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT));
                            } else if (layout_width.equals("wrap_content")) {
                                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            } else {
                                // 解析数字类型的值
                                String value = layout_width.replaceAll("[^\\d]", "");
                                int width = Integer.parseInt(value);
                                if (layout_width.contains("dp")) {
                                    linearLayout.setLayoutParams(new LinearLayout.LayoutParams(dpToPx(context,width), ViewGroup.LayoutParams.MATCH_PARENT));
                                } else if (layout_width.contains("px")) {
                                    linearLayout.setLayoutParams(new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT));
                                }
                            }
                        }
                        if (layout_height != null) {
                            if (layout_height.equals("match_parent")) {
                                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(linearLayout.getLayoutParams().width,
                                        ViewGroup.LayoutParams.MATCH_PARENT));
                            } else if (layout_height.equals("wrap_content")) {
                                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(linearLayout.getLayoutParams().width, ViewGroup.LayoutParams.WRAP_CONTENT));
                            } else {
                                // 解析数字类型的值
                                String value = layout_height.replaceAll("[^\\d]", "");
                                int height = Integer.parseInt(value);
                                if (layout_height.contains("dp")) {
                                    linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(context,height)));
                                } else if (layout_height.contains("px")) {
                                    linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
                                }
                            }
                        }


// 解析 orientation 属性并设置值
                        if (orientation != null) {
                            if (orientation.equals("horizontal")) {
                                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                            } else if (orientation.equals("vertical")) {
                                linearLayout.setOrientation(LinearLayout.VERTICAL);
                            }
                        }
                        if (padding != null) {
                            int paddingVal = dpToPx(context,Integer.parseInt(padding.replaceAll("[^\\d]", "")));
                            linearLayout.setPadding(paddingVal, paddingVal, paddingVal, paddingVal);
                        }
                        if (bg != null && !bg.isEmpty()) {
                            int color = Color.parseColor("#"+bg.substring(1));
                            linearLayout.setBackgroundColor(color);

//                            int colorResId = context.getResources().getIdentifier("color/" + bg.substring(1), null, context.getPackageName());
//                            linearLayout.setBackgroundResource(colorResId);
                        }
                        if (currentLayout == null) {
                            // 如果当前没有 LinearLayout，说明这是第一个，将其设置为根布局
                            layout.addView(linearLayout);
                            ma.put(Integer.valueOf(tag), layout);
                        } else {
                            LinearLayout parentLayout = ma.get(Integer.valueOf(tag));
                            if (parentLayout != null) {
                                // 如果 tag 对应的父布局已经存在，将子视图添加到该布局中
                                parentLayout.addView(linearLayout);
                                currentLayout = parentLayout;
                            } else {
                                // 如果 tag 对应的父布局不存在，将子视图添加到当前布局中
                                currentLayout.addView(linearLayout);
                            }
                            ma.put(Integer.valueOf(tag), currentLayout);
                        }
                        currentLayout = linearLayout;
                    }else if (name.equals("TextView")) {
                        TextView textView = new TextView(context);
                        String text = parser.getAttributeValue(null, "text");
                        String layout_height = parser.getAttributeValue(null, "layout_height");
                        String layout_width = parser.getAttributeValue(null, "layout_width");
                        textView.setText(text);
                        if (layout_width != null) {
                            if (layout_width.equals("match_parent")) {
                                textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                            } else if (layout_width.equals("wrap_content")) {
                                textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            } else {
                                // 解析数字类型的值
                                String value = layout_width.replaceAll("[^\\d]", "");
                                int width = Integer.parseInt(value);
                                if (layout_width.contains("dp")) {
                                    textView.setLayoutParams(new LinearLayout.LayoutParams(dpToPx(context,width), ViewGroup.LayoutParams.MATCH_PARENT));
                                } else if (layout_width.contains("px")) {
                                    textView.setLayoutParams(new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT));
                                }
                            }
                        }
// 解析 layout_height 属性并设置值
                        if (layout_height != null) {
                            if (layout_height.equals("match_parent")) {
                                textView.setLayoutParams(new LinearLayout.LayoutParams(textView.getLayoutParams().width, ViewGroup.LayoutParams.MATCH_PARENT));
                            } else if (layout_height.equals("wrap_content")) {
                                textView.setLayoutParams(new LinearLayout.LayoutParams(textView.getLayoutParams().width, ViewGroup.LayoutParams.WRAP_CONTENT));
                            } else {
                                // 解析数字类型的值
                                String value = layout_height.replaceAll("[^\\d]", "");
                                int height = Integer.parseInt(value);
                                if (layout_height.contains("dp")) {
                                    textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(context,height)));
                                } else if (layout_height.contains("px")) {
                                    textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
                                }
                            }
                        }
// 解析 layout_width 属性并设置值
                        if (currentLayout == null) {
                            // 如果当前没有 LinearLayout，说明这是根布局的子 View，将其直接添加到根布局中
                            layout.addView(textView);
                        } else {
                            currentLayout.addView(textView);
                        }
                    }else if (name.equals("Spinner")) {
                        Spinner spinner = new Spinner(context);
                        String entries = parser.getAttributeValue(null, "entries");
                        String layout_height = parser.getAttributeValue(null, "layout_height");
                        String layout_width = parser.getAttributeValue(null, "layout_width");
                        String idName = parser.getAttributeValue(null, "id");
                        int id = context.getResources().getIdentifier(idName, "id", context.getPackageName());
                        spinner.setId(id);
                        Env.getSpinner_m().put(idName,spinner);
                        if (entries != null) {
//                            String[] options = {entries};
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item);
                            String [] es = entries.split(",");
                            for (int i = 0; i < es.length; i++) {
                                adapter.add(es[i]);
                            }
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(adapter);
                        }
                        if (layout_width != null) {
                            if (layout_width.equals("match_parent")) {
                                spinner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                            } else if (layout_width.equals("wrap_content")) {
                                spinner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            } else {
                                // 解析数字类型的值
                                String value = layout_width.replaceAll("[^\\d]", "");
                                int width = Integer.parseInt(value);
                                if (layout_width.contains("dp")) {
                                    spinner.setLayoutParams(new LinearLayout.LayoutParams(dpToPx(context,width), ViewGroup.LayoutParams.MATCH_PARENT));
                                } else if (layout_width.contains("px")) {
                                    spinner.setLayoutParams(new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT));
                                }
                            }
                        }
// 解析 layout_height 属性并设置值
                        if (layout_height != null) {
                            if (layout_height.equals("match_parent")) {
                                spinner.setLayoutParams(new LinearLayout.LayoutParams(spinner.getLayoutParams().width, ViewGroup.LayoutParams.MATCH_PARENT));
                            } else if (layout_height.equals("wrap_content")) {
                                spinner.setLayoutParams(new LinearLayout.LayoutParams(spinner.getLayoutParams().width, ViewGroup.LayoutParams.WRAP_CONTENT));
                            } else {
                                // 解析数字类型的值
                                String value = layout_height.replaceAll("[^\\d]", "");
                                int height = Integer.parseInt(value);
                                if (layout_height.contains("dp")) {
                                    spinner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(context,height)));
                                } else if (layout_height.contains("px")) {
                                    spinner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
                                }
                            }
                        }
// 解析 layout_width 属性并设置值
                        if (currentLayout == null) {
                            // 如果当前没有 LinearLayout，说明这是根布局的子 View，将其直接添加到根布局中
                            layout.addView(spinner);
                        } else {
                            currentLayout.addView(spinner);
                        }
                    }else if (name.equals("Button")) {
                        Button button = new Button(context);
                        String layout_height = parser.getAttributeValue(null, "layout_height");
                        String layout_width = parser.getAttributeValue(null, "layout_width");
                        String text = parser.getAttributeValue(null, "text");
                        String idName = parser.getAttributeValue(null, "id");
                        System.out.println("========设置的id："+idName);
                        int id = context.getResources().getIdentifier(idName, "id", context.getPackageName());
                        button.setId(id);
                        button.setText(text);
                        Env.getButton_m().put(idName,button);
                        if (layout_width != null) {
                            if (layout_width.equals("match_parent")) {
                                button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                            } else if (layout_width.equals("wrap_content")) {
                                button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            } else {
                                // 解析数字类型的值
                                String value = layout_width.replaceAll("[^\\d]", "");
                                int width = Integer.parseInt(value);
                                if (layout_width.contains("dp")) {
                                    button.setLayoutParams(new LinearLayout.LayoutParams(dpToPx(context,width), ViewGroup.LayoutParams.MATCH_PARENT));
                                } else if (layout_width.contains("px")) {
                                    button.setLayoutParams(new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT));
                                }
                            }
                        }
// 解析 layout_height 属性并设置值
                        if (layout_height != null) {
                            if (layout_height.equals("match_parent")) {
                                button.setLayoutParams(new LinearLayout.LayoutParams(button.getLayoutParams().width, ViewGroup.LayoutParams.MATCH_PARENT));
                            } else if (layout_height.equals("wrap_content")) {
                                button.setLayoutParams(new LinearLayout.LayoutParams(button.getLayoutParams().width, ViewGroup.LayoutParams.WRAP_CONTENT));
                            } else {
                                // 解析数字类型的值
                                String value = layout_height.replaceAll("[^\\d]", "");
                                int height = Integer.parseInt(value);
                                if (layout_height.contains("dp")) {
                                    button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(context,height)));
                                } else if (layout_height.contains("px")) {
                                    button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
                                }
                            }
                        }
                        if (currentLayout == null) {
                            // 如果当前没有 LinearLayout，说明这是根布局的子 View，将其直接添加到根布局中
                            layout.addView(button);
                        } else {
                            currentLayout.addView(button);
                        }
                    }else if (name.equals("EditText")) {
                        EditText edittext = new EditText(context);
                        String layout_height = parser.getAttributeValue(null, "layout_height");
                        String layout_width = parser.getAttributeValue(null, "layout_width");
                        String text = parser.getAttributeValue(null, "text");
                        String idName = parser.getAttributeValue(null, "id");
                        String hint=parser.getAttributeValue(null,"hint");
                        int id = context.getResources().getIdentifier(idName, "id", context.getPackageName());
                        edittext.setId(id);
                        edittext.setText(text);
                        Env.getEdittext_m().put(idName,edittext);
                        if (hint != null) {
                            edittext.setHint(hint);
                        }

                        if (layout_width != null) {
                            if (layout_width.equals("match_parent")) {
                                edittext.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                            } else if (layout_width.equals("wrap_content")) {
                                edittext.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            } else {
                                // 解析数字类型的值
                                String value = layout_width.replaceAll("[^\\d]", "");
                                int width = Integer.parseInt(value);
                                if (layout_width.contains("dp")) {
                                    edittext.setLayoutParams(new LinearLayout.LayoutParams(dpToPx(context,width), ViewGroup.LayoutParams.MATCH_PARENT));
                                } else if (layout_width.contains("px")) {
                                    edittext.setLayoutParams(new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT));
                                }
                            }
                        }
// 解析 layout_height 属性并设置值
                        if (layout_height != null) {
                            if (layout_height.equals("match_parent")) {
                                edittext.setLayoutParams(new LinearLayout.LayoutParams(edittext.getLayoutParams().width, ViewGroup.LayoutParams.MATCH_PARENT));
                            } else if (layout_height.equals("wrap_content")) {
                                edittext.setLayoutParams(new LinearLayout.LayoutParams(edittext.getLayoutParams().width, ViewGroup.LayoutParams.WRAP_CONTENT));
                            } else {
                                // 解析数字类型的值
                                String value = layout_height.replaceAll("[^\\d]", "");
                                int height = Integer.parseInt(value);
                                if (layout_height.contains("dp")) {
                                    edittext.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(context,height)));
                                } else if (layout_height.contains("px")) {
                                    edittext.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
                                }
                            }
                        }

                        if (currentLayout == null) {
                            // 如果当前没有 LinearLayout，说明这是根布局的子 View，将其直接添加到根布局中
                            layout.addView(edittext);
                        } else {
                            currentLayout.addView(edittext);
                        }
                    }
                }
                eventType = parser.next();
            }


        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

// 返回解析后的 View 对象
        return layout;
    }
    public static void showViewOnScreen(View view, Activity ac) {
        ac.setContentView(view);
    }
    public static int dpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density);
    }
}
