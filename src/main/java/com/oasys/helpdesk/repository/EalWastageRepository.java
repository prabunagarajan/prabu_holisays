package com.oasys.helpdesk.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.EalWastage;
import com.oasys.posasset.entity.EALRequestEntity;

@Repository
public interface EalWastageRepository extends JpaRepository<EalWastage, Long> {

	@Query("SELECT e FROM EalWastage e where  e.createdDate BETWEEN :fromDate AND :toDate")
	Page<EalWastage> getByFromDateAndToDate(Pageable pageable, @Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate);

	@Query("SELECT e FROM EalWastage e ")
	Page<EalWastage> getAll(Pageable pageable);

	@Query(value = "SELECT package_size As packagesize,damaged_no_of_barcode AS wastagecases,damaged_no_of_bottle_qr_code AS wastageqrcode,planned_no_of_cases-damaged_no_of_barcode AS usedbarcode,planned_no_of_bottles-damaged_no_of_bottle_qr_code AS usedqrcode FROM eal_wastage er WHERE er.license_number=:license_number and date(er.created_date) between :fromDate and :toDate and er.code_type=:code_type and er.package_size=:package_size and er.created_by=:created_by", nativeQuery = true)
	public List<EALWastageUsage> getCountByLicenseNumberAndCreatedDateBetweenAndCodetypeAndPackageSizeAndCreatedBy(
			@Param("license_number") String license_number, @Param("fromDate") java.util.Date fromDate,
			@Param("toDate") java.util.Date toDate, @Param("code_type") String codetype,
			@Param("package_size") String package_size, @Param("created_by") Long createdBy);

//	@Query(value="SELECT package_size As packagesize,damaged_no_of_barcode AS wastagecases,damaged_no_of_bottle_qr_code AS wastageqrcode,planned_no_of_cases-damaged_no_of_barcode AS usedbarcode,planned_no_of_bottles-damaged_no_of_bottle_qr_code AS usedqrcode FROM eal_wastage er WHERE er.license_number=:license_number and date(er.created_date) between :fromDate and :toDate and er.code_type=:code_type and er.package_size=:package_size",nativeQuery=true)
//	public List<EALWastageUsage> getCountByLicenseNumberAndCreatedDateBetweenAndCodetypeAndPackageSize(@Param("license_number") String license_number,@Param("fromDate") java.util.Date fromDate,@Param("toDate") java.util.Date toDate,@Param("code_type") String codetype,@Param("package_size") String package_size);

	@Query(value = "select * FROM eal_wastage where status = 0", nativeQuery = true)
	List<EalWastage> inprogressList();

	@Query(value = "SELECT package_size As packagesize,damaged_no_of_barcode AS wastagecases,damaged_no_of_bottle_qr_code AS wastageqrcode,planned_no_of_cases-damaged_no_of_barcode AS usedbarcode,planned_no_of_bottles-damaged_no_of_bottle_qr_code AS usedqrcode FROM eal_wastage er WHERE er.license_number=:license_number and date(er.created_date) between :fromDate and :toDate and er.code_type=:code_type and er.package_size=:package_size", nativeQuery = true)
	public List<EALWastageUsage> getCountByLicenseNumberAndCreatedDateBetweenAndCodetypeAndPackageSize(
			@Param("license_number") String license_number, @Param("fromDate") java.util.Date fromDate,
			@Param("toDate") java.util.Date toDate, @Param("code_type") String codetype,
			@Param("package_size") String package_size);

	List<EalWastage> findByApplicationNo(String ApplicationNo);

	@Query(value ="SELECT * FROM eal_wastage  where  application_no=:applicationNumber", nativeQuery = true)
	Optional<EalWastage> getByApplicationNo(String applicationNumber);

	
	 @Query(value = "SELECT * FROM eal_wastage WHERE bottling_plan_id = :bottlingPlanId  order by id limit 1", nativeQuery = true)
	    List<EalWastage> findByBottlingPlanIdStatusAndRequestStatus(@Param("bottlingPlanId") String bottlingPlanId);

	 @Query(value = "SELECT * FROM eal_wastage WHERE bottling_plan_id = :bottlingPlanId and request_status=0", nativeQuery = true)
	List<EalWastage> findByBottlingPlanIdAndRequestStatus(String bottlingPlanId);

	 @Query(value = "SELECT * FROM eal_wastage WHERE bottling_plan_id = :requestedapplnNo order by id limit 1", nativeQuery = true) 
	Optional<EalWastage> getByAppliNo(String requestedapplnNo);




}
