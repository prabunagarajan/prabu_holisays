package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.DESC;
import static com.oasys.helpdesk.constant.Constant.EMAIL_ID;
import static com.oasys.helpdesk.constant.Constant.ID;
import static com.oasys.helpdesk.constant.Constant.MODIFIED_DATE;
import static com.oasys.helpdesk.constant.Constant.ROLE_ID;
import static com.oasys.helpdesk.constant.Constant.USERNAME;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.oasys.helpdesk.async.AsyncCommunicationExecution;
import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.AssociatedUserRequestDTO;
import com.oasys.helpdesk.dto.AssociatedUserResponseDTO;
import com.oasys.helpdesk.dto.AssociateresponseDTO;
import com.oasys.helpdesk.dto.ChangePasswordDto;
import com.oasys.helpdesk.dto.ForgotPasswordDTO;
import com.oasys.helpdesk.dto.LoginHistoryResponseDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.PasswordResetDTO;
import com.oasys.helpdesk.dto.ResetPasswordDtodet;
import com.oasys.helpdesk.dto.UpdateUserRequestDTO;
import com.oasys.helpdesk.dto.UserFieldDTO;
import com.oasys.helpdesk.dto.UserMultipleDistrictDTO;
import com.oasys.helpdesk.dto.UserRequestConDTO;
import com.oasys.helpdesk.dto.UserRequestDTO;
import com.oasys.helpdesk.dto.UserResponseDTO;
import com.oasys.helpdesk.dto.WorkLocationResponseDTO;
import com.oasys.helpdesk.entity.AssociatedUserEntity;
import com.oasys.helpdesk.entity.BackupUserEntity;
import com.oasys.helpdesk.entity.EmailVerification;
import com.oasys.helpdesk.entity.HelpdeskUserAuditEntity;
import com.oasys.helpdesk.entity.LoginHistory;
import com.oasys.helpdesk.entity.OldPasswordEntity;
import com.oasys.helpdesk.entity.PasswordReset;
import com.oasys.helpdesk.entity.RoleMaster;
import com.oasys.helpdesk.entity.ShopcodeEntity;
import com.oasys.helpdesk.entity.UserAttempts;
import com.oasys.helpdesk.entity.UserEntity;
import com.oasys.helpdesk.entity.UserShiftConfigurationEntity;
import com.oasys.helpdesk.entity.WorkLocationEntity;
import com.oasys.helpdesk.mapper.BackupUserMapper;
import com.oasys.helpdesk.mapper.LoginHistoryMapper;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.mapper.UserMapper;
import com.oasys.helpdesk.mapper.UserShiftConfigurationMapper;
import com.oasys.helpdesk.mapper.WorkLocationMapper;
import com.oasys.helpdesk.repository.AssociatedUserRepository;
import com.oasys.helpdesk.repository.BackupUserRepository;
import com.oasys.helpdesk.repository.EmailVerificationRepository;
import com.oasys.helpdesk.repository.Fieldlogin;
import com.oasys.helpdesk.repository.HelpdeskUserAuditRepository;
import com.oasys.helpdesk.repository.LoginHistoryRepository;
import com.oasys.helpdesk.repository.OldPasswordRepository;
import com.oasys.helpdesk.repository.PasswordResetRepository;
import com.oasys.helpdesk.repository.RoleMasterRepository;
import com.oasys.helpdesk.repository.UserAttemptsRepository;
import com.oasys.helpdesk.repository.UserRepository;
import com.oasys.helpdesk.repository.UserRoleRepository;
import com.oasys.helpdesk.repository.UserShiftConfigurationRepository;
import com.oasys.helpdesk.repository.WorkLocationRepository;
import com.oasys.helpdesk.request.OTPVerificationDTO;
import com.oasys.helpdesk.security.ApiResponse;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.security.SecurityUtils;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.EmploymentStatus;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.HelpdeskUserAuditAction;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.MenuPrefix;
import com.oasys.helpdesk.utility.RandomUtil;
import com.oasys.helpdesk.utility.RedisUtil;
import com.oasys.helpdesk.validation.UserRequestValidator;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class UserServiceImpl implements UserService {

	public static final String EMPLOYEE_ID = "Employee ID";

	@Autowired
	private UserRequestValidator validator;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private RoleMasterRepository roleMasterRepository;

	@Autowired
	private WorkLocationRepository workLocationRepository;

	@Autowired
	private UserShiftConfigurationRepository userShiftConfigurationRepository;

	@Autowired
	private BackupUserRepository backupUserRepository;

	@Autowired
	private UserShiftConfigurationMapper userShiftConfigurationMapper;

	@Autowired
	private WorkLocationMapper workLocationMapper;

	@Autowired
	private BackupUserMapper backupUserMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private PaginationMapper paginationMapper;

	@Autowired
	private UserAttemptsRepository userAttemptsRepository;

	@Value("${maxLoginAttempts}")
	private Integer maxLoginAttempts;

	@Autowired
	private LoginHistoryRepository loginHistoryRepository;

	@Autowired
	private PasswordResetRepository passwordResetRepository;

	@Autowired
	private LoginHistoryMapper loginHistoryMapper;

	@Value("${email.create.user.pwd.reset.link}")
	private String resetPasswordLink;

	@Autowired
	private AsyncCommunicationExecution asyncCommunicationExecution;

	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private EmailVerificationRepository emailVerificationRepository;

	@Value("${maxOTPCountAllowed}")
	private Integer maxOTPCountAllowed;

	@Autowired
	private OldPasswordRepository oldPasswordRepository;

	@Autowired
	private RedisUtil redisUtil;

	@Autowired
	private HelpdeskUserAuditRepository helpdeskUserAuditRepository;

	@Autowired
	private AssociatedUserRepository associatedUserRepository;

	@Autowired
	private WorkLocationRepository workflowLocationRepository;

	@Autowired
	private UserRoleRepository userrolerepository;

	@Autowired
	private LoginHistoryRepository loginhistory;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private UserAttemptsRepository userattemptrepository;

	@Override
	@Transactional
	public GenericResponse save(UserRequestDTO requestDTO) {
		GenericResponse response = validator.validateCreateUserRequest(requestDTO);
		if (Objects.isNull(response) || !ErrorCode.SUCCESS_RESPONSE.getErrorCode().equals(response.getErrorCode())) {
			return response;
		}
		UserEntity userEntity = userMapper.requestDTOToEntity(requestDTO);
		String password = CommonUtil.generateRandomPassword();
		userEntity.setPassword(passwordEncoder.encode(password));
		if (Objects.nonNull(userEntity)) {
			UserEntity savedEntity = userRepository.save(userEntity);
			if (Objects.nonNull(requestDTO.getWorkLocationRequestDTO())) {
				requestDTO.getWorkLocationRequestDTO().stream().forEach(requestDTO1 -> {
					WorkLocationEntity workLocationEntity = new WorkLocationEntity();
					if (StringUtils.isBlank(requestDTO1.getEntityTypeCode())) {
						throw new InvalidDataValidation(ResponseMessageConstant.MANDTORY_REQUEST_PARM
								.getMessage(new Object[] { Constant.ENTITY_TYPE_CODE }));
					}
					if (StringUtils.isBlank(requestDTO1.getDepartmentCode())) {
						throw new InvalidDataValidation(ResponseMessageConstant.MANDTORY_REQUEST_PARM
								.getMessage(new Object[] { Constant.DEPARTMENT_CODE }));
					}

					if (StringUtils.isBlank(requestDTO1.getStateCode())) {
						throw new InvalidDataValidation(ResponseMessageConstant.MANDTORY_REQUEST_PARM
								.getMessage(new Object[] { Constant.STATE_CODE }));
					}
					if (StringUtils.isBlank(requestDTO1.getDistrictCode())) {
						throw new InvalidDataValidation(ResponseMessageConstant.MANDTORY_REQUEST_PARM
								.getMessage(new Object[] { Constant.DISTRICT_CODE }));
					}

					workLocationEntity.setStateCode(requestDTO1.getStateCode());
					workLocationEntity.setDepartmentCode(requestDTO1.getDepartmentCode());
					workLocationEntity.setDistrictCode(requestDTO1.getDistrictCode());
					workLocationEntity.setDistrictNames(requestDTO1.getDistrictNames());
					workLocationEntity.setEntityTypeCode(requestDTO1.getEntityTypeCode());
					if (Objects.nonNull(requestDTO1.getUpdateReason())) {
						workLocationEntity.setUpdateReason(requestDTO1.getUpdateReason());
					}
					workLocationEntity.setUser(savedEntity);
					workLocationRepository.save(workLocationEntity);
				});

				// old
//				WorkLocationEntity workflowLocationEntity = workLocationMapper
//						.workLocationRequestDTOTOEntity(requestDTO.getWorkLocationRequestDTO(), null);
//				workflowLocationEntity.setUser(savedEntity);
//				workLocationRepository.save(workflowLocationEntity);

			}
			if (Objects.nonNull(requestDTO.getShiftConfigurationRequestDTO())) {
				UserShiftConfigurationEntity userShiftConfigEntity = userShiftConfigurationMapper
						.userShiftConfigRequestDTOToEntity(requestDTO.getShiftConfigurationRequestDTO(), null);
				userShiftConfigEntity.setUser(savedEntity);
				userShiftConfigurationRepository.save(userShiftConfigEntity);
			}

			if (Objects.nonNull(requestDTO.getBackupUserRequestDTO())) {
				BackupUserEntity backupUserEntity = backupUserMapper
						.backupUserRequestDTOToEntity(requestDTO.getBackupUserRequestDTO(), null);
				backupUserEntity.setUser(savedEntity);
				backupUserRepository.save(backupUserEntity);
			}
			this.saveAuditHistory(userEntity, userEntity.getCreatedDate(), HelpdeskUserAuditAction.CREATED);
			String tokenString = UUID.randomUUID().toString();
			// String link = resetPasswordLink.concat(tokenString);
			String username = userEntity.getFirstName();
			if (StringUtils.isNotBlank(userEntity.getMiddleName())) {
				username = username.concat(Constant.BLANK_STRING).concat(userEntity.getMiddleName());
			}
			username = username.concat(Constant.BLANK_STRING).concat(userEntity.getLastName());
			/*
			 * asyncCommunicationExecution.sendPasswordResetEMail(tokenString,
			 * Locale.ENGLISH, Constant.CUSTOMER_EVENT,
			 * Constant.CUSTOMER_CREATE_USER_SUB_EVENT, link, username, userEntity);
			 */

			asyncCommunicationExecution.sendRegisteredUserPasswordViaEmail(tokenString, Locale.ENGLISH, password,
					username, userEntity);

			return Library.getSuccessfulResponse(userMapper.entityToResponseDTO(savedEntity, Boolean.FALSE),
					ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_CREATED);

		}
		return Library.getFailResponseCode(ErrorCode.RECORD_CREATION_FAILED.getErrorCode(),
				ResponseMessageConstant.RECORD_CREATION_FAILED.getMessage());

	}

	@Override
	public GenericResponse getUsersByRoleId(Long roleId, String districtCode) {
		Optional<RoleMaster> roleMasterOptional = roleMasterRepository.findById(roleId);
		if (!roleMasterOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ROLE_ID }));
		}
		if (Boolean.FALSE.equals(roleMasterOptional.get().getStatus())) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INACTIVE_ID_PASSED.getMessage(new Object[] { ROLE_ID }));
		}
		List<Map<String, String>> userList = new ArrayList<>();
		if (StringUtils.isBlank(districtCode)) {
			userList = userRepository.getUserByRole(roleId);
		} else {
			Long sofware = (long) 12;
			userList = userRepository.getUserByRoleAndDistrict(roleId, districtCode, sofware);
			// userList =
			// userRepository.getUserByRoleAndDistrict(sofware,districtCode,roleId);
		}
		if (CollectionUtils.isEmpty(userList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(userList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getEmployeeId() {
		MenuPrefix prefix = MenuPrefix.getType(EMPLOYEE_ID);
		String id = prefix.toString() + RandomUtil.getRandomNumber();
		while (true) {
			Optional<UserEntity> entity = userRepository.getByEmployeeIdIgnoreCase(id);
			if (entity.isPresent()) {
				id = prefix.toString() + RandomUtil.getRandomNumber();
			} else {
				break;
			}
		}
		return Library.getSuccessfulResponse(id, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	@Transactional
	public GenericResponse changePassword(ChangePasswordDto requestDTO) {
		AuthenticationDTO authenticationDTO = SecurityUtils.findAuthenticationObject();
		if (Objects.isNull(authenticationDTO) || Objects.isNull(authenticationDTO.getUserId())
				|| CollectionUtils.isEmpty(authenticationDTO.getRoleCodes())) {
			log.error("authentication details missing");
			return Library.getFailResponseCode(ErrorCode.ACCESS_DENIED.getErrorCode(), ErrorMessages.ACCESS_DENIED);
		}
		Optional<UserEntity> entity = userRepository.findById(authenticationDTO.getUserId());
		if (!entity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { USERNAME }));
		}
		UserEntity userEntity = entity.get();
		if (!passwordEncoder.matches(requestDTO.getCurrentPassword(), userEntity.getPassword())) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ErrorMessages.INVALID_OLD_PASSWORD);
		}
		List<OldPasswordEntity> oldPasswordEntities = oldPasswordRepository.findByUserId(userEntity.getId());

		if (this.isPasswordAlreadyExist(requestDTO.getPassword(), userEntity.getPassword(), userEntity.getId(),
				oldPasswordEntities)) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(), ErrorMessages.NO_CHANGE);

		}
		if (!requestDTO.getPassword().equals(requestDTO.getConfirmPassword())) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ErrorMessages.NEW_PASSWORD_CONFIRM_PASSWORD_MISMATCH);

		}
		if (Boolean.FALSE.equals(CommonUtil.isStrongPassword(requestDTO.getPassword()))) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.WEAK_PASSWORD.getMessage());
		}

		userEntity.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
		userRepository.save(userEntity);
		if (Objects.nonNull(oldPasswordEntities) && !CollectionUtils.isEmpty(oldPasswordEntities)) {
			if (oldPasswordEntities.size() >= 5) {
				OldPasswordEntity oldPasswordEntity = oldPasswordRepository
						.findTop1ByUserIdOrderByUpdatedDateAsc(userEntity.getId());
				oldPasswordRepository.delete(oldPasswordEntity);
			}
		}
		OldPasswordEntity oldPasswordEntity = new OldPasswordEntity();
		oldPasswordEntity.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
		oldPasswordEntity.setUserId(userEntity.getId());
		oldPasswordEntity.setUpdatedDate(LocalDateTime.now());
		oldPasswordRepository.save(oldPasswordEntity);
