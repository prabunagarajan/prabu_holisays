package com.oasys.helpdesk.repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.dto.ZabbixresponseDTO;
import com.oasys.helpdesk.entity.ZabbixServerEntity;
import com.oasys.posasset.dto.UpexServerDetailsDTO;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface ZabbixServerRepository extends JpaRepository<ZabbixServerEntity, Long> {

@Query(value ="SELECT  CONCAT(FLOOR(t.uptime / (24 * 3600)), ' days ',FLOOR((t.uptime % (24 * 3600)) / 3600), ' hours ',FLOOR((t.uptime % 3600) / 60), ' minutes ',t.uptime % 60, ' seconds') AS UptimeHMS,(t.service_id) As serviceId,(t.status) As status ,(t.server_name) As serverName,(t.sli) As sli,(t.created_date) As createdDate,(t.error_budget) As errorBudget,(t.uptime) As upTime,(t.down_time) As downTime,(t.excluded_downtimes) As excludedDowntimes,(t.slo) As slo,(t.todaydate) As todayDate FROM zabbix_server_data t WHERE t.service_id=:service_id AND t.server_name=:server_name and t.id in(select max(id) as id from zabbix_server_data group by service_id)", nativeQuery = true)
List<ZabbixresponseDTO>  getByClientProdServer(@Param("service_id") String service_id,@Param("server_name") String server_name,Pageable pageable);

@Query(value ="SELECT  CONCAT(FLOOR(t.uptime / (24 * 3600)), ' days ',FLOOR((t.uptime % (24 * 3600)) / 3600), ' hours ',FLOOR((t.uptime % 3600) / 60), ' minutes ',t.uptime % 60, ' seconds') AS UptimeHMS,(t.service_id) As serviceId,(t.status) As status ,(t.server_name) As serverName,(t.sli) As sli,(t.created_date) As createdDate,(t.error_budget) As errorBudget,(t.uptime) As upTime,(t.down_time) As downTime,(t.excluded_downtimes) As excludedDowntimes,(t.slo) As slo,(t.todaydate) As todayDate FROM zabbix_server_data t WHERE t.service_id=:service_id AND t.server_name=:server_name and t.id in(select max(id) as id from zabbix_server_data group by service_id)", nativeQuery = true)
List<ZabbixresponseDTO>  getByPrimaryServer(@Param("service_id") String service_id,@Param("server_name") String server_name,Pageable pageable);

@Query(value ="SELECT  CONCAT(FLOOR(t.uptime / (24 * 3600)), ' days ',FLOOR((t.uptime % (24 * 3600)) / 3600), ' hours ',FLOOR((t.uptime % 3600) / 60), ' minutes ',t.uptime % 60, ' seconds') AS UptimeHMS,(t.service_id) As serviceId,(t.status) As status ,(t.server_name) As serverName,(t.sli) As sli,(t.created_date) As createdDate,(t.error_budget) As errorBudget,(t.uptime) As upTime,(t.down_time) As downTime,(t.excluded_downtimes) As excludedDowntimes,(t.slo) As slo,(t.todaydate) As todayDate FROM zabbix_server_data t WHERE t.service_id=:service_id AND t.server_name=:server_name and t.id in(select max(id) as id from zabbix_server_data group by service_id)", nativeQuery = true)
List<ZabbixresponseDTO>  getByHaproxyServer(@Param("service_id") String service_id,@Param("server_name") String server_name,Pageable pageable);

@Query(value ="SELECT  CONCAT(FLOOR(t.uptime / (24 * 3600)), ' days ',FLOOR((t.uptime % (24 * 3600)) / 3600), ' hours ',FLOOR((t.uptime % 3600) / 60), ' minutes ',t.uptime % 60, ' seconds') AS UptimeHMS,(t.service_id) As serviceId,(t.status) As status ,(t.server_name) As serverName,(t.sli) As sli,(t.created_date) As createdDate,(t.error_budget) As errorBudget,(t.uptime) As upTime,(t.down_time) As downTime,(t.excluded_downtimes) As excludedDowntimes,(t.slo) As slo,(t.todaydate) As todayDate FROM zabbix_server_data t WHERE t.service_id=:service_id AND t.server_name=:server_name and t.id in(select max(id) as id from zabbix_server_data group by service_id)", nativeQuery = true)
List<ZabbixresponseDTO>  getByMasterserverData(@Param("service_id") String service_id,@Param("server_name") String server_name,Pageable pageable);

@Query(value ="SELECT  CONCAT(FLOOR(t.uptime / (24 * 3600)), ' days ',FLOOR((t.uptime % (24 * 3600)) / 3600), ' hours ',FLOOR((t.uptime % 3600) / 60), ' minutes ',t.uptime % 60, ' seconds') AS UptimeHMS,(t.service_id) As serviceId,(t.status) As status ,(t.server_name) As serverName,(t.sli) As sli,(t.created_date) As createdDate,(t.error_budget) As errorBudget,(t.uptime) As upTime,(t.down_time) As downTime,(t.excluded_downtimes) As excludedDowntimes,(t.slo) As slo,(t.todaydate) As todayDate FROM zabbix_server_data t WHERE t.service_id=:service_id AND t.server_name=:server_name and t.id in(select max(id) as id from zabbix_server_data group by service_id)", nativeQuery = true)
List<ZabbixresponseDTO>  getBySlaveDatabaseData(@Param("service_id") String service_id,@Param("server_name") String server_name,Pageable pageable);

@Query(value ="SELECT  CONCAT(FLOOR(t.uptime / (24 * 3600)), ' days ',FLOOR((t.uptime % (24 * 3600)) / 3600), ' hours ',FLOOR((t.uptime % 3600) / 60), ' minutes ',t.uptime % 60, ' seconds') AS UptimeHMS,(t.service_id) As serviceId,(t.status) As status ,(t.server_name) As serverName,(t.sli) As sli,(t.created_date) As createdDate,(t.error_budget) As errorBudget,(t.uptime) As upTime,(t.down_time) As downTime,(t.excluded_downtimes) As excludedDowntimes,(t.slo) As slo,(t.todaydate) As todayDate FROM zabbix_server_data t WHERE t.service_id=:service_id AND t.server_name=:server_name and t.id in(select max(id) as id from zabbix_server_data group by service_id)", nativeQuery = true)
List<ZabbixresponseDTO>  getBysecondaryData(@Param("service_id") String service_id,@Param("server_name") String server_name,Pageable pageable);

@Query(value ="SELECT  CONCAT(FLOOR(t.uptime / (24 * 3600)), ' days ',FLOOR((t.uptime % (24 * 3600)) / 3600), ' hours ',FLOOR((t.uptime % 3600) / 60), ' minutes ',t.uptime % 60, ' seconds') AS UptimeHMS,(t.service_id) As serviceId,(t.status) As status ,(t.server_name) As serverName,(t.sli) As sli,(t.created_date) As createdDate,(t.error_budget) As errorBudget,(t.uptime) As upTime,(t.down_time) As downTime,(t.excluded_downtimes) As excludedDowntimes,(t.slo) As slo,(t.todaydate) As todayDate FROM zabbix_server_data t WHERE t.service_id=:service_id AND t.server_name=:server_name and t.id in(select max(id) as id from zabbix_server_data group by service_id)", nativeQuery = true)
List<ZabbixresponseDTO>  getByuiserverData(@Param("service_id") String service_id,@Param("server_name") String server_name,Pageable pageable);

@Query(value ="SELECT  CONCAT(FLOOR(t.uptime / (24 * 3600)), ' days ',FLOOR((t.uptime % (24 * 3600)) / 3600), ' hours ',FLOOR((t.uptime % 3600) / 60), ' minutes ',t.uptime % 60, ' seconds') AS UptimeHMS,(t.service_id) As serviceId,(t.status) As status ,(t.server_name) As serverName,(t.sli) As sli,(t.created_date) As createdDate,(t.error_budget) As errorBudget,(t.uptime) As upTime,(t.down_time) As downTime,(t.excluded_downtimes) As excludedDowntimes,(t.slo) As slo,(t.todaydate) As todayDate FROM zabbix_server_data t WHERE t.service_id=:service_id AND t.server_name=:server_name and t.id in(select max(id) as id from zabbix_server_data group by service_id)", nativeQuery = true)
List<ZabbixresponseDTO>  getByArchiveData(@Param("service_id") String service_id,@Param("server_name") String server_name,Pageable pageable);

@Query(value ="SELECT  CONCAT(FLOOR(t.uptime / (24 * 3600)), ' days ',FLOOR((t.uptime % (24 * 3600)) / 3600), ' hours ',FLOOR((t.uptime % 3600) / 60), ' minutes ',t.uptime % 60, ' seconds') AS UptimeHMS,(t.service_id) As serviceId,(t.status) As status ,(t.server_name) As serverName,(t.sli) As sli,(t.created_date) As createdDate,(t.error_budget) As errorBudget,(t.uptime) As upTime,(t.down_time) As downTime,(t.excluded_downtimes) As excludedDowntimes,(t.slo) As slo,(t.todaydate) As todayDate FROM zabbix_server_data t WHERE t.service_id=:service_id AND t.server_name=:server_name and t.id in(select max(id) as id from zabbix_server_data group by service_id)", nativeQuery = true)
List<ZabbixresponseDTO>  getByCmsData(@Param("service_id") String service_id,@Param("server_name") String server_name,Pageable pageable);

@Query(value ="SELECT  CONCAT(FLOOR(t.uptime / (24 * 3600)), ' days ',FLOOR((t.uptime % (24 * 3600)) / 3600), ' hours ',FLOOR((t.uptime % 3600) / 60), ' minutes ',t.uptime % 60, ' seconds') AS UptimeHMS,(t.service_id) As serviceId,(t.status) As status ,(t.server_name) As serverName,(t.sli) As sli,(t.created_date) As createdDate,(t.error_budget) As errorBudget,(t.uptime) As upTime,(t.down_time) As downTime,(t.excluded_downtimes) As excludedDowntimes,(t.slo) As slo,(t.todaydate) As todayDate FROM zabbix_server_data t WHERE t.service_id=:service_id AND t.server_name=:server_name and t.id in(select max(id) as id from zabbix_server_data group by service_id)", nativeQuery = true)
List<ZabbixresponseDTO>  getByJenkins(@Param("service_id") String service_id,@Param("server_name") String server_name,Pageable pageable);

@Query(value ="SELECT  CONCAT(FLOOR(t.uptime / (24 * 3600)), ' days ',FLOOR((t.uptime % (24 * 3600)) / 3600), ' hours ',FLOOR((t.uptime % 3600) / 60), ' minutes ',t.uptime % 60, ' seconds') AS UptimeHMS,(t.service_id) As serviceId,(t.status) As status ,(t.server_name) As serverName,(t.sli) As sli,(t.created_date) As createdDate,(t.error_budget) As errorBudget,(t.uptime) As upTime,(t.down_time) As downTime,(t.excluded_downtimes) As excludedDowntimes,(t.slo) As slo,(t.todaydate) As todayDate FROM zabbix_server_data t WHERE t.service_id=:service_id AND t.server_name=:server_name and t.id in(select max(id) as id from zabbix_server_data group by service_id)", nativeQuery = true)
List<ZabbixresponseDTO>  getBymdm(@Param("service_id") String service_id,@Param("server_name") String server_name,Pageable pageable);

@Query(value ="SELECT  CONCAT(FLOOR(t.uptime / (24 * 3600)), ' days ',FLOOR((t.uptime % (24 * 3600)) / 3600), ' hours ',FLOOR((t.uptime % 3600) / 60), ' minutes ',t.uptime % 60, ' seconds') AS UptimeHMS,(t.service_id) As serviceId,(t.status) As status ,(t.server_name) As serverName,(t.sli) As sli,(t.created_date) As createdDate,(t.error_budget) As errorBudget,(t.uptime) As upTime,(t.down_time) As downTime,(t.excluded_downtimes) As excludedDowntimes,(t.slo) As slo,(t.todaydate) As todayDate FROM zabbix_server_data t WHERE t.service_id=:service_id AND t.server_name=:server_name and t.id in(select max(id) as id from zabbix_server_data group by service_id)", nativeQuery = true)
List<ZabbixresponseDTO>  getByQr(@Param("service_id") String service_id,@Param("server_name") String server_name,Pageable pageable);

@Query(value ="SELECT  CONCAT(FLOOR(t.uptime / (24 * 3600)), ' days ',FLOOR((t.uptime % (24 * 3600)) / 3600), ' hours ',FLOOR((t.uptime % 3600) / 60), ' minutes ',t.uptime % 60, ' seconds') AS UptimeHMS,(t.service_id) As serviceId,(t.status) As status ,(t.server_name) As serverName,(t.sli) As sli,(t.created_date) As createdDate,(t.error_budget) As errorBudget,(t.uptime) As upTime,(t.down_time) As downTime,(t.excluded_downtimes) As excludedDowntimes,(t.slo) As slo,(t.todaydate) As todayDate FROM zabbix_server_data t WHERE t.service_id=:service_id AND t.server_name=:server_name and t.id in(select max(id) as id from zabbix_server_data group by service_id)", nativeQuery = true)
List<ZabbixresponseDTO>  getBywso2(@Param("service_id") String service_id,@Param("server_name") String server_name,Pageable pageable);

@Query(value ="SELECT  CONCAT(FLOOR(t.uptime / (24 * 3600)), ' days ',FLOOR((t.uptime % (24 * 3600)) / 3600), ' hours ',FLOOR((t.uptime % 3600) / 60), ' minutes ',t.uptime % 60, ' seconds') AS UptimeHMS,(t.service_id) As serviceId,(t.status) As status ,(t.server_name) As serverName,(t.sli) As sli,(t.created_date) As createdDate,(t.error_budget) As errorBudget,(t.uptime) As upTime,(t.down_time) As downTime,(t.excluded_downtimes) As excludedDowntimes,(t.slo) As slo,(t.todaydate) As todayDate FROM zabbix_server_data t WHERE t.service_id=:service_id AND t.server_name=:server_name and t.id in(select max(id) as id from zabbix_server_data group by service_id)", nativeQuery = true)
List<ZabbixresponseDTO>  getBymis(@Param("service_id") String service_id,@Param("server_name") String server_name,Pageable pageable);

@Query(value ="SELECT \r\n" + 
		"    CONCAT(\r\n" + 
		"        FLOOR(t.uptime / (24 * 3600)), ' days ',\r\n" + 
		"        FLOOR((t.uptime % (24 * 3600)) / 3600), ' hours ',\r\n" + 
		"        FLOOR((t.uptime % 3600) / 60), ' minutes ',\r\n" + 
		"        t.uptime % 60, ' seconds'\r\n" + 
		"    ) AS UptimeHMS,\r\n" + 
		"    t.service_id AS serviceId,\r\n" + 
		"    t.status AS status,\r\n" + 
		"    t.server_name AS serverName,\r\n" + 
		"    t.sli AS sli,\r\n" + 
		"    t.created_date AS createdDate,\r\n" + 
		"    t.error_budget AS errorBudget,\r\n" + 
		"    t.uptime AS upTime,\r\n" + 
		"    t.down_time AS downTime,\r\n" + 
		"    t.excluded_downtimes AS excludedDowntimes,\r\n" + 
		"    t.slo AS slo,\r\n" + 
		"    t.todaydate AS todayDate\r\n" + 
		"FROM \r\n" + 
		"    zabbix_server_data t \r\n" + 
		"WHERE \r\n" + 
		"    t.service_id IN (:service_id) \r\n" + 
		"    AND t.id IN (\r\n" + 
		"        SELECT MAX(id) AS id \r\n" + 
		"        FROM zabbix_server_data \r\n" + 
		"        GROUP BY service_id\r\n" + 
		"    );", nativeQuery = true)
List<ZabbixresponseDTO>  getByAllServer(@Param("service_id") List<String> service_id);


//@Query(value ="SELECT t.server_name AS ServerName,\r\n" + 
//		"    CONCAT( FLOOR(TIME_FORMAT(SEC_TO_TIME(t.total), '%H') / 24), 'days ',MOD(TIME_FORMAT(SEC_TO_TIME(t.total), '%H'), 24), 'h:',TIME_FORMAT(SEC_TO_TIME(t.total), '%im:%ss')) AS 'TotalOperatingHrs',\r\n" + 
//		"    CONCAT(FLOOR(TIME_FORMAT(SEC_TO_TIME(t.up), '%H') / 24), 'days ',MOD(TIME_FORMAT(SEC_TO_TIME(t.up), '%H'), 24), 'h:',TIME_FORMAT(SEC_TO_TIME(t.up), '%im:%ss')) AS 'TotalUptimeHrs',\r\n" + 
//		"    CONCAT(FLOOR(TIME_FORMAT(SEC_TO_TIME(t.down), '%H') / 24), 'days ',MOD(TIME_FORMAT(SEC_TO_TIME(t.down), '%H'), 24), 'h:',TIME_FORMAT(SEC_TO_TIME(t.down), '%im:%ss')) AS 'TotalDowntimeHrs',\r\n" + 
//		"    t.error_budget AS ErrorBudget,t.slo AS Slo\r\n" + 
//		" 	FROM (SELECT server_name,(SUM(uptime) + SUM(down_time)) AS total,SUM(uptime) AS up,SUM(down_time) AS down,error_budget,slo FROM zabbix_server_data\r\n" + 
//		"    WHERE (server_name,DATE(created_date),created_date) IN (SELECT server_name,DATE(created_date),MAX(created_date)FROM zabbix_server_data\r\n" + 
//		"    WHERE (DATE(created_date) BETWEEN :fromDate AND :toDate) OR (:fromDate IS NULL AND :toDate IS NULL)\r\n" + 
//		"    GROUP BY server_name, DATE(created_date))GROUP BY server_name) t;", nativeQuery = true)
//List<UpexServerDetailsDTO> upexServerDetails(String fromDate, String toDate);

@Query(value ="SELECT t.server_name AS ServerName,\r\n" + 
		"TIME_FORMAT(SEC_TO_TIME(t.total), '%H:%i') AS 'TotalOperatingHrs',\r\n" + 
		"TIME_FORMAT(SEC_TO_TIME(t.up), '%H:%i') AS 'TotalUptimeHrs',\r\n" + 
		"TIME_FORMAT(SEC_TO_TIME(t.down), '%H:%i') AS 'TotalDowntimeHrs',\r\n" + 
		"t.error_budget AS ErrorBudget,t.slo AS Slo\r\n" + 
		"FROM\r\n" + 
		"(SELECT server_name,(SUM(uptime)+SUM(down_time)) AS total,SUM(uptime) AS up,SUM(down_time) AS down,error_budget,slo\r\n" + 
		"FROM zabbix_server_data WHERE (server_name,DATE(created_date),created_date)IN(\r\n" + 
		"SELECT server_name,DATE(created_date),MAX(created_date) FROM zabbix_server_data\r\n" + 
		"WHERE (DATE(created_date) BETWEEN :fromDate AND :toDate) OR (:fromDate IS NULL AND :toDate IS NULL)\r\n" + 
		"GROUP BY server_name,DATE(created_date))\r\n" + 
		"GROUP BY server_name)t;", nativeQuery = true)
List<UpexServerDetailsDTO> upexServerDetails(@Param("fromDate") Date fromDate,
		@Param("toDate") Date toDate);


}
