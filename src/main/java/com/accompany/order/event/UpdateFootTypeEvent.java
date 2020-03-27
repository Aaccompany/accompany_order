package com.accompany.order.event;

import com.accompany.order.service.footType.dto.FootType;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

/**
 * @author Accompany
 */
@Data
public class UpdateFootTypeEvent extends ApplicationEvent {
    private FootType footType;
    public UpdateFootTypeEvent(Object source) {
        super(source);
    }
    public UpdateFootTypeEvent(Object source,FootType footType){
        super(source);
        this.footType = footType;
    }
}
