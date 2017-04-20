package com.mani.demo.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;

import com.mani.demo.ServiceAPI;


/**
 * Created by maninder singh on 24/02/17.
 */
public class ServiceModule extends AbstractModule implements ServiceGuiceSupport {

    @Override
    protected void configure() {

        bindServices(serviceBinding(ServiceAPI.class, ServiceImpl.class));
    }


}
