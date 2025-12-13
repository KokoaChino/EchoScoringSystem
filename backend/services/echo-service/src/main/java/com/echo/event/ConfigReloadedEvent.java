package com.echo.event;

import org.springframework.context.ApplicationEvent;


public class ConfigReloadedEvent extends ApplicationEvent {
    public ConfigReloadedEvent(Object source) {
        super(source);
    }
}