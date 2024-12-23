package py.una.pol.auth.services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import py.una.pol.auth.repository.PermissionRepository;
import py.una.pol.auth.repository.RoleRepository;
import py.una.pol.auth.dto.PermissionDto;
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

    /*
     * Busca un rol por su ID.
     *
     * @param id El ID del rol a buscar.
     * @return RoleDTO que representa el rol encontrado.
     * @throws RuntimeException Si no se encuentra el rol.
     */
    public RoleDto findById(Long id) {
        Role rolenew = role.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
        return convertRoleDto(rolenew);
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

    /*
     * Actualiza un rol existente con la información del RoleDTO proporcionado.
     *
     * @param id El ID del rol a actualizar.
     * @param roleDto El RoleDTO que contiene la nueva información del rol.
     * @return RoleDTO que representa el rol actualizado.
     * @throws RuntimeException Si no se encuentra el rol.
     */
    public RoleDto updateRole(Long id, RoleDto roleDto) {
        Role roleNew = role.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
        roleNew.setName(roleDto.getName());
        roleNew = role.save(roleNew);
        return convertRoleDto(roleNew);
    }
    /**
     * Asigna una lista de permisos a un rol existente.
     *
     * @param roleId El ID del rol al que se asignarán los permisos.
     * @param permissionIds La lista de IDs de permisos a asignar.
     * @throws RuntimeException Si el rol no se encuentra o si algunos permisos no existen.
     */
    public void assignPermissionsToRole(Long roleId, List<Long> permissionIds) {
        // Verificar que el rol existe
        Role roleNew = role.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));
    
        // Verificar que todos los permisos existen
        List<Permission> permissions = permissionRepository.findAllById(permissionIds);
    
        // Si no se encuentran todos los permisos, lanzar una excepción
        if (permissions.size() != permissionIds.size()) {
            throw new RuntimeException("Some permissions not found");
        }
    
        // Asignar permisos al rol
        roleNew.setPermissions(new HashSet<>(permissions)); // Usamos un HashSet para evitar duplicados
        role.save(roleNew); // Guardamos el rol con los permisos asignados
    }

    /*
     * Elimina un rol por su ID.
     *
     * @param id El ID del rol a eliminar.
     */
    public boolean delete(Long id) {
        if (!role.existsById(id)) {
            return false; // Rol no encontrado
        }
        role.deleteById(id);
        return true; // Eliminación exitosa
    }

     /**
 * Obtiene la lista de permisos asociados a un rol utilizando una consulta personalizada.
 *
 * @param roleId El ID del rol del que se quieren obtener los permisos.
 * @return Lista de PermissionDTO que representa los permisos del rol.
 * @throws RuntimeException Si no se encuentra el rol o si hay un error en la consulta.
 */
public List<PermissionDto> getPermissionsByRoleId(Long roleId) {
    // Obtener permisos asociados al rol desde el repositorio
    List<Permission> permissions = role.findPermissionsByRoleId(roleId);

    // Convertir la lista de entidades Permission a PermissionDTO
    return permissions.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    private PermissionDto convertToDto(Permission permission) {
        PermissionDto permissionDto = new PermissionDto();
        permissionDto.setId(permission.getId());
        permissionDto.setName(permission.getName());
        permissionDto.setDescription(permission.getDescription());
        return permissionDto;
    }

    /**
     * Busca un rol por su nombre.
     *
     * @param name El nombre del rol a buscar.
     * @return Un Optional que contiene el RoleDTO si se encuentra el rol, o vacío si no.
     */
    public Optional<RoleDto> findRoleByName(String name) {
        return role.findByName(name).map(this::convertRoleDto);
    }

}
