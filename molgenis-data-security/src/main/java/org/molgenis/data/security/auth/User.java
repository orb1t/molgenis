package org.molgenis.data.security.auth;

import static org.molgenis.data.security.auth.UserMetaData.ACTIVATIONCODE;
import static org.molgenis.data.security.auth.UserMetaData.ACTIVE;
import static org.molgenis.data.security.auth.UserMetaData.ADDRESS;
import static org.molgenis.data.security.auth.UserMetaData.AFFILIATION;
import static org.molgenis.data.security.auth.UserMetaData.CHANGE_PASSWORD;
import static org.molgenis.data.security.auth.UserMetaData.CITY;
import static org.molgenis.data.security.auth.UserMetaData.COUNTRY;
import static org.molgenis.data.security.auth.UserMetaData.DEPARTMENT;
import static org.molgenis.data.security.auth.UserMetaData.EMAIL;
import static org.molgenis.data.security.auth.UserMetaData.FAX;
import static org.molgenis.data.security.auth.UserMetaData.FIRSTNAME;
import static org.molgenis.data.security.auth.UserMetaData.GOOGLEACCOUNTID;
import static org.molgenis.data.security.auth.UserMetaData.ID;
import static org.molgenis.data.security.auth.UserMetaData.LANGUAGECODE;
import static org.molgenis.data.security.auth.UserMetaData.LASTNAME;
import static org.molgenis.data.security.auth.UserMetaData.MIDDLENAMES;
import static org.molgenis.data.security.auth.UserMetaData.PASSWORD;
import static org.molgenis.data.security.auth.UserMetaData.PHONE;
import static org.molgenis.data.security.auth.UserMetaData.ROLE;
import static org.molgenis.data.security.auth.UserMetaData.SUPERUSER;
import static org.molgenis.data.security.auth.UserMetaData.TITLE;
import static org.molgenis.data.security.auth.UserMetaData.TOLLFREEPHONE;
import static org.molgenis.data.security.auth.UserMetaData.TWO_FACTOR_AUTHENTICATION;
import static org.molgenis.data.security.auth.UserMetaData.USERNAME;

import javax.annotation.Nullable;
import org.molgenis.data.Entity;
import org.molgenis.data.meta.model.EntityType;
import org.molgenis.data.support.StaticEntity;

public class User extends StaticEntity {
  public User(Entity entity) {
    super(entity);
  }

  public User(EntityType entityType) {
    super(entityType);
  }

  public User(String id, EntityType entityType) {
    super(entityType);
    setId(id);
  }

  public String getId() {
    return getString(ID);
  }

  public void setId(String id) {
    set(ID, id);
  }

  public String getUsername() {
    return getString(USERNAME);
  }

  public void setUsername(String username) {
    set(USERNAME, username);
  }

  public String getPassword() {
    return getString(PASSWORD);
  }

  public void setPassword(String password) {
    set(PASSWORD, password);
  }

  @Nullable
  public String getActivationCode() {
    return getString(ACTIVATIONCODE);
  }

  public void setActivationCode(String activationCode) {
    set(ACTIVATIONCODE, activationCode);
  }

  public boolean isTwoFactorAuthentication() {
    Boolean twoFactorAuthentication = getBoolean(TWO_FACTOR_AUTHENTICATION);
    if (twoFactorAuthentication == null) {
      twoFactorAuthentication = false;
    }
    return twoFactorAuthentication;
  }

  public void setTwoFactorAuthentication(boolean twoFactorAuthentication) {
    set(TWO_FACTOR_AUTHENTICATION, twoFactorAuthentication);
  }

  public Boolean isActive() {
    return getBoolean(ACTIVE);
  }

  public void setActive(Boolean active) {
    set(ACTIVE, active);
  }

  public Boolean isSuperuser() {
    return getBoolean(SUPERUSER);
  }

  public void setSuperuser(Boolean superuser) {
    set(SUPERUSER, superuser);
  }

  @Nullable
  public String getFirstName() {
    return getString(FIRSTNAME);
  }

  public void setFirstName(String firstName) {
    set(FIRSTNAME, firstName);
  }

  @Nullable
  public String getMiddleNames() {
    return getString(MIDDLENAMES);
  }

  public void setMiddleNames(String middleNames) {
    set(MIDDLENAMES, middleNames);
  }

  @Nullable
  public String getLastName() {
    return getString(LASTNAME);
  }

  public void setLastName(String lastName) {
    set(LASTNAME, lastName);
  }

  @Nullable
  public String getTitle() {
    return getString(TITLE);
  }

  public void setTitle(String title) {
    set(TITLE, title);
  }

  @Nullable
  public String getAffiliation() {
    return getString(AFFILIATION);
  }

  public void setAffiliation(String affiliation) {
    set(AFFILIATION, affiliation);
  }

  @Nullable
  public String getDepartment() {
    return getString(DEPARTMENT);
  }

  public void setDepartment(String department) {
    set(DEPARTMENT, department);
  }

  @Nullable
  public String getRole() {
    return getString(ROLE);
  }

  public void setRole(String role) {
    set(ROLE, role);
  }

  @Nullable
  public String getAddress() {
    return getString(ADDRESS);
  }

  public void setAddress(String address) {
    set(ADDRESS, address);
  }

  @Nullable
  public String getPhone() {
    return getString(PHONE);
  }

  public void setPhone(String phone) {
    set(PHONE, phone);
  }

  public String getEmail() {
    return getString(EMAIL);
  }

  public void setEmail(String email) {
    set(EMAIL, email);
  }

  @Nullable
  public String getFax() {
    return getString(FAX);
  }

  public void setFax(String fax) {
    set(FAX, fax);
  }

  @Nullable
  public String getTollFreePhone() {
    return getString(TOLLFREEPHONE);
  }

  public void setTollFreePhone(String tollFreePhone) {
    set(TOLLFREEPHONE, tollFreePhone);
  }

  @Nullable
  public String getCity() {
    return getString(CITY);
  }

  public void setCity(String city) {
    set(CITY, city);
  }

  @Nullable
  public String getCountry() {
    return getString(COUNTRY);
  }

  public void setCountry(String country) {
    set(COUNTRY, country);
  }

  public Boolean isChangePassword() {
    return getBoolean(CHANGE_PASSWORD);
  }

  public void setChangePassword(Boolean changePassword) {
    set(CHANGE_PASSWORD, changePassword);
  }

  @Nullable
  public String getLanguageCode() {
    return getString(LANGUAGECODE);
  }

  public void setLanguageCode(String languageCode) {
    set(LANGUAGECODE, languageCode);
  }

  @Nullable
  public String getGoogleAccountId() {
    return getString(GOOGLEACCOUNTID);
  }

  public void setGoogleAccountId(String googleAccountId) {
    set(GOOGLEACCOUNTID, googleAccountId);
  }
}
