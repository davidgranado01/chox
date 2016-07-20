package idas.chox.keoghs;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.namespace.QName;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.headers.Header;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxb.JAXBDataBinding;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.keoghs.ADAPublicServices.*;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Comment;
import idas.chox.core.model.KeoghsRequest;
import idas.chox.core.model.KeoghsRequestScoreMessage;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.KeoghsRequestService;
import java.util.Date;

/**
 *
 * @author John
 */
public class Keoghs {

    private static final Logger LOG = LoggerFactory.getLogger(Keoghs.class);

//    private static final String KEOGHS_TOKEN = "5DF5864C-5E79-481F-9188-38BF4013D84C";
    public static final int ERROR = -1;
    public static final int NOT_REQUESTED = 0;
    public static final int QUEUED = 1;
    public static final int PENDING = 2;
    public static final int AVAILABLE = 3;

    private KeoghsRequestService keoghsRequestService;
    private ClaimService claimService;
    private String keoghsToken;
    private int maxRequests;
    private int keoghsTimeout;
    private boolean keoghsLogSoap;
    private boolean useDebugEndpoint;

    public void setMaxRequests(int maxRequests) {
        this.maxRequests = maxRequests;
    }
    
    public void setKeoghsToken(String keoghsToken) {
        this.keoghsToken = keoghsToken;
    }

    public void setKeoghsTimeout(int keoghsTimeout) {
        this.keoghsTimeout = keoghsTimeout;
    }

    public void setKeoghsLogSoap(boolean keoghsLogSoap) {
        this.keoghsLogSoap = keoghsLogSoap;
    }

    public void setUseDebugEndpoint(boolean useDebugEndpoint) {
        this.useDebugEndpoint = useDebugEndpoint;
    }

    public void setKeoghsRequestService(KeoghsRequestService keoghsRequestService) {
        this.keoghsRequestService = keoghsRequestService;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }
    
    public boolean queueAndSubmit(Claim claim, String checkType) {
        return queueAndSubmit(claim, checkType, useDebugEndpoint);
    }

    public void check() {
        try {
            check(useDebugEndpoint);
        } catch (JAXBException ex) {
            LOG.error("Error checking status of keoghs requests (with debug={}): {}", useDebugEndpoint, ex.getMessage(), ex);
        }
    }

    public KeoghsRequest queue(Claim claim, String checkType) {
        if (claim.getFraudCheckStatus() == QUEUED || claim.getFraudCheckStatus() == PENDING) {
            LOG.error("Claim '{}' already sent to Keoghs and pending a response: {}", claim.getChoReference(), claim.getFraudCheckStatus());
            return null;
        }

        KeoghsRequest keoghsRequest = new KeoghsRequest();

        try {
            // Count exisiting calls for same claim
            int requestCount = claim.getId() == null ? 0 : keoghsRequestService.getKeoghsRequestByClaim(claim).size();

            String clientBatchReference = claim.getId() != null ? claim.getId().toString().concat("_" + requestCount)
                    : claim.getChorganisation().getId().toString().concat("_" + claim.getChoReference().replaceAll("\\s+", "")).concat("_" + requestCount);

            keoghsRequest.setClaim(claim);
            keoghsRequest.setClientBatchReference(clientBatchReference);
            keoghsRequest.setCheckType(checkType);

            // Add request to Keoghs requests table
            claim.setKeoghsRequest(keoghsRequest);
            claim.setFraudCheckStatus(QUEUED);
            claim.setFraudResultAcknowledged(false);
            LOG.debug("Fraud check status for claim '{}' [id={}] set to QUEUED", claim.getChoReference(), claim.getId());
            keoghsRequestService.saveKeoghsRequest(keoghsRequest);
//            claimService.save(claim);

        } catch (Exception ex) {
            LOG.error("Exception thrown calling Keoghs: {}\n", ex.getMessage(), ex);
            return null;
        } finally {
            // Clean-up resources?
        }

        return keoghsRequest;
    }

    public void submit() {
        boolean result;
        List<KeoghsRequest> keoghsRequests = keoghsRequestService.getQueuedRequests(maxRequests);
        LOG.debug("Found {} queued Keoghs requests.", keoghsRequests.size());
        int requestsSent = 0;
        
        for (KeoghsRequest originalRequest : keoghsRequests) {
            LOG.debug("Processing Keoghs request with reference='{}' ({},{})",
                    new Object[]{originalRequest.getClientBatchReference(),
                        originalRequest.getBatchStatus(), originalRequest.getClaimStatus()});
            result = submit(originalRequest, useDebugEndpoint);
            LOG.debug("Submit claim '{}' status: {}", originalRequest.getClientBatchReference(), result);
            requestsSent++;
            if (maxRequests != -1 && requestsSent >= maxRequests) {
                return;
            }
        }

        return;
    }

