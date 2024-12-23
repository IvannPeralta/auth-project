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

    


      /**
     * Busca un permiso por su ID.
     *
     * @param id El ID del permiso a buscar.
     * @return PermissionDTO que representa el permiso encontrado.
     * @throws RuntimeException Si no se encuentra el permiso.
     */
    public PermissionDto findById(Long id) {
        Permission permissionNew = permission.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found"));
        return convertPermissionDto(permissionNew);
    }

    /**
     * Actualiza un permiso existente con la información del PermissionDTO proporcionado.
     *
     * @param id El ID del permiso a actualizar.
     * @param permissionDto El PermissionDTO que contiene la nueva información del permiso.
     * @return PermissionDTO que representa el permiso actualizado.
     * @throws RuntimeException Si no se encuentra el permiso.
     */
    public PermissionDto update(Long id, PermissionDto permissionDto) {
        Permission permissionNew = permission.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found"));
        permissionNew.setName(permissionDto.getName());
        permissionNew.setDescription(permissionDto.getDescription());
        permissionNew = permission.save(permissionNew);
        return convertPermissionDto(permissionNew);
    }

    /**
     * Elimina un permiso por su ID.
     *
     * @param id El ID del permiso a eliminar.
     */
    public void delete(Long id) {
        permission.deleteById(id);
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
