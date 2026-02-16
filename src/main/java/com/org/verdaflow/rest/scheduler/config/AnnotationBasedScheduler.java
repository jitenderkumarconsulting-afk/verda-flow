package com.org.verdaflow.rest.scheduler.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;

public class AnnotationBasedScheduler implements Scheduler, ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(AnnotationBasedScheduler.class);

    private ApplicationContext applicationContext;
    private ScheduledAnnotationBeanPostProcessor scheduledAwareScheduler;
    private final String schedulerName;

    public AnnotationBasedScheduler(final String schedulerName) {
        this.schedulerName = schedulerName;
    }

    @Override
    public void enable() {
        disable();
        scheduledAwareScheduler = applicationContext.getAutowireCapableBeanFactory()
                .createBean(ScheduledAnnotationBeanPostProcessor.class);
        for (String beanName : applicationContext.getBeanDefinitionNames()) {
            scheduledAwareScheduler.postProcessAfterInitialization(applicationContext.getBean(beanName), beanName);
        }
        scheduledAwareScheduler.onApplicationEvent(new ContextRefreshedEvent(applicationContext));
        log.info("Enabled scheduler {}", schedulerName);
    }

    @Override
    public void disable() {
        if (scheduledAwareScheduler != null) {
            applicationContext.getAutowireCapableBeanFactory().destroyBean(scheduledAwareScheduler);
            scheduledAwareScheduler = null;
            log.info("Disabled scheduler {}", schedulerName);
        }
    }

    @Override
    public String getName() {
        return schedulerName;
    }

    @Override
    public boolean isDisabled() {
        return scheduledAwareScheduler == null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
