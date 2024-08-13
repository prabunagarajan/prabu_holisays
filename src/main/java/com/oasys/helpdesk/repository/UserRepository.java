package com.oasys.helpdesk.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.oasys.helpdesk.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

	@Query("select u from UserEntity u where u.id=:id ")
	UserEntity getById(@Param("id") Long id);

	// @Query("select u from UserEntity u where u.employeeId=:employeeId ")
	@Query(value = "select u.* from user u where u.employee_id=:employeeId ", nativeQuery = true)
	Optional<UserEntity> getByEmployeeIdIgnoreCase(@Param("employeeId") String employeeId);

	public Optional<UserEntity> findByEmailId(@Param("emailId") String emailId);

	public Optional<UserEntity> findByEmailIdAndIdNotIn(@Param("emailId") String emailId,
			@Param("idList") List<Long> idList);

	public Optional<UserEntity> findByPhoneNumber(@Param("phoneNumber") String phoneNumber);

	public Optional<UserEntity> findByPhoneNumberAndIdNotIn(@Param("phoneNumber") String phoneNumber,
			@Param("idList") List<Long> idList);

	public Optional<UserEntity> findByUsernameIgnoreCase(@Param("username") String username);

	public Optional<UserEntity> findByUsernameIgnoreCaseAndIdNotIn(@Param("username") String username,
			@Param("idList") List<Long> idList);

	@Query("SELECT new map(user.id as id, user.employeeId as employeeId, user.emailId as emailId,user.username as username,user.middleName as middleName, user.firstName as firstName,user.lastName as lastName)"
			+ "FROM UserEntity user inner join user.roles roles where roles.status=true and roles.id=:roleId and user.employmentStatus=0 order by user.middleName asc")
	public List<Map<String, String>> getUserByRole(@Param("roleId") Long roleId);

	Page<UserEntity> findByUsernameIgnoreCaseAndEmployeeIdIgnoreCaseAndIsSystemDefaultUser(String username,
			String employeeId, Boolean isSystemDefaultUser, Pageable pageable);

	Page<UserEntity> findByEmployeeIdIgnoreCaseAndIsSystemDefaultUser(String employeeId, Boolean isSystemDefaultUser,
			Pageable pageable);

	Page<UserEntity> findByUsernameIgnoreCaseAndIsSystemDefaultUser(String username, Boolean isSystemDefaultUser,
			Pageable pageable);

	Page<UserEntity> findByIsSystemDefaultUser(Boolean isSystemDefaultUser, Pageable pageable);

	/*
	 * @Query("SELECT new map(user.id as id, user.employeeId as employeeId, user.emailId as emailId,user.username as username,user.middleName as middleName, user.firstName as firstName,user.lastName as lastName)"
	 * +
	 * "FROM UserEntity user inner join user.roles roles inner join user.backupUser backupUser where roles.status=true and roles.id=:roleId and user.employmentStatus=0 and user.id not in (backupUser.backupUserId) order by user.modifiedDate desc"
	 * )
	 */

	@Query(value = "select u.id as id, u.employee_id as employeeId, u.email_id as emailId,u.username as username,u.middle_name as middleName, u.first_name as firstName,u.last_name as lastName from user u, user_role ur, role_master r where r.id=ur.role_id and  u.id=ur.user_id and r.id=:roleId and r.status=true ", nativeQuery = true)
	public List<Map<String, String>> getBackupUserListByRole(@Param("roleId") Long roleId);

	@Query(value = "select u.id as id, u.employee_id as employeeId, u.email_id as emailId,u.username as username,u.middle_name as middleName, u.first_name as firstName,u.last_name as lastName from user u, user_role ur, role_master r where r.id=ur.role_id and  u.id=ur.user_id and r.id=:roleId and r.status=true and u.id !=:userId", nativeQuery = true)
	public List<Map<String, String>> getBackupUserListByRoleAndUserId(@Param("roleId") Long roleId,
			@Param("userId") Long userId);

	@Query("select new map( u.id as id, u.employeeId as employeeId, u.emailId as emailId,u.username "
			+ "as username,u.middleName as middleName, u.firstName as firstName,u.lastName as lastName) from UserEntity u inner join u.roles r where r.defaultRole=true ")
	public List<Map<String, String>> getBackupUserListByRole();

	Optional<UserEntity> findByUsernameOrEmailIdIgnoreCase(@Param("username") String username,
			@Param("emailId") String emailId);

	@Transactional
	@Modifying
	@Query("Update UserEntity u set u.accountLocked = true where u.id=:userId")
	void lockedUser(@Param("userId") Long userId);

	public Optional<UserEntity> findByUsernameIgnoreCaseOrEmailId(@Param("username") String username,
			@Param("emailId") String emailId);

	@Query("SELECT user FROM UserEntity user inner join user.roles roles where roles.status=true and roles.id=:roleId and user.employmentStatus=0 order by user.modifiedDate desc")
	public List<UserEntity> getUserByRoleId(@Param("roleId") Long roleId);

	@Query("SELECT new map(user.id as id, user.employeeId as employeeId, user.emailId as emailId,user.username as username,user.middleName as middleName, user.firstName as firstName,user.lastName as lastName)"
			+ "FROM UserEntity user inner join user.roles roles inner join user.workLocation location where roles.status=true and roles.id=:roleId and user.employmentStatus=0 and location.districtCode=:districtCode or (roles.id=:newrolid) order by user.middleName asc")
	public List<Map<String, String>> getUserByRoleAndDistrict(@Param("roleId") Long roleId,
			@Param("districtCode") String districtCode, @Param("newrolid") Long newrolid);

	@Query("SELECT new map( user.id as id, user.employeeId as employeeId, user.emailId as emailId,user.username as username,user.middleName as middleName, user.firstName as firstName,user.lastName as lastName)"
			+ "FROM UserEntity user inner join user.associatedUsers associatedUsers where  associatedUsers.associatedUserId = :handlingOfficerId and user.employmentStatus=0 order by user.modifiedDate desc")
	public Set<Map<String, String>> getUserByHandlingOfficerId(@Param("handlingOfficerId") Long handlingOfficerId);

	@Query("SELECT new map(user.id as id, user.employeeId as employeeId, user.emailId as emailId,user.username as username,user.middleName as middleName, user.firstName as firstName,user.lastName as lastName)"
			+ "FROM UserEntity user where id in :handlingOfficerIds  and user.employmentStatus=0 order by user.modifiedDate desc")
	public Set<Map<String, String>> getUserByHandlingOfficerIds(
			@Param("handlingOfficerIds") List<Long> handlingOfficerIds);

	@Query("SELECT new map(user.id as id, user.employeeId as employeeId, user.emailId as emailId,user.username as username,user.middleName as middleName, user.firstName as firstName,user.lastName as lastName)"
			+ "FROM UserEntity user inner join user.roles roles inner join user.workLocation location where roles.status=true and roles.id=:roleId and user.employmentStatus=0 and location.districtCode in :districtCode or (roles.id=:newrolid) order by user.middleName asc")
	public List<Map<String, String>> getUserByRoleAndMultiDistrict(@Param("roleId") Long roleId,
			@Param("districtCode") List<String> districtCode, @Param("newrolid") Long newrolid);

	@Query("SELECT new map(user.id as id, user.employeeId as employeeId, user.emailId as emailId,user.username as username,user.middleName as middleName, user.firstName as firstName,user.lastName as lastName)"
			+ "FROM UserEntity user inner join user.roles roles inner join user.workLocation location where roles.status=true and roles.id=:roleId and user.employmentStatus=0  order by user.middleName asc")
	public List<Map<String, String>> getUserByRoleIDshopcode(@Param("roleId") Long roleId);

	public Optional<UserEntity> findByEmployeeId(@Param("employeeId") String empcode);

	@Query("SELECT a FROM UserEntity a where  a.username =:username")
	public Optional<UserEntity> findByUsername(@Param("username") String username);

	@Query("SELECT user FROM UserEntity user inner join user.roles roles where roles.status=true and roles.id=:roleId and user.employmentStatus=0 order by user.modifiedDate desc")
	public List<UserEntity> getUserByRoleIdsoftware(@Param("roleId") Long roleId);

	@Query("SELECT new map(user.id as id, user.employeeId as employeeId, user.emailId as emailId,user.username as username,user.middleName as middleName, user.firstName as firstName,user.lastName as lastName ,user.phoneNumber as phoneNumber)"
			+ "FROM UserEntity user inner join user.roles roles inner join user.workLocation location where roles.status=true and roles.id=:roleId and user.employmentStatus=0 and location.districtCode=:districtCode  order by user.middleName asc")
	public List<Map<String, String>> getUserByRoleAndDistri(@Param("roleId") Long roleId,
			@Param("districtCode") String districtCode);

	@Query("SELECT user FROM UserEntity user inner join user.roles roles where roles.status=true and roles.id=:roleId and user.employmentStatus=0 order by user.modifiedDate desc")
	public List<UserEntity> getUserByRoleIdCCTV(@Param("roleId") Long roleId);

	@Query("select CONCAT(first_name,last_name)  from UserEntity where id=:vendorId")
	String getFirstNameAndLastName(Long vendorId);

}
