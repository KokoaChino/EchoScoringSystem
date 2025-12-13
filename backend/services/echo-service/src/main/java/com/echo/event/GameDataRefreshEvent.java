package com.echo.event;

import org.springframework.context.ApplicationEvent;


public class GameDataRefreshEvent extends ApplicationEvent {
    public GameDataRefreshEvent(Object source) {
        super(source);
    }
}