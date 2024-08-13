package com.oasys.helpdesk.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

@Entity
@Table(name = "REVINFO")
@RevisionEntity(OasysRevisionEntityListener.class)
public class OasysRevisionEntity extends DefaultRevisionEntity {

  private String username;

  public String getUsername() {
    return this.username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

}
