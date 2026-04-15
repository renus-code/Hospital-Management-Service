package com.hospital.auth.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.hospital.auth.model.Permission;

public interface PermissionRepository extends MongoRepository<Permission, String> {

	List<Permission> findByPermissionTitleContainingIgnoreCase(String permissionTitle);

	List<Permission> findByPermissionModuleContainingIgnoreCase(String permissionModule);

	List<Permission> findByPermissionTitleContainingIgnoreCaseAndPermissionModuleContainingIgnoreCase(
			String permissionTitle, String permissionModule);
}