//		redisUtil.delete(userEntity.getEmailId() + Constant.UNDERSCORE + userEntity.getEmployeeId());
		log.info("token deleted for user : {}", userEntity.getEmailId());
		return Library.getSuccessfulResponse(userEntity, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.CHANGE_PASSWORD);
	}

	@Override
	public GenericResponse getAll() {
		List<UserEntity> userList = userRepository.findAll();
		if (CollectionUtils.isEmpty(userList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<UserResponseDTO> responseDTOList = userList.stream()
				.map(user -> userMapper.entityToResponseDTO(user, Boolean.FALSE)).collect(Collectors.toList());
		return Library.getSuccessfulResponse(responseDTOList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getById(Long id) {
		AuthenticationDTO authenticationDTO = SecurityUtils.findAuthenticationObject();
		if (Objects.isNull(authenticationDTO) || Objects.isNull(authenticationDTO.getUserId())
				|| CollectionUtils.isEmpty(authenticationDTO.getRoleCodes())) {
			log.error("authentication details missing");
			return Library.getFailResponseCode(ErrorCode.ACCESS_DENIED.getErrorCode(), ErrorMessages.ACCESS_DENIED);
		}
		List<String> roles = authenticationDTO.getRoleCodes();
		RoleMaster roleMaster = roleMasterRepository.findByRoleCode(Constant.HELPDESK_ADMIN);
		if (!roles.stream().anyMatch(r -> roleMaster.getRoleCode().equals(r))) {
			id = authenticationDTO.getUserId();
		}
		Optional<UserEntity> userEntity = userRepository.findById(id);
		if (!userEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(userMapper.entityToResponseDTO(userEntity.get(), Boolean.TRUE),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}

	@Override
	@Transactional
	public GenericResponse update(UpdateUserRequestDTO requestDTO) {
		Optional<UserEntity> userEntityOptional = userRepository.findById(requestDTO.getId());
		if (!userEntityOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		UserEntity userEntity = userEntityOptional.get();
		userEntity.setUsername(requestDTO.getUsername());
		userEntity.setAddress(requestDTO.getAddress());
		userEntity.setDeviceId(requestDTO.getDeviceId());
		userEntity.setEmailId(requestDTO.getEmailid());
		userEntity.setFirstName(requestDTO.getFirstName());
		userEntity.setLastName(requestDTO.getLastName());
		userEntity.setMiddleName(requestDTO.getMiddleName());
		userEntity.setPhoneNumber(requestDTO.getPhoneNumber());
		userEntity.setDesignationCode(requestDTO.getDesignationCode());
		userEntity.setEmploymentStatus(requestDTO.getEmploymentStatus());
		if (Objects.nonNull(requestDTO.getBackupUserRequestDTO())) {
			RoleMaster roleMaster = roleMasterRepository.findByRoleCode(Constant.HELPDESK_ADMIN);
			if (Objects.isNull(roleMaster) || Objects.isNull(roleMaster.getId())) {
				return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
						ErrorMessages.ROLE_NOT_CONFIGURED);
			}
//			if (!userEntityOptional.get().getRoles().stream()
//					.anyMatch(r -> requestDTO.getBackupUserRequestDTO().getRoleId().equals(r.getId()))
//					&& !roleMaster.getId().equals(requestDTO.getBackupUserRequestDTO().getRoleId())) {
//				return Library.getFailResponseCode(ErrorCode.ROLES_MISMATCH.getErrorCode(),
//						ResponseMessageConstant.ROLES_MISMATCH.getMessage());
//			}

			Long useridq = userEntity.getId();
			userrolerepository.updateAssociatedRecords(requestDTO.getRoleId(), useridq);

		}

//		UserEntity userEntity = userEntityOptional.get();

		if (Objects.nonNull(requestDTO.getWorkLocationRequestDTO())) {
			Long userid = userEntity.getId();
			Optional<WorkLocationEntity> workLocationEntityOptional = workflowLocationRepository.findById(userid);
			try {
				workLocationRepository.deleteAssociatedRecords(userEntity.getId());
			} catch (Exception e) {
				log.info(e + "delete records");
			}

			requestDTO.getWorkLocationRequestDTO().stream().forEach(requestDTO1 -> {
				WorkLocationEntity workLocationEntity = new WorkLocationEntity();
				if (StringUtils.isBlank(requestDTO1.getEntityTypeCode())) {
					throw new InvalidDataValidation(ResponseMessageConstant.MANDTORY_REQUEST_PARM
							.getMessage(new Object[] { Constant.ENTITY_TYPE_CODE }));
				}
				if (StringUtils.isBlank(requestDTO1.getDepartmentCode())) {
					throw new InvalidDataValidation(ResponseMessageConstant.MANDTORY_REQUEST_PARM
							.getMessage(new Object[] { Constant.DEPARTMENT_CODE }));
				}

				if (StringUtils.isBlank(requestDTO1.getStateCode())) {
					throw new InvalidDataValidation(ResponseMessageConstant.MANDTORY_REQUEST_PARM
							.getMessage(new Object[] { Constant.STATE_CODE }));
				}
				if (StringUtils.isBlank(requestDTO1.getDistrictCode())) {
					throw new InvalidDataValidation(ResponseMessageConstant.MANDTORY_REQUEST_PARM
							.getMessage(new Object[] { Constant.DISTRICT_CODE }));
				}

				workLocationEntity.setStateCode(requestDTO1.getStateCode());
				workLocationEntity.setDepartmentCode(requestDTO1.getDepartmentCode());
				workLocationEntity.setDistrictCode(requestDTO1.getDistrictCode());
				workLocationEntity.setDistrictNames(requestDTO1.getDistrictNames());
				workLocationEntity.setEntityTypeCode(requestDTO1.getEntityTypeCode());
				if (Objects.nonNull(requestDTO1.getUpdateReason())) {
					workLocationEntity.setUpdateReason(requestDTO1.getUpdateReason());
				}
				workLocationEntity.setUser(userEntity);
				workLocationRepository.save(workLocationEntity);
			});
		}
		// old method
//		if (Objects.nonNull(requestDTO.getWorkLocationRequestDTO())) {
//			WorkLocationEntity workLocationEntity = workLocationMapper
//					.workLocationRequestDTOTOEntity(requestDTO.getWorkLocationRequestDTO(), userEntity.getId());
//			if (Objects.nonNull(workLocationEntity.getUser())
//					&& !userEntity.getId().equals(workLocationEntity.getUser().getId())) {
//				throw new InvalidDataValidation(ResponseMessageConstant.INVALID_REQUEST_PARM
//						.getMessage(new Object[] { Constant.WORKFLOW_LOCATION_ID }));
//			}
//			workLocationEntity.setUser(userEntity);
//			workLocationRepository.save(workLocationEntity);
//		}
//		

		if (Objects.nonNull(requestDTO.getShiftConfigurationRequestDTO())) {
			UserShiftConfigurationEntity userShiftConfigEntity = userShiftConfigurationMapper
					.userShiftConfigRequestDTOToEntity(requestDTO.getShiftConfigurationRequestDTO(),
							userEntity.getId());
			if (Objects.nonNull(userShiftConfigEntity.getUser())
					&& !userEntity.getId().equals(userShiftConfigEntity.getUser().getId())) {
				throw new InvalidDataValidation(ResponseMessageConstant.INVALID_REQUEST_PARM
						.getMessage(new Object[] { Constant.USER_SHIFT_CONFIGURATION_ID }));
			}
			userShiftConfigEntity.setUser(userEntity);
			userShiftConfigurationRepository.save(userShiftConfigEntity);
		}

		if (Objects.nonNull(requestDTO.getBackupUserRequestDTO())) {
			BackupUserEntity backupUserEntity = backupUserMapper
					.backupUserRequestDTOToEntity(requestDTO.getBackupUserRequestDTO(), userEntity.getId());
			if (Objects.nonNull(backupUserEntity.getUser())
					&& !userEntity.getId().equals(backupUserEntity.getUser().getId())) {
				throw new InvalidDataValidation(ResponseMessageConstant.INVALID_REQUEST_PARM
						.getMessage(new Object[] { Constant.BACKUP_USER_ID }));
			}
			backupUserEntity.setUser(userEntity);
			backupUserEntity.setRoleId(requestDTO.getBackupUserRequestDTO().getRoleId());
			backupUserRepository.save(backupUserEntity);
		}
		this.saveAuditHistory(userEntity, userEntity.getModifiedDate(), HelpdeskUserAuditAction.UPDATED);

		return Library.getSuccessfulResponse(userMapper.entityToResponseDTO(userEntity, Boolean.FALSE),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_UPDATED);

	}

	
	@Override
	public GenericResponse searchByFilter(PaginationRequestDTO paginationDto) {
		Pageable pageable = null;
		Page<UserEntity> list = null;
		String employeeId = null;
		String username = null;
		if (StringUtils.isBlank(paginationDto.getSortField())) {
			paginationDto.setSortField(MODIFIED_DATE);
		}
		if (StringUtils.isBlank(paginationDto.getSortOrder())) {
			paginationDto.setSortOrder(DESC);
		}
		if (paginationDto.getSortOrder().equalsIgnoreCase(ASC)) {
			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
					Sort.by(Direction.ASC, paginationDto.getSortField()));
		} else {
			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
					Sort.by(Direction.DESC, paginationDto.getSortField()));
		}
		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get(Constant.EMPLOYEE_ID))
					&& !paginationDto.getFilters().get(Constant.EMPLOYEE_ID).toString().trim().isEmpty()) {
				try {
					employeeId = String.valueOf(paginationDto.getFilters().get(Constant.EMPLOYEE_ID).toString());
				} catch (Exception e) {
					log.error("error occurred while parsing id :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}

			if (Objects.nonNull(paginationDto.getFilters().get(Constant.USERNAME))
					&& !paginationDto.getFilters().get(Constant.USERNAME).toString().trim().isEmpty()) {
				try {
					username = String.valueOf(paginationDto.getFilters().get(Constant.USERNAME).toString());
				} catch (Exception e) {
					log.error("error occurred while parsing status :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}
		}
		if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(employeeId)) {
			list = userRepository.findByUsernameIgnoreCaseAndEmployeeIdIgnoreCaseAndIsSystemDefaultUser(username,
					employeeId, Boolean.FALSE, pageable);
		} else if (StringUtils.isNotBlank(username) && StringUtils.isBlank(employeeId)) {
			list = userRepository.findByUsernameIgnoreCaseAndIsSystemDefaultUser(username, Boolean.FALSE, pageable);
		} else if (StringUtils.isBlank(username) && StringUtils.isNotBlank(employeeId)) {
			list = userRepository.findByEmployeeIdIgnoreCaseAndIsSystemDefaultUser(employeeId, Boolean.FALSE, pageable);
		}
		if (Objects.isNull(list)) {
			list = userRepository.findByIsSystemDefaultUser(Boolean.FALSE, pageable);
		}
		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		Page<UserResponseDTO> finalResponse = list.map(user -> userMapper.entityToResponseDTO(user, Boolean.FALSE));
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getBackuUserListByRoleId(Long roleId, Long userId) {
		Optional<RoleMaster> roleMasterOptional = roleMasterRepository.findById(roleId);
		if (!roleMasterOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ROLE_ID }));
		}
		if (Boolean.FALSE.equals(roleMasterOptional.get().getStatus())) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INACTIVE_ID_PASSED.getMessage(new Object[] { ROLE_ID }));
		}

		List<Map<String, String>> userList = null;

		if (Objects.isNull(userId)) {
			userList = userRepository.getBackupUserListByRole(roleId);
		} else {
			userList = userRepository.getBackupUserListByRoleAndUserId(roleId, userId);
		}
		if (CollectionUtils.isEmpty(userList)) {
			userList = userRepository.getBackupUserListByRole();
		}

		if (CollectionUtils.isEmpty(userList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(userList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public Boolean updateFailAttempts(String username) {
		Boolean isLocked = false;
		Optional<UserEntity> userEntityOptional = userRepository.findByUsernameOrEmailIdIgnoreCase(username, username);

		if (userEntityOptional.isPresent()) {
			// get already attempted count
			UserAttempts userAttempts = getUserAttempts(userEntityOptional.get().getId());
			if (Objects.isNull(userAttempts)) {
				// if no record, insert a new
				userAttempts = new UserAttempts();
				userAttempts.setUserId(userEntityOptional.get().getId());
				userAttempts.setLastModified(LocalDate.now());
				userAttempts.setAttempts(1);
				userAttemptsRepository.save(userAttempts);
			} else {
				int attempts = userAttempts.getAttempts();
				if (userAttempts.getAttempts() + 1 >= maxLoginAttempts) {
					userRepository.lockedUser(userEntityOptional.get().getId());
					isLocked = true;
				} else {
					userAttempts.setLastModified(LocalDate.now());
					userAttempts.setAttempts(attempts + 1);
					userAttemptsRepository.save(userAttempts);
				}
			}
		} else {
			throw new InvalidDataValidation(ErrorMessages.ERROR_USERID_INVALID);
		}
		return isLocked;
	}

	@Override
	public UserAttempts getUserAttempts(Long userId) {
		return userAttemptsRepository.getUserAttempts(userId);
	}

	@Override
	public void resetFailAttempts(Long userId) {
		userAttemptsRepository.resetFailAttempts(userId);
	}

	@Override
	public void saveUpdateLoginHistory(Long userId, String loginIP, String event) {
		try {
			if (event.equalsIgnoreCase("login")) {
				LoginHistory loginHistory = new LoginHistory(loginIP, userId, LocalDateTime.now());
				loginHistoryRepository.save(loginHistory);
			} else {
				LoginHistory loginHistory = loginHistoryRepository.findTop1ByUserIdOrderByLoginTimeDesc(userId);
				loginHistory.setLogoutTime(LocalDateTime.now());
				loginHistoryRepository.save(loginHistory);
			}
		} catch (Exception exception) {
			log.error("======exception occurred while saveLoginHistory ===" + userId, exception);
		}
	}

	@Override
	public List<LoginHistory> findLastLoginTime(Long userId) {
		return loginHistoryRepository.findTop2ByUserIdOrderByLoginTimeDesc(userId);
	}

	@Override
	public GenericResponse resetPassword(PasswordResetDTO passwordResetDTO) {
		Boolean isSuccess = true;
		GenericResponse baseResponse = null;
		try {
			PasswordReset token = passwordResetRepository.findByToken(passwordResetDTO.getToken());
			if (Objects.isNull(token)) {
				isSuccess = false;
				baseResponse = Library.getFailResponseCode(ErrorCode.INVALID_OTP_OR_TOKEN.getErrorCode(),
						ResponseMessageConstant.INVALID_OTP_OR_TOKEN.getMessage());
			} else if (token.isExpired()) {
				isSuccess = false;
				baseResponse = Library.getFailResponseCode(ErrorCode.EXPIRED_OTP_OR_TOKEN.getErrorCode(),
						ResponseMessageConstant.EXPIRED_OTP_OR_TOKEN.getMessage());
			} else if (Boolean.FALSE.equals(token.getStatus())) {
				isSuccess = false;
				baseResponse = Library.getFailResponseCode(ErrorCode.INACTIVE_OTP_OR_TOKEN.getErrorCode(),
						ResponseMessageConstant.INACTIVE_OTP_OR_TOKEN.getMessage());
			} else {
				UserEntity user = token.getUser();
				if (isSuccess) {
					String finalPassword = passwordEncoder.encode(passwordResetDTO.getPassword());
					log.info("=================recieved password=====" + user.getEmailId());
					user.setPassword(finalPassword);
					user.setAccountLocked(Boolean.FALSE);
					user.setEmploymentStatus(EmploymentStatus.ACTIVE);
					token.setStatus(Boolean.FALSE);
					userRepository.save(user);
					passwordResetRepository.save(token);
					baseResponse = Library.getSuccessfulResponse(null,
							ErrorCode.PASSWORD_UPDATED_SUCCESSFULLY.getCode(),
							ResponseMessageConstant.PASSWORD_UPDATED_SUCCESSFULLY.getMessage());

				}
			}
		} catch (Exception exception) {
			log.error("======resetPassword===" + passwordResetDTO.getToken(), exception);
			isSuccess = false;
		}
		return baseResponse;
	}

	@Override
	public GenericResponse getLoginHistoryBySearchFilter(PaginationRequestDTO paginationDto) {
		Pageable pageable = null;
		Page<LoginHistory> list = null;
		AuthenticationDTO authenticationDTO = null;
		Long userId = null;
		if (StringUtils.isBlank(paginationDto.getSortField())) {
			paginationDto.setSortField(MODIFIED_DATE);
		}
		if (StringUtils.isBlank(paginationDto.getSortOrder())) {
			paginationDto.setSortOrder(DESC);
		}
		if (paginationDto.getSortOrder().equalsIgnoreCase(ASC)) {
			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
					Sort.by(Direction.ASC, paginationDto.getSortField()));
		} else {
			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
					Sort.by(Direction.DESC, paginationDto.getSortField()));
		}

		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get(Constant.USER_ID))
					&& !paginationDto.getFilters().get(Constant.USER_ID).toString().trim().isEmpty()) {
				try {
					userId = Long.valueOf(paginationDto.getFilters().get(Constant.USER_ID).toString());
				} catch (Exception e) {
					log.error("error occurred while parsing id :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}
		}
		if (Objects.isNull(userId)) {
			if (SecurityContextHolder.getContext() != null
					&& SecurityContextHolder.getContext().getAuthentication() != null) {
				authenticationDTO = (AuthenticationDTO) SecurityContextHolder.getContext().getAuthentication()
						.getPrincipal();
			}
			userId = authenticationDTO.getUserId();
		}

		list = loginHistoryRepository.findByUserIdOrderByLoginTimeDesc(userId, pageable);

		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		Page<LoginHistoryResponseDTO> finalResponse = list.map(loginHistoryMapper::convertEntityToResponseDTO);
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}

	@Override
	public Boolean checkEmail(String emailId, Long id) {
		Optional<UserEntity> userOptional = null;
		Boolean isEmailExist = false;
		if (Objects.isNull(id)) {
			userOptional = userRepository.findByEmailId(emailId);
		} else {
			List<Long> idList = new ArrayList<>();
			idList.add(id);
			userOptional = userRepository.findByEmailIdAndIdNotIn(emailId, idList);
		}
		if (userOptional.isPresent()) {
			isEmailExist = true;
		}
		return isEmailExist;
	}

	@Override
	public Boolean checkPhoneNumber(String phoneNumber, Long id) {
		Optional<UserEntity> userOptional = null;
		Boolean isMobileNumberExist = false;
		if (Objects.isNull(id)) {
			userOptional = userRepository.findByPhoneNumber(phoneNumber);
		} else {
			List<Long> idList = new ArrayList<>();
			idList.add(id);
			userOptional = userRepository.findByPhoneNumberAndIdNotIn(phoneNumber, idList);
		}
		if (userOptional.isPresent()) {
			isMobileNumberExist = true;
		}
		return isMobileNumberExist;
	}

	@Override
	public Boolean checkUserName(String username, Long id) {
		Optional<UserEntity> userOptional = null;
		Boolean isUsernameExist = false;
		if (Objects.isNull(id)) {
			userOptional = userRepository.findByUsernameIgnoreCase(username);
		} else {
			List<Long> idList = new ArrayList<>();
			idList.add(id);
			userOptional = userRepository.findByUsernameIgnoreCaseAndIdNotIn(username, idList);
		}
		if (userOptional.isPresent()) {
			isUsernameExist = true;
		}
		return isUsernameExist;
	}

	@Override
	public GenericResponse generateOTP(String emailId) {
		if (!commonUtil.isEmailValid(emailId)) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { EMAIL_ID }));
		}
		EmailVerification emailVerification = emailVerificationRepository.findByEmailId(emailId);
		if (Objects.nonNull(emailVerification)) {
			if (Boolean.TRUE.equals(emailVerification.getVerified())) {
				return Library.getSuccessfulResponse(null, ErrorCode.ALREADY_VERIFIED.getErrorCode(),
						ErrorMessages.EMAIL_ALREADY_VERIFIED);
			}
			if (emailVerification.getOtpCount().equals(maxOTPCountAllowed)
					&& emailVerification.getOtpExpiryDateTime().toLocalDate().isEqual(LocalDate.now())) {
				return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
						ResponseMessageConstant.MAX_OTP_COUNT_EXCEEDED.getMessage(new Object[] { maxOTPCountAllowed }));
			}
		}
		Random rnd = new Random();
		int number = rnd.nextInt(999999);
		String otp = String.format("%06d", number);
		ApiResponse apiResponse = asyncCommunicationExecution.sendOTPViaEmail(Locale.ENGLISH, Constant.CUSTOMER_EVENT,
				Constant.CUSTOMER_PASSWORD_RESET_OTP_SUB_EVENT, otp, emailId);
		if (Objects.isNull(apiResponse)) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), apiResponse.getResponseMsg());

		}
		if (Objects.nonNull(emailVerification)) {
			emailVerification.setOtp(otp);
			emailVerification.setExpiryDateTime(10);
			emailVerification.setVerified(Boolean.FALSE);
			emailVerification.setOtpCount(emailVerification.getOtpCount() + 1);
		} else {
			emailVerification = new EmailVerification();
			emailVerification.setEmailId(emailId);
			emailVerification.setOtp(otp);
			emailVerification.setExpiryDateTime(10);
			emailVerification.setVerified(Boolean.FALSE);
			emailVerification.setOtpCount(1);
		}
		emailVerificationRepository.save(emailVerification);
		return Library.getSuccessfulResponse(null, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.OTP_SENT_SUCCESSFULLY);
	}

	@Override
	public GenericResponse verifyOTP(OTPVerificationDTO otpVerificationDTO) {
		EmailVerification emailVerification = emailVerificationRepository
				.findByEmailId(otpVerificationDTO.getEmailId());
//		if (Objects.isNull(emailVerification)) {
//			return Library.getSuccessfulResponse(null, ErrorCode.NO_RECORD_FOUND.getErrorCode(),
//					ErrorMessages.NO_RECORD_FOUND);
//		}
//		if (Boolean.TRUE.equals(emailVerification.getVerified())) {
//			return Library.getSuccessfulResponse(null, ErrorCode.ALREADY_VERIFIED.getErrorCode(),
//					ErrorMessages.EMAIL_ALREADY_VERIFIED);
//		}
//		if (emailVerification.getOtpExpiryDateTime().isBefore(LocalDateTime.now())) {
//			return Library.getFailResponseCode(ErrorCode.EXPIRED_OTP_OR_TOKEN.getErrorCode(),
//					ResponseMessageConstant.EXPIRED_OTP_OR_TOKEN.getMessage());
//		}
//		if (otpVerificationDTO.getOtp().equals(emailVerification.getOtp())) {
		emailVerification.setVerified(Boolean.TRUE);
		emailVerificationRepository.save(emailVerification);
//		} else {
//			return Library.getFailResponseCode(ErrorCode.INVALID_OTP_OR_TOKEN.getErrorCode(),
//					ResponseMessageConstant.INVALID_OTP_OR_TOKEN.getMessage());
//		}
		return Library.getSuccessfulResponse(null, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.EMAIL_VERIFIED_SUCCESSFULLY);
	}

	private Boolean isPasswordAlreadyExist(String newPassword, String oldPassword, Long userId,
			List<OldPasswordEntity> oldPasswordEntities) {
		Boolean isAlreadyExist = passwordEncoder.matches(newPassword, oldPassword);
		if (Boolean.FALSE.equals(isAlreadyExist)) {
			if (Objects.nonNull(oldPasswordEntities) && !CollectionUtils.isEmpty(oldPasswordEntities)) {
				isAlreadyExist = oldPasswordEntities.stream()
						.anyMatch(entity -> passwordEncoder.matches(newPassword, entity.getPassword()));
			}
		}
		return isAlreadyExist;
	}

	@Override
	public GenericResponse generateForgotPasswordOTP(String emailId) {
		if (StringUtils.isBlank(emailId)) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { EMAIL_ID }));
		}
		if (!commonUtil.isEmailValid(emailId)) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { EMAIL_ID }));
		}
		Optional<UserEntity> userEntity = userRepository.findByEmailId(emailId);
		if (!userEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { EMAIL_ID }));
		}

		Random rnd = new Random();
		int number = rnd.nextInt(999999);
		String otp = String.format("%06d", number);
		ApiResponse apiResponse = asyncCommunicationExecution.sendOTPViaEmail(Locale.ENGLISH, Constant.CUSTOMER_EVENT,
				Constant.CUSTOMER_PASSWORD_RESET_OTP_SUB_EVENT, otp, emailId);
		if (Objects.isNull(apiResponse)) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), apiResponse.getResponseMsg());

		}
		PasswordReset passwordReset = new PasswordReset();
		passwordReset.setToken(otp);
		passwordReset.setUser(userEntity.get());
		passwordReset.setExpiryDate(10);
		passwordResetRepository.save(passwordReset);
		return Library.getSuccessfulResponse(null, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.OTP_SENT_SUCCESSFULLY);
	}

	@Override
	@Transactional
	public GenericResponse forgotPassword(ForgotPasswordDTO requestDTO) {
		Optional<UserEntity> entity = userRepository.findByUsernameIgnoreCaseOrEmailId(requestDTO.getEmailId(),
				requestDTO.getEmailId());
		if (!entity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { EMAIL_ID }));
		}
		UserEntity userEntity = entity.get();
		List<OldPasswordEntity> oldPasswordEntities = oldPasswordRepository.findByUserId(userEntity.getId());

		if (this.isPasswordAlreadyExist(requestDTO.getPassword(), userEntity.getPassword(), userEntity.getId(),
				oldPasswordEntities)) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(), ErrorMessages.NO_CHANGE);

		}
		if (!requestDTO.getPassword().equals(requestDTO.getConfirmPassword())) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ErrorMessages.NEW_PASSWORD_CONFIRM_PASSWORD_MISMATCH);

		}
		if (Boolean.FALSE.equals(CommonUtil.isStrongPassword(requestDTO.getPassword()))) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.WEAK_PASSWORD.getMessage());
		}
		if (Objects.nonNull(oldPasswordEntities) && !CollectionUtils.isEmpty(oldPasswordEntities)) {
			if (oldPasswordEntities.size() >= 5) {
				OldPasswordEntity oldPasswordEntity = oldPasswordRepository
						.findTop1ByUserIdOrderByUpdatedDateAsc(userEntity.getId());
				oldPasswordRepository.delete(oldPasswordEntity);
			}
		}

		Boolean isSuccess = true;
		GenericResponse baseResponse = null;
		try {
			PasswordReset token = passwordResetRepository.findByToken(requestDTO.getOtp());
			if (Objects.isNull(token)) {
				isSuccess = false;
				baseResponse = Library.getFailResponseCode(ErrorCode.INVALID_OTP_OR_TOKEN.getErrorCode(),
						ResponseMessageConstant.INVALID_OTP_OR_TOKEN.getMessage());
			} else if (token.isExpired()) {
				isSuccess = false;
				baseResponse = Library.getFailResponseCode(ErrorCode.EXPIRED_OTP_OR_TOKEN.getErrorCode(),
						ResponseMessageConstant.EXPIRED_OTP_OR_TOKEN.getMessage());
			} else if (Boolean.FALSE.equals(token.getStatus())) {
				isSuccess = false;
				baseResponse = Library.getFailResponseCode(ErrorCode.INACTIVE_OTP_OR_TOKEN.getErrorCode(),
						ResponseMessageConstant.INACTIVE_OTP_OR_TOKEN.getMessage());
			} else {
				UserEntity user = token.getUser();
				if (isSuccess) {
					String finalPassword = passwordEncoder.encode(requestDTO.getPassword());
					log.info("=================recieved password=====" + user.getEmailId());
					user.setPassword(finalPassword);
					user.setAccountLocked(Boolean.FALSE);
					user.setEmploymentStatus(EmploymentStatus.ACTIVE);
					token.setStatus(Boolean.FALSE);
					userRepository.save(user);
					passwordResetRepository.save(token);
					OldPasswordEntity oldPasswordEntity = new OldPasswordEntity();
					oldPasswordEntity.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
					oldPasswordEntity.setUserId(userEntity.getId());
					oldPasswordEntity.setUpdatedDate(LocalDateTime.now());
					oldPasswordRepository.save(oldPasswordEntity);
					baseResponse = Library.getSuccessfulResponse(null,
							ErrorCode.PASSWORD_UPDATED_SUCCESSFULLY.getCode(),
							ResponseMessageConstant.PASSWORD_UPDATED_SUCCESSFULLY.getMessage());
//					redisUtil.delete(userEntity.getEmailId() + Constant.UNDERSCORE + userEntity.getEmployeeId());
					log.info("token deleted for user : {}", userEntity.getEmailId());
				}
			}
		} catch (Exception exception) {
			log.error("======resetPassword=== {} ", exception);
			isSuccess = false;
		}
		return baseResponse;
	}

	@Override
	public GenericResponse logout() {
		AuthenticationDTO authenticationDTO = null;
		try {
			if (SecurityContextHolder.getContext() != null
					&& SecurityContextHolder.getContext().getAuthentication() != null) {
				authenticationDTO = (AuthenticationDTO) SecurityContextHolder.getContext().getAuthentication()
						.getPrincipal();
			}
		} catch (Exception e) {
			log.error("authentication details missing: {}", e);
			return Library.getSuccessfulResponse(null, ErrorCode.SUCCESS_RESPONSE.getCode(),
					ErrorMessages.USER_LOGGED_OUT_SUCCESSFULLY);
		}

		if (Objects.isNull(authenticationDTO)) {
			return Library.getSuccessfulResponse(null, ErrorCode.SUCCESS_RESPONSE.getCode(),
					ErrorMessages.USER_LOGGED_OUT_SUCCESSFULLY);
		}
		Optional<UserEntity> entity = userRepository.findById(authenticationDTO.getUserId());
		if (!entity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(), ErrorMessages.INVALID_USER);
		}
		LoginHistory loginHistory = loginHistoryRepository
				.findTop1ByUserIdOrderByLoginTimeDesc(authenticationDTO.getUserId());

		loginHistory.setLogoutTime(LocalDateTime.now());
		loginHistoryRepository.save(loginHistory);

