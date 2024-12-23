package py.una.pol.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import py.una.pol.auth.dto.RoleDto;
import py.una.pol.auth.model.Permission;
import py.una.pol.auth.model.Role;
import py.una.pol.auth.repository.PermissionRepository;
import py.una.pol.auth.repository.RoleRepository;
import py.una.pol.auth.services.RoleService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PermissionRepository permissionRepository;

    @InjectMocks
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        Role role1 = new Role(1L, "ADMIN", null, null);
        Role role2 = new Role(2L, "USER", null, null);

        when(roleRepository.findAll()).thenReturn(Arrays.asList(role1, role2));

        List<RoleDto> result = roleService.findAll();

        assertEquals(2, result.size());
        assertEquals("ADMIN", result.get(0).getName());
        assertEquals("USER", result.get(1).getName());
    }

    @Test
    void testCreateRol() {
        RoleDto roleDto = new RoleDto();
        roleDto.setName("MODERATOR");

        Role role = new Role(null, "MODERATOR", null, null);
        Role savedRole = new Role(1L, "MODERATOR", null, null);

        when(roleRepository.save(any(Role.class))).thenReturn(savedRole);

        RoleDto result = roleService.createRol(roleDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("MODERATOR", result.getName());
    }

    @Test
    void testAssignPermissionstoRol() {
        Role role = new Role(1L, "ADMIN", null, null);
        Permission permission1 = new Permission(1L, "READ", "Read permissions", null);
        Permission permission2 = new Permission(2L, "WRITE", "Write permissions", null);

        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(permissionRepository.findAllById(Arrays.asList(1L, 2L)))
                .thenReturn(Arrays.asList(permission1, permission2));

        roleService.assignPermissionstoRol(1L, Arrays.asList(1L, 2L));

        verify(roleRepository, times(1)).save(role);
        assertEquals(2, role.getPermissions().size());
    }

    @Test
    void testAssignPermissionstoRolThrowsExceptionWhenRoleNotFound() {
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class,
                () -> roleService.assignPermissionstoRol(1L, Collections.singletonList(1L)));

        assertEquals("No se encontro el Rol", exception.getMessage());
    }

    @Test
    void testAssignPermissionstoRolThrowsExceptionWhenPermissionsMissing() {
        Role role = new Role(1L, "ADMIN", null, null);

        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(permissionRepository.findAllById(Arrays.asList(1L, 2L)))
                .thenReturn(Collections.singletonList(new Permission()));

        Exception exception = assertThrows(RuntimeException.class,
                () -> roleService.assignPermissionstoRol(1L, Arrays.asList(1L, 2L)));

        assertEquals("Ciertos permisos a agregar ya no existen en el sistema", exception.getMessage());
    }
}