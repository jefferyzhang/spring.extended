package com.springextended.core.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  重置 领域事件订阅 拦截工具
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 05 - 05 13:41
 */
public class ResetEventSubscriberHandlerInterceptor implements HandlerInterceptor {
    @Autowired
    private DomainEventStorage storage;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        DomainEventPublisher.instance().reset();

        DomainEventPublisher.instance().subscribe(domainEvent -> storage.add(domainEvent));

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        DomainEventPublisher.instance().reset();
    }
}
