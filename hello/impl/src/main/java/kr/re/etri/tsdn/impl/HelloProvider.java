/*
 * Copyright (c) 2015 ETRI and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package kr.re.etri.tsdn.impl;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.RpcRegistration;
import org.opendaylight.controller.sal.binding.api.BindingAwareProvider;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150105.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloProvider implements BindingAwareProvider, AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(HelloProvider.class);
    private RpcRegistration<HelloService> helloService;
    private DataBroker db;
    
    @Override
    public void onSessionInitiated(ProviderContext session) {
    	
    	
    	HelloImpl hello = new HelloImpl();
    	hello.setSession(session);
        helloService = session.addRpcImplementation(HelloService.class, hello);
        
        db = session.getSALService(DataBroker.class);
        hello.setDb(db);
        
        LOG.info("HelloProvider Session Initiated");
        
    }

    @Override
    public void close() throws Exception {
        LOG.info("HelloProvider Closed");
    }

}
