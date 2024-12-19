package py.una.pol.auth.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import py.una.pol.auth.dto.PermissionDto;
import py.una.pol.auth.model.Permission;
import py.una.pol.auth.repository.PermissionRepository;

@Service
public class PermissionService {
    private final PermissionRepository permission;

    @Autowired
    public PermissionService(PermissionRepository permission){
        this.permission=permission;
    }

    public List<PermissionDto> findAll(){
        return permission.findAll().stream().map(this::convertPermissionDto).collect(Collectors.toList());
    }

    public PermissionDto create(PermissionDto permissionDto) {
        Permission newpermission = convertToEntity(permissionDto);
        newpermission = permission.save(newpermission);
        return convertPermissionDto(newpermission);
    }

    private PermissionDto convertPermissionDto(Permission permission){
        PermissionDto permissionDto = new PermissionDto();
        permissionDto.setId(permission.getId());
        permissionDto.setName(permission.getName());
        permissionDto.setDescription(permission.getDescription());
        return permissionDto;
    }

    private Permission convertToEntity(PermissionDto permissionDto) {
        Permission permission = new Permission();
        permission.setName(permissionDto.getName());
        permission.setDescription(permissionDto.getDescription());
        return permission;
    }
}
