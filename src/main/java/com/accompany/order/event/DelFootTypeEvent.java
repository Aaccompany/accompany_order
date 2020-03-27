package com.accompany.order.event;

import com.accompany.order.service.footType.dto.FootType;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

/**
 * @author Accompany
 */
@Data
public class DelFootTypeEvent extends ApplicationEvent {
    private FootType footType;
    public DelFootTypeEvent(Object source) {
        super(source);
    }
    public DelFootTypeEvent(Object source, FootType footType){
        super(source);
        this.footType = footType;
    }
}
