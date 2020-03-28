package com.accompany.order.event;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

@Data
public class DelFootEvent extends ApplicationEvent {

    private Long footId;

    public DelFootEvent(Object source) {
        super(source);
    }

    public DelFootEvent(Object source, Long footId) {
        super(source);
        this.footId = footId;
    }
}
