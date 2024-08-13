package com.oasys.helpdesk.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.SLADashboardDTO;
//import com.oasys.helpdesk.dto.ZabbixIndividualServerDTO;
import com.oasys.helpdesk.dto.ZabbixServerDTO;
import com.oasys.helpdesk.dto.ZabbixresponseDTO;
import com.oasys.helpdesk.entity.ZabbixMasterServerEntity;
import com.oasys.helpdesk.entity.ZabbixServerEntity;
import com.oasys.helpdesk.repository.ZabbixMasterServerRepository;
import com.oasys.helpdesk.repository.ZabbixServerRepository;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.posasset.dto.UpexServerDetailsDTO;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class SlaUpexClientServiceImpl implements SlaUpexClientService {

	@Autowired
	SlaUpexClientService slaupexclientservice;

	@Value("${zabbix.username}")
	private String zabbixusername;

	@Value("${zabbix.password}")
	private String zabixpassword;

	@Value("${zabbix.url}")
	private String zabbixurl;

	@Autowired
	ZabbixMasterServerRepository zabbixmasterrepo;

	@Autowired
	ZabbixServerRepository zabbixserverrepo;

	private static String authenticate(String apiUrl, String username, String password) throws IOException {
		HttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(apiUrl);

		JSONObject authRequest = new JSONObject();
		authRequest.put("jsonrpc", "2.0");
		authRequest.put("method", "user.login");
		JSONObject params = new JSONObject();
		params.put("user", username);
		params.put("password", password);
		authRequest.put("params", params);
		authRequest.put("id", 1);

		httpPost.setEntity(new StringEntity(authRequest.toString()));
		httpPost.setHeader("Content-Type", "application/json");

		HttpResponse response = httpClient.execute(httpPost);
		JSONObject jsonResponse = new JSONObject(EntityUtils.toString(response.getEntity()));

		if (jsonResponse.has("result")) {
			return jsonResponse.getString("result");
		} else {
			return null;
		}
	}

	private static JSONObject getSLAMetrics(String apiUrl, String authToken, String dashboardId) throws IOException {
		HttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(apiUrl);

		JSONObject slaRequest = new JSONObject();
		slaRequest.put("jsonrpc", "2.0");
		slaRequest.put("method", "sla.getsli");
		JSONObject params = new JSONObject();
		params.put("serviceids", dashboardId);
		params.put("slaid", "1");
		params.put("periods", "1");
		slaRequest.put("params", params);
		slaRequest.put("auth", authToken);
		slaRequest.put("id", 2);

		httpPost.setEntity(new StringEntity(slaRequest.toString()));
		httpPost.setHeader("Content-Type", "application/json");

		HttpResponse response = httpClient.execute(httpPost);
		JSONObject jsonResponse = new JSONObject(EntityUtils.toString(response.getEntity()));

		if (jsonResponse.has("result")) {
			return jsonResponse.getJSONObject("result");
		} else {
			return null;
		}
	}

	public JSONObject UpexClient(SLADashboardDTO requestDTO) {
		// Zabbix API URL
		String zabbixApiUrl = zabbixurl;
		JSONObject json = null;
		// Zabbix API credentials
		String username = zabbixusername;
		String password = zabixpassword;

		// Dashboard or host ID for which you want to retrieve SLA metrics
		String serviceId = requestDTO.getServiceID();
		String serverName = requestDTO.getServerName();

		JSONObject slaMetrics = null;
		JSONObject responsesplit = null;
		try {
			// Zabbix API authentication
			String authToken = authenticate(zabbixApiUrl, username, password);

			if (authToken != null) {
				// Zabbix API call to get SLA metrics
				slaMetrics = getSLAMetrics(zabbixApiUrl, authToken, serviceId);

				if (slaMetrics != null) {
					System.out.println("SLA Metrics: " + slaMetrics.toString());
					return responsesplit(slaMetrics, serviceId, serverName);

				} else {
					log.info(":::::::::::Failed to retrieve SLA metrics:::");
					System.out.println("Failed to retrieve SLA metrics");
				}
			} else {
				System.out.println("Failed to authenticate");
				log.info(":::::::::::Failed to authenticate:::");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	public JSONObject responsesplit(JSONObject slaMetrics, String serviceId, String serverName) {

		ZabbixServerDTO serverobj = new ZabbixServerDTO();

		// Extract values
		JSONArray serviceIds = slaMetrics.getJSONArray("serviceids");
		JSONArray sliArray = slaMetrics.getJSONArray("sli");
		JSONObject sliObject = sliArray.getJSONArray(0).getJSONObject(0);
		int downtime = sliObject.getInt("downtime");
		int sli = sliObject.getInt("sli");
		int errorBudget = sliObject.getInt("error_budget");
		int uptime = sliObject.getInt("uptime");

		// Excluded downtimes is an array, so extract it accordingly
		JSONArray excludedDowntimes = sliObject.getJSONArray("excluded_downtimes");

		// Periods is an array, so extract it accordingly
		JSONArray periods = slaMetrics.getJSONArray("periods");
		JSONObject periodObject = periods.getJSONObject(0);
		// long periodFrom = periodObject.getLong("period_from");
		long periodTo = periodObject.getLong("period_to");

		// String uptimehoursminutessec = convertTimestampToDaysHoursMinutes(uptime);

		// Convert timestamp to LocalDateTime
		LocalDateTime dateTime = convertTimestampToDateTime(periodTo);

		// Format LocalDateTime to a custom format
		String formattedDateTime = formatDateTime(dateTime);
//		JSONObject serverdetails = new JSONObject();
//		serverdetails.put("Current_Date", formattedDateTime);
//		serverdetails.put("SLI", sli);
//		serverdetails.put("Uptime", uptimehoursminutessec);
//		serverdetails.put("Downtime", downtime);
//		serverdetails.put("Error budget", errorBudget + "s");
//		serverdetails.put("Excluded Downtimes", excludedDowntimes);
//		serverdetails.put("serviceId", serviceId);
//		serverdetails.put("serverName", serverName);
		serverobj.setServiceID(serviceId);
		serverobj.setTodaydate(formattedDateTime);
		serverobj.setSli(String.valueOf(sli));
		serverobj.setUpTime(String.valueOf(uptime));
		serverobj.setDownTime(String.valueOf(downtime));
		serverobj.setErrorBudget(String.valueOf(errorBudget));
		serverobj.setServerName(serverName);
		serverobj.setExcludedownTimes(String.valueOf(excludedDowntimes));
		serverobj.setStatus("1");

		int slo = (uptime + downtime - downtime) / (uptime + downtime);
		serverobj.setSlo(slo * 100);
		;
		try {
			ZabbixServerEntity zabbixentity = new ZabbixServerEntity();
			zabbixentity.setServiceID(serverobj.getServiceID());
			zabbixentity.setTodaydate(serverobj.getTodaydate());
			zabbixentity.setSli(serverobj.getSli());
			zabbixentity.setUpTime(serverobj.getUpTime());
			zabbixentity.setDownTime(serverobj.getDownTime());
			zabbixentity.setErrorBudget(serverobj.getErrorBudget());
			zabbixentity.setServerName(serverobj.getServerName());
			zabbixentity.setExcludedownTimes(serverobj.getExcludedownTimes());
			zabbixentity.setStatus(serverobj.getStatus());
			zabbixentity.setSlo(serverobj.getSlo().toString());
			zabbixserverrepo.save(zabbixentity);
		} catch (Exception e) {
			log.info("::::Zabbix server Data Insert Process::::::" + e);
		}
		return null;

	}

	// Function to convert timestamp to LocalDateTime
	private static LocalDateTime convertTimestampToDateTime(long timestampInSeconds) {
		return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestampInSeconds), ZoneOffset.UTC);
	}

	// Function to format LocalDateTime to a custom format
	private static String formatDateTime(LocalDateTime dateTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return dateTime.format(formatter);
	}

	// Function to convert timestamp to days, hours, and minutes
	private static String convertTimestampToDaysHoursMinutes(long timestampInSeconds) {
		long days = timestampInSeconds / (24 * 3600);
		long hours = (timestampInSeconds % (24 * 3600)) / 3600;
		long minutes = (timestampInSeconds % 3600) / 60;
		return String.format("%d days, %02d hours, %02d minutes", days, hours, minutes);
	}

	@Override
	public JSONObject upexciseApiPrimaryServer(SLADashboardDTO requestDTO) {

		// Zabbix API URL
		String zabbixApiUrl = zabbixurl;

		// Zabbix API credentials
		String username = zabbixusername;
		String password = zabixpassword;

		// Dashboard or host ID for which you want to retrieve SLA metrics
		String serviceId = requestDTO.getServiceID();
		String serverName = requestDTO.getServerName();
		JSONObject slaMetrics = null;
		try {
			// Zabbix API authentication
			String authToken = authenticate(zabbixApiUrl, username, password);

			if (authToken != null) {
				// Zabbix API call to get SLA metrics
				slaMetrics = getSLAMetrics(zabbixApiUrl, authToken, serviceId);

				if (slaMetrics != null) {
					System.out.println("SLA Metrics: " + slaMetrics.toString());
					return responsesplit(slaMetrics, serviceId, serverName);

				} else {
					log.info(":::::::::::Failed to retrieve SLA metrics:::");
					System.out.println("Failed to retrieve SLA metrics");
				}
			} else {
				log.info(":::::::::::Failed to authenticate:::");
				System.out.println("Failed to authenticate");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return slaMetrics;

	}

	@Override
	public JSONObject upexciseApiSecondaryServer(SLADashboardDTO requestDTO) {

		// Zabbix API URL
		String zabbixApiUrl = zabbixurl;

		// Zabbix API credentials
		String username = zabbixusername;
		String password = zabixpassword;

		// Dashboard or host ID for which you want to retrieve SLA metrics
		String serviceId = requestDTO.getServiceID();
		String serverName = requestDTO.getServerName();
		JSONObject slaMetrics = null;
		try {
			// Zabbix API authentication
			String authToken = authenticate(zabbixApiUrl, username, password);

			if (authToken != null) {
				// Zabbix API call to get SLA metrics
				slaMetrics = getSLAMetrics(zabbixApiUrl, authToken, serviceId);

				if (slaMetrics != null) {
					System.out.println("SLA Metrics: " + slaMetrics.toString());
					return responsesplit(slaMetrics, serviceId, serverName);
				} else {
					log.info(":::::::::::Failed to retrieve SLA metrics:::");
					System.out.println("Failed to retrieve SLA metrics");
				}
			} else {
				log.info(":::::::::::Failed to authenticate:::");
				System.out.println("Failed to authenticate");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return slaMetrics;

	}

	@Override
	public JSONObject upexciseUIServer(SLADashboardDTO requestDTO) {

		// Zabbix API URL
		String zabbixApiUrl = zabbixurl;

		// Zabbix API credentials
		String username = zabbixusername;
		String password = zabixpassword;

		// Dashboard or host ID for which you want to retrieve SLA metrics
		String serviceId = requestDTO.getServiceID();
		String serverName = requestDTO.getServerName();

		JSONObject slaMetrics = null;
		try {
			// Zabbix API authentication
			String authToken = authenticate(zabbixApiUrl, username, password);

			if (authToken != null) {
				// Zabbix API call to get SLA metrics
				slaMetrics = getSLAMetrics(zabbixApiUrl, authToken, serviceId);

				if (slaMetrics != null) {
					System.out.println("SLA Metrics: " + slaMetrics.toString());
					return responsesplit(slaMetrics, serviceId, serverName);
				} else {
					log.info(":::::::::::Failed to retrieve SLA metrics:::");
					System.out.println("Failed to retrieve SLA metrics");
				}
			} else {
				log.info(":::::::::::Failed to authenticate:::");
				System.out.println("Failed to authenticate");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return slaMetrics;
	}

	@Override
	public JSONObject upexciseHaproxyServer(SLADashboardDTO requestDTO) {

		// Zabbix API URL
		String zabbixApiUrl = zabbixurl;

		// Zabbix API credentials
		String username = zabbixusername;
		String password = zabixpassword;

		// Dashboard or host ID for which you want to retrieve SLA metrics
		String serviceId = requestDTO.getServiceID();
		String serverName = requestDTO.getServerName();
		JSONObject slaMetrics = null;
		try {
			// Zabbix API authentication
			String authToken = authenticate(zabbixApiUrl, username, password);

			if (authToken != null) {
				// Zabbix API call to get SLA metrics
				slaMetrics = getSLAMetrics(zabbixApiUrl, authToken, serviceId);

				if (slaMetrics != null) {
					System.out.println("SLA Metrics: " + slaMetrics.toString());
					return responsesplit(slaMetrics, serviceId, serverName);

				} else {
					log.info(":::::::::::Failed to retrieve SLA metrics:::");
					System.out.println("Failed to retrieve SLA metrics");
				}
			} else {
				log.info(":::::::::::Failed to authenticate:::");
				System.out.println("Failed to authenticate");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return slaMetrics;
	}

	@Override
	public JSONObject upexciseMasterDatabase(SLADashboardDTO requestDTO) {

		// Zabbix API URL
		String zabbixApiUrl = zabbixurl;

		// Zabbix API credentials
		String username = zabbixusername;
		String password = zabixpassword;

		// Dashboard or host ID for which you want to retrieve SLA metrics
		String serviceId = requestDTO.getServiceID();
		String serverName = requestDTO.getServerName();
		JSONObject slaMetrics = null;
		try {
			// Zabbix API authentication
			String authToken = authenticate(zabbixApiUrl, username, password);

			if (authToken != null) {
				// Zabbix API call to get SLA metrics
				slaMetrics = getSLAMetrics(zabbixApiUrl, authToken, serviceId);

				if (slaMetrics != null) {
					System.out.println("SLA Metrics: " + slaMetrics.toString());
					return responsesplit(slaMetrics, serviceId, serverName);
				} else {
					log.info(":::::::::::Failed to retrieve SLA metrics:::");
					System.out.println("Failed to retrieve SLA metrics");
				}
			} else {

				log.info(":::::::::::Failed to authenticate:::");
				System.out.println("Failed to authenticate");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return slaMetrics;
	}

	@Override
	public JSONObject upexciseSlaveDatabase(SLADashboardDTO requestDTO) {

		// Zabbix API URL
		String zabbixApiUrl = zabbixurl;

		// Zabbix API credentials
		String username = zabbixusername;
		String password = zabixpassword;

		// Dashboard or host ID for which you want to retrieve SLA metrics
		String serviceId = requestDTO.getServiceID();
		String serverName = requestDTO.getServerName();
		JSONObject slaMetrics = null;
		try {
			// Zabbix API authentication
			String authToken = authenticate(zabbixApiUrl, username, password);

			if (authToken != null) {
				// Zabbix API call to get SLA metrics
				slaMetrics = getSLAMetrics(zabbixApiUrl, authToken, serviceId);

				if (slaMetrics != null) {
					System.out.println("SLA Metrics: " + slaMetrics.toString());
					return responsesplit(slaMetrics, serviceId, serverName);

				} else {
					log.info(":::::::::::Failed to retrieve SLA metrics:::");
					System.out.println("Failed to retrieve SLA metrics");
				}
			} else {
				log.info(":::::::::::Failed to authenticate:::");
				System.out.println("Failed to authenticate");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return slaMetrics;

	}

	@Override
	public JSONObject archivedtabase(SLADashboardDTO requestDTO) {
		// Zabbix API URL
		String zabbixApiUrl = zabbixurl;

		// Zabbix API credentials
		String username = zabbixusername;
		String password = zabixpassword;

		// Dashboard or host ID for which you want to retrieve SLA metrics
		String serviceId = requestDTO.getServiceID();
		String serverName = requestDTO.getServerName();

		JSONObject slaMetrics = null;
		try {
			// Zabbix API authentication
			String authToken = authenticate(zabbixApiUrl, username, password);

			if (authToken != null) {
				// Zabbix API call to get SLA metrics
				slaMetrics = getSLAMetrics(zabbixApiUrl, authToken, serviceId);

				if (slaMetrics != null) {
					System.out.println("SLA Metrics: " + slaMetrics.toString());
					return responsesplit(slaMetrics, serviceId, serverName);

				} else {
					log.info(":::::::::::Failed to retrieve SLA metrics:::");
					System.out.println("Failed to retrieve SLA metrics");
				}
			} else {
				log.info(":::::::::::Failed to authenticate:::");
				System.out.println("Failed to authenticate");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return slaMetrics;

	}

	@Override
	public JSONObject cms(SLADashboardDTO requestDTO) {
		// Zabbix API URL
		String zabbixApiUrl = zabbixurl;

		// Zabbix API credentials
		String username = zabbixusername;
		String password = zabixpassword;

		// Dashboard or host ID for which you want to retrieve SLA metrics
		String serviceId = requestDTO.getServiceID();
		String serverName = requestDTO.getServerName();
		JSONObject slaMetrics = null;
		try {
			// Zabbix API authentication
			String authToken = authenticate(zabbixApiUrl, username, password);

			if (authToken != null) {
				// Zabbix API call to get SLA metrics
				slaMetrics = getSLAMetrics(zabbixApiUrl, authToken, serviceId);

				if (slaMetrics != null) {
					System.out.println("SLA Metrics: " + slaMetrics.toString());
					return responsesplit(slaMetrics, serviceId, serverName);
				} else {
					System.out.println("Failed to retrieve SLA metrics");
					log.info(":::::::::::Failed to retrieve SLA metrics:::");

				}
			} else {
				System.out.println("Failed to authenticate");
				log.info(":::::::::::Failed to authenticate:::");

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return slaMetrics;

	}

	@Override
	public JSONObject jenkins(SLADashboardDTO requestDTO) {
		// Zabbix API URL
		String zabbixApiUrl = zabbixurl;

		// Zabbix API credentials
		String username = zabbixusername;
		String password = zabixpassword;

		// Dashboard or host ID for which you want to retrieve SLA metrics
		String serviceId = requestDTO.getServiceID();
		String serverName = requestDTO.getServerName();

		JSONObject slaMetrics = null;
		try {
			// Zabbix API authentication
			String authToken = authenticate(zabbixApiUrl, username, password);

			if (authToken != null) {
				// Zabbix API call to get SLA metrics
				slaMetrics = getSLAMetrics(zabbixApiUrl, authToken, serviceId);

				if (slaMetrics != null) {
					System.out.println("SLA Metrics: " + slaMetrics.toString());
					return responsesplit(slaMetrics, serviceId, serverName);
				} else {
					System.out.println("Failed to retrieve SLA metrics");
					log.info(":::::::::::Failed to retrieve SLA metrics:::");

				}
			} else {
				System.out.println("Failed to authenticate");
				log.info(":::::::::::Failed to authenticate:::");

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return slaMetrics;

	}

	@Override
	public JSONObject mdmserver(SLADashboardDTO requestDTO) {
		// Zabbix API URL
		String zabbixApiUrl = zabbixurl;

		// Zabbix API credentials
		String username = zabbixusername;
		String password = zabixpassword;

		// Dashboard or host ID for which you want to retrieve SLA metrics
		String serviceId = requestDTO.getServiceID();
		String serverName = requestDTO.getServerName();

		JSONObject slaMetrics = null;
		try {
			// Zabbix API authentication
			String authToken = authenticate(zabbixApiUrl, username, password);

			if (authToken != null) {
				// Zabbix API call to get SLA metrics
				slaMetrics = getSLAMetrics(zabbixApiUrl, authToken, serviceId);

				if (slaMetrics != null) {
					System.out.println("SLA Metrics: " + slaMetrics.toString());
					return responsesplit(slaMetrics, serviceId, serverName);
				} else {
					System.out.println("Failed to retrieve SLA metrics");
					log.info(":::::::::::Failed to retrieve SLA metrics:::");

				}
			} else {
				System.out.println("Failed to authenticate");
				log.info(":::::::::::Failed to authenticate:::");

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return slaMetrics;

	}

	@Override
	public JSONObject misAPI(SLADashboardDTO requestDTO) {
		// Zabbix API URL
		String zabbixApiUrl = zabbixurl;

		// Zabbix API credentials
		String username = zabbixusername;
		String password = zabixpassword;

		// Dashboard or host ID for which you want to retrieve SLA metrics
		String serviceId = requestDTO.getServiceID();
		String serverName = requestDTO.getServerName();

		JSONObject slaMetrics = null;
		try {
			// Zabbix API authentication
			String authToken = authenticate(zabbixApiUrl, username, password);

			if (authToken != null) {
				// Zabbix API call to get SLA metrics
				slaMetrics = getSLAMetrics(zabbixApiUrl, authToken, serviceId);

				if (slaMetrics != null) {
					System.out.println("SLA Metrics: " + slaMetrics.toString());
					return responsesplit(slaMetrics, serviceId, serverName);

				} else {
					System.out.println("Failed to retrieve SLA metrics");
					log.info(":::::::::::Failed to retrieve SLA metrics:::");

				}
			} else {
				System.out.println("Failed to authenticate");
				log.info(":::::::::::Failed to authenticate:::");

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return slaMetrics;

	}

	@Override
	public JSONObject qrcode(SLADashboardDTO requestDTO) {
		// Zabbix API URL
		String zabbixApiUrl = zabbixurl;

		// Zabbix API credentials
		String username = zabbixusername;
		String password = zabixpassword;

		// Dashboard or host ID for which you want to retrieve SLA metrics
		String serviceId = requestDTO.getServiceID();
		String serverName = requestDTO.getServerName();

		JSONObject slaMetrics = null;
		try {
			// Zabbix API authentication
			String authToken = authenticate(zabbixApiUrl, username, password);

			if (authToken != null) {
				// Zabbix API call to get SLA metrics
				slaMetrics = getSLAMetrics(zabbixApiUrl, authToken, serviceId);

				if (slaMetrics != null) {
					System.out.println("SLA Metrics: " + slaMetrics.toString());
					return responsesplit(slaMetrics, serviceId, serverName);

				} else {
					System.out.println("Failed to retrieve SLA metrics");
					log.info(":::::::::::Failed to retrieve SLA metrics:::");

				}
			} else {
				System.out.println("Failed to authenticate");
				log.info(":::::::::::Failed to authenticate:::");

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return slaMetrics;

	}

	@Override
	public JSONObject wso2(SLADashboardDTO requestDTO) {
		// Zabbix API URL
		String zabbixApiUrl = zabbixurl;

		// Zabbix API credentials
		String username = zabbixusername;
		String password = zabixpassword;

		// Dashboard or host ID for which you want to retrieve SLA metrics
		String serviceId = requestDTO.getServiceID();
		String serverName = requestDTO.getServerName();

		JSONObject slaMetrics = null;
		try {
			// Zabbix API authentication
			String authToken = authenticate(zabbixApiUrl, username, password);

			if (authToken != null) {
				// Zabbix API call to get SLA metrics
				slaMetrics = getSLAMetrics(zabbixApiUrl, authToken, serviceId);

				if (slaMetrics != null) {
					System.out.println("SLA Metrics: " + slaMetrics.toString());
					return responsesplit(slaMetrics, serviceId, serverName);
				} else {
					System.out.println("Failed to retrieve SLA metrics");
					log.info(":::::::::::Failed to retrieve SLA metrics:::");

				}
			} else {
				System.out.println("Failed to authenticate");
				log.info(":::::::::::Failed to authenticate:::");

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return slaMetrics;

	}
//	@Scheduled(fixedRate = 10000)
//	//@Scheduled(cron = "${cron.expression.zabbixserver}")
//	@Transactional
//	public void zabbixserverdata() {
//	try {	
//	SLADashboardDTO requestDTO =new SLADashboardDTO();
//	List<ZabbixMasterServerEntity> serverlist=zabbixmasterrepo.findAllByOrderByCreatedDateAsc();	
//	serverlist.stream().forEach(obj->{
//	String serviceid=obj.getServiceId().toString();
//	String servername=obj.getServerName().toString();
//	requestDTO.setServerName(servername);
//	requestDTO.setServiceID(serviceid);
//		switch (serviceid) {
//		case "2":
//			UpexClient(requestDTO);
//			System.out.println("::::::::::  2  ::::::::::::");
//			break;
//
//		case "3":
//		upexciseApiPrimaryServer(requestDTO);
//		System.out.println("::::::::::  3  ::::::::::::");
//			break;
//		case "4":
//			upexciseApiSecondaryServer(requestDTO);
//			System.out.println("::::::::::  4  ::::::::::::");
//			break;
//		case "5":
//			upexciseUIServer(requestDTO);
//			System.out.println("::::::::::  5  ::::::::::::");
//			break;
//		case "6":
//			upexciseHaproxyServer(requestDTO);
//			System.out.println("::::::::::  6  ::::::::::::");
//			break;
//		case "7":
//			upexciseMasterDatabase(requestDTO);
//			System.out.println("::::::::::  7  ::::::::::::");
//			break;
//		case "8":
//			upexciseSlaveDatabase(requestDTO);
//			System.out.println("::::::::::  8  ::::::::::::");
//			break;
//		case "9":
//			archivedtabase(requestDTO);
//			System.out.println("::::::::::  9 ::::::::::::");
//			break;
//		case "10":
//			cms(requestDTO);
//			System.out.println("::::::::::  10  ::::::::::::");
//			break;
//		case "11":
//			jenkins(requestDTO);
//			System.out.println("::::::::::  11  ::::::::::::");
//			break;
//		case "12":
//			mdmserver(requestDTO);
//			System.out.println("::::::::::  12  ::::::::::::");
//			break;
//		case "13":
//			misAPI(requestDTO);
//			System.out.println("::::::::::  13  ::::::::::::");
//			break;
//		case "14":
//			qrcode(requestDTO);
//			System.out.println("::::::::::  14  ::::::::::::");
//			break;
//		case "15":
//			wso2(requestDTO);
//			System.out.println("::::::::::  15  ::::::::::::");
//			break;
//       }
//	});
//	
//	}
//	catch(Exception e) {
//	//log.info(":::::Zabbixserver Data Scheduler Call:::::::" + e);	
//	}
//	}
//	

	@Override
	public GenericResponse UpexClientServerData(SLADashboardDTO requestDTO) {
		Pageable pageable = (Pageable) PageRequest.of(0, 1); // Limiting to 1 result
		List<ZabbixresponseDTO> clientserver = zabbixserverrepo.getByClientProdServer(requestDTO.getServiceID(),
				requestDTO.getServerName(), pageable);
		if (clientserver.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(clientserver, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse upexciseApiPrimaryServerData(SLADashboardDTO requestDTO) {
		Pageable pageable = (Pageable) PageRequest.of(0, 1); // Limiting to 1 result
		List<ZabbixresponseDTO> primaryserver = zabbixserverrepo.getByPrimaryServer(requestDTO.getServiceID(),
				requestDTO.getServerName(), pageable);
		if (primaryserver.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(primaryserver, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse upexciseHaproxyServerData(SLADashboardDTO requestDTO) {
		Pageable pageable = (Pageable) PageRequest.of(0, 1); // Limiting to 1 result
		List<ZabbixresponseDTO> haproxyserver = zabbixserverrepo.getByHaproxyServer(requestDTO.getServiceID(),
				requestDTO.getServerName(), pageable);
		if (haproxyserver.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(haproxyserver, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse upexciseMasterServerData(SLADashboardDTO requestDTO) {
		Pageable pageable = (Pageable) PageRequest.of(0, 1); // Limiting to 1 result
		List<ZabbixresponseDTO> masterserver = zabbixserverrepo.getByMasterserverData(requestDTO.getServiceID(),
				requestDTO.getServerName(), pageable);
		if (masterserver.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(masterserver, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse upexciseSlaveDatabaseData(SLADashboardDTO requestDTO) {
		Pageable pageable = (Pageable) PageRequest.of(0, 1); // Limiting to 1 result
		List<ZabbixresponseDTO> slavedatabase = zabbixserverrepo.getBySlaveDatabaseData(requestDTO.getServiceID(),
				requestDTO.getServerName(), pageable);
		if (slavedatabase.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(slavedatabase, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse upexciseApiSecondaryServerData(SLADashboardDTO requestDTO) {
		Pageable pageable = (Pageable) PageRequest.of(0, 1); // Limiting to 1 result
		List<ZabbixresponseDTO> apisecondary = zabbixserverrepo.getBysecondaryData(requestDTO.getServiceID(),
				requestDTO.getServerName(), pageable);
		if (apisecondary.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(apisecondary, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse upexciseUIServerData(SLADashboardDTO requestDTO) {
		Pageable pageable = (Pageable) PageRequest.of(0, 1); // Limiting to 1 result
		List<ZabbixresponseDTO> uiserver = zabbixserverrepo.getByuiserverData(requestDTO.getServiceID(),
				requestDTO.getServerName(), pageable);
		if (uiserver.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(uiserver, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse upexcisearchivedtabaseData(SLADashboardDTO requestDTO) {
		Pageable pageable = (Pageable) PageRequest.of(0, 1); // Limiting to 1 result
		List<ZabbixresponseDTO> archiveserver = zabbixserverrepo.getByArchiveData(requestDTO.getServiceID(),
				requestDTO.getServerName(), pageable);
		if (archiveserver.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(archiveserver, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse upexcmstabaseData(SLADashboardDTO requestDTO) {
		Pageable pageable = (Pageable) PageRequest.of(0, 1); // Limiting to 1 result
		List<ZabbixresponseDTO> cmsserver = zabbixserverrepo.getByCmsData(requestDTO.getServiceID(),
				requestDTO.getServerName(), pageable);
		if (cmsserver.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(cmsserver, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse upexjenkinsData(SLADashboardDTO requestDTO) {
		Pageable pageable = (Pageable) PageRequest.of(0, 1); // Limiting to 1 result
		List<ZabbixresponseDTO> jenkinsserver = zabbixserverrepo.getByJenkins(requestDTO.getServiceID(),
				requestDTO.getServerName(), pageable);
		if (jenkinsserver.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(jenkinsserver, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse upexmdmserverData(SLADashboardDTO requestDTO) {
		Pageable pageable = (Pageable) PageRequest.of(0, 1); // Limiting to 1 result
		List<ZabbixresponseDTO> mdmserver = zabbixserverrepo.getBymdm(requestDTO.getServiceID(),
				requestDTO.getServerName(), pageable);
		if (mdmserver.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(mdmserver, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse upexqrerverData(SLADashboardDTO requestDTO) {
		Pageable pageable = (Pageable) PageRequest.of(0, 1); // Limiting to 1 result
		List<ZabbixresponseDTO> qrserver = zabbixserverrepo.getByQr(requestDTO.getServiceID(),
				requestDTO.getServerName(), pageable);
		if (qrserver.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(qrserver, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse wso2serverData(SLADashboardDTO requestDTO) {
		Pageable pageable = (Pageable) PageRequest.of(0, 1); // Limiting to 1 result
		List<ZabbixresponseDTO> wso2server = zabbixserverrepo.getBywso2(requestDTO.getServiceID(),
				requestDTO.getServerName(), pageable);
		if (wso2server.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(wso2server, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse misData(SLADashboardDTO requestDTO) {
		Pageable pageable = (Pageable) PageRequest.of(0, 1); // Limiting to 1 result
		List<ZabbixresponseDTO> misserver = zabbixserverrepo.getBymis(requestDTO.getServiceID(),
				requestDTO.getServerName(), pageable);
		if (misserver.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(misserver, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse AllServerData(SLADashboardDTO requestDTO) {
//		ArrayList<ZabbixIndividualServerDTO> serverlist = new ArrayList<ZabbixIndividualServerDTO>();
//		List<ZabbixresponseDTO> allserver =zabbixserverrepo.getByAllServer(requestDTO.getServicsIds());
////		allserver.stream.foreach(obj->{
////			
////		});
////		
//		
//		
//		if (allserver.isEmpty()) {
//			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
//					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
//		}
		return Library.getSuccessfulResponse(null, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	public GenericResponse upexServerDetails(SLADashboardDTO requestDto) {

		String fromDateStr = requestDto.getFromDate(); // Get start date from request DTO
		String toDateStr = requestDto.getToDate(); // Get end date from request DTO

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Date format for parsing

		Date fromDate;
		Date toDate;
		try {
			fromDate = sdf.parse(fromDateStr); // Parse start date
			toDate = sdf.parse(toDateStr); // Parse end date
		} catch (ParseException e) {
			log.error("Error occurred while parsing date: {}", e.getMessage());
			throw new InvalidDataValidation("Invalid date parameter passed"); // Handle parsing error
		}

		try {
			List<UpexServerDetailsDTO> entitysummary = zabbixserverrepo.upexServerDetails(fromDate, toDate);
			if (entitysummary.isEmpty()) {
				return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
						ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
			}

			else {
				return Library.getSuccessfulResponse(entitysummary, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
						ErrorMessages.RECORED_FOUND);
			}

		} catch (RecordNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"An error occurred while retrieving Upex Server Details.", e);
		}
	}
}
