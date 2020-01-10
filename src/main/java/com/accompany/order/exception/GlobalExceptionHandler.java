package com.accompany.order.exception;

/**
 * @author Accompany
 * Date:2019/12/28
 */

import com.accompany.order.util.Result;
import com.accompany.order.util.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @Value("${spring.profiles.active}")
    private String active;

    public GlobalExceptionHandler() {
    }

    private void exceptionLogger(HttpServletRequest req, Exception e) {
        log.error("{} {}", e.getMessage(), e);
    }

    @PostConstruct
    void init() {
        log.info("全局异常捕获已启动");
    }

    @ResponseBody
    @ExceptionHandler({Exception.class})
    public Result<Object> exceptionHandler(HttpServletRequest request, Exception e) {
        log.info("异常捕获");
        if (e instanceof BaseRuntimeException) {
            BaseRuntimeException ex = (BaseRuntimeException)e;
            ex.printStackTrace();
            return Result.fail("出异常拉", ex.getResultCode());
        } else {
            if (e instanceof MethodArgumentNotValidException) {
                MethodArgumentNotValidException ex = (MethodArgumentNotValidException)e;
                ex.printStackTrace();
                return Result.fail(  "出异常拉" , ResultCode.PARAM_ILLEGAL.modifyMessage("参数异常"));
            } else if (e instanceof BindException) {
                BindException ex = (BindException)e;
                ex.printStackTrace();
                return Result.fail( "出异常拉" , ResultCode.PARAM_ILLEGAL.modifyMessage("参数异常"));
            } else {
                //this.exceptionLogger(request, e);
                e.printStackTrace();
                return Result.fail("出异常拉", ResultCode.FAILURE);
            }
        }
    }
}
