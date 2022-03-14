package com.dstz.bpm.act.id;

import org.activiti.engine.impl.cfg.IdGenerator;


public class ActivitiIdGenerator implements IdGenerator {
    private  com.dstz.base.core.id.IdGenerator idGenerator = null;

    public ActivitiIdGenerator() {
    }

    public void setIdGenerator(com.dstz.base.core.id.IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }


    public String getNextId() {
        return this.idGenerator.getSuid();
    }

}

 