    private IADAPublicServices getADAServices() throws JAXBException {
            ADAPublicServices service = new ADAPublicServices();
            IADAPublicServices port = service.getBasicHttpBindingIADAPublicServices();

            initADAServices(ClientProxy.getClient(port));
            
            return port;
    }

    private IADAPublicServicesDebug getADAServicesDebug() throws JAXBException {
            ADAPublicServicesDebug serviceDebug = new ADAPublicServicesDebug();
            IADAPublicServicesDebug portDebug = serviceDebug.getBasicHttpBindingIADAPublicServicesDebug();

            initADAServices(ClientProxy.getClient(portDebug));

            return portDebug;
    }

    private void initADAServices(Client client) throws JAXBException {
             // Add  logging interceptors
            if (keoghsLogSoap) {
                client.getInInterceptors().add(new LoggingInInterceptor());
                client.getOutInterceptors().add(new LoggingOutInterceptor());
            }
            
            // Increase timeouts to 3 mins
            HTTPConduit http = (HTTPConduit) client.getConduit();
            HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
            httpClientPolicy.setConnectionTimeout(new Long(keoghsTimeout * 60 * 1000));
            httpClientPolicy.setReceiveTimeout(new Long(keoghsTimeout * 60 * 1000));
            http.setClient(httpClientPolicy);
            
            // Add SOAP Headers to web service request
            List<Header> headersList = new ArrayList<>();
            Header tokenHeader = new Header(new QName("http://www.keoghs.co.uk/ADA", "token"), keoghsToken, new JAXBDataBinding(String.class)); 
            headersList.add(tokenHeader);
            client.getRequestContext().put(Header.HEADER_LIST, headersList);
   }
 

