package com.ahjrlc.common.util;

import com.ahjrlc.common.ArrayListPlus;
import com.ahjrlc.common.consts.CommonConst;
import io.swagger.annotations.ApiModelProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 跨应用通用工具集
 *
 * @author aachen0
 * @date 2019年12月31日
 */
public class CommonUtil {
    private final static Logger log = LoggerFactory.getLogger(CommonUtil.class);
    private final static char[] CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private final static char[] CHARS_NUMBER = "0123456789".toCharArray();

    /**
     * 根据一个文件的byte数组解析文件类型，如果解析失败，返回unknow
     *
     * @param fileBytes 文件数组
     * @return 文件类型
     */
    public static @NotNull String getFileType(@NotNull byte[] fileBytes) {
        if (fileBytes.length > 0) {
            String fileTypeHex = getFileHexString(fileBytes).toUpperCase();
            for (Map.Entry<String, String> entry : FileTypeMap.HEX_MAP.entrySet()) {
                String fileTypeHexValue = entry.getValue();
                if (fileTypeHex.startsWith(fileTypeHexValue)) {
                    return entry.getKey();
                }
            }
        }
        return "unknown";
    }

    /**
     * 获取文件byte数组包含文件类型部分，以十六进制字符串返回
     *
     * @param fileBytes 文件二进制数组
     * @return 前28位含文件类型信息的十六进制字符串形式
     */
    private static String getFileHexString(byte[] fileBytes) {
//        包含文件类型的字节长度
        int typeLength = 28;
        StringBuilder stringBuilder = new StringBuilder();
        int length = Math.max(fileBytes.length, typeLength);
        for (int i = 0; i < length; i++) {
            int v = fileBytes[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return new String(stringBuilder);
    }


    /**
     * 对一个字符串序列对中间部分指定长度用指定字符替换，生产打码字符串
     *
     * @param oriString    原始字符串序列
     * @param mosaic       指定马赛克字符
     * @param mosaicLength 指定马赛克长度
     * @return 打码后打字符串
     */
    public static @NotNull String mosaic(@NotNull CharSequence oriString, @NotNull char mosaic, @NotNull int mosaicLength) {
        int totalLength = oriString.length();
        mosaicLength = Math.min(mosaicLength, totalLength);
        int startIndex = Math.round((totalLength - mosaicLength) / 2.0f);
        int endIndex = startIndex + mosaicLength;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < totalLength; i++) {
            if (i >= startIndex && i < endIndex) {
                sb.append(mosaic);
            } else {
                sb.append(oriString.charAt(i));
            }
        }
        return new String(sb);
    }

    /**
     * 将数组转换成mysql可用的in语法字符串，智能判断数据类型
     *
     * @param list 列表集合
     * @return 包含括号的in字符串形式
     */
    public static String inString(List list) {
        if (list != null && list.size() > 0) {
            String s = list.toString();
            if (list.get(0) instanceof Number) {
                return s.replace('[', '(').replace(']', ')');
            } else {
                return s.replace("[", "('").replace("]", "')").replace(",", "','").replace(" ", "");
            }
        }
        return null;
    }

    /**
     * 将下划线分割的命名转换为java的驼峰命名
     *
     * @param underLineName 下划线名称
     * @param upperCamel    首字母是否大写
     * @return 驼峰命名的实体类名
     */
    public static String camel(@NotNull String underLineName, @NotNull boolean upperCamel) {
        if (!"".equals(underLineName.trim())) {
            int size = underLineName.length();
            char[] chars = underLineName.toCharArray();
            String firstChar = chars[0] + "";
            if (upperCamel) {
                firstChar = firstChar.toUpperCase();
            }
            StringBuffer camelName = new StringBuffer(firstChar);
            for (int i = 1; i < size; i++) {
                if (chars[i] != '_') {
                    camelName.append(chars[i]);
                } else {
                    i++;
                    if (i < size) {
                        camelName.append((chars[i] + "").toUpperCase());
                    }
                }
            }
            return new String(camelName);
        }
        return "";
    }

    /**
     * 比较某个对象的每个属性，并给出结果，如果有一个对象是null则返回null，否则返回比较结果
     * 属性的描述共用swagger的FieldDesc注解
     *
     * @param o1 对象1
     * @param o2 对象2
     * @return 差异列表
     */
    public static @NotNull List<Change> compare(@NotNull Object o1, @NotNull Object o2) {
        List<Change> changes = new ArrayList<>();
        Class<?> srcClazz = o1.getClass();
        if (!srcClazz.getName().equals(o2.getClass().getName())) {
            Change change = new Change();
            change.fieldName = "不同对象，无法比较";
            changes.add(change);
            return changes;
        }
        Class superclass = srcClazz.getSuperclass();
        Field[] fields = srcClazz.getDeclaredFields();
        Field[] superFields = superclass.getDeclaredFields();
        List<Field> allFields = new ArrayList<>();
        Collections.addAll(allFields, fields);
        Collections.addAll(allFields, superFields);
        for (Field field : allFields) {
            ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
            String filedTitle = "";
            if (annotation != null) {
                filedTitle = annotation.value();
            }
            String fieldName = field.getName();
            String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            try {
                Method method;
                try {
                    method = srcClazz.getMethod(getterName);
                } catch (NoSuchMethodException e) {
                    continue;
                }
                Object srcObject = method.invoke(o1);
                String oriValue = srcObject == null ? "null" : toStringPlus(srcObject);
                Object targetObject = method.invoke(o2);
                String newValue = targetObject == null ? "null" : toStringPlus(targetObject);
                if (!oriValue.equals(newValue)) {
                    Change change = new Change();
                    change.setFieldName(filedTitle);
                    change.setOriginValue(oriValue);
                    change.setNewValue(newValue);
                    changes.add(change);
                }
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return changes;
    }

    /**
     * 把source的属性复制给target并返回target,
     *
     * @param source 源对象
     * @param target 目标对象
     */
    public static <T> @NotNull T copyFields(@NotNull Object source, @NotNull T target) {
        return copyFields(source, target, null);
    }

    public static <T> @NotNull T copyFields(@NotNull Object source, @NotNull T target, Map<String, String> fieldMapper) {
        return copyFields(source, target, fieldMapper, null);
    }

    public static <T> @NotNull T copyFields(@NotNull Object source, @NotNull T target, Map<String, String> fieldMapper, Map<String, Object> valueMapper) {
        return copyFields(source, target, fieldMapper, valueMapper, null);
    }

    /**
     * 把source的属性复制给target并返回target,如果提供字段映射mapper则优先使用映射属性复制
     *
     * @param source       源对象
     * @param target       目标对象
     * @param filterFields 希望屏蔽的属性列表
     */
    public static <T> @NotNull T copyFields(@NotNull Object source, @NotNull T target, Map<String, String> fieldMapper, Map<String, Object> valueMapper, List<String> filterFields) {
        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();
        List<Field> sourceFields = CommonUtil.getAllFields(sourceClass);
        List<Field> targetFields = CommonUtil.getAllFields(targetClass);
        for (Field field : targetFields) {
            field.setAccessible(true);
//            目标属性名
            String targetFieldName = field.getName();
//            隐藏属性的值
            if (filterFields != null && filterFields.contains(targetFieldName)) {
                continue;
            }
//            跳过属性
            if ("serialVersionUID".equals(targetFieldName)) {
                continue;
            }
            String sourceFieldName = targetFieldName;
            if (fieldMapper != null) {
                String temp = fieldMapper.get(targetFieldName);
                if (temp != null && !"".equals(temp)) {
                    sourceFieldName = temp;
                }
            }
            Field sourceField = findFieldByName(sourceFields, sourceFieldName);
            if (sourceField != null) {
                sourceField.setAccessible(true);
                try {
                    field.set(target, sourceField.get(source));
                } catch (IllegalAccessException e) {
                    log.warn("属性({})复制失败", sourceFieldName);
                    e.printStackTrace();
                }
            }
            if (valueMapper != null && valueMapper.size() > 0) {
                Object targetFieldValue = valueMapper.get(targetFieldName);
//            有指定值
                if (targetFieldValue != null) {
                    try {
                        field.set(target, targetFieldValue);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return target;
    }

    /**
     * @param fields    待查Field列表
     * @param fieldName 查找的Field名
     * @return Field对象，如果未查到，返回null
     */
    private static Field findFieldByName(@NotNull List<Field> fields, @NotNull String fieldName) {
        for (Field field : fields) {
            if (fieldName.equals(field.getName())) {
                return field;
            }
        }
        return null;
    }

    /**
     * 递归查出指定类包含所有父级类中Field
     *
     * @param clazz 查询类对象
     * @return Field列表
     */
    private static @NotNull List<Field> getAllFields(@NotNull Class clazz) {
        List<Field> fields = new ArrayList<>();
        final String objectClazzName = "java.lang.object";
        while (!objectClazzName.equals(clazz.getName().toLowerCase())) {
            //当父类为null的时候说明到达了最上层的父类(Object类).
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass(); //得到父类,然后赋给自己
        }
        return fields;
    }

    /**
     * 将一个集合中的元素替换称新的元素，并复制同名属性值
     *
     * @param collection 集合
     * @param clazz      新元素类型
     * @param <E>        新元素类型
     * @return 新元素列表
     */
    public static <E> @NotNull List<E> copyElementFields(@NotNull Collection<?> collection, @NotNull Class<E> clazz) {
        return copyElementFields(collection, clazz, null);
    }

    public static <E> @NotNull List<E> copyElementFields(@NotNull Collection<?> collection, @NotNull Class<E> clazz, Map<String, String> fieldMapper) {
        return copyElementFields(collection, clazz, fieldMapper, null);
    }

    /**
     * 将一个集合中的元素替换为指定类型元素，并按连个映射表传递属性值，如果未找到映射，则按相同属性名传值
     *
     * @param collection  集合
     * @param clazz       新元素类型
     * @param fieldMapper 集合源元素与目标元素属性名关系映射，key为源元素属性名，value为目标元素属性名
     * @param valueMapper 新元素属性指定值映射表，key为目标元素属性名，value为目标元素属性指定值
     * @param <E>         新元素类型
     * @return 新元素列表
     */
    public static <E> List<E> copyElementFields(@NotNull Collection collection, @NotNull Class<E> clazz, Map<String, String> fieldMapper, Map<String, Object> valueMapper) {
        List<E> target = new ArrayList<>();
        for (Object e : collection) {
            try {
                target.add(copyFields(e, clazz.newInstance(), fieldMapper, valueMapper));
            } catch (InstantiationException | IllegalAccessException ex) {
                log.warn(ex.getMessage());
                ex.printStackTrace();
            }
        }
        return target;
    }

    public static <T> T getElementById(List<T> c, Integer id, Class<T> clazz) {
        Field idField = null;
        try {
            idField = clazz.getDeclaredField("id");
            idField.setAccessible(true);
            for (T t : c) {
                int tId = (int) idField.get(t);
                if (tId == id) {
                    return t;
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 两个对象变化描述
     */
    private static class Change {
        private String fieldName;
        private String originValue;
        private String newValue;

        String getFieldName() {
            return fieldName;
        }

        void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        String getOriginValue() {
            return originValue;
        }

        void setOriginValue(String originValue) {
            this.originValue = originValue;
        }

        public String getNewValue() {
            return newValue;
        }

        void setNewValue(String newValue) {
            this.newValue = newValue;
        }
    }

    private static String toStringPlus(Object o) {
        if (o instanceof Date) {
            Date oriDate = (Date) o;
            return new SimpleDateFormat(CommonConst.DATE_PATTERN).format(oriDate);
        }
        if (o instanceof BigDecimal) {
            BigDecimal decimal = (BigDecimal) o;
            return decimal.stripTrailingZeros().toString();
        }
        return o.toString();
    }


    /**
     * 生成以当前时间为前缀的16位随机码
     *
     * @return
     */
    public static String generateQueryCode() {
        String code = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
        code = code + generateRandomCode(CHARS, 4);
        return code;
    }

    /**
     * 生成6位数字手机验证
     *
     * @return 验证码
     */
    public static String generateVerifyCode() {
        return generateRandomCode(CHARS_NUMBER, 6);
    }

    /**
     * 使用指定字符集生成指定长度随机码
     *
     * @param chars 字符集
     * @param size  随机码长度
     * @return 随机码
     */
    public static String generateRandomCode(char[] chars, int size) {
        if (size > 0) {
            int length = chars.length;
            StringBuilder code = new StringBuilder();
            for (int i = 0; i < size; i++) {
                code.append(chars[new Random().nextInt(length)]);
            }
            return new String(code);
        } else {
            return "";
        }
    }

    /**
     * 将Date对象转换位Calendar对象
     *
     * @param date
     * @return
     */
    public static Calendar convertDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * 判断一个对象是否为null或者空串或者空集合
     *
     * @param object 对象
     * @return 是否为空
     */
    public static boolean isEmpty(Object object) {
        if (object == null) {
            return true;
        }
        // 字符串
        if (object instanceof String) {
            return ((String) object).trim().length() < 1;
        }
        // 集合
        if (object instanceof Collection) {
            return ((Collection) object).size() < 1;
        }
        // 数组无元素，或者有一个空的元素都为空
        if (object.getClass().isArray()) {
            Object[] objects = (Object[]) object;
            return objects.length < 1;
        }
        return false;
    }

    /**
     * 从一个pojo对象集合中抽取对象的指定属性，放入一个新List中并返回
     *
     * @param c         对象集合
     * @param fieldName 集合对象某个属性名称
     * @param <T>       属性类型
     * @return 属性值的List
     * @deprecated 采用clazz参数传递
     */
    @Deprecated
    public static <T> List<T> extractFieldList(Collection c, String fieldName) {
        String getter = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        return extractValueList(c, getter);
    }

    /**
     * 从一个pojo对象集合中抽取对象的指定属性，放入一个新List中并返回
     *
     * @param c         对象集合
     * @param fieldName 集合对象某个属性名称
     * @param <T>       属性类型
     * @return 属性值的List
     */
    public static <T> List<T> extractFieldList(Collection c, String fieldName, Class<T> clazz) {
        String getter = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        return extractValueList(c, getter, clazz);
    }

    /**
     * 从一个pojo对象集合中抽取对象的指定属性，放入一个新List中并返回
     *
     * @param c          对象集合
     * @param methodName 集合对象获取某个值得方法名
     * @param <T>        属性类型
     * @return 属性值的List
     * @deprecated see override method
     */
    public static <T> List<T> extractValueList(Collection c, String methodName) {
        List<T> list = new ArrayList<>();
        if (!isEmpty(c)) {
            for (Object o : c) {
                if (o != null) {
                    Class<?> clazz = o.getClass();
                    try {
                        Method method = clazz.getDeclaredMethod(methodName);
                        Object field = method.invoke(o);
                        list.add((T) field);
                    } catch (NoSuchMethodException | IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException ite) {
                        list.add(null);
                    }
                } else {
                    list.add(null);
                }
            }
        }
        return list;
    }

    /**
     * 从一个pojo对象集合中抽取对象的指定属性，放入一个新List中并返回
     *
     * @param c          对象集合
     * @param methodName 集合对象获取某个值得方法名
     * @param <T>        属性类型
     * @return 属性值的List
     */
    public static <T> List<T> extractValueList(Collection c, String methodName, Class<T> eClass) {
        List<T> list = new ArrayList<>();
        if (!isEmpty(c)) {
            for (Object o : c) {
                if (o != null) {
                    Class<?> clazz = o.getClass();
                    try {
                        Method method = clazz.getDeclaredMethod(methodName);
                        Object field = method.invoke(o);
                        list.add(eClass.cast(field));
                    } catch (NoSuchMethodException | IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException ite) {
                        list.add(null);
                    }
                } else {
                    list.add(null);
                }
            }
        }
        return list;
    }

    /**
     * 将一个表示字节个数的整数自适应单位格式化输出,保留两位小数
     *
     * @param bytes 整数
     * @return 格式化后的字符串
     */
    public static String formatBytes(Long bytes) {
        String sizeString;
        // 强转为float以免丢失数据
        float tmp = bytes;
        // 保留两位小数,不补零，如果需要补零，使用"#.00"
        DecimalFormat df = new DecimalFormat("#.##");
        int k = 1024;
        if (bytes < 0) {
            sizeString = "Wrong number!";
        } else if (bytes < k) {
            // byte
            sizeString = df.format(tmp) + "B";
        } else if (bytes < (long) k * k) {
            // KB
            sizeString = df.format(tmp / k) + "K";
        } else if (bytes < (long) k * k * k) {
            // MB
            sizeString = df.format(tmp / k / k) + "M";
        } else if (bytes < (long) k * k * k * k) {
            // GB
            sizeString = df.format(tmp / k / k / k) + "G";
        } else if (bytes < (long) k * k * k * k * k) {
            // TB
            sizeString = df.format(tmp / k / k / k / k) + "T";
        } else if (bytes < (long) k * k * k * k * k * k) {
            // PB
            sizeString = df.format(tmp / k / k / k / k / k) + "P";
        } else {
            // EB
            sizeString = df.format(tmp / k / k / k / k / k / k) + "E";
        }
        return sizeString;
    }

    /**
     * 模拟日志功能，将字符串按参数拼接成字符串
     *
     * @param content 含{}占位符的字符串内容
     * @param args    字符串拼接参数
     * @return 用参数依次替换占位符后的字符串
     */
    public static String mockLog(String content, Object... args) {
        if (args != null && args.length > 0) {
            for (Object arg : args) {
                content = content.replaceFirst("\\{}", arg == null ? "null" : arg.toString());
            }
        }
        return content;
    }

    /**
     * 逻辑分页
     *
     * @param collection 集合
     * @param page       页码
     * @param limit      分页大小
     * @return 逻辑分页集合
     */
    public static <T> List<T> pageHelper(List<T> collection, Integer page, Integer limit) {
        ArrayListPlus<T> result = new ArrayListPlus<>();
        int length = collection.size();
        result.setTotal(length);
        int startIndex = (page - 1) * limit;
        int endIndex = page * limit;
        endIndex = Math.min(endIndex, length);
        for (int i = startIndex; i < endIndex; i++) {
            result.add(collection.get(i));
        }
        return result;
    }

    /**
     * 简单查询码实现
     *
     * @return
     */
    public static String generatorQueryCode() {
        String code = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
        code = code + generatorRandomCode(CHARS, 4);
        return code;
    }

    /**
     * 6位数字验证码实现
     *
     * @return
     */
    public static String verifyCode() {
        return generatorRandomCode(CHARS_NUMBER, 6);
    }

    /**
     * 使用提供的字符数组生成指定长度的随机序列
     *
     * @param chars 字符数组
     * @param size  长度
     * @return
     */
    private static String generatorRandomCode(char[] chars, int size) {
        if (size > 0) {
            int length = chars.length;
            StringBuilder code = new StringBuilder();
            for (int i = 0; i < size; i++) {
                code.append(chars[new Random().nextInt(length)]);
            }
            return new String(code);
        } else {
            return "";
        }
    }

    /**
     * 转换字符串的编码方式
     *
     * @param src
     * @param oriCharset
     * @param destCharset
     * @return
     */
    public static String convertCharset(String src, Charset oriCharset, Charset destCharset) {
        return new String(src.getBytes(oriCharset), destCharset);
    }

    /**
     * 获取对象指定属性的值
     *
     * @param object    对象
     * @param fieldName 字段
     * @return 对象字段值字符串形式
     */
    public static String getFieldValue(Object object, String fieldName) {
        String getter = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        Class<?> clazz = object.getClass();
        try {
            Method method = clazz.getDeclaredMethod(getter);
            Object field = method.invoke(object);
            if (field != null) {
                return field.toString();
            }
        } catch (NoSuchMethodException e) {
            log.error("对象属性:{}不存在", fieldName);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 解析时间字符串为时间对象
     *
     * @param dateTime
     * @return
     */
    public static Date parseDate(String dateTime) {
        dateTime = dateTime.replaceAll("\\D", "/");
        System.out.println(dateTime);
        String[] dateFormats = {"MM/dd/yy/HH/mm/ss"
                , "MM/dd/yyyy"};
        for (String dateFormat : dateFormats) {
            SimpleDateFormat format = new SimpleDateFormat(dateFormat);
            try {
                return format.parse(dateTime);
            } catch (ParseException ignored) {
            }
        }
        return null;
    }

    /**
     * 将一个集合对象安全强转为List
     *
     * @param obj   集合对象
     * @param clazz 集合元素类型
     * @param <E>   集合元素类
     * @return ArrayList对象
     */
    public static <E> List<E> castList(Object obj, Class<E> clazz) {
        List<E> result = new ArrayList<>();
        if (obj instanceof Collection) {
            for (Object o : (Collection) obj) {
                result.add(clazz.cast(o));
            }
        }
        return result;
    }

}
