package com.oasys.helpdesk.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.LoginHistory;


@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {

	public List<LoginHistory> findTop2ByUserIdOrderByLoginTimeDesc(Long userId);
	
	public LoginHistory findTop1ByUserIdOrderByLoginTimeDesc(Long userId);
	
	public Page<LoginHistory> findByUserIdOrderByLoginTimeDesc(Long userId, Pageable pageable);
	
	
	@Query(value="select (a.username) as username  ,(a.first_name) as firstname ,(l.login_time) as logintime ,(u.role_id) as roleid,(r.role_name) as rolename from user a inner join login_history l on a.id=l.user_id inner join user_role u on u.user_id =l.user_id inner join role_master r on r.id=u.role_id  where l.login_time in (select max(login_time)  from login_history group by user_id) and u.role_id =:role_id and date(l.login_time)=:login_time order by l.login_time  DESC ",nativeQuery=true)
	public Page<Fieldlogin> getCountByLoginTime(@Param("login_time") Date fromDate,@Param("role_id") Long roleId,Pageable pageable);

	@Query(value="select  (a.username) as username  ,(a.first_name) as firstname ,(l.login_time) as logintime ,(u.role_id) as roleid,(r.role_name) as rolename from user a inner join login_history l on a.id=l.user_id inner join user_role u on u.user_id =l.user_id inner join role_master r on r.id=u.role_id  where l.login_time in (select max(login_time)  from login_history group by user_id) and u.role_id =:role_id",nativeQuery=true)
	public Page<Fieldlogin> getCountByRoleid(@Param("role_id") Long roleId,Pageable pageable);
	
	@Query(value="select (a.username) as username  ,(a.first_name) as firstname ,(l.login_time) as logintime ,(u.role_id) as roleid,(r.role_name) as rolename from user a inner join login_history l on a.id=l.user_id inner join user_role u on u.user_id =l.user_id inner join role_master r on r.id=u.role_id  where l.login_time in (select max(login_time)  from login_history group by user_id) and u.role_id =:role_id and date(l.login_time)!=:login_time order by l.login_time  DESC ",nativeQuery=true)
	public Page<Fieldlogin> getCountBynotequalLoginTime(@Param("login_time") Date fromDate,@Param("role_id") Long roleId,Pageable pageable);


}
