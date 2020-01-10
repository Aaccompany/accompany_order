package com.accompany.order.exception;

import com.accompany.order.util.ResultCode;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Accompany
 * Date:2019/12/28
 */
@EqualsAndHashCode
@NoArgsConstructor
public class BaseRuntimeException extends RuntimeException{
    private ResultCode resultCode;

    public BaseRuntimeException(ResultCode resultCode) {
        super(resultCode.toString());
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return this.resultCode;
    }

    public void setResultCode(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof BaseRuntimeException)) {
            return false;
        } else {
            BaseRuntimeException other = (BaseRuntimeException)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$resultCode = this.getResultCode();
                Object other$resultCode = other.getResultCode();
                if (this$resultCode == null) {
                    if (other$resultCode != null) {
                        return false;
                    }
                } else if (!this$resultCode.equals(other$resultCode)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof BaseRuntimeException;
    }
}