//		redisUtil.delete(authenticationDTO.getEmail() + Constant.UNDERSCORE + authenticationDTO.getEmployeeId());
		return Library.getSuccessfulResponse(null, ErrorCode.SUCCESS_RESPONSE.getCode(),
				ErrorMessages.USER_LOGGED_OUT_SUCCESSFULLY);
	}

	private void saveAuditHistory(UserEntity entity, Date date, HelpdeskUserAuditAction action) {
		AuthenticationDTO authenticationDTO = SecurityUtils.findAuthenticationObject();
		ZoneId defaultZoneId = ZoneId.systemDefault();
		LocalDateTime localDateTime = date.toInstant().atZone(defaultZoneId).toLocalDateTime();
		Optional<UserEntity> userEntity = userRepository.findById(authenticationDTO.getUserId());
		if (userEntity.isPresent()) {
			HelpdeskUserAuditEntity helpdeskUserAuditEntity = new HelpdeskUserAuditEntity(userEntity.get(), entity,
					action, EmploymentStatus.getType(entity.getEmploymentStatus()), localDateTime);
			helpdeskUserAuditRepository.save(helpdeskUserAuditEntity);
		}
	}

//	@Override
//	@Transactional
//	public GenericResponse saveOrUpdateAssociatedUsers(AssociatedUserRequestDTO requestDTO) {
//		List<AssociatedUserResponseDTO> responseList = new ArrayList<>();
//
////		GenericResponse response = validator.validateAssociateUserRequestDTO(requestDTO);
////		if (Objects.isNull(response) || !ErrorCode.SUCCESS_RESPONSE.getErrorCode().equals(response.getErrorCode())) {
////			return response;
////		}
//
//		requestDTO.getHandlingOfficerIds().forEach(handlingOfficerId -> {
//			AssociatedUserResponseDTO responseDTO = new AssociatedUserResponseDTO();
//			responseDTO.setHandlingOfficerUserId(handlingOfficerId);
//			if (CollectionUtils.isEmpty(requestDTO.getAssignToOfficerIds())) {
//				responseDTO.setAssignToOfficerList(null);
//				List<AssociatedUserEntity> existingAssociationRecords = associatedUserRepository
//						.getUserByHandlingOfficerId(handlingOfficerId);
//				if (!CollectionUtils.isEmpty(existingAssociationRecords)) {
//					log.info("records to delete :: {}", existingAssociationRecords);
//					associatedUserRepository.deleteAll(existingAssociationRecords);
//				}
//			} else {
//				requestDTO.getAssignToOfficerIds().forEach(assignToOfficerId -> {
//					Optional<AssociatedUserEntity> associatedUserOptional = associatedUserRepository
//							.getUserByHandlingOfficerIdAndInspectingOfficerId(handlingOfficerId, assignToOfficerId);
//					if (!associatedUserOptional.isPresent()) {
//						AssociatedUserEntity associatedUser = new AssociatedUserEntity();
//						Optional<UserEntity> assignToOfficerIdOptional = userRepository.findById(assignToOfficerId);
//						UserEntity assignToOfficerIdEntity = assignToOfficerIdOptional.get();
//						associatedUser.setUser(assignToOfficerIdEntity);
//						associatedUser.setAssociatedUserId(handlingOfficerId);
//						associatedUserRepository.save(associatedUser);
//					}
//				});
////				if (Boolean.TRUE.equals(requestDTO.getIsUpdateRequest())
////						&& !CollectionUtils.isEmpty(requestDTO.getAssignToOfficerIds())) {
//					associatedUserRepository.deleteAssociatedRecords(requestDTO.getAssignToOfficerIds(),
//							handlingOfficerId);
////				}
//				responseDTO.setAssignToOfficerList(requestDTO.getAssignToOfficerIds());
//				responseList.add(responseDTO);
//			}
//			//
//		});
//
//		return Library.getSuccessfulResponse(responseList, ErrorCode.SUCCESS_RESPONSE.getCode(),
//				ErrorMessages.RECORED_UPDATED);
//	}
//	

	@Override
	@Transactional
	public GenericResponse saveOrUpdateAssociatedUsers(AssociatedUserRequestDTO requestDTO) {
		List<AssociatedUserResponseDTO> responseList = new ArrayList<>();
		List<AssociatedUserEntity> finalList = new ArrayList<AssociatedUserEntity>();
		requestDTO.getHandlingOfficerIds().forEach(handlingOfficerId -> {
			AssociatedUserResponseDTO responseDTO = new AssociatedUserResponseDTO();
			responseDTO.setHandlingOfficerUserId(handlingOfficerId.getId());
			responseDTO.setHandlingfirstName(handlingOfficerId.getFirstName());
			if (CollectionUtils.isEmpty(requestDTO.getAssignToOfficerIds())) {
				// responseDTO.setAssignToOfficerList(null);
				List<AssociatedUserEntity> existingAssociationRecords = associatedUserRepository
						.getUserByHandlingOfficerId(responseDTO.getHandlingOfficerUserId());
				if (!CollectionUtils.isEmpty(existingAssociationRecords)) {
					log.info("records to delete :: {}", existingAssociationRecords);
					associatedUserRepository.deleteAll(existingAssociationRecords);
				}
			} else {
				requestDTO.getAssignToOfficerIds().forEach(assignToOfficerId -> {
					responseDTO.setAssignToOfficerId(assignToOfficerId.getId());
					Long assigntoid = responseDTO.getAssignToOfficerId();
					Long handlingofficerid = responseDTO.getHandlingOfficerUserId();
					Long assign2 = assigntoid;

					if ((assigntoid - handlingofficerid) == 0) {
						System.out.println("ASSIGNTOID_HANDLINGOFFICERID_SAME");
						log.info("ASSIGNTOID_HANDLINGOFFICERID_SAME");
					}

					else {
//						Optional<AssociatedUserEntity> assigntooptional = associatedUserRepository
//								.getByAssociatedUserId((responseDTO.getHandlingOfficerUserId()));
//						
						Optional<AssociatedUserEntity> assigntooptional = associatedUserRepository
								.getByUser(assigntoid);

						if (!assigntooptional.isPresent()) {
							Optional<AssociatedUserEntity> associatedUserOptional = associatedUserRepository
									.getUserByHandlingOfficerIdAndInspectingOfficerId(
											responseDTO.getHandlingOfficerUserId(), assignToOfficerId.getId());
							if (!associatedUserOptional.isPresent()) {
								AssociatedUserEntity associatedUser = new AssociatedUserEntity();
								associatedUser.setAssociatedUserId(assignToOfficerId.getId());
								associatedUser.setAssociateduserName(assignToOfficerId.getFirstName());
								associatedUser.setUser(responseDTO.getHandlingOfficerUserId());
								associatedUser.setUserName(responseDTO.getHandlingfirstName());
								associatedUserRepository.save(associatedUser);
								finalList.add(associatedUser);
							}
						}
					}
				});
			}
		});
		if (finalList.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					"Already Have Handling Officer Can't Assign");
		}
		return Library.getSuccessfulResponse(finalList, ErrorCode.SUCCESS_RESPONSE.getCode(),
				ErrorMessages.RECORED_UPDATED);
	}

