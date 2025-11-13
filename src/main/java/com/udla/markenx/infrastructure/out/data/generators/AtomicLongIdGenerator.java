package com.udla.markenx.infrastructure.out.data.generators;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.udla.markenx.application.ports.out.data.generators.EntityIdGenerator;

@Component
@Scope("prototype")
public class AtomicLongIdGenerator implements EntityIdGenerator {
  private final AtomicLong atomicLong;

  public AtomicLongIdGenerator(long initialValue) {
    this.atomicLong = new AtomicLong(initialValue);
  }

  @Override
  public long nextId() {
    return atomicLong.getAndIncrement();
  }
}
