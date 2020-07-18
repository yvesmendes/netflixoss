package com.thoughtmechanix.licenses.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.thoughtmechanix.licenses.clients.OrganizationFeignClient;
import com.thoughtmechanix.licenses.config.ServiceConfig;
import com.thoughtmechanix.licenses.model.License;
import com.thoughtmechanix.licenses.model.Organization;
import com.thoughtmechanix.licenses.repository.LicenseRepository;
import com.thoughtmechanix.licenses.repository.OrganizationRedisRepository;
import com.thoughtmechanix.licenses.utils.UserContext;
import com.thoughtmechanix.licenses.utils.UserContextHolder;

@Service
public class LicenseService {
	
	private static final Logger logger = LoggerFactory.getLogger(LicenseService.class);

	@Autowired
	private LicenseRepository licenseRepository;

	@Autowired
	private ServiceConfig config;

	@Autowired
	private OrganizationFeignClient organizationFeignClient;
	
	@Autowired
    private OrganizationRedisRepository orgRedisRepo;


    private Organization checkRedisCache(String organizationId) {
        try {
        	logger.info("Error encountered while trying to retrieve organization");
            return orgRedisRepo.findOrganization(organizationId);
        }
        catch (Exception ex){
            logger.error("Error encountered while trying to retrieve organization {} check Redis Cache.  Exception {}", organizationId, ex);
            return null;
        }
    }

    private void cacheOrganizationObject(Organization org) {
        try {
            orgRedisRepo.saveOrganization(org);
            logger.info("Error encountered while trying to retrieve organization");
        }catch (Exception ex){
            logger.error("Unable to cache organization {} in Redis. Exception {}", org.getId(), ex);
        }
    }

	private Organization retrieveOrgInfo(String organizationId, String clientType) {
		
		logger.debug("In Licensing Service.getOrganization: {}", UserContextHolder.getContext().getCorrelationId());
		logger.info("Error encountered while trying to retrieve organization");

        Organization org = checkRedisCache(organizationId);

        if (org!=null){
            logger.debug("I have successfully retrieved an organization {} from the redis cache: {}", organizationId, org);
            return org;
        }

        logger.debug("Unable to locate organization from the redis cache: {}.", organizationId);
        
        org = organizationFeignClient.getOrganization(organizationId);
        
        if (org!=null) {
            cacheOrganizationObject(org);
        }
        
		return org;
	}

	public License getLicense(String organizationId, String licenseId, String clientType) {
		License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
		logger.info("Error encountered while trying to retrieve organization");
		Organization org = retrieveOrgInfo(organizationId, clientType);

		return license.withOrganizationName(org.getName()).withContactName(org.getContactName())
				.withContactEmail(org.getContactEmail()).withContactPhone(org.getContactPhone())
				.withComment(config.getExampleProperty());
	}

	@HystrixCommand(fallbackMethod = "buildFallbackLicenseList", threadPoolKey = "licenseByOrgThreadPool", threadPoolProperties = {
			@HystrixProperty(name = "coreSize", value = "30"),
			@HystrixProperty(name = "maxQueueSize", value = "10") }, commandProperties = {
					@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
					@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "75"),
					@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "7000"),
					@HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "15000"),
					@HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "5") })
	public List<License> getLicensesByOrg(String organizationId) {
		return licenseRepository.findByOrganizationId(organizationId);
	}

	@SuppressWarnings("unused")
	private List<License> buildFallbackLicenseList(String organizationId) {
		List<License> fallbackList = new ArrayList<>();
		License license = new License().withId("0000000-00-00000").withOrganizationId(organizationId)
				.withProductName("Sorry no licensing information currently available");

		fallbackList.add(license);
		return fallbackList;
	}

	public void saveLicense(License license) {
		license.withId(UUID.randomUUID().toString());
		licenseRepository.save(license);
	}

	public void updateLicense(License license) {
		licenseRepository.save(license);
	}

	public void deleteLicense(License license) {
		licenseRepository.deleteById(license.getLicenseId());
	}
}
