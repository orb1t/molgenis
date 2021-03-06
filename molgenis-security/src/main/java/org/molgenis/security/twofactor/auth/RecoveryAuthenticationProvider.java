package org.molgenis.security.twofactor.auth;

import org.springframework.security.authentication.AuthenticationProvider;

/**
 * Marker interface for RecoveryAuthenticationProvider when you do not implement is this way you get
 * error messages like:
 *
 * <p>" but was actually of type 'com.sun.proxy.$Proxy76' "
 */
public interface RecoveryAuthenticationProvider extends AuthenticationProvider {}
