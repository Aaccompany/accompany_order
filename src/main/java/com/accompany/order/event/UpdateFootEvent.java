package com.accompany.order.event;

import com.accompany.order.service.foot.dto.Foot;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

@Data
public class UpdateFootEvent extends ApplicationEvent {
    private Foot foot;

    public UpdateFootEvent(Object source) {
        super(source);
    }

    public UpdateFootEvent(Object source, Foot foot) {
        super(source);
        this.foot = foot;
    }
}
