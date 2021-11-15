package cn.jt.smile.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.Instant;
import java.time.ZonedDateTime;

/**
 * @author admin
 */
@SuppressWarnings("all")
@Getter
public class Result<T> {

    public static final String SUCCESSFUL_CODE = "000000";
    public static final String SUCCESSFUL_MESG = "处理成功";
    public static final String WARN_CODE = "0";

    /**
     * 处理结果code
     */
    private String code;
    /**
     * 处理结果描述信息
     */
    private String mesg;
    /**
     * 请求结果生成时间戳
     */
    private Instant time;
    /**
     * 处理结果数据信息
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public Result() {
        this.time = ZonedDateTime.now().toInstant();
    }

    /**
     * 内部使用，用于构造成功的结果
     *
     * @param code
     * @param mesg
     * @param data
     */
    private Result(String code, String mesg, T data) {
        this.code = code;
        this.mesg = mesg;
        this.data = data;
        this.time = ZonedDateTime.now().toInstant();
    }

    /**
     * 快速创建成功结果并返回结果数据
     *
     * @param data
     * @return Result
     */
    public static Result success(Object data) {
        return new Result<>(SUCCESSFUL_CODE, SUCCESSFUL_MESG, data);
    }

    /**
     * 快速创建成功结果
     *
     * @return Result
     */
    public static Result success() {
        return success(null);
    }

    public static Result successBy() {
        return new Result<>("0000", SUCCESSFUL_MESG, null);
    }

    /**
     * 提示语句
     * @param msg
     * @return
     */
    public static Result warn(String msg) {
        return warn(WARN_CODE, msg);
    }

    public static Result warn(String code, String message) {
        return new Result<>(code,message,null);
    }

    /**
     * 自定义错误
     * @param mesg
     * @param data
     * @return
     */
    public static Result fail(String mesg, Object data) {
        return new Result<>("-1",mesg,data);
    }

    /**
     * 成功code=000000
     *
     * @return true/false
     */
    @JsonIgnore
    public boolean isSuccess() {
        return SUCCESSFUL_CODE.equals(this.code);
    }

    /**
     * 失败
     *
     * @return true/false
     */
    @JsonIgnore
    public boolean isFail() {
        return !isSuccess();
    }
}