    private boolean submit(KeoghsRequest keoghsRequest, boolean debug) {

        try {
            Claim claim = keoghsRequest.getClaim();
            if (LOG.isDebugEnabled()) {
                if (claim.getId() == null) {
                    LOG.debug("Claim '{}' has no Id set", claim.getChoReference());
                } else {
                    LOG.debug("Claim '{}' has Id set of {}", claim.getChoReference(), claim.getId());
                }
            }
            com.keoghs.ADAPublicServices.Claim keoghsClaim = getKeoghsClaim(claim);
            keoghsClaim.setClaimNumber(keoghsRequest.getClientBatchReference());
            keoghsRequest.setClaimStatus(0);

            // Set-up the request
            ClaimBatch claimBatch = new ClaimBatch();
            ArrayOfClaim claimArray = new ArrayOfClaim();
            claimArray.getClaims().add(keoghsClaim);
            claimBatch.setClaims(claimArray);

            BatchClaimRequest request = new BatchClaimRequest();
            request.setBatch(claimBatch);
            request.setClientBatchReference(keoghsRequest.getClientBatchReference());

            if (debug) {
                IADAPublicServicesDebug adaServicesDebug = getADAServicesDebug();

                // call web-service...
                LOG.debug("Claim (id={}) sending to Keoghs:\n{}", claim.getId(), request.toString());
                ProcessClaimBatchResponse response = adaServicesDebug.submitMotorClaim(request);
                LOG.debug("Respone is: {}", response.getResults().getMessage().getText());
                keoghsRequest.setResponseMessageDebug(response.getResults().getMessage().getText());
            } else {
                IADAPublicServices adaServices = getADAServices();
                adaServices.submitMotorClaim(request);
            }
            // Update Request
            keoghsRequest.setLastModifiedDate(new Date());
            keoghsRequestService.saveKeoghsRequest(keoghsRequest);
            claim.setFraudCheckStatus(PENDING);
            claim.setFraudResultAcknowledged(false);
            LOG.debug("Fraud check status for claim '{}' [id={}] set to PENDING", claim.getChoReference(), claim.getId());
//            claimService.save(claim);
        } catch (Exception ex) {
            LOG.error("Error submitting claim to Keoghs: {}\n", ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    private boolean queueAndSubmit(Claim claim, String checkType, boolean debug) {
        KeoghsRequest keoghsRequest = queue(claim, checkType);

        if (keoghsRequest == null) {
            return false;
        }

        return submit(keoghsRequest, debug);
    }

    private void check(boolean debug) throws JAXBException {

        // Process all open requests
        List<KeoghsRequest> keoghsRequests = keoghsRequestService.getPendingRequests();
        LOG.debug("Found {} pending Keoghs requests.", keoghsRequests.size());

        BatchClaimScoreRequest request;
        BatchClaimScoreResponse response;
        for (KeoghsRequest originalRequest : keoghsRequests) {
            LOG.debug("Processing Keoghs request with reference='{}' ({},{})",
                    new Object[]{originalRequest.getClientBatchReference(),
                        originalRequest.getBatchStatus(), originalRequest.getClaimStatus()});
            request = new BatchClaimScoreRequest();
            request.setClientBatchReference(originalRequest.getClientBatchReference());
            try {
                if (debug) {
                    IADAPublicServicesDebug adaServicesDebug = getADAServicesDebug();
                    response = adaServicesDebug.getMotorClaimScore(request);
                } else {
                    IADAPublicServices adaServices = getADAServices();
                    response = adaServices.getMotorClaimScore(request);
                }
            } catch (Exception ex) {
                LOG.error("Exception thrown getting Motor Claim Score for client batch reference '{}': {}", request.getClientBatchReference(), ex.getMessage(), ex);
                continue;
            }
            
            originalRequest.setResultStatus(response.getResultStatus().toString());
            List<ClaimScoreResponse> responseList = response.getClaimStatusAndScoreResponses().getClaimScoreResponses();
            LOG.debug("Response list size is {}", responseList.size());
            // There should only be one?
            if (responseList.isEmpty()) {
                LOG.error("No ClaimScoreResponses received for clientBatchReference '{}'", originalRequest.getClientBatchReference());
                continue;
            } else if (responseList.size() != 1) {
                LOG.error("Multiple ClaimScoreResponses received for clientBatchReference '{}': {}", originalRequest.getClientBatchReference(), responseList.size());
            }
            ClaimScoreResponse claimScoreResponse = responseList.get(0);
            originalRequest.setClaimStatus(claimScoreResponse.getStatus().getClaimStatus());
            originalRequest.setBatchStatus(claimScoreResponse.getStatus().getBatchStatus());
            Claim claim = originalRequest.getClaim();
            LOG.debug("Response result status={}, claimStatus={}, batchStatus={} for batch reference {}",
                    new Object[]{response.getResultStatus(), claimScoreResponse.getStatus().getClaimStatus(),
                        claimScoreResponse.getStatus().getBatchStatus(), originalRequest.getClientBatchReference()});
            switch (response.getResultStatus()) {
                case ERROR:
                    claim.setFraudCheckStatus(ERROR);
                    LOG.error("Error response received for client batch reference '{}'", originalRequest.getClientBatchReference());
                    break;
                case IN_PROGRESS:
                    LOG.debug("Pending response received for client batch reference '{}'", originalRequest.getClientBatchReference());
                    claim.setFraudCheckStatus(PENDING);
                    break;
                case SUCCESS:
                    LOG.debug("Success response received for client batch reference '{}'", originalRequest.getClientBatchReference());
                    claim.setFraudCheckStatus(AVAILABLE);
                    originalRequest.setRagResult(claimScoreResponse.getResults().getRagResult().toString());
                    originalRequest.setTotalScore(claimScoreResponse.getResults().getTotalScore());
                    claim.addComment(Comment.newComment(1, "A " + originalRequest.getCheckType() + " Fraud Check was run with a Fraud Score of "
                                + claimScoreResponse.getResults().getTotalScore() + "."));
                    List<ClaimScoreMessage> messages = claimScoreResponse.getResults().getScoreMessages().getClaimScoreMessages();
                    StringBuilder sb = new StringBuilder();
                    for (ClaimScoreMessage message : messages) {
                        KeoghsRequestScoreMessage scoreMessage = new KeoghsRequestScoreMessage();
                        scoreMessage.setScoreMessageHeading(message.getMessageHeading());
                        for (String messageDetail : message.getMessageDetail().getStrings()) {
                            sb.append(messageDetail).append("\n");
                        }
                        scoreMessage.setScoreMessageDetail(sb.toString());
                        originalRequest.addKeoghsRequestScoreMessage(scoreMessage);
                        sb.setLength(0);
                    }
                    break;
            }
            LOG.debug("Saving request and claim for '{}'....", originalRequest.getClientBatchReference());
            originalRequest.setLastModifiedDate(new Date());
            keoghsRequestService.saveKeoghsRequest(originalRequest);
            claimService.save(claim);
        }
    }

    private com.keoghs.ADAPublicServices.Claim getKeoghsClaim(Claim claim) {
        com.keoghs.ADAPublicServices.Claim keoghsClaim = new com.keoghs.ADAPublicServices.Claim();

        try {
            // Map the CHOX Claim to a Keoghs Claim
            keoghsClaim.setOperation(Operation.NORMAL); // not needed
            keoghsClaim.setClaimType(ClaimType.MOTOR);
            keoghsClaim.setClaimNumber(claim.getId() != null ? claim.getId().toString()
                    : claim.getChorganisation().getId().toString().concat("_" + claim.getChoReference().replaceAll("\\s+", "")));

//            keoghsClaim.setExternalServices(null); // not needed
            GregorianCalendar gregory = new GregorianCalendar();
            if (claim.getIncident().getDate() != null) {
                gregory.setTime(claim.getIncident().getDate());
                keoghsClaim.setIncidentDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory));
            }
            Policy policy = new Policy();
//            policy.setPolicyType(PolicyType.PERSONAL_MOTOR);
//            policy.setPolicyType(PolicyType.COMMERCIAL_MOTOR);
            policy.setInsurer(claim.getThirdParty().getInsurer().getName());
            policy.setPolicyNumber(claim.getThirdParty().getPolicyNumber());
            policy.setTradingName(claim.getThirdParty().getInsurerBrand());
            keoghsClaim.setPolicy(policy);

            ClaimInfo claimInfo = new ClaimInfo();
//            claimInfo.setAmbulanceAttended();
//            claimInfo.setBypassFraud();
//            claimInfo.setClaimCode(rating);
//            gregory.setTime(claim.getCreatedDate());
            if (claim.getGtaNoticeDate() != null) {
                gregory.setTime(claim.getGtaNoticeDate());
                claimInfo.setClaimNotificationDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory));
            }
            claimInfo.setClaimStatus(ClaimStatus.OPEN);
            claimInfo.setIncidentCircumstances(claim.getIncident().getIncidentDescription());
            claimInfo.setIncidentLocation(claim.getIncident().getLocation());
//            claimInfo.setPaymentsToDate();
            claimInfo.setPoliceAttended(claim.getIncident().getIsPoliceInvolved());
//            claimInfo.setPoliceForce();
//            claimInfo.setPoliceReference();
//            claimInfo.setReserve();
            claimInfo.setSourceClaimStatus(claim.getStatus());
            if (claim.getInvoice() != null) {
                claimInfo.setTotalClaimCost(claim.getInvoice().getFullTotalToPay());
                claimInfo.setTotalClaimCostLessExcess(claim.getInvoice().getFullTotalToPay());
            }
            keoghsClaim.setMotorClaimInfo(claimInfo);

            // Add Vehicles: Insured and Third Party
            Vehicle insuredVehicle = new Vehicle();
            insuredVehicle.setVehicleInvolvementGroup(VehicleInvolvementGroup.INSURED_VEHICLE);
            insuredVehicle.setOperation(Operation.NORMAL);
//            insuredVehicle.setCategoryOfLoss(VehicleCategoryOfLoss.CAT_A);
//            insuredVehicle.setColour();
//            insuredVehicle.setDamageDescription();
//            insuredVehicle.setEngineCapacity();
//            insuredVehicle.setFuel();
            insuredVehicle.setMake(claim.getThirdParty().getVehicleManufacturer());
            insuredVehicle.setModel(claim.getThirdParty().getVehicleModel());
            insuredVehicle.setVehicleRegistration(claim.getThirdParty().getVehicleRegistration());
//            insuredVehicle.setTransmission();
//            insuredVehicle.setVIN();
            insuredVehicle.setVehicleType(getVehicleTypeFromClass(claim.getThirdParty().getVehicleClass() == null ? "" : claim.getThirdParty().getVehicleClass().getName()));

            Person insuredDriver = new Person();
            if (claim.getThirdParty().getEmail() != null && claim.getThirdParty().getEmail().contains("@")) {
                insuredDriver.setEmailAddress(claim.getThirdParty().getEmail());
            }
            insuredDriver.setFirstName(claim.getThirdParty().getFirstName());
            insuredDriver.setLastName(claim.getThirdParty().getLastName());
            insuredDriver.setLandlineTelephone(claim.getThirdParty().getTelephoneEvening());
            insuredDriver.setMobileTelephone(claim.getThirdParty().getTelephoneDay());
            insuredDriver.setSalutation(getSalutationFromString(claim.getThirdParty().getTitle()));
            insuredDriver.setPartyType(PartyType.INSURED);
            insuredDriver.setSubPartyType(SubPartyType.DRIVER);

            Address insuredDriverAddress = new Address();
            insuredDriverAddress.setAddressLinkType(AddressStatus.CURRENT_ADDRESS);
            insuredDriverAddress.setOperation(Operation.NORMAL);
//            insuredDriverAddress.setBuildingNumber();
            insuredDriverAddress.setBuilding(claim.getThirdParty().getAddress1());
//            insuredDriverAddress.setSubBuilding();
            insuredDriverAddress.setStreet(claim.getThirdParty().getAddress2());
            insuredDriverAddress.setTown(claim.getThirdParty().getAddress3());
            insuredDriverAddress.setLocality(claim.getThirdParty().getAddress4());
            insuredDriverAddress.setCounty(claim.getThirdParty().getAddress5());
            insuredDriverAddress.setPostCode(claim.getThirdParty().getPostcode());

            ArrayOfAddress insuredDriverAddressArray = new ArrayOfAddress();
            insuredDriverAddressArray.getAddresses().add(insuredDriverAddress);
            insuredDriver.setAddresses(insuredDriverAddressArray);
            ArrayOfPerson personArray = new ArrayOfPerson();
            personArray.getPersons().add(insuredDriver);

            insuredVehicle.setPeople(personArray);

            Vehicle thirdPartyVehicle = new Vehicle();
            thirdPartyVehicle.setVehicleInvolvementGroup(VehicleInvolvementGroup.THIRD_PARTY_VEHICLE);
            thirdPartyVehicle.setOperation(Operation.NORMAL);
//            thirdPartyVehicle.setCategoryOfLoss(VehicleCategoryOfLoss.CAT_A);
//            thirdPartyVehicle.setColour();
            thirdPartyVehicle.setDamageDescription(claim.getCustomer().getDamage());
            thirdPartyVehicle.setEngineCapacity(claim.getCustomer().getHpiVehicleCapacity());
            thirdPartyVehicle.setFuel(getVehicleFuelType(claim.getCustomer().getHpiVehicleTransmission()));
            thirdPartyVehicle.setMake(claim.getCustomer().getHpiVehicleManufacturer());
            thirdPartyVehicle.setModel(claim.getCustomer().getHpiVehicleModel());
            thirdPartyVehicle.setVehicleRegistration(claim.getCustomer().getVehicleRegistration());
            thirdPartyVehicle.setTransmission(getVehicleTransmission(claim.getCustomer().getHpiVehicleTransmission(),
                    claim.getCustomer().getVehicleClass() == null ? "" : claim.getCustomer().getVehicleClass().getName()));
//            thirdPartyVehicle.setVIN();
            thirdPartyVehicle.setVehicleType(getVehicleTypeFromClass(claim.getCustomer().getVehicleClass() == null ? "" : claim.getCustomer().getVehicleClass().getName()));

            Person thirdPartyDriver = new Person();
            if (claim.getCustomer().getEmail() != null && claim.getCustomer().getEmail().contains("@")) {
                thirdPartyDriver.setEmailAddress(claim.getCustomer().getEmail());
            }
            thirdPartyDriver.setFirstName(claim.getCustomer().getFirstName());
            thirdPartyDriver.setLastName(claim.getCustomer().getLastName());
            thirdPartyDriver.setLandlineTelephone(claim.getCustomer().getTelephoneEvening());
            thirdPartyDriver.setMobileTelephone(claim.getCustomer().getTelephoneDay());
            thirdPartyDriver.setSalutation(getSalutationFromString(claim.getCustomer().getTitle()));
            thirdPartyDriver.setOccupation(claim.getCustomer().getOccupation());
            thirdPartyDriver.setPartyType(PartyType.THIRD_PARTY);
            thirdPartyDriver.setSubPartyType(SubPartyType.DRIVER);

            Address thirdPartyDriverAddress = new Address();
            thirdPartyDriverAddress.setAddressLinkType(AddressStatus.CURRENT_ADDRESS);
            thirdPartyDriverAddress.setOperation(Operation.NORMAL);
//            thirdPartyDriverAddress.setBuildingNumber();
            thirdPartyDriverAddress.setBuilding(claim.getCustomer().getAddress1());
//            thirdPartyDriverAddress.setSubBuilding();
            thirdPartyDriverAddress.setStreet(claim.getCustomer().getAddress2());
            thirdPartyDriverAddress.setTown(claim.getCustomer().getAddress3());
            thirdPartyDriverAddress.setLocality(claim.getCustomer().getAddress4());
            thirdPartyDriverAddress.setCounty(claim.getCustomer().getAddress5());
            thirdPartyDriverAddress.setPostCode(claim.getCustomer().getPostcode());

            ArrayOfAddress thirdPartyDriverAddressArray = new ArrayOfAddress();
            thirdPartyDriverAddressArray.getAddresses().add(thirdPartyDriverAddress);
            thirdPartyDriver.setAddresses(thirdPartyDriverAddressArray);
            ArrayOfPerson personArray2 = new ArrayOfPerson();
            personArray2.getPersons().add(thirdPartyDriver);

            thirdPartyVehicle.setPeople(personArray2);

            ArrayOfVehicle vehicleArray = new ArrayOfVehicle();
            vehicleArray.getVehicles().add(insuredVehicle);
            vehicleArray.getVehicles().add(thirdPartyVehicle);
            keoghsClaim.setVehicles(vehicleArray);

            // Add Organisations
            ArrayOfOrganisation orgArray = new ArrayOfOrganisation();

            // Add CHO
            Organisation choOrg = new Organisation();
            choOrg.setOrganisationType(OrganisationType.CREDIT_HIRE);
            if (claim.getChorganisation().getName().startsWith("Manual")) {
                choOrg.setOrganisationName(claim.getChorganisation().getName().substring(7));
            } else {
                choOrg.setOrganisationName(claim.getChorganisation().getName());
                // Only set address for non-manula CHOs
                Address choAddress = new Address();
                choAddress.setAddressLinkType(AddressStatus.CURRENT_ADDRESS);
                choAddress.setOperation(Operation.NORMAL);
//                choAddress.setBuildingNumber();
                choAddress.setBuilding(claim.getChorganisation().getAddress1());
//                choAddress.setSubBuilding();
                choAddress.setStreet(claim.getChorganisation().getAddress2());
                choAddress.setTown(claim.getChorganisation().getAddress3());
//                choAddress.setLocality();
                choAddress.setCounty(claim.getChorganisation().getAddress4());
                choAddress.setPostCode(claim.getChorganisation().getPostcode());

                ArrayOfAddress choAddressArray = new ArrayOfAddress();
                choAddressArray.getAddresses().add(choAddress);
                choOrg.setAddresses(choAddressArray);
            }

            choOrg.setTelephone1(claim.getChorganisation().getPhone());
            choOrg.setVatNumber(claim.getChorganisation().getVatNo());
            choOrg.setRegisteredNumber(claim.getChorganisation().getCompanyNo());

            if (claim.getVehicleHire() != null) {
                OrganisationVehicle hireVehicle = new OrganisationVehicle();
                hireVehicle.setVehicleInvolvementGroup(VehicleInvolvementGroup.THIRD_PARTY_HIRE_VEHICLE);
                hireVehicle.setEngineCapacity(claim.getVehicleHire().getHpiVehicleCapacity());
                hireVehicle.setFuel(getVehicleFuelType(claim.getVehicleHire().getHpiVehicleTransmission()));
                if (claim.getVehicleHire().getHireEnd() != null) {
                    gregory.setTime(claim.getVehicleHire().getHireEnd());
                    hireVehicle.setHireEndDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory));
                }
                if (claim.getVehicleHire().getHireStart() != null) {
                    gregory.setTime(claim.getVehicleHire().getHireStart());
                    hireVehicle.setHireStartDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory));
                }
                hireVehicle.setMake(claim.getVehicleHire().getHpiVehicleManufacturer());
                hireVehicle.setModel(claim.getVehicleHire().getHpiVehicleModel());
                if (claim.getVehicleHire().getVehicleClass() != null) {
                    hireVehicle.setTransmission(getVehicleTransmission(claim.getVehicleHire().getHpiVehicleTransmission(), claim.getVehicleHire().getVehicleClass().getName()));
                    hireVehicle.setVehicleType(getVehicleTypeFromClass(claim.getVehicleHire().getVehicleClass().getName()));
                } else {
                    hireVehicle.setVehicleType(VehicleType.UNKNOWN);
                }
                hireVehicle.setVehicleRegistration(claim.getVehicleHire().getVehicleRegistration());
                ArrayOfOrganisationVehicle choVehicles = new ArrayOfOrganisationVehicle();
                choVehicles.getOrganisationVehicles().add(hireVehicle);

                choOrg.setVehicles(choVehicles);
            }

            orgArray.getOrganisations().add(choOrg);

            // Add Insurer
            Organisation insOrg = new Organisation();
            insOrg.setOrganisationType(OrganisationType.INSURER);
            insOrg.setOrganisationName(claim.getInsurer().getName());
            insOrg.setTelephone1(claim.getInsurer().getPhone());
            insOrg.setVatNumber(claim.getInsurer().getVatNo());
            insOrg.setRegisteredNumber(claim.getInsurer().getCompanyNo());

            Address insAddress = new Address();
            insAddress.setAddressLinkType(AddressStatus.CURRENT_ADDRESS);
            insAddress.setOperation(Operation.NORMAL);
