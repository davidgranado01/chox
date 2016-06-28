package idas.chox.core.services;

import idas.chox.core.model.Claim;
import idas.chox.core.model.KeoghsRequest;
import java.util.List;

/**
 *
 * @author John
 */
public interface KeoghsRequestService {
    KeoghsRequest getKeoghsRequest(int keoghsRequestId);

    void saveKeoghsRequest(KeoghsRequest keoghsRequest);

    List<KeoghsRequest> getKeoghsRequestByClaim(Claim claim);
    List<KeoghsRequest> getQueuedRequests();
    List<KeoghsRequest> getPendingRequests();
    KeoghsRequest getKeoghsRequestByClientBatchReference(String clientBatchReference);

}
