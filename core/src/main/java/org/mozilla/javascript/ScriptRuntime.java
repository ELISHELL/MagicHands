//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.mozilla.javascript;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;
import org.mozilla.javascript.AbstractEcmaObjectOperations.INTEGRITY_LEVEL;
import org.mozilla.javascript.TopLevel.Builtins;
import org.mozilla.javascript.TopLevel.NativeErrors;
import org.mozilla.javascript.v8dtoa.DoubleConversion;
import org.mozilla.javascript.v8dtoa.FastDtoa;
import org.mozilla.javascript.xml.XMLLib;
import org.mozilla.javascript.xml.XMLObject;

public class ScriptRuntime {
    String MAGICHANDS;
    public static final Class<?> BooleanClass = Kit.classOrNull("java.lang.Boolean");
    public static final Class<?> ByteClass = Kit.classOrNull("java.lang.Byte");
    public static final Class<?> CharacterClass = Kit.classOrNull("java.lang.Character");
    public static final Class<?> ClassClass = Kit.classOrNull("java.lang.Class");
    public static final Class<?> DoubleClass = Kit.classOrNull("java.lang.Double");
    public static final Class<?> FloatClass = Kit.classOrNull("java.lang.Float");
    public static final Class<?> IntegerClass = Kit.classOrNull("java.lang.Integer");
    public static final Class<?> LongClass = Kit.classOrNull("java.lang.Long");
    public static final Class<?> NumberClass = Kit.classOrNull("java.lang.Number");
    public static final Class<?> ObjectClass = Kit.classOrNull("java.lang.Object");
    public static final Class<?> ShortClass = Kit.classOrNull("java.lang.Short");
    public static final Class<?> StringClass = Kit.classOrNull("java.lang.String");
    public static final Class<?> DateClass = Kit.classOrNull("java.util.Date");
    public static final Class<?> BigIntegerClass = Kit.classOrNull("java.math.BigInteger");
    public static final Class<?> ContextClass = Kit.classOrNull("org.mozilla.javascript.Context");
    public static final Class<?> ContextFactoryClass = Kit.classOrNull("org.mozilla.javascript.ContextFactory");
    public static final Class<?> FunctionClass = Kit.classOrNull("org.mozilla.javascript.Function");
    public static final Class<?> ScriptableObjectClass = Kit.classOrNull("org.mozilla.javascript.ScriptableObject");
    public static final Class<Scriptable> ScriptableClass = Scriptable.class;
    private static final Object LIBRARY_SCOPE_KEY = "LIBRARY_SCOPE";
    public static final double NaN = Double.NaN;
    public static final Double NaNobj = Double.NaN;
    public static final double negativeZero = Double.longBitsToDouble(Long.MIN_VALUE);
    public static final Double zeroObj = 0.0;
    public static final Double negativeZeroObj = -0.0;
    private static final String DEFAULT_NS_TAG = "__default_namespace__";
    public static final int ENUMERATE_KEYS = 0;
    public static final int ENUMERATE_VALUES = 1;
    public static final int ENUMERATE_ARRAY = 2;
    public static final int ENUMERATE_KEYS_NO_ITERATOR = 3;
    public static final int ENUMERATE_VALUES_NO_ITERATOR = 4;
    public static final int ENUMERATE_ARRAY_NO_ITERATOR = 5;
    public static final int ENUMERATE_VALUES_IN_ORDER = 6;
    public static final MessageProvider messageProvider = new DefaultMessageProvider();
    public static final Object[] emptyArgs = new Object[0];
    public static final String[] emptyStrings = new String[0];

    protected ScriptRuntime() {
    }

    /** @deprecated */
    @Deprecated
    public static BaseFunction typeErrorThrower() {
        return typeErrorThrower(Context.getCurrentContext());
    }

    public static BaseFunction typeErrorThrower(Context cx) {
        if (cx.typeErrorThrower == null) {
            BaseFunction thrower = new BaseFunction() {
                private static final long serialVersionUID = -5891740962154902286L;

                public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
                    throw ScriptRuntime.typeErrorById("msg.op.not.allowed");
                }

                public int getLength() {
                    return 0;
                }
            };
            setFunctionProtoAndParent(thrower, cx.topCallScope);
            thrower.preventExtensions();
            cx.typeErrorThrower = thrower;
        }