//                insAddress.setBuildingNumber();
            insAddress.setBuilding(claim.getInsurer().getAddress1());
//                insAddress.setSubBuilding();
            insAddress.setStreet(claim.getInsurer().getAddress2());
            insAddress.setTown(claim.getInsurer().getAddress3());
//                insAddress.setLocality();
            insAddress.setCounty(claim.getInsurer().getAddress4());
            insAddress.setPostCode(claim.getInsurer().getPostcode());

            ArrayOfAddress insAddressArray = new ArrayOfAddress();
            insAddressArray.getAddresses().add(insAddress);
            insOrg.setAddresses(insAddressArray);

            orgArray.getOrganisations().add(insOrg);

            // Add Repairer (if available)
            if (claim.getHireMonitoringDetail() != null && claim.getHireMonitoringDetail().getNameOfRepairer() != null
                    && !claim.getHireMonitoringDetail().getNameOfRepairer().isEmpty()) {
                Organisation repairerOrg = new Organisation();
                repairerOrg.setOrganisationType(OrganisationType.REPAIRER);
                repairerOrg.setOrganisationName(claim.getHireMonitoringDetail().getNameOfRepairer());
                orgArray.getOrganisations().add(repairerOrg);
            }

            // Add Vehicle Engineer (if available)
            if (claim.getEngineerReport() != null) {
                Organisation engineerOrg = new Organisation();
                engineerOrg.setOrganisationType(OrganisationType.VEHICLE_ENGINEER);
                engineerOrg.setOrganisationName(claim.getEngineerReport().getCompany());
                if (claim.getEngineerReport().getEmail() != null && claim.getEngineerReport().getEmail().contains("@")) {
                    engineerOrg.setEmail(claim.getEngineerReport().getEmail());
                }
                engineerOrg.setTelephone1(claim.getEngineerReport().getTelephone());

                Address engineerAddress = new Address();
//                    engineerAddress.setBuilding(claim.getEngineerReport().getAddress1());
//                    engineerAddress.setStreet(claim.getEngineerReport().getAddress2());
//                    engineerAddress.setTown(claim.getEngineerReport().getAddress3());
//                    engineerAddress.setCounty(claim.getEngineerReport().getAddress5());
                engineerAddress.setPostCode(claim.getEngineerReport().getPostcode());

                ArrayOfAddress engineerAddressArray = new ArrayOfAddress();
                engineerAddressArray.getAddresses().add(engineerAddress);
                engineerOrg.setAddresses(engineerAddressArray);
                orgArray.getOrganisations().add(engineerOrg);
            }

            // Add Injury Solicitor (if available)
            if (claim.getIncident().getInjury() != null && claim.getIncident().getInjury().getSolicitor() != null) {
                Organisation solicitorOrg = new Organisation();
                solicitorOrg.setOrganisationType(OrganisationType.SOLICITOR);
                solicitorOrg.setOrganisationName(claim.getIncident().getInjury().getSolicitor().getName());

                if (claim.getIncident().getInjury().getSolicitor().getEmail() != null && claim.getIncident().getInjury().getSolicitor().getEmail().contains("@")) {
                    solicitorOrg.setEmail(claim.getIncident().getInjury().getSolicitor().getEmail());
                }
                solicitorOrg.setTelephone1(claim.getIncident().getInjury().getSolicitor().getTelephone());

                Address solicitorAddress = new Address();
//                    solicitorAddress.setBuilding(claim.getIncident().getInjury().getSolicitor().getAddress1());
//                    solicitorAddress.setStreet(claim.getIncident().getInjury().getSolicitor().getAddress2());
//                    solicitorAddress.setTown(claim.getIncident().getInjury().getSolicitor().getAddress3());
//                    solicitorAddress.setCounty(claim.getIncident().getInjury().getSolicitor().getAddress5());
                solicitorAddress.setPostCode(claim.getIncident().getInjury().getSolicitor().getPostcode());

                ArrayOfAddress solicitorAddressArray = new ArrayOfAddress();
                solicitorAddressArray.getAddresses().add(solicitorAddress);
                solicitorOrg.setAddresses(solicitorAddressArray);

                orgArray.getOrganisations().add(solicitorOrg);
            }

            keoghsClaim.setOrganisations(orgArray);
        } catch (Exception ex) {
            LOG.error("Exception thrown setting-up Keoghs claimn: {}\n", ex.getMessage(), ex);
        }
        return keoghsClaim;
    }

    private VehicleType getVehicleTypeFromClass(String vehicleClass) {
        if (vehicleClass == null) {
            return VehicleType.UNKNOWN;
        }

        if (vehicleClass.startsWith("PV") || vehicleClass.startsWith("CV") || vehicleClass.startsWith("RV")) {
            return VehicleType.VAN;
        } else if (vehicleClass.startsWith("T") || vehicleClass.startsWith("PT")) {
            return VehicleType.TAXI;
        } else if (vehicleClass.startsWith("S") || vehicleClass.startsWith("M") || vehicleClass.startsWith("F")
                || vehicleClass.startsWith("P")) {
            return VehicleType.CAR;
        } else if (vehicleClass.startsWith("CM")) {
            return VehicleType.MINIBUS;
        } else if (vehicleClass.startsWith("B")) {
            return VehicleType.MOTORCYCLE;
        } else if (vehicleClass.startsWith("CP")) {
            return VehicleType.PICKUP;
        }

        return VehicleType.UNKNOWN;
    }

    private Salutation getSalutationFromString(String t) {
        if (t == null) {
            return Salutation.UNKNOWN;
        }
        
        String title = t.toLowerCase();
        if (title.startsWith("mrs")) {
            return Salutation.MRS;
        } else if (title.startsWith("miss")) {
            return Salutation.MISS;
        } else if (title.startsWith("baroness")) {
            return Salutation.BARONESS;
        } else if (title.startsWith("dr")) {
            return Salutation.DR;
        } else if (title.startsWith("lord")) {
            return Salutation.LORD;
        } else if (title.startsWith("master")) {
            return Salutation.MASTER;
        } else if (title.startsWith("mr")) {
            return Salutation.MR;
        } else if (title.startsWith("ms")) {
            return Salutation.MS;
        } else if (title.startsWith("prof")) {
            return Salutation.PROFESSOR;
        } else if (title.startsWith("rev")) {
            return Salutation.REVEREND;
        } else if (title.startsWith("right")) {
            return Salutation.RIGHT_HONOURABLE;
        } else if (title.startsWith("sir")) {
            return Salutation.SIR;
        }

        return Salutation.UNKNOWN;
    }

    private VehicleTransmission getVehicleTransmission(String hpiTransmission, String vehicleClassName) {
        if (hpiTransmission == null) {
            return VehicleTransmission.UNKNOWN;
        }

        if (hpiTransmission.contains("Manual")) {
            return VehicleTransmission.MANUAL;
        } else if (hpiTransmission.contains("Auto") || vehicleClassName.endsWith("A")) {
            return VehicleTransmission.AUTOMATIC;
        }
        return VehicleTransmission.UNKNOWN;
    }

    private VehicleFuelType getVehicleFuelType(String hpiTransmission) {
        if (hpiTransmission == null) {
            return VehicleFuelType.UNKNOWN;
        }

        if (hpiTransmission.contains("Diesel")) {
            return VehicleFuelType.DIESEL;
        } else if (hpiTransmission.contains("Petrol/G")) {
            return VehicleFuelType.LPG;
        } else if (hpiTransmission.contains("Petrol/E")) {
            return VehicleFuelType.HYBRID;
        } else if (hpiTransmission.contains("Petrol")) {
            return VehicleFuelType.PETROL;
        } else if (hpiTransmission.contains("Electric")) {
            return VehicleFuelType.ELECTRIC;
        }
        return VehicleFuelType.UNKNOWN;
    }
}
