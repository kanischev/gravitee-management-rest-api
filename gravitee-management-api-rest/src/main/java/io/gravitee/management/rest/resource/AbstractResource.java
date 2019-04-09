/**
 * Copyright (C) 2015 The Gravitee team (http://gravitee.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gravitee.management.rest.resource;

import io.gravitee.management.idp.api.authentication.UserDetails;
import io.gravitee.management.model.permissions.RolePermission;
import io.gravitee.management.model.permissions.RolePermissionAction;
import io.gravitee.management.model.permissions.RoleScope;
import io.gravitee.management.model.permissions.SystemRole;
import io.gravitee.management.service.ApiService;
import io.gravitee.management.service.MembershipService;
import io.gravitee.management.service.PermissionService;
import io.gravitee.management.service.RoleService;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

/**
 * @author David BRASSELY (david.brassely at graviteesource.com)
 * @author Nicolas GERAUD (nicolas.geraud at graviteesource.com)
 * @author Azize ELAMRANI (azize.elamrani at graviteesource.com)
 * @author GraviteeSource Team
 */
public abstract class AbstractResource {

    public final static String MANAGEMENT_ADMIN = RoleScope.MANAGEMENT.name() + ':' + SystemRole.ADMIN.name();
    public final static String PORTAL_ADMIN = RoleScope.PORTAL.name() + ':' + SystemRole.ADMIN.name();

    @Context
    protected SecurityContext securityContext;

    @Inject
    MembershipService membershipService;
    @Inject
    RoleService roleService;
    @Inject
    ApiService apiService;
    @Inject
    PermissionService permissionService;

    UserDetails getAuthenticatedUserDetails() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    protected String getAuthenticatedUser() {
        return securityContext.getUserPrincipal().getName();
    }

    String getAuthenticatedUserOrNull() {
        return isAuthenticated() ? getAuthenticatedUser() : null;
    }

    protected boolean isAuthenticated() {
        return securityContext.getUserPrincipal() != null;
    }

    protected boolean isAdmin() {
        return  isUserInRole(MANAGEMENT_ADMIN) ||
                isUserInRole(PORTAL_ADMIN);
    }

    private boolean isUserInRole(String role) {
        return securityContext.isUserInRole(role);
    }

    protected boolean hasPermission(RolePermission permission, RolePermissionAction... acls) {
        return hasPermission(permission, null, acls);
    }

    protected boolean hasPermission(RolePermission permission, String referenceId, RolePermissionAction... acls) {
        return isAuthenticated() && (isAdmin() || permissionService.hasPermission(permission, referenceId, acls));
    }

    void checkImageSize(final String picture) {
        if (picture != null) {
            final int imageBase64Length = picture.length();
            final int approximateImageSizeInByte = 3 * imageBase64Length / 4;
            if (approximateImageSizeInByte > 50_000) {
                throw new IllegalArgumentException("The image is too big");
            }
        }
    }
}
