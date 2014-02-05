package idas.chox.data.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Task;
import idas.chox.data.services.EventService;

/**
 *
 * @author John
 */
public enum ChoxEvent {
    TASK_CREATED_EVENT                      (102, "TaskCreatedEvent") {
        @Override
        public void build(EventService generator, Claim claim, Task task) throws Exception {
            LOG.debug("Building TaskCreatedEvent");
            boolean insurerOnly = task.getInsurer() && (task.getVisibility() ==1 || task.getVisibility() ==2);
            boolean choOnly = !task.getInsurer() && (task.getVisibility() ==1 || task.getVisibility() ==2);
            generator.startEvent(claim, this.getName(), this.getEventId(), insurerOnly, choOnly);
            generator.addParameter("taskDescription", task.getDescription());
            generator.addParameter("taskDueDate", task.getDueDate());
            generator.addParameter("taskType", task.getType());
            generator.addParameter("taskVisibility", task.getVisibility()==1 ? "Private" : task.getVisibility()==2 ? "Internal" : "External");
            if (task.getVisibilityRole() != null) {
                generator.addParameter("taskRole", task.getVisibilityRole().toString());
            } else {
                generator.addParameter("taskRole", (String)null);
            }
            generator.completeEvent(claim);
        }
   },

    TASK_COMPLETED_EVENT                    (103, "TaskCompletedEvent") {
        @Override
        public void build(EventService generator, Claim claim, Task task) throws Exception {
            LOG.debug("Building TaskCompletedEvent");
            boolean insurerOnly = task.getInsurer() && (task.getVisibility() ==1 || task.getVisibility() ==2);
            boolean choOnly = !task.getInsurer() && (task.getVisibility() ==1 || task.getVisibility() ==2);
            generator.startEvent(claim, this.getName(), this.getEventId(), insurerOnly, choOnly);
            generator.addParameter("taskDescription", task.getDescription());
            generator.addParameter("taskDueDate", task.getDueDate());
            generator.addParameter("taskType", task.getType());
            generator.addParameter("taskCompletedBy", task.getCompletedBy().getFullName());
            generator.addParameter("taskCompletedDate", task.getCompletedDate());
            generator.addParameter("taskVisibility", task.getVisibility()==1 ? "Private" : task.getVisibility()==2 ? "Internal" : "External");
            if (task.getVisibilityRole() != null) {
                generator.addParameter("taskRole", task.getVisibilityRole().toString());
            } else {
                generator.addParameter("taskRole", (String)null);
            }
            generator.completeEvent(claim);
        }
    },
    CHO_REFERENCE_NO_UPDATED_EVENT          (109, "ChoReferenceNumberUpdatedEvent") {
        @Override
        public void build(EventService generator, Claim claim, String oldReference)  throws Exception {
            LOG.debug("Building ChoReferenceNumberUpdatedEvent");
            generator.startEvent(claim, this.getName(), this.getEventId());
            generator.addParameter("oldReference", oldReference);
            generator.completeEvent(claim);
        }
    };
;
    
    private static final Logger LOG = LoggerFactory.getLogger(ChoxEvent.class);
    private final int eventId;
    private final String name;

    public int getEventId() {
        return eventId;
    }

    public String getName() {
        return name;
    }

    ChoxEvent(int eventId, String name) {
        this.eventId = eventId;
        this.name = name;
    }
    
    @Override
    public String toString() {
        return name;
    }

    public void build(EventService generator, Claim claim) throws Exception {
        LOG.warn("Building generic non-activity based event...");
        generator.startEvent(claim, new StringBuilder().append(this.getName()).append("[*]").toString(), this.getEventId());
        generator.completeEvent(claim);
    }

    public void build(EventService generator, Claim claim, Task task) throws Exception {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void build(EventService generator, Claim claim, String attribute) throws Exception {
        throw new UnsupportedOperationException("Not implemented yet");
    }


}
