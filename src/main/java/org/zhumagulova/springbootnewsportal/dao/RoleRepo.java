package org.zhumagulova.springbootnewsportal.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zhumagulova.springbootnewsportal.model.Role;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {

}
