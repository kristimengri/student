package com.kent.gmail.com.runtime.request;

import java.time.OffsetDateTime;

/** Object Used to Create Base */
public class BaseCreate {

  private OffsetDateTime dateCreated;

  private OffsetDateTime dateUpdated;

  private String description;

  private String name;

  private Boolean softDelete;

  /**
   * @return dateCreated
   */
  public OffsetDateTime getDateCreated() {
    return this.dateCreated;
  }

  /**
   * @param dateCreated dateCreated to set
   * @return BaseCreate
   */
  public <T extends BaseCreate> T setDateCreated(OffsetDateTime dateCreated) {
    this.dateCreated = dateCreated;
    return (T) this;
  }

  /**
   * @return dateUpdated
   */
  public OffsetDateTime getDateUpdated() {
    return this.dateUpdated;
  }

  /**
   * @param dateUpdated dateUpdated to set
   * @return BaseCreate
   */
  public <T extends BaseCreate> T setDateUpdated(OffsetDateTime dateUpdated) {
    this.dateUpdated = dateUpdated;
    return (T) this;
  }

  /**
   * @return description
   */
  public String getDescription() {
    return this.description;
  }

  /**
   * @param description description to set
   * @return BaseCreate
   */
  public <T extends BaseCreate> T setDescription(String description) {
    this.description = description;
    return (T) this;
  }

  /**
   * @return name
   */
  public String getName() {
    return this.name;
  }

  /**
   * @param name name to set
   * @return BaseCreate
   */
  public <T extends BaseCreate> T setName(String name) {
    this.name = name;
    return (T) this;
  }

  /**
   * @return softDelete
   */
  public Boolean getSoftDelete() {
    return this.softDelete;
  }

  /**
   * @param softDelete softDelete to set
   * @return BaseCreate
   */
  public <T extends BaseCreate> T setSoftDelete(Boolean softDelete) {
    this.softDelete = softDelete;
    return (T) this;
  }
}
