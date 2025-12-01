package com.udla.markenx.shared.infrastructure.out.data.persistence.jpa.repository;

import com.udla.markenx.shared.infrastructure.out.data.persistence.jpa.entity.BaseJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseJpaRepository extends JpaRepository<BaseJpaEntity, Long> {

}