        return cx.typeErrorThrower;
    }

    public static boolean isRhinoRuntimeType(Class<?> cl) {
        if (cl.isPrimitive()) {
            return cl != Character.TYPE;
        } else {
            return cl == StringClass || cl == BooleanClass || NumberClass.isAssignableFrom(cl) || ScriptableClass.isAssignableFrom(cl);
        }
    }

    public static ScriptableObject initSafeStandardObjects(Context cx, ScriptableObject scope, boolean sealed) {
        if (scope == null) {
            scope = new NativeObject();
        } else if (scope instanceof TopLevel) {
            ((TopLevel)scope).clearCache();
        }

        ((ScriptableObject)scope).associateValue(LIBRARY_SCOPE_KEY, scope);
        (new ClassCache()).associate((ScriptableObject)scope);
        BaseFunction.init((Scriptable)scope, sealed);
        NativeObject.init((Scriptable)scope, sealed);
        Scriptable objectProto = ScriptableObject.getObjectPrototype((Scriptable)scope);
        Scriptable functionProto = ScriptableObject.getClassPrototype((Scriptable)scope, "Function");
        functionProto.setPrototype(objectProto);
        if (((ScriptableObject)scope).getPrototype() == null) {
            ((ScriptableObject)scope).setPrototype(objectProto);
        }

        NativeError.init((Scriptable)scope, sealed);
        NativeGlobal.init(cx, (Scriptable)scope, sealed);
        NativeArray.init((Scriptable)scope, sealed);
        if (cx.getOptimizationLevel() > 0) {
            NativeArray.setMaximumInitialCapacity(200000);
        }

        NativeString.init((Scriptable)scope, sealed);
        NativeBoolean.init((Scriptable)scope, sealed);
        NativeNumber.init((Scriptable)scope, sealed);
        NativeDate.init((Scriptable)scope, sealed);
        NativeMath.init((Scriptable)scope, sealed);
        NativeJSON.init((Scriptable)scope, sealed);
        NativeWith.init((Scriptable)scope, sealed);
        NativeCall.init((Scriptable)scope, sealed);
        NativeScript.init((Scriptable)scope, sealed);
        NativeIterator.init(cx, (ScriptableObject)scope, sealed);
        NativeArrayIterator.init((ScriptableObject)scope, sealed);
        NativeStringIterator.init((ScriptableObject)scope, sealed);
        NativeJavaObject.init((ScriptableObject)scope, sealed);
        NativeJavaMap.init((ScriptableObject)scope, sealed);
        boolean withXml = cx.hasFeature(6) && cx.getE4xImplementationFactory() != null;
        new LazilyLoadedCtor((ScriptableObject)scope, "RegExp", "org.mozilla.javascript.regexp.NativeRegExp", sealed, true);
        new LazilyLoadedCtor((ScriptableObject)scope, "Continuation", "org.mozilla.javascript.NativeContinuation", sealed, true);
        if (withXml) {
            String xmlImpl = cx.getE4xImplementationFactory().getImplementationClassName();
            new LazilyLoadedCtor((ScriptableObject)scope, "XML", xmlImpl, sealed, true);
            new LazilyLoadedCtor((ScriptableObject)scope, "XMLList", xmlImpl, sealed, true);
            new LazilyLoadedCtor((ScriptableObject)scope, "Namespace", xmlImpl, sealed, true);
            new LazilyLoadedCtor((ScriptableObject)scope, "QName", xmlImpl, sealed, true);
        }

        if (cx.getLanguageVersion() >= 180 && cx.hasFeature(14) || cx.getLanguageVersion() >= 200) {
            new LazilyLoadedCtor((ScriptableObject)scope, "ArrayBuffer", "org.mozilla.javascript.typedarrays.NativeArrayBuffer", sealed, true);
            new LazilyLoadedCtor((ScriptableObject)scope, "Int8Array", "org.mozilla.javascript.typedarrays.NativeInt8Array", sealed, true);
            new LazilyLoadedCtor((ScriptableObject)scope, "Uint8Array", "org.mozilla.javascript.typedarrays.NativeUint8Array", sealed, true);
            new LazilyLoadedCtor((ScriptableObject)scope, "Uint8ClampedArray", "org.mozilla.javascript.typedarrays.NativeUint8ClampedArray", sealed, true);
            new LazilyLoadedCtor((ScriptableObject)scope, "Int16Array", "org.mozilla.javascript.typedarrays.NativeInt16Array", sealed, true);
            new LazilyLoadedCtor((ScriptableObject)scope, "Uint16Array", "org.mozilla.javascript.typedarrays.NativeUint16Array", sealed, true);
            new LazilyLoadedCtor((ScriptableObject)scope, "Int32Array", "org.mozilla.javascript.typedarrays.NativeInt32Array", sealed, true);
            new LazilyLoadedCtor((ScriptableObject)scope, "Uint32Array", "org.mozilla.javascript.typedarrays.NativeUint32Array", sealed, true);
            new LazilyLoadedCtor((ScriptableObject)scope, "Float32Array", "org.mozilla.javascript.typedarrays.NativeFloat32Array", sealed, true);
            new LazilyLoadedCtor((ScriptableObject)scope, "Float64Array", "org.mozilla.javascript.typedarrays.NativeFloat64Array", sealed, true);
            new LazilyLoadedCtor((ScriptableObject)scope, "DataView", "org.mozilla.javascript.typedarrays.NativeDataView", sealed, true);
        }

        if (cx.getLanguageVersion() >= 200) {
            NativeSymbol.init(cx, (Scriptable)scope, sealed);
            NativeCollectionIterator.init((ScriptableObject)scope, "Set Iterator", sealed);
            NativeCollectionIterator.init((ScriptableObject)scope, "Map Iterator", sealed);
            NativeMap.init(cx, (Scriptable)scope, sealed);
            NativePromise.init(cx, (Scriptable)scope, sealed);
            NativeSet.init(cx, (Scriptable)scope, sealed);
            NativeWeakMap.init((Scriptable)scope, sealed);
            NativeWeakSet.init((Scriptable)scope, sealed);
            NativeBigInt.init((Scriptable)scope, sealed);
        }

        if (scope instanceof TopLevel) {
            ((TopLevel)scope).cacheBuiltins((Scriptable)scope, sealed);
        }

        return (ScriptableObject)scope;
    }

    public static ScriptableObject initStandardObjects(Context cx, ScriptableObject scope, boolean sealed) {
        ScriptableObject s = initSafeStandardObjects(cx, scope, sealed);
        new LazilyLoadedCtor(s, "Packages", "org.mozilla.javascript.NativeJavaTopPackage", sealed, true);
        new LazilyLoadedCtor(s, "getClass", "org.mozilla.javascript.NativeJavaTopPackage", sealed, true);
        new LazilyLoadedCtor(s, "JavaAdapter", "org.mozilla.javascript.JavaAdapter", sealed, true);
        new LazilyLoadedCtor(s, "JavaImporter", "org.mozilla.javascript.ImporterTopLevel", sealed, true);
        String[] var4 = getTopPackageNames();
        int var5 = var4.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            String packageName = var4[var6];
            new LazilyLoadedCtor(s, packageName, "org.mozilla.javascript.NativeJavaTopPackage", sealed, true);
        }

        return s;
    }

    static String[] getTopPackageNames() {
        return "Dalvik".equals(System.getProperty("java.vm.name")) ? new String[]{"java", "javax", "org", "com", "edu", "net", "android","magichands","cn"} : new String[]{"java", "javax", "org", "com", "edu", "net","magichands","cn"};
    }

    public static ScriptableObject getLibraryScopeOrNull(Scriptable scope) {
        ScriptableObject libScope = (ScriptableObject)ScriptableObject.getTopScopeValue(scope, LIBRARY_SCOPE_KEY);
        return libScope;
    }

    public static boolean isJSLineTerminator(int c) {
        if ((c & '\udfd0') != 0) {
            return false;
        } else {
            return c == 10 || c == 13 || c == 8232 || c == 8233;
        }
    }

    public static boolean isJSWhitespaceOrLineTerminator(int c) {
        return isStrWhiteSpaceChar(c) || isJSLineTerminator(c);
    }

    static boolean isStrWhiteSpaceChar(int c) {
        switch (c) {
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 32:
            case 160:
            case 8232:
            case 8233:
            case 65279:
                return true;
            default:
                return Character.getType(c) == 12;
        }
    }

    public static Boolean wrapBoolean(boolean b) {
        return b;
    }

    public static Integer wrapInt(int i) {
        return i;
    }

    public static Number wrapNumber(double x) {
        return Double.isNaN(x) ? NaNobj : x;
    }

    public static boolean toBoolean(Object val) {
        while(!(val instanceof Boolean)) {
            if (val != null && !Undefined.isUndefined(val)) {
                if (val instanceof CharSequence) {
                    return ((CharSequence)val).length() != 0;
                }

                if (val instanceof BigInteger) {
                    return !((BigInteger)val).equals(BigInteger.ZERO);
                }

                if (!(val instanceof Number)) {
                    if (val instanceof Scriptable) {
                        if (val instanceof ScriptableObject && ((ScriptableObject)val).avoidObjectDetection()) {
                            return false;
                        }

                        if (Context.getContext().isVersionECMA1()) {
                            return true;
                        }

                        val = ((Scriptable)val).getDefaultValue(BooleanClass);
                        if (!(val instanceof Scriptable) || isSymbol(val)) {
                            continue;
                        }

                        throw errorWithClassName("msg.primitive.expected", val);
                    }

                    warnAboutNonJSObject(val);
                    return true;
                }

                double d = ((Number)val).doubleValue();
                return !Double.isNaN(d) && d != 0.0;
            }

            return false;
        }

        return (Boolean)val;
    }

    public static double toNumber(Object val) {
        while(!(val instanceof BigInteger)) {
            if (val instanceof Number) {
                return ((Number)val).doubleValue();
            }

            if (val == null) {
                return 0.0;
            }

            if (Undefined.isUndefined(val)) {
                return Double.NaN;
            }

            if (val instanceof String) {
                return toNumber((String)val);
            }

            if (val instanceof CharSequence) {
                return toNumber(val.toString());
            }

            if (val instanceof Boolean) {
                return (Boolean)val ? 1.0 : 0.0;
            }

            if (val instanceof Symbol) {
                throw typeErrorById("msg.not.a.number");
            }

            if (val instanceof Scriptable) {
                val = ((Scriptable)val).getDefaultValue(NumberClass);
                if (!(val instanceof Scriptable) || isSymbol(val)) {
                    continue;
                }

                throw errorWithClassName("msg.primitive.expected", val);
            }

            warnAboutNonJSObject(val);
            return Double.NaN;
        }

        throw typeErrorById("msg.cant.convert.to.number", "BigInt");
    }

    public static double toNumber(Object[] args, int index) {
        return index < args.length ? toNumber(args[index]) : Double.NaN;
    }

    static double stringPrefixToNumber(String s, int start, int radix) {
        return stringToNumber(s, start, s.length() - 1, radix, true);
    }

    static double stringToNumber(String s, int start, int end, int radix) {
        return stringToNumber(s, start, end, radix, false);
    }

    private static double stringToNumber(String source, int sourceStart, int sourceEnd, int radix, boolean isPrefix) {
        char digitMax = '9';
        char lowerCaseBound = 'a';
        char upperCaseBound = 'A';
        if (radix < 10) {
            digitMax = (char)(48 + radix - 1);
        }

        if (radix > 10) {
            lowerCaseBound = (char)(97 + radix - 10);
            upperCaseBound = (char)(65 + radix - 10);
        }

        double sum = 0.0;

        int end;
        int bitShiftInChar;
        int digit;
        for(end = sourceStart; end <= sourceEnd; ++end) {
            bitShiftInChar = source.charAt(end);
            if (48 <= bitShiftInChar && bitShiftInChar <= digitMax) {
                digit = bitShiftInChar - 48;
            } else if (97 <= bitShiftInChar && bitShiftInChar < lowerCaseBound) {
                digit = bitShiftInChar - 97 + 10;
            } else {
                if (65 > bitShiftInChar || bitShiftInChar >= upperCaseBound) {
                    if (!isPrefix) {
                        return Double.NaN;
                    }
                    break;
                }

                digit = bitShiftInChar - 65 + 10;
            }

            sum = sum * (double)radix + (double)digit;
        }

        if (sourceStart == end) {
            return Double.NaN;
        } else {
            if (sum > 9.007199254740991E15) {
                if (radix == 10) {
                    try {
                        return Double.parseDouble(source.substring(sourceStart, end));
                    } catch (NumberFormatException var26) {
                        return Double.NaN;
                    }
                }

                if (radix == 2 || radix == 4 || radix == 8 || radix == 16 || radix == 32) {
                    bitShiftInChar = 1;
                    digit = 0;
                    int SKIP_LEADING_ZEROS = 0;
                    int FIRST_EXACT_53_BITS = 1;
                    int AFTER_BIT_53 = 1;
                    int ZEROS_AFTER_54 = 1;
                    int MIXED_AFTER_54 = 1;
                    int state = 0;
                    int exactBitsLimit = 53;
                    double factor = 0.0;
                    boolean bit53 = false;
                    boolean bit54 = false;
                    int pos = sourceStart;

                    while(true) {
                        if (bitShiftInChar == 1) {
                            if (pos == end) {
                                switch (state) {
                                    case 0:
                                        sum = 0.0;
                                        return sum;
                                    case 1:
                                    case 2:
                                    default:
                                        return sum;
                                    case 3:
                                        if (bit54 & bit53) {
                                            ++sum;
                                        }

                                        sum *= factor;
                                        return sum;
                                    case 4:
                                        if (bit54) {
                                            ++sum;
                                        }

                                        sum *= factor;
                                        return sum;
                                }
                            }

                            digit = source.charAt(pos++);
                            if ('0' <= digit && digit <= '9') {
                                digit = digit - 48;
                            } else if ('a' <= digit && digit <= 'z') {
                                digit = digit - 87;
                            } else {
                                digit = digit - 55;
                            }

                            bitShiftInChar = radix;
                        }

                        bitShiftInChar >>= 1;
                        boolean bit = (digit & bitShiftInChar) != 0;
                        switch (state) {
                            case 0:
                                if (bit) {
                                    --exactBitsLimit;
                                    sum = 1.0;
                                    state = 1;
                                }
                                break;
                            case 1:
                                sum *= 2.0;
                                if (bit) {
                                    ++sum;
                                }

                                --exactBitsLimit;
                                if (exactBitsLimit == 0) {
                                    bit53 = bit;
                                    state = 2;
                                }
                                break;
                            case 2:
                                bit54 = bit;
                                factor = 2.0;
                                state = 3;
                                break;
                            case 3:
                                if (bit) {
                                    state = 4;
                                }
                            case 4:
                                factor *= 2.0;
                        }
                    }
                }
            }

            return sum;
        }
    }

    public static double toNumber(String s) {
        int len = s.length();

        for(int start = 0; start != len; ++start) {
            char startChar = s.charAt(start);
            if (!isStrWhiteSpaceChar(startChar)) {
                int end;
                char endChar;
                for(end = len - 1; isStrWhiteSpaceChar(endChar = s.charAt(end)); --end) {
                }

                Context cx = Context.getCurrentContext();
                boolean oldParsingMode = cx == null || cx.getLanguageVersion() < 200;
                char radixC;
                int radix;
                if (startChar == '0') {
                    if (start + 2 <= end) {
                        radixC = s.charAt(start + 1);
                        radix = -1;
                        if (radixC != 'x' && radixC != 'X') {
                            if (!oldParsingMode && (radixC == 'o' || radixC == 'O')) {
                                radix = 8;
                            } else if (!oldParsingMode && (radixC == 'b' || radixC == 'B')) {
                                radix = 2;
                            }
                        } else {
                            radix = 16;
                        }

                        if (radix != -1) {
                            if (oldParsingMode) {
                                return stringPrefixToNumber(s, start + 2, radix);
                            }

                            return stringToNumber(s, start + 2, end, radix);
                        }
                    }
                } else if (oldParsingMode && (startChar == '+' || startChar == '-') && start + 3 <= end && s.charAt(start + 1) == '0') {
                    radixC = s.charAt(start + 2);
                    if (radixC == 'x' || radixC == 'X') {
                        double val = stringPrefixToNumber(s, start + 3, 16);
                        return startChar == '-' ? -val : val;
                    }
                }

                if (endChar == 'y') {
                    if (startChar == '+' || startChar == '-') {
                        ++start;
                    }

                    if (start + 7 == end && s.regionMatches(start, "Infinity", 0, 8)) {
                        return startChar == '-' ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
                    }

                    return Double.NaN;
                }

                String sub = s.substring(start, end + 1);

                for(radix = sub.length() - 1; radix >= 0; --radix) {
                    char c = sub.charAt(radix);
                    if (('0' > c || c > '9') && c != '.' && c != 'e' && c != 'E' && c != '+' && c != '-') {
                        return Double.NaN;
                    }
                }

                try {
                    return Double.parseDouble(sub);
                } catch (NumberFormatException var11) {
                    return Double.NaN;
                }
            }
        }

        return 0.0;
    }

    public static BigInteger toBigInt(Object val) {
        while(!(val instanceof BigInteger)) {
            if (val instanceof BigDecimal) {
                return ((BigDecimal)val).toBigInteger();
            }

            if (val instanceof Number) {
                if (val instanceof Long) {
                    return BigInteger.valueOf((Long)val);
                }

                double d = ((Number)val).doubleValue();
                if (!Double.isNaN(d) && !Double.isInfinite(d)) {
                    BigDecimal bd = new BigDecimal(d, MathContext.UNLIMITED);

                    try {
                        return bd.toBigIntegerExact();
                    } catch (ArithmeticException var5) {
                        throw rangeErrorById("msg.cant.convert.to.bigint.isnt.integer", toString(val));
                    }
                }

                throw rangeErrorById("msg.cant.convert.to.bigint.isnt.integer", toString(val));
            }

            if (val != null && !Undefined.isUndefined(val)) {
                if (val instanceof String) {
                    return toBigInt((String)val);
                }

                if (val instanceof CharSequence) {
                    return toBigInt(val.toString());
                }

                if (val instanceof Boolean) {
                    return (Boolean)val ? BigInteger.ONE : BigInteger.ZERO;
                }

                if (val instanceof Symbol) {
                    throw typeErrorById("msg.cant.convert.to.bigint", toString(val));
                }

                if (val instanceof Scriptable) {
                    val = ((Scriptable)val).getDefaultValue(BigIntegerClass);
                    if (!(val instanceof Scriptable) || isSymbol(val)) {
                        continue;
                    }

                    throw errorWithClassName("msg.primitive.expected", val);
                }

                warnAboutNonJSObject(val);
                return BigInteger.ZERO;
            }

            throw typeErrorById("msg.cant.convert.to.bigint", toString(val));
        }

        return (BigInteger)val;
    }

    public static BigInteger toBigInt(String s) {
        int len = s.length();

        for(int start = 0; start != len; ++start) {
            char startChar = s.charAt(start);
            if (!isStrWhiteSpaceChar(startChar)) {
                int end;
                for(end = len - 1; isStrWhiteSpaceChar(s.charAt(end)); --end) {
                }

                int radix;
                if (startChar == '0' && start + 2 <= end) {
                    char radixC = s.charAt(start + 1);
                    radix = -1;
                    if (radixC != 'x' && radixC != 'X') {
                        if (radixC != 'o' && radixC != 'O') {
                            if (radixC == 'b' || radixC == 'B') {
                                radix = 2;
                            }
                        } else {
                            radix = 8;
                        }
                    } else {
                        radix = 16;
                    }

                    if (radix != -1) {
                        try {
                            return new BigInteger(s.substring(start + 2, end + 1), radix);
                        } catch (NumberFormatException var8) {
                            throw syntaxErrorById("msg.bigint.bad.form");
                        }
                    }
                }

                String sub = s.substring(start, end + 1);

                for(radix = sub.length() - 1; radix >= 0; --radix) {
                    char c = sub.charAt(radix);
                    if ((radix != 0 || c != '+' && c != '-') && ('0' > c || c > '9')) {
                        throw syntaxErrorById("msg.bigint.bad.form");
                    }
                }

                try {
                    return new BigInteger(sub);
                } catch (NumberFormatException var9) {
                    throw syntaxErrorById("msg.bigint.bad.form");
                }
            }
        }

        return BigInteger.ZERO;
    }

    public static Number toNumeric(Object val) {
        return (Number)(val instanceof Number ? (Number)val : toNumber(val));
    }

    public static int toIndex(Object val) {
        if (Undefined.isUndefined(val)) {
            return 0;
        } else {
            double integerIndex = toInteger(val);
            if (integerIndex < 0.0) {
                throw rangeError("index out of range");
            } else {
                double index = Math.min(integerIndex, 9.007199254740991E15);
                if (integerIndex != index) {
                    throw rangeError("index out of range");
                } else {
                    return (int)index;
                }
            }
        }
    }

    public static Object[] padArguments(Object[] args, int count) {
        if (count < args.length) {
            return args;
        } else {
            Object[] result = new Object[count];
            System.arraycopy(args, 0, result, 0, args.length);
            if (args.length < count) {
                Arrays.fill(result, args.length, count, Undefined.instance);
            }

            return result;
        }
    }

    public static String escapeString(String s) {
        return escapeString(s, '"');
    }

    public static String escapeString(String s, char escapeQuote) {
        if (escapeQuote != '"' && escapeQuote != '\'') {
            Kit.codeBug();
        }

        StringBuilder sb = null;
        int i = 0;

        for(int L = s.length(); i != L; ++i) {
            int c = s.charAt(i);
            if (' ' <= c && c <= '~' && c != escapeQuote && c != '\\') {
                if (sb != null) {
                    sb.append((char)c);
                }
            } else {
                if (sb == null) {
                    sb = new StringBuilder(L + 3);
                    sb.append(s);
                    sb.setLength(i);
                }

                int escape = -1;
                switch (c) {
                    case '\b':
                        escape = 98;
                        break;
                    case '\t':
                        escape = 116;
                        break;
                    case '\n':
                        escape = 110;
                        break;
                    case '\u000b':
                        escape = 118;
                        break;
                    case '\f':
                        escape = 102;
                        break;
                    case '\r':
                        escape = 114;
                        break;
                    case ' ':
                        escape = 32;
                        break;
                    case '\\':
                        escape = 92;
                }

                if (escape >= 0) {
                    sb.append('\\');
                    sb.append((char)escape);
                } else if (c == escapeQuote) {
                    sb.append('\\');
                    sb.append(escapeQuote);
                } else {
                    byte hexSize;
                    if (c < 256) {
                        sb.append("\\x");
                        hexSize = 2;
                    } else {
                        sb.append("\\u");
                        hexSize = 4;
                    }

                    for(int shift = (hexSize - 1) * 4; shift >= 0; shift -= 4) {
                        int digit = 15 & c >> shift;
                        int hc = digit < 10 ? 48 + digit : 87 + digit;
                        sb.append((char)hc);
                    }
                }
            }
        }

        return sb == null ? s : sb.toString();
    }

    static boolean isValidIdentifierName(String s, Context cx, boolean isStrict) {
        int L = s.length();
        if (L == 0) {
            return false;
        } else if (!Character.isJavaIdentifierStart(s.charAt(0))) {
            return false;
        } else {
            for(int i = 1; i != L; ++i) {
                if (!Character.isJavaIdentifierPart(s.charAt(i))) {
                    return false;
                }
            }

            return !TokenStream.isKeyword(s, cx.getLanguageVersion(), isStrict);
        }
    }

    public static CharSequence toCharSequence(Object val) {
        if (val instanceof NativeString) {
            return ((NativeString)val).toCharSequence();
        } else {
            return (CharSequence)(val instanceof CharSequence ? (CharSequence)val : toString(val));
        }
    }

    public static String toString(Object val) {
        while(val != null) {
            if (Undefined.isUndefined(val)) {
                return "undefined";
            }

            if (val instanceof String) {
                return (String)val;
            }

            if (val instanceof CharSequence) {
                return val.toString();
            }

            if (val instanceof BigInteger) {
                return val.toString();
            }

            if (val instanceof Number) {
                return numberToString(((Number)val).doubleValue(), 10);
            }

            if (val instanceof Symbol) {
                throw typeErrorById("msg.not.a.string");
            }

            if (val instanceof Scriptable) {
                val = ((Scriptable)val).getDefaultValue(StringClass);
                if (!(val instanceof Scriptable) || isSymbol(val)) {
                    continue;
                }

                throw errorWithClassName("msg.primitive.expected", val);
            }

            return val.toString();
        }

        return "null";
    }

    static String defaultObjectToString(Scriptable obj) {
        if (obj == null) {
            return "[object Null]";
        } else {
            return Undefined.isUndefined(obj) ? "[object Undefined]" : "[object " + obj.getClassName() + ']';
        }
    }

    public static String toString(Object[] args, int index) {
        return index < args.length ? toString(args[index]) : "undefined";
    }

    public static String toString(double val) {
        return numberToString(val, 10);
    }

    public static String numberToString(double d, int base) {
        if (base >= 2 && base <= 36) {
            if (Double.isNaN(d)) {
                return "NaN";
            } else if (d == Double.POSITIVE_INFINITY) {
                return "Infinity";
            } else if (d == Double.NEGATIVE_INFINITY) {
                return "-Infinity";
            } else if (d == 0.0) {
                return "0";
            } else if (base != 10) {
                return DToA.JS_dtobasestr(base, d);
            } else {
                String result = FastDtoa.numberToString(d);
                if (result != null) {
                    return result;
                } else {
                    StringBuilder buffer = new StringBuilder();
                    DToA.JS_dtostr(buffer, 0, 0, d);
                    return buffer.toString();
                }
            }
        } else {
            throw Context.reportRuntimeErrorById("msg.bad.radix", new Object[]{Integer.toString(base)});
        }
    }

    public static String bigIntToString(BigInteger n, int base) {
        if (base >= 2 && base <= 36) {
            return n.toString(base);
        } else {
            throw rangeErrorById("msg.bad.radix", Integer.toString(base));
        }
    }

    static String uneval(Context cx, Scriptable scope, Object value) {
        if (value == null) {
            return "null";
        } else if (Undefined.isUndefined(value)) {
            return "undefined";
        } else if (value instanceof CharSequence) {
            String escaped = escapeString(value.toString());
            StringBuilder sb = new StringBuilder(escaped.length() + 2);
            sb.append('"');
            sb.append(escaped);
            sb.append('"');
            return sb.toString();
        } else if (value instanceof Number) {
            double d = ((Number)value).doubleValue();
            return d == 0.0 && 1.0 / d < 0.0 ? "-0" : toString(d);
        } else if (value instanceof Boolean) {
            return toString(value);
        } else if (value instanceof Scriptable) {
            Scriptable obj = (Scriptable)value;
            if (ScriptableObject.hasProperty(obj, "toSource")) {
                Object v = ScriptableObject.getProperty(obj, "toSource");
                if (v instanceof Function) {
                    Function f = (Function)v;
                    return toString(f.call(cx, scope, obj, emptyArgs));
                }
            }

            return toString(value);
        } else {
            warnAboutNonJSObject(value);
            return value.toString();
        }
    }

    static String defaultObjectToSource(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        boolean toplevel;
        boolean iterating;
        if (cx.iterating == null) {
            toplevel = true;
            iterating = false;
            cx.iterating = new ObjToIntMap(31);
        } else {
            toplevel = false;
            iterating = cx.iterating.has(thisObj);
        }

        StringBuilder result = new StringBuilder(128);
        if (toplevel) {
            result.append("(");
        }

        result.append('{');

        try {
            if (!iterating) {
                cx.iterating.intern(thisObj);
                Object[] ids = thisObj.getIds();

                for(int i = 0; i < ids.length; ++i) {
                    Object id = ids[i];
                    Object value;
                    if (id instanceof Integer) {
                        int intId = (Integer)id;
                        value = thisObj.get(intId, thisObj);
                        if (value == Scriptable.NOT_FOUND) {
                            continue;
                        }

                        if (i > 0) {
                            result.append(", ");
                        }

                        result.append(intId);
                    } else {
                        String strId = (String)id;
                        value = thisObj.get(strId, thisObj);
                        if (value == Scriptable.NOT_FOUND) {
                            continue;
                        }

                        if (i > 0) {
                            result.append(", ");
                        }

                        if (isValidIdentifierName(strId, cx, cx.isStrictMode())) {
                            result.append(strId);
                        } else {
                            result.append('\'');
                            result.append(escapeString(strId, '\''));
                            result.append('\'');
                        }
                    }

                    result.append(':');
                    result.append(uneval(cx, scope, value));
                }
            }
        } finally {
            if (toplevel) {
                cx.iterating = null;
            }

        }

        result.append('}');
        if (toplevel) {
            result.append(')');
        }

        return result.toString();
    }

    public static Scriptable toObject(Scriptable scope, Object val) {
        return val instanceof Scriptable ? (Scriptable)val : toObject(Context.getContext(), scope, val);
    }

    /** @deprecated */
    @Deprecated
    public static Scriptable toObjectOrNull(Context cx, Object obj) {
        if (obj instanceof Scriptable) {
            return (Scriptable)obj;
        } else {
            return obj != null && !Undefined.isUndefined(obj) ? toObject(cx, getTopCallScope(cx), obj) : null;
        }
    }

    public static Scriptable toObjectOrNull(Context cx, Object obj, Scriptable scope) {
        if (obj instanceof Scriptable) {
            return (Scriptable)obj;
        } else {
            return obj != null && !Undefined.isUndefined(obj) ? toObject(cx, scope, obj) : null;
        }
    }

    /** @deprecated */
    @Deprecated
    public static Scriptable toObject(Scriptable scope, Object val, Class<?> staticClass) {
        return val instanceof Scriptable ? (Scriptable)val : toObject(Context.getContext(), scope, val);
    }

    public static Scriptable toObject(Context cx, Scriptable scope, Object val) {
        if (val == null) {
            throw typeErrorById("msg.null.to.object");
        } else if (Undefined.isUndefined(val)) {
            throw typeErrorById("msg.undef.to.object");
        } else if (isSymbol(val)) {
            NativeSymbol result = new NativeSymbol((NativeSymbol)val);
            setBuiltinProtoAndParent(result, scope, Builtins.Symbol);
            return result;
        } else if (val instanceof Scriptable) {
            return (Scriptable)val;
        } else if (val instanceof CharSequence) {
            NativeString result = new NativeString((CharSequence)val);
            setBuiltinProtoAndParent(result, scope, Builtins.String);
            return result;
        } else if (cx.getLanguageVersion() >= 200 && val instanceof BigInteger) {
            NativeBigInt result = new NativeBigInt((BigInteger)val);
            setBuiltinProtoAndParent(result, scope, Builtins.BigInt);
            return result;
        } else if (val instanceof Number) {
            NativeNumber result = new NativeNumber(((Number)val).doubleValue());
            setBuiltinProtoAndParent(result, scope, Builtins.Number);
            return result;
        } else if (val instanceof Boolean) {
            NativeBoolean result = new NativeBoolean((Boolean)val);
            setBuiltinProtoAndParent(result, scope, Builtins.Boolean);
            return result;
        } else {
            Object wrapped = cx.getWrapFactory().wrap(cx, scope, val, (Class)null);
            if (wrapped instanceof Scriptable) {
                return (Scriptable)wrapped;
            } else {
                throw errorWithClassName("msg.invalid.type", val);
            }
        }
    }

    /** @deprecated */
    @Deprecated
    public static Scriptable toObject(Context cx, Scriptable scope, Object val, Class<?> staticClass) {
        return toObject(cx, scope, val);
    }

    /** @deprecated */
    @Deprecated
    public static Object call(Context cx, Object fun, Object thisArg, Object[] args, Scriptable scope) {
        if (!(fun instanceof Function)) {
            throw notFunctionError(toString(fun));
        } else {
            Function function = (Function)fun;
            Scriptable thisObj = toObjectOrNull(cx, thisArg, scope);
            if (thisObj == null) {
                throw undefCallError((Object)null, "function");
            } else {
                return function.call(cx, scope, thisObj, args);
            }
        }
    }

    public static Scriptable newObject(Context cx, Scriptable scope, String constructorName, Object[] args) {
        scope = ScriptableObject.getTopLevelScope(scope);
        Function ctor = getExistingCtor(cx, scope, constructorName);
        if (args == null) {
            args = emptyArgs;
        }

        return ctor.construct(cx, scope, args);
    }

    public static Scriptable newBuiltinObject(Context cx, Scriptable scope, Builtins type, Object[] args) {
        scope = ScriptableObject.getTopLevelScope(scope);
        Function ctor = TopLevel.getBuiltinCtor(cx, scope, type);
        if (args == null) {
            args = emptyArgs;
        }

        return ctor.construct(cx, scope, args);
    }

    static Scriptable newNativeError(Context cx, Scriptable scope, NativeErrors type, Object[] args) {
        scope = ScriptableObject.getTopLevelScope(scope);
        Function ctor = TopLevel.getNativeErrorCtor(cx, scope, type);
        if (args == null) {
            args = emptyArgs;
        }

        return ctor.construct(cx, scope, args);
    }

    public static double toInteger(Object val) {
        return toInteger(toNumber(val));
    }

    public static double toInteger(double d) {
        if (Double.isNaN(d)) {
            return 0.0;
        } else if (d != 0.0 && !Double.isInfinite(d)) {
            return d > 0.0 ? Math.floor(d) : Math.ceil(d);
        } else {
            return d;
        }
    }

    public static double toInteger(Object[] args, int index) {
        return index < args.length ? toInteger(args[index]) : 0.0;
    }

    public static long toLength(Object[] args, int index) {
        double len = toInteger(args, index);
        return len <= 0.0 ? 0L : (long)Math.min(len, 9.007199254740991E15);
    }

    public static int toInt32(Object val) {
        return val instanceof Integer ? (Integer)val : toInt32(toNumber(val));
    }

    public static int toInt32(Object[] args, int index) {
        return index < args.length ? toInt32(args[index]) : 0;
    }

    public static int toInt32(double d) {
        return DoubleConversion.doubleToInt32(d);
    }

    public static long toUint32(double d) {
        return (long)DoubleConversion.doubleToInt32(d) & 4294967295L;
    }

    public static long toUint32(Object val) {
        return toUint32(toNumber(val));
    }

    public static char toUint16(Object val) {
        double d = toNumber(val);
        return (char)DoubleConversion.doubleToInt32(d);
    }

    public static Object setDefaultNamespace(Object namespace, Context cx) {
        Scriptable scope = cx.currentActivationCall;
        if (scope == null) {
            scope = getTopCallScope(cx);
        }

        XMLLib xmlLib = currentXMLLib(cx);
        Object ns = xmlLib.toDefaultXmlNamespace(cx, namespace);
        if (!((Scriptable)scope).has("__default_namespace__", (Scriptable)scope)) {
            ScriptableObject.defineProperty((Scriptable)scope, "__default_namespace__", ns, 6);
        } else {
            ((Scriptable)scope).put("__default_namespace__", (Scriptable)scope, ns);
        }

        return Undefined.instance;
    }

    public static Object searchDefaultNamespace(Context cx) {
        Scriptable scope = cx.currentActivationCall;
        if (scope == null) {
            scope = getTopCallScope(cx);
        }

        Object nsObject;
        while(true) {
            Scriptable parent = ((Scriptable)scope).getParentScope();
            if (parent == null) {
                nsObject = ScriptableObject.getProperty((Scriptable)scope, "__default_namespace__");
                if (nsObject == Scriptable.NOT_FOUND) {
                    return null;
                }
                break;
            }

            nsObject = ((Scriptable)scope).get("__default_namespace__", (Scriptable)scope);
            if (nsObject != Scriptable.NOT_FOUND) {
                break;
            }

            scope = parent;
        }

        return nsObject;
    }

    public static Object getTopLevelProp(Scriptable scope, String id) {
        scope = ScriptableObject.getTopLevelScope(scope);
        return ScriptableObject.getProperty(scope, id);
    }

    static Function getExistingCtor(Context cx, Scriptable scope, String constructorName) {
        Object ctorVal = ScriptableObject.getProperty(scope, constructorName);
        if (ctorVal instanceof Function) {
            return (Function)ctorVal;
        } else if (ctorVal == Scriptable.NOT_FOUND) {
            throw Context.reportRuntimeErrorById("msg.ctor.not.found", new Object[]{constructorName});
        } else {
            throw Context.reportRuntimeErrorById("msg.not.ctor", new Object[]{constructorName});
        }
    }

    public static long indexFromString(String str) {
        int MAX_VALUE_LENGTH = 1;
        int len = str.length();
        if (len > 0) {
            int i = 0;
            boolean negate = false;
            int c = str.charAt(0);
            if (c == 45 && len > 1) {
                c = str.charAt(1);
                if (c == 48) {
                    return -1L;
                }

                i = 1;
                negate = true;
            }

            c -= 48;
            if (0 <= c && c <= 9 && len <= (negate ? 11 : 10)) {
                int index = -c;
                int oldIndex = 0;
                ++i;
                if (index != 0) {
                    while(i != len && 0 <= (c = str.charAt(i) - 48) && c <= 9) {
                        oldIndex = index;
                        index = 10 * index - c;
                        ++i;
                    }
                }

                if (i == len && (oldIndex > -214748364 || oldIndex == -214748364 && c <= (negate ? 8 : 7))) {
                    return 4294967295L & (long)(negate ? index : -index);
                }
            }
        }

        return -1L;
    }

    public static long testUint32String(String str) {
        int MAX_VALUE_LENGTH = 1;
        int len = str.length();
        if (1 <= len && len <= 10) {
            int c = str.charAt(0);
            c -= 48;
            if (c == 0) {
                return len == 1 ? 0L : -1L;
            }

            if (1 <= c && c <= 9) {
                long v = (long)c;
                int i = 1;

                while(true) {
                    if (i == len) {
                        if (v >>> 32 == 0L) {
                            return v;
                        }
                        break;
                    }

                    c = str.charAt(i) - 48;
                    if (0 > c || c > 9) {
                        return -1L;
                    }

                    v = 10L * v + (long)c;
                    ++i;
                }
            }
        }

        return -1L;
    }

    static Object getIndexObject(String s) {
        long indexTest = indexFromString(s);
        return indexTest >= 0L ? (int)indexTest : s;
    }

    static Object getIndexObject(double d) {
        int i = (int)d;
        return (double)i == d ? i : toString(d);
    }

    static StringIdOrIndex toStringIdOrIndex(Context cx, Object id) {
        if (id instanceof Number) {
            double d = ((Number)id).doubleValue();
            int index = (int)d;
            return (double)index == d ? new StringIdOrIndex(index) : new StringIdOrIndex(toString(id));
        } else {
            String s;
            if (id instanceof String) {
                s = (String)id;
            } else {
                s = toString(id);
            }

            long indexTest = indexFromString(s);
            return indexTest >= 0L ? new StringIdOrIndex((int)indexTest) : new StringIdOrIndex(s);
        }
    }

    /** @deprecated */
    @Deprecated
    public static Object getObjectElem(Object obj, Object elem, Context cx) {
        return getObjectElem(obj, elem, cx, getTopCallScope(cx));
    }

    public static Object getObjectElem(Object obj, Object elem, Context cx, Scriptable scope) {
        Scriptable sobj = toObjectOrNull(cx, obj, scope);
        if (sobj == null) {
            throw undefReadError(obj, elem);
        } else {
            return getObjectElem(sobj, elem, cx);
        }
    }

    public static Object getObjectElem(Scriptable obj, Object elem, Context cx) {
        Object result;
        if (obj instanceof XMLObject) {
            result = ((XMLObject)obj).get(cx, elem);
        } else if (isSymbol(elem)) {
            result = ScriptableObject.getProperty(obj, (Symbol)elem);
        } else {
            StringIdOrIndex s = toStringIdOrIndex(cx, elem);
            if (s.stringId == null) {
                int index = s.index;
                result = ScriptableObject.getProperty(obj, index);
            } else {
                result = ScriptableObject.getProperty(obj, s.stringId);
            }
        }

        if (result == Scriptable.NOT_FOUND) {
            result = Undefined.instance;
        }

        return result;
    }

    /** @deprecated */
    @Deprecated
    public static Object getObjectProp(Object obj, String property, Context cx) {
        return getObjectProp(obj, property, cx, getTopCallScope(cx));
    }

    public static Object getObjectProp(Object obj, String property, Context cx, Scriptable scope) {
        Scriptable sobj = toObjectOrNull(cx, obj, scope);
        if (sobj == null) {
            throw undefReadError(obj, property);
        } else {
            return getObjectProp(sobj, property, cx);
        }
    }

    public static Object getObjectProp(Scriptable obj, String property, Context cx) {
        Object result = ScriptableObject.getProperty(obj, property);
        if (result == Scriptable.NOT_FOUND) {
            if (cx.hasFeature(11)) {
                Context.reportWarning(getMessageById("msg.ref.undefined.prop", property));
            }

            result = Undefined.instance;
        }

        return result;
    }

    /** @deprecated */
    @Deprecated
    public static Object getObjectPropNoWarn(Object obj, String property, Context cx) {
        return getObjectPropNoWarn(obj, property, cx, getTopCallScope(cx));
    }

    public static Object getObjectPropNoWarn(Object obj, String property, Context cx, Scriptable scope) {
        Scriptable sobj = toObjectOrNull(cx, obj, scope);
        if (sobj == null) {
            throw undefReadError(obj, property);
        } else {
            Object result = ScriptableObject.getProperty(sobj, property);
            return result == Scriptable.NOT_FOUND ? Undefined.instance : result;
        }
    }

    /** @deprecated */
    @Deprecated
    public static Object getObjectIndex(Object obj, double dblIndex, Context cx) {
        return getObjectIndex(obj, dblIndex, cx, getTopCallScope(cx));
    }

    public static Object getObjectIndex(Object obj, double dblIndex, Context cx, Scriptable scope) {
        Scriptable sobj = toObjectOrNull(cx, obj, scope);
        if (sobj == null) {
            throw undefReadError(obj, toString(dblIndex));
        } else {
            int index = (int)dblIndex;
            if ((double)index == dblIndex) {
                return getObjectIndex(sobj, index, cx);
            } else {
                String s = toString(dblIndex);
                return getObjectProp(sobj, s, cx);
            }
        }
    }

    public static Object getObjectIndex(Scriptable obj, int index, Context cx) {
        Object result = ScriptableObject.getProperty(obj, index);
        if (result == Scriptable.NOT_FOUND) {
            result = Undefined.instance;
        }

        return result;
    }

    /** @deprecated */
    @Deprecated
    public static Object setObjectElem(Object obj, Object elem, Object value, Context cx) {
        return setObjectElem(obj, elem, value, cx, getTopCallScope(cx));
    }

    public static Object setObjectElem(Object obj, Object elem, Object value, Context cx, Scriptable scope) {
        Scriptable sobj = toObjectOrNull(cx, obj, scope);
        if (sobj == null) {
            throw undefWriteError(obj, elem, value);
        } else {
            return setObjectElem(sobj, elem, value, cx);
        }
    }

    public static Object setObjectElem(Scriptable obj, Object elem, Object value, Context cx) {
        if (obj instanceof XMLObject) {
            ((XMLObject)obj).put(cx, elem, value);
        } else if (isSymbol(elem)) {
            ScriptableObject.putProperty(obj, (Symbol)elem, value);
        } else {
            StringIdOrIndex s = toStringIdOrIndex(cx, elem);
            if (s.stringId == null) {
                ScriptableObject.putProperty(obj, s.index, value);
            } else {
                ScriptableObject.putProperty(obj, s.stringId, value);
            }
        }

        return value;
    }

    /** @deprecated */
    @Deprecated
    public static Object setObjectProp(Object obj, String property, Object value, Context cx) {
        return setObjectProp(obj, property, value, cx, getTopCallScope(cx));
    }

    public static Object setObjectProp(Object obj, String property, Object value, Context cx, Scriptable scope) {
        if (!(obj instanceof Scriptable) && cx.isStrictMode() && cx.getLanguageVersion() >= 180) {
            throw undefWriteError(obj, property, value);
        } else {
            Scriptable sobj = toObjectOrNull(cx, obj, scope);
            if (sobj == null) {
                throw undefWriteError(obj, property, value);
            } else {
                return setObjectProp(sobj, property, value, cx);
            }
        }
    }

    public static Object setObjectProp(Scriptable obj, String property, Object value, Context cx) {
        ScriptableObject.putProperty(obj, property, value);
        return value;
    }

    /** @deprecated */
    @Deprecated
    public static Object setObjectIndex(Object obj, double dblIndex, Object value, Context cx) {
        return setObjectIndex(obj, dblIndex, value, cx, getTopCallScope(cx));
    }

    public static Object setObjectIndex(Object obj, double dblIndex, Object value, Context cx, Scriptable scope) {
        Scriptable sobj = toObjectOrNull(cx, obj, scope);
        if (sobj == null) {
            throw undefWriteError(obj, String.valueOf(dblIndex), value);
        } else {
            int index = (int)dblIndex;
            if ((double)index == dblIndex) {
                return setObjectIndex(sobj, index, value, cx);
            } else {
                String s = toString(dblIndex);
                return setObjectProp(sobj, s, value, cx);
            }
        }
    }

    public static Object setObjectIndex(Scriptable obj, int index, Object value, Context cx) {
        ScriptableObject.putProperty(obj, index, value);
        return value;
    }

    public static boolean deleteObjectElem(Scriptable target, Object elem, Context cx) {
        if (isSymbol(elem)) {
            SymbolScriptable so = ScriptableObject.ensureSymbolScriptable(target);
            Symbol s = (Symbol)elem;
            so.delete(s);
            return !so.has(s, target);
        } else {
            StringIdOrIndex s = toStringIdOrIndex(cx, elem);
            if (s.stringId == null) {
                target.delete(s.index);
                return !target.has(s.index, target);
            } else {
                target.delete(s.stringId);
                return !target.has(s.stringId, target);
            }
        }
    }

    public static boolean hasObjectElem(Scriptable target, Object elem, Context cx) {
        boolean result;
        if (isSymbol(elem)) {
            result = ScriptableObject.hasProperty(target, (Symbol)elem);
        } else {
            StringIdOrIndex s = toStringIdOrIndex(cx, elem);
            if (s.stringId == null) {
                result = ScriptableObject.hasProperty(target, s.index);
            } else {
                result = ScriptableObject.hasProperty(target, s.stringId);
            }
        }

        return result;
    }

    public static Object refGet(Ref ref, Context cx) {
        return ref.get(cx);
    }

    /** @deprecated */
    @Deprecated
    public static Object refSet(Ref ref, Object value, Context cx) {
        return refSet(ref, value, cx, getTopCallScope(cx));
    }

    public static Object refSet(Ref ref, Object value, Context cx, Scriptable scope) {
        return ref.set(cx, scope, value);
    }

    public static Object refDel(Ref ref, Context cx) {
        return wrapBoolean(ref.delete(cx));
    }

    static boolean isSpecialProperty(String s) {
        return s.equals("__proto__") || s.equals("__parent__");
    }

    /** @deprecated */
    @Deprecated
    public static Ref specialRef(Object obj, String specialProperty, Context cx) {
        return specialRef(obj, specialProperty, cx, getTopCallScope(cx));
    }

    public static Ref specialRef(Object obj, String specialProperty, Context cx, Scriptable scope) {
        return SpecialRef.createSpecial(cx, scope, obj, specialProperty);
    }

    /** @deprecated */
    @Deprecated
    public static Object delete(Object obj, Object id, Context cx) {
        return delete(obj, id, cx, false);
    }

    /** @deprecated */
    @Deprecated
    public static Object delete(Object obj, Object id, Context cx, boolean isName) {
        return delete(obj, id, cx, getTopCallScope(cx), isName);
    }

    public static Object delete(Object obj, Object id, Context cx, Scriptable scope, boolean isName) {
        Scriptable sobj = toObjectOrNull(cx, obj, scope);
        if (sobj == null) {
            if (isName) {
                return Boolean.TRUE;
            } else {
                throw undefDeleteError(obj, id);
            }
        } else {
            boolean result = deleteObjectElem(sobj, id, cx);
            return wrapBoolean(result);
        }
    }

    public static Object name(Context cx, Scriptable scope, String name) {
        Scriptable parent = scope.getParentScope();
        if (parent == null) {
            Object result = topScopeName(cx, scope, name);
            if (result == Scriptable.NOT_FOUND) {
                throw notFoundError(scope, name);
            } else {
                return result;
            }
        } else {
            return nameOrFunction(cx, scope, parent, name, false);
        }
    }

    private static Object nameOrFunction(Context cx, Scriptable scope, Scriptable parentScope, String name, boolean asFunctionCall) {
        Scriptable thisObj = scope;
        XMLObject firstXMLObject = null;

        Object result;
        while(true) {
            if (scope instanceof NativeWith) {
                Scriptable withObj = scope.getPrototype();
                if (withObj instanceof XMLObject) {
                    XMLObject xmlObj = (XMLObject)withObj;
                    if (xmlObj.has(name, xmlObj)) {
                        thisObj = xmlObj;
                        result = xmlObj.get(name, xmlObj);
                        break;
                    }

                    if (firstXMLObject == null) {
                        firstXMLObject = xmlObj;
                    }
                } else {
                    result = ScriptableObject.getProperty(withObj, name);
                    if (result != Scriptable.NOT_FOUND) {
                        thisObj = withObj;
                        break;
                    }
                }
            } else if (scope instanceof NativeCall) {
                result = scope.get(name, scope);
                if (result != Scriptable.NOT_FOUND) {
                    if (asFunctionCall) {
                        thisObj = ScriptableObject.getTopLevelScope(parentScope);
                    }
                    break;
                }
            } else {
                result = ScriptableObject.getProperty(scope, name);
                if (result != Scriptable.NOT_FOUND) {
                    thisObj = scope;
                    break;
                }
            }

            scope = parentScope;
            parentScope = parentScope.getParentScope();
            if (parentScope == null) {
                result = topScopeName(cx, scope, name);
                if (result == Scriptable.NOT_FOUND) {
                    if (firstXMLObject == null || asFunctionCall) {
                        throw notFoundError(scope, name);
                    }

                    result = firstXMLObject.get(name, firstXMLObject);
                }

                thisObj = scope;
                break;
            }
        }

        if (asFunctionCall) {
            if (!(result instanceof Callable)) {
                throw notFunctionError(result, name);
            }

            storeScriptable(cx, (Scriptable)thisObj);
        }

        return result;
    }

    private static Object topScopeName(Context cx, Scriptable scope, String name) {
        if (cx.useDynamicScope) {
            scope = checkDynamicScope(cx.topCallScope, scope);
        }

        return ScriptableObject.getProperty(scope, name);
    }

    public static Scriptable bind(Context cx, Scriptable scope, String id) {
        Scriptable firstXMLObject = null;
        Scriptable parent = scope.getParentScope();
        if (parent != null) {
            label47:
            do {
                if (!(scope instanceof NativeWith)) {
                    while(!ScriptableObject.hasProperty(scope, id)) {
                        scope = parent;
                        parent = parent.getParentScope();
                        if (parent == null) {
                            break label47;
                        }
                    }

                    return scope;
                }

                Scriptable withObj = scope.getPrototype();
                if (withObj instanceof XMLObject) {
                    XMLObject xmlObject = (XMLObject)withObj;
                    if (xmlObject.has(cx, id)) {
                        return xmlObject;
                    }

                    if (firstXMLObject == null) {
                        firstXMLObject = xmlObject;
                    }
                } else if (ScriptableObject.hasProperty(withObj, id)) {
                    return withObj;
                }

                scope = parent;
                parent = parent.getParentScope();
            } while(parent != null);
        }

        if (cx.useDynamicScope) {
            scope = checkDynamicScope(cx.topCallScope, scope);
        }

        return (Scriptable)(ScriptableObject.hasProperty(scope, id) ? scope : firstXMLObject);
    }

    public static Object setName(Scriptable bound, Object value, Context cx, Scriptable scope, String id) {
        if (bound != null) {
            ScriptableObject.putProperty(bound, id, value);
        } else {
            if (cx.hasFeature(11) || cx.hasFeature(8)) {
                Context.reportWarning(getMessageById("msg.assn.create.strict", id));
            }

            bound = ScriptableObject.getTopLevelScope(scope);
            if (cx.useDynamicScope) {
                bound = checkDynamicScope(cx.topCallScope, bound);
            }

            bound.put(id, bound, value);
        }

        return value;
    }

    public static Object strictSetName(Scriptable bound, Object value, Context cx, Scriptable scope, String id) {
        if (bound != null) {
            ScriptableObject.putProperty(bound, id, value);
            return value;
        } else {
            String msg = "Assignment to undefined \"" + id + "\" in strict mode";
            throw constructError("ReferenceError", msg);
        }
    }

    public static Object setConst(Scriptable bound, Object value, Context cx, String id) {
        if (bound instanceof XMLObject) {
            bound.put(id, bound, value);
        } else {
            ScriptableObject.putConstProperty(bound, id, value);
        }

        return value;
    }

    public static Scriptable toIterator(Context cx, Scriptable scope, Scriptable obj, boolean keyOnly) {
        if (ScriptableObject.hasProperty(obj, "__iterator__")) {
            Object v = ScriptableObject.getProperty(obj, "__iterator__");
            if (!(v instanceof Callable)) {
                throw typeErrorById("msg.invalid.iterator");
            } else {
                Callable f = (Callable)v;
                Object[] args = new Object[]{keyOnly ? Boolean.TRUE : Boolean.FALSE};
                v = f.call(cx, scope, obj, args);
                if (!(v instanceof Scriptable)) {
                    throw typeErrorById("msg.iterator.primitive");
                } else {
                    return (Scriptable)v;
                }
            }
        } else {
            return null;
        }
    }

    /** @deprecated */
    @Deprecated
    public static Object enumInit(Object value, Context cx, boolean enumValues) {
        return enumInit(value, cx, enumValues ? 1 : 0);
    }

    /** @deprecated */
    @Deprecated
    public static Object enumInit(Object value, Context cx, int enumType) {
        return enumInit(value, cx, getTopCallScope(cx), enumType);
    }

    public static Object enumInit(Object value, Context cx, Scriptable scope, int enumType) {
        IdEnumeration x = new IdEnumeration();
        x.obj = toObjectOrNull(cx, value, scope);
        if (enumType == 6) {
            x.enumType = enumType;
            x.iterator = null;
            return enumInitInOrder(cx, x);
        } else if (x.obj == null) {
            return x;
        } else {
            x.enumType = enumType;
            x.iterator = null;
            if (enumType != 3 && enumType != 4 && enumType != 5) {
                x.iterator = toIterator(cx, x.obj.getParentScope(), x.obj, enumType == 0);
            }

            if (x.iterator == null) {
                enumChangeObject(x);
            }

            return x;
        }
    }

    private static Object enumInitInOrder(Context cx, IdEnumeration x) {
        if (x.obj instanceof SymbolScriptable && ScriptableObject.hasProperty(x.obj, SymbolKey.ITERATOR)) {
            Object iterator = ScriptableObject.getProperty(x.obj, SymbolKey.ITERATOR);
            if (!(iterator instanceof Callable)) {
                throw typeErrorById("msg.not.iterable", toString(x.obj));
            } else {
                Callable f = (Callable)iterator;
                Scriptable scope = x.obj.getParentScope();
                Object[] args = new Object[0];
                Object v = f.call(cx, scope, x.obj, args);
                if (!(v instanceof Scriptable)) {
                    throw typeErrorById("msg.not.iterable", toString(x.obj));
                } else {
                    x.iterator = (Scriptable)v;
                    return x;
                }
            }
        } else {
            throw typeErrorById("msg.not.iterable", toString(x.obj));
        }
    }

    public static void setEnumNumbers(Object enumObj, boolean enumNumbers) {
        ((IdEnumeration)enumObj).enumNumbers = enumNumbers;
    }

    public static Boolean enumNext(Object enumObj) {
        IdEnumeration x = (IdEnumeration)enumObj;
        Object id;
        if (x.iterator != null) {
            if (x.enumType == 6) {
                return enumNextInOrder(x);
            } else {
                id = ScriptableObject.getProperty(x.iterator, "next");
                if (!(id instanceof Callable)) {
                    return Boolean.FALSE;
                } else {
                    Callable f = (Callable)id;
                    Context cx = Context.getContext();

                    try {
                        x.currentId = f.call(cx, x.iterator.getParentScope(), x.iterator, emptyArgs);
                        return Boolean.TRUE;
                    } catch (JavaScriptException var6) {
                        if (var6.getValue() instanceof NativeIterator.StopIteration) {
                            return Boolean.FALSE;
                        } else {
                            throw var6;
                        }
                    }
                }
            }
        } else {
            while(true) {
                do {
                    label60:
                    do {
                        while(x.obj != null) {
                            if (x.index != x.ids.length) {
                                id = x.ids[x.index++];
                                continue label60;
                            }

                            x.obj = x.obj.getPrototype();
                            enumChangeObject(x);
                        }

                        return Boolean.FALSE;
                    } while(x.used != null && x.used.has(id));
                } while(id instanceof Symbol);

                if (id instanceof String) {
                    String strId = (String)id;
                    if (x.obj.has(strId, x.obj)) {
                        x.currentId = strId;
                        break;
                    }
                } else {
                    int intId = ((Number)id).intValue();
                    if (x.obj.has(intId, x.obj)) {
                        x.currentId = x.enumNumbers ? intId : String.valueOf(intId);
                        break;
                    }
                }
            }

            return Boolean.TRUE;
        }
    }

    private static Boolean enumNextInOrder(IdEnumeration enumObj) {
        Object v = ScriptableObject.getProperty(enumObj.iterator, "next");
        if (!(v instanceof Callable)) {
            throw notFunctionError(enumObj.iterator, "next");
        } else {
            Callable f = (Callable)v;
            Context cx = Context.getContext();
            Scriptable scope = enumObj.iterator.getParentScope();
            Object r = f.call(cx, scope, enumObj.iterator, emptyArgs);
            Scriptable iteratorResult = toObject(cx, scope, r);
            Object done = ScriptableObject.getProperty(iteratorResult, "done");
            if (done != Scriptable.NOT_FOUND && toBoolean(done)) {
                return Boolean.FALSE;
            } else {
                enumObj.currentId = ScriptableObject.getProperty(iteratorResult, "value");
                return Boolean.TRUE;
            }
        }
    }

    public static Object enumId(Object enumObj, Context cx) {
        IdEnumeration x = (IdEnumeration)enumObj;
        if (x.iterator != null) {
            return x.currentId;
        } else {
            switch (x.enumType) {
                case 0:
                case 3:
                    return x.currentId;
                case 1:
                case 4:
                    return enumValue(enumObj, cx);
                case 2:
                case 5:
                    Object[] elements = new Object[]{x.currentId, enumValue(enumObj, cx)};
                    return cx.newArray(ScriptableObject.getTopLevelScope(x.obj), elements);
                default:
                    throw Kit.codeBug();
            }
        }
    }

    public static Object enumValue(Object enumObj, Context cx) {
        IdEnumeration x = (IdEnumeration)enumObj;
        Object result;
        if (isSymbol(x.currentId)) {
            SymbolScriptable so = ScriptableObject.ensureSymbolScriptable(x.obj);
            result = so.get((Symbol)x.currentId, x.obj);
        } else {
            StringIdOrIndex s = toStringIdOrIndex(cx, x.currentId);
            if (s.stringId == null) {
                result = x.obj.get(s.index, x.obj);
            } else {
                result = x.obj.get(s.stringId, x.obj);
            }
        }

        return result;
    }

    private static void enumChangeObject(IdEnumeration x) {
        Object[] ids;
        for(ids = null; x.obj != null; x.obj = x.obj.getPrototype()) {
            ids = x.obj.getIds();
            if (ids.length != 0) {
                break;
            }
        }

        if (x.obj != null && x.ids != null) {
            Object[] previous = x.ids;
            int L = previous.length;
            if (x.used == null) {
                x.used = new ObjToIntMap(L);
            }

            for(int i = 0; i != L; ++i) {
                x.used.intern(previous[i]);
            }
        }

        x.ids = ids;
        x.index = 0;
    }

    public static boolean loadFromIterable(Context cx, Scriptable scope, Object arg1, BiConsumer<Object, Object> setter) {
        if (arg1 != null && !Undefined.isUndefined(arg1)) {
            Object ito = callIterator(arg1, cx, scope);
            if (Undefined.isUndefined(ito)) {
                return false;
            } else {
                IteratorLikeIterable it = new IteratorLikeIterable(cx, scope, ito);
                Throwable var6 = null;

                try {
                    Object finalKey;
                    Object finalVal;
                    try {
                        for(IteratorLikeIterable.Itr var7 = it.iterator(); var7.hasNext(); setter.accept(finalKey, finalVal)) {
                            Object val = var7.next();
                            Scriptable sVal = ScriptableObject.ensureScriptable(val);
                            if (sVal instanceof Symbol) {
                                throw typeErrorById("msg.arg.not.object", typeof(sVal));
                            }

                            finalKey = sVal.get(0, sVal);
                            if (finalKey == Scriptable.NOT_FOUND) {
                                finalKey = Undefined.instance;
                            }

                            finalVal = sVal.get(1, sVal);
                            if (finalVal == Scriptable.NOT_FOUND) {
                                finalVal = Undefined.instance;
                            }
                        }
                    } catch (Throwable var19) {
                        var6 = var19;
                        throw var19;
                    }
                } finally {
                    if (it != null) {
                        if (var6 != null) {
                            try {
                                it.close();
                            } catch (Throwable var18) {
                                var6.addSuppressed(var18);
                            }
                        } else {
                            it.close();
                        }
                    }

                }

                return true;
            }
        } else {
            return false;
        }
    }

    public static Callable getNameFunctionAndThis(String name, Context cx, Scriptable scope) {
        Scriptable parent = scope.getParentScope();
        if (parent == null) {
            Object result = topScopeName(cx, scope, name);
            if (!(result instanceof Callable)) {
                if (result == Scriptable.NOT_FOUND) {
                    throw notFoundError(scope, name);
                } else {
                    throw notFunctionError(result, name);
                }
            } else {
                storeScriptable(cx, scope);
                return (Callable)result;
            }
        } else {
            return (Callable)nameOrFunction(cx, scope, parent, name, true);
        }
    }

    /** @deprecated */
    @Deprecated
    public static Callable getElemFunctionAndThis(Object obj, Object elem, Context cx) {
        return getElemFunctionAndThis(obj, elem, cx, getTopCallScope(cx));
    }

    public static Callable getElemFunctionAndThis(Object obj, Object elem, Context cx, Scriptable scope) {
        Scriptable thisObj;
        Object value;
        if (isSymbol(elem)) {
            thisObj = toObjectOrNull(cx, obj, scope);
            if (thisObj == null) {
                throw undefCallError(obj, String.valueOf(elem));
            }

            value = ScriptableObject.getProperty(thisObj, (Symbol)elem);
        } else {
            StringIdOrIndex s = toStringIdOrIndex(cx, elem);
            if (s.stringId != null) {
                return getPropFunctionAndThis(obj, s.stringId, cx, scope);
            }

            thisObj = toObjectOrNull(cx, obj, scope);
            if (thisObj == null) {
                throw undefCallError(obj, String.valueOf(elem));
            }

            value = ScriptableObject.getProperty(thisObj, s.index);
        }

        if (!(value instanceof Callable)) {
            throw notFunctionError(value, elem);
        } else {
            storeScriptable(cx, thisObj);
            return (Callable)value;
        }
    }

    /** @deprecated */
    @Deprecated
    public static Callable getPropFunctionAndThis(Object obj, String property, Context cx) {
        return getPropFunctionAndThis(obj, property, cx, getTopCallScope(cx));
    }

    public static Callable getPropFunctionAndThis(Object obj, String property, Context cx, Scriptable scope) {
        Scriptable thisObj = toObjectOrNull(cx, obj, scope);
        return getPropFunctionAndThisHelper(obj, property, cx, thisObj);
    }

    private static Callable getPropFunctionAndThisHelper(Object obj, String property, Context cx, Scriptable thisObj) {
        if (thisObj == null) {
            throw undefCallError(obj, property);
        } else {
            Object value = ScriptableObject.getProperty(thisObj, property);
            if (!(value instanceof Callable)) {
                Object noSuchMethod = ScriptableObject.getProperty(thisObj, "__noSuchMethod__");
                if (noSuchMethod instanceof Callable) {
                    value = new NoSuchMethodShim((Callable)noSuchMethod, property);
                }
            }

            if (!(value instanceof Callable)) {
                throw notFunctionError(thisObj, value, property);
            } else {
                storeScriptable(cx, thisObj);
                return (Callable)value;
            }
        }
    }

    public static Callable getValueFunctionAndThis(Object value, Context cx) {
        if (!(value instanceof Callable)) {
            throw notFunctionError(value);
        } else {
            Callable f = (Callable)value;
            Scriptable thisObj = null;
            if (f instanceof Scriptable) {
                thisObj = ((Scriptable)f).getParentScope();
            }

            if (thisObj == null) {
                if (cx.topCallScope == null) {
                    throw new IllegalStateException();
                }

                thisObj = cx.topCallScope;
            }

            if (thisObj.getParentScope() != null && !(thisObj instanceof NativeWith) && thisObj instanceof NativeCall) {
                thisObj = ScriptableObject.getTopLevelScope(thisObj);
            }

            storeScriptable(cx, thisObj);
            return f;
        }
    }

    public static Object callIterator(Object obj, Context cx, Scriptable scope) {
        Callable getIterator = getElemFunctionAndThis(obj, SymbolKey.ITERATOR, cx, scope);
        Scriptable iterable = lastStoredScriptable(cx);
        return getIterator.call(cx, scope, iterable, emptyArgs);
    }

    public static boolean isIteratorDone(Context cx, Object result) {
        if (!(result instanceof Scriptable)) {
            return false;
        } else {
            Object prop = getObjectProp((Scriptable)result, "done", cx);
            return toBoolean(prop);
        }
    }

    public static Ref callRef(Callable function, Scriptable thisObj, Object[] args, Context cx) {
        if (function instanceof RefCallable) {
            RefCallable rfunction = (RefCallable)function;
            Ref ref = rfunction.refCall(cx, thisObj, args);
            if (ref == null) {
                throw new IllegalStateException(rfunction.getClass().getName() + ".refCall() returned null");
            } else {
                return ref;
            }
        } else {
            String msg = getMessageById("msg.no.ref.from.function", toString(function));
            throw constructError("ReferenceError", msg);
        }
    }

    public static Scriptable newObject(Object fun, Context cx, Scriptable scope, Object[] args) {
        if (!(fun instanceof Function)) {
            throw notFunctionError(fun);
        } else {
            Function function = (Function)fun;
            return function.construct(cx, scope, args);
        }
    }

    public static Object callSpecial(Context cx, Callable fun, Scriptable thisObj, Object[] args, Scriptable scope, Scriptable callerThis, int callType, String filename, int lineNumber) {
        if (callType == 1) {
            if (thisObj.getParentScope() == null && NativeGlobal.isEvalFunction(fun)) {
                return evalSpecial(cx, scope, callerThis, args, filename, lineNumber);
            }
        } else {
            if (callType != 2) {
                throw Kit.codeBug();
            }

            if (NativeWith.isWithFunction(fun)) {
                throw Context.reportRuntimeErrorById("msg.only.from.new", new Object[]{"With"});
            }
        }

        return fun.call(cx, scope, thisObj, args);
    }

    public static Object newSpecial(Context cx, Object fun, Object[] args, Scriptable scope, int callType) {
        if (callType == 1) {
            if (NativeGlobal.isEvalFunction(fun)) {
                throw typeErrorById("msg.not.ctor", "eval");
            }
        } else {
            if (callType != 2) {
                throw Kit.codeBug();
            }

            if (NativeWith.isWithFunction(fun)) {
                return NativeWith.newWithSpecial(cx, scope, args);
            }
        }

        return newObject(fun, cx, scope, args);
    }

    public static Object applyOrCall(boolean isApply, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        int L = args.length;
        Callable function = getCallable(thisObj);
        Scriptable callThis = null;
        if (L != 0) {
            if (cx.hasFeature(15)) {
                callThis = toObjectOrNull(cx, args[0], scope);
            } else {
                callThis = args[0] == Undefined.instance ? Undefined.SCRIPTABLE_UNDEFINED : toObjectOrNull(cx, args[0], scope);
            }
        }

        if (callThis == null && cx.hasFeature(15)) {
            callThis = getTopCallScope(cx);
        }

        Object[] callArgs;
        if (isApply) {
            callArgs = L <= 1 ? emptyArgs : getApplyArguments(cx, args[1]);
        } else if (L <= 1) {
            callArgs = emptyArgs;
        } else {
            callArgs = new Object[L - 1];
            System.arraycopy(args, 1, callArgs, 0, L - 1);
        }

        return function.call(cx, scope, callThis, callArgs);
    }

    private static boolean isArrayLike(Scriptable obj) {
        return obj != null && (obj instanceof NativeArray || obj instanceof Arguments || ScriptableObject.hasProperty(obj, "length"));
    }

    static Object[] getApplyArguments(Context cx, Object arg1) {
        if (arg1 != null && !Undefined.isUndefined(arg1)) {
            if (arg1 instanceof Scriptable && isArrayLike((Scriptable)arg1)) {
                return cx.getElements((Scriptable)arg1);
            } else if (arg1 instanceof ScriptableObject) {
                return emptyArgs;
            } else {
                throw typeErrorById("msg.arg.isnt.array");
            }
        } else {
            return emptyArgs;
        }
    }

    static Callable getCallable(Scriptable thisObj) {
        Callable function;
        if (thisObj instanceof Callable) {
            function = (Callable)thisObj;
        } else {
            if (thisObj == null) {
                throw notFunctionError((Object)null, (Object)null);
            }

            Object value = thisObj.getDefaultValue(FunctionClass);
            if (!(value instanceof Callable)) {
                throw notFunctionError(value, thisObj);
            }

            function = (Callable)value;
        }

        return function;
    }

    public static Object evalSpecial(Context cx, Scriptable scope, Object thisArg, Object[] args, String filename, int lineNumber) {
        if (args.length < 1) {
            return Undefined.instance;
        } else {
            Object x = args[0];
            String sourceName;
            if (!(x instanceof CharSequence)) {
                if (!cx.hasFeature(11) && !cx.hasFeature(9)) {
                    sourceName = getMessageById("msg.eval.nonstring");
                    Context.reportWarning(sourceName);
                    return x;
                } else {
                    throw Context.reportRuntimeErrorById("msg.eval.nonstring.strict", new Object[0]);
                }
            } else {
                if (filename == null) {
                    int[] linep = new int[1];
                    filename = Context.getSourcePositionFromStack(linep);
                    if (filename != null) {
                        lineNumber = linep[0];
                    } else {
                        filename = "";
                    }
                }

                sourceName = makeUrlForGeneratedScript(true, filename, lineNumber);
                ErrorReporter reporter = DefaultErrorReporter.forEval(cx.getErrorReporter());
                Evaluator evaluator = Context.createInterpreter();
                if (evaluator == null) {
                    throw new JavaScriptException("Interpreter not present", filename, lineNumber);
                } else {
                    Script script = cx.compileString(x.toString(), evaluator, reporter, sourceName, 1, (Object)null);
                    evaluator.setEvalScriptFlag(script);
                    Callable c = (Callable)script;
                    return c.call(cx, scope, (Scriptable)thisArg, emptyArgs);
                }
            }
        }
    }

    public static String typeof(Object value) {
        if (value == null) {
            return "object";
        } else if (value == Undefined.instance) {
            return "undefined";
        } else if (value instanceof Delegator) {
            return typeof(((Delegator)value).getDelegee());
        } else if (value instanceof ScriptableObject) {
            return ((ScriptableObject)value).getTypeOf();
        } else if (value instanceof Scriptable) {
            return value instanceof Callable ? "function" : "object";
        } else if (value instanceof CharSequence) {
            return "string";
        } else if (value instanceof BigInteger) {
            return "bigint";
        } else if (value instanceof Number) {
            return "number";
        } else if (value instanceof Boolean) {
            return "boolean";
        } else {
            throw errorWithClassName("msg.invalid.type", value);
        }
    }

    public static String typeofName(Scriptable scope, String id) {
        Context cx = Context.getContext();
        Scriptable val = bind(cx, scope, id);
        return val == null ? "undefined" : typeof(getObjectProp(val, id, cx));
    }

    public static boolean isObject(Object value) {
        if (value == null) {
            return false;
        } else if (Undefined.isUndefined(value)) {
            return false;
        } else if (!(value instanceof ScriptableObject)) {
            if (value instanceof Scriptable) {
                return !(value instanceof Callable);
            } else {
                return false;
            }
        } else {
            String type = ((ScriptableObject)value).getTypeOf();
            return "object".equals(type) || "function".equals(type);
        }
    }

    public static Object add(Object val1, Object val2, Context cx) {
        if (val1 instanceof BigInteger && val2 instanceof BigInteger) {
            return ((BigInteger)val1).add((BigInteger)val2);
        } else if ((!(val1 instanceof Number) || !(val2 instanceof BigInteger)) && (!(val1 instanceof BigInteger) || !(val2 instanceof Number))) {
            if (val1 instanceof Number && val2 instanceof Number) {
                return wrapNumber(((Number)val1).doubleValue() + ((Number)val2).doubleValue());
            } else if (val1 instanceof CharSequence && val2 instanceof CharSequence) {
                return new ConsString((CharSequence)val1, (CharSequence)val2);
            } else {
                Object test;
                if (val1 instanceof XMLObject) {
                    test = ((XMLObject)val1).addValues(cx, true, val2);
                    if (test != Scriptable.NOT_FOUND) {
                        return test;
                    }
                }

                if (val2 instanceof XMLObject) {
                    test = ((XMLObject)val2).addValues(cx, false, val1);
                    if (test != Scriptable.NOT_FOUND) {
                        return test;
                    }
                }

                if (!(val1 instanceof Symbol) && !(val2 instanceof Symbol)) {
                    if (val1 instanceof Scriptable) {
                        val1 = ((Scriptable)val1).getDefaultValue((Class)null);
                    }

                    if (val2 instanceof Scriptable) {
                        val2 = ((Scriptable)val2).getDefaultValue((Class)null);
                    }

                    if (!(val1 instanceof CharSequence) && !(val2 instanceof CharSequence)) {
                        Number num1 = val1 instanceof Number ? (Number)val1 : toNumeric(val1);
                        Number num2 = val2 instanceof Number ? (Number)val2 : toNumeric(val2);
                        if (num1 instanceof BigInteger && num2 instanceof BigInteger) {
                            return ((BigInteger)num1).add((BigInteger)num2);
                        } else if (!(num1 instanceof BigInteger) && !(num2 instanceof BigInteger)) {
                            return num1.doubleValue() + num2.doubleValue();
                        } else {
                            throw typeErrorById("msg.cant.convert.to.number", "BigInt");
                        }
                    } else {
                        return new ConsString(toCharSequence(val1), toCharSequence(val2));
                    }
                } else {
                    throw typeErrorById("msg.not.a.number");
                }
            }
        } else {
            throw typeErrorById("msg.cant.convert.to.number", "BigInt");
        }
    }

    /** @deprecated */
    @Deprecated
    public static CharSequence add(CharSequence val1, Object val2) {
        return new ConsString(val1, toCharSequence(val2));
    }

    /** @deprecated */
    @Deprecated
    public static CharSequence add(Object val1, CharSequence val2) {
        return new ConsString(toCharSequence(val1), val2);
    }

    public static Number subtract(Number val1, Number val2) {
        if (val1 instanceof BigInteger && val2 instanceof BigInteger) {
            return ((BigInteger)val1).subtract((BigInteger)val2);
        } else if (!(val1 instanceof BigInteger) && !(val2 instanceof BigInteger)) {
            return val1.doubleValue() - val2.doubleValue();
        } else {
            throw typeErrorById("msg.cant.convert.to.number", "BigInt");
        }
    }

    public static Number multiply(Number val1, Number val2) {
        if (val1 instanceof BigInteger && val2 instanceof BigInteger) {
            return ((BigInteger)val1).multiply((BigInteger)val2);
        } else if (!(val1 instanceof BigInteger) && !(val2 instanceof BigInteger)) {
            return val1.doubleValue() * val2.doubleValue();
        } else {
            throw typeErrorById("msg.cant.convert.to.number", "BigInt");
        }
    }

    public static Number divide(Number val1, Number val2) {
        if (val1 instanceof BigInteger && val2 instanceof BigInteger) {
            if (val2.equals(BigInteger.ZERO)) {
                throw rangeErrorById("msg.division.zero");
            } else {
                return ((BigInteger)val1).divide((BigInteger)val2);
            }
        } else if (!(val1 instanceof BigInteger) && !(val2 instanceof BigInteger)) {
            return val1.doubleValue() / val2.doubleValue();
        } else {
            throw typeErrorById("msg.cant.convert.to.number", "BigInt");
        }
    }

    public static Number remainder(Number val1, Number val2) {
        if (val1 instanceof BigInteger && val2 instanceof BigInteger) {
            if (val2.equals(BigInteger.ZERO)) {
                throw rangeErrorById("msg.division.zero");
            } else {
                return ((BigInteger)val1).remainder((BigInteger)val2);
            }
        } else if (!(val1 instanceof BigInteger) && !(val2 instanceof BigInteger)) {
            return val1.doubleValue() % val2.doubleValue();
        } else {
            throw typeErrorById("msg.cant.convert.to.number", "BigInt");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    public static Number exponentiate(Number val1, Number val2) {
        if (val1 instanceof BigInteger && val2 instanceof BigInteger) {
            if (((BigInteger)val2).signum() == -1) {
                throw rangeErrorById("msg.bigint.negative.exponent");
            } else {
                try {
                    int intVal2 = ((BigInteger)val2).intValueExact();
                    return ((BigInteger)val1).pow(intVal2);
                } catch (ArithmeticException var3) {
                    throw rangeErrorById("msg.bigint.out.of.range.arithmetic");
                }
            }
        } else if (!(val1 instanceof BigInteger) && !(val2 instanceof BigInteger)) {
            return Math.pow(val1.doubleValue(), val2.doubleValue());
        } else {
            throw typeErrorById("msg.cant.convert.to.number", "BigInt");
        }
    }

    public static Number bitwiseAND(Number val1, Number val2) {
        if (val1 instanceof BigInteger && val2 instanceof BigInteger) {
            return ((BigInteger)val1).and((BigInteger)val2);
        } else if (!(val1 instanceof BigInteger) && !(val2 instanceof BigInteger)) {
            int result = toInt32(val1.doubleValue()) & toInt32(val2.doubleValue());
            return (double)result;
        } else {
            throw typeErrorById("msg.cant.convert.to.number", "BigInt");
        }
    }

    public static Number bitwiseOR(Number val1, Number val2) {
        if (val1 instanceof BigInteger && val2 instanceof BigInteger) {
            return ((BigInteger)val1).or((BigInteger)val2);
        } else if (!(val1 instanceof BigInteger) && !(val2 instanceof BigInteger)) {
            int result = toInt32(val1.doubleValue()) | toInt32(val2.doubleValue());
            return (double)result;
        } else {
            throw typeErrorById("msg.cant.convert.to.number", "BigInt");
        }
    }

    public static Number bitwiseXOR(Number val1, Number val2) {
        if (val1 instanceof BigInteger && val2 instanceof BigInteger) {
            return ((BigInteger)val1).xor((BigInteger)val2);
        } else if (!(val1 instanceof BigInteger) && !(val2 instanceof BigInteger)) {
            int result = toInt32(val1.doubleValue()) ^ toInt32(val2.doubleValue());
            return (double)result;
        } else {
            throw typeErrorById("msg.cant.convert.to.number", "BigInt");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    public static Number leftShift(Number val1, Number val2) {
        int result;
        if (val1 instanceof BigInteger && val2 instanceof BigInteger) {
            try {
                result = ((BigInteger)val2).intValueExact();
                return ((BigInteger)val1).shiftLeft(result);
            } catch (ArithmeticException var3) {
                throw rangeErrorById("msg.bigint.out.of.range.arithmetic");
            }
        } else if (!(val1 instanceof BigInteger) && !(val2 instanceof BigInteger)) {
            result = toInt32(val1.doubleValue()) << toInt32(val2.doubleValue());
            return (double)result;
        } else {
            throw typeErrorById("msg.cant.convert.to.number", "BigInt");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    public static Number signedRightShift(Number val1, Number val2) {
        int result;
        if (val1 instanceof BigInteger && val2 instanceof BigInteger) {
            try {
                result = ((BigInteger)val2).intValueExact();
                return ((BigInteger)val1).shiftRight(result);
            } catch (ArithmeticException var3) {
                throw rangeErrorById("msg.bigint.out.of.range.arithmetic");
            }
        } else if (!(val1 instanceof BigInteger) && !(val2 instanceof BigInteger)) {
            result = toInt32(val1.doubleValue()) >> toInt32(val2.doubleValue());
            return (double)result;
        } else {
            throw typeErrorById("msg.cant.convert.to.number", "BigInt");
        }
    }

    public static Number bitwiseNOT(Number val) {
        if (val instanceof BigInteger) {
            return ((BigInteger)val).not();
        } else {
            int result = ~toInt32(val.doubleValue());
            return (double)result;
        }
    }

    /** @deprecated */
    @Deprecated
    public static Object nameIncrDecr(Scriptable scopeChain, String id, int incrDecrMask) {
        return nameIncrDecr(scopeChain, id, Context.getContext(), incrDecrMask);
    }

    public static Object nameIncrDecr(Scriptable scopeChain, String id, Context cx, int incrDecrMask) {
        do {
            if (cx.useDynamicScope && scopeChain.getParentScope() == null) {
                scopeChain = checkDynamicScope(cx.topCallScope, scopeChain);
            }

            Scriptable target = scopeChain;

            while(!(target instanceof NativeWith) || !(target.getPrototype() instanceof XMLObject)) {
                Object value = target.get(id, scopeChain);
                if (value != Scriptable.NOT_FOUND) {
                    return doScriptableIncrDecr(target, id, scopeChain, value, incrDecrMask);
                }

                target = target.getPrototype();
                if (target == null) {
                    break;
                }
            }

            scopeChain = scopeChain.getParentScope();
        } while(scopeChain != null);

        throw notFoundError((Scriptable)null, id);
    }

    /** @deprecated */
    @Deprecated
    public static Object propIncrDecr(Object obj, String id, Context cx, int incrDecrMask) {
        return propIncrDecr(obj, id, cx, getTopCallScope(cx), incrDecrMask);
    }

    public static Object propIncrDecr(Object obj, String id, Context cx, Scriptable scope, int incrDecrMask) {
        Scriptable start = toObjectOrNull(cx, obj, scope);
        if (start == null) {
            throw undefReadError(obj, id);
        } else {
            Scriptable target = start;

            do {
                Object value = target.get(id, start);
                if (value != Scriptable.NOT_FOUND) {
                    return doScriptableIncrDecr(target, id, start, value, incrDecrMask);
                }

                target = target.getPrototype();
            } while(target != null);

            start.put(id, start, NaNobj);
            return NaNobj;
        }
    }

    private static Object doScriptableIncrDecr(Scriptable target, String id, Scriptable protoChainStart, Object value, int incrDecrMask) {
        boolean post = (incrDecrMask & 2) != 0;
        Number number;
        if (value instanceof Number) {
            number = (Number)value;
        } else {
            number = toNumeric(value);
        }

        Object result;
        if (number instanceof BigInteger) {
            if ((incrDecrMask & 1) == 0) {
                result = ((BigInteger)number).add(BigInteger.ONE);
            } else {
                result = ((BigInteger)number).subtract(BigInteger.ONE);
            }
        } else if ((incrDecrMask & 1) == 0) {
            result = number.doubleValue() + 1.0;
        } else {
            result = number.doubleValue() - 1.0;
        }

        target.put(id, protoChainStart, result);
        return post ? number : result;
    }

    /** @deprecated */
    @Deprecated
    public static Object elemIncrDecr(Object obj, Object index, Context cx, int incrDecrMask) {
        return elemIncrDecr(obj, index, cx, getTopCallScope(cx), incrDecrMask);
    }

    public static Object elemIncrDecr(Object obj, Object index, Context cx, Scriptable scope, int incrDecrMask) {
        Object value = getObjectElem(obj, index, cx, scope);
        boolean post = (incrDecrMask & 2) != 0;
        Number number;
        if (value instanceof Number) {
            number = (Number)value;
        } else {
            number = toNumeric(value);
        }

        Object result;
        if (number instanceof BigInteger) {
            if ((incrDecrMask & 1) == 0) {
                result = ((BigInteger)number).add(BigInteger.ONE);
            } else {
                result = ((BigInteger)number).subtract(BigInteger.ONE);
            }
        } else if ((incrDecrMask & 1) == 0) {
            result = number.doubleValue() + 1.0;
        } else {
            result = number.doubleValue() - 1.0;
        }

        setObjectElem(obj, index, result, cx, scope);
        return post ? number : result;
    }

    /** @deprecated */
    @Deprecated
    public static Object refIncrDecr(Ref ref, Context cx, int incrDecrMask) {
        return refIncrDecr(ref, cx, getTopCallScope(cx), incrDecrMask);
    }

    public static Object refIncrDecr(Ref ref, Context cx, Scriptable scope, int incrDecrMask) {
        Object value = ref.get(cx);
        boolean post = (incrDecrMask & 2) != 0;
        Number number;
        if (value instanceof Number) {
            number = (Number)value;
        } else {
            number = toNumeric(value);
        }

        Object result;
        if (number instanceof BigInteger) {
            if ((incrDecrMask & 1) == 0) {
                result = ((BigInteger)number).add(BigInteger.ONE);
            } else {
                result = ((BigInteger)number).subtract(BigInteger.ONE);
            }
        } else if ((incrDecrMask & 1) == 0) {
            result = number.doubleValue() + 1.0;
        } else {
            result = number.doubleValue() - 1.0;
        }

        ref.set(cx, scope, result);
        return post ? number : result;
    }

    public static Number negate(Number val) {
        return (Number)(val instanceof BigInteger ? ((BigInteger)val).negate() : -val.doubleValue());
    }

    public static Object toPrimitive(Object val) {
        return toPrimitive(val, (Class)null);
    }

    public static Object toPrimitive(Object val, Class<?> typeHint) {
        if (!(val instanceof Scriptable)) {
            return val;
        } else {
            Scriptable s = (Scriptable)val;
            Object result = s.getDefaultValue(typeHint);
            if (result instanceof Scriptable && !isSymbol(result)) {
                throw typeErrorById("msg.bad.default.value");
            } else {
                return result;
            }
        }
    }

    public static boolean eq(Object x, Object y) {
        Object unwrappedX;
        if (x != null && !Undefined.isUndefined(x)) {
            if (x instanceof BigInteger) {
                return eqBigInt((BigInteger)x, y);
            } else if (x instanceof Number) {
                return eqNumber(((Number)x).doubleValue(), y);
            } else if (x == y) {
                return true;
            } else if (x instanceof CharSequence) {
                return eqString((CharSequence)x, y);
            } else {
                Object unwrappedY;
                if (x instanceof Boolean) {
                    boolean b = (Boolean)x;
                    if (y instanceof Boolean) {
                        return b == (Boolean)y;
                    } else {
                        if (y instanceof ScriptableObject) {
                            unwrappedY = ((ScriptableObject)y).equivalentValues(x);
                            if (unwrappedY != Scriptable.NOT_FOUND) {
                                return (Boolean)unwrappedY;
                            }
                        }

                        return eqNumber(b ? 1.0 : 0.0, y);
                    }
                } else if (x instanceof Scriptable) {
                    if (x instanceof Delegator) {
                        x = ((Delegator)x).getDelegee();
                        if (y instanceof Delegator) {
                            return eq(x, ((Delegator)y).getDelegee());
                        }

                        if (x == y) {
                            return true;
                        }
                    }

                    if (y instanceof Delegator && ((Delegator)y).getDelegee() == x) {
                        return true;
                    } else if (!(y instanceof Scriptable)) {
                        if (y instanceof Boolean) {
                            if (x instanceof ScriptableObject) {
                                unwrappedX = ((ScriptableObject)x).equivalentValues(y);
                                if (unwrappedX != Scriptable.NOT_FOUND) {
                                    return (Boolean)unwrappedX;
                                }
                            }

                            double d = (Boolean)y ? 1.0 : 0.0;
                            return eqNumber(d, x);
                        } else if (y instanceof BigInteger) {
                            return eqBigInt((BigInteger)y, x);
                        } else if (y instanceof Number) {
                            return eqNumber(((Number)y).doubleValue(), x);
                        } else {
                            return y instanceof CharSequence ? eqString((CharSequence)y, x) : false;
                        }
                    } else {
                        if (x instanceof ScriptableObject) {
                            unwrappedX = ((ScriptableObject)x).equivalentValues(y);
                            if (unwrappedX != Scriptable.NOT_FOUND) {
                                return (Boolean)unwrappedX;
                            }
                        }

                        if (y instanceof ScriptableObject) {
                            unwrappedX = ((ScriptableObject)y).equivalentValues(x);
                            if (unwrappedX != Scriptable.NOT_FOUND) {
                                return (Boolean)unwrappedX;
                            }
                        }

                        if (x instanceof Wrapper && y instanceof Wrapper) {
                            unwrappedX = ((Wrapper)x).unwrap();
                            unwrappedY = ((Wrapper)y).unwrap();
                            return unwrappedX == unwrappedY || isPrimitive(unwrappedX) && isPrimitive(unwrappedY) && eq(unwrappedX, unwrappedY);
                        } else {
                            return false;
                        }
                    }
                } else {
                    warnAboutNonJSObject(x);
                    return x == y;
                }
            }
        } else if (y != null && !Undefined.isUndefined(y)) {
            if (y instanceof ScriptableObject) {
                unwrappedX = ((ScriptableObject)y).equivalentValues(x);
                if (unwrappedX != Scriptable.NOT_FOUND) {
                    return (Boolean)unwrappedX;
                }
            }

            return false;
        } else {
            return true;
        }
    }

    public static boolean same(Object x, Object y) {
        if (!typeof(x).equals(typeof(y))) {
            return false;
        } else if (x instanceof Number) {
            return isNaN(x) && isNaN(y) ? true : x.equals(y);
        } else {
            return eq(x, y);
        }
    }

    public static boolean sameZero(Object x, Object y) {
        if (!typeof(x).equals(typeof(y))) {
            return false;
        } else if (x instanceof BigInteger) {
            return x.equals(y);
        } else if (x instanceof Number) {
            if (isNaN(x) && isNaN(y)) {
                return true;
            } else {
                double dx = ((Number)x).doubleValue();
                if (y instanceof Number) {
                    double dy = ((Number)y).doubleValue();
                    if (dx == negativeZero && dy == 0.0 || dx == 0.0 && dy == negativeZero) {
                        return true;
                    }
                }

                return eqNumber(dx, y);
            }
        } else {
            return eq(x, y);
        }
    }

    public static boolean isNaN(Object n) {
        if (n instanceof Double) {
            return ((Double)n).isNaN();
        } else {
            return n instanceof Float ? ((Float)n).isNaN() : false;
        }
    }

    public static boolean isPrimitive(Object obj) {
        return obj == null || Undefined.isUndefined(obj) || obj instanceof Number || obj instanceof String || obj instanceof Boolean;
    }

    static boolean eqNumber(double x, Object y) {
        while(true) {
            if (y != null && !Undefined.isUndefined(y)) {
                if (y instanceof BigInteger) {
                    return eqBigInt((BigInteger)y, x);
                }

                if (y instanceof Number) {
                    return x == ((Number)y).doubleValue();
                }

                if (y instanceof CharSequence) {
                    return x == toNumber(y);
                }

                if (y instanceof Boolean) {
                    return x == ((Boolean)y ? 1.0 : 0.0);
                }

                if (isSymbol(y)) {
                    return false;
                }

                if (y instanceof Scriptable) {
                    if (y instanceof ScriptableObject) {
                        Object xval = wrapNumber(x);
                        Object test = ((ScriptableObject)y).equivalentValues(xval);
                        if (test != Scriptable.NOT_FOUND) {
                            return (Boolean)test;
                        }
                    }

                    y = toPrimitive(y);
                    continue;
                }

                warnAboutNonJSObject(y);
                return false;
            }

            return false;
        }
    }

    static boolean eqBigInt(BigInteger x, Object y) {
        while(true) {
            if (y != null && !Undefined.isUndefined(y)) {
                if (y instanceof BigInteger) {
                    return x.equals(y);
                }

                if (y instanceof Number) {
                    return eqBigInt(x, ((Number)y).doubleValue());
                }

                BigInteger biy;
                if (y instanceof CharSequence) {
                    try {
                        biy = toBigInt(y);
                    } catch (EcmaError var4) {
                        return false;
                    }

                    return x.equals(biy);
                }

                if (y instanceof Boolean) {
                    biy = (Boolean)y ? BigInteger.ONE : BigInteger.ZERO;
                    return x.equals(biy);
                }

                if (isSymbol(y)) {
                    return false;
                }

                if (y instanceof Scriptable) {
                    if (y instanceof ScriptableObject) {
                        Object test = ((ScriptableObject)y).equivalentValues(x);
                        if (test != Scriptable.NOT_FOUND) {
                            return (Boolean)test;
                        }
                    }

                    y = toPrimitive(y);
                    continue;
                }

                warnAboutNonJSObject(y);
                return false;
            }

            return false;
        }
    }

    private static boolean eqBigInt(BigInteger x, double y) {
        if (!Double.isNaN(y) && !Double.isInfinite(y)) {
            double d = Math.ceil(y);
            if (d != y) {
                return false;
            } else {
                BigDecimal bdx = new BigDecimal(x);
                BigDecimal bdy = new BigDecimal(d, MathContext.UNLIMITED);
                return bdx.compareTo(bdy) == 0;
            }
        } else {
            return false;
        }
    }

    private static boolean eqString(CharSequence x, Object y) {
        while(true) {
            if (y != null && !Undefined.isUndefined(y)) {
                if (!(y instanceof CharSequence)) {
                    if (y instanceof BigInteger) {
                        BigInteger bix;
                        try {
                            bix = toBigInt((Object)x);
                        } catch (EcmaError var4) {
                            return false;
                        }

                        return bix.equals(y);
                    }

                    if (y instanceof Number) {
                        return toNumber(x.toString()) == ((Number)y).doubleValue();
                    }

                    if (y instanceof Boolean) {
                        return toNumber(x.toString()) == ((Boolean)y ? 1.0 : 0.0);
                    }

                    if (isSymbol(y)) {
                        return false;
                    }

                    if (y instanceof Scriptable) {
                        if (y instanceof ScriptableObject) {
                            Object test = ((ScriptableObject)y).equivalentValues(x.toString());
                            if (test != Scriptable.NOT_FOUND) {
                                return (Boolean)test;
                            }
                        }

                        y = toPrimitive(y);
                        continue;
                    }

                    warnAboutNonJSObject(y);
                    return false;
                }

                CharSequence c = (CharSequence)y;
                return x.length() == c.length() && x.toString().equals(c.toString());
            }

            return false;
        }
    }

    public static boolean shallowEq(Object x, Object y) {
        if (x == y) {
            if (!(x instanceof Number)) {
                return true;
            } else {
                double d = ((Number)x).doubleValue();
                return !Double.isNaN(d);
            }
        } else if (x != null && x != Undefined.instance && x != Undefined.SCRIPTABLE_UNDEFINED) {
            if (x instanceof BigInteger) {
                if (y instanceof BigInteger) {
                    return x.equals(y);
                }
            } else if (x instanceof Number && !(x instanceof BigInteger)) {
                if (y instanceof Number && !(y instanceof BigInteger)) {
                    return ((Number)x).doubleValue() == ((Number)y).doubleValue();
                }
            } else if (x instanceof CharSequence) {
                if (y instanceof CharSequence) {
                    return x.toString().equals(y.toString());
                }
            } else if (x instanceof Boolean) {
                if (y instanceof Boolean) {
                    return x.equals(y);
                }
            } else {
                if (!(x instanceof Scriptable)) {
                    warnAboutNonJSObject(x);
                    return x == y;
                }

                if (x instanceof Wrapper && y instanceof Wrapper) {
                    return ((Wrapper)x).unwrap() == ((Wrapper)y).unwrap();
                }

                if (x instanceof Delegator) {
                    x = ((Delegator)x).getDelegee();
                    if (y instanceof Delegator) {
                        return shallowEq(x, ((Delegator)y).getDelegee());
                    }

                    if (x == y) {
                        return true;
                    }
                }

                if (y instanceof Delegator && ((Delegator)y).getDelegee() == x) {
                    return true;
                }
            }

            return false;
        } else {
            return x == Undefined.instance && y == Undefined.SCRIPTABLE_UNDEFINED || x == Undefined.SCRIPTABLE_UNDEFINED && y == Undefined.instance;
        }
    }

    public static boolean instanceOf(Object a, Object b, Context cx) {
        if (!(b instanceof Scriptable)) {
            throw typeErrorById("msg.instanceof.not.object");
        } else {
            return !(a instanceof Scriptable) ? false : ((Scriptable)b).hasInstance((Scriptable)a);
        }
    }

    public static boolean jsDelegatesTo(Scriptable lhs, Scriptable rhs) {
        for(Scriptable proto = lhs.getPrototype(); proto != null; proto = proto.getPrototype()) {
            if (proto.equals(rhs)) {
                return true;
            }
        }

        return false;
    }

    public static boolean in(Object a, Object b, Context cx) {
        if (!(b instanceof Scriptable)) {
            throw typeErrorById("msg.in.not.object");
        } else {
            return hasObjectElem((Scriptable)b, a, cx);
        }
    }

    public static boolean compare(Object val1, Object val2, int op) {
        assert op == 17 || op == 15 || op == 16 || op == 14;

        if (val1 instanceof Number && val2 instanceof Number) {
            return compare((Number)val1, (Number)val2, op);
        } else if (!(val1 instanceof Symbol) && !(val2 instanceof Symbol)) {
            if (val1 instanceof Scriptable) {
                val1 = ((Scriptable)val1).getDefaultValue(NumberClass);
            }

            if (val2 instanceof Scriptable) {
                val2 = ((Scriptable)val2).getDefaultValue(NumberClass);
            }

            return val1 instanceof CharSequence && val2 instanceof CharSequence ? compareTo(val1.toString(), val2.toString(), op) : compare(toNumeric(val1), toNumeric(val2), op);
        } else {
            throw typeErrorById("msg.compare.symbol");
        }
    }

    public static boolean compare(Number val1, Number val2, int op) {
        assert op == 17 || op == 15 || op == 16 || op == 14;

        if (val1 instanceof BigInteger && val2 instanceof BigInteger) {
            return compareTo((BigInteger)val1, (BigInteger)val2, op);
        } else if (!(val1 instanceof BigInteger) && !(val2 instanceof BigInteger)) {
            return compareTo(val1.doubleValue(), val2.doubleValue(), op);
        } else {
            BigDecimal bd1;
            if (val1 instanceof BigInteger) {
                bd1 = new BigDecimal((BigInteger)val1);
            } else {
                double d = val1.doubleValue();
                if (Double.isNaN(d)) {
                    return false;
                }

                if (d == Double.POSITIVE_INFINITY) {
                    return op == 17 || op == 16;
                }

                if (d == Double.NEGATIVE_INFINITY) {
                    return op == 15 || op == 14;
                }

                bd1 = new BigDecimal(d, MathContext.UNLIMITED);
            }

            BigDecimal bd2;
            if (val2 instanceof BigInteger) {
                bd2 = new BigDecimal((BigInteger)val2);
            } else {
                double d = val2.doubleValue();
                if (Double.isNaN(d)) {
                    return false;
                }

                if (d == Double.POSITIVE_INFINITY) {
                    return op == 15 || op == 14;
                }

                if (d == Double.NEGATIVE_INFINITY) {
                    return op == 17 || op == 16;
                }

                bd2 = new BigDecimal(d, MathContext.UNLIMITED);
            }

            return compareTo(bd1, bd2, op);
        }
    }

    private static <T> boolean compareTo(Comparable<T> val1, T val2, int op) {
        switch (op) {
            case 14:
                return val1.compareTo(val2) < 0;
            case 15:
                return val1.compareTo(val2) <= 0;
            case 16:
                return val1.compareTo(val2) > 0;
            case 17:
                return val1.compareTo(val2) >= 0;
            default:
                throw Kit.codeBug();
        }
    }

    private static <T> boolean compareTo(double d1, double d2, int op) {
        switch (op) {
            case 14:
                return d1 < d2;
            case 15:
                return d1 <= d2;
            case 16:
                return d1 > d2;
            case 17:
                return d1 >= d2;
            default:
                throw Kit.codeBug();
        }
    }

    public static ScriptableObject getGlobal(Context cx) {
        String GLOBAL_CLASS = "org.mozilla.javascript.tools.shell.Global";
        Class<?> globalClass = Kit.classOrNull("org.mozilla.javascript.tools.shell.Global");
        if (globalClass != null) {
            try {
                Class<?>[] parm = new Class[]{ContextClass};
                Constructor<?> globalClassCtor = globalClass.getConstructor(parm);
                Object[] arg = new Object[]{cx};
                return (ScriptableObject)globalClassCtor.newInstance(arg);
            } catch (RuntimeException var6) {
                throw var6;
            } catch (Exception var7) {
            }
        }

        return new ImporterTopLevel(cx);
    }

    public static boolean hasTopCall(Context cx) {
        return cx.topCallScope != null;
    }

    public static Scriptable getTopCallScope(Context cx) {
        Scriptable scope = cx.topCallScope;
        if (scope == null) {
            throw new IllegalStateException();
        } else {
            return scope;
        }
    }

    /** @deprecated */
    @Deprecated
    public static Object doTopCall(Callable callable, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        return doTopCall(callable, cx, scope, thisObj, args, cx.isTopLevelStrict);
    }

    public static Object doTopCall(Callable callable, Context cx, Scriptable scope, Scriptable thisObj, Object[] args, boolean isTopLevelStrict) {
        if (scope == null) {
            throw new IllegalArgumentException();
        } else if (cx.topCallScope != null) {
            throw new IllegalStateException();
        } else {
            cx.topCallScope = ScriptableObject.getTopLevelScope(scope);
            cx.useDynamicScope = cx.hasFeature(7);
            boolean previousTopLevelStrict = cx.isTopLevelStrict;
            cx.isTopLevelStrict = isTopLevelStrict;
            ContextFactory f = cx.getFactory();

            Object result;
            try {
                result = f.doTopCall(callable, cx, scope, thisObj, args);
            } finally {
                cx.topCallScope = null;
                cx.cachedXMLLib = null;
                cx.isTopLevelStrict = previousTopLevelStrict;
                if (cx.currentActivationCall != null) {
                    throw new IllegalStateException();
                }

            }

            return result;
        }
    }

    static Scriptable checkDynamicScope(Scriptable possibleDynamicScope, Scriptable staticTopScope) {
        if (possibleDynamicScope == staticTopScope) {
            return possibleDynamicScope;
        } else {
            Scriptable proto = possibleDynamicScope;

            do {
                proto = proto.getPrototype();
                if (proto == staticTopScope) {
                    return possibleDynamicScope;
                }
            } while(proto != null);

            return staticTopScope;
        }
    }

    public static void addInstructionCount(Context cx, int instructionsToAdd) {
        cx.instructionCount += instructionsToAdd;
        if (cx.instructionCount > cx.instructionThreshold) {
            cx.observeInstructionCount(cx.instructionCount);
            cx.instructionCount = 0;
        }

    }

    public static void initScript(NativeFunction funObj, Scriptable thisObj, Context cx, Scriptable scope, boolean evalScript) {
        if (cx.topCallScope == null) {
            throw new IllegalStateException();
        } else {
            int varCount = funObj.getParamAndVarCount();
            if (varCount != 0) {
                Scriptable varScope;
                for(varScope = scope; varScope instanceof NativeWith; varScope = varScope.getParentScope()) {
                }

                int i = varCount;

                while(true) {
                    while(true) {
                        while(i-- != 0) {
                            String name = funObj.getParamOrVarName(i);
                            boolean isConst = funObj.getParamOrVarConst(i);
                            if (!ScriptableObject.hasProperty(scope, name)) {
                                if (isConst) {
                                    ScriptableObject.defineConstProperty(varScope, name);
                                } else if (!evalScript) {
                                    if (!(funObj instanceof InterpretedFunction) || ((InterpretedFunction)funObj).hasFunctionNamed(name)) {
                                        ScriptableObject.defineProperty(varScope, name, Undefined.instance, 4);
                                    }
                                } else {
                                    varScope.put(name, varScope, Undefined.instance);
                                }
                            } else {
                                ScriptableObject.redefineProperty(scope, name, isConst);
                            }
                        }

                        return;
                    }
                }
            }
        }
    }

    /** @deprecated */
    @Deprecated
    public static Scriptable createFunctionActivation(NativeFunction funObj, Scriptable scope, Object[] args) {
        return createFunctionActivation(funObj, scope, args, false);
    }

    public static Scriptable createFunctionActivation(NativeFunction funObj, Scriptable scope, Object[] args, boolean isStrict) {
        return new NativeCall(funObj, scope, args, false, isStrict);
    }

    public static Scriptable createArrowFunctionActivation(NativeFunction funObj, Scriptable scope, Object[] args, boolean isStrict) {
        return new NativeCall(funObj, scope, args, true, isStrict);
    }

    public static void enterActivationFunction(Context cx, Scriptable scope) {
        if (cx.topCallScope == null) {
            throw new IllegalStateException();
        } else {
            NativeCall call = (NativeCall)scope;
            call.parentActivationCall = cx.currentActivationCall;
            cx.currentActivationCall = call;
            call.defineAttributesForArguments();
        }
    }

    public static void exitActivationFunction(Context cx) {
        NativeCall call = cx.currentActivationCall;
        cx.currentActivationCall = call.parentActivationCall;
        call.parentActivationCall = null;
    }

    static NativeCall findFunctionActivation(Context cx, Function f) {
        for(NativeCall call = cx.currentActivationCall; call != null; call = call.parentActivationCall) {
            if (call.function == f) {
                return call;
            }
        }

        return null;
    }

    public static Scriptable newCatchScope(Throwable t, Scriptable lastCatchScope, String exceptionName, Context cx, Scriptable scope) {
        Object obj;
        boolean cacheObj;
        NativeObject catchScopeObject;
        if (t instanceof JavaScriptException) {
            cacheObj = false;
            obj = ((JavaScriptException)t).getValue();
        } else {
            cacheObj = true;
            if (lastCatchScope != null) {
                catchScopeObject = (NativeObject)lastCatchScope;
                obj = catchScopeObject.getAssociatedValue(t);
                if (obj == null) {
                    Kit.codeBug();
                }
            } else {
                Throwable javaException = null;
                NativeErrors type;
                String errorMsg;
                Object re;
                if (t instanceof EcmaError) {
                    EcmaError ee = (EcmaError)t;
                    re = ee;
                    type = NativeErrors.valueOf(ee.getName());
                    errorMsg = ee.getErrorMessage();
                } else if (t instanceof WrappedException) {
                    WrappedException we = (WrappedException)t;
                    re = we;
                    javaException = we.getWrappedException();
                    if (!isVisible(cx, javaException)) {
                        type = NativeErrors.InternalError;
                        errorMsg = javaException.getMessage();
                    } else {
                        type = NativeErrors.JavaException;
                        errorMsg = javaException.getClass().getName() + ": " + javaException.getMessage();
                    }
                } else if (t instanceof EvaluatorException) {
                    EvaluatorException ee = (EvaluatorException)t;
                    re = ee;
                    type = NativeErrors.InternalError;
                    errorMsg = ee.getMessage();
                } else {
                    if (!cx.hasFeature(13)) {
                        throw Kit.codeBug();
                    }

                    re = new WrappedException(t);
                    type = NativeErrors.JavaException;
                    errorMsg = t.toString();
                }

                String sourceUri = ((RhinoException)re).sourceName();
                if (sourceUri == null) {
                    sourceUri = "";
                }

                int line = ((RhinoException)re).lineNumber();
                Object[] args;
                if (line > 0) {
                    args = new Object[]{errorMsg, sourceUri, line};
                } else {
                    args = new Object[]{errorMsg, sourceUri};
                }

                Scriptable errorObject = newNativeError(cx, scope, type, args);
                if (errorObject instanceof NativeError) {
                    ((NativeError)errorObject).setStackProvider((RhinoException)re);
                }

                Object wrap;
                if (javaException != null && isVisible(cx, javaException)) {
                    wrap = cx.getWrapFactory().wrap(cx, scope, javaException, (Class)null);
                    ScriptableObject.defineProperty(errorObject, "javaException", wrap, 7);
                }

                if (isVisible(cx, re)) {
                    wrap = cx.getWrapFactory().wrap(cx, scope, re, (Class)null);
                    ScriptableObject.defineProperty(errorObject, "rhinoException", wrap, 7);
                }

                obj = errorObject;
            }
        }

        catchScopeObject = new NativeObject();
        catchScopeObject.defineProperty(exceptionName, obj, 4);
        if (isVisible(cx, t)) {
            catchScopeObject.defineProperty("__exception__", Context.javaToJS(t, scope), 6);
        }

        if (cacheObj) {
            catchScopeObject.associateValue(t, obj);
        }

        return catchScopeObject;
    }

    public static Scriptable wrapException(Throwable t, Scriptable scope, Context cx) {
        Throwable javaException = null;
        Object re;
        String errorName;
        String errorMsg;
        if (t instanceof EcmaError) {
            EcmaError ee = (EcmaError)t;
            re = ee;
            errorName = ee.getName();
            errorMsg = ee.getErrorMessage();
        } else if (t instanceof WrappedException) {
            WrappedException we = (WrappedException)t;
            re = we;
            javaException = we.getWrappedException();
            errorName = "JavaException";
            errorMsg = javaException.getClass().getName() + ": " + javaException.getMessage();
        } else if (t instanceof EvaluatorException) {
            EvaluatorException ee = (EvaluatorException)t;
            re = ee;
            errorName = "InternalError";
            errorMsg = ee.getMessage();
        } else {
            if (!cx.hasFeature(13)) {
                throw Kit.codeBug();
            }

            re = new WrappedException(t);
            errorName = "JavaException";
            errorMsg = t.toString();
        }

        String sourceUri = ((RhinoException)re).sourceName();
        if (sourceUri == null) {
            sourceUri = "";
        }

        int line = ((RhinoException)re).lineNumber();
        Object[] args;
        if (line > 0) {
            args = new Object[]{errorMsg, sourceUri, line};
        } else {
            args = new Object[]{errorMsg, sourceUri};
        }

        Scriptable errorObject = cx.newObject(scope, errorName, args);
        ScriptableObject.putProperty(errorObject, "name", errorName);
        if (errorObject instanceof NativeError) {
            ((NativeError)errorObject).setStackProvider((RhinoException)re);
        }

        Object wrap;
        if (javaException != null && isVisible(cx, javaException)) {
            wrap = cx.getWrapFactory().wrap(cx, scope, javaException, (Class)null);
            ScriptableObject.defineProperty(errorObject, "javaException", wrap, 7);
        }

        if (isVisible(cx, re)) {
            wrap = cx.getWrapFactory().wrap(cx, scope, re, (Class)null);
            ScriptableObject.defineProperty(errorObject, "rhinoException", wrap, 7);
        }

        return errorObject;
    }

    private static boolean isVisible(Context cx, Object obj) {
        ClassShutter shutter = cx.getClassShutter();
        return shutter == null || shutter.visibleToScripts(obj.getClass().getName());
    }

    public static Scriptable enterWith(Object obj, Context cx, Scriptable scope) {
        Scriptable sobj = toObjectOrNull(cx, obj, scope);
        if (sobj == null) {
            throw typeErrorById("msg.undef.with", toString(obj));
        } else if (sobj instanceof XMLObject) {
            XMLObject xmlObject = (XMLObject)sobj;
            return xmlObject.enterWith(scope);
        } else {
            return new NativeWith(scope, sobj);
        }
    }

    public static Scriptable leaveWith(Scriptable scope) {
        NativeWith nw = (NativeWith)scope;
        return nw.getParentScope();
    }

    public static Scriptable enterDotQuery(Object value, Scriptable scope) {
        if (!(value instanceof XMLObject)) {
            throw notXmlError(value);
        } else {
            XMLObject object = (XMLObject)value;
            return object.enterDotQuery(scope);
        }
    }

    public static Object updateDotQuery(boolean value, Scriptable scope) {
        NativeWith nw = (NativeWith)scope;
        return nw.updateDotQuery(value);
    }

    public static Scriptable leaveDotQuery(Scriptable scope) {
        NativeWith nw = (NativeWith)scope;
        return nw.getParentScope();
    }

    public static void setFunctionProtoAndParent(BaseFunction fn, Scriptable scope) {
        setFunctionProtoAndParent(fn, scope, false);
    }

    public static void setFunctionProtoAndParent(BaseFunction fn, Scriptable scope, boolean es6GeneratorFunction) {
        fn.setParentScope(scope);
        if (es6GeneratorFunction) {
            fn.setPrototype(ScriptableObject.getGeneratorFunctionPrototype(scope));
        } else {
            fn.setPrototype(ScriptableObject.getFunctionPrototype(scope));
        }

    }

    public static void setObjectProtoAndParent(ScriptableObject object, Scriptable scope) {
        scope = ScriptableObject.getTopLevelScope(scope);
        object.setParentScope(scope);
        Scriptable proto = ScriptableObject.getClassPrototype(scope, object.getClassName());
        object.setPrototype(proto);
    }

    public static void setBuiltinProtoAndParent(ScriptableObject object, Scriptable scope, Builtins type) {
        scope = ScriptableObject.getTopLevelScope(scope);
        object.setParentScope(scope);
        object.setPrototype(TopLevel.getBuiltinPrototype(scope, type));
    }

    public static void initFunction(Context cx, Scriptable scope, NativeFunction function, int type, boolean fromEvalCode) {
        String name;
        if (type == 1) {
            name = function.getFunctionName();
            if (name != null && name.length() != 0) {
                if (!fromEvalCode) {
                    ScriptableObject.defineProperty(scope, name, function, 4);
                } else {
                    scope.put(name, scope, function);
                }
            }
        } else {
            if (type != 3) {
                throw Kit.codeBug();
            }

            name = function.getFunctionName();
            if (name != null && name.length() != 0) {
                while(scope instanceof NativeWith) {
                    scope = scope.getParentScope();
                }

                scope.put(name, scope, function);
            }
        }

    }

    public static Scriptable newArrayLiteral(Object[] objects, int[] skipIndices, Context cx, Scriptable scope) {
        int SKIP_DENSITY = 1;
        int count = objects.length;
        int skipCount = 0;
        if (skipIndices != null) {
            skipCount = skipIndices.length;
        }

        int length = count + skipCount;
        int skip;
        int i;
        int j;
        if (length > 1 && skipCount * 2 < length) {
            Object[] sparse;
            if (skipCount == 0) {
                sparse = objects;
            } else {
                sparse = new Object[length];
                skip = 0;
                i = 0;

                for(j = 0; i != length; ++i) {
                    if (skip != skipCount && skipIndices[skip] == i) {
                        sparse[i] = Scriptable.NOT_FOUND;
                        ++skip;
                    } else {
                        sparse[i] = objects[j];
                        ++j;
                    }
                }
            }

            return cx.newArray(scope, sparse);
        } else {
            Scriptable array = cx.newArray(scope, length);
            skip = 0;
            i = 0;

            for(j = 0; i != length; ++i) {
                if (skip != skipCount && skipIndices[skip] == i) {
                    ++skip;
                } else {
                    array.put(i, array, objects[j]);
                    ++j;
                }
            }

            return array;
        }
    }

    /** @deprecated */
    @Deprecated
    public static Scriptable newObjectLiteral(Object[] propertyIds, Object[] propertyValues, Context cx, Scriptable scope) {
        return newObjectLiteral(propertyIds, propertyValues, (int[])null, cx, scope);
    }

    public static Scriptable newObjectLiteral(Object[] propertyIds, Object[] propertyValues, int[] getterSetters, Context cx, Scriptable scope) {
        Scriptable object = cx.newObject(scope);
        int end = propertyIds == null ? 0 : propertyIds.length;

        for(int i = 0; i != end; ++i) {
            Object id = propertyIds[i];
            int getterSetter = getterSetters == null ? 0 : getterSetters[i];
            Object value = propertyValues[i];
            if (getterSetter == 0) {
                if (id instanceof Symbol) {
                    Symbol sym = (Symbol)id;
                    SymbolScriptable so = (SymbolScriptable)object;
                    so.put(sym, object, value);
                } else if (id instanceof Integer) {
                    int index = (Integer)id;
                    object.put(index, object, value);
                } else {
                    String stringId = toString(id);
                    if (isSpecialProperty(stringId)) {
                        Ref ref = specialRef(object, stringId, cx, scope);
                        ref.set(cx, scope, value);
                    } else {
                        object.put(stringId, object, value);
                    }
                }
            } else {
                ScriptableObject so = (ScriptableObject)object;
                Callable getterOrSetter = (Callable)value;
                boolean isSetter = getterSetter == 1;
                String key = id instanceof String ? (String)id : null;
                int index = key == null ? (Integer)id : 0;
                so.setGetterOrSetter(key, index, getterOrSetter, isSetter);
            }
        }

        return object;
    }

    public static boolean isArrayObject(Object obj) {
        return obj instanceof NativeArray || obj instanceof Arguments;
    }

    public static Object[] getArrayElements(Scriptable object) {
        Context cx = Context.getContext();
        long longLen = NativeArray.getLengthProperty(cx, object);
        if (longLen > 2147483647L) {
            throw new IllegalArgumentException();
        } else {
            int len = (int)longLen;
            if (len == 0) {
                return emptyArgs;
            } else {
                Object[] result = new Object[len];

                for(int i = 0; i < len; ++i) {
                    Object elem = ScriptableObject.getProperty(object, i);
                    result[i] = elem == Scriptable.NOT_FOUND ? Undefined.instance : elem;
                }

                return result;
            }
        }
    }

    static void checkDeprecated(Context cx, String name) {
        int version = cx.getLanguageVersion();
        if (version >= 140 || version == 0) {
            String msg = getMessageById("msg.deprec.ctor", name);
            if (version != 0) {
                throw Context.reportRuntimeError(msg);
            }

            Context.reportWarning(msg);
        }

    }

    /** @deprecated */
    @Deprecated
    public static String getMessage0(String messageId) {
        return getMessage(messageId, (Object[])null);
    }

    /** @deprecated */
    @Deprecated
    public static String getMessage1(String messageId, Object arg1) {
        Object[] arguments = new Object[]{arg1};
        return getMessage(messageId, arguments);
    }

    /** @deprecated */
    @Deprecated
    public static String getMessage2(String messageId, Object arg1, Object arg2) {
        Object[] arguments = new Object[]{arg1, arg2};
        return getMessage(messageId, arguments);
    }

    /** @deprecated */
    @Deprecated
    public static String getMessage3(String messageId, Object arg1, Object arg2, Object arg3) {
        Object[] arguments = new Object[]{arg1, arg2, arg3};
        return getMessage(messageId, arguments);
    }

    /** @deprecated */
    @Deprecated
    public static String getMessage4(String messageId, Object arg1, Object arg2, Object arg3, Object arg4) {
        Object[] arguments = new Object[]{arg1, arg2, arg3, arg4};
        return getMessage(messageId, arguments);
    }

    /** @deprecated */
    @Deprecated
    public static String getMessage(String messageId, Object[] arguments) {
        return messageProvider.getMessage(messageId, arguments);
    }

    public static String getMessageById(String messageId, Object... args) {
        return messageProvider.getMessage(messageId, args);
    }

    public static EcmaError constructError(String error, String message) {
        int[] linep = new int[1];
        String filename = Context.getSourcePositionFromStack(linep);
        return constructError(error, message, filename, linep[0], (String)null, 0);
    }

    public static EcmaError constructError(String error, String message, int lineNumberDelta) {
        int[] linep = new int[1];
        String filename = Context.getSourcePositionFromStack(linep);
        if (linep[0] != 0) {
            linep[0] += lineNumberDelta;
        }

        return constructError(error, message, filename, linep[0], (String)null, 0);
    }

    public static EcmaError constructError(String error, String message, String sourceName, int lineNumber, String lineSource, int columnNumber) {
        return new EcmaError(error, message, sourceName, lineNumber, lineSource, columnNumber);
    }

    public static EcmaError rangeError(String message) {
        return constructError("RangeError", message);
    }

    public static EcmaError rangeErrorById(String messageId, Object... args) {
        String msg = getMessageById(messageId, args);
        return rangeError(msg);
    }

    public static EcmaError typeError(String message) {
        return constructError("TypeError", message);
    }

    public static EcmaError typeErrorById(String messageId, Object... args) {
        String msg = getMessageById(messageId, args);
        return typeError(msg);
    }

    /** @deprecated */
    @Deprecated
    public static EcmaError typeError0(String messageId) {
        String msg = getMessage0(messageId);
        return typeError(msg);
    }

    /** @deprecated */
    @Deprecated
    public static EcmaError typeError1(String messageId, Object arg1) {
        String msg = getMessage1(messageId, arg1);
        return typeError(msg);
    }

    /** @deprecated */
    @Deprecated
    public static EcmaError typeError2(String messageId, Object arg1, Object arg2) {
        String msg = getMessage2(messageId, arg1, arg2);
        return typeError(msg);
    }

    /** @deprecated */
    @Deprecated
    public static EcmaError typeError3(String messageId, String arg1, String arg2, String arg3) {
        String msg = getMessage3(messageId, arg1, arg2, arg3);
        return typeError(msg);
    }

    public static RuntimeException undefReadError(Object object, Object id) {
        return typeErrorById("msg.undef.prop.read", toString(object), toString(id));
    }

    public static RuntimeException undefCallError(Object object, Object id) {
        return typeErrorById("msg.undef.method.call", toString(object), toString(id));
    }

    public static RuntimeException undefWriteError(Object object, Object id, Object value) {
        return typeErrorById("msg.undef.prop.write", toString(object), toString(id), toString(value));
    }

    private static RuntimeException undefDeleteError(Object object, Object id) {
        throw typeErrorById("msg.undef.prop.delete", toString(object), toString(id));
    }

    public static RuntimeException notFoundError(Scriptable object, String property) {
        String msg = getMessageById("msg.is.not.defined", property);
        throw constructError("ReferenceError", msg);
    }

    public static RuntimeException notFunctionError(Object value) {
        return notFunctionError(value, value);
    }

    public static RuntimeException notFunctionError(Object value, Object messageHelper) {
        String msg = messageHelper == null ? "null" : messageHelper.toString();
        return value == Scriptable.NOT_FOUND ? typeErrorById("msg.function.not.found", msg) : typeErrorById("msg.isnt.function", msg, typeof(value));
    }

    public static RuntimeException notFunctionError(Object obj, Object value, String propertyName) {
        String objString = toString(obj);
        if (obj instanceof NativeFunction) {
            int paren = objString.indexOf(41);
            int curly = objString.indexOf(123, paren);
            if (curly > -1) {
                objString = objString.substring(0, curly + 1) + "...}";
            }
        }

        return value == Scriptable.NOT_FOUND ? typeErrorById("msg.function.not.found.in", propertyName, objString) : typeErrorById("msg.isnt.function.in", propertyName, objString, typeof(value));
    }

    private static RuntimeException notXmlError(Object value) {
        throw typeErrorById("msg.isnt.xml.object", toString(value));
    }

    public static EcmaError syntaxError(String message) {
        return constructError("SyntaxError", message);
    }

    public static EcmaError syntaxErrorById(String messageId, Object... args) {
        String msg = getMessageById(messageId, args);
        return syntaxError(msg);
    }

    private static void warnAboutNonJSObject(Object nonJSObject) {
        String omitParam = getMessageById("params.omit.non.js.object.warning");
        if (!"true".equals(omitParam)) {
            String message = getMessageById("msg.non.js.object.warning", nonJSObject, nonJSObject.getClass().getName());
            Context.reportWarning(message);
            System.err.println(message);
        }

    }

    public static RegExpProxy getRegExpProxy(Context cx) {
        return cx.getRegExpProxy();
    }

    public static void setRegExpProxy(Context cx, RegExpProxy proxy) {
        if (proxy == null) {
            throw new IllegalArgumentException();
        } else {
            cx.regExpProxy = proxy;
        }
    }

    public static RegExpProxy checkRegExpProxy(Context cx) {
        RegExpProxy result = getRegExpProxy(cx);
        if (result == null) {
            throw Context.reportRuntimeErrorById("msg.no.regexp", new Object[0]);
        } else {
            return result;
        }
    }

    public static Scriptable wrapRegExp(Context cx, Scriptable scope, Object compiled) {
        return cx.getRegExpProxy().wrapRegExp(cx, scope, compiled);
    }

    public static Scriptable getTemplateLiteralCallSite(Context cx, Scriptable scope, Object[] strings, int index) {
        Object callsite = strings[index];
        if (callsite instanceof Scriptable) {
            return (Scriptable)callsite;
        } else {
            assert callsite instanceof String[];

            String[] vals = (String[])((String[])callsite);

            assert (vals.length & 1) == 0;

            ScriptableObject siteObj = (ScriptableObject)cx.newArray(scope, vals.length >>> 1);
            ScriptableObject rawObj = (ScriptableObject)cx.newArray(scope, vals.length >>> 1);
            siteObj.put("raw", siteObj, rawObj);
            siteObj.setAttributes("raw", 2);
            int i = 0;

            for(int n = vals.length; i < n; i += 2) {
                int idx = i >>> 1;
                siteObj.put(idx, siteObj, vals[i] == null ? Undefined.instance : vals[i]);
                rawObj.put(idx, rawObj, vals[i + 1]);
            }

            AbstractEcmaObjectOperations.setIntegrityLevel(cx, rawObj, INTEGRITY_LEVEL.FROZEN);
            AbstractEcmaObjectOperations.setIntegrityLevel(cx, siteObj, INTEGRITY_LEVEL.FROZEN);
            strings[index] = siteObj;
            return siteObj;
        }
    }

    private static XMLLib currentXMLLib(Context cx) {
        if (cx.topCallScope == null) {
            throw new IllegalStateException();
        } else {
            XMLLib xmlLib = cx.cachedXMLLib;
            if (xmlLib == null) {
                xmlLib = XMLLib.extractFromScope(cx.topCallScope);
                if (xmlLib == null) {
                    throw new IllegalStateException();
                }

                cx.cachedXMLLib = xmlLib;
            }

            return xmlLib;
        }
    }

    public static String escapeAttributeValue(Object value, Context cx) {
        XMLLib xmlLib = currentXMLLib(cx);
        return xmlLib.escapeAttributeValue(value);
    }

    public static String escapeTextValue(Object value, Context cx) {
        XMLLib xmlLib = currentXMLLib(cx);
        return xmlLib.escapeTextValue(value);
    }

    public static Ref memberRef(Object obj, Object elem, Context cx, int memberTypeFlags) {
        if (!(obj instanceof XMLObject)) {
            throw notXmlError(obj);
        } else {
            XMLObject xmlObject = (XMLObject)obj;
            return xmlObject.memberRef(cx, elem, memberTypeFlags);
        }
    }

    public static Ref memberRef(Object obj, Object namespace, Object elem, Context cx, int memberTypeFlags) {
        if (!(obj instanceof XMLObject)) {
            throw notXmlError(obj);
        } else {
            XMLObject xmlObject = (XMLObject)obj;
            return xmlObject.memberRef(cx, namespace, elem, memberTypeFlags);
        }
    }

    public static Ref nameRef(Object name, Context cx, Scriptable scope, int memberTypeFlags) {
        XMLLib xmlLib = currentXMLLib(cx);
        return xmlLib.nameRef(cx, name, scope, memberTypeFlags);
    }

    public static Ref nameRef(Object namespace, Object name, Context cx, Scriptable scope, int memberTypeFlags) {
        XMLLib xmlLib = currentXMLLib(cx);
        return xmlLib.nameRef(cx, namespace, name, scope, memberTypeFlags);
    }

    public static void storeUint32Result(Context cx, long value) {
        if (value >>> 32 != 0L) {
            throw new IllegalArgumentException();
        } else {
            cx.scratchUint32 = value;
        }
    }

    public static long lastUint32Result(Context cx) {
        long value = cx.scratchUint32;
        if (value >>> 32 != 0L) {
            throw new IllegalStateException();
        } else {
            return value;
        }
    }

    private static void storeScriptable(Context cx, Scriptable value) {
        if (cx.scratchScriptable != null) {
            throw new IllegalStateException();
        } else {
            cx.scratchScriptable = value;
        }
    }

    public static Scriptable lastStoredScriptable(Context cx) {
        Scriptable result = cx.scratchScriptable;
        cx.scratchScriptable = null;
        return result;
    }

    static String makeUrlForGeneratedScript(boolean isEval, String masterScriptUrl, int masterScriptLine) {
        return isEval ? masterScriptUrl + '#' + masterScriptLine + "(eval)" : masterScriptUrl + '#' + masterScriptLine + "(Function)";
    }

    static boolean isGeneratedScript(String sourceUrl) {
        return sourceUrl.indexOf("(eval)") >= 0 || sourceUrl.indexOf("(Function)") >= 0;
    }

    static boolean isSymbol(Object obj) {
        return obj instanceof NativeSymbol && ((NativeSymbol)obj).isSymbol() || obj instanceof SymbolKey;
    }

    private static RuntimeException errorWithClassName(String msg, Object val) {
        return Context.reportRuntimeErrorById(msg, new Object[]{val.getClass().getName()});
    }

    public static JavaScriptException throwError(Context cx, Scriptable scope, String message) {
        int[] linep = new int[]{0};
        String filename = Context.getSourcePositionFromStack(linep);
        Scriptable error = newBuiltinObject(cx, scope, Builtins.Error, new Object[]{message, filename, linep[0]});
        return new JavaScriptException(error, filename, linep[0]);
    }

    public static JavaScriptException throwCustomError(Context cx, Scriptable scope, String constructorName, String message) {
        int[] linep = new int[]{0};
        String filename = Context.getSourcePositionFromStack(linep);
        Scriptable error = cx.newObject(scope, constructorName, new Object[]{message, filename, linep[0]});
        return new JavaScriptException(error, filename, linep[0]);
    }

    private static class DefaultMessageProvider implements MessageProvider {
        private DefaultMessageProvider() {
        }

        public String getMessage(String messageId, Object[] arguments) {
            String defaultResource = "org.mozilla.javascript.resources.Messages";
            Context cx = Context.getCurrentContext();
            Locale locale = cx != null ? cx.getLocale() : Locale.getDefault();
            ResourceBundle rb = ResourceBundle.getBundle("org.mozilla.javascript.resources.Messages", locale);

            String formatString;
            try {
                formatString = rb.getString(messageId);
            } catch (MissingResourceException var9) {
                throw new RuntimeException("no message resource found for message property " + messageId);
            }

            MessageFormat formatter = new MessageFormat(formatString);
            return formatter.format(arguments);
        }
    }

    public interface MessageProvider {
        String getMessage(String var1, Object[] var2);
    }

    private static class IdEnumeration implements Serializable {
        private static final long serialVersionUID = 1L;
        Scriptable obj;
        Object[] ids;
        ObjToIntMap used;
        Object currentId;
        int index;
        int enumType;
        boolean enumNumbers;
        Scriptable iterator;

        private IdEnumeration() {
        }
    }

    static final class StringIdOrIndex {
        final String stringId;
        final int index;

        StringIdOrIndex(String stringId) {
            this.stringId = stringId;
            this.index = -1;
        }

        StringIdOrIndex(int index) {
            this.stringId = null;
            this.index = index;
        }
    }

    static class NoSuchMethodShim implements Callable {
        String methodName;
        Callable noSuchMethodMethod;

        NoSuchMethodShim(Callable noSuchMethodMethod, String methodName) {
            this.noSuchMethodMethod = noSuchMethodMethod;
            this.methodName = methodName;
        }

        public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
            Object[] nestedArgs = new Object[]{this.methodName, ScriptRuntime.newArrayLiteral(args, (int[])null, cx, scope)};
            return this.noSuchMethodMethod.call(cx, scope, thisObj, nestedArgs);
        }
    }
}
