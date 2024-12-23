package py.una.pol.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import py.una.pol.auth.dto.PermissionDto;
import py.una.pol.auth.model.Permission;
import py.una.pol.auth.repository.PermissionRepository;
import py.una.pol.auth.services.PermissionService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PermissionServiceTest {

    @Mock
    private PermissionRepository permissionRepository;

    @InjectMocks
    private PermissionService permissionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        Permission permission1 = new Permission(1L, "READ", "Read permissions", null);
        Permission permission2 = new Permission(2L, "WRITE", "Write permissions", null);

        when(permissionRepository.findAll()).thenReturn(Arrays.asList(permission1, permission2));

        List<PermissionDto> result = permissionService.findAll();

        assertEquals(2, result.size());
        assertEquals("READ", result.get(0).getName());
        assertEquals("WRITE", result.get(1).getName());
    }

    @Test
    void testCreate() {
        PermissionDto dto = new PermissionDto();
        dto.setName("EXECUTE");
        dto.setDescription("Execute permissions");

        Permission permission = new Permission(null, "EXECUTE", "Execute permissions", null);
        Permission savedPermission = new Permission(1L, "EXECUTE", "Execute permissions", null);

        when(permissionRepository.save(any(Permission.class))).thenReturn(savedPermission);

        PermissionDto result = permissionService.create(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("EXECUTE", result.getName());
    }
}