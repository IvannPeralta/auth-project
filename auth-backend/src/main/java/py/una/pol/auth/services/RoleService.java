package py.una.pol.auth.services;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import py.una.pol.auth.repository.PermissionRepository;
import py.una.pol.auth.repository.RoleRepository;
import py.una.pol.auth.dto.RoleDto;
import py.una.pol.auth.model.Permission;
import py.una.pol.auth.model.Role;
import java.util.stream.Collectors;
@Service
public class RoleService {
    private RoleRepository role;
    private final PermissionRepository permissionRepository;

    @Autowired
    public RoleService(RoleRepository role,PermissionRepository permissionRepository){
        this.role=role;
        this.permissionRepository=permissionRepository;
    }

    public List<RoleDto> findAll(){
        return role.findAll().stream().map(this::convertRoleDto).collect(Collectors.toList());
    }

    private RoleDto convertRoleDto(Role role){
        RoleDto roleDto = new RoleDto();
        roleDto.setId(role.getId());
        roleDto.setName(role.getName());

        return roleDto;
    }

    public RoleDto createRol(RoleDto rolData){
        Role rol = new Role();
        rol.setName(rolData.getName());
        /* guardar el rol creado en la base de datos */
        rol=role.save(rol);
        return convertRoleDto(rol);
    }

    /* Metodo que asigna permiso o permisos a un rol del sistema */
    public void assignPermissionstoRol(Long roleId,List<Long> permissionsId){
        Role rolModificar=role.findById(roleId).orElseThrow(() ->
            new RuntimeException("No se encontro el Rol"));

        //verificar si todos los permisos existen
        List<Permission> permissions=permissionRepository.findAllById(permissionsId);

        if(permissionsId.size()!=permissions.size()){
            throw new RuntimeException("Ciertos permisos a agregar ya no existen en el sistema");
        }

        rolModificar.setPermissions(new HashSet<>(permissions));

        role.save(rolModificar);

    }
}