//	@Override
//	public GenericResponse getAssociatedUsers(Long userId) {
//		Set<Map<String, String>> userList = new HashSet<>();
//		
//		Optional<UserEntity> userEntity = userRepository.findById(userId);
//		if (!userEntity.isPresent()) {
//			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
//					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { Constant.USER_ID }));
//		}
//		if (userEntity.get().getRoles().stream()
//				.anyMatch(r -> Constant.HANDLING_OFFICER_ROLE.equals(r.getRoleCode()))) {
//			userList = userRepository.getUserByHandlingOfficerId(userId);
//		}else if(userEntity.get().getRoles().stream()
//				.anyMatch(r -> Constant.ASSIGN_TO_OFFICER_ROLE.equals(r.getRoleCode()))){
//			List<Long> handlingOfficerIds = associatedUserRepository.getUserByInspectingOfficerId(userId);
//			userList = userRepository.getUserByHandlingOfficerIds(handlingOfficerIds);
//		}
//		
//		if (CollectionUtils.isEmpty(userList)) {
//			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
//					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
//		}
//		return Library.getSuccessfulResponse(userList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
//				ErrorMessages.RECORED_FOUND);
//	}

	@Override
	public GenericResponse getAssociatedUsers(Long userId) {
		HashMap<String, String> map = new HashMap<String, String>();

		List<AssociatedUserEntity> assigngroupfrom = associatedUserRepository.findByAssociatedUserId(userId);

		if (assigngroupfrom.isEmpty()) {
			map.put("Role", "HANDLING_OFFICER");
		} else {
			map.put("Role", "ASSIGN_TO_OFFICER");
		}
//		List<AssociatedUserEntity> assignfrom = associatedUserRepository.findByUser(userId);
//		
//		if (assignfrom.isEmpty()) {
//			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
//					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { Constant.USER_ID }));
//		}	
//		else {
//			map.put(userId.toString(),"HANDLING_OFFICER");		
//		}

		return Library.getSuccessfulResponse(map, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

//	@Override
//	public GenericResponse getAssociatedUsersTest(Long userId) {
//		Set<Map<String, String>> userList = new HashSet<>();
//		
//		Optional<UserEntity> userEntity = userRepository.findById(userId);
//		if (!userEntity.isPresent()) {
//			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
//					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { Constant.USER_ID }));
//		}
//		if (userEntity.get().getRoles().stream()
//				.anyMatch(r -> Constant.HANDLING_OFFICER_ROLE.equals(r.getRoleCode()))) {
//			userList = userRepository.getUserByHandlingOfficerId(userId);
//		}else if(userEntity.get().getRoles().stream()
//				.anyMatch(r -> Constant.ASSIGN_TO_OFFICER_ROLE.equals(r.getRoleCode()))){
//			List<Long> handlingOfficerIds = associatedUserRepository.getUserByInspectingOfficerId(userId);
//			userList = userRepository.getUserByHandlingOfficerIds(handlingOfficerIds);
//		}
//		
//		if (CollectionUtils.isEmpty(userList)) {
//			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
//					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
//		}
//		return Library.getSuccessfulResponse(userList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
//				ErrorMessages.RECORED_FOUND);
//	}

	@Override
	public GenericResponse getuseriddistinct() {

		List<Object> depTypeEntity = associatedUserRepository.findAssociatedUser();

		// List<Long> depTypeEntity =
		// associatedUserRepository.findAllByOrderByModifiedDateDesc();

		if (depTypeEntity.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}

		List<AssociateresponseDTO> depResponseList = new ArrayList<>();
		return Library.getSuccessfulResponse(depTypeEntity, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}

	@Override
	public GenericResponse getAssociatedUsersdropdown(Long userId) {
		List<AssociatedUserEntity> associteusersdropdown = associatedUserRepository
				.findAllByUserOrderByModifiedDateDesc(userId);
		if (CollectionUtils.isEmpty(associteusersdropdown)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<AssociateresponseDTO> associateResponseList = associteusersdropdown.stream()
				.map(loginHistoryMapper::convertEntityToResponseDTOhandlingofficerdropdown)
				.collect(Collectors.toList());
		return Library.getSuccessfulResponse(associateResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getByUserid(Long userid) {
		List<WorkLocationEntity> DepList = workflowLocationRepository.findAllByUserId(userid);
		if (CollectionUtils.isEmpty(DepList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<WorkLocationResponseDTO> depResponseList = DepList.stream()
				.map(workLocationMapper::workLocationEnityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(depResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	@Transactional
	public GenericResponse resetPassworddetails(ResetPasswordDtodet requestDTO) {
		AuthenticationDTO authenticationDTO = SecurityUtils.findAuthenticationObject();
		if (Objects.isNull(authenticationDTO) || Objects.isNull(authenticationDTO.getUserId())
				|| CollectionUtils.isEmpty(authenticationDTO.getRoleCodes())) {
			log.error("authentication details missing");
			return Library.getFailResponseCode(ErrorCode.ACCESS_DENIED.getErrorCode(), ErrorMessages.ACCESS_DENIED);
		}
		Optional<UserEntity> entity = userRepository.findById(requestDTO.getUserId());
		if (!entity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { USERNAME }));
		}
		UserEntity userEntity = entity.get();
		requestDTO.setPassword("Test@123");
		userEntity.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
		userRepository.save(userEntity);
		return Library.getSuccessfulResponse(userEntity, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.CHANGE_PASSWORD);
	}

	@Override
	public GenericResponse fieldlogindetails(UserFieldDTO requestDto) {
		List<Fieldlogin> userloginList = null;
		Date fromDate = null;
		final Date toDate;
		if (!requestDto.getFromDate().isEmpty()) {
			try {
				fromDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.parse(requestDto.getFromDate() + " " + "00:00:00");
			} catch (ParseException e) {
				log.error("error occurred while parsing date : {}", e.getMessage());
				throw new InvalidDataValidation("Invalid date parameter passed");
			}

		}

		if (fromDate == null) {
			// userloginList = loginhistory.getCountByRoleid(requestDto.getRoleId());
		} else {
			// userloginList =
			// loginhistory.getCountByLoginTime(fromDate,requestDto.getRoleId());
		}
		if (CollectionUtils.isEmpty(userloginList)) {
			throw new RecordNotFoundException("No Record Found");
		}

		return Library.getSuccessfulResponse(userloginList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}

	@Override
	public GenericResponse getAllByRequestFilter(PaginationRequestDTO paginationDto,
			AuthenticationDTO authenticationDTO) {
		Pageable pageable = null;

		Page<Fieldlogin> userloginList = null;
		Long roleid = null;
		String createdDate = null;
		Date finalDate = null;
//		paginationDto.setSortField("modified_date");
//
//		if (StringUtils.isBlank(paginationDto.getSortOrder())) {
//			paginationDto.setSortOrder(DESC);
//		}
//		if (paginationDto.getSortOrder().equalsIgnoreCase(ASC)) {
//			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
//					Sort.by(Direction.ASC, paginationDto.getSortField()));
//		} else {
//			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
//					Sort.by(Direction.DESC, paginationDto.getSortField()));
//		}
		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("fromDate"))
					&& !paginationDto.getFilters().get("fromDate").toString().trim().isEmpty()) {
				try {
					createdDate = String.valueOf(paginationDto.getFilters().get("fromDate").toString());

					try {
						finalDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(createdDate + " " + "00:00:00");
					} catch (ParseException e) {
						log.error("error occurred while parsing date : {}", e.getMessage());
						throw new InvalidDataValidation("Invalid date parameter passed");
					}

				} catch (Exception e) {
					log.error("error occurred while parsing refertic_number :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}
		}
		if (Objects.nonNull(paginationDto.getFilters().get("roleId"))
				&& !paginationDto.getFilters().get("roleId").toString().trim().isEmpty()) {
			try {

				roleid = Long.valueOf(paginationDto.getFilters().get("roleId").toString());
			} catch (Exception e) {
				log.error("error occurred while parsing grievancecategory :: {}", e);
				return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
						ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
			}
		}

		String login = paginationDto.getFilters().get("login").toString();
		/// String notlogin=paginationDto.getFilters().get("notlogin").toString();
		if (login.equalsIgnoreCase("login")) {
			if (Objects.nonNull(finalDate) && Objects.nonNull(roleid)) {
				userloginList = loginhistory.getCountByLoginTime(finalDate, roleid, pageable);
			}
		}

		if (login.equalsIgnoreCase("notlogin")) {
			if (Objects.nonNull(finalDate) && Objects.nonNull(roleid)) {
				userloginList = loginhistory.getCountBynotequalLoginTime(finalDate, roleid, pageable);
			}
		}

		else if (Objects.isNull(finalDate) && Objects.nonNull(roleid)) {
			userloginList = loginhistory.getCountByRoleid(roleid, pageable);
		}

		Page<Fieldlogin> finalResponse = userloginList;
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}

//	@Override
//	public GenericResponse getAllByRequestFilter(PaginationRequestDTO requestData, AuthenticationDTO authenticationDTO) throws ParseException {
//		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
//		
//			List<LoginHistory> list = this.getRecordsByFilterDTO(requestData,authenticationDTO);
//			if (CollectionUtils.isEmpty(list)) {
//				throw new RecordNotFoundException("No Record Found");
//			}
//			List<CreateTicketResponseDto> dtoList = list.stream().map(createTicketMapper::convertEntityToResponseDTO)
//					.collect(Collectors.toList());
//			List<LoginHistory> finalResponse = list;
//			paginationResponseDTO.setContents(dtoList);
//			paginationResponseDTO.setNumberOfElements(Objects.nonNull(count) ? count.intValue() : null);
//			paginationResponseDTO.setTotalElements(count);
//			return Library.getSuccessfulResponse(paginationResponseDTO,
//					ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
//		
//	}
//	
//	public List<LoginHistory> getRecordsByFilterDTO(PaginationRequestDTO filterRequestDTO, AuthenticationDTO authenticationDTO) throws ParseException {
//		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//		CriteriaQuery<LoginHistory> cq = cb.createQuery(LoginHistory.class);
//		Root<LoginHistory> from = cq.from(LoginHistory.class);
//		List<Predicate> list = new ArrayList<>();
//		TypedQuery<LoginHistory> typedQuery = null;
//		//addCriteria(cb, list, filterRequestDTO, from,authenticationDTO);
//		//cq.where(cb.and(list.toArray(new Predicate[list.size()])));
//		
//		cq.where("select a.username ,a.first_name ,l.login_time,u.role_id,r.role_name  from   user a  \r\n" + 
//				"inner join login_history l on a.id=l.user_id \r\n" + 
//				"inner join user_role u on u.user_id =l.user_id \r\n" + 
//				"inner join role_master r on r.id=u.role_id \r\n" + 
//				"where l.login_time in (select max(login_time) \r\n" + 
//				"from login_history group by user_id) and u.role_id =3 and date(l.login_time)='2022-11-16' order by l.login_time  DESC");
//		cq.distinct(true);
//		if (StringUtils.isBlank(filterRequestDTO.getSortField())) {
//			filterRequestDTO.setSortField(MODIFIED_DATE);
//		}
//		if (StringUtils.isNotBlank(filterRequestDTO.getSortOrder())
//				&& filterRequestDTO.getSortOrder().equalsIgnoreCase(ASC)) {
//			cq.orderBy(cb.asc(from.get(filterRequestDTO.getSortField())));
//			
//		} else {
//			cq.orderBy(cb.desc(from.get(filterRequestDTO.getSortField())));
//
//		}
//		if (Objects.nonNull(filterRequestDTO.getPaginationSize()) && Objects.nonNull(filterRequestDTO.getPaginationSize())) {
//			typedQuery = entityManager.createQuery(cq)
//					.setFirstResult((filterRequestDTO.getPageNo() * filterRequestDTO.getPaginationSize()))
//					.setMaxResults(filterRequestDTO.getPaginationSize());
//		} else {
//			typedQuery = entityManager.createQuery(cq);
//		}
//
//		List<LoginHistory> data = typedQuery.getResultList();
//		if (CollectionUtils.isEmpty(data)) {
//			throw new RecordNotFoundException("No Record Found");
//		}
//		return data;
//	}
//	

	@Override
	public GenericResponse getUsersByRoleIdMultipledistrict(UserMultipleDistrictDTO requestDTO) {
		Optional<RoleMaster> roleMasterOptional = roleMasterRepository.findById(requestDTO.getRoleId());
		if (!roleMasterOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ROLE_ID }));
		}
		if (Boolean.FALSE.equals(roleMasterOptional.get().getStatus())) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INACTIVE_ID_PASSED.getMessage(new Object[] { ROLE_ID }));
		}
		List<Map<String, String>> userList = new ArrayList<>();
		if (requestDTO.getDistrictCode().isEmpty() || requestDTO.getDistrictCode() == null) {
			userList = userRepository.getUserByRole(requestDTO.getRoleId());
		} else {
			Long sofware = (long) 12;
			userList = userRepository.getUserByRoleAndMultiDistrict(requestDTO.getRoleId(),
					requestDTO.getDistrictCode(), sofware);

		}
		if (CollectionUtils.isEmpty(userList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(userList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	public GenericResponse getUsersByRoleIdshop() {
		Long Fieldengineer = (long) 3;
		Optional<RoleMaster> roleMasterOptional = roleMasterRepository.findById(Fieldengineer);
		if (!roleMasterOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ROLE_ID }));
		}
		if (Boolean.FALSE.equals(roleMasterOptional.get().getStatus())) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INACTIVE_ID_PASSED.getMessage(new Object[] { ROLE_ID }));
		}
		List<Map<String, String>> userList = new ArrayList<>();
		userList = userRepository.getUserByRoleIDshopcode(Fieldengineer);

		if (CollectionUtils.isEmpty(userList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(userList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	public GenericResponse getUserByassigntoidinfo(UserRequestConDTO requestDTO) {
		List<UserRequestConDTO> uselist = new ArrayList<UserRequestConDTO>();
		try {
			Optional<UserEntity> userentity = userRepository.findById(requestDTO.getAssigntoId());
			UserRequestConDTO user = new UserRequestConDTO();
			if (userentity.isPresent()) {
				user.setId(userentity.get().getId());
				user.setEmailId(userentity.get().getEmailId());
				user.setEmployeeId(userentity.get().getEmployeeId());
				user.setFirstName(userentity.get().getFirstName());
				user.setLastName(userentity.get().getLastName());
				user.setMiddleName(userentity.get().getMiddleName());
				user.setPhoneNumber(userentity.get().getPhoneNumber());
				user.setUsername(userentity.get().getUsername());
				user.setUsernameMiddlename(userentity.get().getUsername() + "-" + (userentity.get().getMiddleName()));
				uselist.add(user);
			} else {
				return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
						ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
			}

		} catch (Exception e) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), "");
		}
		return Library.getSuccessfulResponse(uselist, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	@Transactional
	public GenericResponse resetPasswordattempts(ResetPasswordDtodet requestDTO) {
		AuthenticationDTO authenticationDTO = SecurityUtils.findAuthenticationObject();
		if (Objects.isNull(authenticationDTO) || Objects.isNull(authenticationDTO.getUserId())
				|| CollectionUtils.isEmpty(authenticationDTO.getRoleCodes())) {
			log.error("authentication details missing");
			return Library.getFailResponseCode(ErrorCode.ACCESS_DENIED.getErrorCode(), ErrorMessages.ACCESS_DENIED);
		}
		Optional<UserEntity> entity = userRepository.findById(requestDTO.getUserId());
		if (!entity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { USERNAME }));
		}
		UserEntity userEntity = entity.get();
		userEntity.setAccountLocked(false);
		userRepository.save(userEntity);
		userattemptrepository.resetFailAttempts(requestDTO.getUserId());
		return Library.getSuccessfulResponse(userEntity, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				"Password Reset Attempts Successfully.");
	}

}
